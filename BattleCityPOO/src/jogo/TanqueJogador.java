package jogo;

public class TanqueJogador extends Tanque {
    private int pontuacao;
    private boolean vivo; 
    private String tipo;

    private long ultimoMovimento;
    private long ultimoTiro;
    private long cooldownMovimento;
    private long cooldownTiro;

    // --- VARIÁVEIS DE POWER-UP ---
    private boolean invulneravel = false;
    private boolean tiroEstrela = false;
    private long fimInvulnerabilidade = 0;
    private long fimEstrela = 0; // NOVO: Tempo da estrela

    public TanqueJogador(int posX, int posY, String tipo) {
        super(posX, posY, 3, 1); 
        this.pontuacao = 0;
        this.vivo = true;
        this.tipo = tipo;

        switch (tipo) {
            case "Forte": this.vida = 4; this.cooldownMovimento = 250; this.cooldownTiro = 800; break;
            case "Rápido": this.vida = 2; this.cooldownMovimento = 80; this.cooldownTiro = 500; break;
            case "Metralhadora": this.vida = 2; this.cooldownMovimento = 150; this.cooldownTiro = 200; break;
            case "Normal": default: this.vida = 3; this.cooldownMovimento = 150; this.cooldownTiro = 500; break;
        }
    }

    // --- MÉTODOS DE POWER-UP ---
    public void adicionarVida() {
        if (this.vida < 5) this.vida++; 
    }

    public void ativarInvulnerabilidade(long duracao) {
        this.invulneravel = true;
        this.fimInvulnerabilidade = System.currentTimeMillis() + duracao;
    }

    public boolean isInvulneravel() { return invulneravel; }
    
    // NOVO: Estrela agora recebe tempo de duração
    public void ativarTiroEstrela(long duracao) { 
        this.tiroEstrela = true; 
        this.fimEstrela = System.currentTimeMillis() + duracao;
    }
    
    public boolean isTiroEstrela() { return tiroEstrela; }

    @Override
    public void receberDano(int dano) {
        if (invulneravel) return; 
        super.receberDano(dano);
    }

    public String getTipo() { return tipo; }
    public boolean podeMoverAgora() { return System.currentTimeMillis() - ultimoMovimento >= cooldownMovimento; }
    public void registrarMovimento() { this.ultimoMovimento = System.currentTimeMillis(); }
    public boolean podeAtirarAgora() { return System.currentTimeMillis() - ultimoTiro >= cooldownTiro; }
    public void registrarTiro() { this.ultimoTiro = System.currentTimeMillis(); }

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

        int forcaTiro = tiroEstrela ? 2 : 1;
        return new Projetil(tiroX, tiroY, this.direcaoAtual, 1, false, forcaTiro, tiroEstrela); 
    }

    public void adicionarPontos(int pontos) { this.pontuacao += pontos; }
    public int getPontuacao() { return pontuacao; }
    public void destruir() { this.vivo = false; }

    @Override
    public void run() {
        while (vivo) {
            try {
                long agora = System.currentTimeMillis();
                if (invulneravel && agora > fimInvulnerabilidade) {
                    invulneravel = false;
                }
                
                if (tiroEstrela && agora > fimEstrela) {
                    tiroEstrela = false;
                }

                Thread.sleep(50); 
            } catch (InterruptedException e) { }
        }
    }
}