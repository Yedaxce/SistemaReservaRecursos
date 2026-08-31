package reservas.logic;

public class Funcionario extends Usuario {

    private String nombre;
    private String telefono;

    public Funcionario(String id, String clave, String nombre, String telefono) {
        super(id, clave, Rol.FUNCIONARIO);
        //validacion
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
        //validacion
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
