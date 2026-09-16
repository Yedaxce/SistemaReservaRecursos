package reservas.presentation.estadisticasP;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EstadisticasView extends JPanel{
    private JButton btnCargarRecursos;
    private JButton btnCargarActividades;
    private JTable tableEstadisticasRecursos;
    private JTable tableEstadisticasActividades;
    private JScrollPane JScrollPaneActvidades;
    private JScrollPane JScrollPaneRecursos;
    private JPanel panelGraficoRecursos;
    private JPanel panelGraficoActividades;
    private DatePicker dpFechaDesdeRecursos;
    private DatePicker dpFechaHastaRecursos;
    private DatePicker dpFechaDesdeActividades;
    private DatePicker dpFechaHastaActividades;

    private EstadisticasRecursosTableModel tableModelRecursos;
    private EstadisticasActividadesTableModel tableModelActividades;

    public EstadisticasView() {
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        dpFechaDesdeRecursos = crearDatePicker();
        dpFechaHastaRecursos = crearDatePicker();
        dpFechaDesdeRecursos.setDate(LocalDate.now().minusMonths(1));
        dpFechaHastaRecursos.setDate(LocalDate.now());
        btnCargarRecursos = new JButton("Cargar");
        btnCargarRecursos.setIcon(cargarIcono("/icons/check.png"));

        tableModelRecursos = new EstadisticasRecursosTableModel();
        tableEstadisticasRecursos = new JTable(tableModelRecursos);
        tableEstadisticasRecursos.setShowHorizontalLines(true);
        tableEstadisticasRecursos.setShowVerticalLines(true);
        tableEstadisticasRecursos.setGridColor(Color.GRAY);
        JScrollPaneRecursos = new JScrollPane(tableEstadisticasRecursos);
        JScrollPaneRecursos.setPreferredSize(new Dimension(300, 110));

        panelGraficoRecursos = new JPanel(new BorderLayout());

        dpFechaDesdeActividades = crearDatePicker();
        dpFechaHastaActividades = crearDatePicker();
        dpFechaDesdeActividades.setDate(LocalDate.now().minusMonths(1));
        dpFechaHastaActividades.setDate(LocalDate.now());
        btnCargarActividades = new JButton("Cargar");
        btnCargarActividades.setIcon(cargarIcono("/icons/check.png"));

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
        panelFechas.add(new JLabel("Desde:"));
        panelFechas.add(dpFechaDesdeRecursos);
        panelFechas.add(new JLabel("Hasta:"));
        panelFechas.add(dpFechaHastaRecursos);
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
        panelFechas.add(new JLabel("Desde:"));
        panelFechas.add(dpFechaDesdeActividades);
        panelFechas.add(new JLabel("Hasta:"));
        panelFechas.add(dpFechaHastaActividades);
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

    private ImageIcon cargarIcono(String ruta) {
        URL url = getClass().getResource(ruta);
        return (url != null) ? new ImageIcon(url) : null;
    }

    public JButton getBtnCargarRecursos() { return btnCargarRecursos; }
    public JButton getBtnCargarActividades() { return btnCargarActividades; }
    public DatePicker getDpFechaDesdeRecursos() { return dpFechaDesdeRecursos; }
    public DatePicker getDpFechaHastaRecursos() { return dpFechaHastaRecursos; }
    public DatePicker getDpFechaDesdeActividades() { return dpFechaDesdeActividades; }
    public DatePicker getDpFechaHastaActividades() { return dpFechaHastaActividades; }

    private DatePicker crearDatePicker() {
        DatePicker datePicker = new DatePicker();
        DatePickerSettings settings = datePicker.getSettings();
        settings.setLocale(new Locale("es", "CR"));
        settings.setFormatForDatesCommonEra(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        return datePicker;
    }


}
