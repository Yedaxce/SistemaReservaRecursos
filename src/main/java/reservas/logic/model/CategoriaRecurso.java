package reservas.logic.model;
import java.util.Objects;

/*agrupa los recurso del mismo tipo*/

public class CategoriaRecurso {
    private final String id;
    private String descripcion;

    public CategoriaRecurso(String id, String descripcion) {
        //validaciones
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Sin id asignado a la categoria");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("Descripción de categoría vacía");
        }
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            //validacion
            throw new IllegalArgumentException("Descripción vacía");
        }
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CategoriaRecurso)) {
            return false;
        }
        CategoriaRecurso otra = (CategoriaRecurso) obj;
        return id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return descripcion;
    }

}
