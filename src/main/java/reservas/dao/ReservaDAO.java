package reservas.dao;

import reservas.logic.EstadoReserva;
import reservas.logic.Funcionario;
import reservas.logic.Recurso;
import reservas.logic.Reserva;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    private final String archivo = "reservas.xml";
    private final FuncionarioDAO funcionarioDAO;
    private final RecursoDAO recursoDAO;

    public ReservaDAO() {
        this.funcionarioDAO = new FuncionarioDAO();
        this.recursoDAO = new RecursoDAO();
        File file = new File(archivo);
        if (!file.exists()) {
            guardarEnXML(new ArrayList<>());
        }
    }

    public Reserva buscarPorId(String id) {
        if (id == null) return null;
        for (Reserva r : listarTodos()) {
            if (r.getId().equalsIgnoreCase(id)) return r;
        }
        return null;
    }

    public List<Reserva> listarPorFuncionario(String idFuncionario) {
        List<Reserva> res = new ArrayList<>();
        if (idFuncionario == null) return res;
        for (Reserva r : listarTodos()) {
            if (r.getFuncionario() != null && r.getFuncionario().getId().equalsIgnoreCase(idFuncionario)) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reserva> listarPorFecha(LocalDate fecha) {
        List<Reserva> res = new ArrayList<>();
        if (fecha == null) return res;
        for (Reserva r : listarTodos()) {
            if (r.getFecha() != null && r.getFecha().equals(fecha)) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reserva> listarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        List<Reserva> res = new ArrayList<>();
        if (desde == null || hasta == null) return res;
        for (Reserva r : listarTodos()) {
            if (r.getFecha() != null && !r.getFecha().isBefore(desde) && !r.getFecha().isAfter(hasta)) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reserva> listarActivas() {
        List<Reserva> res = new ArrayList<>();
        for (Reserva r : listarTodos()) {
            if (r.estaActiva()) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reserva> listarTodos() {
        return cargarDesdeXML();
    }

    public void guardar(Reserva reserva) {
        List<Reserva> lista = cargarDesdeXML();
        lista.add(reserva);
        guardarEnXML(lista);
    }

    public void actualizar(Reserva reserva) {
        List<Reserva> lista = cargarDesdeXML();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equalsIgnoreCase(reserva.getId())) {
                lista.set(i, reserva);
                break;
            }
        }
        guardarEnXML(lista);
    }

    public void eliminar(String id) {
        List<Reserva> lista = cargarDesdeXML();
        lista.removeIf(r -> r.getId().equalsIgnoreCase(id));
        guardarEnXML(lista);
    }

    public String generarNuevoId() {
        List<Reserva> lista = cargarDesdeXML();
        int max = 0;
        for (Reserva r : lista) {
            if (r.getId().startsWith("RES-")) {
                try {
                    int num = Integer.parseInt(r.getId().substring(4));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return String.format("RES-%06d", max + 1);
    }

    private List<Reserva> cargarDesdeXML() {
        List<Reserva> lista = new ArrayList<>();
        File file = new File(archivo);
        if (!file.exists()) return lista;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("reserva");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) n;
                    String id = elem.getElementsByTagName("id").item(0).getTextContent();
                    String actividad = elem.getElementsByTagName("actividad").item(0).getTextContent();
                    LocalDate fecha = LocalDate.parse(elem.getElementsByTagName("fecha").item(0).getTextContent());
                    LocalTime horaInicio = LocalTime.parse(elem.getElementsByTagName("horaInicio").item(0).getTextContent());
                    LocalTime horaFin = LocalTime.parse(elem.getElementsByTagName("horaFin").item(0).getTextContent());
                    String idFunc = elem.getElementsByTagName("funcionarioId").item(0).getTextContent();
                    String estadoStr = elem.getElementsByTagName("estado").item(0).getTextContent();

                    Funcionario func = funcionarioDAO.buscarPorId(idFunc);
                    if (func != null) {
                        Reserva r = new Reserva(id, actividad, fecha, horaInicio, horaFin, func);

                        if (EstadoReserva.CANCELADA.name().equalsIgnoreCase(estadoStr)) {
                            r.cancelar();
                        }

                        NodeList recNodes = elem.getElementsByTagName("recursoId");
                        for (int j = 0; j < recNodes.getLength(); j++) {
                            String idRec = recNodes.item(j).getTextContent();
                            Recurso rec = recursoDAO.buscarPorId(idRec);
                            if (rec != null) {
                                r.agregarRecurso(rec);
                            }
                        }
                        lista.add(r);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void guardarEnXML(List<Reserva> lista) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("reservas");
            doc.appendChild(root);

            for (Reserva r : lista) {
                Element elemRes = doc.createElement("reserva");

                Element elemId = doc.createElement("id");
                elemId.appendChild(doc.createTextNode(r.getId()));
                elemRes.appendChild(elemId);

                Element elemAct = doc.createElement("actividad");
                elemAct.appendChild(doc.createTextNode(r.getActividad()));
                elemRes.appendChild(elemAct);

                Element elemFecha = doc.createElement("fecha");
                elemFecha.appendChild(doc.createTextNode(r.getFecha().toString()));
                elemRes.appendChild(elemFecha);

                Element elemInicio = doc.createElement("horaInicio");
                elemInicio.appendChild(doc.createTextNode(r.getHoraInicio().toString()));
                elemRes.appendChild(elemInicio);

                Element elemFin = doc.createElement("horaFin");
                elemFin.appendChild(doc.createTextNode(r.getHoraFin().toString()));
                elemRes.appendChild(elemFin);

                Element elemFunc = doc.createElement("funcionarioId");
                elemFunc.appendChild(doc.createTextNode(r.getFuncionario() != null ? r.getFuncionario().getId() : ""));
                elemRes.appendChild(elemFunc);

                Element elemEstado = doc.createElement("estado");
                elemEstado.appendChild(doc.createTextNode(r.getEstado() != null ? r.getEstado().name() : EstadoReserva.ACTIVA.name()));
                elemRes.appendChild(elemEstado);

                Element elemRecursos = doc.createElement("recursos");
                if (r.getRecursos() != null) {
                    for (Recurso rec : r.getRecursos()) {
                        Element elemRecId = doc.createElement("recursoId");
                        elemRecId.appendChild(doc.createTextNode(rec.getId()));
                        elemRecursos.appendChild(elemRecId);
                    }
                }
                elemRes.appendChild(elemRecursos);

                root.appendChild(elemRes);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(archivo));
            t.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}