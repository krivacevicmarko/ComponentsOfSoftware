package spec;


import java.io.IOException;
import java.util.*;

/**
 *
 * @author Teodor Jakovljevic,Marko Krivacevic
 *
 */


/**
 * Klasa koja sluzi za import i export CSV i Json fajlova
 *
 */
public abstract class ImportExport {
    private List<Termin> listaTermina;


    public abstract boolean loadData(String s, String s1) throws IOException;

    public abstract boolean exportData(String s) throws IOException;

    /**
     * funkcija koja se koristi za ucitavanje podataka iz CSV fajla
     *
     * @param path putanja do csv fajla koji zelimo da ucitamo
     * @param configPath putanja do konfiguracionog fajla
     * @return boolean
     * @throws IOException
     */
    public abstract boolean loadData(String path, String configPath,String datumi,String izuzetiDani) throws IOException;

    /**
     * Funkcija koja se koristi za eksportovanje podataka u txt fajlu
     *
     * @param path putanja do fajla u kom zelimo da ispisemo listu termina
     * @throws IOException
     */
    public abstract boolean exportData(List<Termin> lista,String path) throws IOException;

    public List<Termin> getListaTermina() {
        if (listaTermina == null)
            listaTermina = new ArrayList<>();
        return listaTermina;
    }
 public void setListaTermina(List<Termin> listaTermina){
        this.listaTermina = listaTermina;
 }
}
