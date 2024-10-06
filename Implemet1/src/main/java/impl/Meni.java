package impl;

import spec.Prostorija;
import spec.Termin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Scanner;

public class Meni {

     public void ispisiMeni() {
         ImportExportCSV importExportCSV = new ImportExportCSV();
         OperacijeRaspored operacijeRaspored = new OperacijeRaspored();
         ExportToPdf exportToPdf = new ExportToPdf();
         operacijeRaspored.initRaspored();
         ImportExportJSON importExportJson = new ImportExportJSON();
         Filterr filterr = new Filterr();
         Scanner scanner = new Scanner(System.in);


         String line = "";
         while (true) {
             System.out.println("Izaberite opciju:\n");
             System.out.println("1. Ucitavanje fajlova\n");
             System.out.println("2. Filtriranje rasporeda\n");
             System.out.println("3. Dodavanje termina u raspored\n");
             System.out.println("4. Brisanje termina iz rasporeda\n");
             System.out.println("5. Premestanje termina\n");
             System.out.println("6. Export rasporeda\n");
             System.out.println("7. Zauzeti termini\n");
             System.out.println("8. Slobodni termini\n");
             System.out.println("9. spisak prostorija\n");
             line = scanner.nextLine();
             //int opcija = Integer.parseInt(line.toString());
             switch (line.toString()) {
                 case "1":
                     System.out.println("Izaberite opciju 1 ako zelite da radite sa CSV fajlom,ili opciju 2 ako zelite da radite sa JSON fajlom\n");
                     String opcija = scanner.nextLine();
                     switch (opcija) {
                         case "1":
                             System.out.println("Unesite period vazenja rasporeda u formatu dd/MM/yyyy-dd/MM/yyyy\n");
                             String datumi = scanner.nextLine();
                             System.out.println("Unesite izuzete dane razdvojene zarezom u formatu dd/MM/yyyy,dd/MM/yyyy..");
                             String izuzetiDani = scanner.nextLine();
                             System.out.println("Unesite putanju do CSV i konfig fajla(razdvojene zarezom)\n");
                             String linija = scanner.nextLine();
                             try {
                                 importExportCSV.loadData(linija.split(",")[0], linija.split(",")[1],datumi,izuzetiDani);
                                 //System.out.println(importExportCSV.getListaTermina());
                                 operacijeRaspored.getListaTermina().addAll(importExportCSV.getListaTermina());
                                 System.out.println(operacijeRaspored.getListaTermina());
                             } catch (IOException e) {
                                 System.out.println("Greska");
                                 return;
                             }
                             try {
                                 operacijeRaspored.importProstorija("C:\\Users\\Krivi\\Desktop\\projekat\\Implemet1\\src\\ucionice.csv","C:\\Users\\Krivi\\Desktop\\projekat\\Implemet1\\src\\configP.txt");
                             } catch (IOException e) {
                                 throw new RuntimeException(e);
                             }
                             try {
                                 operacijeRaspored.importSlobodnihTermina("C:\\Users\\Krivi\\Desktop\\projekat\\Implemet1\\src\\termini.txt",datumi,izuzetiDani);
                             } catch (FileNotFoundException e) {
                                 throw new RuntimeException(e);
                             }
                             operacijeRaspored.refreshList();
                             for (Termin termin1 : operacijeRaspored.getListaTermina()){
                                 for (Prostorija prostorija : operacijeRaspored.getListaProstorija()){
                                     if (termin1.getUcionica().getNaziv().equals(prostorija.getNaziv())){
                                         termin1.getUcionica().getOsobine().putAll(prostorija.getOsobine());
                                     }
                                 }
                             }

                             break;

                         case "2":
                             System.out.println("Unesite period vazenja rasporeda u formatu dd/MM/yyyy-dd/MM/yyyy\n");
                             String dat = scanner.nextLine();
                             System.out.println("Unesite izuzete dane razdvojene zarezom u formatu dd/MM/yyyy,dd/MM/yyyy..");
                             String izuzeti = scanner.nextLine();
                             System.out.println("Unesite putanju do JSON fajla \n");
                             String lajna = scanner.nextLine();
                             importExportJson.loadJson(lajna,dat,izuzeti);
                             operacijeRaspored.getListaTermina().addAll(importExportJson.getListaTermina());
                             System.out.println(operacijeRaspored.getListaTermina());
                             try {
                                 operacijeRaspored.importProstorija("C:\\Users\\Krivi\\Desktop\\projekat\\Implemet1\\src\\ucionice.csv","C:\\Users\\Krivi\\Desktop\\projekat\\Implemet1\\src\\configP.txt");
                             } catch (IOException e) {
                                 throw new RuntimeException(e);
                             }
                             try {
                                 operacijeRaspored.importSlobodnihTermina("C:\\Users\\Krivi\\Desktop\\projekat\\Implemet1\\src\\termini.txt",dat,izuzeti);
                             } catch (FileNotFoundException e) {
                                 throw new RuntimeException(e);
                             }
                             operacijeRaspored.refreshList();
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
                     System.out.println("Unesite opciju 1 ako zelite da izlistavate zauzete termine u rasporedu,ili opciju 2 ako zelite da izlistavate slobodne termine\n");
                     String in = scanner.nextLine();
                     switch (in){
                         case "1":
                             System.out.println(filterr.filtrirajTermine(operacijeRaspored.getListaTermina(), tip, parametar));
                             System.out.println("Unesite DA ako zelite da exportujete");
                             String export = scanner.nextLine();
                             if (export.toLowerCase().equals("da")){
                                 System.out.println("Unesite naziv fajla u koji zelite da exportujete\n");
                                 String naziv = scanner.nextLine();
                                 try {
                                     importExportCSV.exportData(filterr.filtrirajTermine(operacijeRaspored.getListaTermina(),tip,parametar),naziv);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                             break;

                         case "2":
                             System.out.println(filterr.filtrirajTermine(operacijeRaspored.getListaSlobodnih(), tip, parametar));
                             System.out.println("Unesite DA ako zelite da exportujete");
                             String exp = scanner.nextLine();
                             if (exp.toLowerCase().equals("da")){
                                 System.out.println("Unesite naziv fajla u koji zelite da exportujete\n");
                                 String naz = scanner.nextLine();
                                 try {
                                     importExportCSV.exportData(filterr.filtrirajTermine(operacijeRaspored.getListaSlobodnih(),tip,parametar),naz);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                             break;
                     }


                     break;

                 case "3":
                     System.out.println("Unesite parametre za novi termin. Parametri moraju biti zadati redom: Pocetak,Kraj,Ucionica,Ostalo(tip:parametar)\n");
                     String linija1 = scanner.nextLine();
                     String[] splitter1 = linija1.split(",");
                     int splitterLen = splitter1.length;
                     switch (splitterLen) {
                         case 4:
                             LocalDate date = LocalDate.parse(splitter1[0],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                             LocalTime pocetak = LocalTime.parse(splitter1[1]);
                             LocalTime kraj = LocalTime.parse(splitter1[2]);
                             Prostorija prostorija = new Prostorija(splitter1[3]);
                             Termin termin = new Termin(date,pocetak, kraj, prostorija);
                             operacijeRaspored.addTermin(termin);
                             break;
                         case 5:
                             HashMap<String, String> mapa = new HashMap<>();
                             LocalDate date1 = LocalDate.parse(splitter1[0],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                             LocalTime pocetak2 = LocalTime.parse(splitter1[1]);
                             LocalTime kraj2 = LocalTime.parse(splitter1[2]);
                             Prostorija prostorija2 = new Prostorija(splitter1[3]);
                             String[] splitter1od3 = splitter1[4].split(":");
                             mapa.put(splitter1od3[0], splitter1od3[1]);
                             Termin termin2 = new Termin(date1,pocetak2, kraj2, prostorija2, mapa);
                             operacijeRaspored.addTermin(termin2);
                             break;
                         case 6:
                             HashMap<String, String> mapa1 = new HashMap<>();
                             LocalDate date2 = LocalDate.parse(splitter1[0],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                             LocalTime pocetak3 = LocalTime.parse(splitter1[1]);
                             LocalTime kraj3 = LocalTime.parse(splitter1[2]);
                             Prostorija prostorija3 = new Prostorija(splitter1[3]);
                             String[] splitter1od4 = splitter1[4].split(":");
                             mapa1.put(splitter1od4[0], splitter1od4[1]);
                             String[] sp = splitter1[5].split(":");
                             mapa1.put(sp[0], sp[1]);
                             Termin termin3 = new Termin(date2,pocetak3, kraj3, prostorija3, mapa1);
                             operacijeRaspored.addTermin(termin3);
                             break;
                         case 7:
                             HashMap<String, String> mapa2 = new HashMap<>();
                             LocalDate date3 = LocalDate.parse(splitter1[0],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                             LocalTime pocetak4 = LocalTime.parse(splitter1[1]);
                             LocalTime kraj4 = LocalTime.parse(splitter1[2]);
                             Prostorija prostorija4 = new Prostorija(splitter1[3]);
                             String[] splitter1od5 = splitter1[4].split(":");
                             mapa2.put(splitter1od5[0], splitter1od5[1]);
                             String[] sp1 = splitter1[5].split(":");
                             mapa2.put(sp1[0], sp1[1]);
                             String[] sp2 = splitter1[6].split(":");
                             mapa2.put(sp2[0], sp2[1]);
                             Termin termin4 = new Termin(date3,pocetak4, kraj4, prostorija4, mapa2);
                             operacijeRaspored.addTermin(termin4);
                             break;
                         case 8:
                             HashMap<String, String> mapa3 = new HashMap<>();
                             LocalDate date4 = LocalDate.parse(splitter1[0],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                             LocalTime pocetak5 = LocalTime.parse(splitter1[1]);
                             LocalTime kraj5 = LocalTime.parse(splitter1[2]);
                             Prostorija prostorija5 = new Prostorija(splitter1[3]);
                             String[] splitter1od6 = splitter1[4].split(":");
                             mapa3.put(splitter1od6[0], splitter1od6[1]);
                             String[] sp3 = splitter1[5].split(":");
                             mapa3.put(sp3[0], sp3[1]);
                             String[] sp4 = splitter1[6].split(":");
                             mapa3.put(sp4[0], sp4[1]);
                             String[] sp5 = splitter1[7].split(":");
                             mapa3.put(sp5[0], sp5[1]);
                             Termin termin5 = new Termin(date4,pocetak5, kraj5, prostorija5, mapa3);
                             operacijeRaspored.addTermin(termin5);
                             break;
                     }

                     System.out.println(operacijeRaspored.getListaTermina());

                     break;

                 case "4":
                     System.out.println("Unesite datum,vreme pocetka,vreme zavrsetka i ucionicu termina koji zelite da obriste razdvojene zarezom\n");
                     String linija2 = scanner.nextLine();
                     String[] splitter2 = linija2.split(",");
                     LocalDate dateee = LocalDate.parse(splitter2[0],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                     LocalTime pocetak = LocalTime.parse(splitter2[1]);
                     LocalTime kraj = LocalTime.parse(splitter2[2]);
                     Prostorija prostorija = new Prostorija(splitter2[3]);
                     Termin termin = new Termin(dateee,pocetak, kraj, prostorija);
                     operacijeRaspored.deleteTermin(termin);
                     //System.out.println(operacijeRaspored.getListaTermina());
                     break;

                 case "5":
                     System.out.println("Unesite termin koji zelite da zamenite novim terminom\n");
                     String linija3 = scanner.nextLine();
                     String[] splitter3 = linija3.split(",");
                     LocalDate dante = LocalDate.parse(splitter3[0],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                     LocalTime pocetak1 = LocalTime.parse(splitter3[1]);
                     LocalTime kraj1 = LocalTime.parse(splitter3[2]);
                     Prostorija prostorija1 = new Prostorija(splitter3[3]);
                     Termin termin1 = new Termin(dante,pocetak1, kraj1, prostorija1);
                     System.out.println("Unesite nova vremena za termin u formatu hh:mm razdvojena zarezom \n");
                     String li = scanner.nextLine();
                     String[] spliterrrr = li.split(",");
                     LocalDate dat = LocalDate.parse(spliterrrr[0],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                     LocalTime pocetak2 = LocalTime.parse(spliterrrr[1]);
                     LocalTime kraj2 = LocalTime.parse(spliterrrr[2]);
                     Prostorija prostorija2 = new Prostorija(spliterrrr[3]);
                     Termin termin2 = new Termin(dat,pocetak2,kraj2,prostorija2);
                     operacijeRaspored.moveTermin(termin1,termin2);
                     break;

                 case "6":
                     System.out.println("Unesite opciju 1 ako zelite da se fajl eksportuje u CSV formatu,opciju 2 ako zelite da se eksportuje u Json formatu,ili opciju 3 ako zelite da eksportujete u pdf formatu\n");
                     String export = scanner.nextLine();
                     switch (export) {
                         case "1":
                             System.out.println("Unesite naziv fajla u kome ce se lista exportovati");
                             String linija4 = scanner.nextLine();
                             try {
                                 importExportCSV.exportData(operacijeRaspored.getListaTermina(),linija4);
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             break;
                         case "2":
                             System.out.println("Unesite naziv fajla u kome ce se lista exportovati");
                             String linija5 = scanner.nextLine();
                             importExportJson.exportJson(operacijeRaspored.getListaTermina(),linija5);
                             break;
                         case "3":
                             System.out.println("Unesite naziv PDF fajla u kome ce se lista exportovati");
                             String pdf = scanner.nextLine();
                            exportToPdf.exportToPdf(operacijeRaspored.getListaTermina(),pdf + ".pdf");
                            break;
                     }

                     break;

                 case "7":
                     System.out.println(operacijeRaspored.getListaTermina());
                     break;

                 case "8":
                     System.out.println(operacijeRaspored.getListaSlobodnih());
                     break;


                 case "9":
                     System.out.println(operacijeRaspored.getListaProstorija());
             }

         }

     }
}

