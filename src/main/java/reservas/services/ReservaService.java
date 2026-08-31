package reservas.services;

import reservas.dao.FuncionarioDAO;
import reservas.dao.RecursoDAO;
import reservas.dao.ReservaDAO;
import reservas.logic.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaService {
    private final ReservaDAO reservaDAO;
    private final RecursoDAO recursoDAO;
    private final FuncionarioDAO funcionarioDAO;

    public ReservaService() {
        this.reservaDAO = new ReservaDAO();
        this.recursoDAO = new RecursoDAO();
        this.funcionarioDAO = new FuncionarioDAO();
    }

    public List<Reserva> listarPorFuncionario(String idFuncionario) {
        return reservaDAO.listarPorFuncionario(idFuncionario);
    }

    public ResultadoReserva crearReserva(String actividad, LocalDate fecha, LocalTime horaInicio,
                                         LocalTime horaFin, Funcionario funcionario,
                                         List<CategoriaRecurso> categoriasSolicitadas) {

        List<CategoriaRecurso> noDisponibles = new ArrayList<>();
        List<Recurso> recursosAsignar = new ArrayList<>();

        // 1. Validar disponibilidad por cada categoría solicitada
        for (CategoriaRecurso cat : categoriasSolicitadas) {
            List<Recurso> disp = obtenerRecursosDisponibles(fecha, horaInicio, horaFin, cat);
            if (disp.isEmpty()) {
                noDisponibles.add(cat);
            } else {
                // Tomar el primer recurso disponible de esa categoría
                recursosAsignar.add(disp.get(0));
            }
        }

        // 2. Si alguna categoría no tenía disponibilidad, la reserva falla
        if (!noDisponibles.isEmpty()) {
            return new ResultadoReserva(false, "No hay disponibilidad en todas las categorías requeridas.", noDisponibles, null);
        }

        // 3. Crear la reserva y asignarle los recursos
        String nuevoId = reservaDAO.generarNuevoId();
        Reserva nuevaReserva = new Reserva(nuevoId, actividad, fecha, horaInicio, horaFin, funcionario);
        for (Recurso r : recursosAsignar) {
            nuevaReserva.agregarRecurso(r);
        }

        reservaDAO.guardar(nuevaReserva);
        return new ResultadoReserva(true, "Reserva registrada con éxito.", noDisponibles, nuevaReserva);
    }

    public boolean cancelarReserva(String idReserva) {
        Reserva r = reservaDAO.buscarPorId(idReserva);
        if (r != null && r.estaActiva()) {
            r.cancelar();
            reservaDAO.actualizar(r);
            return true;
        }
        return false;
    }

    public List<Recurso> obtenerRecursosDisponibles(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, CategoriaRecurso categoria) {
        List<Recurso> todosRecursoCat = recursoDAO.listarPorCategoria(categoria.getId());
        List<Reserva> reservasFecha = reservaDAO.listarPorFecha(fecha);
        List<Recurso> disponibles = new ArrayList<>();

        for (Recurso r : todosRecursoCat) {
            boolean ocupado = false;
            for (Reserva res : reservasFecha) {
                if (res.estaActiva() && res.getRecursos().contains(r)) {
                    // Verificar solapamiento de horarios
                    if (horaInicio.isBefore(res.getHoraFin()) && res.getHoraInicio().isBefore(horaFin)) {
                        ocupado = true;
                        break;
                    }
                }
            }
            if (!ocupado) {
                disponibles.add(r);
            }
        }
        return disponibles;
    }
}