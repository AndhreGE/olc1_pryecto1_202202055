package entorno;

import java.util.HashMap;

public class Entorno {
    // Mapa de variables (Simbolos)
    public HashMap<String, Simbolo> tablaSimbolos;

    public Entorno() {
        this.tablaSimbolos = new HashMap<>();
    }

    public void agregar(String nombre, Simbolo s) {
        tablaSimbolos.put(nombre, s);
    }

    public Simbolo buscar(String nombre) {
        return tablaSimbolos.get(nombre);
    }
}