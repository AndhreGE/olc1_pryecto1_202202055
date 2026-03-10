package arbol;

import entorno.Entorno;
import entorno.Simbolo;
import entorno.Tipo;

public class Operacion implements Expresion {
    private Expresion izquierda;
    private Expresion derecha;
    private Operador operador;

    // Binaria (a > b)
    public Operacion(Expresion izquierda, Operador operador, Expresion derecha) {
        this.izquierda = izquierda;
        this.operador = operador;
        this.derecha = derecha;
    }

    // Unaria (!a)
    public Operacion(Operador operador, Expresion izquierda) {
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = null;
    }

    @Override
    public Object evaluar(Entorno env) {
        // 1. Evaluar operandos
        Object op1 = (izquierda != null) ? izquierda.evaluar(env) : null;
        Object op2 = (derecha != null) ? derecha.evaluar(env) : null;

        // 2. Extraer valores reales (desempaquetar Simbolo si es necesario)
        Object val1 = obtenerValor(op1);
        Object val2 = obtenerValor(op2);

        // 3. Aplicar lógica según operador
        switch (operador) {
            case IGUAL_IGUAL:
                return new Simbolo("res", Tipo.BOOLEAN, val1.equals(val2));
            case DIFERENTE:
                return new Simbolo("res", Tipo.BOOLEAN, !val1.equals(val2));
                
            // Operadores Numéricos (Convertimos todo a Double para comparar)
            case MAYOR:
                return new Simbolo("res", Tipo.BOOLEAN, aDouble(val1) > aDouble(val2));
            case MENOR:
                return new Simbolo("res", Tipo.BOOLEAN, aDouble(val1) < aDouble(val2));
            case MAYOR_IGUAL:
                return new Simbolo("res", Tipo.BOOLEAN, aDouble(val1) >= aDouble(val2));
            case MENOR_IGUAL:
                return new Simbolo("res", Tipo.BOOLEAN, aDouble(val1) <= aDouble(val2));
                
            // Operadores Lógicos
            case AND:
                return new Simbolo("res", Tipo.BOOLEAN, (boolean)val1 && (boolean)val2);
            case OR:
                return new Simbolo("res", Tipo.BOOLEAN, (boolean)val1 || (boolean)val2);
            case NOT:
                return new Simbolo("res", Tipo.BOOLEAN, !(boolean)val1);
                
            default:
                return null;
        }
    }

    // Auxiliar: Saca el valor de un Simbolo o lo deja pasar si ya es valor
    private Object obtenerValor(Object o) {
        if (o instanceof Simbolo) return ((Simbolo)o).valor;
        return o;
    }

    // Auxiliar: Convierte a Double para comparaciones numéricas
    private double aDouble(Object o) {
        if (o == null) return 0;
        return Double.parseDouble(o.toString());
    }
}