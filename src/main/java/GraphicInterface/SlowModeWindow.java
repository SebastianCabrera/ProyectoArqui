package GraphicInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JButton;

public class SlowModeWindow extends JFrame {

    private JPanel contentPane;
    private JPanel panelCore0;
    private JPanel panelCore1;
    private JLabel lblThread00;
    private JLabel lblThread01;
    private JLabel lblThread10;
    private JLabel lblFile00;
    private JLabel lblFile01;
    private JLabel lblFile10;
    private JLabel lblCurentClock;
    private JLabel lblClockValue;
    private JLabel lblPress;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SlowModeWindow frame = new SlowModeWindow();
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
    public SlowModeWindow() {
        setTitle("Slow Mode Data");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 316, 303);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panelInformation = new JPanel();
        panelInformation.setBorder(new TitledBorder(null, "Core Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelInformation.setBounds(10, 36, 291, 172);
        contentPane.add(panelInformation);
        panelInformation.setLayout(null);

        JLabel lblFilesRunning = new JLabel("Files running:");
        lblFilesRunning.setBounds(10, 21, 86, 14);
        panelInformation.add(lblFilesRunning);

        panelCore0 = new JPanel();
        panelCore0.setBorder(new TitledBorder(null, "Core 0", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelCore0.setBounds(10, 46, 130, 114);
        panelInformation.add(panelCore0);
        panelCore0.setLayout(null);

        lblThread00 = new JLabel("Thread 0:");
        lblThread00.setBounds(10, 21, 57, 14);
        panelCore0.add(lblThread00);

        lblThread01 = new JLabel("Thread 1:");
        lblThread01.setBounds(10, 46, 57, 14);
        panelCore0.add(lblThread01);

        lblFile00 = new JLabel("<File 0>");
        lblFile00.setBounds(74, 21, 46, 14);
        panelCore0.add(lblFile00);

        lblFile01 = new JLabel("<File 1>");
        lblFile01.setBounds(74, 46, 46, 14);
        panelCore0.add(lblFile01);

        panelCore1 = new JPanel();
        panelCore1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Core 1", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelCore1.setBounds(150, 46, 130, 114);
        panelInformation.add(panelCore1);
        panelCore1.setLayout(null);

        lblThread10 = new JLabel("Thread 0:");
        lblThread10.setBounds(10, 21, 52, 14);
        panelCore1.add(lblThread10);

        lblFile10 = new JLabel("<File 0>");
        lblFile10.setBounds(72, 21, 46, 14);
        panelCore1.add(lblFile10);

        lblCurentClock = new JLabel("Curent clock value:");
        lblCurentClock.setBounds(10, 11, 108, 14);
        contentPane.add(lblCurentClock);

        lblClockValue = new JLabel("<Clock>");
        lblClockValue.setBounds(128, 11, 46, 14);
        contentPane.add(lblClockValue);

        lblPress = new JLabel("Press \"Space\" to advance <N> cycles");
        lblPress.setBounds(10, 219, 290, 14);
        contentPane.add(lblPress);

        JButton btnFinish = new JButton("Finish");
        btnFinish.setEnabled(false);
        btnFinish.setBounds(212, 241, 89, 23);
        contentPane.add(btnFinish);
    }
}
