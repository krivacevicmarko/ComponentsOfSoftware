package org.example;
import spec.FilterTermina;
import spec.Termin;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Filter extends FilterTermina {

        @Override
    public void izlistajZauzeteTermine(List<Termin> list) {
        System.out.println(list);
    }

    @Override
    public void izlistajSlobodneTermine(List<Termin> list) {
        System.out.println(list);
    }

    @Override
    public List<Termin> filtrirajTermine(List<Termin> list, String s, String s1) {

        ArrayList<Termin> finalnaLista = new ArrayList<>();


        switch(s.toLowerCase()){
            case "vreme":
                //pocetak i kraj
                if(s1.contains("-")) {
                    String[] splitter = s1.split("-");
                    LocalTime pocetak = LocalTime.parse(splitter[0]);
                    LocalTime kraj = LocalTime.parse(splitter[1]);
                    System.out.println(pocetak + "-" + kraj);
                    for (Termin termin : list) {
                        if (pocetak.equals(termin.getDatumVremePocetak().toLocalTime()) &&
                                kraj.equals(termin.getDatumVremeKraj().toLocalTime())) {
                            finalnaLista.add(termin);
                        }
                    }
                    // npr 09:00 1:30
                }else if (s1.contains(" ")){
                    String[] splitter = s1.split(" ");
                    LocalTime pocetak = LocalTime.parse(splitter[0]);
                    LocalTime temp = LocalTime.parse(splitter[1], DateTimeFormatter.ofPattern("H:mm"));

                    LocalTime kraj = pocetak.plusHours(temp.getHour()).plusMinutes(temp.getMinute());
                    System.out.println(pocetak + "-" + kraj);
                    for(Termin termin : list){
                        if(pocetak.equals(termin.getDatumVremePocetak().toLocalTime())
                                && kraj.equals(termin.getDatumVremeKraj().toLocalTime())){
                            finalnaLista.add(termin);
                        }
                    }
                }
                break;

            case "ucionica":
                for(Termin termin: list){

                    if(termin.getUcionica().getNaziv().contains(s1)){
                        finalnaLista.add(termin);
                    }
                }
                break;
                //Dva nacina filtriranja
            // 1) Po datumu  => 10/01/2021 21/01/2021
            // 2) Po danu i trajanju => uto 11-12
            case "datum":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                System.out.println(s1);
                if(s1.contains("/")) {
                    String[] splitter = s1.split(" ");
                    LocalDate pocetak = LocalDate.parse(splitter[0], formatter);
                    LocalDate kraj = LocalDate.parse(splitter[1], formatter);
                    for (Termin termin : list) {
                        if (termin.getDatumVremePocetak().toLocalDate().equals(pocetak) &&
                                termin.getDatumVremeKraj().toLocalDate().equals(kraj)) {
                            finalnaLista.add(termin);
                        }
                    }
                }
                else if(s1.contains("-")){
                    String[] splitter =  s1.split(" ");
                    String dan = splitter[0];
                    String trajanje = splitter[1];
                    String[] pomocniSplitter = trajanje.split("-");
                    LocalTime pocetak = LocalTime.parse(pomocniSplitter[0]);
                    LocalTime kraj = LocalTime.parse(pomocniSplitter[1]);
                    for(Termin termin: list){
                        if(termin.getOstalo().containsValue(dan) && termin.getDatumVremePocetak().toLocalTime().equals(pocetak) &&
                        termin.getDatumVremeKraj().toLocalTime().equals(kraj)){
                            finalnaLista.add(termin);
                        }
                    }

                }
                break;

            case "grupe":
                for(Termin termin : list){
                    if(termin.getOstalo().containsKey(s)){
                        String linija = termin.getOstalo().get(s);
                        System.out.println(linija);
                        if(linija.contains(s1)){
                            finalnaLista.add(termin);
                        }
                    }
                }
                break;

            case "tip": case "nastavnik": case "dan":
                for(Termin termin: list){
                    if(termin.getOstalo().containsKey(s) && termin.getOstalo().containsValue(s1)){
                        finalnaLista.add(termin);
                    }
                }
                break;

        }




        return finalnaLista;
    }
}

