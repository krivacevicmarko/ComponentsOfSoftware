package impl;
import spec.FilterTermina;
import spec.Termin;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Filterr extends FilterTermina {
    OperacijeRaspored operacijeRaspored;

    @Override
    public void izlistajZauzeteTermine(List<Termin> list) {
        System.out.println(list);
    }

    @Override
    public void izlistajSlobodneTermine(List<Termin> list) {

    }

    @Override
    public List<Termin> filtrirajTermine(List<Termin> list,String s, String s1) {

        ArrayList<Termin> finalnaLista = new ArrayList<>();


        switch(s.toLowerCase()){

            case "vreme":
                //pocetak i kraj
                if(s1.contains("-")) {
                    String[] splituj = s1.split("-");
                    LocalTime poc = LocalTime.parse(splituj[0]);
                    LocalTime kr = LocalTime.parse(splituj[1]);
                    System.out.println(poc + " " + kr);
                    for (Termin termin : list) {
                        if (poc.equals(termin.getStart()) && kr.equals(termin.getKraj())) {
                            finalnaLista.add(termin);
                        }
                    }
                }
                break;

            case "datum":
                LocalDate date = LocalDate.parse(s1, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                System.out.println(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                for (Termin termin : list){
                    if(termin.getDatum().equals(date)){
                        finalnaLista.add(termin);
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

            case "tip": case "nastavnik":case "dan": case "predmet":
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
