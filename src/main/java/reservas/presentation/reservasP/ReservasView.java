package reservas.presentation.reservasP;

import reservas.logic.model.CategoriaRecurso;
import reservas.logic.model.Recurso;
import reservas.logic.model.Reserva;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReservasView extends JPanel implements PropertyChangeListener {
    private JButton btnImprimir;
    private JButton btnExtraer;
    private JTextField txtActividad;
    private JButton btnFecha;
    private JTextField txtFecha;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFin;
    private JTable tableMisReservas;
    private JTable tableCategorias;
    private JButton btnLimpiar;
    private JButton btnCancelar;
    private JButton btnReservar;
    private JTextArea txtFrase;
    private JComboBox cmbHoraInicio;
    private JComboBox cmbHoraFin;

    private JList<CategoriaRecurso> listCategorias;
    private ReservasController controller;
    private ReservasModel model;

        public ReservasView() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new BorderLayout(10, 10));

            // PANEL NORTE: Formulario Nueva Reserva
            JPanel panelNuevaReserva = new JPanel(new GridBagLayout());
            panelNuevaReserva.setBorder(BorderFactory.createTitledBorder("Nueva reserva"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(4, 4, 4, 4);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Fila 0: Frase + Botón Extraer
            gbc.gridx = 0;
            gbc.gridy = 0;
            panelNuevaReserva.add(new JLabel("Frase"), gbc);
            txtFrase = new JTextArea(2, 30);
            txtFrase.setLineWrap(true);
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            panelNuevaReserva.add(new JScrollPane(txtFrase), gbc);

            gbc.gridx = 4;
            gbc.gridwidth = 1;
            panelNuevaReserva.add(btnExtraer, gbc);

            // Fila 1: Actividad
            gbc.gridx = 0;
            gbc.gridy = 1;
            panelNuevaReserva.add(new JLabel("Actividad"), gbc);
            txtActividad = new JTextField(20);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            panelNuevaReserva.add(txtActividad, gbc);

            // Fila 2: Fecha / Horas
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 2;
            panelNuevaReserva.add(new JLabel("Fecha"), gbc);

            JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            txtFecha = new JTextField(8);
            btnFecha = new JButton("...");
            panelFecha.add(txtFecha);
            panelFecha.add(btnFecha);
            gbc.gridx = 1;
            panelNuevaReserva.add(panelFecha, gbc);

            gbc.gridx = 2;
            panelNuevaReserva.add(new JLabel("Hora inicio"), gbc);
            cmbHoraInicio = crearComboHoras();
            gbc.gridx = 3;
            panelNuevaReserva.add(cmbHoraInicio, gbc);

            // Fila 3: Horas Fin
            gbc.gridx = 2;
            gbc.gridy = 3;
            panelNuevaReserva.add(new JLabel("Hora fin"), gbc);
            cmbHoraFin = crearComboHoras();
            gbc.gridx = 3;
            panelNuevaReserva.add(cmbHoraFin, gbc);

            // Fila 4: Lista Categorías
            gbc.gridx = 0;
            gbc.gridy = 4;
            panelNuevaReserva.add(new JLabel("Categorías"), gbc);
            listCategorias = new JList<>(new DefaultListModel<>());
            listCategorias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane scrollCat = new JScrollPane(listCategorias);
            scrollCat.setPreferredSize(new Dimension(200, 80));
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            panelNuevaReserva.add(scrollCat, gbc);

            // Fila 5: Botones Acción Formulario
            JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            panelBotonesForm.add(btnReservar);
            panelBotonesForm.add(btnCancelar);
            panelBotonesForm.add(btnLimpiar);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 5;
            panelNuevaReserva.add(panelBotonesForm, gbc);

            add(panelNuevaReserva, BorderLayout.NORTH);

            // PANEL CENTRO: Mis Reservas (Tabla)
            JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
            panelTabla.setBorder(BorderFactory.createTitledBorder("Mis reservas"));

            tableMisReservas = new JTable();
            tableMisReservas.setRowHeight(22);
            panelTabla.add(new JScrollPane(tableMisReservas), BorderLayout.CENTER);

            JPanel panelEast = new JPanel(new FlowLayout());
            panelEast.add(btnImprimir);
            panelTabla.add(panelEast, BorderLayout.EAST);

            add(panelTabla, BorderLayout.CENTER);

            // EVENTOS
            tableMisReservas.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && tableMisReservas.getSelectedRow() != -1) {
                    int fila = tableMisReservas.getSelectedRow();
                    if (model != null && fila < model.getMisReservas().size()) {
                        Reserva seleccionada = model.getMisReservas().get(fila);

                        List<CategoriaRecurso> categoriasReserva = seleccionada.getRecursos().stream()
                                .map(Recurso::getCategoria)
                                .filter(Objects::nonNull)
                                .distinct()
                                .collect(Collectors.toList());

                        if (controller != null) {
                            model.actualizarFormulario(
                                    seleccionada.getActividad(),
                                    seleccionada.getFecha(),
                                    seleccionada.getHoraInicio(),
                                    seleccionada.getHoraFin(),
                                    categoriasReserva
                            );
                        }
                    }
                }
            });

            btnExtraer.addActionListener(e -> {
                if (controller != null && !txtFrase.getText().isBlank()) {
                    controller.extraerConIA(txtFrase.getText());
                }
            });

            btnReservar.addActionListener(e -> {
                if (controller != null) {
                    try {
                        String act = txtActividad.getText();
                        LocalDate f = LocalDate.parse(txtFecha.getText());
                        LocalTime hIn = LocalTime.parse(cmbHoraInicio.getSelectedItem().toString());
                        LocalTime hFin = LocalTime.parse(cmbHoraFin.getSelectedItem().toString());
                        List<CategoriaRecurso> cats = listCategorias.getSelectedValuesList();

                        controller.crearReserva(act, f, hIn, hFin, cats);
                    } catch (Exception ex) {
                        mostrarMensajeError("Asegúrese de ingresar los datos correctamente.");
                    }
                }
            });

            btnCancelar.addActionListener(e -> {
                if (controller != null) {
                    int row = tableMisReservas.getSelectedRow();
                    if (row >= 0) {
                        controller.cancelarReserva(row);
                    } else {
                        mostrarMensajeError("Seleccione una reserva de la tabla para cancelar.");
                    }
                }
            });

            btnLimpiar.addActionListener(e -> {
                if (tableMisReservas != null) {
                    tableMisReservas.clearSelection();
                }
                if (controller != null) controller.limpiarFormulario();
            });

            btnImprimir.addActionListener(e -> {
                if (controller != null) {
                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            controller.imprimirReporte(fileChooser.getSelectedFile().getAbsolutePath() + ".pdf");
                            mostrarMensajeInfo("Reporte generado exitosamente.");
                        } catch (Exception ex) {
                            mostrarMensajeError("Error al imprimir: " + ex.getMessage());
                        }
                    }
                }
            });

            btnFecha.addActionListener(e -> {
                String f = JOptionPane.showInputDialog(this, "Ingrese fecha (AAAA-MM-DD):", txtFecha.getText());
                if (f != null && !f.isBlank()) txtFecha.setText(f);
            });
        }

        private JComboBox<String> crearComboHoras() {
            JComboBox<String> combo = new JComboBox<>();
            for (int h = 6; h <= 20; h++) {
                combo.addItem(String.format("%02d:00", h));
            }
            return combo;
        }

        private ImageIcon cargarIcono(String ruta) {
            URL url = getClass().getResource(ruta);
            return (url != null) ? new ImageIcon(url) : null;
        }

        public void mostrarMensajeInfo(String msj) {
            JOptionPane.showMessageDialog(this, msj, "Información", JOptionPane.INFORMATION_MESSAGE);
        }

        public void mostrarMensajeError(String msj) {
            JOptionPane.showMessageDialog(this, msj, "Error", JOptionPane.ERROR_MESSAGE);
        }

        public void setController(ReservasController controller) {
            this.controller = controller;
        }

        public void setModel(ReservasModel model) {
            this.model = model;
            model.addPropertyChangeListener(this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            switch (evt.getPropertyName()) {
                case ReservasModel.CATEGORIAS:
                    DefaultListModel<CategoriaRecurso> listModel = new DefaultListModel<>();
                    model.getCategoriasDisponibles().forEach(listModel::addElement);
                    listCategorias.setModel(listModel);
                    break;

                case ReservasModel.DATOS_FORMULARIO:
                    // 1. Limpiar campos de texto
                    txtFrase.setText("");
                    txtActividad.setText(model.getActividad());

                    // 2. Dejar la fecha en blanco si el modelo la manda nula
                    if (model.getFecha() == null) {
                        txtFecha.setText("");
                    } else {
                        txtFecha.setText(model.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }

                    // 3. Dejar los ComboBox de hora sin selección (en blanco)
                    if (model.getHoraInicio() == null) {
                        cmbHoraInicio.setSelectedIndex(-1);
                    } else {
                        cmbHoraInicio.setSelectedItem(model.getHoraInicio().toString());
                    }

                    if (model.getHoraFin() == null) {
                        cmbHoraFin.setSelectedIndex(-1);
                    } else {
                        cmbHoraFin.setSelectedItem(model.getHoraFin().toString());
                    }

                    // 5. Manejo de deselección del JList de categorías
                    if (model.getCategoriasSeleccionadas() == null || model.getCategoriasSeleccionadas().isEmpty()) {
                        listCategorias.clearSelection();
                    } else {
                        DefaultListModel<CategoriaRecurso> currentModel = (DefaultListModel<CategoriaRecurso>) listCategorias.getModel();
                        List<Integer> indicesList = new ArrayList<>();

                        for (CategoriaRecurso catSeleccionada : model.getCategoriasSeleccionadas()) {
                            for (int i = 0; i < currentModel.getSize(); i++) {
                                if (currentModel.getElementAt(i).getId().equals(catSeleccionada.getId())) {
                                    indicesList.add(i);
                                    break;
                                }
                            }
                        }

                        int[] indices = indicesList.stream().mapToInt(Integer::intValue).toArray();
                        listCategorias.setSelectedIndices(indices);
                    }
                    break;

                case ReservasModel.TABLA_RESERVAS:
                    tableMisReservas.setModel(new ReservasTableModel(model.getMisReservas()));
                    break;
            }
        }
    }