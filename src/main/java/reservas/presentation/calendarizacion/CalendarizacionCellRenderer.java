package reservas.presentation.calendarizacion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CalendarizacionCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (column > 0 && value != null && !value.toString().isBlank()) {
            c.setBackground(new Color(255, 255, 204)); // Fondo amarillo
        } else {
            c.setBackground(Color.WHITE);
        }
        return c;
    }
}