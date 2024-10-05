package main.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Skeleton {
    public int x, y;
    private Image skeletonImageLeft;  // Imagen cuando el esqueleto va hacia la izquierda
    private Image skeletonImageRight; // Imagen cuando el esqueleto va hacia la derecha
    private Player player;
    private int width = 64;  // Ancho del esqueleto
    private int height = 64; // Alto del esqueleto

    private long lastDamageTime = 0;
    private long damageInterval = 1000;
    public boolean isDead = false;
    private int health = 5;

    private boolean movingRight = true; // Indica si el esqueleto está yendo a la derecha
    private List<Skeleton> skeletons; // Lista de todos los esqueletos para detectar colisiones
    private List<Arrow> arrows; // Lista de flechas disparadas
    private long lastArrowShotTime = 0; // Tiempo del último disparo
    private long arrowInterval = 2000; // Intervalo entre disparos

    private boolean isWaiting = false; // Indica si el esqueleto está en espera
    private long waitStartTime; // Tiempo en que comenzó a esperar
    private long waitDuration; // Duración de la espera
    private Random random;

    public Skeleton(int x, int y, Player player, Image skeletonImageLeft, Image skeletonImageRight, List<Skeleton> skeletons) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.skeletonImageLeft = skeletonImageLeft;
        this.skeletonImageRight = skeletonImageRight;
        this.skeletons = skeletons;  // Guardamos la lista de esqueletos
        this.arrows = new ArrayList<>(); // Inicializamos la lista de flechas
        this.random = new Random(); // Inicializamos el generador de números aleatorios
    }

    public void update() {
        if (isDead) return;

        // Lógica de espera
        if (isWaiting) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - waitStartTime >= waitDuration) {
                isWaiting = false; // Termina la espera
                shootArrow(); // Dispara la flecha después de esperar
                lastArrowShotTime = currentTime; // Actualiza el tiempo de disparo
            }
            return; // No se mueve ni hace nada mientras espera
        }

        // Movimiento hacia el jugador en el eje X
        if (x < player.x) {
            x += 1; // Moverse hacia la derecha
            movingRight = true;  // Está yendo a la derecha
        } else if (x > player.x) {
            x -= 1; // Moverse hacia la izquierda
            movingRight = false; // Está yendo a la izquierda
        }

        // Lógica de disparo
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastArrowShotTime >= arrowInterval) {
            // Establece un tiempo de espera aleatorio entre 1000 y 3000 ms
            waitDuration = random.nextInt(2000) + 1000; // 1000 a 3000 ms
            waitStartTime = currentTime; // Guardar el tiempo de inicio de espera
            isWaiting = true; // Establecer el estado de espera
        }

        // Actualizar flechas
        for (int i = 0; i < arrows.size(); i++) {
            Arrow arrow = arrows.get(i);
            arrow.update();
            if (arrow.isOutOfBounds()) {
                arrows.remove(i);
                i--;
            }
        }

        // Detectar si colisiona con el jugador
        if (isCollidingWithPlayer()) {
            if (currentTime - lastDamageTime >= damageInterval && player.vida > 0) {
                player.vida--; // Reducir la vida del jugador
                lastDamageTime = currentTime;
            }
        }

        // Detectar colisiones con otros esqueletos
        checkCollisionWithSkeletons();
    }

    public void draw(Graphics g) {
        if (!isDead) {
            // Dibujar la imagen correspondiente dependiendo de la dirección
            if (movingRight) {
                g.drawImage(skeletonImageRight, x, y, null);
            } else {
                g.drawImage(skeletonImageLeft, x, y, null);
            }
            // Dibujar las flechas
            for (Arrow arrow : arrows) {
                arrow.draw(g);
            }
        }
    }

    // Método para disparar una flecha
    private void shootArrow() {
        arrows.add(new Arrow(x + (movingRight ? width : 0), y + height / 2 - 20, movingRight, player));
    }

    // Método para detectar colisión con el jugador
    private boolean isCollidingWithPlayer() {
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();
        
        return (x < player.x + playerWidth && x + width > player.x &&
                y < player.y + playerHeight && y + height > player.y);
    }

    // Método para detectar colisiones entre esqueletos
    private void checkCollisionWithSkeletons() {
        for (Skeleton other : skeletons) {
            if (other != this && isCollidingWithSkeleton(other)) {
                // Empujar a los esqueletos fuera del área de colisión
                if (x < other.x) {
                    x -= 1; // Moverse a la izquierda
                } else {
                    x += 1; // Moverse a la derecha
                }
            }
        }
    }

    // Método para detectar si dos esqueletos colisionan
    private boolean isCollidingWithSkeleton(Skeleton other) {
        return (x < other.x + width && x + width > other.x &&
                y < other.y + height && y + height > other.y);
    }

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

        // Verificar colisión con otros esqueletos
        checkCollisionWithSkeletonsKnockback(knockbackDistance / 2); // Propagar el empuje a otros esqueletos
    }

    // Modificación del método de colisiones con esqueletos para incluir el empuje
    private void checkCollisionWithSkeletonsKnockback(int secondaryKnockback) {
        for (Skeleton other : skeletons) {
            if (other != this && isCollidingWithSkeleton(other)) {
                // Si este esqueleto colisiona con otro, empujar al otro esqueleto
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
