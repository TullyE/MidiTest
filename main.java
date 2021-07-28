import javax.swing.*;
import java.awt.Dimension;

public class main
{
    
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
        

        myFrame.setVisible(true);
    }
}
