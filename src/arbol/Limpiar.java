package arbol;

import entorno.Contexto;
import entorno.Entorno;
import entorno.Tabla;

public class Limpiar implements Instruccion {
    private String nombreTabla;

    public Limpiar(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    @Override
    public Object ejecutar(Entorno env) {
        Contexto contexto = Contexto.getInstancia();
        
        if (contexto.tablas.containsKey(nombreTabla)) {
            Tabla tabla = contexto.tablas.get(nombreTabla);
            tabla.filas.clear(); // Borra todas las filas de la lista
            
            contexto.actualizarArchivo();
            System.out.println("🗑️ Tabla '" + nombreTabla + "' vaciada correctamente.");
        } else {
            System.out.println("❌ Error: Tabla '" + nombreTabla + "' no existe.");
        }
        return null;
    }
}