package reservas.presentation.calendarizacion;

import reservas.logic.model.CategoriaRecurso;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarizacionView extends JPanel implements PropertyChangeListener {    private JPanel panelPrincipalCalendarizacion;
    private JTable tableCalendarizacion;
    private JTextField txtFecha;
    private JButton btnFecha;
    private JComboBox comboBoxCategoria;
    private JButton btnCargar;
    private JButton btnImprimir;
    private JScrollPane JScrollPane;
    private JPanel JPanelFiltros;

    private CalendarizacionController controller;
    private CalendarizacionModel model;

    public CalendarizacionView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        panelFiltros.add(new JLabel("Fecha:"));
        txtFecha = new JTextField(10);
        txtFecha.setEditable(false);
        txtFecha.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        panelFiltros.add(txtFecha);
        panelFiltros.add(btnFecha);

        panelFiltros.add(new JLabel("Categoría:"));
        comboBoxCategoria = new JComboBox<>();
        panelFiltros.add(comboBoxCategoria);

        panelFiltros.add(btnCargar);
        panelFiltros.add(btnImprimir);
        add(panelFiltros, BorderLayout.NORTH);

        tableCalendarizacion.setRowHeight(25);
        tableCalendarizacion.setDefaultRenderer(Object.class, new CalendarizacionCellRenderer());

        JScrollPane = new JScrollPane(tableCalendarizacion);
        JScrollPane.setBorder(BorderFactory.createTitledBorder("Calendarización de recursos"));
        add(JScrollPane, BorderLayout.CENTER);

        // Eventos
        btnCargar.addActionListener(e -> {
            if (controller != null) {
                CategoriaRecurso cat = (CategoriaRecurso) comboBoxCategoria.getSelectedItem();
                LocalDate fecha = LocalDate.parse(txtFecha.getText());
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

        btnFecha.addActionListener(e -> {
            // Ejemplo sencillo para cambiar/ingresar fecha vía dialogo
            String nuevaFecha = JOptionPane.showInputDialog(this, "Ingrese la fecha (AAAA-MM-DD):", txtFecha.getText());
            if (nuevaFecha != null && !nuevaFecha.isBlank()) {
                try {
                    LocalDate.parse(nuevaFecha);
                    txtFecha.setText(nuevaFecha);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
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