package jogo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JanelaJogo extends JFrame {
    private Mapa mapa;
    private PainelJogo painel;

    public JanelaJogo(Mapa mapa) {
        this.mapa = mapa;
        setTitle("Battle City POO"); 
        setSize(730, 640); 
        setFocusable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setResizable(false); 
        setLocationRelativeTo(null); 
        
        painel = new PainelJogo(mapa);
        add(painel);

        Timer timer = new Timer(16, e -> {
            painel.repaint();
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int tecla = e.getKeyCode();

                if (mapa.isGameOver() || mapa.isVitoria()) {
                    if (tecla == KeyEvent.VK_ENTER) {
                        if (mapa.isVitoria()) {
                            mapa.avancarFase();
                        } else {
                            mapa.encerrarJogo();
                            JanelaJogo.this.dispose(); 
                            new MenuInicial().setVisible(true); 
                        }
                    } else if (tecla == KeyEvent.VK_ESCAPE) {
                        mapa.encerrarJogo(); 
                        System.exit(0); 
                    }
                    return; 
                }

                if (tecla == KeyEvent.VK_P) {
                    mapa.setPausado(!mapa.isPausado());
                    if (mapa.isPausado()) {
                        mostrarMenuPausa();
                    }
                    return;
                }
                
                if (!mapa.isPausado()) {
                    TanqueJogador jogador = mapa.getJogador();
                    if (jogador != null && jogador.getVida() > 0) {
                        Direcao novaDirecao = null;
                        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP) novaDirecao = Direcao.CIMA;
                        else if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN) novaDirecao = Direcao.BAIXO;
                        else if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT) novaDirecao = Direcao.ESQUERDA;
                        else if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT) novaDirecao = Direcao.DIREITA;

                        if (novaDirecao != null) {
                            jogador.setDirecaoAtual(novaDirecao);
                            int proxX = jogador.getPosX();
                            int proxY = jogador.getPosY();
                            
                            switch (novaDirecao) {
                                case CIMA:    proxY--; break;
                                case BAIXO:   proxY++; break;
                                case ESQUERDA: proxX--; break;
                                case DIREITA:  proxX++; break;
                            }

                            if (jogador.podeMoverAgora() && mapa.podeMover(proxX, proxY)) {
                                jogador.mover(novaDirecao);
                                jogador.registrarMovimento();
                            }
                        }

                        if (tecla == KeyEvent.VK_SPACE) {
                            if (jogador.podeAtirarAgora()) {
                                Projetil tiro = jogador.atirar();
                                mapa.adicionarProjetil(tiro);
                                jogador.registrarTiro();
                            }
                        }
                    }
                }
            }
        });
    }

    private void mostrarMenuPausa() {
        JDialog dialog = new JDialog(this, "Jogo Pausado", true);
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(4, 1, 10, 10));
        dialog.setUndecorated(true); 
        dialog.getContentPane().setBackground(new Color(50, 50, 50)); 

        JLabel lblPausa = new JLabel("PAUSADO", SwingConstants.CENTER);
        lblPausa.setForeground(Color.YELLOW);
        lblPausa.setFont(new Font("Arial", Font.BOLD, 24));

        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setBackground(Color.LIGHT_GRAY);
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 16));
        btnContinuar.setFocusPainted(false);
        btnContinuar.addActionListener(ev -> {
            mapa.setPausado(false);
            dialog.dispose(); 
        });

        JButton btnReiniciar = new JButton("Reiniciar Fase");
        btnReiniciar.setBackground(Color.LIGHT_GRAY);
        btnReiniciar.setFont(new Font("Arial", Font.BOLD, 16));
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.addActionListener(ev -> {
            dialog.dispose();
            mapa.reiniciarFase();
        });

        JButton btnNovo = new JButton("Desistir (Menu)");
        btnNovo.setBackground(Color.LIGHT_GRAY);
        btnNovo.setFont(new Font("Arial", Font.BOLD, 16));
        btnNovo.setFocusPainted(false);
        btnNovo.addActionListener(ev -> {
            dialog.dispose();
            mapa.encerrarJogo();
            this.dispose();      
            new MenuInicial().setVisible(true); 
        });

        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        dialog.add(lblPausa);
        dialog.add(btnContinuar);
        dialog.add(btnReiniciar);
        dialog.add(btnNovo);

        dialog.setVisible(true);
    }
}