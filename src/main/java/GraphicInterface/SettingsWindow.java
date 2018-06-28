package GraphicInterface;

import java.awt.EventQueue;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class SettingsWindow extends JFrame {

    private JPanel settingsPane;
    private JTextField textFieldQuantum;
    private JTextField textFieldCycles;
    private JFileChooser fileChooser;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {

            // Para que las ventanas tengan el aspecto del sistema operativo y no el default de Java
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "Failed to set the program appearance to the\n system default.",
                    "Appearance error", JOptionPane.ERROR_MESSAGE);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SettingsWindow frame = new SettingsWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SettingsWindow() {
        setResizable(false);
        setTitle("MIPS Simulator Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 358, 246);
        settingsPane = new JPanel();
        settingsPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(settingsPane);
        settingsPane.setLayout(null);

        JPanel panelSlowMode = new JPanel();
        panelSlowMode.setBorder(new TitledBorder(null, "Slow Mode", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelSlowMode.setBounds(10, 76, 330, 98);
        settingsPane.add(panelSlowMode);
        panelSlowMode.setLayout(null);

        JLabel lblSlowMode = new JLabel("Slow mode:");
        lblSlowMode.setBounds(10, 22, 66, 14);
        panelSlowMode.add(lblSlowMode);

        textFieldCycles = new JTextField();

        textFieldCycles.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent key) {
                if(key.getKeyChar() < 48 || key.getKeyChar() > 57){
                    key.consume();
                }
            }

        });

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(".\\hilillos"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (.txt)", "txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setApproveButtonText("Run!");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        textFieldCycles.setEnabled(false);
        textFieldCycles.setToolTipText("");
        textFieldCycles.setBounds(197, 43, 118, 20);
        panelSlowMode.add(textFieldCycles);
        textFieldCycles.setColumns(10);

        JRadioButton rdbtnActivated = new JRadioButton("Activated");
        rdbtnActivated.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg) {
                textFieldCycles.setEnabled(true);
            }

        });

        rdbtnActivated.setBounds(10, 42, 86, 23);
        panelSlowMode.add(rdbtnActivated);

        JRadioButton rdbtnDeactivated = new JRadioButton("Deactivated");
        rdbtnDeactivated.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg) {
                textFieldCycles.setEnabled(false);
            }

        });

        rdbtnDeactivated.setSelected(true);
        rdbtnDeactivated.setBounds(10, 68, 86, 23);
        panelSlowMode.add(rdbtnDeactivated);

        ButtonGroup rdbtnGroupSlowMode = new ButtonGroup();
        rdbtnGroupSlowMode.add(rdbtnActivated);
        rdbtnGroupSlowMode.add(rdbtnDeactivated);

        JLabel lblCycles = new JLabel("Clock cycles to advance:");
        lblCycles.setBounds(197, 22, 136, 14);
        panelSlowMode.add(lblCycles);

        JPanel panelGeneral = new JPanel();
        panelGeneral.setBorder(new TitledBorder(null, "General Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelGeneral.setBounds(10, 11, 330, 54);
        settingsPane.add(panelGeneral);
        panelGeneral.setLayout(null);

        JLabel lblQuantum = new JLabel("Quantum for each file:");
        lblQuantum.setBounds(10, 21, 137, 14);
        panelGeneral.add(lblQuantum);

        textFieldQuantum = new JTextField();
        textFieldQuantum.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent key) {
                if(key.getKeyChar() < 48 || key.getKeyChar() > 57){
                    key.consume();
                }
            }

        });

        textFieldQuantum.setBounds(197, 18, 118, 20);
        panelGeneral.add(textFieldQuantum);
        textFieldQuantum.setColumns(10);

        JButton btnReady = new JButton("Ready!");
        btnReady.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                if(textFieldQuantum.getText().isEmpty() || (textFieldCycles.isEnabled() && textFieldCycles.getText().isEmpty())){
                    JOptionPane.showMessageDialog(null, "Please, fill the text fields to continue.", "Empty text field error", JOptionPane.ERROR_MESSAGE);
                }else{
                    dispose();
                    if(fileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION){
                        File[] files = fileChooser.getSelectedFiles();
                        String selectedFiles = "Selected files:\n";
                        for(int i = 0; i < files.length; i++){
                            selectedFiles += files[i].getName() + "\n";
                        }
                        selectedFiles += "(Testing message)";

                        JOptionPane.showMessageDialog(null, selectedFiles, "Selected files", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }
        });
        btnReady.setBounds(152, 185, 89, 23);
        settingsPane.add(btnReady);

        JButton btnExit = new JButton("Exit");
        btnExit.setBounds(251, 185, 89, 23);

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });

        settingsPane.add(btnExit);
    }
}
