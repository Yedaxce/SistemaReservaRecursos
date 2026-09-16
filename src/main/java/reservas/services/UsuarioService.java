package reservas.services;

import reservas.dao.Data;
import reservas.dao.UsuarioDAO;
import reservas.dao.XmlPersister;
import reservas.logic.Usuario;

public class UsuarioService {
    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario login(String id, String clave) {
        if (id == null || clave == null) return null;
        return usuarioDAO.validarLogin(id, clave);
    }


    public boolean existeUsuario(String id) {
        try {
            Data data = XmlPersister.instance().load();
            return data.getUsuarios().stream()
                    .anyMatch(u -> u.getId().equalsIgnoreCase(id));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean cambiarClave(String id, String claveActual, String nuevaClave) throws Exception {
        Data data = XmlPersister.instance().load();

        // 1. Buscar usuario
        Usuario usuario = data.getUsuarios().stream()
                .filter(u -> u.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);

        // 2. Validar existencia
        if (usuario == null) {
            throw new Exception("El ID de usuario '" + id + "' no existe en el sistema.");
        }

        // 3. Validar clave actual
        if (!usuario.getClave().equals(claveActual)) {
            throw new Exception("La contraseña actual es incorrecta.");
        }

        // 4. Validar que la nueva clave sea diferente a la actual
        if (claveActual.equals(nuevaClave)) {
            throw new Exception("La nueva contraseña no puede ser igual a la contraseña actual.");
        }

        // 5. Guardar cambios
        usuario.setClave(nuevaClave);
        XmlPersister.instance().store(data);
        return true;
    }
}