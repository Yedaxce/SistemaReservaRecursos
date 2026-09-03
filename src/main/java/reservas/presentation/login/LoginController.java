package reservas.presentation.login;

import reservas.logic.Usuario;
import reservas.presentation.Sesion;
import reservas.services.UsuarioService;

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
}