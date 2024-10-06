package spec;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Teodor Jakovljevic,Marko Krivacevic
 *
 */


/**
 * klasa koja sadrzi podatke o svim terminima
 *
 */
public abstract class Raspored {

    /**
     * Funkcija koja inicijalizuje raposred i liste u kojima cuvamo podatke
     *
     */
    public abstract void initRaspored();

    /**
     * Funkcija u kojoj se dodaju nove prostorije
     *
     * @param prostorija ucionica koju zelimo da dodamo
     */
    public abstract void addProstorija(Prostorija prostorija);


    /**
     *Funkcija u kojoj se dodaje novi termin rasporedu
     *
     * @param termin termin koji zelimo da ubacimo u raspored
     */
    public abstract void addTermin(Termin termin);


    /**
     * Funkcija u kojoj se brise odredjeni termin iz rasporeda
     *
     * @param termin termin koji zelimo da izbrisemo iz rasporeda
     */
    public abstract void deleteTermin(Termin termin);


    /**
     * Funkcija u kojoj zelimo da zamenimo dva termina
     *
     * @param termin1 termin koji zelimo da uklonimo i na njegovo mesto stavimo drugi
     * @param termin2 termin koji zelimo da dodamo na mesto drugog termina
     */
    public abstract void moveTermin(Termin termin1,Termin termin2);


    /**
     * Funkcija koja proverava da li je odredjeni termin zauzet ili slobodan
     *
     * @param termin termin za koji zelimo da proverimo njegovu dostuponost
     */
    public abstract boolean checkTermin(Termin termin);


    /**
     *
     * @param putanja
     * @return
     */
    public abstract boolean importProstorija(String putanja,String configPath) throws IOException;


    public abstract boolean importSlobodnihTermina(String putanja,String datumi,String izuzetiDani) throws FileNotFoundException;

    /**
     * Funkcija u kojoj se pravi lista slobodnih termina
     *
     */

    /**
     *
     */
    public abstract void fillSlobodniTermini();

    /**
     *
     */
    public abstract void refreshList();

}
