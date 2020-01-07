package View;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private int size = 400;
    private JMenuItem menuItem1;
    private JTextArea textArea;

    public GUI(String title){
        JPanel panel = buildUpperPanel();
        frame = new JFrame(title);

        frame.setJMenuBar(buildMenuBar());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        //frame.setResizable(false);
        frame.add(panel, BorderLayout.NORTH);


        //this.menuListener = menuListener;
        //this.buttonListener = buttonListener;
    }

    public void show() {
        frame.setVisible(true);
    }

    /**
     * Build the menu bar
     * @return The menu bar
     */
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        menuItem1 = new JMenuItem("Meny1");

        fileMenu.add(menuItem1);

        JMenu helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * Builds the upper panel by creating a JPanel and filling it with
     * a JTextField for inputting a classname and JButton
     *
     * @return The created panel
     */
    private JPanel buildUpperPanel() {
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        upperPanel.setBorder(BorderFactory.createTitledBorder("Yeet"));

        textArea = new JTextArea("");
        upperPanel.add(textArea, BorderLayout.EAST);
        return upperPanel;
    }
}
