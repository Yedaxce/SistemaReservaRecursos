package reservas.presentation.reservasP;

import reservas.logic.model.CategoriaRecurso;
import reservas.logic.model.Funcionario;
import reservas.services.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservasController {

    private final ReservasView view;
    private final ReservasModel model;
    private final ReservaService reservaService;
    private final CategoriaRecursoService categoriaService;
    private final IAService iaService;
    private final GenerarPdfService pdfService;

    public ReservasController(ReservasView view, ReservasModel model, Funcionario usuarioLogueado) {
        this.view = view;
        this.model = model;
        this.reservaService = new ReservaService();
        this.categoriaService = new CategoriaRecursoService();
        this.iaService = new IAService();
        this.pdfService = new GenerarPdfService();

        model.setFuncionarioLogueado(usuarioLogueado);
        view.setController(this);
        view.setModel(model);

        model.setCategoriasDisponibles(categoriaService.listarTodos());
        refrescarTabla();
    }

    public void extraerConIA(String frase) {
        DatosReservaDTO dto = iaService.extraerDatosReserva(frase);

        List<CategoriaRecurso> seleccionadas = new ArrayList<>();
        for (String catNombre : dto.getCategorias()) {
            model.getCategoriasDisponibles().stream()
                    .filter(c -> c.getDescripcion().equalsIgnoreCase(catNombre))
                    .findFirst()
                    .ifPresent(seleccionadas::add);
        }

        model.setFrase(frase);
        model.actualizarFormulario(
                dto.getActividad(),
                dto.getFecha(),
                dto.getHoraInicio(),
                dto.getHoraFin(),
                seleccionadas
        );
    }

    public void crearReserva(String actividad, LocalDate fecha, LocalTime hInicio, LocalTime hFin, List<CategoriaRecurso> categorias) {
        ResultadoReserva resultado = reservaService.crearReserva(
                actividad, fecha, hInicio, hFin, model.getFuncionarioLogueado(), categorias
        );

        if (resultado.isExitosa()) {
            refrescarTabla();
            limpiarFormulario();
            view.mostrarMensajeInfo(resultado.getMensaje());
        } else {
            view.mostrarMensajeError(resultado.getMensaje());
        }
    }

    public void cancelarReserva(int filaSeleccionada) {
        if (filaSeleccionada >= 0 && filaSeleccionada < model.getMisReservas().size()) {
            String idReserva = model.getMisReservas().get(filaSeleccionada).getId();
            boolean exito = reservaService.cancelarReserva(idReserva);
            if (exito) {
                refrescarTabla();
                view.mostrarMensajeInfo("Reserva " + idReserva + " cancelada exitosamente.");
            } else {
                view.mostrarMensajeError("No se pudo cancelar la reserva seleccionada.");
            }
        }
    }

    public void refrescarTabla() {
        if (model.getFuncionarioLogueado() != null) {
            model.setMisReservas(reservaService.listarPorFuncionario(model.getFuncionarioLogueado().getId()));
        }
    }

    public void limpiarFormulario() {
        model.actualizarFormulario("", LocalDate.now(), null, null, new ArrayList<>());
    }

    public void imprimirReporte(String ruta) throws IOException {
        String[] encabezados = {"Id", "Actividad", "Fecha", "Horario", "Recursos", "Estado"};
        ReservasTableModel tableModel = new ReservasTableModel(model.getMisReservas());
        List<Object[]> filas = new ArrayList<>();

        for (int r = 0; r < tableModel.getRowCount(); r++) {
            Object[] fila = new Object[tableModel.getColumnCount()];
            for (int c = 0; c < tableModel.getColumnCount(); c++) {
                fila[c] = tableModel.getValueAt(r, c);
            }
            filas.add(fila);
        }
        pdfService.generarPdf("Mis Reservas - " + model.getFuncionarioLogueado().getNombre(), encabezados, filas, ruta);
    }
}