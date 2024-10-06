package spec;



import java.util.List;

public abstract class ImportExportJson {

    private List<Termin> lista;

    public abstract boolean loadJson(String path,String datumi,String izuzetiDani);

    public abstract boolean exportJson(List<Termin> lista,String path);


    public List<Termin> getLista() {
        return lista;
    }

    public void setLista(List<Termin> lista) {
        this.lista = lista;
    }
}
