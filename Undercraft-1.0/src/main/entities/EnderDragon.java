package main.entities;

import java.awt.Graphics;
import java.awt.Image;

import main.ui.Window;

public class EnderDragon {
    public int x, y;
    private Image enderDragonImageLeft;  // Imagen cuando el zombie va hacia la izquierda
    private Image enderDragonImageRight; // Imagen cuando el zombie va hacia la derecha
    private Player player;
    private int width = 283;  // Ancho del zombie
    private int height = 64; // Alto del zombie
    
    private long lastDamageTime = 0;
    private long damageInterval = 1000;
    public boolean isDead = false;
    public int health = 10;
    private int estadoX = 0;
    private int estadoY = 0;
    private int countReposo = 0;
    private int j = 0;
    private int movX = 700;
    private int movY = 400;
    private boolean isCPressed;

    private boolean movingRight = true; // Indica si el zombie está yendo a la derecha
    
	public EnderDragon(int x, int y, Player player, Image enderDragonImageLeft, Image enderDragonImageRight) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.enderDragonImageLeft = enderDragonImageLeft;
        this.enderDragonImageRight = enderDragonImageRight;
	}
	
    public void update(boolean isCPressed) {
        // Movimiento hacia el jugador en el eje X
    	this.isCPressed = isCPressed;
        // Detectar si colisiona con el jugador
        if (isCollidingWithPlayer()) {
            long currentTime = System.currentTimeMillis();
            
        	if(isCPressed) {
        		health--;
        	}

            
            if (currentTime - lastDamageTime >= damageInterval && player.vida > 0 && health != 0) {
                player.vida--; // Reducir la vida del jugador
                lastDamageTime = currentTime;
            }
        }

    }
    
    public void draw(Graphics g) {
        if (health > 0) {
            // Dibujar la imagen correspondiente dependiendo de la dirección
            if (Window.estadoX == 1) {
                g.drawImage(enderDragonImageRight, x, y, null);
            } else {
                g.drawImage(enderDragonImageLeft, x, y, null);
            }
        }
    }
    
    public void takeDamage() {
        if (health > 0) {
            health--;
            if (health <= 0) {
                die();
            }
        }
    }
    
    private void die() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }

    // Método para detectar colisión con el jugador
    public boolean isCollidingWithPlayer() {
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();
        
        return (x < player.x + playerWidth && x + width > player.x &&
                y < player.y + playerHeight && y + height > player.y);
    }
    
    public void setX(int in_x) {
    	x = in_x;
    }
    
    public void setY(int in_y) {
    	y = in_y;
    }

}
