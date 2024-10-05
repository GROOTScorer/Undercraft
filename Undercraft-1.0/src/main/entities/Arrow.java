package main.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Arrow {

    private int x, y;
    private int width = 44;  // Ancho de la flecha
    private int height = 14;  // Alto de la flecha
    private int speed = 5;   // Velocidad de la flecha
    private boolean movingRight;
    private Player player;   // Referencia al jugador
    private boolean hasCollided = false; // Indica si la flecha ya colisionó
    private Image arrowImageLeft;  // Imagen de la flecha apuntando a la izquierda
    private Image arrowImageRight; // Imagen de la flecha apuntando a la derecha
    private long spawnTime; // Tiempo en que la flecha fue disparada
    private static final long LIFETIME = 4000; // Tiempo de vida de la flecha en milisegundos

    public Arrow(int x, int y, boolean movingRight, Player player) {
        this.x = x;
        this.y = y;
        this.movingRight = movingRight;
        this.player = player; // Asignar referencia al jugador
        this.spawnTime = System.currentTimeMillis(); // Guardar el tiempo de disparo

        // Cargar las imágenes de las flechas
        try {
            arrowImageLeft = ImageIO.read(getClass().getResource("FlechaIzquierda.png"));
            arrowImageRight = ImageIO.read(getClass().getResource("FlechaDerecha.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Mover la flecha hacia la derecha o izquierda
        if (movingRight) {
            x += speed;
        } else {
            x -= speed;
        }

        // Verificar colisión con el jugador solo si no ha colisionado antes
        if (!hasCollided && isCollidingWithPlayer() && player.vida > 0) {
            player.vida--; // Reducir la vida del jugador
            hasCollided = true; // Marcar la flecha como colisionada
        }
    }

    public void draw(Graphics g) {
        // Dibujar la flecha usando la imagen correspondiente
        if (movingRight) {
            g.drawImage(arrowImageRight, x, y, width, height, null); // Dibujar flecha a la derecha
        } else {
            g.drawImage(arrowImageLeft, x, y, width, height, null); // Dibujar flecha a la izquierda
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOutOfBounds() {
        // Verificar si la flecha sale de los límites de la pantalla
        return x < 0 || x > 800 || isExpired(); // Cambia el ancho de acuerdo a tu ventana
    }

    // Verificar si la flecha ha expirado
    private boolean isExpired() {
        return System.currentTimeMillis() - spawnTime > LIFETIME;
    }

    // Verificar colisión con el jugador
    private boolean isCollidingWithPlayer() {
        int playerX = player.getX();
        int playerY = player.getY();
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();

        // Comprobar si la flecha está dentro de los límites del jugador
        return (x < playerX + playerWidth && x + width > playerX &&
                y < playerY + playerHeight && y + height > playerY);
    }
}
