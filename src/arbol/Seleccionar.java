package arbol;

import entorno.Contexto;
import entorno.Entorno;
import entorno.Simbolo;
import entorno.Tabla;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Seleccionar implements Instruccion {
    private String nombreTabla;
    private LinkedList<String> columnasAProyectar; // null si es *
    private Expresion filtro;

    public Seleccionar(String nombreTabla, LinkedList<String> columnasAProyectar, Expresion filtro) {
        this.nombreTabla = nombreTabla;
        this.columnasAProyectar = columnasAProyectar;
        this.filtro = filtro;
    }

    @Override
    public Object ejecutar(Entorno env) {
        Contexto contexto = Contexto.getInstancia();
        
        // 1. Validar tabla
        if (!contexto.tablas.containsKey(nombreTabla)) {
            System.out.println("❌ Error: Tabla '" + nombreTabla + "' no encontrada.");
            return null;
        }

        Tabla tabla = contexto.tablas.get(nombreTabla);
        System.out.println("\n--- RESULTADOS DE: " + nombreTabla + " ---");
        
        int cont = 0;
        
        // 2. Recorrer filas
        for (HashMap<String, Object> fila : tabla.filas) {
            
            // 3. Crear Entorno Local para la fila
            // Esto permite que cuando el filtro busque "edad", encuentre el valor de ESTA fila
            Entorno entornoFila = new Entorno();
            
            // Llenar el entorno con los datos de la fila actual
            for (Map.Entry<String, Object> campo : fila.entrySet()) {
                // Creamos simbolos temporales para que la Expresion los pueda leer
                Simbolo s = new Simbolo(campo.getKey(), null); // El tipo no importa mucho aqui para leer
                s.valor = campo.getValue();
                entornoFila.agregar(campo.getKey(), s); // Necesitamos implementar agregar en Entorno
            }

            // 4. Evaluar el filtro
            boolean pasaFiltro = true;
            if (filtro != null) {
                Simbolo resultado = (Simbolo) filtro.evaluar(entornoFila);
                pasaFiltro = (boolean) resultado.valor;
            }

            // 5. Si pasa el filtro, imprimir
            if (pasaFiltro) {
                imprimirFila(fila);
                cont++;
            }
        }
        System.out.println("---------------------------------");
        System.out.println("Total registros: " + cont + "\n");
        return null;
    }

    private void imprimirFila(HashMap<String, Object> fila) {
        StringBuilder sb = new StringBuilder();
        
        // Si columnasAProyectar es null, es un SELECT *
        if (columnasAProyectar == null) {
            for (Object val : fila.values()) {
                sb.append(val).append("\t | \t");
            }
        } else {
            // Si hay columnas especificas
            for (String col : columnasAProyectar) {
                if (fila.containsKey(col)) {
                    sb.append(fila.get(col)).append("\t | \t");
                }
            }
        }
        System.out.println(sb.toString());
    }
}