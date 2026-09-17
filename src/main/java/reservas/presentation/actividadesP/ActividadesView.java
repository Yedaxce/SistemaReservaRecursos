package reservas.presentation.actividadesP;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import reservas.presentation.actividadesP.ActividadesCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ActividadesView extends JPanel implements PropertyChangeListener {
    private JScrollPane JScrollPanel;
    private JButton btnCargar;
    private JButton btnImprimir;
    private JTable tableActividades;
    private DatePicker dpFecha;

    private ActividadesController controller;
    private ActividadesModel model;

    public ActividadesView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        dpFecha = new DatePicker();
        dpFecha.setDate(LocalDate.now());
        DatePickerSettings settings = dpFecha.getSettings();
        settings.setLocale(new Locale("es", "CR"));
        settings.setFormatForDatesCommonEra(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        // Panel Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Semana"));

        panelFiltros.add(new JLabel("Fecha de referencia:"));
        panelFiltros.add(dpFecha);

        btnCargar.setIcon(cargarIcono("/icons/check.png"));
        panelFiltros.add(btnCargar);

        btnImprimir.setIcon(cargarIcono("/icons/pdf.png"));
        panelFiltros.add(btnImprimir);

        add(panelFiltros, BorderLayout.NORTH);

        // Tabla con JScrollPane
        tableActividades = new JTable();
        tableActividades.setRowHeight(25);
        tableActividades.setDefaultRenderer(Object.class, new ActividadesCellRenderer());
        tableActividades.setShowGrid(true);
        tableActividades.setShowHorizontalLines(true);
        tableActividades.setShowVerticalLines(true);
        tableActividades.setGridColor(Color.GRAY);

        JScrollPanel = new JScrollPane(tableActividades);
        JScrollPanel.setBorder(BorderFactory.createTitledBorder("Actividades semanales"));
        add(JScrollPanel, BorderLayout.CENTER);


        btnCargar.addActionListener(e -> {
            if (controller != null) {
            LocalDate fecha = dpFecha.getDate();
            if (fecha == null) {
                JOptionPane.showMessageDialog(this, "seleccione una fecha de referencia", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            controller.cargarActividades(fecha);
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
                        JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    public void setController(ActividadesController controller) {
        this.controller = controller;
    }

    public void setModel(ActividadesModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    private ImageIcon cargarIcono(String ruta) {
        URL url = getClass().getResource(ruta);
        return (url != null) ? new ImageIcon(url) : null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ActividadesModel.TABLA.equals(evt.getPropertyName())) {
            ActividadesTableModel tableModel = new ActividadesTableModel(model.getFechaReferencia(), model.getReservas());
            tableActividades.setModel(tableModel);
        }
    }
}