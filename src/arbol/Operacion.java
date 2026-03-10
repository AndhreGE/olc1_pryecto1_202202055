package arbol;

import entorno.Entorno;
import entorno.Simbolo;
import entorno.Tipo;

public class Operacion implements Expresion {
    private Expresion izquierda;
    private Expresion derecha;
    private Operador operador;

    // Constructor para operaciones binarias (a > b)
    public Operacion(Expresion izquierda, Operador operador, Expresion derecha) {
        this.izquierda = izquierda;
        this.operador = operador;
        this.derecha = derecha;
    }

    // Constructor para operaciones unarias (!a)
    public Operacion(Operador operador, Expresion izquierda) {
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = null;
    }

    @Override
    public Object evaluar(Entorno env) {
        // 1. Evaluar los lados
        // Nota: Si 'izquierda' es una variable (ID), el entorno debe devolver su valor actual.
        // Por ahora, asumimos que devuelve primitivos o símbolos.
        
        Object op1 = (izquierda != null) ? izquierda.evaluar(env) : null;
        Object op2 = (derecha != null) ? derecha.evaluar(env) : null;

        // Extraer valores reales (si vienen envueltos en Simbolo)
        Object val1 = (op1 instanceof Simbolo) ? ((Simbolo)op1).valor : op1;
        Object val2 = (op2 instanceof Simbolo) ? ((Simbolo)op2).valor : op2;

        switch (operador) {
            case MAYOR:
                return new Simbolo("res", Tipo.BOOLEAN, devolverNumero(val1) > devolverNumero(val2));
            case MENOR:
                return new Simbolo("res", Tipo.BOOLEAN, devolverNumero(val1) < devolverNumero(val2));
            case IGUAL_IGUAL:
                return new Simbolo("res", Tipo.BOOLEAN, val1.equals(val2));
            // ... Puedes agregar el resto (>=, <=, !=) aquí
            default:
                return null;
        }
    }

    // Método auxiliar para convertir cualquier cosa a Double y poder comparar
    private double devolverNumero(Object o) {
        if (o instanceof Integer) return (int) o;
        if (o instanceof Double) return (double) o;
        if (o instanceof String) return Double.parseDouble((String) o); // Por si acaso
        return 0;
    }
}