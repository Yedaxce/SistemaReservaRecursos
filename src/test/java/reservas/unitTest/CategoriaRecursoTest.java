package reservas.unitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import reservas.logic.CategoriaRecurso;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaRecursoTest {

    @Test
    @DisplayName("Una Categoria de Recurso válida se crea y cambia de descripción sin lanzar excepción")
    void creaCategoriaYActualizaDescripcion() {
        CategoriaRecurso categoria = new CategoriaRecurso("CAT-000001", "Sala para 10 personas");

        assertEquals("CAT-000001", categoria.getId());
        assertEquals("Sala para 10 personas", categoria.getDescripcion());
        assertEquals(categoria, new CategoriaRecurso("CAT-000001", "Sala central"));
        assertEquals(categoria.hashCode(), new CategoriaRecurso("CAT-000001", "Sala central").hashCode());
    }

    @Test
    @DisplayName("Validar identificación y descripción ")
    void rechazaIdentificadorYDescripcionInvalidos() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new CategoriaRecurso("", "Salas")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new CategoriaRecurso("CAT-000001", " ")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new CategoriaRecurso("CAT-000001", "Salas").setDescripcion(null))
        );
    }
}


