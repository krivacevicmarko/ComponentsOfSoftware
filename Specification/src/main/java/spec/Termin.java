package spec;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Teodor Jakovljevic,Marko Krivacevic
 *
 */



/**
 * klasa koja sadrzi podatke o terminu
 */

public class Termin {

    private LocalTime pocetak;
    private LocalTime kraj;
    private Prostorija ucionica;
    private LocalDate datum;
    private LocalDateTime datumVremePocetak;
    private LocalDateTime datumVremeKraj;
    private Map<String,String> ostalo;

    public Termin(){
        this.ostalo = new HashMap<>();
    }


    //IMP2
    public Termin(Prostorija ucionica,LocalDate datum, LocalDateTime datumVremePocetak, LocalDateTime datumVremeKraj, Map<String, String> ostalo) {
        this.ucionica = ucionica;
        this.datum = datum;
        this.datumVremePocetak = datumVremePocetak;
        this.datumVremeKraj = datumVremeKraj;
        this.ostalo = ostalo;
    }

    public Termin(Prostorija ucionica, LocalDateTime datumVremePocetak, LocalDateTime datumVremeKraj) {
        this.ucionica = ucionica;
        this.datumVremePocetak = datumVremePocetak;
        this.datumVremeKraj = datumVremeKraj;
        this.ostalo = new HashMap<>();
    }

    // za IMP1
    public Termin(LocalDate datum,LocalTime pocetak, LocalTime kraj, Prostorija ucionica) {
        this.datum = datum;
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.ucionica = ucionica;
        this.ostalo = new HashMap<>();
    }

    public Termin(LocalDate datum,LocalTime pocetak, LocalTime kraj, Prostorija ucionica, Map<String, String> ostalo) {
        this.datum = datum;
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.ucionica = ucionica;
        this.ostalo = ostalo;
    }
    //

    public LocalTime getStart() {
        return pocetak;
    }

    public void setStart(LocalTime pocetak) {
        this.pocetak = pocetak;
    }

    public LocalTime getKraj() {
        return kraj;
    }

    public void setKraj(LocalTime kraj) {
        this.kraj = kraj;
    }

    public Prostorija getUcionica() {
        return ucionica;
    }

    public void setUcionica(Prostorija ucionica) {
        this.ucionica = ucionica;
    }

    public Map<String, String> getOstalo() {
        return ostalo;
    }

    public void setOstalo(Map<String, String> ostalo) {
        this.ostalo = ostalo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Termin termin = (Termin) object;
        return Objects.equals(pocetak, termin.pocetak) && Objects.equals(kraj, termin.kraj) && Objects.equals(ucionica, termin.ucionica) && Objects.equals(ostalo, termin.ostalo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pocetak, kraj, ucionica, ostalo);
    }

    @Override
    public String toString() {
        if(datumVremeKraj==null) {
            return "Termin{" + "datum=" + datum.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    ", pocetak=" + pocetak +
                    ", kraj=" + kraj +
                    ", ucionica=" + ucionica +
                    ", ostalo=" + ostalo +
                    '}'+ "\n";
        }else{
            return "Termin{" +
                    "pocetak=" + datumVremePocetak +
                    ", kraj=" + datumVremeKraj +
                    ", ucionica=" + ucionica +
                    ", ostalo=" + ostalo +
                    '}' + "\n";
        }
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public LocalTime getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalTime pocetak) {
        this.pocetak = pocetak;
    }

    public LocalDateTime getDatumVremePocetak() {
        return datumVremePocetak;
    }

    public void setDatumVremePocetak(LocalDateTime datumVremePocetak) {
        this.datumVremePocetak = datumVremePocetak;
    }

    public LocalDateTime getDatumVremeKraj() {
        return datumVremeKraj;
    }

    public void setDatumVremeKraj(LocalDateTime datumVremeKraj) {
        this.datumVremeKraj = datumVremeKraj;
    }
}