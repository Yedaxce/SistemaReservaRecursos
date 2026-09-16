package reservas.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reservas.logic.model.Recurso;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecursoServiceTest {

    private RecursoService recursoService;

    @BeforeEach
    void setUp() {
        recursoService = new RecursoService();
    }

    @Test
    @DisplayName("Validar consulta de todos los recursos")
    void testListarTodos() {
        List<Recurso> recursos = recursoService.listarTodos();
        assertNotNull(recursos, "La lista de recursos no debe ser nula");
    }

    @Test
    @DisplayName("Validar filtrado por categoría inexistente")
    void testListarPorCategoriaInexistente() {
        List<Recurso> recursos = recursoService.listarPorCategoria("CAT_INEXISTENTE_999");
        assertNotNull(recursos, "La lista retornada no debe ser nula");
    }

}