package main;

public class Generador {
    public static void main(String[] args) {
        generarCompilador();
    }

    private static void generarCompilador() {
        try {
            String ruta = "src/analizadores/";
            
            // 1. Generar Analizador Léxico (JFlex)
            String[] opJFlex = { ruta + "Lexico.jflex", "-d", ruta };
            jflex.Main.generate(opJFlex);
            
            // 2. Generar Analizador Sintáctico (CUP)
            String[] opCup = { "-destdir", ruta, "-parser", "Sintactico", ruta + "Sintactico.cup" };
            java_cup.Main.main(opCup);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}