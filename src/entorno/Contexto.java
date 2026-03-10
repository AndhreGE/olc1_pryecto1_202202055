package entorno;
import java.util.HashMap;

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
    
    public void limpiar() {
        tablas.clear();
        dbActual = "";
    }
}