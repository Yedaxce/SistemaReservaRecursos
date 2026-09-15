package reservas.presentation.actividadesP;

import reservas.presentation.actividadesP.ActividadesCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ActividadesView extends JPanel implements PropertyChangeListener {
    private JScrollPane JScrollPanel;
    private JTextField txtFechaRef;
    private JButton btnFecha;
    private JButton btnCargar;
    private JButton btnImprimir;
    private JTable tableActividades;

    private ActividadesController controller;
    private ActividadesModel model;

    public ActividadesView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel Filtros (Norte)
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Semana"));

        panelFiltros.add(new JLabel("Fecha de referencia:"));
        txtFechaRef = new JTextField(12);
        txtFechaRef.setEditable(false);
        txtFechaRef.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        panelFiltros.add(txtFechaRef);

        btnFecha = new JButton("...");
        panelFiltros.add(btnFecha);

        btnCargar = new JButton("Cargar");
        btnCargar.setIcon(cargarIcono("/iconos/check.png"));
        panelFiltros.add(btnCargar);

        btnImprimir = new JButton("Imprimir");
        btnImprimir.setIcon(cargarIcono("/iconos/pdf.png"));
        panelFiltros.add(btnImprimir);

        add(panelFiltros, BorderLayout.NORTH);

        // Tabla con JScrollPane (Centro)
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

        // Eventos
        btnCargar.addActionListener(e -> {
            if (controller != null) {
                LocalDate fecha = LocalDate.parse(txtFechaRef.getText());
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

        btnFecha.addActionListener(e -> {
            String nuevaFecha = JOptionPane.showInputDialog(this, "Ingrese fecha de referencia (AAAA-MM-DD):", txtFechaRef.getText());
            if (nuevaFecha != null && !nuevaFecha.isBlank()) {
                try {
                    LocalDate.parse(nuevaFecha);
                    txtFechaRef.setText(nuevaFecha);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Formato inválido. Use AAAA-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private ImageIcon cargarIcono(String ruta) {
        URL url = getClass().getResource(ruta);
        return (url != null) ? new ImageIcon(url) : null;
    }

    public void setController(ActividadesController controller) {
        this.controller = controller;
    }

    public void setModel(ActividadesModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ActividadesModel.TABLA.equals(evt.getPropertyName())) {
            ActividadesTableModel tableModel = new ActividadesTableModel(model.getFechaReferencia(), model.getReservas());
            tableActividades.setModel(tableModel);
        }
    }
}