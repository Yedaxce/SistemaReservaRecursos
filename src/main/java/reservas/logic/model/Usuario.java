package reservas.logic.model;

import java.util.Objects;

public abstract class Usuario {

    private final String id;
    private String clave;
    private final Rol rol; //Enum de Rol del Usuario:  ADMIN, FUNCIONARIO

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
        return id;
    }

    public String getClave() {
        return clave;
    }

    public Rol getRol() {
        return rol;
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