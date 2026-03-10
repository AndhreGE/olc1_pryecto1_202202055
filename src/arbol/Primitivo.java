package arbol;

import entorno.Entorno;
import entorno.Simbolo;
import entorno.Tipo;

public class Primitivo implements Expresion {
    private Object valor;
    private Tipo tipo;

    public Primitivo(Object valor, Tipo tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    @Override
    public Object evaluar(Entorno env) {
        // Devolvemos un Simbolo "anónimo" que contiene el valor y su tipo
        // Usamos el nombre "literal" porque no es una variable
        Simbolo dato = new Simbolo("literal", tipo);
        dato.valor = this.valor;
        return dato;
    }
}