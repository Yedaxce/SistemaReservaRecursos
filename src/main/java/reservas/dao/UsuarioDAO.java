package reservas.dao;

import reservas.logic.Usuario;

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

    public boolean actualizarClave(String id, String claveActual, String nuevaClave) throws Exception {
        Data data = XmlPersister.instance().load();

        // 1. Buscar el usuario en la lista
        Usuario usuarioEncontrado = null;
        for (Usuario u : data.getUsuarios()) {
            if (u.getId().equalsIgnoreCase(id)) {
                usuarioEncontrado = u;
                break;
            }
        }

        // 2. Validar si el ID de usuario existe
        if (usuarioEncontrado == null) {
            throw new Exception("El usuario con ID '" + id + "' no existe en el sistema.");
        }

        // 3. Validar si la contraseña actual ingresada coincide
        if (!usuarioEncontrado.getClave().equals(claveActual)) {
            throw new Exception("La contraseña actual es incorrecta.");
        }

        // 4. Actualizar la contraseña y guardar en el XML
        usuarioEncontrado.setClave(nuevaClave);
        XmlPersister.instance().store(data);
        return true;
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