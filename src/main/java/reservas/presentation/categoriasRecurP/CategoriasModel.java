package reservas.presentation.categoriasRecurP;

import reservas.logic.model.CategoriaRecurso;
import reservas.presentation.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriasModel extends AbstractModel {

    private CategoriaRecurso actual;
    private List<CategoriaRecurso> lista = new ArrayList<>();

    public static final String ACTUAL = "actual";
    public static final String LISTA = "lista";

    public CategoriaRecurso getActual() {
        return actual;
    }

    public void setActual(CategoriaRecurso actual) {
        this.actual = actual;
        firePropertyChange(ACTUAL);
    }

    public List<CategoriaRecurso> getLista() {
        return lista;
    }

    public void setLista(List<CategoriaRecurso> lista) {
        this.lista = lista;
        firePropertyChange(LISTA);
    }
}

