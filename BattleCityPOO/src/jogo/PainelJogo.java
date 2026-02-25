package jogo;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;       
import java.io.File;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

public class PainelJogo extends JPanel {
    private Mapa mapa;
    private final int TAMANHO_BLOCO = 40;
    
    private final int OFFSET_X = 40; 
    private final int OFFSET_Y = 40; 
    
    private Image imagemGameOver;
    private Image imgTijolo;
    private Image imgMetal;
    private Image imgAgua;
    private Image imgArvore;
    private Image imgBase;
    private Image imgVidas;
    private Image imgTanqueContador; 

    private Image imgJogCima, imgJogBaixo, imgJogEsq, imgJogDir;
    private Image imgBotNormalCima, imgBotNormalBaixo, imgBotNormalEsq, imgBotNormalDir;
    private Image imgBotRapidoCima, imgBotRapidoBaixo, imgBotRapidoEsq, imgBotRapidoDir;
    private Image imgBotForteCima, imgBotForteBaixo, imgBotForteEsq, imgBotForteDir; 
    private Image imgBotMetralhaCima, imgBotMetralhaBaixo, imgBotMetralhaEsq, imgBotMetralhaDir;
    
    private Image imgBomba;
    private Image imgCapacete;
    private Image imgEstrela;
    private Image imgPa;
    private Image imgRelogio;
    private Image imgVidaExtra;

    public PainelJogo(Mapa mapa) {
        this.mapa = mapa;
        setBackground(new Color(100, 100, 100));

        imagemGameOver = carregarImagem("gameover.png");
        imgTijolo = carregarImagem("texturas/tijolo.jpg");
        imgMetal = carregarImagem("texturas/metal.png");
        imgAgua = carregarImagem("texturas/agua.gif");
        imgArvore = carregarImagem("texturas/arvore.png");
        imgBase = carregarImagem("texturas/wolf_base.gif");
        imgVidas = carregarImagem("texturas/vidas.png");
        imgTanqueContador = carregarImagem("texturas/tanque_contador.png");

        imgBomba = carregarImagem("texturas/bomba.png");
        imgCapacete = carregarImagem("texturas/capacete.png");
        imgEstrela = carregarImagem("texturas/estrela.gif");
        imgPa = carregarImagem("texturas/pa.png");
        imgRelogio = carregarImagem("texturas/relogio.png");
        imgVidaExtra = carregarImagem("texturas/vidas.png"); 

        String prefixo = "normal";
        if (mapa.getJogador() != null && mapa.getJogador().getTipo() != null) {
            prefixo = mapa.getJogador().getTipo().toLowerCase();
            if (prefixo.equals("rápido")) prefixo = "rapido"; 
            if (prefixo.equals("metralhadora")) prefixo = "metralha"; 
        }
        
        imgJogCima = carregarImagem("player/" + prefixo + "_cima.png");
        imgJogBaixo = carregarImagem("player/" + prefixo + "_baixo.png");
        imgJogEsq = carregarImagem("player/" + prefixo + "_esquerda.png");
        imgJogDir = carregarImagem("player/" + prefixo + "_direita.png");

        imgBotNormalCima = carregarImagem("bot/bot_normal_cima.png");
        imgBotNormalBaixo = carregarImagem("bot/bot_normal_baixo.png");
        imgBotNormalEsq = carregarImagem("bot/bot_normal_esquerda.png");
        imgBotNormalDir = carregarImagem("bot/bot_normal_direita.png");

        imgBotRapidoCima = carregarImagem("bot/bot_rapido_cima.png");
        imgBotRapidoBaixo = carregarImagem("bot/bot_rapido_baixo.png");
        imgBotRapidoEsq = carregarImagem("bot/bot_rapido_esquerda.png");
        imgBotRapidoDir = carregarImagem("bot/bot_rapido_direita.png");

        imgBotForteCima = carregarImagem("bot/bot_forte_cima.png");
        imgBotForteBaixo = carregarImagem("bot/bot_forte_baixo.png");
        imgBotForteEsq = carregarImagem("bot/bot_forte_esquerda.png");
        imgBotForteDir = carregarImagem("bot/bot_forte_direita.png");
        
        imgBotMetralhaCima = carregarImagem("bot/bot_metralha_cima.png");
        imgBotMetralhaBaixo = carregarImagem("bot/bot_metralha_baixo.png");
        imgBotMetralhaEsq = carregarImagem("bot/bot_metralha_esquerda.png");
        imgBotMetralhaDir = carregarImagem("bot/bot_metralha_direita.png");
    }

    private Image carregarImagem(String nomeArquivo) {
        File arquivo = new File("src/jogo/imagens/" + nomeArquivo);
        if (arquivo.exists()) return new ImageIcon(arquivo.getAbsolutePath()).getImage();
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int larguraMatrizPx = mapa.getLargura() * TAMANHO_BLOCO;
        int alturaMatrizPx = mapa.getAltura() * TAMANHO_BLOCO;

        g.setColor(Color.BLACK);
        g.fillRect(OFFSET_X, OFFSET_Y, larguraMatrizPx, alturaMatrizPx);

        Bloco[][] grade = mapa.getGrade();
        for (int x = 0; x < mapa.getLargura(); x++) {
            for (int y = 0; y < mapa.getAltura(); y++) {
                if (grade[x][y] != null && !(grade[x][y] instanceof BlocoArvore)) {
                    int posX = (x * TAMANHO_BLOCO) + OFFSET_X;
                    int posY = (y * TAMANHO_BLOCO) + OFFSET_Y;

                    if (grade[x][y] instanceof BlocoTijolo) {
                        if (imgTijolo != null) g.drawImage(imgTijolo, posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO, this);
                        else { g.setColor(new Color(153, 51, 0)); g.fillRect(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO); }
                    } else if (grade[x][y] instanceof BlocoAco) {
                        if (imgMetal != null) g.drawImage(imgMetal, posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO, this);
                        else { g.setColor(Color.LIGHT_GRAY); g.fillRect(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO); }
                    } else if (grade[x][y] instanceof BlocoAgua) {
                        if (imgAgua != null) g.drawImage(imgAgua, posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO, this);
                        else { g.setColor(Color.CYAN); g.fillRect(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO); }
                    } else if (grade[x][y] instanceof Base) {
                        if (imgBase != null) g.drawImage(imgBase, posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO, this);
                        else { g.setColor(Color.BLUE); g.fillRect(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO); }
                        
                        if (((Base) grade[x][y]).isProtegida()) {
                            g.setColor(new Color(255, 255, 255, 120)); 
                            g.fillOval(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO);
                            g.setColor(Color.WHITE);
                            g.drawOval(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO);
                        }
                    }
                    g.setColor(Color.BLACK);
                    g.drawRect(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO);
                }
            }
        }
        
        for (PowerUp p : mapa.getPowerUps()) {
            int posX = (p.getPosX() * TAMANHO_BLOCO) + OFFSET_X;
            int posY = (p.getPosY() * TAMANHO_BLOCO) + OFFSET_Y;
            
            Image imgPoder = null;
            switch(p.getTipo()) {
                case ESTRELA: imgPoder = imgEstrela; break;
                case CAPACETE: imgPoder = imgCapacete; break;
                case PA: imgPoder = imgPa; break;
                case RELOGIO: imgPoder = imgRelogio; break;
                case BOMBA: imgPoder = imgBomba; break;
                case VIDA: imgPoder = imgVidaExtra; break;
            }
            
            if (imgPoder != null) {
                int tamPoder = TAMANHO_BLOCO - 10;
                g.drawImage(imgPoder, posX + 5, posY + 5, tamPoder, tamPoder, this);
            } else {
                switch(p.getTipo()) {
                    case ESTRELA: g.setColor(Color.YELLOW); break;
                    case CAPACETE: g.setColor(Color.LIGHT_GRAY); break;
                    case PA: g.setColor(new Color(139, 69, 19)); break;
                    case RELOGIO: g.setColor(Color.CYAN); break;
                    case BOMBA: g.setColor(Color.RED); break;
                    case VIDA: g.setColor(Color.GREEN); break;
                }
                g.fillOval(posX + 10, posY + 10, 20, 20); 
                g.setColor(Color.BLACK);
                g.drawOval(posX + 10, posY + 10, 20, 20);
            }
        }

        TanqueJogador jogador = mapa.getJogador();
        if (jogador != null && jogador.getVida() > 0) {
            int posX = (jogador.getPosX() * TAMANHO_BLOCO) + OFFSET_X;
            int posY = (jogador.getPosY() * TAMANHO_BLOCO) + OFFSET_Y;

            Image imgAtualJogador = imgJogCima;
            if (jogador.getDirecaoAtual() != null) {
                switch (jogador.getDirecaoAtual()) {
                    case CIMA:    imgAtualJogador = imgJogCima; break;
                    case BAIXO:   imgAtualJogador = imgJogBaixo; break;
                    case ESQUERDA: imgAtualJogador = imgJogEsq; break;
                    case DIREITA:  imgAtualJogador = imgJogDir; break;
                }
            }
            if (imgAtualJogador != null) g.drawImage(imgAtualJogador, posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO, this);
            else { g.setColor(Color.GREEN); g.fillRect(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO); }

            if (jogador.isInvulneravel()) {
                g.setColor(new Color(255, 255, 0, 100)); 
                g.fillOval(posX - 5, posY - 5, TAMANHO_BLOCO + 10, TAMANHO_BLOCO + 10);
                g.setColor(Color.YELLOW);
                g.drawOval(posX - 5, posY - 5, TAMANHO_BLOCO + 10, TAMANHO_BLOCO + 10);
            }
        }

        for (TanqueInimigo inimigo : mapa.getInimigos()) {
            int posX = (inimigo.getPosX() * TAMANHO_BLOCO) + OFFSET_X;
            int posY = (inimigo.getPosY() * TAMANHO_BLOCO) + OFFSET_Y;

            Image imgAtualInimigo = null;
            String tipo = inimigo.getTipo();

            if (tipo.equals("Normal")) {
                switch (inimigo.getDirecaoAtual()) {
                    case CIMA: imgAtualInimigo = imgBotNormalCima; break;
                    case BAIXO: imgAtualInimigo = imgBotNormalBaixo; break;
                    case ESQUERDA: imgAtualInimigo = imgBotNormalEsq; break;
                    case DIREITA: imgAtualInimigo = imgBotNormalDir; break;
                }
            } else if (tipo.equals("Rápido")) {
                switch (inimigo.getDirecaoAtual()) {
                    case CIMA: imgAtualInimigo = imgBotRapidoCima; break;
                    case BAIXO: imgAtualInimigo = imgBotRapidoBaixo; break;
                    case ESQUERDA: imgAtualInimigo = imgBotRapidoEsq; break;
                    case DIREITA: imgAtualInimigo = imgBotRapidoDir; break;
                }
            } else if (tipo.equals("Blindado")) {
                switch (inimigo.getDirecaoAtual()) {
                    case CIMA: imgAtualInimigo = imgBotForteCima; break;
                    case BAIXO: imgAtualInimigo = imgBotForteBaixo; break;
                    case ESQUERDA: imgAtualInimigo = imgBotForteEsq; break;
                    case DIREITA: imgAtualInimigo = imgBotForteDir; break;
                }
            } else if (tipo.equals("Metralhadora")) {
                switch (inimigo.getDirecaoAtual()) {
                    case CIMA: imgAtualInimigo = imgBotMetralhaCima; break;
                    case BAIXO: imgAtualInimigo = imgBotMetralhaBaixo; break;
                    case ESQUERDA: imgAtualInimigo = imgBotMetralhaEsq; break;
                    case DIREITA: imgAtualInimigo = imgBotMetralhaDir; break;
                }
            }

            if (imgAtualInimigo != null) g.drawImage(imgAtualInimigo, posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO, this);
            else { g.setColor(Color.RED); g.fillRect(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO); }
        }

        for (int x = 0; x < mapa.getLargura(); x++) {
            for (int y = 0; y < mapa.getAltura(); y++) {
                if (grade[x][y] instanceof BlocoArvore) {
                    int posX = (x * TAMANHO_BLOCO) + OFFSET_X;
                    int posY = (y * TAMANHO_BLOCO) + OFFSET_Y;
                    
                    if (imgArvore != null) {
                        boolean temTanqueAqui = false;
                        if (jogador != null && jogador.getPosX() == x && jogador.getPosY() == y) temTanqueAqui = true;
                        if (!temTanqueAqui) {
                            for (TanqueInimigo inimigo : mapa.getInimigos()) {
                                if (inimigo.getPosX() == x && inimigo.getPosY() == y) { temTanqueAqui = true; break; }
                            }
                        }

                        Graphics2D g2d = (Graphics2D) g;
                        Composite compositeOriginal = g2d.getComposite();
                        if (temTanqueAqui) g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                        
                        g.drawImage(imgArvore, posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO, this);
                        g2d.setComposite(compositeOriginal);
                    } else {
                        g.setColor(new Color(0, 100, 0, 150)); 
                        g.fillRect(posX, posY, TAMANHO_BLOCO, TAMANHO_BLOCO);
                    }
                }
            }
        }

        g.setColor(Color.YELLOW);
        for (Projetil p : mapa.getProjeteis()) {
            if (p.isAtivo()) {
                int tamTiro = TAMANHO_BLOCO / 4;
                int off = (TAMANHO_BLOCO - tamTiro) / 2;
                int posX = (p.getPosX() * TAMANHO_BLOCO) + off + OFFSET_X;
                int posY = (p.getPosY() * TAMANHO_BLOCO) + off + OFFSET_Y;
                g.fillRect(posX, posY, tamTiro, tamTiro);
            }
        }

        int inicioHudX = OFFSET_X + larguraMatrizPx + 20;

        g.setFont(new Font("Arial", Font.BOLD, 16));
        
        g.setColor(Color.CYAN);
        g.drawString("JOGADOR:", inicioHudX, OFFSET_Y + 20);
        g.setColor(Color.WHITE);
        g.drawString(mapa.getNomeJogador(), inicioHudX, OFFSET_Y + 40);
        g.setColor(Color.YELLOW);
        g.drawString("FASE " + mapa.getNivelAtual(), inicioHudX, OFFSET_Y + 80);
        g.drawString(mapa.getNomeFase(), inicioHudX, OFFSET_Y + 105);

        if (jogador != null) {
            g.setColor(Color.WHITE);
            g.drawString("PONTOS", inicioHudX, OFFSET_Y + 160);
            g.drawString(String.valueOf(jogador.getPontuacao()), inicioHudX, OFFSET_Y + 185);

            g.drawString("VIDAS", inicioHudX, OFFSET_Y + 240);
            if (imgVidas != null) g.drawImage(imgVidas, inicioHudX, OFFSET_Y + 255, 30, 30, this); 
            g.drawString("x " + jogador.getVida(), inicioHudX + 40, OFFSET_Y + 278);
            
            g.drawString("INIMIGOS", inicioHudX, OFFSET_Y + 330);
            if (imgTanqueContador != null) g.drawImage(imgTanqueContador, inicioHudX, OFFSET_Y + 345, 30, 30, this);
            g.drawString("x " + mapa.getInimigosRestantes(), inicioHudX + 40, OFFSET_Y + 368);
            
            int hudPowerY = OFFSET_Y + 420;
            g.setColor(Color.YELLOW);
            g.drawString("P. ATIVOS:", inicioHudX, hudPowerY);
            hudPowerY += 15;

            if (jogador.isInvulneravel() && imgCapacete != null) { g.drawImage(imgCapacete, inicioHudX, hudPowerY, 30, 30, this); hudPowerY += 35; }
            if (jogador.isTiroEstrela() && imgEstrela != null) { g.drawImage(imgEstrela, inicioHudX, hudPowerY, 30, 30, this); hudPowerY += 35; }
            if (mapa.isInimigosCongelados() && imgRelogio != null) { g.drawImage(imgRelogio, inicioHudX, hudPowerY, 30, 30, this); hudPowerY += 35; }
            if (mapa.isBaseProtegida() && imgPa != null) { g.drawImage(imgPa, inicioHudX, hudPowerY, 30, 30, this); hudPowerY += 35; }
        }

        if (mapa.isGameOver()) {
            if (imagemGameOver != null) g.drawImage(imagemGameOver, OFFSET_X, OFFSET_Y, larguraMatrizPx, alturaMatrizPx, this);
            else { g.setColor(Color.RED); g.setFont(new Font("Arial", Font.BOLD, 50)); g.drawString("GAME OVER", OFFSET_X + 100, OFFSET_Y + 260); }
            
            g.setColor(new Color(0, 0, 0, 180)); 
            g.fillRect(OFFSET_X + 70, OFFSET_Y + 345, 380, 90);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Pressione [ENTER] - Voltar ao Menu", OFFSET_X + 85, OFFSET_Y + 380);
            
            g.setColor(Color.WHITE);
            g.drawString("Pressione [ESC] - Sair do Jogo", OFFSET_X + 110, OFFSET_Y + 415);
            
        } else if (mapa.isVitoria()) {
            g.setColor(Color.YELLOW); 
            g.setFont(new Font("Arial", Font.BOLD, 45)); 
            g.drawString("FASE " + mapa.getNivelAtual() + " CONCLUÍDA!", OFFSET_X + 25, OFFSET_Y + 260);
            
            g.setColor(new Color(0, 0, 0, 180)); 
            g.fillRect(OFFSET_X + 50, OFFSET_Y + 345, 420, 90);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Pressione [ENTER] - Próxima Fase (+30%)", OFFSET_X + 60, OFFSET_Y + 380);
            
            g.setColor(Color.WHITE);
            g.drawString("Pressione [ESC] - Sair do Jogo", OFFSET_X + 110, OFFSET_Y + 415);
        }
    }
}