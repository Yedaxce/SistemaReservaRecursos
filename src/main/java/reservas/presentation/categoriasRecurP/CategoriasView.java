package reservas.presentation.categoriasRecurP;

import reservas.logic.model.CategoriaRecurso;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class CategoriasView implements PropertyChangeListener {
    private JPanel panelPrincipalCategorias;
    private JTextField descripcionFld;
    private JButton buscarBtn;
    private JButton imprimirBtn;
    private JTextField idCatFld;
    private JTextField descripcionCatFld;
    private JPanel botonesCategoria;
    private JButton guardarBtn;
    private JButton borrarBtn;
    private JButton limpiarBtn;
    private JTable tablaCategorias;

    private CategoriasController controller;
    private CategoriasModel model;

    public CategoriasView() {
        registrarEventos();
    }

    private void registrarEventos() {
        buscarBtn.addActionListener(e -> controller.buscar(descripcionFld.getText().trim()));

        imprimirBtn.addActionListener(e -> onImprimir());

        guardarBtn.addActionListener(e -> onGuardar());

        borrarBtn.addActionListener(e -> onBorrar());

        limpiarBtn.addActionListener(e -> controller.limpiar());

        tablaCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCategorias.getSelectedRow() != -1) {
                controller.seleccionar(tablaCategorias.getSelectedRow());
            }
        });
    }

    private void onGuardar() {
        try {
            boolean esNueva = (model.getActual() == null);
            String descripcion = descripcionCatFld.getText().trim();

            if (esNueva) {
                // El ID lo genera el Service
                controller.crear(descripcion);
                JOptionPane.showMessageDialog(panelPrincipalCategorias, "Categoría creada correctamente.");
            } else {
                CategoriaRecurso cat = new CategoriaRecurso(idCatFld.getText().trim(), descripcion);
                controller.actualizar(cat);
                JOptionPane.showMessageDialog(panelPrincipalCategorias, "Categoría actualizada correctamente.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelPrincipalCategorias, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBorrar() {
        if (model.getActual() == null) {
            JOptionPane.showMessageDialog(panelPrincipalCategorias, "Seleccione una categoría de la tabla.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(panelPrincipalCategorias,
                "¿Eliminar la categoría " + model.getActual().getId() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            controller.eliminar(model.getActual().getId());
        }
    }

    private void onImprimir() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("categorias.pdf"));
        int resultado = chooser.showSaveDialog(panelPrincipalCategorias);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".pdf")) {
                ruta += ".pdf";
            }
            controller.imprimir(ruta);
            JOptionPane.showMessageDialog(panelPrincipalCategorias, "PDF generado correctamente en:\n" + ruta);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(panelPrincipalCategorias,
                    "No se pudo generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setController(CategoriasController controller) {
        this.controller = controller;
    }

    public void setModel(CategoriasModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public JPanel getPanel() {
        return panelPrincipalCategorias;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case CategoriasModel.LISTA: {
                int[] cols = {CategoriasTableModel.ID, CategoriasTableModel.DESCRIPCION};
                tablaCategorias.setModel(new CategoriasTableModel(cols, model.getLista()));
                break;
            }
            case CategoriasModel.ACTUAL: {
                CategoriaRecurso c = model.getActual();

                if (c == null) {
                    idCatFld.setText("");
                    descripcionCatFld.setText("");
                    tablaCategorias.clearSelection();

                    idCatFld.setEnabled(true);
                    idCatFld.setEditable(true);
                } else {
                    idCatFld.setText(c.getId());
                    descripcionCatFld.setText(c.getDescripcion());

                    // El ID NO es editable para proteger la llave primaria
                    idCatFld.setEnabled(false);
                    idCatFld.setEditable(false);
                }

                descripcionCatFld.setEnabled(true);
                descripcionCatFld.setEditable(true);
                break;
            }
        }
    }
}

