package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import spec.Termin;
import spec.Raspored;
import spec.Prostorija;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OperacijeRaspored extends Raspored {


    private List<Termin> listaTermina; //lista svih ikada termina

    private HashMap<String, ArrayList<Termin>> mapaSlobodnihTermina;
    private List<Prostorija> listaProstorija;
    private List<Termin> listaSlobodnih;

    private List<LocalDate> listaDatuma;
    private List<LocalDate> izuzetiDani;
    private HashMap<LocalDate, List<Termin>> mapaRasporeda;

    private List<LocalDateTime> nova;
    private HashMap<LocalDate,List<Termin>> mapa;

    public OperacijeRaspored(){
        initRaspored();
    }


    @Override
    public void refreshList() {

    }

    @Override
    public void initRaspored() {
        listaTermina = new ArrayList<>();
        listaSlobodnih = new ArrayList<>();
        mapaSlobodnihTermina = new HashMap<>();
        listaProstorija = new ArrayList<>();
        listaDatuma = new ArrayList<>();
        izuzetiDani = new ArrayList<>();
        mapaRasporeda = new HashMap<>();
        nova = new ArrayList<>();
        mapa = new HashMap<>();
        try {
            importDatuma("C:\\Users\\Krivi\\Desktop\\projekat\\Imp2\\src\\main\\resources\\datumi.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            importProstorija("C:\\Users\\Krivi\\Desktop\\projekat\\Imp2\\src\\main\\resources\\ucionice.csv","C:\\Users\\Krivi\\Desktop\\projekat\\Imp2\\src\\main\\resources\\configP.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            importujSlobodne("C:\\Users\\Krivi\\Desktop\\projekat\\Imp2\\src\\main\\resources\\termini.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //napravi();
        fillSlobodniTermini();
    }

    public void napravi(){
       for (LocalDate localDate : listaDatuma){
           mapaRasporeda.put(localDate,listaSlobodnih);
       }
        System.out.println(mapaRasporeda);
    }

    public void importDatuma(String s) throws  IOException{


        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line="";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while ((line = br.readLine()) != null){
                LocalDate date = LocalDate.parse(line,formatter);
                listaDatuma.add(date);
            }
        }
    }

    @Override
    public void addProstorija(Prostorija prostorija) {
         if(!listaProstorija.contains(prostorija)) {
             listaProstorija.add(prostorija);
         }
    }



    @Override
    public void addTermin(Termin termin) {
        //TO:DO Moram da dodam da se datum dodaje kao ponavljanje od jednog datuma do drugog i to par puta

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        LocalDate pocetak = termin.getDatumVremePocetak().toLocalDate();
        LocalDate kraj = termin.getDatumVremeKraj().toLocalDate();
        System.out.println(pocetak);
        System.out.println(kraj);

        if (mapaSlobodnihTermina.containsKey(termin.getUcionica().getNaziv())) {
            for (LocalDate currentDate = pocetak; !currentDate.isAfter(kraj); currentDate = currentDate.plusDays(1)) {
                System.out.println("DA");
                if (currentDate.getDayOfWeek().equals(pocetak.getDayOfWeek())) {
                    LocalDateTime newPocetak = currentDate.atTime(termin.getDatumVremePocetak().toLocalTime());
                    LocalDateTime newKraj = currentDate.atTime(termin.getDatumVremeKraj().toLocalTime());
                    System.out.println(newKraj);
                    Termin noviTermin;
                    if (termin.getOstalo() != null) {
                        noviTermin = new Termin(termin.getUcionica(), newPocetak, newKraj);
                    } else {
                        noviTermin = new Termin(termin.getUcionica(), newPocetak, newKraj, termin.getOstalo());
                    }
                    listaTermina.add(noviTermin);

                    if(mapa.containsKey(termin.getDatumVremePocetak().toLocalDate())){
                        ArrayList<Termin> temp = (ArrayList<Termin>) mapa.get(termin.getDatumVremePocetak().toLocalDate());
                        for(Termin termin1 : temp){
                            if( termin1.getDatumVremePocetak().toLocalTime().equals(termin.getDatumVremePocetak().toLocalTime())
                            && termin1.getDatumVremeKraj().toLocalTime().equals(termin.getDatumVremeKraj().toLocalTime())
                            && termin1.getUcionica().getNaziv().equals(termin.getUcionica().getNaziv())){
                                temp.remove(termin1);
                            }

                        }
                    }

//                    if(checkTermin(noviTermin)){
//                        listaTermina.add(noviTermin);
//                        mapaSlobodnihTermina.remove(noviTermin.getUcionica(),noviTermin);
//                    }
                }
            }
        }
    }

    @Override
    public void deleteTermin(Termin termin) {
       if (mapaSlobodnihTermina.containsKey(termin.getUcionica())) {
            ArrayList<Termin> listaSlobodnihTermina = mapaSlobodnihTermina.get(termin.getUcionica());
            listaTermina.remove(termin);
           listaSlobodnihTermina.add(termin);
       }
    }

    @Override
    public void moveTermin(Termin termin, Termin termin1) {
        termin.setStart(termin1.getStart());
        termin.setKraj(termin1.getKraj());
    }

    @Override
    public boolean checkTermin(Termin termin) {

       LocalDate datumPocetka = termin.getDatumVremePocetak().toLocalDate();
        LocalDate datumKraja = termin.getDatumVremeKraj().toLocalDate();

        LocalDateTime pocetak = termin.getDatumVremePocetak();

        LocalDateTime kraj = termin.getDatumVremeKraj();


        for (Termin temp : listaTermina) {
            if (listaDatuma.contains(datumPocetka) && !izuzetiDani.contains(datumPocetka)
            && listaDatuma.contains(datumKraja) && !izuzetiDani.contains(datumKraja)) {
                 return (!pocetak.isAfter(temp.getDatumVremePocetak()) ||
                        !kraj.isBefore(temp.getDatumVremeKraj())) &&
                        (!pocetak.isBefore(temp.getDatumVremePocetak())
                                || !kraj.isAfter(temp.getDatumVremePocetak()) || !kraj.isBefore(temp.getDatumVremeKraj())) &&
                        (!pocetak.isAfter(temp.getDatumVremePocetak()) ||
                                !pocetak.isBefore(temp.getDatumVremeKraj()) || !kraj.isAfter(temp.getDatumVremeKraj())) &&
                        (!pocetak.isBefore(temp.getDatumVremePocetak()) || !kraj.isAfter(temp.getDatumVremeKraj())) &&
                        pocetak != temp.getDatumVremePocetak() && kraj != temp.getDatumVremeKraj();
            }


        }
        return false;
    }



//    public void fillMap(ArrayList<LocalDate> datumi, ArrayList<Termin> termini, ArrayList<LocalDate> izuzeci) {
//
//
//        ArrayList<Termin> temp;
//        for (LocalDate date : datumi) {
//            temp = new ArrayList<>();
//            for (Termin appointment : termini) {
//                if (appointment.getDatumVremePocetak().toLocalDate().equals(date)
//                || appointment.getDatumVremeKraj().toLocalDate().equals(date) && !izuzeci.contains(appointment.getDatumVremePocetak().toLocalDate())
//                && !izuzeci.contains(appointment.getDatumVremeKraj().toLocalDate())) {
//                    temp.add(appointment);
//                }
//            }
//            if(!mapaRasporeda.containsValue(temp)) {
//                mapaRasporeda.put(date, temp);
//            }
//        }
//
//    }




    public void addTerminiDate(HashMap<LocalDate, List<Termin>> hashMap, Termin termin, String date) {
        //bice verovatno zapis tipa 29.09.2021. - 30.09.2021
        //morace da se string pretvori u LocalDate parsiranjem

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String[] spliter = date.split(" ");
        LocalDate pocetak = LocalDate.parse(spliter[0],formatter);
        LocalDate kraj = LocalDate.parse(spliter[1],formatter);
        System.out.println(pocetak);
        System.out.println(kraj);

        LocalDate datum = null;
        Termin noviTermin = null;
        ArrayList<Termin> termini = null;

        for (LocalDate currentDate = pocetak; !currentDate.isAfter(kraj); currentDate = currentDate.plusDays(1)) {
            if(currentDate.getDayOfWeek().equals(pocetak.getDayOfWeek())) {
                if (termin.getOstalo() != null) {
                    noviTermin = new Termin(termin.getUcionica(), currentDate.atTime(termin.getDatumVremePocetak().toLocalTime()), currentDate.atTime(termin.getDatumVremeKraj().toLocalTime()));
                } else {
                    noviTermin = new Termin(termin.getUcionica(), currentDate.atTime(termin.getDatumVremePocetak().toLocalTime()), currentDate.atTime(termin.getDatumVremeKraj().toLocalTime()), termin.getOstalo());
                }
                termini = new ArrayList<>();
                termini.add(noviTermin);
                hashMap.put(currentDate, termini);
            }
        }

    }

    @Override
    public boolean importProstorija(String s, String s1) throws IOException {
        importujProstorije(s,s1);
        return true;
    }


    @Override
    public boolean importSlobodnihTermina(String s) throws FileNotFoundException {
        importujSlobodne(s);
        return true;
    }

    public void importujSlobodne(String file) throws FileNotFoundException{
        File fileSlobodni = new File(file);
        Scanner scanner = new Scanner(fileSlobodni);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split("-",2);
            LocalTime pocetak = LocalTime.parse(splitLine[0]);
            LocalTime kraj = LocalTime.parse(splitLine[1]);
            for(Prostorija prostorija : listaProstorija){
                Termin termin = new Termin(pocetak,kraj,prostorija);
                listaSlobodnih.add(termin);
            }
        }
        for (LocalDate localDate : listaDatuma){
            mapa.put(localDate,listaSlobodnih);
        }
        scanner.close();
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
    public void fillSlobodniTermini() {
        for (LocalDate localDate : listaDatuma){
            String datum = localDate.toString();
                mapaSlobodnihTermina.put(datum, (ArrayList<Termin>) listaSlobodnih);

        }
//        for(Prostorija prostorija : listaProstorija){
//            List<Termin> pomocna = new ArrayList<>();
//            for(Termin termin : listaSlobodnih){
//                if((prostorija.getNaziv().equals(termin.getUcionica().getNaziv()))){
//                    pomocna.add(termin);
//                    mapaSlobodnihTermina.put(prostorija.getNaziv(), (ArrayList<Termin>) pomocna);
//                }
//            }
//
//        }
    }

    public List<Termin> getListaSlobodnih() {
        return listaSlobodnih;
    }

    public void setListaSlobodnih(List<Termin> listaSlobodnih) {
        this.listaSlobodnih = listaSlobodnih;
    }

    public List<Termin> getListaTermina() {
        return listaTermina;
    }

    public void setListaTermina(List<Termin> listaTermina) {
        this.listaTermina = listaTermina;
    }

    public HashMap<String, ArrayList<Termin>> getMapaSlobodnihTermina() {
        return mapaSlobodnihTermina;
    }

    public void setMapaSlobodnihTermina(HashMap<String, ArrayList<Termin>> mapaSlobodnihTermina) {
        this.mapaSlobodnihTermina = mapaSlobodnihTermina;
    }

    public List<Prostorija> getListaProstorija() {
        return listaProstorija;
    }

    public void setListaProstorija(List<Prostorija> listaProstorija) {
        this.listaProstorija = listaProstorija;
    }

    public List<LocalDate> getListaDatuma() {
        return listaDatuma;
    }

    public void setListaDatuma(List<LocalDate> listaDatuma) {
        this.listaDatuma = listaDatuma;
    }

    public List<LocalDate> getIzuzetiDani() {
        return izuzetiDani;
    }

    public void setIzuzetiDani(List<LocalDate> izuzetiDani) {
        this.izuzetiDani = izuzetiDani;
    }

    public HashMap<LocalDate, List<Termin>> getMapaRasporeda() {
        return mapaRasporeda;
    }

    public void setMapaRasporeda(HashMap<LocalDate, List<Termin>> mapaRasporeda) {
        this.mapaRasporeda = mapaRasporeda;
    }

    public HashMap<LocalDate, List<Termin>> getMapa() {
        return mapa;
    }

    public void setMapa(HashMap<LocalDate, List<Termin>> mapa) {
        this.mapa = mapa;
    }
}

