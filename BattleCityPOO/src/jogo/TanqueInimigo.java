package jogo;

import java.util.Random;

public abstract class TanqueInimigo extends Tanque {
    protected String tipo; 
    protected boolean vivo;
    protected Mapa mapa;
    protected Random random;

    public TanqueInimigo(int posX, int posY, int vida, int velocidade, String tipo) {
        super(posX, posY, vida, velocidade);
        this.tipo = tipo;
        this.direcaoAtual = Direcao.BAIXO; 
        this.vivo = true; 
        this.random = new Random();
    }

    public void setMapa(Mapa mapa) { this.mapa = mapa; }
    public String getTipo() { return tipo; }
    public void destruir() { this.vivo = false; }

    @Override
    public Projetil atirar() {
        int tiroX = this.posX;
        int tiroY = this.posY;

        switch (this.direcaoAtual) {
            case CIMA:    tiroY--; break; 
            case BAIXO:   tiroY++; break; 
            case ESQUERDA: tiroX--; break; 
            case DIREITA:  tiroX++; break; 
        }
        
        return new Projetil(tiroX, tiroY, this.direcaoAtual, 1, true);
    }

    protected void tentarAtirar(int chanceTiro) {
        if (random.nextInt(100) < chanceTiro) {
            Projetil tiro = atirar();
            tiro.setMapa(mapa);
            mapa.getProjeteis().add(tiro);
            new Thread(tiro).start();
        }
    }
    
    protected abstract void executarComportamentoAI();
    protected abstract int getTempoDeEspera();

    @Override
    public void run() {
        while (vivo) {
            if (mapa != null && !mapa.isPausado() && !mapa.isInimigosCongelados()) {
                executarComportamentoAI();
            }
            try {
                Thread.sleep(getTempoDeEspera());
            } catch (InterruptedException e) {
                System.out.println("Thread do Inimigo interrompida.");
            }
        }
    }
}