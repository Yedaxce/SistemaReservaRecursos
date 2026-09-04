package reservas.presentation.recursosP;

import reservas.logic.Recurso;
import reservas.presentation.AbstractTableModel;

import java.util.List;

public class RecursosTableModel extends AbstractTableModel<Recurso> {
    public static final int ID = 0;
    public static final int DESCRIPCION = 1;
    public static final int CATEGORIA = 2;

    public RecursosTableModel(int[] cols, List<Recurso> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID] = "Id";
        colNames[DESCRIPCION] = "Descripción";
        colNames[CATEGORIA] = "Categoría";
    }

    @Override
    protected Object getPropetyAt(Recurso e, int col) {
        switch (cols[col]) {
            case ID: return e.getId();
            case DESCRIPCION: return e.getDescripcion();
            case CATEGORIA: return e.getCategoria().getDescripcion();
            default: return "";
        }
    }
}
