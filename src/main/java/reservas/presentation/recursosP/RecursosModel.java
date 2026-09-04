package reservas.presentation.recursosP;

import reservas.logic.CategoriaRecurso;
import reservas.logic.Recurso;
import reservas.presentation.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public class RecursosModel extends AbstractModel {
    private Recurso actual;
    private List<Recurso> lista = new ArrayList<>();
    private List<CategoriaRecurso> categorias = new ArrayList<>();

    public static final String ACTUAL = "actual";
    public static final String LISTA = "lista";
    public static final String CATEGORIAS = "categorias";

    public Recurso getActual() {
        return actual;
    }

    public void setActual(Recurso actual) {
        this.actual = actual;
        firePropertyChange(ACTUAL);
    }

    public List<Recurso> getLista() {
        return lista;
    }

    public void setLista(List<Recurso> lista) {
        this.lista = lista;
        firePropertyChange(LISTA);
    }

    public List<CategoriaRecurso> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaRecurso> categorias) {
        this.categorias = categorias;
        firePropertyChange(CATEGORIAS);
    }
}
