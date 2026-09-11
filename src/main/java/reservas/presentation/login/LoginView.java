package reservas.presentation.login;

import javax.swing.*;
import java.awt.event.*;

public class LoginView extends JDialog {
    private JPanel logPane;
    private JButton btnEntrar;
    private JButton btnCancelar;
    private JTextField fldLogId;
    private JPasswordField fldPClave;

    private LoginController controller;
    private LoginModel model;

    public LoginView() {
        setResizable(false);

        setContentPane(logPane);
        setModal(true);
        getRootPane().setDefaultButton(btnEntrar);

        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logIn();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        logPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void logIn() {
        try {
            String id = fldLogId.getText().trim();
            String clave = new String(fldPClave.getPassword());
            controller.login(id, clave);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de acceso", JOptionPane.ERROR_MESSAGE);
            fldPClave.setText("");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setController(LoginController controller) {
        this.controller = controller;
    }

    public void setModel(LoginModel model) {
        this.model = model;
    }
}
