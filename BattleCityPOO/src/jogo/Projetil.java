package jogo;

public class Projetil extends ElementoJogo implements Movel, Runnable {
    private Direcao direcao;
    private int velocidade;
    private boolean ativo;
    private Mapa mapa; 
    private boolean tiroInimigo; 
    private int forcaTiro;      
    private boolean tiroEstrela; 

    public Projetil(int posX, int posY, Direcao direcao, int velocidade, boolean tiroInimigo) {
        super(posX, posY);
        this.direcao = direcao;
        this.velocidade = velocidade;
        this.ativo = true;
        this.tiroInimigo = tiroInimigo;
        this.forcaTiro = 1;
        this.tiroEstrela = false;
    }

    public Projetil(int posX, int posY, Direcao direcao, int velocidade, boolean tiroInimigo, int forcaTiro, boolean tiroEstrela) {
        super(posX, posY);
        this.direcao = direcao;
        this.velocidade = velocidade;
        this.ativo = true;
        this.tiroInimigo = tiroInimigo;
        this.forcaTiro = forcaTiro;
        this.tiroEstrela = tiroEstrela;
    }

    public void setMapa(Mapa mapa) { this.mapa = mapa; }
    public boolean isTiroInimigo() { return tiroInimigo; }
    public int getForcaTiro() { return forcaTiro; }
    
    public boolean isTiroEstrela() { return tiroEstrela; } 

    @Override
    public void mover(Direcao d) {
        switch (this.direcao) {
            case CIMA:    this.posY -= velocidade; break;
            case BAIXO:   this.posY += velocidade; break;
            case ESQUERDA: this.posX -= velocidade; break;
            case DIREITA:  this.posX += velocidade; break;
        }
    }

    @Override
    public int getVelocidade() { return velocidade; }
    public boolean isAtivo() { return ativo; }
    public void destruir() { this.ativo = false; }

    @Override
    public void run() {
        while (isAtivo()) {
            if (mapa != null && !mapa.isPausado()) {
                mapa.processarColisaoTiro(this);
            }
            try {
                Thread.sleep(50); 
            } catch (InterruptedException e) { }

            if (isAtivo() && mapa != null && !mapa.isPausado()) {
                mover(this.direcao);
            }
        }
    }
}