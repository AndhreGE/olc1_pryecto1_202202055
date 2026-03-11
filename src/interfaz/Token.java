package interfaz;

public class Token {
    public String lexema;
    public String tipo;
    public int linea;
    public int columna;

    public Token(String lexema, String tipo, int linea, int columna) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
    }
}