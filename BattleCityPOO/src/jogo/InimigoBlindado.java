package jogo;

public class InimigoBlindado extends TanqueInimigo {

    public InimigoBlindado(int posX, int posY) {
        super(posX, posY, 2, 1, "Blindado"); 
    }

    @Override
    protected void executarComportamentoAI() {
        TanqueJogador jogador = mapa.getJogador();
        
        if (jogador != null && jogador.getVida() > 0) {
            int diffX = jogador.getPosX() - this.posX;
            int diffY = jogador.getPosY() - this.posY;
            
            if (Math.abs(diffX) > Math.abs(diffY)) {
                this.direcaoAtual = (diffX > 0) ? Direcao.DIREITA : Direcao.ESQUERDA;
            } else {
                this.direcaoAtual = (diffY > 0) ? Direcao.BAIXO : Direcao.CIMA;
            }
        }
        
        int proximoX = this.posX;
        int proximoY = this.posY;
        switch (this.direcaoAtual) {
            case CIMA:    proximoY--; break;
            case BAIXO:   proximoY++; break;
            case ESQUERDA: proximoX--; break;
            case DIREITA:  proximoX++; break;
        }
        
        if (mapa.podeMover(proximoX, proximoY)) {
            mover(this.direcaoAtual); 
        } else {
            Direcao[] direcoes = Direcao.values();
            this.direcaoAtual = direcoes[random.nextInt(direcoes.length)];
        }
        tentarAtirar(50);
    }

    @Override
    protected int getTempoDeEspera() {
        return 1000;
    }
}