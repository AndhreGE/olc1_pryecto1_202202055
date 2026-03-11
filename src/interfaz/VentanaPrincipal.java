package interfaz;

import analizadores.Lexico;
import analizadores.Sintactico;
import arbol.Instruccion;
import entorno.Contexto;
import entorno.Entorno;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class VentanaPrincipal extends JFrame {
    
    private JTextArea txtEntrada;
    private JTextArea txtSalida;
    private JButton btnEjecutar;
    
    // Variable para saber qué archivo estamos editando
    private File archivoActual; 

    public VentanaPrincipal() {
        setTitle("Proyecto ELI - Nuevo Archivo");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // --- 1. BARRA DE MENÚ (NUEVO) ---
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuArchivo = new JMenu("Archivo");
        
        JMenuItem itemNuevo = new JMenuItem("Nuevo");
        JMenuItem itemAbrir = new JMenuItem("Abrir...");
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        JMenuItem itemGuardarComo = new JMenuItem("Guardar Como...");

        // Acciones del Menú
        itemNuevo.addActionListener(e -> nuevoArchivo());
        itemAbrir.addActionListener(e -> abrirArchivo());
        itemGuardar.addActionListener(e -> guardarArchivo());
        itemGuardarComo.addActionListener(e -> guardarComo());

        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemAbrir);
        menuArchivo.addSeparator();
        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemGuardarComo);
        
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar); // Agregamos la barra a la ventana

        // --- 2. PANEL DE BOTONES ---
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(45, 45, 45));
        
        btnEjecutar = new JButton("Ejecutar");
        styleButton(btnEjecutar, new Color(0, 153, 51));
        
        JButton btnReporte = new JButton("Reporte Errores");
        styleButton(btnReporte, new Color(255, 102, 0)); // Naranja
        
        JButton btnTokens = new JButton("Tokens");
        styleButton(btnTokens, new Color(0, 102, 204)); // Azul
        
        // Acciones de Botones
        btnEjecutar.addActionListener(e -> analizar());
        btnReporte.addActionListener(e -> GenerarReporte.generarHTML());
        btnTokens.addActionListener(e -> GenerarReporte.generarReporteTokens());

        panelSuperior.add(btnEjecutar);
        panelSuperior.add(btnReporte);
        panelSuperior.add(btnTokens);
        
        add(panelSuperior, BorderLayout.NORTH);

        // --- 3. EDITOR Y CONSOLA ---
        txtEntrada = new JTextArea();
        txtEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtEntrada.setBackground(new Color(30, 30, 30));
        txtEntrada.setForeground(Color.CYAN);
        txtEntrada.setCaretColor(Color.WHITE);
        JScrollPane scrollEntrada = new JScrollPane(txtEntrada);
        scrollEntrada.setBorder(BorderFactory.createTitledBorder(null, "Editor Código ELI", 0, 0, null, Color.WHITE));

        txtSalida = new JTextArea();
        txtSalida.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtSalida.setBackground(new Color(10, 10, 10));
        txtSalida.setForeground(Color.GREEN);
        txtSalida.setEditable(false);
        JScrollPane scrollSalida = new JScrollPane(txtSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder(null, "Consola", 0, 0, null, Color.WHITE));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollEntrada, scrollSalida);
        splitPane.setDividerLocation(400); 
        splitPane.setResizeWeight(0.7);
        
        add(splitPane, BorderLayout.CENTER);

        // Redirigir consola
        PrintStream printStream = new PrintStream(new SalidaConsola(txtSalida));
        System.setOut(printStream);
        System.setErr(printStream);
    }
    
    // Helper para estilizar botones
    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    // --- MÉTODOS DE ARCHIVOS (NUEVO) ---

    private void nuevoArchivo() {
        archivoActual = null;
        txtEntrada.setText("");
        setTitle("Proyecto ELI - Nuevo Archivo");
    }

    private void abrirArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos ELI (*.eli)", "eli"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoActual = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoActual))) {
                txtEntrada.read(reader, null); // Cargar texto al JTextArea
                setTitle("Proyecto ELI - " + archivoActual.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir archivo: " + ex.getMessage());
            }
        }
    }

    private void guardarArchivo() {
        if (archivoActual == null) {
            guardarComo(); // Si es nuevo, pedir nombre
        } else {
            escribirArchivo();
        }
    }

    private void guardarComo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos ELI (*.eli)", "eli"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoActual = fileChooser.getSelectedFile();
            // Asegurar extensión .eli
            if (!archivoActual.getName().toLowerCase().endsWith(".eli")) {
                archivoActual = new File(archivoActual.getAbsolutePath() + ".eli");
            }
            escribirArchivo();
        }
    }

    private void escribirArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoActual))) {
            txtEntrada.write(writer); // Guardar texto del JTextArea
            setTitle("Proyecto ELI - " + archivoActual.getName());
            JOptionPane.showMessageDialog(this, "Archivo guardado exitosamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    // --- ANALISIS ---

    private void analizar() {
        txtSalida.setText(""); 
        Contexto.getInstancia().errores.clear(); 
        Contexto.getInstancia().tokens.clear();
        
        String texto = txtEntrada.getText();

        if (texto.isEmpty()) {
            System.out.println("El editor está vacío.");
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
                System.out.println("No se generaron instrucciones (posible error sintáctico).");
            }
            
            System.out.println("\n--- FIN DEL PROCESO ---");

        } catch (Exception e) {
            System.out.println("Error grave: " + e.getMessage());
            e.printStackTrace();
        }
    }
}