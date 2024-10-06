package org.example;

import spec.Prostorija;
import spec.Termin;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Main {
    public static void main(String[] args) throws IOException {

        Meni meni = new Meni();
        meni.ispisiMeni();

//        OperacijeRaspored raspored = new OperacijeRaspored();
//        Filter filter = new Filter();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//        LocalDateTime time1 = LocalDateTime.parse("15/02/2023 09:15", formatter);
//        LocalDateTime time2 = LocalDateTime.parse("16/02/2023 11:00", formatter);
//        LocalDateTime time3 = LocalDateTime.parse("13/04/2023 11:15", formatter);
//        LocalDateTime time4 = LocalDateTime.parse("19/02/2023 13:00", formatter);
//        LocalDateTime time5 = LocalDateTime.parse("01/05/2023 12:00", formatter);
//
//
//        Prostorija prostorija1 = new Prostorija("raf04");
//        Prostorija prostorija2 = new Prostorija("raf01");
//        Prostorija prostorija3 = new Prostorija("rg07");
//        HashMap<String, String> ostalo = new HashMap<>();
//        ostalo.put("dan", "UTO");
//        ostalo.put("grupe", "101, 102, 103");
//        ostalo.put("tip", "v");
//        ostalo.put("nastavnik", "redza");
//        Termin termin1 = new Termin(prostorija3, time1, time2);
//        Termin termin2 = new Termin(prostorija3, time2, time3 , ostalo);
//        Termin termin3 = new Termin(prostorija3, time5, time4);
//        Termin termin4 = new Termin(prostorija3, time3, time5);  //18/04 - 01/05
//        Termin termin5 = new Termin(prostorija3, time1, time5);
//        raspored.getListaDatuma().add(termin1.getDatumVremePocetak().toLocalDate());
//        raspored.getListaDatuma().add(termin3.getDatumVremePocetak().toLocalDate());
//        raspored.getListaDatuma().add(termin4.getDatumVremePocetak().toLocalDate());
//        raspored.getListaTermina().add(termin2);
//
//
//        raspored.getListaTermina().add(termin3);
//
//        ArrayList<Termin> listaSlob = new ArrayList<>();
//        listaSlob.add(termin1);
//        listaSlob.add(termin2);
//        listaSlob.add(termin3);
//        listaSlob.add(termin4);
//        listaSlob.add(termin5);
//        raspored.getMapaSlobodnihTermina().put(prostorija3,listaSlob);
//        raspored.getIzuzetiDani().add(termin1.getDatumVremePocetak().toLocalDate());
//
//        raspored.getMapaRasporeda().get(prostorija3);
//        raspored.addTermin(termin4);
//
//
//        raspored.fillMap((ArrayList<LocalDate>) raspored.getListaDatuma(), (ArrayList<Termin>) raspored.getListaTermina(), (ArrayList<LocalDate>) raspored.getIzuzetiDani());
//
//        Termin noviTermin = termin1;
//
//       // raspored.addTerminiDate(raspored.getMapaRasporeda(), noviTermin, "01/11/2021 01/12/2021");
//        //System.out.println(raspored.getMapaRasporeda());
//
//        System.out.println(raspored.getListaTermina());


    }
}