package reservas.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IAService {

    private static final String API_KEY = "AQ.Ab8RN6KdyAsO3cebsC7n_ZjxqyKMCUqOb0xS1hCSnTZs3IAKRw";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public IAService() {
    }

    public DatosReservaDTO extraerDatosReserva(String frase) {
        if (frase == null || frase.isBlank()) {
            return new DatosReservaDTO("", LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(9, 0), new ArrayList<>());
        }

        // Intento 1: Invocación a API
        if (!API_KEY.isBlank()) {
            try {
                DatosReservaDTO resultadoIA = consultarLLM(frase);
                if (resultadoIA != null) {
                    return resultadoIA;
                }
            } catch (Exception e) {
                System.err.println("Advertencia: No se pudo contactar al servicio de IA externo. Utilizando motor de análisis local... " + e.getMessage());
            }
        }

        // Intento 2: Procesador local de respaldo basado en Regex
        return extraerFallbackLocal(frase);
    }

    private DatosReservaDTO consultarLLM(String frase) throws Exception {
        String prompt = "Extrae los datos de la siguiente frase de reserva de recursos."
                + " Responde UNICAMENTE con un objeto JSON sin formato Markdown ni bloques de codigo."
                + " Formato esperado: {\"actividad\": \"texto\", \"fecha\": \"YYYY-MM-DD\", \"horaInicio\": \"HH:MM\", \"horaFin\": \"HH:MM\", \"categorias\": [\"cat1\", \"cat2\"]}"
                + " Frase: \"" + frase + "\"";

        String jsonBody = "{\"contents\": [{\"parts\": [{\"text\": \"" + prompt.replace("\"", "\\\"") + "\"}]}]}";

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(5))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return parsearJsonRespuesta(response.body());
        }
        return null;
    }

    private DatosReservaDTO parsearJsonRespuesta(String json) {
        try {
            // Extraer actividad
            String actividad = "Reunión de trabajo";
            if (json.contains("\"actividad\"")) {
                int start = json.indexOf("\"actividad\"", json.indexOf("{")) + 12;
                int end = json.indexOf("\"", json.indexOf(":", start) + 1);
                actividad = json.substring(json.indexOf(":", start) + 2, end);
            }

            // Extraer fecha
            LocalDate fecha = LocalDate.now().plusDays(1);
            Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            Matcher dateMatcher = datePattern.matcher(json);
            if (dateMatcher.find()) {
                fecha = LocalDate.parse(dateMatcher.group());
            }

            // Extraer horas
            LocalTime horaInicio = LocalTime.of(8, 0);
            LocalTime horaFin = LocalTime.of(10, 0);
            Pattern timePattern = Pattern.compile("\\d{2}:\\d{2}");
            Matcher timeMatcher = timePattern.matcher(json);
            if (timeMatcher.find()) {
                horaInicio = LocalTime.parse(timeMatcher.group());
                if (timeMatcher.find()) {
                    horaFin = LocalTime.parse(timeMatcher.group());
                }
            }

            // Extraer categorías
            List<String> categorias = new ArrayList<>();
            if (json.toLowerCase().contains("laptop") || json.toLowerCase().contains("computadora")) {
                categorias.add("Laptop windows");
            }
            if (json.toLowerCase().contains("sala")) {
                categorias.add("Sala para 10 personas");
            }

            return new DatosReservaDTO(actividad, fecha, horaInicio, horaFin, categorias);
        } catch (Exception e) {
            return null;
        }
    }

    private DatosReservaDTO extraerFallbackLocal(String frase) {
        String texto = frase.toLowerCase();

        // 1. Actividad predeterminada
        String actividad = "Reunión de Trabajo";
        if (texto.contains("junta directiva")) {
            actividad = "Sesión de Junta Directiva";
        } else if (texto.contains("charla")) {
            actividad = "Charla Técnica";
        } else if (texto.contains("capacitación") || texto.contains("capacitacion")) {
            actividad = "Capacitación de Personal";
        }

        // 2. Extracción de Fecha
        LocalDate fecha = LocalDate.now().plusDays(1);
        if (texto.contains("mañana") || texto.contains("manana")) {
            fecha = LocalDate.now().plusDays(1);
        } else if (texto.contains("hoy")) {
            fecha = LocalDate.now();
        }

        // 3. Extracción de Horarios
        LocalTime horaInicio = LocalTime.of(8, 0);
        LocalTime horaFin = LocalTime.of(10, 0);

        Pattern horaPattern = Pattern.compile("(\\d{1,2})\\s*(am|pm|a\\.m\\.|p\\.m\\.)?");
        Matcher matcher = horaPattern.matcher(texto);

        List<Integer> horasEncontradas = new ArrayList<>();
        while (matcher.find()) {
            try {
                int h = Integer.parseInt(matcher.group(1));
                String mer = matcher.group(2);
                if (mer != null && (mer.contains("pm") || mer.contains("p.m.")) && h < 12) {
                    h += 12;
                }
                horasEncontradas.add(h);
            } catch (Exception ignored) {
            }
        }

        if (horasEncontradas.size() >= 2) {
            horaInicio = LocalTime.of(horasEncontradas.get(0), 0);
            horaFin = LocalTime.of(horasEncontradas.get(1), 0);
        } else if (horasEncontradas.size() == 1) {
            horaInicio = LocalTime.of(horasEncontradas.get(0), 0);
            horaFin = horaInicio.plusHours(2);
        }

        // 4. Extracción de Categorías
        List<String> categorias = new ArrayList<>();
        if (texto.contains("laptop") || texto.contains("computadora") || texto.contains("pc")) {
            categorias.add("Laptop windows");
        }
        if (texto.contains("sala")) {
            if (texto.contains("junta")) {
                categorias.add("Sala de Juntas");
            } else {
                categorias.add("Sala para 10 personas");
            }
        }
        if (texto.contains("proyector") || texto.contains("video beam")) {
            categorias.add("Proyector HD");
        }

        if (categorias.isEmpty()) {
            categorias.add("Sala para 10 personas");
        }

        return new DatosReservaDTO(actividad, fecha, horaInicio, horaFin, categorias);
    }
}