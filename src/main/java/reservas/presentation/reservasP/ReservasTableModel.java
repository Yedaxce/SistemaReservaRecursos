package reservas.presentation.reservasP;

import reservas.logic.Recurso;
import reservas.logic.Reserva;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class ReservasTableModel extends AbstractTableModel {
    public static final int ID = 0;
    public static final int ACTIVIDAD = 1;
    public static final int FECHA = 2;
    public static final int HORARIO = 3;
    public static final int RECURSOS = 4;
    public static final int ESTADO = 5;

    private final String[] cols = {"Id", "Actividad", "Fecha", "Horario", "Recursos", "Estado"};
    private final List<Reserva> filas;

    public ReservasTableModel(List<Reserva> filas) {
        this.filas = filas;
    }

    @Override
    public int getRowCount() { return filas.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int col) { return cols[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        Reserva r = filas.get(row);
        switch (col) {
            case ID: return r.getId();
            case ACTIVIDAD: return r.getActividad();
            case FECHA: return r.getFecha().toString();
            case HORARIO: return r.getHoraInicio() + " - " + r.getHoraFin();
            case RECURSOS:
                return r.getRecursos().stream()
                        .map(Recurso::getId)
                        .collect(Collectors.joining(", "));
            case ESTADO: return r.estaActiva() ? "ACTIVA" : "CANCELADA";
            default: return "";
        }
    }
}