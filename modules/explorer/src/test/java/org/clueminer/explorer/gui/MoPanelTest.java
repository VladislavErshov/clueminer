package org.clueminer.explorer.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author deric
 */
public class MoPanelTest extends JFrame {

    private static final long serialVersionUID = 5839891138296381065L;

    private MoPanel frame;

    public MoPanelTest() {
        setLayout(new GridBagLayout());
        initComponents();

    }

    // this function will be run from the EDT
    private static void createAndShowGUI() throws Exception {
        MoPanelTest main = new MoPanelTest();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(500, 500);
        main.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });
    }

    private void initComponents() {
        frame = new MoPanel();
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        add((Component) frame, c);
    }

}
