package reservas.presentation.funcionarioP;

import reservas.logic.Funcionario;
import reservas.presentation.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public class FuncionarioModel extends AbstractModel {

    private Funcionario actual;
    private List<Funcionario> lista = new ArrayList<>();

    public static final String ACTUAL = "actual";
    public static final String LISTA = "lista";

    public Funcionario getActual() {
        return actual;
    }

    public void setActual(Funcionario current) {
        this.actual = current;
        firePropertyChange(ACTUAL);
    }

    public List<Funcionario> getLista() {
        return lista;
    }


    public void setLista(List<Funcionario> list) {
        this.lista = list;
        firePropertyChange(LISTA);
    }

}
