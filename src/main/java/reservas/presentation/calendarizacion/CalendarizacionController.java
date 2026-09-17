package reservas.presentation.calendarizacion;

import reservas.logic.CategoriaRecurso;
import reservas.logic.Recurso;
import reservas.logic.Reserva;
import reservas.services.CategoriaRecursoService;
import reservas.services.GenerarPdfService;
import reservas.services.RecursoService;
import reservas.services.ReservaService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarizacionController {

    private final CalendarizacionView view;
    private final CalendarizacionModel model;
    private final CategoriaRecursoService categoriaService;
    private final RecursoService recursoService;
    private final ReservaService reservaService;
    private final GenerarPdfService pdfService;

    public CalendarizacionController(CalendarizacionView view, CalendarizacionModel model) {
        this.view = view;
        this.model = model;
        this.categoriaService = new CategoriaRecursoService();
        this.recursoService = new RecursoService();
        this.reservaService = new ReservaService();
        this.pdfService = new GenerarPdfService();

        view.setController(this);
        view.setModel(model);

        model.setCategorias(categoriaService.listarTodos());
    }



    public void cargarCalendarizacion(LocalDate fecha, CategoriaRecurso categoria) {
        if (fecha == null || categoria == null) return;

        model.setFecha(fecha);
        model.setCategoriaSeleccionada(categoria);

        List<Recurso> recursos = recursoService.listarPorCategoria(categoria.getId());
        List<Reserva> reservas = reservaService.buscarPorFechaYCategoria(fecha, categoria.getId());

        model.setDatosTabla(recursos, reservas);
    }

    public void cargarCategorias() {
        model.setCategorias(categoriaService.listarTodos());
    }

    public void imprimirReporte(String ruta) throws IOException {
        String[] encabezados = new String[1 + model.getRecursos().size()];
        encabezados[0] = "Hora";
        for (int i = 0; i < model.getRecursos().size(); i++) {
            encabezados[i + 1] = model.getRecursos().get(i).getDescripcion();
        }

        CalendarizacionTableModel tableModel = new CalendarizacionTableModel(model.getRecursos(), model.getReservas());
        List<Object[]> filas = new ArrayList<>();

        for (int r = 0; r < tableModel.getRowCount(); r++) {
            Object[] fila = new Object[tableModel.getColumnCount()];
            for (int c = 0; c < tableModel.getColumnCount(); c++) {
                fila[c] = tableModel.getValueAt(r, c);
            }
            filas.add(fila);
        }

        pdfService.generarPdf("Calendarización de Recursos", encabezados, filas, ruta);
    }



}
