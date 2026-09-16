package reservas.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class IAServiceTest {

    private IAService iaService;

    @BeforeEach
    void setUp() {
        iaService = new IAService();
    }

    @Test
    @DisplayName("Validar extracción de datos con procesamiento local/LLM")
    void testExtraerDatosReserva() {
        String frase = "reunión de trabajo el 18 de noviembre de 8am a 10am en una sala para 10 personas usando laptop";

        DatosReservaDTO dto = iaService.extraerDatosReserva(frase);

        assertNotNull(dto, "El objeto DTO no debe ser nulo");
        assertNotNull(dto.getActividad(), "La actividad extraída no debe ser nula");
        assertNotNull(dto.getFecha(), "La fecha extraída no debe ser nula");
        assertEquals(LocalTime.of(8, 0), dto.getHoraInicio(), "La hora de inicio debe ser 08:00");
        assertEquals(LocalTime.of(10, 0), dto.getHoraFin(), "La hora de fin debe ser 10:00");

        assertFalse(dto.getCategorias().isEmpty(), "Debe identificar al menos una categoría de recurso");
        assertTrue(dto.getCategorias().contains("Laptop windows"), "Debe reconocer la categoría Laptop windows");
    }

    @Test
    @DisplayName("Validar que responda ante frases vacías o breves")
    void testExtraerDatosFraseCorta() {
        DatosReservaDTO dto = iaService.extraerDatosReserva("charla corta");

        assertNotNull(dto, "El DTO debe procesarse incluso con entrada mínima");
        assertNotNull(dto.getHoraInicio());
        assertNotNull(dto.getHoraFin());
    }
}