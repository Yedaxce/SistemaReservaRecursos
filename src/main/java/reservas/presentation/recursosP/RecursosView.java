package reservas.presentation.recursosP;

import reservas.logic.model.CategoriaRecurso;
import reservas.logic.model.Recurso;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.beans.PropertyChangeListener;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;

public class RecursosView implements PropertyChangeListener {
    private JPanel JPPrincipalRecursos;
    private JTextField descripcionRecursoFld;
    private JComboBox <CategoriaRecurso> categoriasBox;
    private JButton buscarButton;
    private JButton imprimirButton;
    private JPanel JPFiltro;

    private JPanel JPRecurso;
    private JTextField idRecursoFld;
    private JTextField descipRecursoFld;
    private JComboBox <CategoriaRecurso> categoriasRecurBox;
    private JButton guardarButton;
    private JButton borrarButton;
    private JButton limpiarButton;

    private JTable listaRecursosTable;
    private JScrollPane JScrollPane;

    private RecursosController controller;
    private RecursosModel model;


    public RecursosView() {
        registrarEventos();
    }

    private void registrarEventos() {
        buscarButton.addActionListener(e -> {
            CategoriaRecurso cat = (CategoriaRecurso) categoriasBox.getSelectedItem();
            controller.buscar(descripcionRecursoFld.getText().trim(), cat);
        });

        imprimirButton.addActionListener(e -> onImprimir());

        guardarButton.addActionListener(e -> onGuardar());

        borrarButton.addActionListener(e -> onBorrar());

        limpiarButton.addActionListener(e -> controller.limpiar());

        listaRecursosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaRecursosTable.getSelectedRow() != -1) {
                controller.seleccionar(listaRecursosTable.getSelectedRow());
            }
        });
    }

    private void onGuardar() {
        try {
            boolean esNuevo = (model.getActual() == null);
            CategoriaRecurso categoriaSeleccionada = (CategoriaRecurso) categoriasRecurBox.getSelectedItem();

            Recurso r = new Recurso(
                    idRecursoFld.getText().trim(),
                    descipRecursoFld.getText().trim(),
                    categoriaSeleccionada
            );

            if (esNuevo) {
                controller.crear(r);
                JOptionPane.showMessageDialog(JPPrincipalRecursos, "Recurso creado correctamente.");
            } else {
                controller.actualizar(r);
                JOptionPane.showMessageDialog(JPPrincipalRecursos, "Recurso actualizado correctamente.");
            }
        } catch (Exception ex) {
            // Cubre validaciones del modelo (Recurso) y errores del Service (ID duplicado, etc.)
            JOptionPane.showMessageDialog(JPPrincipalRecursos, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBorrar() {
        if (model.getActual() == null) {
            JOptionPane.showMessageDialog(JPPrincipalRecursos, "Seleccione un recurso de la tabla.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(JPPrincipalRecursos,
                "¿Eliminar el recurso " + model.getActual().getDescripcion() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            controller.eliminar(model.getActual().getId());
        }
    }

    private void onImprimir() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("recursos.pdf"));
        int resultado = chooser.showSaveDialog(JPPrincipalRecursos);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".pdf")) {
                ruta += ".pdf";
            }
            controller.imprimir(ruta);
            JOptionPane.showMessageDialog(JPPrincipalRecursos, "PDF generado correctamente en:\n" + ruta);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(JPPrincipalRecursos,
                    "No se pudo generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setController(RecursosController controller) {
        this.controller = controller;
    }

    public void setModel(RecursosModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    // para agregar la pastaña con JTabbedPane desde Application.java
    public JPanel getPanel() {
        return JPPrincipalRecursos;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case RecursosModel.LISTA: {
            int[] cols = {RecursosTableModel.ID, RecursosTableModel.DESCRIPCION, RecursosTableModel.CATEGORIA};
            listaRecursosTable.setModel(new RecursosTableModel(cols, model.getLista()));
            break;
        }
            case RecursosModel.CATEGORIAS: {
                DefaultComboBoxModel<CategoriaRecurso> modeloFiltro = new DefaultComboBoxModel<>();
                DefaultComboBoxModel<CategoriaRecurso> modeloForm = new DefaultComboBoxModel<>();
                for (CategoriaRecurso c : model.getCategorias()) {
                    modeloFiltro.addElement(c);
                    modeloForm.addElement(c);
                }
                categoriasBox.setModel(modeloFiltro);
                categoriasRecurBox.setModel(modeloForm);
                break;
            }
            case RecursosModel.ACTUAL: {
                Recurso r = model.getActual();
                if (r == null) {
                    idRecursoFld.setText("");
                    descipRecursoFld.setText("");
                    if (categoriasRecurBox.getItemCount() > 0) {
                        categoriasRecurBox.setSelectedIndex(0);
                    }
                    idRecursoFld.setEditable(true);
                    listaRecursosTable.clearSelection();
                } else {
                    idRecursoFld.setText(r.getId());
                    descipRecursoFld.setText(r.getDescripcion());
                    categoriasRecurBox.setSelectedItem(r.getCategoria());
                    idRecursoFld.setEditable(false); // el ID no cambia al editar
                }
                break;
            }
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
