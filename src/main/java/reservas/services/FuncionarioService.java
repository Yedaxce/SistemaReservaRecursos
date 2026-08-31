package reservas.services;

import reservas.dao.FuncionarioDAO;
import reservas.dao.UsuarioDAO;
import reservas.logic.model.Funcionario;

import java.util.List;

public class FuncionarioService {
    private final FuncionarioDAO funcionarioDAO;
    private final UsuarioDAO usuarioDAO;

    public FuncionarioService() {
        this.funcionarioDAO = new FuncionarioDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    public Funcionario buscarPorId(String id) {
        return funcionarioDAO.buscarPorId(id);
    }

    public List<Funcionario> buscarPorNombre(String nombre) {
        return funcionarioDAO.buscarPorNombre(nombre);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioDAO.listarTodos();
    }

    public void crear(Funcionario funcionario) {
        if (funcionarioDAO.buscarPorId(funcionario.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un funcionario/usuario registrado con el ID: " + funcionario.getId());
        }
        funcionarioDAO.guardar(funcionario);
    }

    public void actualizar(Funcionario funcionario) {
        funcionarioDAO.actualizar(funcionario);
    }

    public void eliminar(String id) {
        funcionarioDAO.eliminar(id);
    }
}