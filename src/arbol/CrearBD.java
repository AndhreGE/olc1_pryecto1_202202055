package arbol;

import entorno.Contexto; // <--- Importante
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
        
        // CORRECCIÓN 1: Inicializamos tables como objeto {} para ser consistente con Contexto
        String contenido = "{\n" +
                        "  \"database\": \"" + nombre + "\",\n" +
                        "  \"tables\": {}\n" + 
                        "}";
        
        try {
            File archivo = new File(ruta);
            
            if (archivo.getParentFile() != null) {
                archivo.getParentFile().mkdirs();
            }
            
            FileWriter writer = new FileWriter(archivo);
            writer.write(contenido);
            writer.close();
            
            System.out.println("Archivo creado exitosamente en: " + ruta);
            
        } catch (Exception e) {
            System.err.println("Error al crear el archivo: " + e.getMessage());
        }

        // CORRECCIÓN 2: Obtenemos la instancia antes de usarla
        Contexto contexto = Contexto.getInstancia(); 
        contexto.rutaArchivo = this.ruta;
        
        return null;
    }
}