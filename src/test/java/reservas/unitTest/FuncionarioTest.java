package reservas.unitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import reservas.logic.model.Funcionario;
import reservas.logic.model.Rol;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioTest {

    @Test
    @DisplayName("Un Funcionario válido se crea sin lanzar excepción")
    void creaFuncionarioConRolFuncionario() {
        Funcionario funcionario = new Funcionario("F-01", "clave", "Ana Solis", "8888-8888");
        assertAll(
                () -> assertEquals("F-01", funcionario.getId()),
                () -> assertEquals("Ana Solis", funcionario.getNombre()),
                () -> assertEquals("8888-8888", funcionario.getTelefono()),
                () -> assertEquals(Rol.FUNCIONARIO, funcionario.getRol())
        );
    }

    @Test
    @DisplayName("Validar datos invalidos")
    void rechazaDatosObligatoriosInvalidos() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Funcionario("", "clave", "Ana", "8888")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Funcionario("F-01", "", "Ana", "8888")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Funcionario("F-01", "clave", " ", "8888"))
        );
    }

    @Test
    @DisplayName("Validar actualización de clave y datos")
    void validaClaveYPermiteActualizarDatos() {
        Funcionario funcionario = new Funcionario("F-01", "clave", "Ana", "8888");

        funcionario.setNombre("Ana Solis");
        funcionario.setTelefono("9999");
        funcionario.setClave("nueva-clave");

        assertEquals("Ana Solis", funcionario.getNombre());
        assertEquals("9999", funcionario.getTelefono());
        assertTrue(funcionario.validarClave("nueva-clave"));
        assertFalse(funcionario.validarClave("clave"));
    }
}
