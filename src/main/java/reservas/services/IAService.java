package reservas.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class IAService {

    public IAService() {
    }

    public DatosReservaDTO extraerDatosReserva(String frase) {
        // Implementación base/mock para extracción de lenguaje natural
        return new DatosReservaDTO(
                "Reunión extraída de frase",
                LocalDate.now().plusDays(1),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                new ArrayList<>()
        );
    }
}