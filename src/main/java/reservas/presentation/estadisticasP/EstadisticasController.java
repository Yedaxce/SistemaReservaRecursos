package reservas.presentation.estadisticasP;

import reservas.logic.model.Reserva;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EstadisticasController {

    private final EstadisticasView view;
    private final EstadisticasModel model;
    private final List<Reserva> listaReservas;

    public EstadisticasController(EstadisticasView view, EstadisticasModel model, List<Reserva> listaReservas) {
        this.view = view;
        this.model = model;
        this.listaReservas = listaReservas;

        initListeners();
    }

    private void initListeners() {
        // Eventos para cargar estadísticas
        this.view.getBtnCargarRecursos().addActionListener(e -> cargarEstadisticasRecursos());
        this.view.getBtnCargarActividades().addActionListener(e -> cargarEstadisticasActividades());

    }

    private void cargarEstadisticasRecursos() {
        try {
            LocalDate desde = view.getDpFechaDesdeRecursos().getDate();
            LocalDate hasta = view.getDpFechaHastaRecursos().getDate();
            validarRango(desde, hasta);

            Map<String, Integer> datos = model.obtenerRecursosUsados(listaReservas, desde, hasta);
            view.actualizarRecursos(datos);
        } catch (Exception ex) {
            mostrarError("Seleccione un rango de fechas válido.");
        }
    }

    private void cargarEstadisticasActividades() {
        try {
            LocalDate desde = view.getDpFechaDesdeActividades().getDate();
            LocalDate hasta = view.getDpFechaHastaActividades().getDate();
            validarRango(desde, hasta);

            Map<String, Integer> datos = model.obtenerActividadesPorSemana(listaReservas, desde, hasta);
            view.actualizarActividades(datos);
        } catch (Exception ex) {
            mostrarError("Seleccione un rango de fechas válido.");
        }
    }

    private void validarRango(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new IllegalArgumentException("Rango de fechas inválido");
        }
    }

    private void mostrarError(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(view, mensaje, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}