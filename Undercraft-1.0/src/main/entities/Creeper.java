package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import main.ui.Window;

public class Creeper {

    // Variables del creeper
    private int x, y;
    private int speed = 2; // Velocidad de movimiento del creeper
    private Player player;
    private int explosionRadius = 100; // Rango en el que se activa el temporizador
    private int activationRadius = 50; // Rango más pequeño para activar el temporizador
    private long timerStart = 0; // Tiempo en el que se activó el temporizador
    private boolean isTimerActive = false; // Indica si el temporizador está activo
    private long explosionTime = 1000; // Tiempo para la explosión (3 segundos)
    private boolean hasExploded = false; // Indica si el creeper ya explotó
    private int maxDamage = 5; // Daño máximo cuando está en el centro
    public boolean isDead = false;
    private int health = 3; // Vida del creeper

    // Variables para manejo de imágenes
    private Image creeperImageLeft;  // Imagen cuando el creeper va hacia la izquierda
    private Image creeperImageRight; // Imagen cuando el creeper va hacia la derecha
    private boolean movingRight = true; // Indica si el creeper está yendo a la derecha

    public Creeper(int x, int y, Player player, Image creeperImageLeft, Image creeperImageRight) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.creeperImageLeft = creeperImageLeft;
        this.creeperImageRight = creeperImageRight;
    }

    public void update() {
        // Si el creeper no ha explotado aún
        if (!hasExploded) {
            // Detectar colisión o cercanía del jugador
            if (isPlayerInActivationRange()) {
                if (!isTimerActive) {
                    // Activar el temporizador si no está activo
                    timerStart = System.currentTimeMillis();
                    isTimerActive = true;
                } else {
                    // Si el temporizador está activo, comprobar el tiempo transcurrido
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - timerStart >= explosionTime) {
                        // El creeper explota y calcula el daño basado en la distancia
                        int damage = calculateDamage();
                        player.vida -= damage;
                        hasExploded = true;
                    }
                }
            } else {
                // Si el jugador sale del rango de activación, desactivar el temporizador
                if (isTimerActive) {
                    isTimerActive = false;
                    timerStart = 0;
                }
                // Mover al creeper hacia el jugador si el temporizador no está activo
                followPlayer();
            }
        }

        // Colisión de enemigos con suelo
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
    }

    public void draw(Graphics g) {
        // Dibujar solo si el creeper no ha explotado
        if (!hasExploded) {
            if (movingRight) {
                g.drawImage(creeperImageRight, x, y+7, null);
            } else {
                g.drawImage(creeperImageLeft, x, y+7, null);
            }
        }
    }

    // Función para verificar si el jugador está dentro del rango de activación
    private boolean isPlayerInActivationRange() {
        int playerX = player.getX();
        int playerY = player.getY();
        double distance = Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));
        return distance <= activationRadius;
    }

    // Función para calcular el daño basado en la distancia
    private int calculateDamage() {
        int playerX = player.getX();
        int playerY = player.getY();
        double distance = Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));

        // El daño disminuye linealmente con la distancia
        if (distance > explosionRadius) {
            return 0; // Sin daño si está fuera del rango de explosión
        }

        // Calcular daño basado en qué tan cerca está del creeper
        double damageFactor = 1 - ((distance - (explosionRadius - activationRadius)) / activationRadius); // Factor de daño entre 0 y 1
        return (int) (maxDamage * damageFactor); // Daño proporcional a la cercanía
    }

    // Función para seguir al jugador
    private void followPlayer() {
        int playerX = player.getX();
        int playerY = player.getY();

        // Movimiento horizontal
        if (x < playerX) {
            x += speed;
            movingRight = true;  // Está yendo a la derecha
        } else if (x > playerX) {
            x -= speed;
            movingRight = false; // Está yendo a la izquierda
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void takeDamage() {
        if (health > 0) {
            health--; // Reducir la vida del creeper
            // Si la vida del creeper es 0 o menos, eliminar o marcar como "muerto"
            if (health <= 0) {
                die();
            } else {
                applyKnockback(); // Aplicar retroceso al recibir daño
            }
        }
    }

    private void applyKnockback() {
        int knockbackDistance = 20; // Distancia del retroceso

        // Retroceso basado en la dirección de movimiento
        if (movingRight) {
            x -= knockbackDistance; // Si va a la derecha, retroceder a la izquierda
        } else {
            x += knockbackDistance; // Si va a la izquierda, retroceder a la derecha
        }

        // Asegurar que no traspase las paredes o el borde del nivel
        if (Window.nivelSeleccionado == 3 && Window.frameSeleccionado == 3 && x <= 192) {
            x = 192;
        }
        if (Window.nivelSeleccionado == 3 && Window.frameSeleccionado == 5 && x <= 455) {
            x = 455;
        }
    }

    private void die() {
        isDead = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return 64;
    }

    public int getHeight() {
        return 64;
    }
}
