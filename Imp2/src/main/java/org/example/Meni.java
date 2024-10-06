package org.example;

import spec.Prostorija;
import spec.Termin;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class Meni {
    public void ispisiMeni() {
         Scanner scanner = new Scanner(System.in);
         ImportExportCsv importExportCsv = new ImportExportCsv();
         ImportExportJson importExportJson = new ImportExportJson();
         OperacijeRaspored operacijeRaspored = new OperacijeRaspored();
         Filter filter = new Filter();

         String line = "";
        while(true){
            System.out.println("Izaberite opciju:\n");
            System.out.println("1. Ucitavanje fajlova\n");
            System.out.println("2. Filtriranje rasporeda\n");
            System.out.println("3. Dodavanje termina u raspored\n");
            System.out.println("4. Brisanje termina iz rasporeda\n");
            System.out.println("5. Premestanje termina\n");
            System.out.println("6. Export rasporeda\n");
            System.out.println("7. Zauzeti termini\n");
            System.out.println("8. Slobodni termini\n");
            line = scanner.nextLine();
            switch (line.toString()){
                case "1":
                    System.out.println("Izaberite opciju 1 ako zelite da radite sa CSV fajlom,ili opciju 2 ako zelite da radite sa JSON-om\n");
                    String opcija = scanner.nextLine();
                    switch (opcija){
                        case "1":
                            System.out.println("Unesite putanju do CSV i konfig fajla(razdvojene zarezom)");
                            String linija = scanner.nextLine();
                            try {
                                importExportCsv.loadData(linija.split(",")[0],linija.split(",")[1]);
                                operacijeRaspored.getListaTermina().addAll(importExportCsv.getListaTermina());
                            }catch (IOException e){
                                e.printStackTrace();
                                return;
                            }
                            System.out.println(operacijeRaspored.getListaTermina());
                            break;

                        case "2":
                            System.out.println("Unesite putanju do JSON fajla\n");
                            String lin = scanner.nextLine();
                            importExportJson.loadJson(lin);
                            System.out.println(importExportJson.getLista());
                            operacijeRaspored.getListaTermina().addAll(importExportJson.getLista());
                            break;
                    }

                    break;

                case "2":
                    System.out.println("Unesite tip i parametar po kome zelite da filtrirate razdvojene zarezom\n" +
                            "Ako zelite da filtrirate po vremenu,unesite jednu od opcija:(hh:mm-hh:mm)pocetak i kraj termina,ili (hh:mm hh:mm)pocetak i trajanje termina\n" );
                    String input = scanner.nextLine();
                    String[] splitter = input.split(",");
                    String tip = splitter[0];
                    String parametar = splitter[1];
                    System.out.println(filter.filtrirajTermine(operacijeRaspored.getListaTermina(), tip, parametar));
                    System.out.println("Unesite DA ako zelite da exportujete");
                    break;


                case "3":
                    System.out.println("Unesite parametre za novi termin,redosledom:ucionica,pocetak,kraj,ostalo\n");
                    String case3 = scanner.nextLine();
                    String[] split = case3.split(",");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    int len = split.length;
                    switch (len){
                        case 3:
                            LocalDateTime pocetak = LocalDateTime.parse(split[0],formatter);
                            LocalDateTime kraj = LocalDateTime.parse(split[1],formatter);
                            Prostorija prostorija = new Prostorija(split[2]);
                            Termin termin = new Termin(prostorija,pocetak,kraj);
                            operacijeRaspored.addTermin(termin);
                            break;

                        case 4:
                            HashMap<String, String> mapa = new HashMap<>();
                            LocalDateTime pocetak2 = LocalDateTime.parse(split[0]);
                            LocalDateTime kraj2 = LocalDateTime.parse(split[1]);
                            Prostorija prostorija2 = new Prostorija(split[2]);
                            String[] splitter1od3 = split[3].split(":");
                            mapa.put(splitter1od3[0], splitter1od3[1]);
                            Termin termin2 = new Termin(prostorija2,pocetak2, kraj2, mapa);
                            operacijeRaspored.addTermin(termin2);
                            break;

                        case 5:
                            HashMap<String, String> mapa1 = new HashMap<>();
                            LocalDateTime pocetak3 = LocalDateTime.parse(split[0]);
                            LocalDateTime kraj3 = LocalDateTime.parse(split[1]);
                            Prostorija prostorija3 = new Prostorija(split[2]);
                            String[] splitter1od4 = split[3].split(":");
                            mapa1.put(splitter1od4[0], splitter1od4[1]);
                            String[] sp = split[4].split(":");
                            mapa1.put(sp[0], sp[1]);
                            Termin termin3 = new Termin(prostorija3,pocetak3, kraj3, mapa1);
                            operacijeRaspored.addTermin(termin3);
                            break;


                        case 6:
                            HashMap<String, String> mapa2 = new HashMap<>();
                            LocalDateTime pocetak4 = LocalDateTime.parse(split[0]);
                            LocalDateTime kraj4 = LocalDateTime.parse(split[1]);
                            Prostorija prostorija4 = new Prostorija(split[2]);
                            String[] splitter1od5 = split[3].split(":");
                            mapa2.put(splitter1od5[0], splitter1od5[1]);
                            String[] sp1 = split[4].split(":");
                            mapa2.put(sp1[0], sp1[1]);
                            String[] sp2 = split[5].split(":");
                            mapa2.put(sp2[0], sp2[1]);
                            Termin termin4 = new Termin(prostorija4,pocetak4, kraj4, mapa2);
                            operacijeRaspored.addTermin(termin4);
                            break;

                        case 7:
                            HashMap<String, String> mapa3 = new HashMap<>();
                            LocalDateTime pocetak5 = LocalDateTime.parse(split[0]);
                            LocalDateTime kraj5 = LocalDateTime.parse(split[1]);
                            Prostorija prostorija5 = new Prostorija(split[2]);
                            String[] splitter1od6 = split[3].split(":");
                            mapa3.put(splitter1od6[0], splitter1od6[1]);
                            String[] sp3 = split[4].split(":");
                            mapa3.put(sp3[0], sp3[1]);
                            String[] sp4 = split[5].split(":");
                            mapa3.put(sp4[0], sp4[1]);
                            String[] sp5 = split[6].split(":");
                            mapa3.put(sp5[0], sp5[1]);
                            Termin termin5 = new Termin(prostorija5,pocetak5, kraj5, mapa3);
                            operacijeRaspored.addTermin(termin5);
                            break;
                    }

                    break;
                case "4":
                    System.out.println(operacijeRaspored.getMapaSlobodnihTermina());

                    break;

                case "5":
                    System.out.println(operacijeRaspored.getMapa());

                    break;


                case "6":

                    break;

                case "7":
                    System.out.println(operacijeRaspored.getListaTermina());

                    break;

                case "8":
                    System.out.println(operacijeRaspored.getMapaSlobodnihTermina());

                    break;


            }
    }

    }
}