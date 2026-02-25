package jogo;

public class InimigoRapido extends TanqueInimigo {

    public InimigoRapido(int posX, int posY) {
        super(posX, posY, 1, 1, "Rápido");
    }

    @Override
    protected void executarComportamentoAI() {
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
            switch (this.direcaoAtual) {
                case CIMA: this.direcaoAtual = Direcao.BAIXO; break;
                case BAIXO: this.direcaoAtual = Direcao.CIMA; break;
                case ESQUERDA: this.direcaoAtual = Direcao.DIREITA; break;
                case DIREITA: this.direcaoAtual = Direcao.ESQUERDA; break;
            }
        }
        tentarAtirar(25);
    }

    @Override
    protected int getTempoDeEspera() {
        return 500;
    }
}