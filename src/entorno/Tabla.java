package entorno;

import java.util.HashMap;
import java.util.LinkedList;

public class Tabla {
    public String nombre;
    // Guardamos la definición de las columnas (ej: id -> int, nombre -> string)
    public HashMap<String, Tipo> columnas; 
    // Guardamos los datos. Cada fila es una lista de valores.
    public LinkedList<HashMap<String, Object>> filas;

    public Tabla(String nombre) {
        this.nombre = nombre;
        this.columnas = new HashMap<>();
        this.filas = new LinkedList<>();
    }
    
    public void agregarColumna(String nombre, Tipo tipo) {
        columnas.put(nombre, tipo);
    }
}