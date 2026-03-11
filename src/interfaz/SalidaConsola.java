package interfaz;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

public class SalidaConsola extends OutputStream {
    private JTextArea textArea;

    public SalidaConsola(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // Redirige cada byte que se imprime hacia el JTextArea
        textArea.append(String.valueOf((char) b));
        // Mueve el scroll hacia abajo automáticamente
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}