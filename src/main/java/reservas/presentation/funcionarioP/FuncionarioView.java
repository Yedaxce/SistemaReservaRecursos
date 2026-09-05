package reservas.presentation.funcionarioP;

import reservas.logic.model.Funcionario;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class FuncionarioView implements PropertyChangeListener {

    private JPanel panelPrincipalFuncionarios;
    private JTextField buscarIdFld;
    private JTextField buscarNombreFld;
    private JPanel panelBusqueda;
    private JButton imprimirBtn;
    private JButton buscarBtn;

    private JPanel panelFuncionario;
    private JTextField idAddFld;
    private JTextField nombreAddFld;
    private JTextField telefonoAddFld;
    private JButton guardarBtn;
    private JButton borrarBtn;
    private JButton limpiarBtn;
    private JTable tablaFuncionarios;
    private JPasswordField claveAddFld;
    private JPanel panelBotones;

    private FuncionarioController controller;
    private FuncionarioModel model;

    public FuncionarioView() {
        registrarEventos();
    }

    private void registrarEventos() {
        buscarBtn.addActionListener(e ->
                controller.buscar(buscarNombreFld.getText().trim()));

        guardarBtn.addActionListener(e -> onGuardar());

        borrarBtn.addActionListener(e -> onBorrar());

        limpiarBtn.addActionListener(e -> controller.limpiar());

        tablaFuncionarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaFuncionarios.getSelectedRow() != -1) {
                controller.seleccionar(tablaFuncionarios.getSelectedRow());
            }
        });
    }

    private void onGuardar() {
        try {
            boolean esNuevo = (model.getActual() == null);
            // ⚠ Construye el Funcionario leyendo tus campos reales del formulario
            Funcionario f = new Funcionario(
                    idAddFld.getText().trim(),
                    new String(claveAddFld.getPassword()),
                    nombreAddFld.getText().trim(),
                    telefonoAddFld.getText().trim()
            );
            if (esNuevo) {
                controller.crear(f);
                JOptionPane.showMessageDialog(panelPrincipalFuncionarios, "Funcionario creado correctamente.");
            } else {
                controller.actualizar(f);
                JOptionPane.showMessageDialog(panelPrincipalFuncionarios, "Funcionario actualizado correctamente.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelPrincipalFuncionarios, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBorrar() {
        if (model.getActual() == null) {
            JOptionPane.showMessageDialog(panelPrincipalFuncionarios, "Seleccione un funcionario de la tabla.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(panelPrincipalFuncionarios,
                "¿Eliminar al funcionario " + model.getActual().getNombre() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            controller.eliminar(model.getActual().getId());
        }
    }

    public void setController(FuncionarioController controller) {
        this.controller = controller;
    }

    public void setModel(FuncionarioModel model) {
        this.model = model;
        model.addPropertyChangeListener(this); // la Vista se vuelve "observadora" del Model
    }

    public JPanel getPanel() {
        return panelPrincipalFuncionarios;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case FuncionarioModel.LISTA: {
                int[] cols = {FuncionarioTableModel.ID, FuncionarioTableModel.NOMBRE,
                        FuncionarioTableModel.TELEFONO};
                tablaFuncionarios.setModel(new FuncionarioTableModel(cols, model.getLista()));
                break;
            }
            case FuncionarioModel.ACTUAL: {
                Funcionario f = model.getActual();
                if (f == null) {
                    // Modo "nuevo": limpiar formulario y habilitar el ID
                    idAddFld.setText("");
                    nombreAddFld.setText("");
                    telefonoAddFld.setText("");
                    claveAddFld.setText("");
                    idAddFld.setEditable(true);
                    tablaFuncionarios.clearSelection();
                } else {
                    // Modo "edición": el ID no se cambia (es la llave del registro)
                    idAddFld.setText(f.getId());
                    idAddFld.setEditable(false);
                    nombreAddFld.setText(f.getNombre());
                    telefonoAddFld.setText(f.getTelefono());
                    claveAddFld.setText(f.getClave());
                                    }
                break;
            }
        }
    }
}
