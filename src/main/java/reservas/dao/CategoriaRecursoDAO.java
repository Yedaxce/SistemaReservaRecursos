package reservas.dao;

import reservas.logic.CategoriaRecurso;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRecursoDAO {
    private final String archivo = "categorias.xml";

    public CategoriaRecursoDAO() {
        File file = new File(archivo);
        if (!file.exists()) {
            guardarEnXML(new ArrayList<>());
        }
    }

    public CategoriaRecurso buscarPorId(String id) {
        if (id == null) return null;
        for (CategoriaRecurso c : listarTodos()) {
            if (c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    public List<CategoriaRecurso> buscarPorDescripcion(String descripcion) {
        List<CategoriaRecurso> res = new ArrayList<>();
        if (descripcion == null) return res;
        for (CategoriaRecurso c : listarTodos()) {
            if (c.getDescripcion().toLowerCase().contains(descripcion.toLowerCase())) {
                res.add(c);
            }
        }
        return res;
    }

    public List<CategoriaRecurso> listarTodos() {
        return cargarDesdeXML();
    }

    public void guardar(CategoriaRecurso categoria) {
        List<CategoriaRecurso> lista = cargarDesdeXML();
        lista.add(categoria);
        guardarEnXML(lista);
    }

    public void actualizar(CategoriaRecurso categoria) {
        List<CategoriaRecurso> lista = cargarDesdeXML();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equalsIgnoreCase(categoria.getId())) {
                lista.set(i, categoria);
                break;
            }
        }
        guardarEnXML(lista);
    }

    public void eliminar(String id) {
        List<CategoriaRecurso> lista = cargarDesdeXML();
        lista.removeIf(c -> c.getId().equalsIgnoreCase(id));
        guardarEnXML(lista);
    }

    public String generarNuevoId() {
        List<CategoriaRecurso> lista = cargarDesdeXML();
        int max = 0;
        for (CategoriaRecurso c : lista) {
            if (c.getId().startsWith("CAT-")) {
                try {
                    int num = Integer.parseInt(c.getId().substring(4));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return String.format("CAT-%06d", max + 1);
    }

    private List<CategoriaRecurso> cargarDesdeXML() {
        List<CategoriaRecurso> lista = new ArrayList<>();
        File file = new File(archivo);
        if (!file.exists()) return lista;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("categoria");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) n;
                    String id = elem.getElementsByTagName("id").item(0).getTextContent();
                    String desc = elem.getElementsByTagName("descripcion").item(0).getTextContent();
                    lista.add(new CategoriaRecurso(id, desc));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void guardarEnXML(List<CategoriaRecurso> lista) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("categorias");
            doc.appendChild(root);

            for (CategoriaRecurso c : lista) {
                Element elemCat = doc.createElement("categoria");

                Element elemId = doc.createElement("id");
                elemId.appendChild(doc.createTextNode(c.getId()));
                elemCat.appendChild(elemId);

                Element elemDesc = doc.createElement("descripcion");
                elemDesc.appendChild(doc.createTextNode(c.getDescripcion()));
                elemCat.appendChild(elemDesc);

                root.appendChild(elemCat);
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