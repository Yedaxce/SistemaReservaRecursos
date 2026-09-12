package reservas;

import reservas.logic.model.Administrador;
import reservas.logic.model.Funcionario;
import reservas.logic.model.Reserva;
import reservas.logic.model.Usuario;
import reservas.presentation.Sesion;
import reservas.presentation.actividadesP.ActividadesController;
import reservas.presentation.actividadesP.ActividadesModel;
import reservas.presentation.actividadesP.ActividadesView;
import reservas.presentation.calendarizacion.CalendarizacionController;
import reservas.presentation.calendarizacion.CalendarizacionModel;
import reservas.presentation.calendarizacion.CalendarizacionView;
import reservas.presentation.categoriasRecurP.CategoriasModel;
import reservas.presentation.categoriasRecurP.CategoriasView;
import reservas.presentation.categoriasRecurP.CategoriasController;
import reservas.presentation.estadisticasP.EstadisticasController;
import reservas.presentation.estadisticasP.EstadisticasModel;
import reservas.presentation.estadisticasP.EstadisticasView;
import reservas.presentation.funcionarioP.FuncionarioController;
import reservas.presentation.funcionarioP.FuncionarioModel;
import reservas.presentation.funcionarioP.FuncionarioView;
import reservas.presentation.login.LoginController;
import reservas.presentation.login.LoginModel;
import reservas.presentation.login.LoginView;
import reservas.presentation.recursosP.RecursosController;
import reservas.presentation.recursosP.RecursosModel;
import reservas.presentation.recursosP.RecursosView;
import reservas.presentation.reservasP.ReservasController;
import reservas.presentation.reservasP.ReservasModel;
import reservas.presentation.reservasP.ReservasView;
import reservas.services.ReservaService;

import javax.swing.*;
import java.util.List;

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

        //      Pestaña Calendarizacion
        CalendarizacionView calendarizacionView = new CalendarizacionView();
        CalendarizacionModel calendarizacionModel = new CalendarizacionModel();
        CalendarizacionController calendarizacionController = new CalendarizacionController(calendarizacionView,calendarizacionModel);

        //      Pestaña Actividades
        ActividadesView actividadesView = new ActividadesView();
        ActividadesModel actividadesModel = new ActividadesModel();
        ActividadesController actividadesController = new ActividadesController(actividadesView,actividadesModel);

        //      Pestaña Reservas
        // Obtener el usuario autenticado dinámicamente desde la Sesión
        Usuario usuarioActual = Sesion.getUsuario();

        Funcionario funcionarioActual = null;
        if (usuarioActual instanceof Funcionario) {funcionarioActual = (Funcionario) usuarioActual;}        ReservasView reservasView = new ReservasView();
        ReservasModel reservasModel = new ReservasModel();
        ReservasController reservasController = new ReservasController(reservasView,reservasModel, funcionarioActual);

        //      Pestaña Estadisticas
        ReservaService reservaService = new ReservaService();
        List<Reserva> listaReservas = reservaService.listarTodas();
        EstadisticasView estadisticasView = new EstadisticasView();
        EstadisticasModel estadisticasModel = new EstadisticasModel();
        EstadisticasController estadisticasController = new EstadisticasController(estadisticasView,estadisticasModel,listaReservas);

        switch (Sesion.getUsuario().getRol()) {
            case ADMIN:
                tabbedPane.addTab("Funcionarios", funcionarioView.getPanel());
                tabbedPane.addTab("Categorías", categoriasView.getPanel());
                tabbedPane.addTab("Recursos", recursosView.getPanel());
                tabbedPane.addTab("Calendarizacion", calendarizacionView);
                tabbedPane.addTab("Actividades", actividadesView);
                tabbedPane.addTab("Estadísticas", estadisticasView);
                break;
            case FUNCIONARIO:
                tabbedPane.addTab("Reservas", reservasView);
                tabbedPane.addTab("Calendarizacion", calendarizacionView);
                tabbedPane.addTab("Actividades", actividadesView);
                tabbedPane.addTab("Estadísticas", estadisticasView);
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
