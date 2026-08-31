package reservas.services;

import reservas.dao.UsuarioDAO;
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

    public boolean cambiarClave(String id, String claveActual, String claveNueva) {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u != null && u.validarClave(claveActual)) {
            return usuarioDAO.actualizarClave(id, claveNueva);
        }
        return false;
    }
}