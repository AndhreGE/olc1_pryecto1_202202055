package entorno;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Contexto {
    // Instancia única (Singleton)
    private static Contexto instancia;
    
    // Base de datos actual (Nombre -> Tabla)
    public HashMap<String, Tabla> tablas;
    public String dbActual;
    public String rutaArchivo;

    private Contexto() {
        tablas = new HashMap<>();
        dbActual = "";
        rutaArchivo = "";
    }

    public static Contexto getInstancia() {
        if (instancia == null) {
            instancia = new Contexto();
        }
        return instancia;
    }
    
    // --- NUEVO MÉTODO PARA GUARDAR ---
    public void actualizarArchivo() {
        if (rutaArchivo.isEmpty()) return;

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"database\": \"").append(dbActual).append("\",\n");
        json.append("  \"tables\": {\n");

        int contadorTablas = 0;
        for (Map.Entry<String, Tabla> entry : tablas.entrySet()) {
            Tabla t = entry.getValue();
            json.append("    \"").append(t.nombre).append("\": {\n");
            
            // 1. Schema (Columnas)
            json.append("      \"schema\": {\n");
            int contadorCols = 0;
            for (Map.Entry<String, Tipo> col : t.columnas.entrySet()) {
                json.append("        \"").append(col.getKey()).append("\": \"").append(col.getValue().toString().toLowerCase()).append("\"");
                if (contadorCols < t.columnas.size() - 1) json.append(",");
                json.append("\n");
                contadorCols++;
            }
            json.append("      },\n");

            // 2. Records (Filas)
            json.append("      \"records\": [\n");
            for (int i = 0; i < t.filas.size(); i++) {
                json.append("        {");
                Map<String, Object> fila = t.filas.get(i);
                int contadorCeldas = 0;
                for (Map.Entry<String, Object> celda : fila.entrySet()) {
                    String llave = celda.getKey();
                    Object valor = celda.getValue();
                    
                    json.append("\"").append(llave).append("\": ");
                    
                    // Si es string, poner comillas
                    if (valor instanceof String) {
                        json.append("\"").append(valor).append("\"");
                    } else {
                        json.append(valor);
                    }
                    
                    if (contadorCeldas < fila.size() - 1) json.append(", ");
                    contadorCeldas++;
                }
                json.append("}");
                if (i < t.filas.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("      ]\n");

            json.append("    }");
            if (contadorTablas < tablas.size() - 1) json.append(",");
            json.append("\n");
            contadorTablas++;
        }

        json.append("  }\n"); // Cierre tables
        json.append("}"); // Cierre json

        // Escribir al disco
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write(json.toString());
            // System.out.println("💾 Auto-guardado realizado en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar JSON: " + e.getMessage());
        }
    }
    
    public void limpiar() {
        tablas.clear();
        dbActual = "";
    }
}