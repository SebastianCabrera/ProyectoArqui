package GraphicInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

public class ResultsWindow extends JFrame {

    private JPanel contentPane;
    private JTable tableDataMem;
    private JTable tableCache0;
    private JTable tableCache1;
    private JPanel panelRegistersContent;
    private JTable tableRegisters;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ResultsWindow frame = new ResultsWindow();
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
    public ResultsWindow() {
        setTitle("Simulation Results");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 628, 606);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panelDataMem = new JPanel();
        panelDataMem.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Data memory", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelDataMem.setBounds(10, 11, 602, 161);
        contentPane.add(panelDataMem);
        panelDataMem.setLayout(null);

        tableDataMem = new JTable();
        tableDataMem.setBounds(10, 22, 582, 128);
        panelDataMem.add(tableDataMem);
        tableDataMem.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        tableDataMem.setModel(new DefaultTableModel(
                new Object[][] {
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                },
                new String[] {
                        "", "", "", "", "", "", "", "", "", "", "", ""
                }
        ) {
            Class[] columnTypes = new Class[] {
                    Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
            boolean[] columnEditables = new boolean[] {
                    false, false, false, false, false, false, false, false, false, false, false, false
            };
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });

        JPanel panelCache = new JPanel();
        panelCache.setBorder(new TitledBorder(null, "Data Cache", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelCache.setBounds(10, 183, 602, 161);
        contentPane.add(panelCache);
        panelCache.setLayout(null);

        JPanel panelCache0 = new JPanel();
        panelCache0.setBorder(new TitledBorder(null, "Cache 0", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelCache0.setBounds(10, 21, 372, 130);
        panelCache.add(panelCache0);
        panelCache0.setLayout(null);

        tableCache0 = new JTable();
        tableCache0.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        tableCache0.setModel(new DefaultTableModel(
                new Object[][] {
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, ""},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                },
                new String[] {
                        "", "", "", "", "", "", "", ""
                }
        ) {
            Class[] columnTypes = new Class[] {
                    String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        tableCache0.getColumnModel().getColumn(0).setResizable(false);
        tableCache0.getColumnModel().getColumn(1).setResizable(false);
        tableCache0.getColumnModel().getColumn(2).setResizable(false);
        tableCache0.getColumnModel().getColumn(3).setResizable(false);
        tableCache0.getColumnModel().getColumn(4).setResizable(false);
        tableCache0.getColumnModel().getColumn(5).setResizable(false);
        tableCache0.getColumnModel().getColumn(6).setResizable(false);
        tableCache0.getColumnModel().getColumn(7).setResizable(false);
        tableCache0.setBounds(10, 21, 352, 96);
        panelCache0.add(tableCache0);

        JPanel panelCache1 = new JPanel();
        panelCache1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Cache 1", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelCache1.setBounds(392, 21, 199, 130);
        panelCache.add(panelCache1);
        panelCache1.setLayout(null);

        tableCache1 = new JTable();
        tableCache1.setModel(new DefaultTableModel(
                new Object[][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                },
                new String[] {
                        "", "", "", ""
                }
        ) {
            Class[] columnTypes = new Class[] {
                    String.class, String.class, String.class, String.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
            boolean[] columnEditables = new boolean[] {
                    false, false, false, false
            };
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tableCache1.getColumnModel().getColumn(0).setResizable(false);
        tableCache1.getColumnModel().getColumn(1).setResizable(false);
        tableCache1.getColumnModel().getColumn(2).setResizable(false);
        tableCache1.getColumnModel().getColumn(3).setResizable(false);
        tableCache1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        tableCache1.setBounds(10, 21, 176, 96);
        panelCache1.add(tableCache1);

        panelRegistersContent = new JPanel();
        panelRegistersContent.setBorder(new TitledBorder(null, "Registers content", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelRegistersContent.setBounds(10, 366, 602, 166);
        contentPane.add(panelRegistersContent);
        panelRegistersContent.setLayout(null);

        JComboBox comboBoxFiles = new JComboBox();
        comboBoxFiles.setModel(new DefaultComboBoxModel(new String[] {"Select a file"}));
        comboBoxFiles.setBounds(10, 18, 175, 20);
        panelRegistersContent.add(comboBoxFiles);

        JLabel lblCycles = new JLabel("Total cycles used:");
        lblCycles.setBounds(10, 58, 91, 14);
        panelRegistersContent.add(lblCycles);

        JLabel labelCyclesValue = new JLabel("<Cycles>");
        labelCyclesValue.setBounds(139, 58, 62, 14);
        panelRegistersContent.add(labelCyclesValue);

        JLabel lblPC = new JLabel("PC:");
        lblPC.setBounds(10, 83, 46, 14);
        panelRegistersContent.add(lblPC);

        JLabel lblPCValue = new JLabel("<PC>");
        lblPCValue.setBounds(139, 83, 46, 14);
        panelRegistersContent.add(lblPCValue);

        JPanel panelRegisters = new JPanel();
        panelRegisters.setBorder(new TitledBorder(null, "Registers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelRegisters.setBounds(220, 58, 372, 97);
        panelRegistersContent.add(panelRegisters);
        panelRegisters.setLayout(null);

        tableRegisters = new JTable();
        tableRegisters.setBounds(10, 21, 352, 64);
        panelRegisters.add(tableRegisters);
        tableRegisters.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        tableRegisters.setModel(new DefaultTableModel(
                new Object[][] {
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                },
                new String[] {
                        "", "", "", "", "", "", "", ""
                }
        ) {
            Class[] columnTypes = new Class[] {
                    Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
            boolean[] columnEditables = new boolean[] {
                    false, false, false, false, false, false, false, false
            };
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tableRegisters.getColumnModel().getColumn(0).setResizable(false);
        tableRegisters.getColumnModel().getColumn(1).setResizable(false);
        tableRegisters.getColumnModel().getColumn(2).setResizable(false);
        tableRegisters.getColumnModel().getColumn(3).setResizable(false);
        tableRegisters.getColumnModel().getColumn(4).setResizable(false);
        tableRegisters.getColumnModel().getColumn(5).setResizable(false);
        tableRegisters.getColumnModel().getColumn(6).setResizable(false);
        tableRegisters.getColumnModel().getColumn(7).setResizable(false);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(523, 544, 89, 23);
        contentPane.add(btnClose);
        tableDataMem.getColumnModel().getColumn(0).setResizable(false);
        tableDataMem.getColumnModel().getColumn(1).setResizable(false);
        tableDataMem.getColumnModel().getColumn(2).setResizable(false);
        tableDataMem.getColumnModel().getColumn(3).setResizable(false);
        tableDataMem.getColumnModel().getColumn(4).setResizable(false);
        tableDataMem.getColumnModel().getColumn(5).setResizable(false);
        tableDataMem.getColumnModel().getColumn(6).setResizable(false);
        tableDataMem.getColumnModel().getColumn(7).setResizable(false);
        tableDataMem.getColumnModel().getColumn(8).setResizable(false);
        tableDataMem.getColumnModel().getColumn(9).setResizable(false);
        tableDataMem.getColumnModel().getColumn(10).setResizable(false);
        tableDataMem.getColumnModel().getColumn(11).setResizable(false);
    }
}