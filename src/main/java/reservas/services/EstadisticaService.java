package reservas.services;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import reservas.dao.ReservaDAO;
import reservas.logic.CategoriaRecurso;
import reservas.logic.Recurso;
import reservas.logic.Reserva;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

public class EstadisticaService {

    private final ReservaDAO reservaDAO;

    public EstadisticaService() {
        this.reservaDAO = new ReservaDAO();
    }


    // 1. MÉTODOS DE LÓGICA DE DATOS
    /**Calcula la cantidad de usos por categoría de recurso en un período dado.*/
    public Map<CategoriaRecurso, Integer> recursosUsadosEnPeriodo(LocalDate desde, LocalDate hasta) {
        Map<CategoriaRecurso, Integer> resultado = new HashMap<>();

        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            return resultado;
        }

        List<Reserva> reservasEnPeriodo = reservaDAO.listarPorRangoFechas(desde, hasta);

        for (Reserva reserva : reservasEnPeriodo) {
            if (reserva.estaActiva()) {
                for (Recurso recurso : reserva.getRecursos()) {
                    if (recurso != null && recurso.getCategoria() != null) {
                        CategoriaRecurso cat = recurso.getCategoria();
                        resultado.put(cat, resultado.getOrDefault(cat, 0) + 1);
                    }
                }
            }
        }

        return resultado;
    }

    /**Cuenta cantidad de actividades reservadas por semanaa*/
    public Map<String, Integer> actividadesPorSemana(LocalDate desde, LocalDate hasta) {
        Map<String, Integer> resultado = new TreeMap<>(); // Orden cronológico automático

        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            return resultado;
        }

        List<Reserva> reservasEnPeriodo = reservaDAO.listarPorRangoFechas(desde, hasta);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (Reserva reserva : reservasEnPeriodo) {
            if (reserva.estaActiva()) {
                LocalDate fechaReserva = reserva.getFecha();
                LocalDate inicioSemana = fechaReserva.with(weekFields.dayOfWeek(), 1);
                String claveSemana = inicioSemana.toString();

                resultado.put(claveSemana, resultado.getOrDefault(claveSemana, 0) + 1);
            }
        }

        return resultado;
    }

    // 2. MÉTODOS DE GENERACIÓN DE GRÁFICOS
    /** Genera el objeto JFreeChart de Barras para la cantidad de Recursos Usados.*/
    public JFreeChart generarGraficoRecursos(LocalDate desde, LocalDate hasta) {
        Map<CategoriaRecurso, Integer> datos = recursosUsadosEnPeriodo(desde, hasta);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<CategoriaRecurso, Integer> entry : datos.entrySet()) {
            dataset.addValue(entry.getValue(), "Recursos", entry.getKey().getDescripcion());
        }

        return ChartFactory.createBarChart(
                "Recursos Usados",       // Título del gráfico
                "Categoría",             // Etiqueta Eje X
                "Cantidad",              // Etiqueta Eje Y
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    public JFreeChart generarGraficoActividades(LocalDate desde, LocalDate hasta) {
        Map<String, Integer> datos = actividadesPorSemana(desde, hasta);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.addValue(entry.getValue(), "Semana", entry.getKey());
        }

        return ChartFactory.createBarChart(
                "Actividades Realizadas", // Título del gráfico
                "Semana",                 // Etiqueta Eje X
                "Cantidad",               // Etiqueta Eje Y
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }
}