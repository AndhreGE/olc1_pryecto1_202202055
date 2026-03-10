package arbol;

import entorno.Contexto;
import entorno.Entorno;

public class UsarBD implements Instruccion {
    private String nombre;

    public UsarBD(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public Object ejecutar(Entorno env) {
        // 1. Obtener la memoria global
        Contexto contexto = Contexto.getInstancia();
        
        // 2. ACTUALIZAR la base de datos activa
        contexto.dbActual = this.nombre;
        
        System.out.println("EJECUTANDO: Seleccionar Base de Datos " + nombre);
        System.out.println("✅ Contexto cambiado a: " + contexto.dbActual);
        
        return null;
    }
}