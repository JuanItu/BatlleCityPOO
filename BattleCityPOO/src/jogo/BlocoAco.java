package jogo;

public class BlocoAco extends Bloco {
    private boolean destruido = false; 

    public BlocoAco(int posX, int posY) {
        super(posX, posY, false, false);
    }

    @Override
    public void interagirComTiro(Projetil tiro) {
        tiro.destruir(); 
    
        if (tiro.isTiroEstrela()) {
            this.destruido = true;
        }
    }
    
    public boolean isDestruido() { return destruido; }
}