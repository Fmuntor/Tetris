package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Simple Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Asignal GamePanel a la ventana window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack(); // El tamaño de GamePanel pasa a ser el de window

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();

    }
}
