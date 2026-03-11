package interfaz;

import analizadores.Lexico;
import analizadores.Sintactico;
import arbol.Instruccion;
import entorno.Entorno;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.LinkedList;
import javax.swing.*;
import entorno.Contexto;

public class VentanaPrincipal extends JFrame {
    
    private JTextArea txtEntrada;
    private JTextArea txtSalida;
    private JButton btnEjecutar;

    
    public VentanaPrincipal() {
        setTitle("Proyecto ELI - Compilador 🚀");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana
        setLayout(new BorderLayout());

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // 1. Panel Superior (Botones)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(45, 45, 45));
        
        btnEjecutar = new JButton("▶ Ejecutar");
        btnEjecutar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEjecutar.setBackground(new Color(0, 153, 51));
        btnEjecutar.setForeground(Color.WHITE);
        btnEjecutar.setFocusPainted(false);
        // ...
        JButton btnReporte = new JButton("📄 Reporte Errores");
        btnReporte.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReporte.setBackground(new Color(255, 153, 51)); // Color Naranja
        btnReporte.setForeground(Color.WHITE);
        btnReporte.setFocusPainted(false);
        
        // Acción del botón
        btnEjecutar.addActionListener((ActionEvent e) -> {
            analizar();

        // Añadir al panel
        panelSuperior.add(btnEjecutar);
        panelSuperior.add(btnReporte); // <--- Nuevo botón
        // ...
        });

        btnReporte.addActionListener((ActionEvent e) -> {
            GenerarReporte.generarHTML();
        });

        panelSuperior.add(btnEjecutar);
        add(panelSuperior, BorderLayout.NORTH);

        // 2. Área Central (Split: Entrada vs Salida)
        txtEntrada = new JTextArea();
        txtEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtEntrada.setBackground(new Color(30, 30, 30));
        txtEntrada.setForeground(Color.CYAN);
        txtEntrada.setCaretColor(Color.WHITE);
        JScrollPane scrollEntrada = new JScrollPane(txtEntrada);
        scrollEntrada.setBorder(BorderFactory.createTitledBorder(null, "Editor de Código", 0, 0, null, Color.WHITE));

        txtSalida = new JTextArea();
        txtSalida.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtSalida.setBackground(new Color(0, 0, 0));
        txtSalida.setForeground(Color.GREEN);
        txtSalida.setEditable(false);
        JScrollPane scrollSalida = new JScrollPane(txtSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder(null, "Consola de Salida", 0, 0, null, Color.WHITE));

        // Dividir la pantalla
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollEntrada, scrollSalida);
        splitPane.setDividerLocation(350); // Altura inicial del editor
        splitPane.setResizeWeight(0.7);
        
        add(splitPane, BorderLayout.CENTER);

        // 3. Redirigir System.out a nuestra consola
        PrintStream printStream = new PrintStream(new SalidaConsola(txtSalida));
        System.setOut(printStream);
        System.setErr(printStream);
    }

    private void analizar() {
        // Limpiar consola antes de ejecutar
        txtSalida.setText(""); 
        
        // --- CORRECCIÓN AQUÍ ---
        // Antes decía: Entorno.Contexto.getInstancia()... (MAL)
        // Debe decir: Contexto.getInstancia()... (BIEN)
        entorno.Contexto.getInstancia().errores.clear(); 
        
        String texto = txtEntrada.getText();

        if (texto.isEmpty()) {
            System.out.println("⚠️ El editor está vacío.");
            return;
        }

        try {
            System.out.println("--- INICIANDO ANALISIS ---");
            
            Lexico lexer = new Lexico(new StringReader(texto));
            Sintactico parser = new Sintactico(lexer);
            parser.parse();

            LinkedList<Instruccion> ast = parser.AST;

            if (ast != null) {
                System.out.println("--- EJECUCION AST ---");
                Entorno global = new Entorno();
                for (Instruccion ins : ast) {
                    if (ins != null) ins.ejecutar(global);
                }
            } else {
                System.out.println("❌ No se generaron instrucciones (posible error sintáctico).");
            }
            
            System.out.println("\n--- FIN DEL PROCESO ---");

        } catch (Exception e) {
            System.out.println("🔥 Error grave: " + e.getMessage());
            e.printStackTrace();
        }
    }
}