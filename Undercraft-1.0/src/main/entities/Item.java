package main.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import main.ui.Window;

public class Item {

    private int x, y, width, height;
    private Player player;
    public boolean isActive; // Estado del objeto (si está activo o no)
    private boolean isColliding; // Estado de colisión
    public boolean isEPressed; // Almacena si se presionó 'E'
    private Image activeImage; // Imagen cuando el objeto está activo
    private Image inactiveImage; // Imagen cuando el objeto está desactivado
    public boolean estado = false;
    // Constructor que recibe dos imágenes
    public Item(int x, int y, int width, int height, Player player, Image activeImage, Image inactiveImage) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.player = player;
        this.isActive = true; // El objeto está activo al inicio
        this.isColliding = false; // Al principio no está colisionando
        this.isEPressed = false; // Al principio no se ha presionado 'E'
        this.activeImage = activeImage; // Imagen activa
        this.inactiveImage = inactiveImage; // Imagen desactivada
    }

    public void update(boolean isEPressed) {
        // Actualizamos el estado de la tecla 'E'
        this.isEPressed = isEPressed;
        
        // Verificar si el jugador está colisionando con el objeto
        if (isActive && checkCollision()) {
            isColliding = true;
            if (isEPressed) { // Si se presiona la tecla E
                Window.palancaActivada = true;
                isActive = false; // Cambiar a estado desactivado
                estado = true;
            }
        } else {
            isColliding = false; // No hay colisión
        }
    }

    public void draw(Graphics g) {
        if (isActive) {
            // Dibujar la imagen activa si está activo
            g.drawImage(activeImage, x, y, width, height, null);
        } else {
            // Dibujar la imagen desactivada si no está activo
            g.drawImage(inactiveImage, x, y, width, height, null);
        }
    }

    // Este método ahora devuelve el estado de la tecla 'E'
    public boolean estado() {
        return estado;
    }
    
    // Función para verificar si el jugador está colisionando con el objeto
    private boolean checkCollision() {
        // Obtener el rectángulo del jugador
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        // Obtener el rectángulo del objeto Item
        Rectangle itemRect = new Rectangle(x, y, width, height);
        // Verificar si los rectángulos se intersectan (colisión)
        return playerRect.intersects(itemRect);
    }
    
    public void setX(int in_x) {
    	x = in_x;
    }
}
