package main;

import analizadores.Lexico;
import analizadores.Sintactico;
import arbol.Instruccion;
import entorno.Entorno;
import java.io.FileReader;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        try {
            String ruta = "entradas/prueba.eli"; 
            
            Lexico lexer = new Lexico(new FileReader(ruta));
            Sintactico parser = new Sintactico(lexer);
            parser.parse(); // Analiza el archivo
            
            // Obtenemos la lista de instrucciones (AST) que generó el parser
            LinkedList<Instruccion> ast = parser.AST;
            
            if (ast != null) {
                System.out.println("--- EJECUCION ---");
                Entorno global = new Entorno();
                
                // Recorremos y ejecutamos cada instrucción
                for (Instruccion ins : ast) {
                    if (ins != null) {
                        ins.ejecutar(global);
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}