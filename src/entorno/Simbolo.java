package entorno;

public class Simbolo {
    public String nombre;
    public Tipo tipo;
    public Object valor; 

    public Simbolo(String nombre, Tipo tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = null;
    }
}