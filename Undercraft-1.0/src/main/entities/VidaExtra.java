package main.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class VidaExtra {
    
    private Player player;
    private int x, y;
    private boolean isActive;
    private Image vidaExtraImage; // Variable para almacenar la imagen

    public VidaExtra(int x, int y, Player player) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.isActive = true; // El objeto está activo cuando se crea

        // Cargar la imagen de vida extra
        try {
            vidaExtraImage = ImageIO.read(getClass().getResource("../ui/ManzanaDorada.png"));
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir error si no se encuentra la imagen
        }
    }

    public void update() {
        if (isActive && checkCollision()) {
            if (Player.vida < 10) {
                // Asegurarse de no exceder el máximo de 10
                int vidaRestante = 10 - Player.vida;
                Player.vida += Math.min(vidaRestante, 5); // Sumar solo lo necesario para no superar 10
            }
            isActive = false; // Desactivar la vida extra después de ser recogida
        }
    }

    public void draw(Graphics g) {
        if (isActive) {
            if (vidaExtraImage != null) {
                // Dibujar la imagen si está activa y se ha cargado correctamente
                g.drawImage(vidaExtraImage, x, y, 32, 32, null);
            }
        }
    }

    private boolean checkCollision() {
        // Obtener el rectángulo del jugador
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        // Obtener el rectángulo del objeto VidaExtra
        Rectangle vidaExtraRect = new Rectangle(x, y, 50, 50);
        // Verificar si los rectángulos se intersectan
        return playerRect.intersects(vidaExtraRect);
    }
}
