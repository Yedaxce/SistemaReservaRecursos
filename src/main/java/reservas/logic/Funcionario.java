package reservas.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Funcionario extends Usuario {

    private String nombre;
    private String telefono;

    public Funcionario(){
        super();
        this.setRol(Rol.FUNCIONARIO);
    }

    public Funcionario(String id, String clave, String nombre, String telefono) {
        super(id, clave, Rol.FUNCIONARIO);
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre del funcionario vacío");
        }
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre del funcionario vacío");
        }
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return nombre + " (" + getId() + ")";
    }
}
