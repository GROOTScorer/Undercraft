package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class SuperSalto {
    private Player player;
    private int x, y;
    public boolean isActive;
    private boolean isEPressed;
    
    public SuperSalto(int x, int y, Player player) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.isActive = true; // El objeto está activo cuando se crea
    }
    
    public void update(boolean isEPressed) {
        this.isEPressed = isEPressed;
        if (isActive && checkCollision()) {
            // Aumentar la altura del salto del jugador
        	if(isEPressed) {
                if (Player.salto == 15) { // Solo si el salto no está ya potenciado
                    Player.salto = 22; // Ajusta este valor según sea necesario
                }      
                isActive = false;

        	}
            
            // Aquí podrías realizar otras acciones, como aumentar la puntuación del jugador
        }
    }
    
    public void draw(Graphics g) {
        if (isActive) {
            // Establecer el color verde
            g.setColor(Color.BLUE); // Cambié el color a azul para diferenciar el power-up de vida
            g.fillRect(x, y, 50, 50); // Usar fillRect para rellenar el cuadrado con color
        }
    }

    private boolean checkCollision() {
        // Obtener el rectángulo del jugador
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        // Obtener el rectángulo del objeto SaltoPotenciado
        Rectangle saltoPotenciadoRect = new Rectangle(x, y, 50, 50);
        // Verificar si los rectángulos se intersectan
        return playerRect.intersects(saltoPotenciadoRect);
    }


}