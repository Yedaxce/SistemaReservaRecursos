package reservas.dao;

import jakarta.xml.bind.annotation.*;
import reservas.logic.CategoriaRecurso;
import reservas.logic.Recurso;
import reservas.logic.Reserva;
import reservas.logic.Usuario;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class
Data {

    @XmlElementWrapper(name = "usuarios")
    @XmlElement(name = "usuario")
    private List<Usuario> usuarios;

    @XmlElementWrapper(name = "categorias")
    @XmlElement(name = "categoria")
    private List<CategoriaRecurso> categorias;

    @XmlElementWrapper(name = "recursos")
    @XmlElement(name = "recurso")
    private List<Recurso> recursos;

    @XmlElementWrapper(name = "reservas")
    @XmlElement(name = "reserva")
    private List<Reserva> reservas;

    public Data() {
        this.usuarios = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.recursos = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }

    public List<CategoriaRecurso> getCategorias() { return categorias; }
    public void setCategorias(List<CategoriaRecurso> categorias) { this.categorias = categorias; }

    public List<Recurso> getRecursos() { return recursos; }
    public void setRecursos(List<Recurso> recursos) { this.recursos = recursos; }

    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}