package reservas.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IAService {

    private static final String API_KEY = "AQ.Ab8RN6KdyAsO3cebsC7n_ZjxqyKMCUqOb0xS1hCSnTZs3IAKRw";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-8b:generateContent?key=" + API_KEY;
    public IAService() {
    }

    public DatosReservaDTO extraerDatosReserva(String frase) {
        // 1. Intentar extracción con la API de Gemini
        try {
            DatosReservaDTO dto = consultarLLM(frase);
            if (dto != null) {
                return dto;
            }
        } catch (Exception e) {
            System.err.println("API de Gemini no disponible (" + e.getMessage() + "). Cambiando a procesamiento local.");
        }

        // 2. Fallback local asistido por Regex si la API falla
        return extraerLocal(frase);
    }

    private DatosReservaDTO consultarLLM(String frase) throws Exception {
        LocalDate fechaHoy = LocalDate.now();

        // Prompt minimalista en una sola línea para agilizar la respuesta del modelo
        String prompt = "Extrae en JSON estricto sin markdown: {\"actividad\":\"texto\",\"fecha\":\"YYYY-MM-DD\",\"horaInicio\":\"HH:mm\",\"horaFin\":\"HH:mm\",\"categorias\":[\"cat1\"]}. Fecha actual: "
                + fechaHoy + ". Frase: \"" + frase.replace("\"", "'") + "\"";

        String escapedPrompt = prompt.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", " ");

        String jsonBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapedPrompt + "\"}]}]}";

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(4))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(5))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return parsearJsonRespuesta(response.body(), frase);
        }
        return null;
    }

    private DatosReservaDTO parsearJsonRespuesta(String jsonApi, String fraseOriginal) {
        try {
            String textContent = jsonApi;
            if (jsonApi.contains("\"text\":")) {
                int startText = jsonApi.indexOf("\"text\":") + 8;
                int endText = jsonApi.lastIndexOf("}");
                textContent = jsonApi.substring(startText, endText)
                        .replace("\\\"", "\"")
                        .replace("\\n", " ")
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();
            }

            // Actividad
            String actividad = "Reunión";
            Pattern actPattern = Pattern.compile("\"actividad\"\\s*:\\s*\"([^\"]+)\"");
            Matcher actMatcher = actPattern.matcher(textContent);
            if (actMatcher.find()) {
                actividad = actMatcher.group(1);
            }

            // Fecha
            LocalDate fecha = LocalDate.now().plusDays(1);
            Pattern datePattern = Pattern.compile("\"fecha\"\\s*:\\s*\"(\\d{4}-\\d{2}-\\d{2})\"");
            Matcher dateMatcher = datePattern.matcher(textContent);
            if (dateMatcher.find()) {
                fecha = LocalDate.parse(dateMatcher.group(1));
            }

            // Horas
            LocalTime horaInicio = LocalTime.of(8, 0);
            LocalTime horaFin = LocalTime.of(10, 0);

            Pattern iniPattern = Pattern.compile("\"horaInicio\"\\s*:\\s*\"(\\d{1,2}:\\d{2})\"");
            Matcher iniMatcher = iniPattern.matcher(textContent);
            if (iniMatcher.find()) {
                horaInicio = LocalTime.parse(normalizarHora(iniMatcher.group(1)));
            }

            Pattern finPattern = Pattern.compile("\"horaFin\"\\s*:\\s*\"(\\d{1,2}:\\d{2})\"");
            Matcher finMatcher = finPattern.matcher(textContent);
            if (finMatcher.find()) {
                horaFin = LocalTime.parse(normalizarHora(finMatcher.group(1)));
            }

            // Selección de categorías mediante el análisis léxico local sobre el texto original
            List<String> categorias = extraerCategoriasLocales(fraseOriginal);

            return new DatosReservaDTO(actividad, fecha, horaInicio, horaFin, categorias);
        } catch (Exception e) {
            return null;
        }
    }

    private String normalizarHora(String hora) {
        return hora.length() == 4 ? "0" + hora : hora;
    }

    private DatosReservaDTO extraerLocal(String frase) {
        String texto = frase.toLowerCase().trim();

        // 1. Extraer Actividad
        String actividad = "Reunión de trabajo";
        Pattern actPattern = Pattern.compile("(reunión|reunion|charla|capacitación|capacitacion|junta|sesión|sesion|taller|evento)(\\s+de\\s+|\\s+para\\s+|\\s+)([^0-9\\.\\,]+?)(?=\\s+el|\\s+de|\\s+a\\s+las|\\s+en|\\s+usando|\\s+\\d|$)");
        Matcher actMatcher = actPattern.matcher(texto);
        if (actMatcher.find()) {
            actividad = actMatcher.group(0).trim();
        }

        // 2. Extraer Fecha
        LocalDate hoy = LocalDate.now();
        LocalDate fecha = hoy.plusDays(1);

        if (texto.contains("hoy")) {
            fecha = hoy;
        } else if (texto.contains("mañana") || texto.contains("manana")) {
            fecha = hoy.plusDays(1);
        } else {
            Pattern fechaPattern = Pattern.compile("(\\d{1,2})\\s+de\\s+([a-z]+)");
            Matcher fechaMatcher = fechaPattern.matcher(texto);
            if (fechaMatcher.find()) {
                int dia = Integer.parseInt(fechaMatcher.group(1));
                int mes = obtenerNumeroMes(fechaMatcher.group(2));
                fecha = LocalDate.of(hoy.getYear(), mes, dia);
            }
        }

        // 3. Extraer Horarios
        LocalTime[] horarios = extraerHorarios(texto);

        // 4. Categorías locales
        List<String> categorias = extraerCategoriasLocales(texto);

        return new DatosReservaDTO(actividad, fecha, horarios[0], horarios[1], categorias);
    }

    private LocalTime[] extraerHorarios(String texto) {
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fin = LocalTime.of(10, 0);

        // 1. Eliminamos fechas ("18 de noviembre") para no confundir días con horas (18 -> 18:00)
        String textoSinFechas = texto.replaceAll("\\b\\d{1,2}\\s+de\\s+[a-z]+\\b", "");

        // 2. Buscar patrón de rango explícito ("8am a 10am", "8 a 10", "8:00 a 10:00")
        Pattern rangoPattern = Pattern.compile("(\\d{1,2})(?::(\\d{2}))?\\s*(am|pm|a\\.m\\.|p\\.m\\.)?\\s*(?:a|hasta|-)\\s*(\\d{1,2})(?::(\\d{2}))?\\s*(am|pm|a\\.m\\.|p\\.m\\.)?");
        Matcher rangoMatcher = rangoPattern.matcher(textoSinFechas);

        if (rangoMatcher.find()) {
            int h1 = Integer.parseInt(rangoMatcher.group(1));
            int m1 = rangoMatcher.group(2) != null ? Integer.parseInt(rangoMatcher.group(2)) : 0;
            String mer1 = rangoMatcher.group(3);

            int h2 = Integer.parseInt(rangoMatcher.group(4));
            int m2 = rangoMatcher.group(5) != null ? Integer.parseInt(rangoMatcher.group(5)) : 0;
            String mer2 = rangoMatcher.group(6);

            if (mer1 == null && mer2 != null) {
                mer1 = mer2;
            }

            if (mer1 != null && (mer1.contains("pm") || mer1.contains("p.m.")) && h1 < 12) h1 += 12;
            if (mer2 != null && (mer2.contains("pm") || mer2.contains("p.m.")) && h2 < 12) h2 += 12;

            return new LocalTime[]{LocalTime.of(h1, m1), LocalTime.of(h2, m2)};
        }

        // 3. Buscador individual de respaldo
        Pattern horaPattern = Pattern.compile("\\b(\\d{1,2})(?::(\\d{2}))?\\s*(am|pm|a\\.m\\.|p\\.m\\.)\\b");
        Matcher matcher = horaPattern.matcher(textoSinFechas);

        List<LocalTime> horas = new ArrayList<>();
        while (matcher.find()) {
            int h = Integer.parseInt(matcher.group(1));
            int m = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;
            String mer = matcher.group(3);

            if (mer.contains("pm") || mer.contains("p.m.")) {
                if (h < 12) h += 12;
            } else if (mer.contains("am") || mer.contains("a.m.")) {
                if (h == 12) h = 0;
            }
            horas.add(LocalTime.of(h, m));
        }

        if (horas.size() >= 2) {
            inicio = horas.get(0);
            fin = horas.get(1);
        } else if (horas.size() == 1) {
            inicio = horas.get(0);
            fin = inicio.plusHours(2);
        }

        return new LocalTime[]{inicio, fin};
    }

    private List<String> extraerCategoriasLocales(String texto) {
        List<String> categorias = new ArrayList<>();
        String t = texto.toLowerCase();

        // Extracción de Laptops
        if (t.contains("laptop") || t.contains("computadora") || t.contains("pc")) {
            categorias.add("Laptop windows");
        }

        // Extracción de Salas evaluando capacidad explícita
        if (t.contains("sala")) {
            if (t.contains("5 personas") || t.contains("5 pax") || t.contains("cinco personas") || t.contains("para 5")) {
                categorias.add("Sala para 5 personas");
            } else if (t.contains("10 personas") || t.contains("10 pax") || t.contains("diez personas") || t.contains("para 10")) {
                categorias.add("Sala para 10 personas");
            } else if (t.contains("junta")) {
                categorias.add("Sala de Juntas");
            } else {
                // Si no especifica número ni tipo, evalúa si viene la palabra '5' o '10'
                Pattern p5 = Pattern.compile("\\b5\\b");
                Pattern p10 = Pattern.compile("\\b10\\b");

                if (p5.matcher(t).find()) {
                    categorias.add("Sala para 5 personas");
                } else if (p10.matcher(t).find()) {
                    categorias.add("Sala para 10 personas");
                } else {
                    categorias.add("Sala para 10 personas"); // Opción por defecto genérica
                }
            }
        }

        // Extracción de Proyector
        if (t.contains("proyector") || t.contains("beam")) {
            categorias.add("Proyector HD");
        }

        return categorias;
    }

    private int obtenerNumeroMes(String mes) {
        Map<String, Integer> meses = new HashMap<>();
        meses.put("enero", 1); meses.put("febrero", 2); meses.put("marzo", 3);
        meses.put("abril", 4); meses.put("mayo", 5); meses.put("junio", 6);
        meses.put("julio", 7); meses.put("agosto", 8); meses.put("septiembre", 9);
        meses.put("setiembre", 9); meses.put("octubre", 10); meses.put("noviembre", 11);
        meses.put("diciembre", 12);

        return meses.getOrDefault(mes, LocalDate.now().getMonthValue());
    }
}