package jogo;

public class Base extends Bloco {
    private boolean destruida;
    private boolean protegida; 

    public Base(int posX, int posY) {
        super(posX, posY, false, true);
        this.destruida = false;
        this.protegida = false;
    }

    @Override
    public void interagirComTiro(Projetil tiro) {
        tiro.destruir();
        
        if (!protegida) {
            this.destruida = true;
            System.out.println("GAME OVER! A Base foi destruída.");
        }
    }

    public boolean isDestruida() { return destruida; }
    
    public boolean isProtegida() { return protegida; }
    public void setProtegida(boolean protegida) { this.protegida = protegida; }
}