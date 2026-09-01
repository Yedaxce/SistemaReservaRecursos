package reservas.dao;

import reservas.logic.model.Reserva;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public ReservaDAO() {}

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
        try {
            Data data = XmlPersister.instance().load();
            data.getReservas().add(reserva);
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Reserva reserva) {
        try {
            Data data = XmlPersister.instance().load();
            List<Reserva> lista = data.getReservas();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equalsIgnoreCase(reserva.getId())) {
                    lista.set(i, reserva);
                    break;
                }
            }
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(String id) {
        try {
            Data data = XmlPersister.instance().load();
            data.getReservas().removeIf(r -> r.getId().equalsIgnoreCase(id));
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generarNuevoId() {
        List<Reserva> lista = listarTodos();
        int max = 0;
        for (Reserva r : lista) {
            if (r.getId() != null && r.getId().startsWith("RES-")) {
                try {
                    int num = Integer.parseInt(r.getId().substring(4));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return String.format("RES-%06d", max + 1);
    }

    public List<Reserva> cargarDesdeXML() {
        try {
            return XmlPersister.instance().load().getReservas();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void guardarEnXML(List<Reserva> lista) {
        try {
            Data data = XmlPersister.instance().load();
            data.setReservas(lista);
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}