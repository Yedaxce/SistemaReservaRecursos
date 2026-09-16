package reservas.unitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import reservas.logic.CategoriaRecurso;
import reservas.logic.Recurso;

import static org.junit.jupiter.api.Assertions.*;

class RecursoTest {
    private CategoriaRecurso categoria;
    @BeforeEach
    void inicializarCategoria() {
        categoria = new CategoriaRecurso("CAT-000001", "Salas de reunión");
    }

    @Test
    @DisplayName("Crear recurso asociado a una categoria")
    void creaRecursoAsociadoASuCategoria() {
        CategoriaRecurso categoria = new CategoriaRecurso("CAT-01", "Salas");
        Recurso recurso = new Recurso("REC-000", "Sala 101", categoria);

        assertEquals("REC-000", recurso.getId());
        assertEquals("Sala 101", recurso.getDescripcion());
        assertSame(categoria, recurso.getCategoria());
    }

    @Test
    @DisplayName("Validar datos obligatorios")
    void rechazaDatosObligatoriosInvalidos() {
        CategoriaRecurso categoria = new CategoriaRecurso("CAT-1", "Salas");

        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Recurso("", "Sala 101", categoria)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Recurso("REC-1", " ", categoria)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Recurso("REC-1", "Sala 101", null))
        );
    }
}
