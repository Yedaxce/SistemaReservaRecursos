package reservas.services;

import reservas.dao.ReservaDAO;
import reservas.logic.CategoriaRecurso;
import reservas.logic.Recurso;
import reservas.logic.Reserva;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticaService {
    private final ReservaDAO reservaDAO;

    public EstadisticaService() {
        this.reservaDAO = new ReservaDAO();
    }

    public Map<CategoriaRecurso, Integer> recursosUsadosEnPeriodo(LocalDate desde, LocalDate hasta) {
        Map<CategoriaRecurso, Integer> conteo = new HashMap<>();
        List<Reserva> reservas = reservaDAO.listarPorRangoFechas(desde, hasta);

        for (Reserva r : reservas) {
            if (r.estaActiva()) {
                for (Recurso rec : r.getRecursos()) {
                    CategoriaRecurso cat = rec.getCategoria();
                    if (cat != null) {
                        conteo.put(cat, conteo.getOrDefault(cat, 0) + 1);
                    }
                }
            }
        }
        return conteo;
    }

    public Map<String, Integer> actividadesPorSemana(LocalDate desde, LocalDate hasta) {
        Map<String, Integer> conteo = new HashMap<>();
        List<Reserva> reservas = reservaDAO.listarPorRangoFechas(desde, hasta);

        for (Reserva r : reservas) {
            if (r.estaActiva()) {
                String semanaKey = r.getFecha().toString(); // Agrupado por fecha/semana
                conteo.put(semanaKey, conteo.getOrDefault(semanaKey, 0) + 1);
            }
        }
        return conteo;
    }
}