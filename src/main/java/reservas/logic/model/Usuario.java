package reservas.logic.model;

import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Administrador.class, Funcionario.class})
public abstract class Usuario {

    @XmlID
    @XmlElement(name = "id", required = true)
    private String id;

    @XmlElement(name = "clave")
    private String clave;

    @XmlElement(name = "rol")
    private Rol rol; //Enum de Rol del Usuario:  ADMIN, FUNCIONARIO

    public Usuario() {
    }

    protected Usuario(String id, String clave, Rol rol) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID del usuario vacío");
        }
        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("Clave vacía");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }
        this.id = id;
        this.clave = clave;
        this.rol = rol;
    }

    public String getId() {
        return id != null ? id : "";
    }

    public String getClave() {
        return clave;
    }

    public Rol getRol() {
        if (this.rol == null) {
            // Si por alguna razón el rol está nulo al leer el XML,
            // determina el rol según la instancia real de la clase
            if (this instanceof Administrador) {
                this.rol = Rol.ADMIN;
            } else if (this instanceof Funcionario) {
                this.rol = Rol.FUNCIONARIO;
            }
        }
        return this.rol;
    }

    public void setRol(Rol rol){
        this.rol = rol;
    }

    public void setClave(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("Clave vacía");
        }
        this.clave = clave;
    }

    public boolean validarClave(String claveIngresada) {
        return clave.equals(claveIngresada);
    }

    // Dos usuarios son "el mismo" si tienen el mismo id
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Usuario)) {
            return false;
        }
        Usuario otro = (Usuario) obj;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " (" + rol + ")";
    }
}