package arbol;

import entorno.Contexto;
import entorno.Entorno;
import entorno.Simbolo;
import entorno.Tabla;
import java.util.LinkedList;

public class CrearTabla implements Instruccion {
    private String nombre;
    private LinkedList<Simbolo> columnas;

    public CrearTabla(String nombre, LinkedList<Simbolo> columnas) {
        this.nombre = nombre;
        this.columnas = columnas;
    }

    @Override
    public Object ejecutar(Entorno env) {
        // Obtenemos el Singleton (la memoria global)
        Contexto contexto = Contexto.getInstancia();
        
        // 1. Validar seguridad: ¿Tenemos una DB seleccionada?
        if (contexto.dbActual.isEmpty()) {
            System.out.println("❌ Error: No has seleccionado una base de datos. Usa 'USE <nombre>;'");
            return null;
        }

        System.out.println("EJECUTANDO: Crear Tabla '" + nombre + "' en base de datos " + contexto.dbActual);

        // 2. Verificar duplicados
        if (contexto.tablas.containsKey(nombre)) {
            System.out.println("⚠️ Advertencia: La tabla '" + nombre + "' ya existe.");
            return null;
        }

        // 3. Crear la tabla y configurar sus columnas
        Tabla nuevaTabla = new Tabla(nombre);
        for (Simbolo col : columnas) {
            nuevaTabla.agregarColumna(col.nombre, col.tipo);
        }

        // 4. Guardar en memoria
        contexto.tablas.put(nombre, nuevaTabla);
        System.out.println("✅ Tabla '" + nombre + "' creada exitosamente con " + columnas.size() + " columnas.");
        
        contexto.tablas.put(nombre, nuevaTabla);
        contexto.actualizarArchivo(); // <--- AGREGAR ESTO
        System.out.println("Tabla '" + nombre + "' creada...");
        return null;
    }
}