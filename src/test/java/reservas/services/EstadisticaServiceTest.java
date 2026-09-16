package reservas.services;

import org.jfree.chart.JFreeChart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reservas.logic.model.CategoriaRecurso;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticaServiceTest {

    private EstadisticaService estadisticaService;

    @BeforeEach
    void setUp() {
        estadisticaService = new EstadisticaService();
    }

    @Test
    @DisplayName("Validar cálculo de recursos usados en un periodo válido")
    void testRecursosUsadosEnPeriodoValido() {
        LocalDate desde = LocalDate.now().minusDays(30);
        LocalDate hasta = LocalDate.now();

        Map<CategoriaRecurso, Integer> resultado = estadisticaService.recursosUsadosEnPeriodo(desde, hasta);

        assertNotNull(resultado, "El mapa de recursos no debe ser nulo");
    }

    @Test
    @DisplayName("Validar manejo de fechas inválidas o nulas en recursosUsadosEnPeriodo")
    void testRecursosUsadosFechasInvalida() {
        // Rango invertido (desde es posterior a hasta)
        LocalDate desde = LocalDate.now().plusDays(10);
        LocalDate hasta = LocalDate.now();

        Map<CategoriaRecurso, Integer> resultadoInvertido = estadisticaService.recursosUsadosEnPeriodo(desde, hasta);
        assertTrue(resultadoInvertido.isEmpty(), "Con rango invertido debe retornar un mapa vacío");

        // Fechas nulas
        Map<CategoriaRecurso, Integer> resultadoNull = estadisticaService.recursosUsadosEnPeriodo(null, null);
        assertTrue(resultadoNull.isEmpty(), "Con fechas nulas debe retornar un mapa vacío");
    }

    @Test
    @DisplayName("Validar cálculo de actividades por semana")
    void testActividadesPorSemana() {
        LocalDate desde = LocalDate.now().minusDays(30);
        LocalDate hasta = LocalDate.now();

        Map<String, Integer> resultado = estadisticaService.actividadesPorSemana(desde, hasta);

        assertNotNull(resultado, "El mapa de actividades por semana no debe ser nulo");
    }

    @Test
    @DisplayName("Validar generación de objetos JFreeChart sin errores")
    void testGenerarGraficos() {
        LocalDate desde = LocalDate.now().minusDays(30);
        LocalDate hasta = LocalDate.now();

        JFreeChart graficoRecursos = estadisticaService.generarGraficoRecursos(desde, hasta);
        assertNotNull(graficoRecursos, "El gráfico de recursos JFreeChart debe crearse exitosamente");

        JFreeChart graficoActividades = estadisticaService.generarGraficoActividades(desde, hasta);
        assertNotNull(graficoActividades, "El gráfico de actividades JFreeChart debe crearse exitosamente");
    }
}