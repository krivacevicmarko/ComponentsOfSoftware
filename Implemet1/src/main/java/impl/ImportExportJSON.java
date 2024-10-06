package impl;

import com.google.gson.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import spec.ImportExportJson;
import spec.Prostorija;
import spec.Termin;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportExportJSON extends ImportExportJson {


    @Override
    public boolean loadJson(String s,String datumi,String izuzetiDani) {
        loadJSONN(s,datumi,izuzetiDani);
        return true;
    }

    @Override
    public boolean exportJson(List<Termin> list,String s) {
        exportToJson(list,s);
        return true;
    }

    private ArrayList<Termin> listaTermina = new ArrayList<>();

    public void loadJSONN(String filename,String datumi,String izuzetiDani) {
        String[] podeli = datumi.split("-");
        LocalDate pocetakDatuma = LocalDate.parse(podeli[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate krajDatuma = LocalDate.parse(podeli[1],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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
        try (FileReader reader = new FileReader(filename)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray appointmentsArray = jsonObject.getAsJsonArray("appointments");

            for (JsonElement appointmentElement : appointmentsArray) {
                JsonObject appointmentObject = appointmentElement.getAsJsonObject();
                LocalTime pocetak = LocalTime.parse(appointmentObject.get("Pocetak").getAsString());
                LocalTime kraj = LocalTime.parse(appointmentObject.get("Kraj").getAsString());
                String prostorija = appointmentObject.get("Prostorija").getAsString();
                Prostorija prostorija1 = new Prostorija(prostorija);
                HashMap<String,String> novaMapa = new HashMap<>();
                if((appointmentObject.get("Predmet")!=null)){
                    String predmet = appointmentObject.get("Predmet").getAsString();
                    novaMapa.put("Predmet",predmet);
                }
                if((appointmentObject.get("Grupe"))!=null) {
                    String grupe = appointmentObject.get("Grupe").getAsString();
                    novaMapa.put("Grupe",grupe);
                }
                if(appointmentObject.get("Nastavnik")!=null) {
                    String nastavnik = appointmentObject.get("Nastavnik").getAsString();
                    novaMapa.put("Nastavnik",nastavnik);

                }
                if(appointmentObject.get("Tip")!=null) {
                    String tip = appointmentObject.get("Tip").getAsString();
                    novaMapa.put("Tip",tip);

                }
                if(appointmentObject.get("Dan")!=null) {
                    String dan = appointmentObject.get("Dan").getAsString();
                    novaMapa.put("Dan",dan);
                }
                if (novaMapa.containsKey("Dan")){
                    for(LocalDate current = pocetakDatuma ; current.isBefore(krajDatuma) ; current = current.plusDays(1)){
                        String dan = novaMapa.get("Dan");
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
                        Termin termin = new Termin(current,pocetak,kraj,prostorija1,novaMapa);
                        if (!(izuzetiLista.contains(current))){
                            if (current.getDayOfWeek().equals(dann1)) {
                                termin.setDatum(current);
                                getListaTermina().add(termin);
                            }
                        }
                    }
                }

//                if(!(novaMapa.isEmpty())){
//                    Termin termin1 = new Termin(pocetak,kraj,prostorija1,novaMapa);
//                    listaTermina.add(termin1);
//                }else{
//                    Termin termin = new Termin(pocetak, kraj, prostorija1);
//                    listaTermina.add(termin);
//                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void exportToJson(List<Termin> lista, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Create a JsonObject for each Termin object and add them to a JsonArray
            JsonArray jsonArray = new JsonArray();
            for (Termin termin : lista) {
                JsonObject terminObject = new JsonObject();
                terminObject.addProperty("datum", String.valueOf(termin.getDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                terminObject.addProperty("pocetak", termin.getPocetak().toString());
                terminObject.addProperty("kraj", termin.getKraj().toString());
                terminObject.addProperty("prostorija", termin.getUcionica().toString());
                if(termin.getOstalo().containsKey("Predmet")){
                    terminObject.addProperty("predmet",termin.getOstalo().get("Predmet"));
                }
                if(termin.getOstalo().containsKey("Grupe")){
                    terminObject.addProperty("grupe", termin.getOstalo().get("Grupe"));
                }
                if(termin.getOstalo().containsKey("Nastavnik")){
                    terminObject.addProperty("nastavnik", termin.getOstalo().get("Nastavnik"));
                }
                if(termin.getOstalo().containsKey("Tip")){
                    terminObject.addProperty("tip", termin.getOstalo().get("Tip"));
                }
                if(termin.getOstalo().containsKey("Dan")){
                    terminObject.addProperty("dan", termin.getOstalo().get("Dan"));
                }
                jsonArray.add(terminObject);
            }

            // Create a JsonObject with the JsonArray as the "appointments" field
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("appointments", jsonArray);

            // Convert the JsonObject to a JSON string and write it to the file
            String jsonString = gson.toJson(jsonObject);
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Termin> getListaTermina() {
        return listaTermina;
    }

    public void setListaTermina(ArrayList<Termin> listaTermina) {
        this.listaTermina = listaTermina;
    }
}
