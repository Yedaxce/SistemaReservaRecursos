package reservas.logic.model;

//un recurso concreto es reservable e individual
//es responsabilidad de ReservaService saber si un recurso está disponible o no

public class Recurso {
    private final String id;
    private String descripcion;
    private CategoriaRecurso categoria;

    //validaciones
    public Recurso(String id, String descripcion, CategoriaRecurso categoria) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID del recurso vacío");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("Descripción del recurso vacía");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Recurso sin categoría");
        }
        this.id = id;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public CategoriaRecurso getCategoria() {
        return categoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
