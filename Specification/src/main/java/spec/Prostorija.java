package spec;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Teodor Jakovljevic,Marko Krivacevic
 *
 */

/**
 * klasa koja sadrzi podatke o ucionicama
 *
 */
public class Prostorija {   

    private String naziv;
    private HashMap<String,String> osobine;

    public Prostorija() {
        osobine = new HashMap<>();
    }

    public Prostorija(String naziv) {
        this.naziv = naziv;
        this.osobine = new HashMap<>();
    }

    public Prostorija(String naziv, HashMap<String, String> osobine) {
        this.naziv = naziv;
        this.osobine = osobine;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Prostorija that = (Prostorija) object;
        return Objects.equals(naziv, that.naziv) && Objects.equals(osobine, that.osobine);
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public HashMap<String, String> getOsobine() {
        return osobine;
    }

    public void setOsobine(HashMap<String, String> osobine) {
        this.osobine = osobine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(naziv, osobine);
    }

    @Override
    public String toString() {
        return "Prostorija{" +
                "naziv='" + naziv + '\'' +
                ", osobine=" + osobine +
                '}' + "\n";
    }
}
