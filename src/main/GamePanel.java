package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread;
    PlayManager pm;


    public GamePanel() {

        //Ajustes del Panel
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        // Implementar KeyListener
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        pm = new PlayManager();
    }

    public void launchGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // Game Loop. En cada uno vamos a hacer un update (posiciones de objetos, puntuación...) y un draw (pintar objetos o información)
    
        // Calculamos el intervalo de tiempo en nanosegundos entre cada frame.
        double drawInterval = 1000000000 / FPS;
    
        // Variable para llevar un seguimiento del tiempo acumulado desde el último frame.
        double delta = 0;
    
        // Almacenamos el tiempo actual en nanosegundos.
        long lastTime = System.nanoTime();
        long currentTime;
    
        // Bucle del juego, se ejecuta mientras gameThread no sea nulo.
        while (gameThread != null) {
            // Registramos el tiempo actual en nanosegundos.
            currentTime = System.nanoTime();
    
            // Calculamos el tiempo transcurrido desde el último frame y lo sumamos a delta.
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
    
            // Si ha pasado suficiente tiempo para procesar un nuevo frame.
            if (delta >= 1) {
                // Llamamos a la función para actualizar la lógica del juego.
                update();
    
                // Llamamos a la función para representar gráficamente el estado actual del juego.
                repaint();
    
                // Decrementamos delta en 1 para indicar que hemos procesado un frame.
                delta--;
            }
        }
    }

    private void update(){
        if(KeyHandler.pausePressed == false){
            pm.update();
        }

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        pm.draw(g2);
    }
    
}
