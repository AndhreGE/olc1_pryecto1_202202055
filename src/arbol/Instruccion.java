package arbol;
import entorno.Entorno;

public interface Instruccion {
    // Método que ejecutará la acción (crear tabla, insertar, etc.)
    public Object ejecutar(Entorno env);
}