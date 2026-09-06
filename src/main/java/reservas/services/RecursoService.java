package reservas.services;

import reservas.dao.CategoriaRecursoDAO;
import reservas.dao.RecursoDAO;
import reservas.logic.model.Recurso;

import java.util.List;

public class RecursoService {
    private final RecursoDAO recursoDAO;
    private final CategoriaRecursoDAO categoriaDAO;

    public RecursoService() {
        this.recursoDAO = new RecursoDAO();
        this.categoriaDAO = new CategoriaRecursoDAO();
    }

    public Recurso buscarPorId(String id) {
        return recursoDAO.buscarPorId(id);
    }

    public List<Recurso> buscarPorDescripcion(String descripcion) {
        return recursoDAO.buscarPorDescripcion(descripcion);
    }

    public List<Recurso> listarPorCategoria(String idCategoria) {
        return recursoDAO.listarPorCategoria(idCategoria);
    }

    public List<Recurso> listarTodos() {
        return recursoDAO.listarTodos();
    }

    public void crear(Recurso recurso) {
        if (recursoDAO.buscarPorId(recurso.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un recurso con el activo/ID: " + recurso.getId());
        }
        recursoDAO.guardar(recurso);
    }

    public void actualizar(Recurso recurso) {
        recursoDAO.actualizar(recurso);
    }

    public void eliminar(String id) {
        recursoDAO.eliminar(id);
    }
}