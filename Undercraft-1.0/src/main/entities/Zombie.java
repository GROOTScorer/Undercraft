package main.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import main.ui.Window;

public class Zombie {
    public int x, y;
    private Image zombieImageLeft;  // Imagen cuando el zombie va hacia la izquierda
    private Image zombieImageRight; // Imagen cuando el zombie va hacia la derecha
    private Player player;
    private int width = 64;  // Ancho del zombie
    private int height = 64; // Alto del zombie
    
    private long lastDamageTime = 0;
    private long damageInterval = 1000;
    public boolean isDead = false;
    private int health = 3;

    private boolean movingRight = true; // Indica si el zombie está yendo a la derecha

    private List<Zombie> zombies; // Lista de todos los zombies para detectar colisiones

    public Zombie(int x, int y, Player player, Image zombieImageLeft, Image zombieImageRight, List<Zombie> zombies) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.zombieImageLeft = zombieImageLeft;
        this.zombieImageRight = zombieImageRight;
        this.zombies = zombies;  // Guardamos la lista de zombies
    }

    public void update() {
        // Movimiento hacia el jugador en el eje X
        if (x < player.x) {
            x += 1; // Moverse hacia la derecha
            movingRight = true;  // Está yendo a la derecha
        } else if (x > player.x) {
            x -= 1; // Moverse hacia la izquierda
            movingRight = false; // Está yendo a la izquierda
        }

        // Detectar si colisiona con el jugador
        if (isCollidingWithPlayer()) {
            long currentTime = System.currentTimeMillis();
            
            if (currentTime - lastDamageTime >= damageInterval && player.vida > 0) {
                player.vida--; // Reducir la vida del jugador
                lastDamageTime = currentTime;
            }
        }

        // Detectar colisiones con otros zombies
        checkCollisionWithZombies();

        // Colisiones con los bordes del nivel
        if (Window.nivelSeleccionado == 3 && Window.frameSeleccionado == 3) {
            if (x <= 192) {
                x = 192;
            }
        }

        if (Window.nivelSeleccionado == 3 && Window.frameSeleccionado == 5) {
            if (x <= 455) {
                x = 455;
            }
        }
        
        if (Window.nivelSeleccionado == 4 && Window.frameSeleccionado == 2) {
            if (x >= 32*11 - width && x <= 32*11 + 32*4 - width  ) {
                x = 32*11 - width ;
            }
            
            else if(x <= 32*11 + 32*4 && x >= 32 * 11 ) {
            	x = 32*11 + 32*4;
            }
        }
    }

    public void draw(Graphics g) {
        if (health > 0) {
            // Dibujar la imagen correspondiente dependiendo de la dirección
            if (movingRight) {
                g.drawImage(zombieImageRight, x, y, null);
            } else {
                g.drawImage(zombieImageLeft, x, y, null);
            }
        }
    }

    // Método para detectar colisión con el jugador
    private boolean isCollidingWithPlayer() {
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();
        
        return (x < player.x + playerWidth && x + width > player.x &&
                y < player.y + playerHeight && y + height > player.y);
    }

    // Método para detectar colisiones entre zombies
    private void checkCollisionWithZombies() {
        for (Zombie other : zombies) {
            if (other != this && isCollidingWithZombie(other)) {
                // Empujar a los zombies fuera del área de colisión
                if (x < other.x) {
                    x -= 1; // Moverse a la izquierda
                } else {
                    x += 1; // Moverse a la derecha
                }
            }
        }
    }

    // Método para detectar si dos zombies colisionan
    private boolean isCollidingWithZombie(Zombie other) {
        return (x < other.x + other.width && x + width > other.x &&
                y < other.y + other.height && y + height > other.y);
    }

    // Otros métodos y getters

    public void takeDamage() {
        if (health > 0) {
            health--;
            if (health <= 0) {
                die();
            } else {
                applyKnockback();  // Aplicar retroceso al recibir daño
            }
        }
    }

    // Método para aplicar retroceso
    private void applyKnockback() {
        int knockbackDistance = 20; // Distancia del retroceso

        // Retroceso basado en la dirección de movimiento
        if (movingRight) {
            x -= knockbackDistance; // Si va a la derecha, retroceder a la izquierda
        } else {
            x += knockbackDistance; // Si va a la izquierda, retroceder a la derecha
        }

        // Verificar colisión con otros zombies
        checkCollisionWithZombiesKnockback(knockbackDistance / 2); // Propagar el empuje a otros zombies
    }

    // Modificación del método de colisiones con zombies para incluir el empuje
    private void checkCollisionWithZombiesKnockback(int secondaryKnockback) {
        for (Zombie other : zombies) {
            if (other != this && isCollidingWithZombie(other)) {
                // Si este zombie colisiona con otro, empujar al otro zombie
                if (x < other.x) {
                    other.x += secondaryKnockback; // Empujar a la derecha
                } else {
                    other.x -= secondaryKnockback; // Empujar a la izquierda
                }
            }
        }
    }


    private void die() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
