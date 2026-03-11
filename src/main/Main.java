package main;

import interfaz.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        // Ejecutar la ventana en el hilo de eventos de Swing
        java.awt.EventQueue.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}