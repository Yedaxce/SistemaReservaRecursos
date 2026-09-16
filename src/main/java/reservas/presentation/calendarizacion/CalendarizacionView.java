package reservas.presentation.calendarizacion;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import reservas.logic.model.CategoriaRecurso;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CalendarizacionView extends JPanel implements PropertyChangeListener {    private JPanel panelPrincipalCalendarizacion;
    private JTable tableCalendarizacion;
    private JComboBox comboBoxCategoria;
    private JButton btnCargar;
    private JButton btnImprimir;
    private JScrollPane JScrollPane;
    private JPanel JPanelFiltros;
    private DatePicker dpFecha;

    private CalendarizacionController controller;
    private CalendarizacionModel model;

    public CalendarizacionView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        dpFecha = new DatePicker();
        dpFecha.setDate(LocalDate.now());
        DatePickerSettings settings = dpFecha.getSettings();
        settings.setLocale(new Locale("es", "CR"));
        settings.setFormatForDatesCommonEra(DateTimeFormatter.ofPattern("dd/MMMM/yyyy"));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        panelFiltros.add(new JLabel("Fecha de referencia:"));
        panelFiltros.add(dpFecha);

        panelFiltros.add(new JLabel("Categoría:"));
        comboBoxCategoria = new JComboBox<>();
        panelFiltros.add(comboBoxCategoria);

        btnCargar = new JButton("Cargar");
        btnCargar.setIcon(cargarIcono("/icons/check.png"));
        panelFiltros.add(btnCargar);

        btnImprimir = new JButton("Imprimir");
        btnImprimir.setIcon(cargarIcono("/icons/pdf.png"));
        panelFiltros.add(btnImprimir);
        add(panelFiltros, BorderLayout.NORTH);

        tableCalendarizacion.setRowHeight(25);
        tableCalendarizacion.setDefaultRenderer(Object.class, new CalendarizacionCellRenderer());
        tableCalendarizacion.setShowHorizontalLines(true);
        tableCalendarizacion.setShowVerticalLines(true);
        tableCalendarizacion.setGridColor(Color.GRAY);

        JScrollPane = new JScrollPane(tableCalendarizacion);
        JScrollPane.setBorder(BorderFactory.createTitledBorder("Calendarización de recursos"));
        add(JScrollPane, BorderLayout.CENTER);

        // Eventos
        btnCargar.addActionListener(e -> {
            if (controller != null) {
                CategoriaRecurso cat = (CategoriaRecurso) comboBoxCategoria.getSelectedItem();
                LocalDate fecha = dpFecha.getDate();
                controller.cargarCalendarizacion(fecha, cat);
            }
        });

        btnImprimir.addActionListener(e -> {
            if (controller != null) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        String ruta = fileChooser.getSelectedFile().getAbsolutePath() + ".pdf";
                        controller.imprimirReporte(ruta);
                        JOptionPane.showMessageDialog(this, "Reporte PDF generado exitosamente.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    private ImageIcon cargarIcono(String ruta) {
        URL url = getClass().getResource(ruta);
        return (url != null) ? new ImageIcon(url) : null;
    }
    public void setController(CalendarizacionController controller) {
        this.controller = controller;
    }

    public void setModel(CalendarizacionModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case CalendarizacionModel.CATEGORIAS:
                comboBoxCategoria.removeAllItems();
                for (CategoriaRecurso cat : model.getCategorias()) {
                    comboBoxCategoria.addItem(cat);
                }
                break;

            case CalendarizacionModel.TABLA:
                CalendarizacionTableModel tableModel = new CalendarizacionTableModel(model.getRecursos(), model.getReservas());
                tableCalendarizacion.setModel(tableModel);
                break;
        }
    }
}