package reservas.services;

import reservas.dao.CategoriaRecursoDAO;
import reservas.logic.CategoriaRecurso;

import java.util.List;

public class CategoriaRecursoService {
    private final CategoriaRecursoDAO categoriaDAO;

    public CategoriaRecursoService() {
        this.categoriaDAO = new CategoriaRecursoDAO();
    }

    public CategoriaRecurso buscarPorId(String id) {
        return categoriaDAO.buscarPorId(id);
    }

    public List<CategoriaRecurso> buscarPorDescripcion(String descripcion) {
        return categoriaDAO.buscarPorDescripcion(descripcion);
    }

    public List<CategoriaRecurso> listarTodos() {
        return categoriaDAO.listarTodos();
    }

    public CategoriaRecurso crear(String descripcion) {
        String nuevoId = categoriaDAO.generarNuevoId();
        CategoriaRecurso cat = new CategoriaRecurso(nuevoId, descripcion);
        categoriaDAO.guardar(cat);
        return cat;
    }

    public void actualizar(CategoriaRecurso categoria) {
        categoriaDAO.actualizar(categoria);
    }

    public void eliminar(String id) {
        categoriaDAO.eliminar(id);
    }
}