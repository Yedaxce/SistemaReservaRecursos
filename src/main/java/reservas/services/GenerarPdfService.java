package reservas.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Servicio genérico de generación de reportes en PDF.
 * No conoce Recurso, CategoriaRecurso ni ninguna otra clase de dominio --
 * cada Controller le arma sus propios encabezados y filas ya convertidas
 * a texto. Así, agregar un reporte nuevo (Funcionarios, Reservas...) nunca
 * requiere tocar esta clase.
 */

public class GenerarPdfService {


    public void generarPdf(String titulo, String[] encabezados, List<Object[]> filas, String ruta) throws IOException {
        try (PdfWriter writer = new PdfWriter(ruta);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            document.add(new Paragraph(titulo).setBold().setFontSize(16));

            float[] anchos = new float[encabezados.length];
            Arrays.fill(anchos, 1f); // todas las columnas con el mismo ancho relativo
            Table tabla = new Table(UnitValue.createPercentArray(anchos)).useAllAvailableWidth();

            for (String encabezado : encabezados) {
                tabla.addHeaderCell(new Cell().add(new Paragraph(encabezado).setBold()));
            }

            for (Object[] fila : filas) {
                for (Object valor : fila) {
                    tabla.addCell(new Cell().add(new Paragraph(valor == null ? "" : valor.toString())));
                }
            }

            document.add(tabla);
        }
    }

}
