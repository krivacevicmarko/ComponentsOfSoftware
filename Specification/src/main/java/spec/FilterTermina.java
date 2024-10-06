package spec;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Teodor Jakovljevic,Marko Krivacevic
 *
 */


/**
 * Klasa koja sadrzi funkcije za izlistavanje i filtriranje termina po zeljenim kriterijumima
 *
 */
public abstract class FilterTermina {

    /**
     * Lista koju cemo koristiti za filtriranje
     *
     */
    private List<Termin> listaTermina;

    /**
     * funkcija koja izlistava i prikazuje listu zauzetih termina
     *
     * @param listaTermina lista za koju zelimo da izlistamo zauzete termine
     */
    public abstract void izlistajZauzeteTermine(List<Termin> listaTermina);

    /**
     * funkcija koja izlistava i prikazuje listu slobodnih termina
     *
     * @param listaTermina lista za koju zelimo da izlistamo slobodne termine
     */
    public abstract void izlistajSlobodneTermine(List<Termin> listaTermina);

    /**
     * funmcija koja filtrira listu termina po zeljenim kriterijumima
     *
     * @param listaTermina lista koju zelimo da filtriramo
     * @param tip parametar po kom zelimo da filtriramo listu
     * @param parametar parametar koji zelimo da filtriramo
     */
    public abstract List<Termin> filtrirajTermine(List<Termin> listaTermina,String tip,String parametar);

    public List<Termin> getListaTermina(){
        if(listaTermina==null)
            listaTermina = new ArrayList<>();
        return listaTermina;
    }

    public void setListaTermina(List<Termin> listaTermina) {
        this.listaTermina = listaTermina;
    }
}
