package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import mino.Block;
import mino.Mino;
import mino.Mino_Bar;
import mino.Mino_L1;
import mino.Mino_L2;
import mino.Mino_Square;
import mino.Mino_T;
import mino.Mino_Z1;
import mino.Mino_Z2;

public class PlayManager {

    // Esta clase va a manejar elementos basicos del gameplay. Dibujar area de
    // juego, admin tetrominos, manejar acciones basicas como borrar lineas o añadir
    // puntuaciones.

    // Main Play Area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    // Ajustamos estos valores porque mas tarde asignaremos un bloque como 30x30px,
    // con lo que podremos poner 12 bloques horizontales y 20 verticales.

    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;

    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    // Otros
    public static int dropInterval = 60; // El mino cae en un intervalo de cada 60 frames.

    public PlayManager() {

        // Main Play Area Frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); // 1280/2 - 360/2 = 460
        // Se sustrae la mitad del ancho de la play area desde el punto central de la
        // window

        right_x = left_x + WIDTH; // 460 + 360 = 720
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE; // Start x estara alrededor de la zona central del play area.
        MINO_START_Y = top_y + Block.SIZE; // Añadimos el block size al top y.

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        // Set Starting Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

    }

    private Mino pickMino() {

        // Pillar un mino random
        Mino mino = null;
        int i = new Random().nextInt(7);

        switch (i) {
            case 0:
                mino = new Mino_L1();
                break;
            case 1:
                mino = new Mino_L2();
                break;
            case 2:
                mino = new Mino_Square();
                break;
            case 3:
                mino = new Mino_Bar();
                break;
            case 4:
                mino = new Mino_T();
                break;
            case 5:
                mino = new Mino_Z1();
                break;
            case 6:
                mino = new Mino_Z2();
                break;
        }
        return mino;
    }

    public void update() {

        // Check si esta activo currentMino
        if (currentMino.activo == false) {
            // Si no esta activo (ha llegado al suelo), se pone en staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            currentMino.deactivating = false;

            // Reemplazamos el currentMino por el nextMino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            // Cuando un mino se vuelve inactivo, se chequean si alguna linea puede ser borrada
            checkDelete();

        } else {
            currentMino.update();
        }

    }

    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;

        while (x < right_x && y < bottom_y) {
            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    // Incrementa el conteo si hay un StaticBlock
                    blockCount++;
                }
            }

            x += Block.SIZE;

            if (x == right_x) {

                // Si blockCount = 12 significa que la linea y actual esta rellena de bloques,
                // podemos eliminarla
                if (blockCount == 12) {
                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        // Borrar los bloques de la linea y actual
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    // Al haberse borrado una linea, bajamos el resto de bloques 1 bloque hacia
                    // abajo
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        // Si un bloque esta por encima del actual y, se mueve abajo el tamaño del
                        // bloque
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;

            }
        }
    }

    public void draw(Graphics2D g2) {
        // Dibujar Play Area Frame

        // Establecemos el color del trazo (en este caso, blanco).
        g2.setColor(Color.white);

        // Configuramos el grosor del trazo a 4 píxeles.
        g2.setStroke(new BasicStroke(4f));

        // Dibujamos un rectángulo que representa el marco del área de juego.
        // left_x-4 y top_y-4 se utilizan para que el rectángulo sea ligeramente más
        // grande que el área de juego principal,
        // de manera que se crea un borde alrededor del área de juego.
        // WIDTH+8 y HEIGHT+8 se utilizan para que el rectángulo sea más grande que el
        // área de juego real.
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        // Dibujar frame de siguiente Mino
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);

        // Dibujar current_Mino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        // Dibujar next_Mino
        nextMino.draw(g2);

        // Dibujar Static Blocks (bloques que ya han llegado al suelo)
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        // Dibujar Pause
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }
    }

}
