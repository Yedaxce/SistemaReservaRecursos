package reservas.presentation.actividadesP;

import reservas.logic.model.Reserva;
import reservas.presentation.AbstractModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActividadesModel extends AbstractModel {

    private LocalDate fechaReferencia = LocalDate.now();
    private List<Reserva> reservas = new ArrayList<>();

    public static final String FECHA = "fecha";
    public static final String TABLA = "tabla";

    public LocalDate getFechaReferencia() { return fechaReferencia; }
    public void setFechaReferencia(LocalDate fechaReferencia) {
        this.fechaReferencia = fechaReferencia;
        firePropertyChange(FECHA);
    }

    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
        firePropertyChange(TABLA);
    }
}
