package reservas.unitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import reservas.logic.CategoriaRecurso;
import reservas.logic.EstadoReserva;
import reservas.logic.Funcionario;
import reservas.logic.Recurso;
import reservas.logic.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private static final LocalDate FECHA = LocalDate.of(2026, 9, 21);
    private static final Funcionario FUNCIONARIO =
            new Funcionario("F-01", "clave", "Ana Solis", "8888-8888");

    @Test
    @DisplayName("Una Reserva válida se crea ACTIVA y sin recursos")
    void creaReservaActivaSinRecursos() {
        Reserva reserva = new Reserva("RES-000", "Reunión trimestral", FECHA,
                LocalTime.of(9, 0), LocalTime.of(10, 0), FUNCIONARIO);

        assertAll(
                () -> assertEquals("RES-000", reserva.getId()),
                () -> assertEquals(EstadoReserva.ACTIVA, reserva.getEstado()),
                () -> assertTrue(reserva.estaActiva()),
                () -> assertTrue(reserva.getRecursos().isEmpty())
        );
    }

    @Test
    @DisplayName("Validar horario y datos obligatorios")
    void rechazaHorarioInvalidoYDatosObligatorios() {
        assertAll(
                //sin id de reserva
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Reserva("", "Reunión", FECHA, LocalTime.of(9, 0),
                                LocalTime.of(10, 0), FUNCIONARIO)),
                //hora de inicio despues a la hora de fin
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Reserva("RES-000", "Reunión", FECHA, LocalTime.of(10, 0),
                                LocalTime.of(9, 0), FUNCIONARIO)),
                //sin hora de inicio
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Reserva("RES-000", "Reunión", FECHA, null,
                                LocalTime.of(10, 0), FUNCIONARIO)),
                //sin funcionario asociado
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Reserva("RES-000", "Reunión", FECHA, LocalTime.of(9, 0),
                                LocalTime.of(10, 0), null))
        );
    }

    @Test
    @DisplayName("solapamiento misma fecha")
    void detectaSolapamientoSoloEnLaMismaFecha() {
        Reserva base = reserva("RES-001", FECHA, LocalTime.of(9, 0), LocalTime.of(10, 0));
        Reserva consecutiva = reserva("RES-002", FECHA, LocalTime.of(10, 0), LocalTime.of(11, 0));
        Reserva solapada = reserva("RES-003", FECHA, LocalTime.of(9, 59), LocalTime.of(11, 0));
        Reserva reservaOtroDia = reserva("RES-004", FECHA.plusDays(1), LocalTime.of(9, 30), LocalTime.of(10, 30));

        assertFalse(base.seSolapaCon(consecutiva));
        assertTrue(base.seSolapaCon(solapada));
        assertFalse(base.seSolapaCon(reservaOtroDia));
        assertFalse(base.seSolapaCon(null));
    }

    @Test
    @DisplayName("Verificar cambio de estado de la reserva (cancelar)")
    void cancelarCambiaEstadoYAgregaRecursos() {
        Reserva reserva = reserva("RES-000", FECHA, LocalTime.of(9, 0), LocalTime.of(10, 0));
        CategoriaRecurso categoria = new CategoriaRecurso("CAT-000001", "Salas");
        Recurso recurso = new Recurso("REC-000", "Sala 1", categoria);

        reserva.agregarRecurso(recurso);
        reserva.cancelar();

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertFalse(reserva.estaActiva());
        assertEquals(1, reserva.getRecursos().size());
        assertThrows(UnsupportedOperationException.class, () -> reserva.getRecursos().clear());
    }

    private Reserva reserva(String id, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        return new Reserva(id, "Reunión", fecha, inicio, fin, FUNCIONARIO);
    }
}
