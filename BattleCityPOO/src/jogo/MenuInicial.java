package jogo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuInicial extends JFrame {
    
    private JPanel painelPrincipal;

    public MenuInicial() {
        setTitle("Battle City - Menu Principal");
        setSize(535, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        mostrarMenuPrincipal();
    }

    private void mostrarMenuPrincipal() {
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBackground(Color.BLACK);

        JLabel titulo = new JLabel("BATTLE CITY");
        titulo.setForeground(Color.YELLOW);
        titulo.setFont(new Font("Arial", Font.BOLD, 50));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 50)));
        painelPrincipal.add(titulo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 80)));

        painelPrincipal.add(criarBotaoMenu("1 - JOGAR", e -> mostrarConfiguracoesJogo()));
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        painelPrincipal.add(criarBotaoMenu("2 - RANKING", e -> mostrarRanking()));
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        painelPrincipal.add(criarBotaoMenu("3 - SAIR", e -> System.exit(0)));

        setContentPane(painelPrincipal);
        revalidate();
        repaint();
    }

    private JButton criarBotaoMenu(String texto, java.awt.event.ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setFont(new Font("Arial", Font.BOLD, 24));
        botao.setForeground(Color.WHITE);
        botao.setBackground(Color.DARK_GRAY);
        botao.setFocusPainted(false);
        botao.setMaximumSize(new Dimension(250, 50));
        botao.addActionListener(acao);
        return botao;
    }

    private void mostrarConfiguracoesJogo() {
        painelPrincipal.setVisible(false);

        JPanel painelConfig = new JPanel();
        painelConfig.setLayout(new GridLayout(5, 2, 10, 20));
        painelConfig.setBackground(Color.BLACK);
        painelConfig.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel lblNome = new JLabel("Nome do Jogador:");
        lblNome.setForeground(Color.WHITE);
        lblNome.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField txtNome = new JTextField("Jogador 1");
        txtNome.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel lblDificuldade = new JLabel("Dificuldade:");
        lblDificuldade.setForeground(Color.WHITE);
        lblDificuldade.setFont(new Font("Arial", Font.BOLD, 18));
        String[] dificuldades = {"Fácil", "Média", "Difícil"};
        JComboBox<String> comboDificuldade = new JComboBox<>(dificuldades);
        comboDificuldade.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel lblTanque = new JLabel("Tipo de Tanque:");
        lblTanque.setForeground(Color.WHITE);
        lblTanque.setFont(new Font("Arial", Font.BOLD, 18));
        String[] tanques = {"Normal", "Rápido", "Forte", "Metralhadora"};
        JComboBox<String> comboTanque = new JComboBox<>(tanques);
        comboTanque.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel lblMapa = new JLabel("Escolher Mapa:");
        lblMapa.setForeground(Color.WHITE);
        lblMapa.setFont(new Font("Arial", Font.BOLD, 18));
        
        String[] mapas = {"Aleatório", "1 - Clássico", "2 - Floresta", "3 - Fortaleza Aquática"};
        JComboBox<String> comboMapa = new JComboBox<>(mapas);
        comboMapa.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton btnIniciar = new JButton("COMEÇAR");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 20));
        btnIniciar.setBackground(Color.GREEN);
        btnIniciar.addActionListener(e -> iniciarJogo(
                txtNome.getText(), 
                (String) comboDificuldade.getSelectedItem(), 
                (String) comboTanque.getSelectedItem(), 
                comboMapa.getSelectedIndex() 
        ));

        JButton btnVoltar = new JButton("VOLTAR");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 20));
        btnVoltar.setBackground(Color.RED);
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.addActionListener(e -> {
            setContentPane(painelPrincipal);
            painelPrincipal.setVisible(true);
            revalidate();
            repaint();
        });

        painelConfig.add(lblNome); painelConfig.add(txtNome);
        painelConfig.add(lblDificuldade); painelConfig.add(comboDificuldade);
        painelConfig.add(lblTanque); painelConfig.add(comboTanque);
        painelConfig.add(lblMapa); painelConfig.add(comboMapa);
        painelConfig.add(btnVoltar); painelConfig.add(btnIniciar);

        setContentPane(painelConfig);
        revalidate();
        repaint();
    }

    private void mostrarRanking() {
        List<String> top10 = Ranking.lerRanking();
        StringBuilder sb = new StringBuilder("=== TOP 10 JOGADORES ===\n\n");
        
        if (top10.isEmpty()) {
            sb.append("Nenhuma pontuação registada ainda.");
        } else {
            for (int i = 0; i < top10.size(); i++) {
                sb.append((i + 1)).append("º - ").append(top10.get(i)).append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }

    private void iniciarJogo(String nomeJogador, String dificuldade, String tipoTanque, int mapaEscolhido) {
        this.dispose(); 
        
        Mapa mapa = new Mapa(13, 13, tipoTanque, nomeJogador, dificuldade, mapaEscolhido);
        
        JanelaJogo janela = new JanelaJogo(mapa);
        janela.setVisible(true);
    }
}