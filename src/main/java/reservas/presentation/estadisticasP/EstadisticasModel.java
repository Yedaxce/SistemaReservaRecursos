package reservas.presentation.estadisticasP;

import reservas.logic.Recurso;
import reservas.logic.Reserva;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasModel {

    public Map<String, Integer> obtenerRecursosUsados(List<Reserva> reservas, LocalDate desde, LocalDate hasta) {
        Map<String, Integer> conteo = new LinkedHashMap<>();

        for (Reserva r : reservas) {
            LocalDate f = r.getFecha();
            if ((f.isEqual(desde) || f.isAfter(desde)) && (f.isEqual(hasta) || f.isBefore(hasta))) {
                // Obtiene la lista de recursos asociada a la reserva
                List<Recurso> listaRecursos = r.getRecursos();

                if (listaRecursos != null) {
                    for (Recurso recurso : listaRecursos) {
                        if (recurso != null && recurso.getCategoria() != null) {
                            String nombreCategoria = recurso.getCategoria().getDescripcion();
                            conteo.put(nombreCategoria, conteo.getOrDefault(nombreCategoria, 0) + 1);
                        }
                    }
                }
            }
        }
        return conteo;
    }

    public Map<String, Integer> obtenerActividadesPorSemana(List<Reserva> reservas, LocalDate desde, LocalDate hasta) {
        Map<String, Integer> conteoSemanal = new LinkedHashMap<>();

        for (Reserva r : reservas) {
            LocalDate f = r.getFecha();
            if ((f.isEqual(desde) || f.isAfter(desde)) && (f.isEqual(hasta) || f.isBefore(hasta))) {
                LocalDate lunes = f.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                String claveSemana = lunes.toString();
                conteoSemanal.put(claveSemana, conteoSemanal.getOrDefault(claveSemana, 0) + 1);
            }
        }
        return conteoSemanal;
    }
}