package jogo;

public class BlocoTijolo extends Bloco {
    private boolean destruido;

    public BlocoTijolo(int posX, int posY) {
        
        super(posX, posY, false, true); 
        this.destruido = false;
        
    }

    @Override
    public void interagirComTiro(Projetil tiro) {
        tiro.destruir();
        this.destruido = true; 
    }

    public boolean isDestruido() { 
        return destruido; 
    }
}