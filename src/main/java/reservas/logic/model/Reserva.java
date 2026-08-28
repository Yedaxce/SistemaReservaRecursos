package reservas.logic.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Reserva {
    private final String id;
    private String actividad;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private final Funcionario funcionario;
    private EstadoReserva estado;
    private final List<Recurso> recursos;

    public Reserva(String id, String actividad, LocalDate fecha, LocalTime horaInicio,
                   LocalTime horaFin, Funcionario funcionario) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID de la reserva vacío");
        }
        if (actividad == null || actividad.isBlank()) {
            throw new IllegalArgumentException("La actividad no puede estar vacía");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (funcionario == null) {
            throw new IllegalArgumentException("Sin funcionario asignado");
        }
        validarHorario(horaInicio, horaFin);

        this.id = id;
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.funcionario = funcionario;
        this.estado = EstadoReserva.ACTIVA;
        this.recursos = new ArrayList<>();
    }

    private static void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (horaInicio == null) {
            throw new IllegalArgumentException("Hora de inicio sin asignar");
        }
        if (horaFin == null) {
            throw new IllegalArgumentException("Hora de final sin asignar");
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("Incongruencia en el horario");
        }
    }

    public String getId() {
        return id;
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

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public EstadoReserva getEstado() {
        return estado;
    }


    public List<Recurso> getRecursos() {
        //crea una lista (solo lectura) de los recursos disponibles
        return Collections.unmodifiableList(recursos);
    }

    public void setActividad(String actividad) {
        if (actividad == null || actividad.isBlank()) {
            throw new IllegalArgumentException("La actividad no puede estar vacía");
        }
        this.actividad = actividad;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("Fecha sin asignar");
        }
        this.fecha = fecha;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        validarHorario(horaInicio, this.horaFin);
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalTime horaFin) {
        validarHorario(this.horaInicio, horaFin);
        this.horaFin = horaFin;
    }

    public void agregarRecurso(Recurso recurso) {
        if (recurso == null) {
            throw new IllegalArgumentException("Recurso no encontrado");
        }
        recursos.add(recurso);
    }

    public void cancelar() {
        this.estado = EstadoReserva.CANCELADA;
    }

    public boolean estaActiva() {
        return estado == EstadoReserva.ACTIVA;
    }


    public boolean seSolapaCon(Reserva otra) {
        if (otra == null || !fecha.equals(otra.fecha)) {
            return false;
        }
        return horaInicio.isBefore(otra.horaFin) && otra.horaInicio.isBefore(horaFin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Reserva)) {
            return false;
        }
        Reserva otra = (Reserva) obj;
        return id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " - " + actividad + " (" + fecha + " " + horaInicio + "-" + horaFin + ")";
    }
}
