package jogo;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList; 

public class Mapa {
    private int largura;
    private int altura;
    private Bloco[][] grade; 
    
    private String nomeFase;
    private String nomeJogador;
    private String dificuldade;
    private int numeroMapa; 
    private int nivelAtual = 1;
    
    private TanqueJogador jogador;
    private List<TanqueInimigo> inimigos;
    private List<Projetil> projeteis;
    private List<String> filaInimigos; 
    
    private List<PowerUp> powerUps;
    private boolean inimigosCongelados = false;
    private long fimCongelamento = 0;
    
    private boolean gameOver = false;
    private boolean vitoria = false; 
    private boolean rankingSalvo = false; 
    
    private volatile boolean pausado = false;
    private volatile boolean spawnThreadRunning = false;
    
    private String tipoTanqueJogador;

    public Mapa(int largura, int altura, String tipoTanqueJogador, String nomeJogador, String dificuldade, int mapaEscolhido) {
        this.largura = largura;
        this.altura = altura;
        this.tipoTanqueJogador = tipoTanqueJogador;
        this.nomeJogador = nomeJogador;
        this.dificuldade = dificuldade;
        this.grade = new Bloco[largura][altura]; 
        
        this.inimigos = new CopyOnWriteArrayList<>();
        this.projeteis = new CopyOnWriteArrayList<>();
        this.filaInimigos = new CopyOnWriteArrayList<>();
        this.powerUps = new CopyOnWriteArrayList<>(); 
        
        if (mapaEscolhido == 0) {
            mapaEscolhido = new Random().nextInt(3) + 1; 
        }
        this.numeroMapa = mapaEscolhido;
        this.nomeFase = "Mapa " + this.numeroMapa + " - " + obterNomeDoMapa(this.numeroMapa) + " (" + dificuldade + ")";
        
        inicializarMapa(this.numeroMapa);
        prepararFilaInimigos();
        iniciarThreadSpawn();
        iniciarThreadPowerUps();
        
        if (this.jogador != null) {
            new Thread(this.jogador).start();
        }
    }

    private String obterNomeDoMapa(int numero) {
        if (numero == 1) return "Clássico";
        if (numero == 2) return "Floresta";
        if (numero == 3) return "Fortaleza Aquática";
        return "Desconhecido";
    }

    public String getNomeFase() { return nomeFase; }
    public String getNomeJogador() { return nomeJogador; }
    public String getDificuldade() { return dificuldade; }
    public int getInimigosRestantes() { return inimigos.size() + filaInimigos.size(); }
    public boolean isPausado() { return pausado; }
    public void setPausado(boolean pausado) { this.pausado = pausado; }
    public int getNivelAtual() { return nivelAtual; } 
    
    public List<PowerUp> getPowerUps() { return powerUps; }
    public boolean isInimigosCongelados() { return inimigosCongelados; }
    
    public void avancarFase() {
        this.nivelAtual++;
        this.vitoria = false; 
        
        for (Projetil p : projeteis) p.destruir();
        for (TanqueInimigo ti : inimigos) ti.destruir();
        
        projeteis.clear();
        inimigos.clear();
        powerUps.clear();
        inimigosCongelados = false;

        this.numeroMapa = new Random().nextInt(3) + 1;
        this.nomeFase = "Mapa " + this.numeroMapa + " - " + obterNomeDoMapa(this.numeroMapa) + " (" + dificuldade + ")";
        
        this.grade = new Bloco[largura][altura];
        
        inicializarMapa(this.numeroMapa);
        prepararFilaInimigos();
    }

    private void iniciarThreadPowerUps() {
        new Thread(() -> {
            while (!gameOver) {
                if (!pausado && !vitoria && jogador != null && jogador.getVida() > 0) {
                    List<PowerUp> remover = new ArrayList<>();
                    
                    for (PowerUp p : powerUps) {
                        if (p.expirou()) {
                            remover.add(p);
                            continue;
                        }
                        if (p.getPosX() == jogador.getPosX() && p.getPosY() == jogador.getPosY()) {
                            aplicarPowerUp(p);
                            remover.add(p);
                        }
                    }
                    powerUps.removeAll(remover);
                    
                    if (inimigosCongelados && System.currentTimeMillis() > fimCongelamento) {
                        inimigosCongelados = false;
                    }
                }
                try { Thread.sleep(100); } catch (Exception e) {}
            }
        }).start();
    }

    private void aplicarPowerUp(PowerUp p) {
        switch (p.getTipo()) {
            case ESTRELA: jogador.ativarTiroEstrela(15000); break; 
            case CAPACETE: jogador.ativarInvulnerabilidade(8000); break;
            case VIDA: jogador.adicionarVida(); break;
            case RELOGIO:
                inimigosCongelados = true;
                fimCongelamento = System.currentTimeMillis() + 8000; 
                break;
            case BOMBA:
                for (TanqueInimigo inimigo : inimigos) {
                    inimigo.destruir();
                    if (jogador != null) jogador.adicionarPontos(100);
                }
                inimigos.clear();
                if (filaInimigos.isEmpty()) {
                    this.vitoria = true;
                }
                break;
            case PA:
                for (int x = 0; x < largura; x++) {
                    for (int y = 0; y < altura; y++) {
                        if (grade[x][y] instanceof Base) {
                            Base b = (Base) grade[x][y];
                            b.setProtegida(true);
                            new Thread(() -> {
                                try { Thread.sleep(15000); } catch (Exception e) {}
                                b.setProtegida(false);
                            }).start();
                        }
                    }
                }
                break;
        }
    }

    public void reiniciarFase() {
        for (Projetil p : projeteis) p.destruir();
        for (TanqueInimigo ti : inimigos) ti.destruir();
        if (jogador != null) jogador.destruir();

        projeteis.clear();
        inimigos.clear();
        powerUps.clear();
        inimigosCongelados = false;

        gameOver = false;
        vitoria = false;
        pausado = false;

        this.grade = new Bloco[largura][altura];
        this.jogador = null; 
        inicializarMapa(this.numeroMapa);

        if (jogador != null) {
            new Thread(jogador).start();
        }

        prepararFilaInimigos();
    }

    public void encerrarJogo() {
        spawnThreadRunning = false;
        if (!rankingSalvo && jogador != null) {
            Ranking.salvarPontuacao(nomeJogador, jogador.getPontuacao());
            rankingSalvo = true;
            System.out.println("Pontuação salva no Ranking!");
        }
        for (Projetil p : projeteis) p.destruir();
        for (TanqueInimigo ti : inimigos) ti.destruir();
        if (jogador != null) jogador.destruir();
    }

    private void inicializarMapa(int numeroMapa) {
        int basePosX = largura / 2;
        int basePosY = altura - 1;
        
        grade[basePosX][basePosY] = new Base(basePosX, basePosY);
        grade[basePosX - 1][basePosY] = new BlocoTijolo(basePosX - 1, basePosY);
        grade[basePosX + 1][basePosY] = new BlocoTijolo(basePosX + 1, basePosY);
        grade[basePosX - 1][basePosY - 1] = new BlocoTijolo(basePosX - 1, basePosY - 1);
        grade[basePosX][basePosY - 1] = new BlocoTijolo(basePosX, basePosY - 1);
        grade[basePosX + 1][basePosY - 1] = new BlocoTijolo(basePosX + 1, basePosY - 1);

        if (numeroMapa == 1) {
            for (int y = 1; y <= 4; y++) { grade[1][y] = new BlocoTijolo(1, y); grade[3][y] = new BlocoTijolo(3, y); grade[9][y] = new BlocoTijolo(9, y); grade[11][y] = new BlocoTijolo(11, y); }
            for (int y = 1; y <= 3; y++) { grade[1][y] = new BlocoTijolo(1, y); grade[3][y] = new BlocoTijolo(3, y); grade[9][y] = new BlocoTijolo(9, y); }
            for (int x = 1; x <= 2; x++) { grade[1+x][7] = new BlocoTijolo(1+x, 7); grade[8+x][7] = new BlocoTijolo(8+x, 7); }
            for (int x = 1; x <= 3; x++) { grade[4+x][8] = new BlocoTijolo(4+x, 8); }
            for (int y = 1; y <= 3; y++) { grade[1][8+y] = new BlocoTijolo(1, 8+y); grade[3][8+y] = new BlocoTijolo(3, 8+y); grade[11][8+y] = new BlocoTijolo(11, 8+y); grade[9][8+y] = new BlocoTijolo(9, 8+y); }
            for (int x : new int[]{5, 7}) { grade[x][1] = new BlocoTijolo(x, 1); grade[x][2] = new BlocoTijolo(x, 2); grade[x][3] = new BlocoTijolo(x, 3); }
            grade[0][7] = new BlocoAco(0, 7); grade[12][7] = new BlocoAco(12, 7); grade[6][2] = new BlocoAco(6, 2); 
            grade[5][6] = new BlocoTijolo(5, 6); grade[7][6] = new BlocoTijolo(7, 6);
        } else if (numeroMapa == 2) {
            for (int y = 1; y <= 5; y++) { 
                grade[2][y] = new BlocoArvore(2, y);
                grade[3][y] = new BlocoArvore(3, y);
                grade[9][6+y] = new BlocoArvore(9, 6+y);
                grade[10][6+y] = new BlocoArvore(10, 6+y); }
            for (int y = 1; y <= 6; y++) { 
                grade[1][y] = new BlocoArvore(1, y);
                grade[11][5+y] = new BlocoArvore(11, 5+y); }
            for (int y = 1; y <= 3; y++) { 
                grade[0][2+y] = new BlocoArvore(0, 2+y);
                grade[2][7+y] = new BlocoAco(2, 7+y);
                grade[12][6+y] = new BlocoArvore(12, 6+y);
                grade[1][8+y] = new BlocoArvore(1, 8+y);
                 grade[0][8+y] = new BlocoTijolo(0, 8+y); }
            for (int y = 0; y <= 1; y++) { grade[5][y] = new BlocoTijolo(5, y);
                grade[8][4+y] = new BlocoTijolo(8, 4+y); }
            for (int x = 0; x <= 2; x++) { 
                grade[5+x][7] = new BlocoAco(5+x,7);
                grade[10+x][2] = new BlocoAco(10+x, 2);
                grade[9+x][4] = new BlocoTijolo(9+x, 4);
                grade[x][12] = new BlocoTijolo(x, 12);
                grade[2+x][11] = new BlocoArvore(2+x, 11);  }
            for (int x = 0; x <= 3; x++) {
                grade[4+x][8] = new BlocoTijolo(4+x,8); }
 
            grade[0][2] = new BlocoAco(0, 2); 
            grade[10][12] = new BlocoTijolo(10, 12); 
        
        } else if (numeroMapa == 3) { 

            for (int x = 0; x <= 3; x++) { 
                grade[1+x][3] = new BlocoAgua(1+x,3);
                grade[8+x][3] = new BlocoAgua(8+x,3); }
            for (int x = 0; x <= 6; x++) { 
                grade[3+x][8] = new BlocoAgua(3+x,8); }

            for (int x = 0; x <= 2; x++) { 
                grade[5+x][4] = new BlocoArvore(5+x,4);
                grade[5+x][6] = new BlocoArvore(5+x,6);}
            
            for (int x = 0; x <= 4; x++){
                grade[4+x][5] = new BlocoArvore(4+x,5);}

            for (int x = 0; x <= 2; x++) { 
                grade[1+x][6] = new BlocoTijolo(1+x, 6);
                grade[9+x][6] = new BlocoTijolo(9+x,6); }   
             for (int y = 0; y <= 3; y++) {
                grade[12][6+y] = new BlocoTijolo(12, 6+y);
                grade[0][6+y] = new BlocoTijolo(0, 6+y);}
            grade[0][6] = new BlocoAco(0, 6);
            grade[12][6] = new BlocoAco(12, 6);  
            grade[6][3] = new BlocoArvore(3, 7);   
            grade[6][7] = new BlocoArvore(7, 7);
            grade[6][5] = new BlocoAco(7, 7);
            grade[6][1] = new BlocoAco(6, 1);
            grade[5][1] = new BlocoTijolo(5, 1);
            grade[7][1] = new BlocoTijolo(7, 1);
        }

        if (this.jogador == null) {
            this.jogador = new TanqueJogador(4, altura - 1, tipoTanqueJogador);
        } else {
            this.jogador.setPosX(4);
            this.jogador.setPosY(altura - 1);
            this.jogador.setDirecaoAtual(Direcao.CIMA);
        }
    }

    private TanqueInimigo criarInimigo(String tipo, int x, int y) {
        switch (tipo) {
            case "Rápido": return new InimigoRapido(x, y);
            case "Blindado": return new InimigoBlindado(x, y);
            case "Metralhadora": return new InimigoArmado(x, y);
            default: return new InimigoNormal(x, y);
        }
    }

    private void prepararFilaInimigos() {
        filaInimigos.clear();
        List<String> lista = new ArrayList<>();
        
        double fatorCrescimento = Math.pow(1.3, nivelAtual - 1); 
        
        int nNormal = 0, nBlindado = 0, nRapido = 0, nMetralha = 0;
        
        if (dificuldade.equals("Fácil")) {
            nNormal = (int)(7 * fatorCrescimento);
            nBlindado = (int)(3 * fatorCrescimento); 
        } 
        else if (dificuldade.equals("Média")) {
            nNormal = (int)(8 * fatorCrescimento);
            nBlindado = (int)(3 * fatorCrescimento);
            nRapido = (int)(4 * fatorCrescimento);
        } 
        else if (dificuldade.equals("Difícil")) {
            nNormal = (int)(7 * fatorCrescimento);
            nBlindado = (int)(7 * fatorCrescimento);
            nRapido = (int)(7 * fatorCrescimento);
            nMetralha = (int)(4 * fatorCrescimento);
        }
        
        for(int i=0; i<nNormal; i++) lista.add("Normal");
        for(int i=0; i<nBlindado; i++) lista.add("Blindado");
        for(int i=0; i<nRapido; i++) lista.add("Rápido");
        for(int i=0; i<nMetralha; i++) lista.add("Metralhadora");
        
        Collections.shuffle(lista);
        this.filaInimigos.addAll(lista);

        int[][] pontosIniciais = {{0,0}, {largura/2, 0}, {largura-1, 0}};
        for (int[] p : pontosIniciais) {
            if (!filaInimigos.isEmpty() && isAreaLivreParaSpawn(p[0], p[1])) {
                String tipo = filaInimigos.remove(0);
                TanqueInimigo novo = criarInimigo(tipo, p[0], p[1]);
                novo.setMapa(this);
                inimigos.add(novo);
                new Thread(novo).start();
            }
        }
    }

    private void iniciarThreadSpawn() {
        if (spawnThreadRunning) return; 
        spawnThreadRunning = true;
        
        new Thread(() -> {
            while (spawnThreadRunning) {
                try { Thread.sleep(3000); } catch (Exception e) {} 
                
                if (!pausado && !gameOver && !vitoria) {
                    tentarSpawnInimigo();
                }
            }
        }).start();
    }

    private void tentarSpawnInimigo() {
        if (inimigos.size() < 4 && !filaInimigos.isEmpty()) {
            List<int[]> pontosSpawn = new ArrayList<>();
            pontosSpawn.add(new int[]{0, 0});
            pontosSpawn.add(new int[]{largura / 2, 0});
            pontosSpawn.add(new int[]{largura - 1, 0});
            
            Collections.shuffle(pontosSpawn);
            
            for (int[] p : pontosSpawn) {
                if (isAreaLivreParaSpawn(p[0], p[1])) {
                    String tipo = filaInimigos.remove(0);
                    TanqueInimigo novo = criarInimigo(tipo, p[0], p[1]);
                    novo.setMapa(this);
                    inimigos.add(novo);
                    new Thread(novo).start();
                    break; 
                }
            }
        }
    }

    private boolean isAreaLivreParaSpawn(int x, int y) {
        return podeMover(x, y); 
    }

    public boolean podeMover(int x, int y) {
        if (x < 0 || x >= largura || y < 0 || y >= altura) return false;
        
        if (grade[x][y] != null && !grade[x][y].isTransponivel()) return false;
        
        if (jogador != null && jogador.getVida() > 0 && jogador.getPosX() == x && jogador.getPosY() == y) return false;

        for (TanqueInimigo inimigo : inimigos) {
            if (inimigo.getPosX() == x && inimigo.getPosY() == y) return false; 
        }
        return true;
    }

    public void adicionarProjetil(Projetil p) {
        p.setMapa(this);
        projeteis.add(p);
        new Thread(p).start();
    }

    public Bloco getBloco(int x, int y) {
        if (x >= 0 && x < largura && y >= 0 && y < altura) return grade[x][y];
        return null;
    }

    public Bloco[][] getGrade() { return grade; }
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
    public TanqueJogador getJogador() { return jogador; }
    public List<TanqueInimigo> getInimigos() { return inimigos; }
    public List<Projetil> getProjeteis() { return projeteis; }
    public boolean isGameOver() { return gameOver; }
    public boolean isVitoria() { return vitoria; }

    public void processarColisaoTiro(Projetil tiro) {
        int tx = tiro.getPosX();
        int ty = tiro.getPosY();

        if (tx < 0 || tx >= largura || ty < 0 || ty >= altura) {
            tiro.destruir(); return;
        }

        if (jogador != null && tx == jogador.getPosX() && ty == jogador.getPosY()) {
            if (tiro.isTiroInimigo() == false) return; 
            
            tiro.destruir();
            jogador.receberDano(1);
            if (jogador.getVida() <= 0) {
                jogador.destruir(); 
                this.gameOver = true;
                encerrarJogo(); 
            }
            return;
        }

        for (TanqueInimigo inimigo : inimigos) {
            if (tx == inimigo.getPosX() && ty == inimigo.getPosY()) {
                if (tiro.isTiroInimigo()) continue; 

                tiro.destruir();
                inimigo.receberDano(tiro.getForcaTiro());
                
                if (inimigo.getVida() <= 0) {
                    inimigo.destruir(); 
                    inimigos.remove(inimigo); 
                    if (jogador != null) jogador.adicionarPontos(100);
                    
                    if (new Random().nextInt(100) < 20) {
                        powerUps.add(new PowerUp(tx, ty));
                    }
                    
                    if (inimigos.isEmpty() && filaInimigos.isEmpty()) {
                        this.vitoria = true; 
                    }
                }
                return;
            }
        }

        if (grade[tx][ty] != null) {
            Bloco bloco = grade[tx][ty];
            bloco.interagirComTiro(tiro); 

            if (bloco instanceof BlocoTijolo && ((BlocoTijolo) bloco).isDestruido()) {
                grade[tx][ty] = null;
                if (new Random().nextInt(100) < 15) {
                    powerUps.add(new PowerUp(tx, ty));
                }
            } else if (bloco instanceof Base && ((Base) bloco).isDestruida()) {
                grade[tx][ty] = null; 
                this.gameOver = true;
                encerrarJogo(); 
            } else if (bloco instanceof BlocoAco && ((BlocoAco) bloco).isDestruido()) {
                grade[tx][ty] = null; 
            }
        }
    }

    public boolean isBaseProtegida() {
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                if (grade[x][y] instanceof Base) {
                    return ((Base) grade[x][y]).isProtegida();
                }
            }
        }
        return false;
    }
}