package reservas.logic;

//el administrador no tiene otros atributos ni metros propios
public class Administrador extends Usuario {

    public Administrador() {
        super();
        this.setRol(Rol.ADMIN);
    }

    public Administrador(String id, String clave) {
        super(id, clave, Rol.ADMIN);
    }
}
