package reservas.dao;

import reservas.logic.Recurso;

import java.util.ArrayList;
import java.util.List;

public class RecursoDAO {

    public RecursoDAO() {}

    public Recurso buscarPorId(String id) {
        if (id == null) return null;
        for (Recurso r : listarTodos()) {
            if (r.getId().equalsIgnoreCase(id)) return r;
        }
        return null;
    }

    public List<Recurso> buscarPorDescripcion(String descripcion) {
        List<Recurso> res = new ArrayList<>();
        if (descripcion == null) return res;
        for (Recurso r : listarTodos()) {
            if (r.getDescripcion().toLowerCase().contains(descripcion.toLowerCase())) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Recurso> listarPorCategoria(String idCategoria) {
        List<Recurso> res = new ArrayList<>();
        if (idCategoria == null) return res;
        for (Recurso r : listarTodos()) {
            if (r.getCategoria() != null && r.getCategoria().getId().equalsIgnoreCase(idCategoria)) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Recurso> listarTodos() {
        return cargarDesdeXML();
    }

    public void guardar(Recurso recurso) {
        try {
            Data data = XmlPersister.instance().load();
            data.getRecursos().add(recurso);
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Recurso recurso) {
        try {
            Data data = XmlPersister.instance().load();
            List<Recurso> lista = data.getRecursos();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equalsIgnoreCase(recurso.getId())) {
                    lista.set(i, recurso);
                    break;
                }
            }
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(String id) {
        try {
            Data data = XmlPersister.instance().load();
            data.getRecursos().removeIf(r -> r.getId().equalsIgnoreCase(id));
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Recurso> cargarDesdeXML() {
        try {
            return XmlPersister.instance().load().getRecursos();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private void guardarEnXML(List<Recurso> lista) {
        try {
            Data data = XmlPersister.instance().load();
            data.setRecursos(lista);
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}