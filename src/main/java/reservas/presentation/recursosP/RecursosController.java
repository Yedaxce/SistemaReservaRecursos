package reservas.presentation.recursosP;

import reservas.logic.model.CategoriaRecurso;
import reservas.logic.model.Recurso;
import reservas.services.CategoriaRecursoService;
import reservas.services.RecursoService;

import java.util.List;

public class RecursosController {

    private final RecursosView view;
    private final RecursosModel model;
    private final RecursoService service;
    private final CategoriaRecursoService categoriaService;

    public RecursosController(RecursosView view, RecursosModel model) {
        this.view = view;
        this.model = model;
        this.service = new RecursoService();
        this.categoriaService = new CategoriaRecursoService();
        view.setController(this);
        view.setModel(model);
        model.setCategorias(categoriaService.listarTodos()); // llena los combos
        model.setLista(service.listarTodos());   // carga inicial de la tabla
    }

    public void crear(Recurso r) throws Exception {
        service.crear(r);  // IllegalArgumentException (ID duplicado)
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

    /**
     * Filtra por categoría (usa el DAO/Service) y, si además se escribió texto
     * en el campo de descripción, refina el resultado en memoria --
     * RecursoService no tiene un buscarPorDescripcion()
     * en caso de implementarlo borrarlo aqui.
     */


    public void buscar(String descripcion, CategoriaRecurso categoria) {
        List<Recurso> base = service.listarPorCategoria(categoria.getId());

        if (descripcion == null || descripcion.isBlank()) {
            model.setLista(base);
        } else {
            String texto = descripcion.toLowerCase();
            model.setLista(base.stream()
                    .filter(r -> r.getDescripcion().toLowerCase().contains(texto))
                    .toList());
        }
    }

}
