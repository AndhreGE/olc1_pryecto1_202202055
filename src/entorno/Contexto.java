package entorno;

import interfaz.Excepcion; // <--- IMPORTANTE: Para manejar los errores
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList; // <--- IMPORTANTE: Para la lista
import java.util.Map;

public class Contexto {
    // Instancia única (Singleton)
    private static Contexto instancia;
    
    // Base de datos actual
    public HashMap<String, Tabla> tablas;
    public String dbActual;
    public String rutaArchivo;
    
    // Lista de errores (Lexicos, Sintacticos, Semanticos)
    public LinkedList<Excepcion> errores;

    // Constructor privado
    private Contexto() {
        tablas = new HashMap<>();
        dbActual = "";
        rutaArchivo = "";
        errores = new LinkedList<>(); // Inicializamos la lista
    }

    // Método Singleton
    public static Contexto getInstancia() {
        if (instancia == null) {
            instancia = new Contexto();
        }
        return instancia;
    }

    // Método para agregar un error a la lista
    public void agregarError(String tipo, String desc, String linea, String columna) {
        errores.add(new Excepcion(tipo, desc, linea, columna));
    }
    
    // Sobrecarga por si pasas int en vez de String
    public void agregarError(String tipo, String desc, int linea, int columna) {
        errores.add(new Excepcion(tipo, desc, String.valueOf(linea), String.valueOf(columna)));
    }

    // Método para guardar el JSON (Persistencia)
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
            
            // 1. Schema
            json.append("      \"schema\": {\n");
            int contadorCols = 0;
            for (Map.Entry<String, Tipo> col : t.columnas.entrySet()) {
                json.append("        \"").append(col.getKey()).append("\": \"").append(col.getValue().toString().toLowerCase()).append("\"");
                if (contadorCols < t.columnas.size() - 1) json.append(",");
                json.append("\n");
                contadorCols++;
            }
            json.append("      },\n");

            // 2. Records
            json.append("      \"records\": [\n");
            for (int i = 0; i < t.filas.size(); i++) {
                json.append("        {");
                Map<String, Object> fila = t.filas.get(i);
                int contadorCeldas = 0;
                for (Map.Entry<String, Object> celda : fila.entrySet()) {
                    String llave = celda.getKey();
                    Object valor = celda.getValue();
                    
                    json.append("\"").append(llave).append("\": ");
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
        } catch (IOException e) {
            System.err.println("❌ Error al guardar JSON: " + e.getMessage());
        }
    }
    
    public void limpiar() {
        tablas.clear();
        dbActual = "";
        errores.clear();
    }
}