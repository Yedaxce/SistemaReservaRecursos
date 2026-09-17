package reservas.presentation.funcionarioP;

import reservas.logic.Funcionario;
import reservas.services.FuncionarioService;
import reservas.services.GenerarPdfService;

import java.io.IOException;
import java.util.List;

public class FuncionarioController {
    private final FuncionarioView view;
    private final FuncionarioModel model;
    private final FuncionarioService service;
    private final GenerarPdfService pdfService;

    public FuncionarioController(FuncionarioView view, FuncionarioModel model) {
        this.view = view;
        this.model = model;
        this.service = new FuncionarioService();
        this.pdfService = new GenerarPdfService();
        view.setController(this);
        view.setModel(model);
        model.setLista(service.listarTodos());
    }

    public void crear(Funcionario funcionario) throws Exception {
        service.crear(funcionario);
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    public void actualizar(Funcionario funcionario) throws Exception {
        service.actualizar(funcionario);
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    public void eliminar(String id) {
        service.eliminar(id);
        model.setActual(null);
        model.setLista(service.listarTodos());
    }

    public void limpiar() {
        model.setActual(null);
    }

    public void seleccionar(int row) {
        if (row >= 0 && row < model.getLista().size()) {
            model.setActual(model.getLista().get(row));
        }
    }

    public void buscar(String id, String nombre) {
        boolean tieneId = id != null && !id.isBlank();
        boolean tieneNombre = nombre != null && !nombre.isBlank();

        if (!tieneId && !tieneNombre) {
            model.setLista(service.listarTodos());
        } else {
            List<Funcionario> resultados = service.listarTodos().stream()
                    .filter(f -> (!tieneId || f.getId().toLowerCase().contains(id.toLowerCase()))
                            && (!tieneNombre || f.getNombre().toLowerCase().contains(nombre.toLowerCase())))
                    .toList();
            model.setLista(resultados);
        }
    }

    public void imprimir(String ruta) throws IOException {
        String[] encabezados = {"Id", "Nombre", "Teléfono"};
        List<Object[]> filas = model.getLista().stream()
                .map(c -> new Object[]{c.getId(), c.getNombre(), c.getTelefono()})
                .toList();
        pdfService.generarPdf("Listado de Categorías", encabezados, filas, ruta);
    }

}
