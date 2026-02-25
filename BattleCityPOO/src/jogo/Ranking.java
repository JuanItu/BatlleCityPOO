package jogo;

import java.io.*;
import java.util.*;

public class Ranking {
    private static final String ARQUIVO = "ranking.txt";

    public static void salvarPontuacao(String nome, int pontos) {
        if (nome == null || nome.trim().isEmpty()) {
            nome = "Anônimo";
        }
        
        List<String> linhas = lerRanking();
        linhas.add(nome + " - " + pontos + " pts");
        linhas.sort((a, b) -> {
            try {
                int p1 = Integer.parseInt(a.split(" - ")[1].replace(" pts", ""));
                int p2 = Integer.parseInt(b.split(" - ")[1].replace(" pts", ""));
                return Integer.compare(p2, p1);
            } catch (Exception e) {
                return 0;
            }
        });

        if (linhas.size() > 10) {
            linhas = linhas.subList(0, 10);
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(ARQUIVO))) {
            for (String linha : linhas) {
                out.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o ranking: " + e.getMessage());
        }
    }

    public static List<String> lerRanking() {
        List<String> linhas = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return linhas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o ranking: " + e.getMessage());
        }
        return linhas;
    }
}