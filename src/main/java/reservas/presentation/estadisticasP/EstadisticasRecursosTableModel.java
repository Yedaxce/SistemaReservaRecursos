package reservas.presentation.estadisticasP;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EstadisticasRecursosTableModel extends AbstractTableModel {
    private final String[] cols = {"Categoría", "Cantidad"};
    private final List<Object[]> filas = new ArrayList<>();

    public void setDatos(Map<String, Integer> datos) {
        filas.clear();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            filas.add(new Object[]{entry.getKey(), entry.getValue()});
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() { return filas.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int col) { return cols[col]; }

    @Override
    public Object getValueAt(int row, int col) { return filas.get(row)[col]; }
}