package reservas.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reservas.logic.Usuario;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService();
    }

    @Test
    @DisplayName("Validar autenticación con credenciales nulas o inexistentes")
    void testLoginInvalido() {
        Usuario usuarioNull = usuarioService.login(null, "123");
        assertNull(usuarioNull, "El login con ID nulo debe retornar null");

        Usuario usuarioInexistente = usuarioService.login("USUARIO_INEXISTENTE_999", "clave_falsa");
        assertNull(usuarioInexistente, "El login con datos falsos debe retornar null");
    }

    @Test
    @DisplayName("Validar verificación de existencia de un usuario falso")
    void testExisteUsuarioInexistente() {
        boolean existe = usuarioService.existeUsuario("ID_FALSO_123");
        assertFalse(existe, "Un ID inexistente debe retornar false");
    }

    @Test
    @DisplayName("Validar que cambiarClave lance excepción si el usuario no existe")
    void testCambiarClaveUsuarioInexistente() {
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarClave("ID_FALSO_123", "claveVieja", "claveNueva");
        });

        assertTrue(exception.getMessage().contains("no existe en el sistema"));
    }
}