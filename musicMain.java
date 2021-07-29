/**
musicMain.java
File to run
Tully Eva
07/29/2021
*/
import javax.swing.*;
import java.awt.Dimension;

public class musicMain
{
    /**
     * initiate the Frame
     * and create a HomepageMenu panel
     * @param args
     */
    public static void main(String[] args)
    {
        HomepageMenu myPanel = new HomepageMenu();
        JFrame myFrame = new JFrame("Generative Music");
        myFrame.setPreferredSize(new Dimension(1000, 1000));
        myFrame.setSize(new Dimension(1000, 1000));
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setResizable(false);
        myFrame.add(myPanel);
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);

        myFrame.setVisible(true);
    }
}
