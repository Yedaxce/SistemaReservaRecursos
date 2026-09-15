package reservas.presentation.estadisticasP;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class EstadisticasView extends JPanel{
    private JTextField txtFechaDesdeRecursos;
    private JButton btnFechaDesdeRecursos;
    private JTextField txtJFechaHastaRecursos;
    private JButton btnFechaHastaRecursos;
    private JButton btnCargarRecursos;
    private JButton btnCargarActividades;
    private JButton bntFechaHastaActividades;
    private JTextField txtFechaDesdeActividades;
    private JButton btnFechaDesdeActividades;
    private JTextField txtJFechaHastaActividades;
    private JTable tableEstadisticasRecursos;
    private JTable tableEstadisticasActividades;
    private JScrollPane JScrollPaneActvidades;
    private JScrollPane JScrollPaneRecursos;
    private JPanel panelGraficoRecursos;
    private JPanel panelGraficoActividades;

    private EstadisticasRecursosTableModel tableModelRecursos;
    private EstadisticasActividadesTableModel tableModelActividades;

    public EstadisticasView() {
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        txtFechaDesdeRecursos = new JTextField(10);
        txtFechaDesdeRecursos.setEditable(false);
        txtJFechaHastaRecursos = new JTextField(10);
        txtJFechaHastaRecursos.setEditable(false);

        tableModelRecursos = new EstadisticasRecursosTableModel();
        tableEstadisticasRecursos = new JTable(tableModelRecursos);
        tableEstadisticasRecursos.setShowHorizontalLines(true);
        tableEstadisticasRecursos.setShowVerticalLines(true);
        tableEstadisticasRecursos.setGridColor(Color.GRAY);
        JScrollPaneRecursos = new JScrollPane(tableEstadisticasRecursos);
        JScrollPaneRecursos.setPreferredSize(new Dimension(300, 110));

        panelGraficoRecursos = new JPanel(new BorderLayout());

        txtFechaDesdeActividades = new JTextField(10);
        txtFechaDesdeActividades.setEditable(false);
        txtJFechaHastaActividades = new JTextField(10);
        txtJFechaHastaActividades.setEditable(false);

        tableModelActividades = new EstadisticasActividadesTableModel();
        tableEstadisticasActividades = new JTable(tableModelActividades);
        tableEstadisticasActividades.setShowHorizontalLines(true);
        tableEstadisticasActividades.setShowVerticalLines(true);
        tableEstadisticasActividades.setGridColor(Color.GRAY);
        JScrollPaneActvidades = new JScrollPane(tableEstadisticasActividades);
        JScrollPaneActvidades.setPreferredSize(new Dimension(300, 110));

        panelGraficoActividades = new JPanel(new BorderLayout());
    }

    private void setupLayout() {
        setLayout(new GridLayout(1, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelRecursos());
        add(crearPanelActividades());
    }

    private JPanel crearPanelRecursos() {
        JPanel main = new JPanel(new BorderLayout(5, 5));
        main.setBorder(BorderFactory.createTitledBorder("Recursos"));

        JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFechas.setBorder(BorderFactory.createTitledBorder("Fechas Desde y Hasta"));
        panelFechas.add(txtFechaDesdeRecursos);
        panelFechas.add(btnFechaDesdeRecursos);
        panelFechas.add(txtJFechaHastaRecursos);
        panelFechas.add(btnFechaHastaRecursos);
        panelFechas.add(btnCargarRecursos);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        panelTabla.add(JScrollPaneRecursos, BorderLayout.CENTER);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(panelFechas, BorderLayout.NORTH);
        topContainer.add(panelTabla, BorderLayout.CENTER);

        panelGraficoRecursos.setBorder(BorderFactory.createTitledBorder("Gráfico"));

        main.add(topContainer, BorderLayout.NORTH);
        main.add(panelGraficoRecursos, BorderLayout.CENTER);

        return main;
    }

    private JPanel crearPanelActividades() {
        JPanel main = new JPanel(new BorderLayout(5, 5));
        main.setBorder(BorderFactory.createTitledBorder("Actividades"));

        JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFechas.setBorder(BorderFactory.createTitledBorder("Fechas Desde y Hasta"));
        panelFechas.add(txtFechaDesdeActividades);
        panelFechas.add(btnFechaDesdeActividades);
        panelFechas.add(txtJFechaHastaActividades);
        panelFechas.add(bntFechaHastaActividades);
        panelFechas.add(btnCargarActividades);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        panelTabla.add(JScrollPaneActvidades, BorderLayout.CENTER);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(panelFechas, BorderLayout.NORTH);
        topContainer.add(panelTabla, BorderLayout.CENTER);

        panelGraficoActividades.setBorder(BorderFactory.createTitledBorder("Gráfico"));

        main.add(topContainer, BorderLayout.NORTH);
        main.add(panelGraficoActividades, BorderLayout.CENTER);

        return main;
    }

    public void actualizarRecursos(Map<String, Integer> datos) {
        tableModelRecursos.setDatos(datos);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.addValue(entry.getValue(), "Recurso", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart("Recursos Usados", "", "Cantidad", dataset, PlotOrientation.VERTICAL, true, true, false);

        panelGraficoRecursos.removeAll();
        panelGraficoRecursos.add(new ChartPanel(chart), BorderLayout.CENTER);
        panelGraficoRecursos.revalidate();
        panelGraficoRecursos.repaint();
    }

    public void actualizarActividades(Map<String, Integer> datos) {
        tableModelActividades.setDatos(datos);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.addValue(entry.getValue(), "Semana", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart("Actividades Realizadas", "", "Cantidad", dataset, PlotOrientation.VERTICAL, true, true, false);

        panelGraficoActividades.removeAll();
        panelGraficoActividades.add(new ChartPanel(chart), BorderLayout.CENTER);
        panelGraficoActividades.revalidate();
        panelGraficoActividades.repaint();
    }

    public JButton getBtnCargarRecursos() { return btnCargarRecursos; }
    public JButton getBtnCargarActividades() { return btnCargarActividades; }
    public JButton getBtnFechaDesdeRecursos() { return btnFechaDesdeRecursos; }
    public JButton getBtnFechaHastaRecursos() { return btnFechaHastaRecursos; }
    public JButton getBtnFechaDesdeActividades() { return btnFechaDesdeActividades; }
    public JButton getBntFechaHastaActividades() { return bntFechaHastaActividades; }
    public JTextField getTxtFechaDesdeRecursos() { return txtFechaDesdeRecursos; }
    public JTextField getTxtJFechaHastaRecursos() { return txtJFechaHastaRecursos; }
    public JTextField getTxtFechaDesdeActividades() { return txtFechaDesdeActividades; }
    public JTextField getTxtJFechaHastaActividades() { return txtJFechaHastaActividades; }
}
