package reservas.presentation.categoriasRecurP;

import reservas.logic.CategoriaRecurso;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

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

        // implementar impresión a PDF con iText7 (ver pom.xml),
        // igual que en Recursos. Queda sin acción por ahora.
        // imprimirBtn.addActionListener(e -> {});

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
                // El ID lo genera el Service -- no se lee de idCatFld al crear.
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

    public void setController(CategoriasController controller) {
        this.controller = controller;
    }

    public void setModel(CategoriasModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    // ⚠ Necesario para insertar esta Vista en el JTabbedPane desde Application.java
    // (ej. tabbedPane.addTab("Categorías", categoriasView.getPanel())).
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
                } else {
                    idCatFld.setText(c.getId());
                    descripcionCatFld.setText(c.getDescripcion());
                }
                // idCatFld siempre queda de solo lectura: el ID nunca lo escribe
                // el usuario, ni al crear (lo genera el Service) ni al editar.
                idCatFld.setEditable(false);
                break;
            }
        }
    }
}

