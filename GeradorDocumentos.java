package experimentos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Gera um corpus sintético grande a partir dos textos-base de documentos/,
 * para os testes de desempenho com volumes crescentes. (Ferramenta pronta.)
 *
 * Uso (via Makefile):  make gerar N=200
 *   java -cp bin experimentos.GeradorDocumentos <dir_base> <dir_saida> <N> [semente]
 *
 * Cada documento gerado junta frases sorteadas dos textos-base. A cada 10 documentos,
 * um é uma "cópia com ruído" de um anterior (frases embaralhadas e palavras removidas),
 * simulando plágio, para que existam pares bem similares no corpus.
 */
public class GeradorDocumentos {

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Uso: java -cp bin experimentos.GeradorDocumentos <dir_base> <dir_saida> <N> [semente]");
            System.exit(1);
        }
        Path base = Path.of(args[0]);
        Path saida = Path.of(args[1]);
        int n = Integer.parseInt(args[2]);
        long semente = args.length > 3 ? Long.parseLong(args[3]) : 42L;
        Random rnd = new Random(semente);

        List<String> frases = new ArrayList<>();
        try (Stream<Path> arquivos = Files.list(base)) {
            for (Path p : (Iterable<Path>) arquivos.filter(f -> f.toString().endsWith(".txt")).sorted()::iterator) {
                String texto = Files.readString(p, StandardCharsets.UTF_8);
                for (String f : texto.split("(?<=[.!?])\\s+")) {
                    if (f.trim().split("\\s+").length >= 5) frases.add(f.trim());
                }
            }
        }
        if (frases.isEmpty()) throw new IOException("nenhuma frase encontrada em " + base);

        Files.createDirectories(saida);
        List<String> gerados = new ArrayList<>();
        int largura = String.valueOf(n).length();
        for (int i = 0; i < n; i++) {
            String conteudo;
            if (i % 10 == 9 && !gerados.isEmpty()) {
                conteudo = copiaComRuido(gerados.get(rnd.nextInt(gerados.size())), rnd);
            } else {
                int qtd = 3 + rnd.nextInt(6); // 3 a 8 frases
                StringBuilder sb = new StringBuilder();
                for (int f = 0; f < qtd; f++) sb.append(frases.get(rnd.nextInt(frases.size()))).append(' ');
                conteudo = sb.toString().trim();
            }
            gerados.add(conteudo);
            String nome = String.format("gen%0" + largura + "d.txt", i + 1);
            Files.writeString(saida.resolve(nome), conteudo + "\n", StandardCharsets.UTF_8);
        }
        System.out.println(n + " documentos gerados em " + saida);
    }

    private static String copiaComRuido(String original, Random rnd) {
        String[] frases = original.split("(?<=[.!?])\\s+");
        for (int i = frases.length - 1; i > 0; i--) { // embaralha as frases
            int j = rnd.nextInt(i + 1);
            String t = frases[i]; frases[i] = frases[j]; frases[j] = t;
        }
        StringBuilder sb = new StringBuilder();
        for (String f : frases) {
            for (String palavra : f.split("\\s+")) {
                if (rnd.nextDouble() >= 0.1) sb.append(palavra).append(' '); // remove ~10% das palavras
            }
        }
        return sb.toString().trim();
    }
}
