# Definición de la Gramática (Notación BNF)

La siguiente sección detalla la estructura sintáctica del lenguaje ELI, procesada mediante el analizador LALR generado por CUP.

## 1. Símbolos Terminales (Tokens)
Los componentes léxicos básicos definidos en la gramática son:

`PR_DATABASE`, `PR_USE`, `PR_TABLE`, `PR_READ`, `PR_FIELDS`, `PR_FILTER`, `PR_STORE`, `PR_AT`, `PR_EXPORT`, `PR_ADD`, `PR_UPDATE`, `PR_SET`, `PR_CLEAR`, `TIP_INT`, `TIP_STRING`, `TIP_FLOAT`, `TIP_BOOL`, `TIP_ARRAY`, `TIP_OBJECT`, `LLAVE_A`, `LLAVE_C`, `PAR_A`, `PAR_C`, `COR_A`, `COR_C`, `COMA`, `PUNTOCOMA`, `DOSPUNTOS`, `PUNTO`, `ASTERISCO`, `IGUAL`, `IGUAL_IGUAL`, `DIFERENTE`, `MENOR`, `MAYOR`, `MENOR_IGUAL`, `MAYOR_IGUAL`, `AND`, `OR`, `NOT`, `TRUE`, `FALSE`, `NULL`, `ENTERO`, `DECIMAL`, `CADENA`, `ID`.

---

## 2. Producciones de la Gramática

### Inicio y Flujo Principal
* `<inicio>` ::= `<instrucciones>`
* `<instrucciones>` ::= `<instrucciones>` `<instruccion>` | `<instruccion>` 
* `<instruccion>` ::= `<def_database>` 
    | `<sentencia_use>` 
    | `<crear_tabla>` 
    | `<sentencia_read>` 
    | `<sentencia_add>` 
    | `<sentencia_update>` 
    | `<sentencia_clear>` 
    | `<sentencia_export>` 

### Definición de Datos y Estructuras
* `<def_database>` ::= `PR_DATABASE` `ID` `LLAVE_A` `PR_STORE` `PR_AT` `CADENA` `PUNTOCOMA` `LLAVE_C`

* `<sentencia_use>` ::= `PR_USE` `ID` `PUNTOCOMA` 

* `<crear_tabla>` ::= `PR_TABLE` `ID` `LLAVE_A` `<lista_campos_tabla>` `LLAVE_C` 

* `<lista_campos_tabla>` ::= `<lista_campos_tabla>` `<campo_tabla>` | `<campo_tabla>` 

* `<campo_tabla>` ::= `ID` `DOSPUNTOS` `<tipo_dato>` `PUNTOCOMA` 

* `<tipo_dato>` ::= `TIP_INT` | `TIP_STRING` | `TIP_FLOAT` | `TIP_BOOL` | `TIP_ARRAY` | `TIP_OBJECT` 

### Operaciones de Manipulación (CRUD)
* `<sentencia_add>` ::= `PR_ADD` `ID` `LLAVE_A` `<lista_valores_insertar>` `LLAVE_C` `PUNTOCOMA` 

* `<lista_valores_insertar>` ::= `<lista_valores_insertar>` `COMA` `<valor_campo>` | `<valor_campo>` 

* `<valor_campo>` ::= `ID` `DOSPUNTOS` `<expresion>` 

* `<sentencia_read>` ::= `PR_READ` `ID` `LLAVE_A` `PR_FIELDS` `DOSPUNTOS` `<lista_campos_leer>` `PUNTOCOMA` `PR_FILTER` `DOSPUNTOS` `<expresion>` `PUNTOCOMA` `LLAVE_C` `PUNTOCOMA` 

* `<lista_campos_leer>` ::= `<lista_campos_leer>` `COMA` `<campo_leer>` | `<campo_leer>` | `ASTERISCO` 

* `<campo_leer>` ::= `ID` 

* `<sentencia_update>` ::= `PR_UPDATE` `ID` `LLAVE_A` `PR_SET` `DOSPUNTOS` `<lista_asignaciones_update>` `PUNTOCOMA` `PR_FILTER` `DOSPUNTOS` `<expresion>` `PUNTOCOMA` `LLAVE_C` `PUNTOCOMA` 
    | `PR_UPDATE` `ID` `LLAVE_A` `PR_SET` `DOSPUNTOS` `<lista_asignaciones_update>` `PUNTOCOMA` `LLAVE_C` `PUNTOCOMA` 
* `<lista_asignaciones_update>` ::= `<lista_asignaciones_update>` `COMA` `<asignacion_update>` | `<asignacion_update>` 
* `<asignacion_update>` ::= `ID` `IGUAL` `<expresion>` 
* `<sentencia_clear>` ::= `PR_CLEAR` `ID` `PUNTOCOMA` 
* `<sentencia_export>` ::= `PR_EXPORT` `CADENA` `PUNTOCOMA` 

### Expresiones Lógicas y Relacionales

* `<expresion>` ::= `<expresion>` `AND` `<expresion>` 
    | `<expresion>` `OR` `<expresion>` 
    | `NOT` `<expresion>` 
    | `<expresion>` `IGUAL_IGUAL` `<expresion>` 
    | `<expresion>` `DIFERENTE` `<expresion>` 
    | `<expresion>` `MENOR` `<expresion>` 
    | `<expresion>` `MAYOR` `<expresion>` 
    | `<expresion>` `MENOR_IGUAL` `<expresion>` 
    | `<expresion>` `MAYOR_IGUAL` `<expresion>` 
    | `PAR_A` `<expresion>` `PAR_C` 
    | `<term>` 

### Términos y Valores Primitivos
* `<term>` ::= `ENTERO` | `DECIMAL` | `CADENA` | `TRUE` | `FALSE` | `NULL` | `ID` 

---

## 3. Precedencia de Operadores
La resolución de ambigüedades en las expresiones se gestiona mediante la siguiente jerarquía (de menor a mayor prioridad).
1. **OR** (Asociatividad izquierda) 
2. **AND** (Asociatividad izquierda) 
3. **Relacionales** (`==`, `!=`, `<`, `>`, `<=`, `>=`) 
4. **NOT** (Asociatividad derecha) 