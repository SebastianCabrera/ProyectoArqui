import Control.Program;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
    public static void main (String args[]) {

        /*************************************************************************************************
         * Código para iniciar y cambiar el aspecto de las ventanas. ¡No borrar! Se activará luego cuando
         * lo demás esté listo.
         *************************************************************************************************/

        try {

            // Para que las ventanas tengan el aspecto del sistema operativo y no el default de Java
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "Failed to set the program appearance to the\n system default.",
                    "Appearance error", JOptionPane.ERROR_MESSAGE);
        }

        /*// Para correr la ventana a través de un thread
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SettingsWindow frame = new SettingsWindow();

                    // Para que las ventanas aparezcan en el centro de la pantalla
                    frame.setLocationRelativeTo(null);

                    // Para que aparezca la ventana
                    frame.setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Failed to run the program. Cannot set the thread to execute it.",
                            "Execution error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });*/

        // Probando con los hilillos (hardcoding)
        String[] files = {"0.txt", "1.txt", "2.txt", "3.txt", "4.txt", "5.txt"};

        Program program = new Program();
        program.loadInstructions(files);
        program.runProgram();
    }
}
