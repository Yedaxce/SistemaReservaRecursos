package reservas.dao;

import reservas.logic.model.CategoriaRecurso;
import reservas.logic.model.Recurso;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecursoDAO {
    private final String archivo = "recursos.xml";
    private final CategoriaRecursoDAO categoriaDAO;

    public RecursoDAO() {
        this.categoriaDAO = new CategoriaRecursoDAO();
        File file = new File(archivo);
        if (!file.exists()) {
            guardarEnXML(new ArrayList<>());
        }
    }

    public Recurso buscarPorId(String id) {
        if (id == null) return null;
        for (Recurso r : listarTodos()) {
            if (r.getId().equalsIgnoreCase(id)) return r;
        }
        return null;
    }

    public List<Recurso> listarPorCategoria(String idCategoria) {
        List<Recurso> res = new ArrayList<>();
        if (idCategoria == null) return res;
        for (Recurso r : listarTodos()) {
            if (r.getCategoria() != null && r.getCategoria().getId().equalsIgnoreCase(idCategoria)) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Recurso> listarTodos() {
        return cargarDesdeXML();
    }

    public void guardar(Recurso recurso) {
        List<Recurso> lista = cargarDesdeXML();
        lista.add(recurso);
        guardarEnXML(lista);
    }

    public void actualizar(Recurso recurso) {
        List<Recurso> lista = cargarDesdeXML();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equalsIgnoreCase(recurso.getId())) {
                lista.set(i, recurso);
                break;
            }
        }
        guardarEnXML(lista);
    }

    public void eliminar(String id) {
        List<Recurso> lista = cargarDesdeXML();
        lista.removeIf(r -> r.getId().equalsIgnoreCase(id));
        guardarEnXML(lista);
    }

    private List<Recurso> cargarDesdeXML() {
        List<Recurso> lista = new ArrayList<>();
        File file = new File(archivo);
        if (!file.exists()) return lista;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("recurso");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) n;
                    String id = elem.getElementsByTagName("id").item(0).getTextContent();
                    String desc = elem.getElementsByTagName("descripcion").item(0).getTextContent();
                    String idCat = elem.getElementsByTagName("categoriaId").item(0).getTextContent();

                    CategoriaRecurso cat = categoriaDAO.buscarPorId(idCat);
                    if (cat != null) {
                        lista.add(new Recurso(id, desc, cat));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void guardarEnXML(List<Recurso> lista) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("recursos");
            doc.appendChild(root);

            for (Recurso r : lista) {
                Element elemRec = doc.createElement("recurso");

                Element elemId = doc.createElement("id");
                elemId.appendChild(doc.createTextNode(r.getId()));
                elemRec.appendChild(elemId);

                Element elemDesc = doc.createElement("descripcion");
                elemDesc.appendChild(doc.createTextNode(r.getDescripcion()));
                elemRec.appendChild(elemDesc);

                Element elemCat = doc.createElement("categoriaId");
                elemCat.appendChild(doc.createTextNode(r.getCategoria() != null ? r.getCategoria().getId() : ""));
                elemRec.appendChild(elemCat);

                root.appendChild(elemRec);
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