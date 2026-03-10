package arbol;
import entorno.Entorno;

public interface Expresion {
    // Método que devuelve un valor (ej: true, 5, "hola")
    public Object evaluar(Entorno env);
}