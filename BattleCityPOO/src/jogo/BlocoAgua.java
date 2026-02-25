package jogo;

public class BlocoAgua extends Bloco {
    public BlocoAgua(int posX, int posY) {
    
        super(posX, posY, false, false);

    }

    @Override
    public void interagirComTiro(Projetil tiro) {
    }
}