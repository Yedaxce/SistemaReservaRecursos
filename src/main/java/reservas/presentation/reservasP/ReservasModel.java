package reservas.presentation.reservasP;

import reservas.logic.CategoriaRecurso;
import reservas.logic.Funcionario;
import reservas.logic.Reserva;
import reservas.presentation.AbstractModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservasModel extends AbstractModel {

    private Funcionario funcionarioLogueado;

    private String frase = "";
    private String actividad = "";
    private LocalDate fecha = LocalDate.now();
    private LocalTime horaInicio = null;
    private LocalTime horaFin = null;

    private List<CategoriaRecurso> categoriasDisponibles = new ArrayList<>();
    private List<CategoriaRecurso> categoriasSeleccionadas = new ArrayList<>();
    private List<Reserva> misReservas = new ArrayList<>();

    public static final String DATOS_FORMULARIO = "datosFormulario";
    public static final String CATEGORIAS = "categorias";
    public static final String TABLA_RESERVAS = "tablaReservas";

    public Funcionario getFuncionarioLogueado() { return funcionarioLogueado; }
    public void setFuncionarioLogueado(Funcionario funcionarioLogueado) {
        this.funcionarioLogueado = funcionarioLogueado;
    }

    public String getFrase() { return frase; }
    public void setFrase(String frase) { this.frase = frase; }

    public String getActividad() { return actividad; }
    public void setActividad(String actividad) { this.actividad = actividad; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public List<CategoriaRecurso> getCategoriasDisponibles() { return categoriasDisponibles; }
    public void setCategoriasDisponibles(List<CategoriaRecurso> categoriasDisponibles) {
        this.categoriasDisponibles = categoriasDisponibles;
        firePropertyChange(CATEGORIAS);
    }

    public List<CategoriaRecurso> getCategoriasSeleccionadas() { return categoriasSeleccionadas; }
    public void setCategoriasSeleccionadas(List<CategoriaRecurso> categoriasSeleccionadas) {
        this.categoriasSeleccionadas = categoriasSeleccionadas;
    }

    public List<Reserva> getMisReservas() { return misReservas; }
    public void setMisReservas(List<Reserva> misReservas) {
        this.misReservas = misReservas;
        firePropertyChange(TABLA_RESERVAS);
    }

    public void actualizarFormulario(String actividad, LocalDate fecha, LocalTime hInicio, LocalTime hFin, List<CategoriaRecurso> seleccionadas) {
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = hInicio;
        this.horaFin = hFin;
        this.categoriasSeleccionadas = seleccionadas;
        firePropertyChange(DATOS_FORMULARIO);
    }
}