package reservas.presentation.login;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CambioContraView extends JDialog {
    private JPanel panel1;
    private JTextField txtClaveActual;
    private JButton btnCheck;
    private JButton btnCancelar;
    private JTextField txtVerificacionClaveNueva;
    private JTextField txtClaveNueva;

    private boolean aceptado = false;

    public CambioContraView(JDialog parent) {
        super(parent, "Cambiar Clave", true);
        setContentPane(panel1);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);

        btnCheck.addActionListener(e -> {
            this.aceptado = true;
            setVisible(false);
            dispose();
        });

        btnCancelar.addActionListener(e -> {
            this.aceptado = false;
            setVisible(false);
            dispose();
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                aceptado = false;
                dispose();
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            aceptado = false;
        }
        super.setVisible(b);
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public String getClaveActual() {
        return txtClaveActual.getText();
    }

    public String getClaveNueva() {
        return txtClaveNueva.getText();
    }

    public String getVerificacionClaveNueva() {
        return txtVerificacionClaveNueva.getText();
    }
}
