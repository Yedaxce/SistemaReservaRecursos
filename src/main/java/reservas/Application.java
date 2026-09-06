package reservas;

import reservas.logic.model.Administrador;
import reservas.presentation.Sesion;
import reservas.presentation.categoriasRecurP.CategoriasController;
import reservas.presentation.categoriasRecurP.CategoriasModel;
import reservas.presentation.categoriasRecurP.CategoriasView;
import reservas.presentation.login.LoginController;
import reservas.presentation.login.LoginModel;
import reservas.presentation.login.LoginView;

import reservas.logic.model.Funcionario;
import reservas.presentation.funcionarioP.FuncionarioController;
import reservas.presentation.funcionarioP.FuncionarioModel;
import reservas.presentation.funcionarioP.FuncionarioView;

import reservas.presentation.recursosP.RecursosController;
import reservas.presentation.recursosP.RecursosModel;
import reservas.presentation.recursosP.RecursosView;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {}
        database();
        doLogin();
        if (Sesion.isLoggedIn()) {
            doRun();
        }

    }

    private static void doLogin() {
        LoginView loginView = new LoginView();
        LoginModel loginModel = new LoginModel();
        LoginController loginController = new LoginController(loginView, loginModel);
        loginView.pack();
        loginView.setLocationRelativeTo(null);
        loginView.setVisible(true);
    }

    private static void doRun() {
        JFrame window = new JFrame("Sistema de Reserva de Recursos");
        JTabbedPane tabbedPane = new JTabbedPane();
        window.setContentPane(tabbedPane);

        //      Pestaña Funcionarios
        FuncionarioView funcionarioView = new FuncionarioView();
        FuncionarioModel funcionarioModel = new FuncionarioModel();
        FuncionarioController funcionarioController = new FuncionarioController(funcionarioView, funcionarioModel);

        //      Pestaña Categorias de Recursos
        CategoriasView categoriasView = new CategoriasView();
        CategoriasModel categoriasModel = new CategoriasModel();
        CategoriasController categoriasController = new CategoriasController(categoriasView, categoriasModel);

        //      Pestaña Recursos
        RecursosView recursosView = new RecursosView();
        RecursosModel recursosModel = new RecursosModel();
        RecursosController recursosController = new RecursosController(recursosView,recursosModel);

        // ESPACIO PARA AGREGAR LAS PESTAÑAS/VENTANAS A LA APP,
        //solo "desbloquear" segun se vaya haciendo, con la sintaxis
        // tabbedPane.addTab("nombreVentana", ventanaView.getPanel());

        switch (Sesion.getUsuario().getRol()) {
            case ADMIN:
                tabbedPane.addTab("Funcionarios", funcionarioView.getPanel());
                tabbedPane.addTab("Categorías", categoriasView.getPanel());
                tabbedPane.addTab("Recursos", recursosView.getPanel());
                // tabbedPane.addTab("Calendarizacion", calendarizacionView.getPanel());
                // tabbedPane.addTab("Actividades", actividadesView.getPanel());
                // tabbedPane.addTab("Estadísticas", estadisticaView.getPanel());
                break;
            case FUNCIONARIO:
                // tabbedPane.addTab("Reservas", reservaView.getPanel());
                // tabbedPane.addTab("Calendarizacion", calendarizacionView.getPanel());
                // tabbedPane.addTab("Actividades", actividadesView.getPanel());
                // tabbedPane.addTab("Estadísticas", estadisticaView.getPanel());
                break;
        }

        window.setTitle("Sistema de Reserva de Recursos - " + Sesion.getUsuario().getId()
                + " (" + Sesion.getUsuario().getRol() + ")");
        window.setSize(900, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }

    public static void database () {
        Funcionario f1 = new Funcionario("111", "JP-1", "Juan Perez", "1234");
        Administrador Ad1 = new Administrador("222", "yd-21");
    }

}
