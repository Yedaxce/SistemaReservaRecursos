package reservas.presentation.categoriasRecurP;

import reservas.logic.CategoriaRecurso;
import reservas.services.CategoriaRecursoService;

public class CategoriasController {

    private final CategoriasView view;
    private final CategoriasModel model;
    private final CategoriaRecursoService service;

    public CategoriasController(CategoriasView view, CategoriasModel model) {
        this.view = view;
        this.model = model;
        this.service = new CategoriaRecursoService();
        view.setController(this);
        view.setModel(model);
        model.setLista(service.listarTodos());
    }

    // ID generado por Service (categoriaDAO.generarNuevoId())
    public void crear(String descripcion) {
        service.crear(descripcion);
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    public void actualizar(CategoriaRecurso categoria) {
        service.actualizar(categoria);
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

    // buscarPorDescripcion ya existe en el Service
    public void buscar(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            model.setLista(service.listarTodos());
        } else {
            model.setLista(service.buscarPorDescripcion(descripcion));
        }
    }
}

