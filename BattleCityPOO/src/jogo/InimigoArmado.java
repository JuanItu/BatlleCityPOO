package jogo;

public class InimigoArmado extends TanqueInimigo {

    public InimigoArmado(int posX, int posY) {
        super(posX, posY, 1, 1, "Metralhadora");
    }

    @Override
    protected void executarComportamentoAI() {
        TanqueJogador jogador = mapa.getJogador();
        boolean alinhado = false;

        if (jogador != null && jogador.getVida() > 0) {
            if (jogador.getPosX() == this.posX) {
                this.direcaoAtual = (jogador.getPosY() > this.posY) ? Direcao.BAIXO : Direcao.CIMA;
                alinhado = true;
            } else if (jogador.getPosY() == this.posY) {
                this.direcaoAtual = (jogador.getPosX() > this.posX) ? Direcao.DIREITA : Direcao.ESQUERDA;
                alinhado = true;
            }
        }

        if (alinhado) {

            tentarAtirar(80); 

        } else {
            int proximoX = this.posX;
            int proximoY = this.posY;
            switch (this.direcaoAtual) {
                case CIMA: proximoY--; break;
                case BAIXO: proximoY++; break;
                case ESQUERDA: proximoX--; break;
                case DIREITA: proximoX++; break;
            }
            
            if (mapa.podeMover(proximoX, proximoY)) {
                mover(this.direcaoAtual); 
            } else {
                Direcao[] direcoes = Direcao.values();
                this.direcaoAtual = direcoes[random.nextInt(direcoes.length)];
            }
            tentarAtirar(25);
        }
    }

    @Override
    protected int getTempoDeEspera() {
        return 500;
    }
}