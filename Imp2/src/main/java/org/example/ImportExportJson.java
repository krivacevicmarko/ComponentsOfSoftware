package org.example;

import com.google.gson.*;
import spec.Prostorija;
import spec.Termin;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImportExportJson extends spec.ImportExportJson {

    @Override
    public boolean loadJson(String s) {
        loadJSONN(s);
        return true;
    }

    @Override
    public boolean exportJson(String s) {
        exportToJson(lista,s);
        return true;
    }

    private ArrayList<Termin> lista = new ArrayList<>();


    public void loadJSONN(String filename) {

        try (FileReader reader = new FileReader(filename)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray appointmentsArray = jsonObject.getAsJsonArray("appointments");

            for (JsonElement appointmentElement : appointmentsArray) {
                JsonObject appointmentObject = appointmentElement.getAsJsonObject();
                LocalDateTime pocetak = LocalDateTime.parse(appointmentObject.get("pocetak").getAsString());
                LocalDateTime kraj = LocalDateTime.parse(appointmentObject.get("kraj").getAsString());
                String prostorija = appointmentObject.get("prostorija").getAsString();
                Prostorija prostorija1 = new Prostorija(prostorija);
                HashMap<String,String> novaMapa = new HashMap<>();
                if((appointmentObject.get("grupe"))!=null) {
                    String grupe = appointmentObject.get("grupe").getAsString();
                    novaMapa.put("grupe",grupe);
                }
                if(appointmentObject.get("nastavnik")!=null) {
                    String nastavnik = appointmentObject.get("nastavnik").getAsString();
                    novaMapa.put("nastavnik",nastavnik);

                }
                if(appointmentObject.get("tip")!=null) {
                    String tip = appointmentObject.get("tip").getAsString();
                    novaMapa.put("tip",tip);

                }
                if(appointmentObject.get("dan")!=null) {
                    String dan = appointmentObject.get("dan").getAsString();
                    novaMapa.put("dan",dan);
                }
                if(!(novaMapa.isEmpty())){
                    Termin termin1 = new Termin(prostorija1,pocetak,kraj,novaMapa);
                    lista.add(termin1);
                }else{
                    Termin termin = new Termin(prostorija1,pocetak,kraj);
                    lista.add(termin);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void exportToJson(List<Termin> listaTermina, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Create a JsonObject for each Termin object and add them to a JsonArray
            JsonArray jsonArray = new JsonArray();
            for (Termin termin : lista) {
                JsonObject terminObject = new JsonObject();
                terminObject.addProperty("pocetak", termin.getDatumVremePocetak().toString());
                terminObject.addProperty("kraj", termin.getDatumVremeKraj().toString());
                terminObject.addProperty("prostorija", termin.getUcionica().toString());
                if(termin.getOstalo().containsKey("grupe")){
                    terminObject.addProperty("grupe", termin.getOstalo().get("grupe"));
                }
                if(termin.getOstalo().containsKey("nastavnik")){
                    terminObject.addProperty("nastavnik", termin.getOstalo().get("nastavnik"));
                }
                if(termin.getOstalo().containsKey("tip")){
                    terminObject.addProperty("tip", termin.getOstalo().get("tip"));
                }
                if(termin.getOstalo().containsKey("dan")){
                    terminObject.addProperty("dan", termin.getOstalo().get("dan"));
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

}
