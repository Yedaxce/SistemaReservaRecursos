package reservas.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reservas.logic.Reserva;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservaDAOIT {

    private ReservaDAO reservaDAO;

    @BeforeEach
    void setUp() {
        reservaDAO = new ReservaDAO();
    }

    @Test
    @DisplayName("Prueba de Integración: Cargar reservas desde XML")
    void testCargarDesdeXML() {
        List<Reserva> lista = reservaDAO.cargarDesdeXML();
        assertNotNull(lista, "La lista cargada desde el XML no debe ser nula");
    }

    @Test
    @DisplayName("Prueba de Integración: Generar ID e interactuar con el DAO")
    void testGenerarNuevoIdYBuscar() {
        String nuevoId = reservaDAO.generarNuevoId();
        assertNotNull(nuevoId, "El nuevo ID generado no debe ser nulo");
        assertTrue(nuevoId.startsWith("RES-"), "El ID generado debe iniciar con el prefijo RES-");

        Reserva noExistente = reservaDAO.buscarPorId("ID_INEXISTENTE_999");
        assertNull(noExistente, "Buscar un ID inexistente debe retornar null");
    }
}