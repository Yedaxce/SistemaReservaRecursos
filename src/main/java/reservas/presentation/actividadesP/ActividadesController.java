package reservas.presentation.actividadesP;

import reservas.logic.Reserva;
import reservas.services.GenerarPdfService;
import reservas.services.ReservaService;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActividadesController {

    private final ActividadesView view;
    private final ActividadesModel model;
    private final ReservaService reservaService;
    private final GenerarPdfService pdfService;

    public ActividadesController(ActividadesView view, ActividadesModel model) {
        this.view = view;
        this.model = model;
        this.reservaService = new ReservaService();
        this.pdfService = new GenerarPdfService();

        this.view.setController(this);
        this.view.setModel(model);

        LocalDate fechaInicial = model.getFechaReferencia() != null ? model.getFechaReferencia() : LocalDate.now();
        cargarActividades(fechaInicial);
    }

    public void cargarActividades(LocalDate fecha) {
        if (fecha == null) {
            fecha = LocalDate.now();
        }

        model.setFechaReferencia(fecha);
        LocalDate inicioSemana = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate finSemana = inicioSemana.plusDays(6);

        //Obtiene todas las reservas registradas
        List<Reserva> todas = reservaService.listarTodas();

        //Filtra inicamente las reservas dentro del rango MONDAY - SUNDAY de la semana seleccionada
        List<Reserva> reservasSemana = todas.stream()
                .filter(r -> r.estaActiva())
                .filter(r -> !r.getFecha().isBefore(inicioSemana) && !r.getFecha().isAfter(finSemana))
                .collect(Collectors.toList());

        model.setReservas(reservasSemana);
    }

    public void imprimirReporte(String ruta) throws IOException {
        ActividadesTableModel tableModel = new ActividadesTableModel(model.getFechaReferencia(), model.getReservas());

        String[] encabezados = new String[tableModel.getColumnCount()];
        for (int c = 0; c < tableModel.getColumnCount(); c++) {
            encabezados[c] = tableModel.getColumnName(c);
        }

        List<Object[]> filas = new ArrayList<>();
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            Object[] fila = new Object[tableModel.getColumnCount()];
            for (int c = 0; c < tableModel.getColumnCount(); c++) {
                fila[c] = tableModel.getValueAt(r, c);
            }
            filas.add(fila);
        }

        pdfService.generarPdf("Actividades Semanales", encabezados, filas, ruta);
    }
}