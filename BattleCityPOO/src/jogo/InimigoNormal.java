package jogo;

public class InimigoNormal extends TanqueInimigo {
    
    public InimigoNormal(int posX, int posY) {
        super(posX, posY, 1, 1, "Normal");
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