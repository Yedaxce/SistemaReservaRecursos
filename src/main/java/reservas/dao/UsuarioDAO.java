package reservas.dao;

import reservas.logic.model.Administrador;
import reservas.logic.model.Funcionario;
import reservas.logic.model.Rol;
import reservas.logic.model.Usuario;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final String archivo = "usuarios.xml";

    public UsuarioDAO() {
        File file = new File(archivo);
        if (!file.exists()) {
            guardarEnXML(new ArrayList<>());
        }
    }

    public Usuario buscarPorId(String id) {
        if (id == null || id.isBlank()) return null;
        List<Usuario> lista = cargarDesdeXML();
        for (Usuario u : lista) {
            if (u.getId().equalsIgnoreCase(id)) {
                return u;
            }
        }
        return null;
    }

    public Usuario validarLogin(String id, String clave) {
        Usuario u = buscarPorId(id);
        if (u != null && u.validarClave(clave)) {
            return u;
        }
        return null;
    }

    public boolean actualizarClave(String id, String nuevaClave) {
        List<Usuario> lista = cargarDesdeXML();
        for (Usuario u : lista) {
            if (u.getId().equalsIgnoreCase(id)) {
                u.setClave(nuevaClave);
                guardarEnXML(lista);
                return true;
            }
        }
        return false;
    }

    public List<Usuario> listarTodos() {
        return cargarDesdeXML();
    }

    public void guardar(Usuario usuario) {
        List<Usuario> lista = cargarDesdeXML();
        lista.removeIf(u -> u.getId().equalsIgnoreCase(usuario.getId()));
        lista.add(usuario);
        guardarEnXML(lista);
    }

    public void eliminar(String id) {
        List<Usuario> lista = cargarDesdeXML();
        lista.removeIf(u -> u.getId().equalsIgnoreCase(id));
        guardarEnXML(lista);
    }

    public List<Usuario> cargarDesdeXML() {
        List<Usuario> usuarios = new ArrayList<>();
        File file = new File(archivo);
        if (!file.exists()) return usuarios;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("usuario");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    String id = elem.getElementsByTagName("id").item(0).getTextContent();
                    String clave = elem.getElementsByTagName("clave").item(0).getTextContent();
                    String rolStr = elem.getElementsByTagName("rol").item(0).getTextContent();

                    if (Rol.ADMIN.name().equalsIgnoreCase(rolStr)) {
                        usuarios.add(new Administrador(id, clave));
                    } else if (Rol.FUNCIONARIO.name().equalsIgnoreCase(rolStr)) {
                        String nombre = elem.getElementsByTagName("nombre").item(0).getTextContent();
                        String telefono = elem.getElementsByTagName("telefono").item(0).getTextContent();
                        usuarios.add(new Funcionario(id, clave, nombre, telefono));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void guardarEnXML(List<Usuario> lista) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("usuarios");
            doc.appendChild(root);

            for (Usuario u : lista) {
                Element elemUsuario = doc.createElement("usuario");

                Element elemId = doc.createElement("id");
                elemId.appendChild(doc.createTextNode(u.getId()));
                elemUsuario.appendChild(elemId);

                Element elemClave = doc.createElement("clave");
                elemClave.appendChild(doc.createTextNode(u.getClave()));
                elemUsuario.appendChild(elemClave);

                Element elemRol = doc.createElement("rol");
                elemRol.appendChild(doc.createTextNode(u.getRol().name()));
                elemUsuario.appendChild(elemRol);

                if (u instanceof Funcionario) {
                    Funcionario f = (Funcionario) u;
                    Element elemNombre = doc.createElement("nombre");
                    elemNombre.appendChild(doc.createTextNode(f.getNombre() != null ? f.getNombre() : ""));
                    elemUsuario.appendChild(elemNombre);

                    Element elemTel = doc.createElement("telefono");
                    elemTel.appendChild(doc.createTextNode(f.getTelefono() != null ? f.getTelefono() : ""));
                    elemUsuario.appendChild(elemTel);
                }

                root.appendChild(elemUsuario);
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