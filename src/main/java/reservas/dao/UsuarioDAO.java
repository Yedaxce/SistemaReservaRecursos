package reservas.dao;

import reservas.logic.model.Usuario;
import java.util.List;

public class UsuarioDAO {

    public UsuarioDAO() {}

    public Usuario buscarPorId(String id) {
        if (id == null || id.isBlank()) return null;
        for (Usuario u : listarTodos()) {
            if (u.getId().equalsIgnoreCase(id)) return u;
        }
        return null;
    }

    public Usuario validarLogin(String id, String clave) {
        Usuario u = buscarPorId(id);
        if (u != null && u.validarClave(clave)) {
            return u;
        }
        return null;
    }

    public boolean actualizarClave(String id, String nuevaClave) {
        try {
            Data data = XmlPersister.instance().load();
            for (Usuario u : data.getUsuarios()) {
                if (u.getId().equalsIgnoreCase(id)) {
                    u.setClave(nuevaClave);
                    XmlPersister.instance().store(data);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Usuario> listarTodos() {
        return cargarDesdeXML();
    }

    public void guardar(Usuario usuario) {
        try {
            Data data = XmlPersister.instance().load();
            data.getUsuarios().removeIf(u -> u.getId().equalsIgnoreCase(usuario.getId()));
            data.getUsuarios().add(usuario);
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(String id) {
        try {
            Data data = XmlPersister.instance().load();
            data.getUsuarios().removeIf(u -> u.getId().equalsIgnoreCase(id));
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> cargarDesdeXML() {
        try {
            return XmlPersister.instance().load().getUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void guardarEnXML(List<Usuario> lista) {
        try {
            Data data = XmlPersister.instance().load();
            data.setUsuarios(lista);
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}