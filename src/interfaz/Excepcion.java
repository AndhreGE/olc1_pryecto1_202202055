package interfaz;

public class Excepcion {
    public String tipo;
    public String descripcion;
    public String linea;
    public String columna;

    public Excepcion(String tipo, String descripcion, String linea, String columna) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.linea = linea;
        this.columna = columna;
    }

    @Override
    public String toString() {
        return tipo + " - " + descripcion + " [" + linea + "," + columna + "]";
    }
}