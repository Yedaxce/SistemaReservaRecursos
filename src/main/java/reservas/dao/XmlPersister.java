package reservas.dao;

import jakarta.xml.bind.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import reservas.logic.model.*;


public class XmlPersister {
    private final String path;
    private static XmlPersister theInstance;

    public static synchronized XmlPersister instance() {
        if (theInstance == null) {
            theInstance = new XmlPersister("datos.xml");
        }
        return theInstance;
    }

    public XmlPersister(String p) {
        this.path = p;
    }

    public Data load() throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            Data defaultData = new Data();
            store(defaultData);
            return defaultData;
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class, Administrador.class, Funcionario.class);
        FileInputStream is = new FileInputStream(path);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Data result = (Data) unmarshaller.unmarshal(is);
        is.close();
        return result;
    }

    public void store(Data d) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class, Administrador.class, Funcionario.class);
        FileOutputStream os = new FileOutputStream(path);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(d, os);
        os.flush();
        os.close();
    }
}