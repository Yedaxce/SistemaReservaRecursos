package reservas.presentation.funcionarioP;

import reservas.logic.Funcionario;
import reservas.services.FuncionarioService;

public class FuncionarioController {
    private final FuncionarioView view;
    private final FuncionarioModel model;
    private final FuncionarioService service;

    public FuncionarioController(FuncionarioView view, FuncionarioModel model) {
        this.view = view;
        this.model = model;
        this.service = new FuncionarioService();
        view.setController(this);
        view.setModel(model);          // aquí la View se suscribe como observador
        model.setLista(service.listarTodos()); // carga inicial de la tabla
    }

    /** Crear un funcionario nuevo. */
    public void crear(Funcionario funcionario) throws Exception {
        service.crear(funcionario);              // puede lanzar IllegalArgumentException (ID duplicado)
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    /** Actualizar un funcionario existente (nombre/telefono/clave). */
    public void actualizar(Funcionario funcionario) throws Exception {
        service.actualizar(funcionario);
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    /** Elimina por id (llamado tras confirmar en la Vista). */
    public void eliminar(String id) {
        service.eliminar(id);
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    /** Limpia el formulario -> vuelve a modo "nuevo". */
    public void limpiar() {
        model.setActual(null);
    }

    /** El usuario hizo clic/seleccionó una fila de la tabla -> carga el formulario. */
    public void seleccionar(int row) {
        if (row >= 0 && row < model.getLista().size()) {
            model.setActual(model.getLista().get(row));
        }
    }

    /** Filtra por nombre; si está vacío, vuelve a listar todos. */
    public void buscar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            model.setLista(service.listarTodos());
        } else {
            model.setLista(service.buscarPorNombre(nombre));
        }
    }
}
