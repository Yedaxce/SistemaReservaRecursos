package reservas.presentation.actividadesP;

import reservas.logic.Reserva;

import javax.swing.table.AbstractTableModel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class ActividadesTableModel extends AbstractTableModel {

    private final List<LocalTime> horas;
    private final List<LocalDate> diasSemana;
    private final List<Reserva> reservas;

    public ActividadesTableModel(LocalDate fechaReferencia, List<Reserva> reservas) {
        this.reservas = reservas;
        this.horas = generarHoras();
        this.diasSemana = calcularDiasSemana(fechaReferencia);
    }

    private List<LocalTime> generarHoras() {
        List<LocalTime> lista = new ArrayList<>();
        for (int h = 6; h <= 20; h++) {
            lista.add(LocalTime.of(h, 0));
        }
        return lista;
    }

    private List<LocalDate> calcularDiasSemana(LocalDate ref) {
        List<LocalDate> dias = new ArrayList<>();
        LocalDate inicioSemana = ref.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int i = 0; i < 7; i++) {
            dias.add(inicioSemana.plusDays(i));
        }
        return dias;
    }

    @Override
    public int getRowCount() {
        return horas.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) return "Hora";
        LocalDate dia = diasSemana.get(col - 1);
        String nombreDia = switch (dia.getDayOfWeek()) {
            case MONDAY -> "lun";
            case TUESDAY -> "mar";
            case WEDNESDAY -> "mié";
            case THURSDAY -> "jue";
            case FRIDAY -> "vie";
            case SATURDAY -> "sáb";
            case SUNDAY -> "dom";
        };
        return nombreDia + " " + dia.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public Object getValueAt(int row, int col) {
        LocalTime hora = horas.get(row);
        if (col == 0) return hora.toString();

        LocalDate dia = diasSemana.get(col - 1);

        return reservas.stream()
                .filter(r -> r.estaActiva() &&
                        r.getFecha() != null &&
                        r.getFecha().equals(dia) &&
                        !hora.isBefore(r.getHoraInicio()) && hora.isBefore(r.getHoraFin()))
                .map(r -> {
                    String nombreFuncionario = (r.getFuncionario() != null && r.getFuncionario().getNombre() != null)
                            ? r.getFuncionario().getNombre()
                            : "Sin asignar";
                    return r.getActividad() + " (" + nombreFuncionario + ")";
                })
                .findFirst()
                .orElse("");
    }

}