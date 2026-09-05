package reservas.dao;

import reservas.logic.model.Funcionario;
import reservas.logic.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    private final UsuarioDAO usuarioDAO;

    public FuncionarioDAO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Funcionario buscarPorId(String id) {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u instanceof Funcionario) {
            return (Funcionario) u;
        }
        return null;
    }

    public List<Funcionario> buscarPorNombre(String nombre) {
        List<Funcionario> resultado = new ArrayList<>();
        if (nombre == null) return resultado;

        for (Funcionario f : listarTodos()) {
            if (f.getNombre() != null && f.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.add(f);
            }
        }
        return resultado;
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();
        for (Usuario u : usuarioDAO.listarTodos()) {
            if (u instanceof Funcionario) {
                funcionarios.add((Funcionario) u);
            }
        }
        return funcionarios;
    }

    public void guardar(Funcionario funcionario) {
        usuarioDAO.guardar(funcionario);
    }

    public void actualizar(Funcionario funcionario) {
        usuarioDAO.guardar(funcionario);
    }

    public void eliminar(String id) {
        usuarioDAO.eliminar(id);
    }
}