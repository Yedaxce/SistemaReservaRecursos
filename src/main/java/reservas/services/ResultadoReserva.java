package reservas.services;

import reservas.logic.CategoriaRecurso;
import reservas.logic.Reserva;

import java.util.List;

public class ResultadoReserva {
    private final boolean exitosa;
    private final String mensaje;
    private final List<CategoriaRecurso> categoriasNoDisponibles;
    private final Reserva reserva;

    public ResultadoReserva(boolean exitosa, String mensaje, List<CategoriaRecurso> categoriasNoDisponibles, Reserva reserva) {
        this.exitosa = exitosa;
        this.mensaje = mensaje;
        this.categoriasNoDisponibles = categoriasNoDisponibles;
        this.reserva = reserva;
    }

    public boolean isExitosa() {
        return exitosa;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<CategoriaRecurso> getCategoriasNoDisponibles() {
        return categoriasNoDisponibles;
    }

    public Reserva getReserva() {
        return reserva;
    }
}