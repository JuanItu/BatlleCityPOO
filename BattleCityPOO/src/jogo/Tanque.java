package jogo;

public abstract class Tanque extends ElementoJogo implements Movel, Runnable {
    protected int vida;
    protected int velocidade;
    protected Direcao direcaoAtual;

    public Tanque(int posX, int posY, int vida, int velocidade) {
        super(posX, posY); 
        this.vida = vida;
        this.velocidade = velocidade;
        this.direcaoAtual = Direcao.CIMA; 
    }

    @Override
    public void mover(Direcao direcao) {
        this.direcaoAtual = direcao;
        switch (direcao) {
            case CIMA:    this.posY -= velocidade; break;
            case BAIXO:   this.posY += velocidade; break;
            case ESQUERDA: this.posX -= velocidade; break;
            case DIREITA:  this.posX += velocidade; break;
        }
    }

    @Override
    public int getVelocidade() {
        return velocidade;
    }

    public abstract Projetil atirar();

    public int getVida() { return vida; }
    public void receberDano(int dano) { this.vida -= dano; }
    public Direcao getDirecaoAtual() { return direcaoAtual; }
    public void setDirecaoAtual(Direcao d) { this.direcaoAtual = d; }
}