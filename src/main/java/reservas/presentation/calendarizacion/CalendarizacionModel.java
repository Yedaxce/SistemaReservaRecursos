package reservas.presentation.calendarizacion;

import reservas.logic.model.CategoriaRecurso;
import reservas.logic.model.Recurso;
import reservas.logic.model.Reserva;
import reservas.presentation.AbstractModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarizacionModel extends AbstractModel {

    private LocalDate fecha = LocalDate.now();
    private CategoriaRecurso categoriaSeleccionada;
    private List<CategoriaRecurso> categorias = new ArrayList<>();
    private List<Recurso> recursos = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();

    public static final String FECHA = "fecha";
    public static final String CATEGORIAS = "categorias";
    public static final String TABLA = "tabla";

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
        firePropertyChange(FECHA);
    }

    public CategoriaRecurso getCategoriaSeleccionada() { return categoriaSeleccionada; }
    public void setCategoriaSeleccionada(CategoriaRecurso categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public List<CategoriaRecurso> getCategorias() { return categorias; }
    public void setCategorias(List<CategoriaRecurso> categorias) {
        this.categorias = categorias;
        firePropertyChange(CATEGORIAS);
    }

    public List<Recurso> getRecursos() { return recursos; }
    public List<Reserva> getReservas() { return reservas; }

    public void setDatosTabla(List<Recurso> recursos, List<Reserva> reservas) {
        this.recursos = recursos;
        this.reservas = reservas;
        firePropertyChange(TABLA);
    }
}
