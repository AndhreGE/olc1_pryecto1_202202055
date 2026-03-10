package entorno;

public class Simbolo {
    public String nombre;
    public Tipo tipo;
    public Object valor;

    // Constructor 1: Solo nombre y tipo (usado en CrearTabla)
    public Simbolo(String nombre, Tipo tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = null;
    }

    // Constructor 2: Nombre, tipo y VALOR (usado en Operaciones y Filtros)
    public Simbolo(String nombre, Tipo tipo, Object valor) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
    }
}