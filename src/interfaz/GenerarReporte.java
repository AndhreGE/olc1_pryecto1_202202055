package interfaz;

import entorno.Contexto;
import java.io.FileWriter;

public class GenerarReporte {
    
    public static void generarHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Reporte de Errores</title>");
        html.append("<style>");
        html.append("table { border-collapse: collapse; width: 80%; margin: 20px auto; font-family: Arial, sans-serif; }");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        html.append("th { background-color: #f44336; color: white; }");
        html.append("tr:nth-child(even) { background-color: #f2f2f2; }");
        html.append("h1 { text-align: center; color: #333; }");
        html.append("</style></head><body>");
        
        html.append("<h1>Reporte de Errores - Proyecto ELI</h1>");
        html.append("<table>");
        html.append("<tr><th>Tipo</th><th>Descripción</th><th>Línea</th><th>Columna</th></tr>");
        
        // Llenar tabla con la lista del Contexto
        Contexto ctx = Contexto.getInstancia();
        if (ctx.errores.isEmpty()) {
            html.append("<tr><td colspan='4' style='text-align:center'>✅ ¡No se encontraron errores!</td></tr>");
        } else {
            for (Excepcion err : ctx.errores) {
                html.append("<tr>");
                html.append("<td>").append(err.tipo).append("</td>");
                html.append("<td>").append(err.descripcion).append("</td>");
                html.append("<td>").append(err.linea).append("</td>");
                html.append("<td>").append(err.columna).append("</td>");
                html.append("</tr>");
            }
        }
        
        html.append("</table></body></html>");
        
        // Guardar archivo
        try {
            FileWriter writer = new FileWriter("reportes/errores.html");
            writer.write(html.toString());
            writer.close();
            System.out.println("📄 Reporte generado: reportes/errores.html");
            
            // Intentar abrirlo automáticamente (opcional)
            java.awt.Desktop.getDesktop().browse(new java.io.File("reportes/errores.html").toURI());
            
        } catch (Exception e) {
            System.out.println("Error guardando reporte: " + e.getMessage());
        }
    }
}