package analizadores;
import java_cup.runtime.Symbol;

%%

// --- Configuración JFlex ---
%class Lexico
%public
%line
%column
%cup
%unicode
%ignorecase

// AQUI ESTABA EL ERROR: Declaramos el estado exclusivo para comentarios
%xstate COMENTARIO

// --- Código de Usuario ---
%{
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }
    
    private Symbol symbol(int type) {
        return new Symbol(type, yyline+1, yycolumn+1);
    }
%}

// --- Expresiones Regulares ---
BLANCOS = [ \t\r\n]+
ENTERO = [0-9]+
DECIMAL = [0-9]+\.[0-9]+
ID = [a-zA-Z_][a-zA-Z0-9_]*
CADENA = \"[^\"]*\"

// Comentarios
COMENTARIO_LINEA = "##" [^\r\n]*

// CORRECCION: El enunciado dice que abre y cierra con lo mismo (#*)
COMENTARIO_BLOQUE_APERTURA = "#*"
COMENTARIO_BLOQUE_CIERRE = "#*"

%%

// --- Reglas Léxicas ---

// Reglas activas en el estado normal (YYINITIAL)
<YYINITIAL> {

    // 1. Palabras Reservadas
    "database"  { return symbol(sym.PR_DATABASE, yytext()); }
    "use"       { return symbol(sym.PR_USE, yytext()); }
    "table"     { return symbol(sym.PR_TABLE, yytext()); }
    "read"      { return symbol(sym.PR_READ, yytext()); }
    "fields"    { return symbol(sym.PR_FIELDS, yytext()); }
    "filter"    { return symbol(sym.PR_FILTER, yytext()); }
    "store"     { return symbol(sym.PR_STORE, yytext()); }
    "at"        { return symbol(sym.PR_AT, yytext()); }
    "export"    { return symbol(sym.PR_EXPORT, yytext()); }
    "add"       { return symbol(sym.PR_ADD, yytext()); }
    "update"    { return symbol(sym.PR_UPDATE, yytext()); }
    "set"       { return symbol(sym.PR_SET, yytext()); }
    "clear"     { return symbol(sym.PR_CLEAR, yytext()); }

    // 2. Tipos de Datos
    "int"       { return symbol(sym.TIP_INT, yytext()); }
    "string"    { return symbol(sym.TIP_STRING, yytext()); }
    "float"     { return symbol(sym.TIP_FLOAT, yytext()); }
    "bool"      { return symbol(sym.TIP_BOOL, yytext()); }
    "array"     { return symbol(sym.TIP_ARRAY, yytext()); }
    "object"    { return symbol(sym.TIP_OBJECT, yytext()); }
    
    // 3. Simbolos y Signos
    "{"         { return symbol(sym.LLAVE_A); }
    "}"         { return symbol(sym.LLAVE_C); }
    "("         { return symbol(sym.PAR_A); }
    ")"         { return symbol(sym.PAR_C); }
    "["         { return symbol(sym.COR_A); }
    "]"         { return symbol(sym.COR_C); }
    ","         { return symbol(sym.COMA); }
    ";"         { return symbol(sym.PUNTOCOMA); }
    ":"         { return symbol(sym.DOSPUNTOS); }
    "."         { return symbol(sym.PUNTO); }
    "*"         { return symbol(sym.ASTERISCO); } 
    "="         { return symbol(sym.IGUAL); } 

    // 4. Operadores Relacionales y Lógicos
    "=="        { return symbol(sym.IGUAL_IGUAL); }
    "!="        { return symbol(sym.DIFERENTE); }
    "<"         { return symbol(sym.MENOR); }
    ">"         { return symbol(sym.MAYOR); }
    "<="        { return symbol(sym.MENOR_IGUAL); }
    ">="        { return symbol(sym.MAYOR_IGUAL); }
    "&&"        { return symbol(sym.AND); }
    "||"        { return symbol(sym.OR); }
    "!"         { return symbol(sym.NOT); }

    // 5. Valores Literales
    "true"      { return symbol(sym.TRUE); }
    "false"     { return symbol(sym.FALSE); }
    "null"      { return symbol(sym.NULL); }
    
    {ENTERO}    { return symbol(sym.ENTERO, yytext()); }
    {DECIMAL}   { return symbol(sym.DECIMAL, yytext()); }
    {CADENA}    { return symbol(sym.CADENA, yytext()); }
    {ID}        { return symbol(sym.ID, yytext()); }

    // 6. Ignorar espacios y comentarios simples
    {BLANCOS}   { /* Ignorar */ }
    {COMENTARIO_LINEA} { /* Ignorar comentario de linea */ }
    
    // Al encontrar "#*", cambiamos al estado COMENTARIO
    {COMENTARIO_BLOQUE_APERTURA} { yybegin(COMENTARIO); }

}

// Reglas exclusivas para el estado COMENTARIO
<COMENTARIO> {
    // Si encontramos "#*", cerramos el comentario y volvemos al estado inicial
    {COMENTARIO_BLOQUE_CIERRE} { yybegin(YYINITIAL); }
    
    // Cualquier otro caracter ([^]) se ignora dentro del comentario
    [^] { /* Ignorar todo dentro del comentario */ }
}

// Error Léxico (Cualquier cosa no reconocida en estado inicial)
[^] { 
    System.err.println("Error lexico: " + yytext() + " en linea " + (yyline+1) + ", columna " + (yycolumn+1)); 
}