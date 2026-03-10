package arbol;

import entorno.Entorno;
import java.io.File;
import java.io.FileWriter;

public class CrearBD implements Instruccion {
    private String nombre;
    private String ruta;

    public CrearBD(String nombre, String ruta) {
        this.nombre = nombre;
        this.ruta = ruta;
    }

    @Override
    public Object ejecutar(Entorno env) {
        System.out.println("EJECUTANDO: Crear Base de Datos '" + nombre + "'");
        
        // 1. Crear el contenido JSON básico
        String contenido = "{\n" +
                           "  \"database\": \"" + nombre + "\",\n" +
                           "  \"tables\": []\n" +
                           "}";
        
        try {
            // 2. Crear el archivo físico
            File archivo = new File(ruta);
            
            // Asegurarnos que la carpeta exista
            if (archivo.getParentFile() != null) {
                archivo.getParentFile().mkdirs();
            }
            
            // 3. Escribir en el archivo
            FileWriter writer = new FileWriter(archivo);
            writer.write(contenido);
            writer.close();
            
            System.out.println("✅ Archivo creado exitosamente en: " + ruta);
            
        } catch (Exception e) {
            System.err.println(" Error al crear el archivo: " + e.getMessage());
        }
        
        return null;
    }
}