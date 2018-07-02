package GraphicInterface;

import Control.Program;
import Enums.Codes;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SlowModeWindow extends JFrame {

    private JPanel contentPane;
    private JPanel panelCore0;
    private JPanel panelCore1;
    private JLabel lblFile0;
    private JLabel lblFile1;
    private JLabel lblFileName0;
    private JLabel lblFileName1;
    private JLabel lblCurentClock;
    private JLabel lblClockValue;
    private JLabel lblPress;
    private JButton btnAdvance;
    private JButton btnFinish;
    private CyclicBarrier barrier;
    private Program mainProgram;

    /**
     * Create the frame.
     */
    public SlowModeWindow(Program p, CyclicBarrier slowBarrier, String slowCycles) {
        this.mainProgram = p;
        this.barrier = slowBarrier;

        setTitle("Slow Mode Data");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 316, 244);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panelInformation = new JPanel();
        panelInformation.setBorder(new TitledBorder(null, "Core Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelInformation.setBounds(10, 36, 291, 111);
        contentPane.add(panelInformation);
        panelInformation.setLayout(null);

        JLabel lblFilesRunning = new JLabel("Files running:");
        lblFilesRunning.setBounds(10, 21, 86, 14);
        panelInformation.add(lblFilesRunning);

        panelCore0 = new JPanel();
        panelCore0.setBorder(new TitledBorder(null, "Core 0", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelCore0.setBounds(10, 46, 131, 51);
        panelInformation.add(panelCore0);
        panelCore0.setLayout(null);

        lblFile0 = new JLabel("File:");
        lblFile0.setBounds(10, 21, 57, 14);
        panelCore0.add(lblFile0);

        lblFileName0 = new JLabel("N/A");
        lblFileName0.setBounds(48, 21, 46, 14);
        panelCore0.add(lblFileName0);

        panelCore1 = new JPanel();
        panelCore1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Core 1", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelCore1.setBounds(150, 46, 131, 51);
        panelInformation.add(panelCore1);
        panelCore1.setLayout(null);

        lblFile1 = new JLabel("File:");
        lblFile1.setBounds(10, 21, 52, 14);
        panelCore1.add(lblFile1);

        lblFileName1 = new JLabel("N/A");
        lblFileName1.setBounds(47, 21, 46, 14);
        panelCore1.add(lblFileName1);

        lblCurentClock = new JLabel("Curent clock value:");
        lblCurentClock.setBounds(10, 11, 108, 14);
        contentPane.add(lblCurentClock);

        lblClockValue = new JLabel("0");
        lblClockValue.setBounds(128, 11, 46, 14);
        contentPane.add(lblClockValue);

        lblPress = new JLabel("Press \"Advance\" button to advance " + slowCycles + " cycles");
        lblPress.setBounds(10, 158, 290, 14);
        contentPane.add(lblPress);

        btnFinish = new JButton("Finish");
        btnFinish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                barrier.reset();
                dispose();
            }
        });
        btnFinish.setEnabled(false);
        btnFinish.setBounds(212, 183, 89, 23);
        contentPane.add(btnFinish);

        btnAdvance = new JButton("Advance");
        btnAdvance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                lblFileName0.setText(mainProgram.getCurrentRunningFile(Codes.CORE_0));
                lblFileName1.setText(mainProgram.getCurrentRunningFile(Codes.CORE_1));
                lblClockValue.setText("" + mainProgram.getCurrentClock());
                try {
                    slowBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        btnAdvance.setBounds(113, 183, 89, 23);
        contentPane.add(btnAdvance);
    }

    public void setFinishAvailable(){
        this.btnFinish.setEnabled(true);
        this.btnAdvance.setEnabled(false);
    }
}
