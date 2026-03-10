package arbol;

import entorno.Contexto;
import entorno.Entorno;
import entorno.Simbolo;
import entorno.Tabla;
import entorno.Tipo;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Actualizar implements Instruccion {
    private String nombreTabla;
    private LinkedList<Simbolo> asignaciones; // Lista de (Columna, NuevaExpresion)
    private Expresion filtro;

    public Actualizar(String nombreTabla, LinkedList<Simbolo> asignaciones, Expresion filtro) {
        this.nombreTabla = nombreTabla;
        this.asignaciones = asignaciones;
        this.filtro = filtro;
    }

    @Override
    public Object ejecutar(Entorno env) {
        Contexto contexto = Contexto.getInstancia();
        
        if (!contexto.tablas.containsKey(nombreTabla)) {
            System.out.println("❌ Error: Tabla '" + nombreTabla + "' no encontrada.");
            return null;
        }

        Tabla tabla = contexto.tablas.get(nombreTabla);
        int modificados = 0;

        // Recorrer filas
        for (HashMap<String, Object> fila : tabla.filas) {
            
            // 1. Crear entorno temporal para evaluar filtro
            Entorno entornoFila = new Entorno();
            for (Map.Entry<String, Object> campo : fila.entrySet()) {
                Simbolo s = new Simbolo(campo.getKey(), null, campo.getValue());
                entornoFila.agregar(campo.getKey(), s);
            }

            // 2. Verificar filtro
            boolean pasaFiltro = true;
            if (filtro != null) {
                Simbolo res = (Simbolo) filtro.evaluar(entornoFila);
                pasaFiltro = (boolean) res.valor;
            }

            // 3. Si pasa, actualizar valores
            if (pasaFiltro) {
                for (Simbolo asignacion : asignaciones) {
                    String columna = asignacion.nombre;
                    if (!tabla.columnas.containsKey(columna)) {
                        System.out.println("❌ Error: Columna '" + columna + "' no existe.");
                        continue;
                    }

                    // Evaluar el nuevo valor (usando el entorno de la fila por si es edad = edad + 1)
                    Expresion exp = (Expresion) asignacion.valor;
                    Simbolo valorNuevo = (Simbolo) exp.evaluar(entornoFila);

                    // Validar Tipos (Simple)
                    if (tabla.columnas.get(columna) != valorNuevo.tipo) {
                         System.out.println("❌ Error Tipo: No se puede asignar " + valorNuevo.tipo + " a " + columna);
                         continue;
                    }

                    // Actualizar el mapa de la fila
                    fila.put(columna, valorNuevo.valor);
                }
                modificados++;
            }
        }

        // Guardar cambios
        contexto.actualizarArchivo();
        System.out.println("✅ Actualización en '" + nombreTabla + "': " + modificados + " registros modificados.");
        
        return null;
    }
}