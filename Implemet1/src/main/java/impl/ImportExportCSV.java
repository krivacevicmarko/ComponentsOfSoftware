package impl;

import spec.ImportExport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import spec.Prostorija;
import spec.Raspored;
import spec.Termin;

import java.sql.SQLOutput;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.*;
import java.util.*;

public class ImportExportCSV extends ImportExport{
    @Override
    public boolean loadData(String filePath, String configPath,String datumi,String izuzetiDani) throws IOException {
        loadApache(filePath,configPath,datumi,izuzetiDani);
        return true;
    }

    @Override
    public boolean exportData(List<Termin> list,String path) throws IOException {
        writeData(list,path);
        return true;
    }



    public void loadApache(String filePath, String configPath,String datumi,String izuzetiDani) throws IOException {
        String[] podeli = datumi.split("-");
        LocalDate poc = LocalDate.parse(podeli[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate kraj = LocalDate.parse(podeli[1],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<LocalDate> izuzetiLista = new ArrayList<>();
        if(izuzetiDani.contains(",")) {
            String[] sp = izuzetiDani.split(",");
            for (String dateString : sp) {
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                izuzetiLista.add(date);
            }
        }else {
            LocalDate datummm = LocalDate.parse(izuzetiDani,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            izuzetiLista.add(datummm);
        }
        List<Config> columnMappings = readConfig(configPath);
        Map<Integer, String> mappings = new HashMap<>();
        for(Config configMapping : columnMappings) {
            mappings.put(configMapping.getIndex(), configMapping.getOriginal());
        }

        FileReader fileReader = new FileReader(filePath);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(mappings.get(-1));


        for (CSVRecord record : parser) {
            Termin appointment = new Termin();

            for (Config entry : columnMappings) {
                int columnIndex = entry.getIndex();

                if(columnIndex == -1) continue;

                String columnName = entry.getCustom();

                switch (mappings.get(columnIndex)) {
                    case "place":
                        appointment.setUcionica(new Prostorija(record.get(columnIndex)));
                        break;
                    case "termin":
                        String[] splitter = record.get(columnIndex).split("-");
                        LocalTime startDateTime = LocalTime.parse(splitter[0]);
                        appointment.setStart(startDateTime);
                        LocalTime endDateTime = LocalTime.parse(splitter[1]);
                        appointment.setKraj(endDateTime);
                        break;
                    case "additional":
                        appointment.getOstalo().put(columnName, record.get(columnIndex));
                        break;
                }
            }
            if (appointment.getOstalo().containsKey("Dan")){
                for(LocalDate current = poc ; current.isBefore(kraj) ; current = current.plusDays(1)){
                    String dan = appointment.getOstalo().get("Dan");
                    DayOfWeek dann1 = null;
                    if(dan.equals("PON")){
                        dann1 = DayOfWeek.MONDAY;
                    } else if (dan.equals("UTO")) {
                        dann1 = DayOfWeek.TUESDAY;
                    } else if (dan.equals("SRE")) {
                        dann1 = DayOfWeek.WEDNESDAY;
                    } else if (dan.equals("ČET")) {
                        dann1 = DayOfWeek.THURSDAY;
                    } else if (dan.equals("PET")) {
                        dann1 = DayOfWeek.FRIDAY;
                    }
                    Termin termin = new Termin(current,appointment.getStart(),appointment.getKraj(),appointment.getUcionica(),appointment.getOstalo());
                    if (!(izuzetiLista.contains(current))){
                        if (current.getDayOfWeek().equals(dann1)) {
                            termin.setDatum(current);
                            getListaTermina().add(termin);
                        }
                    }
                }
            }
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

    private void writeData(List<Termin> lista,String path) throws IOException {
        // Create a FileWriter and CSVPrinter
        FileWriter fileWriter = new FileWriter(path);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);
        for (Termin termin : lista) {
            csvPrinter.printRecord(
                    "Termin={datum=" + termin.getDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "pocetak=" + termin.getStart(),
                    "kraj=" + termin.getKraj(),
                    termin.getUcionica(),
                    termin.getOstalo()
            );
        }

        csvPrinter.close();
        fileWriter.close();
    }

}
