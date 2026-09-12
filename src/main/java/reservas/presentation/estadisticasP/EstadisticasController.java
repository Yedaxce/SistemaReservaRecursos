package reservas.presentation.estadisticasP;

import reservas.logic.model.Reserva;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

        this.view.getBtnFechaDesdeRecursos().addActionListener(e -> seleccionarFecha(view.getTxtFechaDesdeRecursos()));
        this.view.getBtnFechaHastaRecursos().addActionListener(e -> seleccionarFecha(view.getTxtJFechaHastaRecursos()));
        this.view.getBtnFechaDesdeActividades().addActionListener(e -> seleccionarFecha(view.getTxtFechaDesdeActividades()));
        this.view.getBntFechaHastaActividades().addActionListener(e -> seleccionarFecha(view.getTxtJFechaHastaActividades()));
    }

    private void seleccionarFecha(JTextField txtCampo) {
        String fechaActual = txtCampo.getText().trim();
        if (fechaActual.isEmpty()) {
            fechaActual = LocalDate.now().toString();
        }

        // Se asigna el resultado a una variable local en una sola llamada
        Object input = JOptionPane.showInputDialog(
                view,
                "Ingrese la fecha (AAAA-MM-DD):",
                "Seleccionar Fecha",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                fechaActual
        );

        if (input != null && !input.toString().trim().isEmpty()) {
            try {
                LocalDate parsed = LocalDate.parse(input.toString().trim());
                txtCampo.setText(parsed.toString());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(
                        view,
                        "Formato de fecha inválido. Debe ser AAAA-MM-DD.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void cargarEstadisticasRecursos() {
        try {
            LocalDate desde = LocalDate.parse(view.getTxtFechaDesdeRecursos().getText().trim());
            LocalDate hasta = LocalDate.parse(view.getTxtJFechaHastaRecursos().getText().trim());

            Map<String, Integer> datos = model.obtenerRecursosUsados(listaReservas, desde, hasta);
            view.actualizarRecursos(datos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Seleccione o ingrese fechas válidas (AAAA-MM-DD)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEstadisticasActividades() {
        try {
            LocalDate desde = LocalDate.parse(view.getTxtFechaDesdeActividades().getText().trim());
            LocalDate hasta = LocalDate.parse(view.getTxtJFechaHastaActividades().getText().trim());

            Map<String, Integer> datos = model.obtenerActividadesPorSemana(listaReservas, desde, hasta);
            view.actualizarActividades(datos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Seleccione o ingrese fechas válidas (AAAA-MM-DD)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}