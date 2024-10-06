package impl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import spec.ImportExport;
import spec.Prostorija;
import spec.Raspored;
import spec.Termin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import impl.ImportExportCSV;

public class OperacijeRaspored extends Raspored {

    public List<Termin> listaTermina;
    private HashMap<Prostorija,ArrayList<Termin>> mapaSlobodnihTermina;
    private List<Prostorija> listaProstorija;
    private List<Termin> listaSlobodnih;

    @Override
    public void initRaspored() {
            listaTermina = new ArrayList<>();
            mapaSlobodnihTermina = new HashMap<>();
            listaSlobodnih = new ArrayList<>();
            listaProstorija = new ArrayList<>();
    }

    //@Override
    public void addProstorija(Prostorija prostorija) {
         listaProstorija.add(prostorija);
    }

    @Override
    public boolean importProstorija(String filePath,String configPath)throws IOException{
        importujProstorije(filePath,configPath);
        return true;
    }

    public void importujProstorije(String filePath, String configPath) throws IOException {
        List<Config> columnMappings = readConfig(configPath);
        Map<Integer, String> mappings = new HashMap<>();
        for(Config configMapping : columnMappings) {
            mappings.put(configMapping.getIndex(), configMapping.getOriginal());
        }

        FileReader fileReader = new FileReader(filePath);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);

        for (CSVRecord record : parser) {
            Prostorija prostorija = new Prostorija();

            for (Config entry : columnMappings) {
                int columnIndex = entry.getIndex();

                String columnName = entry.getCustom();

                switch (mappings.get(columnIndex)) {
                    case "naziv":
                      prostorija.setNaziv(record.get(columnIndex));
                      break;
                    case "osobine":
                        prostorija.getOsobine().put(columnName, record.get(columnIndex));
                        break;
                }
            }
            listaProstorija.add(prostorija);
        }
    }

    private static List<Config> readConfig(String filePath) throws FileNotFoundException {
        List<Config> mappings = new ArrayList<>();
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(" ", 3);

            mappings.add(new Config(Integer.valueOf(splitLine[0]), splitLine[1], splitLine[2]));
        }
        scanner.close();
        return mappings;
    }

    @Override
    public boolean importSlobodnihTermina(String s,String s1,String s2) throws FileNotFoundException {
        importujSlobodne(s,s1,s2);
        return true;
    }


    public void importujSlobodne(String file,String s1,String s2) throws FileNotFoundException{
        String[] splituj = s1.split("-");
        LocalDate datumPocetka = LocalDate.parse(splituj[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate datumZavrsetka = LocalDate.parse(splituj[1],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<LocalDate> izuzetiLista = new ArrayList<>();
        if(s2.contains(",")) {
            String[] sp = s2.split(",");
            for (String dateString : sp) {
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                izuzetiLista.add(date);
            }
        }else {
            LocalDate datummm = LocalDate.parse(s2,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            izuzetiLista.add(datummm);
        }
        File fileSlobodni = new File(file);
        Scanner scanner = new Scanner(fileSlobodni);
        System.out.println(datumPocetka);
        System.out.println(datumZavrsetka);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split("-",2);
            LocalTime pocetak = LocalTime.parse(splitLine[0]);
            LocalTime kraj = LocalTime.parse(splitLine[1]);
            for (LocalDate current = datumPocetka ; current.isBefore(datumZavrsetka) ; current = current.plusDays(1)){
                if(!(izuzetiLista.contains(current))) {
                    for (Prostorija prostorija : listaProstorija) {
                        Termin termin = new Termin(current, pocetak, kraj, prostorija);
                            for (Termin termin1 : listaTermina) {
                            if ((!(termin1.getDatum().equals(termin.getDatum()))
                                    && !(termin1.getStart().equals(termin.getStart())
                                    && !(termin1.getKraj().equals(termin.getKraj()))
                                    && !(termin1.getUcionica().getNaziv().equals(termin.getUcionica().getNaziv()))))) {
                                listaSlobodnih.add(termin);
                                break;
                            }
                        }
                    }
                }
            }
        }
        scanner.close();
    }
    @Override
    public void fillSlobodniTermini() {
         for(Prostorija prostorija : listaProstorija){
             ArrayList<Termin> temp = new ArrayList<>();
             for (Termin termin : listaSlobodnih){
                 if(prostorija.getNaziv().equals(termin.getUcionica().getNaziv())){
                     temp.add(termin);
                 }
             }
             mapaSlobodnihTermina.put(prostorija,temp);
         }
    }

    @Override
    public void moveTermin(Termin termin1,Termin termin2) {
        ListIterator<Termin> ite = listaTermina.listIterator();
        HashMap<String,String> nova = new HashMap<>();
        boolean obrisi = false;
        while(ite.hasNext()){
            Termin termin = ite.next();
            if ( (termin.getDatum().equals(termin1.getDatum()))
                   && (termin.getStart().equals(termin1.getStart()))
                    && (termin.getKraj().equals(termin1.getKraj()))
                    && (termin.getUcionica().getNaziv().equals(termin1.getUcionica().getNaziv()))){
                obrisi = true;
                nova.putAll(termin.getOstalo());
                termin2.getOstalo().putAll(nova);
                for(Prostorija prostorija : listaProstorija){
                    if (prostorija.getNaziv().equals(termin.getUcionica().getNaziv())){
                        Prostorija prostor = new Prostorija(prostorija.getNaziv(),prostorija.getOsobine());
                        Termin zalistuslobodnih = new Termin(termin.getDatum(),termin.getStart(),termin.getKraj(),prostor);
                        listaSlobodnih.add(zalistuslobodnih);
                    }
                }
                ite.remove();
                break;
            }
        }
        if(obrisi){
            ite.add(termin2);
            ListIterator<Termin> iter = listaSlobodnih.listIterator();
            boolean nasao = false;
            while(iter.hasNext()){
                Termin terminbris = iter.next();
                if( (terminbris.getDatum().equals(termin2.getDatum()))
                        && (terminbris.getStart().equals(termin2.getStart()))
                &&(terminbris.getKraj().equals(termin2.getKraj()))
                && (terminbris.getUcionica().getNaziv().equals(termin2.getUcionica().getNaziv()))){
                    nasao = true;
                    break;
                }
            }
            if(nasao){
                iter.remove();
            }
        }
    }

    @Override
    public void refreshList() {
        ListIterator<Termin> iteite = listaSlobodnih.listIterator();
        while (iteite.hasNext()) {
            Termin tete = iteite.next();
            ListIterator<Termin> ite = listaTermina.listIterator();
            while (ite.hasNext()) {
                Termin te = ite.next();
                if (tete.getDatum().equals(te.getDatum())
                        && tete.getStart().equals(te.getStart())
                        && tete.getUcionica().getNaziv().equals(te.getUcionica().getNaziv())
                        && tete.getKraj().equals(te.getKraj())) {
                    iteite.remove();
                }
            }
        }
    }



    @Override
    public void addTermin(Termin termin) {

        ListIterator<Termin> iterator = listaTermina.listIterator();
        boolean dodaj = true;
        while(iterator.hasNext()) {
            Termin termin1 = iterator.next();
            if ( (termin1.getDatum().equals(termin.getDatum()))
                  &&  (termin1.getStart().equals(termin.getStart()))
                    && (termin1.getKraj().equals(termin.getKraj()))
                    && (termin1.getUcionica().getNaziv().equals(termin.getUcionica().getNaziv()))) {
                dodaj = false;
                break;
            }
        }
            if(dodaj) {
                iterator.add(termin);
                ListIterator<Termin> iterator1 = listaSlobodnih.listIterator();
                boolean nadjen = false;
                while(iterator1.hasNext()){
                    Termin termin2 = iterator1.next();
                    if (  (termin2.getDatum().equals(termin.getDatum()))
                           && (termin2.getStart().equals(termin.getStart()))
                            && (termin2.getKraj().equals(termin.getKraj())
                            && (termin2.getUcionica().getNaziv().equals(termin.getUcionica().getNaziv())))) {
                        nadjen=true;
                        break;
                    }
                }
                if(nadjen){
                    iterator1.remove();
                }
             }
        }



    @Override
    public void deleteTermin(Termin termin) {
        Iterator<Termin> iterator = listaTermina.iterator();
        while(iterator.hasNext()){
            Termin termin1 = iterator.next();
            if( termin1.getDatum().equals(termin.getDatum())
                   && termin1.getStart().equals(termin.getStart())
        && termin1.getKraj().equals(termin.getKraj())
        && termin1.getUcionica().getNaziv().equals(termin.getUcionica().getNaziv())){
                for (Prostorija prostorija : listaProstorija){
                    if(prostorija.getNaziv().equals(termin.getUcionica().getNaziv())){
                        termin.getUcionica().getOsobine().putAll(prostorija.getOsobine());
                    }
                }
                listaSlobodnih.add(termin);
                iterator.remove();
            }
        }
        System.out.println(listaTermina);
    }

    //10:00,12:00,raf01

    @Override
    public boolean checkTermin(Termin termin) {

        LocalTime pocetak = termin.getStart();
        LocalTime kraj = termin.getKraj();
        for(Termin temp : listaTermina){
            if(pocetak.isAfter(temp.getStart()) &&
                    kraj.isBefore(temp.getKraj())
                    || pocetak.isBefore(temp.getStart())
                    && kraj.isAfter(temp.getStart())&& kraj.isBefore(temp.getKraj())
                    || pocetak.isAfter(temp.getStart()) &&
                    pocetak.isBefore(temp.getKraj()) && kraj.isAfter(temp.getKraj())
                    || pocetak.isBefore(temp.getStart()) && kraj.isAfter(temp.getKraj())
                    || pocetak == temp.getStart() || kraj == temp.getKraj()){
                return false;
            }
        }
        return true;
    }

    public HashMap<Prostorija, ArrayList<Termin>> getMapaSlobodnihTermina() {
        return mapaSlobodnihTermina;
    }

    public void setMapaSlobodnihTermina(HashMap<Prostorija, ArrayList<Termin>> mapaSlobodnihTermina) {
        this.mapaSlobodnihTermina = mapaSlobodnihTermina;
    }

    public List<Prostorija> getListaProstorija() {
        return listaProstorija;
    }

    public void setListaProstorija(List<Prostorija> listaProstorija) {
        this.listaProstorija = listaProstorija;
    }

    public List<Termin> getListaTermina() {
        return listaTermina;
    }

    public void setListaTermina(List<Termin> listaTermina) {
        this.listaTermina = listaTermina;
    }

    public List<Termin> getListaSlobodnih() {
        return listaSlobodnih;
    }

    public void setListaSlobodnih(List<Termin> listaSlobodnih) {
        this.listaSlobodnih = listaSlobodnih;
    }


}
