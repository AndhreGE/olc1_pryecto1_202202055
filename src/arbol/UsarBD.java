package arbol;
import entorno.Entorno;

public class UsarBD implements Instruccion {
    private String nombre;

    public UsarBD(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public Object ejecutar(Entorno env) {
        System.out.println("EJECUTANDO: Seleccionar Base de Datos " + nombre);
        return null;
    }
}