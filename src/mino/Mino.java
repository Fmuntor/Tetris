package mino;

import java.awt.Color;
import java.awt.Graphics2D;

import main.KeyHandler;
import main.PlayManager;

public class Mino {
    // Esta es una clase padrem, con lo que todos los minos extends esta clase

    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direccion = 1; // Hay 4 direcciones (1/2/3/4)
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean activo = true;

    // Para poder mover la pieza una vez toque el suelo para reubicarla antes de que salga otra:
    public boolean deactivating;
    int deactivateCounter = 0;

    public void create(Color c) {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);

    }

    public void setXY(int x, int y) {
    }

    public void updateXY(int direccion) {

        checkRotationCollision();

        if (leftCollision == false && rightCollision == false && bottomCollision == false) {
            // Usaremos el array tempB en vez del b, ya que tenemos que manejar colisiones,
            // y en caso de rotar y que colisione debemos poder cancelar la rotacion.
            this.direccion = direccion;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }

    }

    public void getDireccion1() {
    }

    public void getDireccion2() {
    }

    public void getDireccion3() {
    }

    public void getDireccion4() {
    }

    public void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // Chequear colision con Static Block
        checkStaticBlockCollision();

        // Chequear la colision del frame
        // Pared izquierda
        for (int i = 0; i < b.length; i++) {
            if (b[i].x == PlayManager.left_x) { // Como se toma la referencia desde el borde izquierdo de cualquier
                                                // bloque del mino, no se añade nada mas
                leftCollision = true;
            }
        }

        // Pared derecha
        for (int i = 0; i < b.length; i++) {
            if (b[i].x + Block.SIZE == PlayManager.right_x) { // Al tomar de referencia el borde izquierdo, añadimos el
                                                              // tamaño de un bloque para que la referencia este en-
                rightCollision = true; // -el borde derecho.
            }
        }

        // Suelo
        for (int i = 0; i < b.length; i++) {
            if (b[i].y + Block.SIZE == PlayManager.bottom_y) { // Al igual que en el borde derecho, se añade un bloque
                                                               // pero hacia abajo.
                bottomCollision = true;
            }
        }
    }

    public void checkRotationCollision() {

        // Este metodo es igual que checkMovementCollision, pero comprobando las array
        // tempB y comprobando si es mayor o menor
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // Chequear colision con Static Block
        checkStaticBlockCollision();

        // Chequear la colision del frame
        // Pared izquierda
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x < PlayManager.left_x) { // Como se toma la referencia desde el borde izquierdo de cualquier
                                                   // bloque del mino, no se añade nada mas
                leftCollision = true;
            }
        }

        // Pared derecha
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x + Block.SIZE > PlayManager.right_x) { // Al tomar de referencia el borde izquierdo, añadimos
                                                                 // el
                                                                 // tamaño de un bloque para que la referencia este en-
                rightCollision = true; // -el borde derecho.
            }
        }

        // Suelo
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].y + Block.SIZE > PlayManager.bottom_y) { // Al igual que en el borde derecho, se añade un
                                                                  // bloque
                                                                  // pero hacia abajo.
                bottomCollision = true;
            }
        }
    }

    private void checkStaticBlockCollision(){
        // Recorremos el array de Static Blocks
        for(int i=0; i < PlayManager.staticBlocks.size(); i++){
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            // Check abajo
            for(int ii = 0; ii < b.length; ii++){
                if(b[ii].y + Block.SIZE == targetY && b[ii].x == targetX){
                    bottomCollision = true;
                }
            }

            // Check izquierda
            for(int ii = 0; ii < b.length; ii++){
                if(b[ii].x - Block.SIZE == targetX && b[ii].y == targetY){
                    leftCollision = true;
                }
            }

            // Check derecha
            for(int ii = 0; ii < b.length; ii++){
                if(b[ii].x + Block.SIZE == targetX && b[ii].y == targetY){
                    rightCollision = true;
                }
            }
        }
    }

    public void update() {

        if(deactivating){
            deactivating();
        }

        // Mover el mino
        if (KeyHandler.upPressed) {
            switch (direccion) {
                case 1:
                    getDireccion2();
                    break;
                case 2:
                    getDireccion3();
                    break;
                case 3:
                    getDireccion4();
                    break;
                case 4:
                    getDireccion1();
                    break;
            }
            KeyHandler.upPressed = false;
        }

        checkMovementCollision(); // Se comprueba antes de realizar ningún movimiento para evitar que se salte la
                                  // colision.

        if (KeyHandler.downPressed) {
            // Si el mino no ha tocado el suelo, puede seguir bajando.
            if (bottomCollision == false) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                // Cuando se mueve abajo, se resetea el autoDropCounter
                autoDropCounter = 0;
            }
            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed) {
            if (leftCollision == false) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }

            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed) {
            if (rightCollision == false) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }

            KeyHandler.rightPressed = false;
        }

        if (bottomCollision) {
            deactivating=true; // Si hay colision, se desactiva el Mino
        } else {

            autoDropCounter++; // El contador aumenta cada frame
            if (autoDropCounter == PlayManager.dropInterval) {
                // El mino baja
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
    }
    private void deactivating(){
        deactivateCounter++;

        // Esperar 45 frames hasta desactivarse
        if(deactivateCounter==45){
            deactivateCounter = 0;
            checkMovementCollision(); // Chequear si sigue colisionando con el suelo

            // Si el bottom sigue colisionando despues de 45 frames, se desactiva el mino.
            if(bottomCollision){
                activo = false;
            }
        }
    }
    public void draw(Graphics2D g2) {

        int margen = 2; // Añadimos una linea de 2px para diferenciar cada bloque del mino.
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x + margen, b[0].y + margen, Block.SIZE - (margen * 2), Block.SIZE - (margen * 2));
        g2.fillRect(b[1].x + margen, b[1].y + margen, Block.SIZE - (margen * 2), Block.SIZE - (margen * 2));
        g2.fillRect(b[2].x + margen, b[2].y + margen, Block.SIZE - (margen * 2), Block.SIZE - (margen * 2));
        g2.fillRect(b[3].x + margen, b[3].y + margen, Block.SIZE - (margen * 2), Block.SIZE - (margen * 2));
    }

}
