package jogo;

public abstract class Bloco extends ElementoJogo {
    protected boolean transponivel; 
    protected boolean destrutivel;  

    public Bloco(int posX, int posY, boolean transponivel, boolean destrutivel) {
        super(posX, posY);
        this.transponivel = transponivel;
        this.destrutivel = destrutivel;
    }

    public boolean isTransponivel() { return transponivel; }
    public boolean isDestrutivel() { return destrutivel; }
    
    
    public abstract void interagirComTiro(Projetil tiro);
}
