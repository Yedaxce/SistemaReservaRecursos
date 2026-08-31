package reservas.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DatosReservaDTO {
    private final String actividad;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private final List<String> categorias;

    public DatosReservaDTO(String actividad, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, List<String> categorias) {
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.categorias = categorias;
    }

    public String getActividad() {
        return actividad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public List<String> getCategorias() {
        return categorias;
    }
}