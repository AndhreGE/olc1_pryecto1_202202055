  ![enter image description here](https://www.pngplay.com/wp-content/uploads/13/Software-IT-Services-Transparent-File.png)
# ELI - Educational Language Implementation (ELI-NOSQL)

## 1. Descripción del Proyecto

Este proyecto consiste en el desarrollo de una aplicación educativa capaz de gestionar datos e interpretar programas escritos en un lenguaje declarativo propio llamado **ELI**. El sistema funciona de manera independiente a cualquier motor de almacenamiento externo, permitiendo definir estructuras de datos, realizar consultas, inserciones y persistir la información en archivos JSON.

## 2. Objetivos

-   **General:** Desarrollar el front-end de un compilador para el lenguaje ELI, aplicando conceptos de análisis léxico, sintáctico y gestión de tablas de símbolos.
    
-   **Específicos:**
    
    -   Implementar un intérprete de código de alto nivel.
        
    -   Fortalecer el uso de herramientas como **JFlex** y **CUP** para la generación de analizadores.
        
    -   Gestionar la persistencia de datos localmente mediante formatos estructurados.
        

## 3. Características del Lenguaje ELI

ELI es un lenguaje inspirado en bases de datos NoSQL con las siguientes capacidades:

### Tipos de Datos Soportados

-   **Primitivos:**  `int` (enteros), `float` (decimales), `bool` (verdadero/falso), `string` (cadenas de texto).
    
-   **Complejos:**  `array` (colecciones), `object` (pares clave-valor) y `null`.
    

### Operadores y Lógica

-   **Relacionales:**  `==`, `!=`, `>`, `<`, `>=`, `<=`.
    
-   **Lógicos:**  `&&` (AND), `||` (OR), `!` (NOT).
    
-   **Comentarios:** Soporta comentarios de una línea (`##`) y multilínea (`#* ... #*`).
    

### Instrucciones Principales

-   `database`: Define el contexto y ruta de persistencia (`store at`).
    
-   `table`: Crea estructuras de almacenamiento.
    
-   `add`, `read`, `update`, `clear`: Operaciones para manipular y consultar registros.
    
-   `export`: Guarda resultados de consultas en archivos JSON externos.
    

## 4. Requerimientos Técnicos e Instalación

-   **Lenguaje:** Java.
    
-   **Herramientas de Compilación:** JFlex (Léxico) y CUP (Sintáctico).
    
-   **Interfaz Gráfica:** Swing/JavaFX (según implementación) para el editor de código y visualización de datos.
    
-   **Persistencia:** Base de datos en memoria (H2) y archivos JSON.
    

## 5. Reportes Generados

El sistema incluye módulos para la visualización de:

1.  **Tabla de Tokens:** Lista de todos los componentes léxicos identificados.
    
2.  **Tabla de Errores:** Detalle de errores léxicos y sintácticos (tipo, descripción, línea y columna).
    
3.  **Salida de Consultas:** Área dedicada a mostrar los resultados de las instrucciones `read`.
    

----------

**Desarrollado para:** Facultad de Ingeniería, Universidad San Carlos de Guatemala (USAC)
