package reservas.logic.model;

//el administrador no tiene otros atributos ni metros propios
public class Administrador extends Usuario {
    public Administrador(String id, String clave) {
        super(id, clave, Rol.ADMIN);
    }
}
