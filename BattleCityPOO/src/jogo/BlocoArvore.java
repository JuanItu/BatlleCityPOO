package jogo;

public class BlocoArvore extends Bloco {
    public BlocoArvore(int posX, int posY) {

        super(posX, posY, true, false);
        
    }

    @Override
    public void interagirComTiro(Projetil tiro) {
    }
}