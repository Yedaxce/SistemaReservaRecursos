package reservas.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reservas.logic.model.CategoriaRecurso;
import reservas.logic.model.Funcionario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservaServiceTest {

    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        reservaService = new ReservaService();
    }

    @Test
    @DisplayName("Validar creación de reserva con datos válidos")
    void testCrearReservaValida() {

        Funcionario funcionario = new Funcionario("111","jp66", "Luna Amaris", "222333");

        List<CategoriaRecurso> categorias = new ArrayList<>();
        CategoriaRecurso cat = new CategoriaRecurso("CAT-000001", "Sala para 10 personas");
        categorias.add(cat);

        ResultadoReserva resultado = reservaService.crearReserva(
                "Reunión de prueba",
                LocalDate.now().plusDays(1),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                funcionario,
                categorias
        );

        assertNotNull(resultado, "El resultado de la reserva no debe ser nulo");
    }
}