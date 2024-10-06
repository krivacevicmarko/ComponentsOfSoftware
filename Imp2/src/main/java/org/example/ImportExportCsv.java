package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import spec.ImportExport;
import spec.Prostorija;
import spec.Termin;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ImportExportCsv extends ImportExport {

    @Override
     public boolean loadData(String s, String s1) throws IOException {
        loadApache(s,s1);
        return true;
    }

    @Override
    public boolean exportData(String s) throws IOException {
        writeData(s);
        return true;
    }

    public void loadApache(String filePath, String configPath) throws IOException {
        List<Config> columnMappings = readConfig(configPath);
        Map<Integer, String> mappings = new HashMap<>();
        for(Config configMapping : columnMappings) {
            mappings.put(configMapping.getIndex(), configMapping.getOriginal());
        }

        FileReader fileReader = new FileReader(filePath);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(mappings.get(-1));

        for (CSVRecord record : parser) {
            Termin appointment = new Termin();

            for (Config entry : columnMappings) {
                int columnIndex = entry.getIndex();

                if(columnIndex == -1) continue;

                String columnName = entry.getCustom();

                switch (mappings.get(columnIndex)) {
                    case "place":
                        appointment.setUcionica(new Prostorija(record.get(columnIndex)));
                        System.out.println(appointment.getUcionica());
                        break;
                    case "termin":
                        String[] splitter = record.get(columnIndex).split("-");
                        LocalDateTime startDateTime = LocalDateTime.parse(splitter[0],formatter);
                        appointment.setDatumVremePocetak(startDateTime);
                        LocalDateTime endDateTime = LocalDateTime.parse(splitter[1],formatter);
                        appointment.setDatumVremeKraj(endDateTime);
                        break;
                    case "additional":
                        appointment.getOstalo().put(columnName, record.get(columnIndex));
                        break;
                }
            }
            System.out.println(appointment);
            getListaTermina().add(appointment);
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

    private void writeData(String path) throws IOException {
        // Create a FileWriter and CSVPrinter
        FileWriter fileWriter = new FileWriter(path);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

        for (Termin termin : super.getListaTermina()) {
            csvPrinter.printRecord(
                    termin.getDatumVremePocetak(),
                    termin.getDatumVremeKraj(),
                    termin.getUcionica(),
                    termin.getOstalo()
            );
        }

        csvPrinter.close();
        fileWriter.close();
    }

    @Override
    public boolean loadData(String path, String configPath, String datumi, String izuzetiDani) throws IOException {
        return false;
    }

    @Override
    public boolean exportData(List<Termin> lista, String path) throws IOException {
        return false;
    }
}
