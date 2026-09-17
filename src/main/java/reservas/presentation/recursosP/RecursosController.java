package reservas.presentation.recursosP;

import reservas.logic.CategoriaRecurso;
import reservas.logic.Recurso;
import reservas.services.CategoriaRecursoService;
import reservas.services.RecursoService;
import reservas.services.GenerarPdfService;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class RecursosController {

    private final RecursosView view;
    private final RecursosModel model;
    private final RecursoService service;
    private final CategoriaRecursoService categoriaService;
    private final GenerarPdfService pdfService;

    public RecursosController(RecursosView view, RecursosModel model) {
        this.view = view;
        this.model = model;
        this.service = new RecursoService();
        this.categoriaService = new CategoriaRecursoService();
        this.pdfService = new GenerarPdfService();
        view.setController(this);
        view.setModel(model);
        model.setCategorias(categoriaService.listarTodos());
        model.setLista(service.listarTodos());
    }

    public void cargarCategorias() {
        model.setCategorias(categoriaService.listarTodos());
    }

    public void crear(Recurso r) throws Exception {
        service.crear(r);
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    public void actualizar(Recurso r) throws Exception {
        service.actualizar(r);
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    public void eliminar(String id) {
        service.eliminar(id);
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    public void limpiar() {
        model.setActual(null);
    }

    public void seleccionar(int row) {
        if (row >= 0 && row < model.getLista().size()) {
            model.setActual(model.getLista().get(row));
        }
    }


    public void buscar(String descripcion, CategoriaRecurso categoria) {
        if (categoria == null) {
            // Solo pasa si no hay ninguna categoría cargada
            model.setLista(new ArrayList<>());
            return;
        }
        if (descripcion == null || descripcion.isBlank()) {
            model.setLista(service.listarPorCategoria(categoria.getId()));
        } else {
            model.setLista(service.buscarPorDescripcion(descripcion).stream()
                    .filter(r -> r.getCategoria().getId().equals(categoria.getId()))
                    .toList());
        }
    }

    public void imprimir(String rutaSalida) throws IOException {
        String[] encabezados = {"Id", "Descripción", "Categoría"};
        List<Object[]> filas = model.getLista().stream()
                .map(r -> new Object[]{r.getId(), r.getDescripcion(), r.getCategoria().getDescripcion()})
                .toList();
        pdfService.generarPdf("Listado de Recursos", encabezados, filas, rutaSalida);
    }
}
