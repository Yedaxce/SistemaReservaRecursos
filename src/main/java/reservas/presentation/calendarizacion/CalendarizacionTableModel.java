package reservas.presentation.calendarizacion;

import reservas.logic.model.Recurso;
import reservas.logic.model.Reserva;

import javax.swing.table.AbstractTableModel;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarizacionTableModel extends AbstractTableModel {

    private final List<LocalTime> horas;
    private final List<Recurso> recursos;
    private final List<Reserva> reservas;

    public CalendarizacionTableModel(List<Recurso> recursos, List<Reserva> reservas) {
        this.recursos = recursos;
        this.reservas = reservas;
        this.horas = generarHoras();
    }

    private List<LocalTime> generarHoras() {
        List<LocalTime> lista = new ArrayList<>();
        for (int h = 6; h <= 20; h++) {
            lista.add(LocalTime.of(h, 0));
        }
        return lista;
    }

    @Override
    public int getRowCount() {
        return horas.size();
    }

    @Override
    public int getColumnCount() {
        return 1 + recursos.size(); // Columna 0 es "Hora", las demás son los Recursos
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) return "Hora";
        return recursos.get(col - 1).getDescripcion();
    }

    @Override
    public Object getValueAt(int row, int col) {
        LocalTime hora = horas.get(row);
        if (col == 0) return hora.toString();

        Recurso recurso = recursos.get(col - 1);

        return reservas.stream()
                .filter(r -> r.estaActiva() &&
                        r.getRecursos().stream().anyMatch(rec -> rec.getId().equals(recurso.getId())) &&
                        !hora.isBefore(r.getHoraInicio()) && hora.isBefore(r.getHoraFin()))
                .map(r -> r.getActividad() + " - " + r.getFuncionario().getNombre())
                .findFirst()
                .orElse("");
    }
}