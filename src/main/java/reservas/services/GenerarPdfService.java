package reservas.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GenerarPdfService {

    public void generarPdf(String titulo, String[] encabezados, List<Object[]> filas, String ruta) throws IOException {
        // 1. Inicializar el escritor y el documento PDF
        PdfWriter writer = new PdfWriter(new FileOutputStream(ruta));
        PdfDocument pdf = new PdfDocument(writer);
        Document documento = new Document(pdf);

        try {
            // 2. Agregar el Título del reporte
            Paragraph pTitulo = new Paragraph(titulo)
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            documento.add(pTitulo);

            // 3. Crear la tabla dinámicamente según el número de encabezados
            int numColumnas = encabezados.length;
            Table tabla = new Table(UnitValue.createPercentArray(numColumnas)).useAllAvailableWidth();

            // 4. Agregar los Encabezados a la tabla
            for (String encabezado : encabezados) {
                Cell celdaEncabezado = new Cell()
                        .add(new Paragraph(encabezado).setBold())
                        .setTextAlignment(TextAlignment.CENTER);
                tabla.addHeaderCell(celdaEncabezado);
            }

            // 5. Agregar las Filas de datos de forma genérica
            for (Object[] fila : filas) {
                for (Object celda : fila) {
                    // Si el objeto es nulo, inserta un texto vacío para evitar errores
                    String textoCelda = (celda != null) ? celda.toString() : "";
                    tabla.addCell(new Cell().add(new Paragraph(textoCelda)));
                }
            }

            // 6. Añadir la tabla al documento
            documento.add(tabla);

        } finally {
            // 7. Cerrar el documento de forma segura
            documento.close();
        }
    }


}

