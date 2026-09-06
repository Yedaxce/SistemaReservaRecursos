package reservas.dao;

import reservas.logic.model.CategoriaRecurso;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRecursoDAO {

    public CategoriaRecursoDAO() {}

    public CategoriaRecurso buscarPorId(String id) {
        if (id == null) return null;
        for (CategoriaRecurso c : listarTodos()) {
            if (c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    public List<CategoriaRecurso> buscarPorDescripcion(String descripcion) {
        List<CategoriaRecurso> res = new ArrayList<>();
        if (descripcion == null) return res;
        for (CategoriaRecurso c : listarTodos()) {
            if (c.getDescripcion() != null && c.getDescripcion().toLowerCase().contains(descripcion.toLowerCase())) {
                res.add(c);
            }
        }
        return res;
    }

    public List<CategoriaRecurso> listarTodos() {
        return cargarDesdeXML();
    }

    public void guardar(CategoriaRecurso categoria) {
        try {
            Data data = XmlPersister.instance().load();
            data.getCategorias().add(categoria);
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar(CategoriaRecurso categoria) {
        try {
            Data data = XmlPersister.instance().load();
            List<CategoriaRecurso> lista = data.getCategorias();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equalsIgnoreCase(categoria.getId())) {
                    lista.set(i, categoria);
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
            data.getCategorias().removeIf(c -> c.getId().equalsIgnoreCase(id));
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generarNuevoId() {
        List<CategoriaRecurso> lista = listarTodos();
        int max = 0;
        for (CategoriaRecurso c : lista) {
            if (c.getId() != null && c.getId().startsWith("CAT-")) {
                try {
                    int num = Integer.parseInt(c.getId().substring(4));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return String.format("CAT-%06d", max + 1);
    }

    private List<CategoriaRecurso> cargarDesdeXML() {
        try {
            return XmlPersister.instance().load().getCategorias();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private void guardarEnXML(List<CategoriaRecurso> lista) {
        try {
            Data data = XmlPersister.instance().load();
            data.setCategorias(lista);
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}