package pkg2ddrawingapp;

import javax.swing.JFrame;

public class Draw_Test {
    
    public static void main(String[] args) {
        Draw_Pane file1 = new Draw_Pane();
        file1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        file1.setSize(700, 500);
        file1.setVisible(true);
    }
    
}
