package jogo;

import java.util.Random;

public class PowerUp extends ElementoJogo {
    public enum Tipo { ESTRELA, CAPACETE, PA, RELOGIO, BOMBA, VIDA }
    
    private Tipo tipo;
    private long tempoCriacao;

    public PowerUp(int posX, int posY) {
        super(posX, posY);
        this.tempoCriacao = System.currentTimeMillis();
        Tipo[] tipos = Tipo.values();
        this.tipo = tipos[new Random().nextInt(tipos.length)];
    }

    public Tipo getTipo() { return tipo; }
    
    public boolean expirou() { 
        return System.currentTimeMillis() - tempoCriacao > 15000; 
    } 
}