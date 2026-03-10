package arbol;

import entorno.Contexto;
import entorno.Entorno;
import entorno.Simbolo;
import entorno.Tabla;
import entorno.Tipo;
import java.util.HashMap;
import java.util.LinkedList;

public class Insertar implements Instruccion {
    private String nombreTabla;
    private LinkedList<Simbolo> listaValores; // Aquí vienen los pares id:expresion

    public Insertar(String nombreTabla, LinkedList<Simbolo> listaValores) {
        this.nombreTabla = nombreTabla;
        this.listaValores = listaValores;
    }

    @Override
    public Object ejecutar(Entorno env) {
        Contexto contexto = Contexto.getInstancia();
        
        // 1. Validar que la tabla exista
        if (!contexto.tablas.containsKey(nombreTabla)) {
            System.out.println("❌ Error: La tabla '" + nombreTabla + "' no existe.");
            return null;
        }

        Tabla tabla = contexto.tablas.get(nombreTabla);
        HashMap<String, Object> nuevoRegistro = new HashMap<>();

        // 2. Recorrer los valores que queremos insertar
        for (Simbolo s : listaValores) {
            String nombreColumna = s.nombre;
            
            // ¿Existe la columna en la tabla?
            if (!tabla.columnas.containsKey(nombreColumna)) {
                System.out.println("❌ Error: La columna '" + nombreColumna + "' no existe en la tabla " + nombreTabla);
                return null;
            }

            // Evaluar la expresión (ej: obtener el 22 de "edad: 22")
            // s.valor aquí es una Expresion (porque así lo guardaremos en el parser)
            Expresion exp = (Expresion) s.valor; 
            Simbolo valorEvaluado = (Simbolo) exp.evaluar(env);

            // Validar Tipos (Simple)
            Tipo tipoEsperado = tabla.columnas.get(nombreColumna);
            if (tipoEsperado != valorEvaluado.tipo) {
                System.out.println("❌ Error de Tipo: La columna '" + nombreColumna + "' espera " + tipoEsperado + " pero recibió " + valorEvaluado.tipo);
                return null;
            }

            // Guardar el dato limpio
            nuevoRegistro.put(nombreColumna, valorEvaluado.valor);
        }

        // 3. Insertar la fila en la tabla
        tabla.filas.add(nuevoRegistro);
        System.out.println("✅ Registro insertado en '" + nombreTabla + "'. Total filas: " + tabla.filas.size());
        
        tabla.filas.add(nuevoRegistro);
        contexto.actualizarArchivo(); // <--- AGREGAR ESTO
        System.out.println("Registro insertado...");
        return null;
    }
}
