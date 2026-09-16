package reservas.presentation.login;

import reservas.logic.Usuario;
import reservas.presentation.Sesion;
import reservas.services.UsuarioService;

import javax.swing.*;

public class LoginController {
    Usuario actual;

    private final LoginView view;
    private final LoginModel model;
    private final UsuarioService usuarioService;

    public LoginController(LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;
        this.usuarioService = new UsuarioService();
        view.setController(this);
        view.setModel(model);
    }
    public void login(String id, String clave) throws Exception {
        if (id == null || id.isBlank() || clave == null || clave.isBlank()) {
            throw new Exception("Debe indicar usuario y clave.");
        }
        Usuario logueado = usuarioService.login(id, clave);
        if (logueado == null) {
            throw new Exception("Usuario o clave incorrectos.");
        }
        Sesion.setUsuario(logueado);
    }

    public void solicitarCambioContrasena() {
        String idUsuario = view.getIdUsuario();

        if (idUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, ingrese un ID de usuario en el campo 'Usuario'.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!usuarioService.existeUsuario(idUsuario)) {
            JOptionPane.showMessageDialog(view, "El usuario con ID '" + idUsuario + "' no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CambioContraView dialog = new CambioContraView(view);
        dialog.setVisible(true); // La ejecución se pausa aquí hasta que se cierre el diálogo

        // SI CANCELÓ, SALE INMEDIATAMENTE (Sin ventanas emergentes ni validaciones)
        if (!dialog.isAceptado()) {
            return;
        }

        String actual = dialog.getClaveActual();
        String nueva = dialog.getClaveNueva();
        String confirmacion = dialog.getVerificacionClaveNueva();

        if (actual.isBlank() || nueva.isBlank() || confirmacion.isBlank()) {
            JOptionPane.showMessageDialog(view, "Debe llenar todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nueva.equals(confirmacion)) {
            JOptionPane.showMessageDialog(view, "La nueva clave y su confirmación no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean exito = usuarioService.cambiarClave(idUsuario, actual, nueva);
            if (exito) {
                JOptionPane.showMessageDialog(view, "Contraseña actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
        }
    }

}