package experimentos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import app.LeitorDocumentos;
import avl.ArvoreAVL;
import avl.IndiceSimilaridade;
import avl.ListaOrdenada;
import avl.RegistroRotacoes;
import hash.FuncaoHash;
import hash.HashMultiplicativo;
import hash.HashPolinomial;
import hash.TabelaHashEncadeada;
import modelo.Documento;
import modelo.ParDocumentos;
import similaridade.Cosseno;
import similaridade.MetricaSimilaridade;
import texto.Normalizador;
import texto.StopWords;

/**
 * Testes de desempenho para a seção 4 (Análise Experimental).
 *
 * Responsável: Catarina (Pessoa 3), com apoio de Cauê (hash) e Mateus (AVL vs lista)
 *
 * Uso (via Makefile):  make benchmark
 *   java -cp bin experimentos.Benchmark <diretorio_base> <saida.csv>
 *
 * O que medir (sugestão):
 *  1. Tempo total (leitura + hash + pares + AVL) para N = 10, 25, 50, 100, 200 documentos
 *     -> gráfico de tempo médio por volume de documentos
 *  2. Distribuição de colisões das DUAS funções hash no mesmo conjunto
 *     -> EstatisticasHash.histogramaCsv() / resumo()
 *  3. Tempo de inserção e de busca: ArvoreAVL vs ListaOrdenada
 *     (as duas implementam IndiceSimilaridade, então dá para trocar uma pela outra)
 *  4. Rotações por tipo durante a inserção (RegistroRotacoes)
 *
 * Boas práticas: aquecer a JVM (rodar algumas vezes antes de medir), repetir cada medida
 * (ex.: 10x) e usar a média; medir com System.nanoTime().
 */
public class Benchmark {

    public static final int[] VOLUMES = {10, 25, 50, 100, 200};
    public static final int REPETICOES = 10;
    public static final int AQUECIMENTO = 3;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Uso: java -cp bin experimentos.Benchmark <diretorio_documentos> <saida.csv>");
            System.exit(1);
        }
        Path diretorio = Path.of(args[0]);
        if (!Files.exists(diretorio) || !Files.isDirectory(diretorio)) {
            throw new IOException("diretório de documentos inválido: " + diretorio);
        }

        List<Path> arquivos;
        try (Stream<Path> stream = Files.list(diretorio)) {
            arquivos = stream
                    .filter(Files::isRegularFile)
                    .filter(arquivo -> arquivo.getFileName().toString().endsWith(".txt"))
                    .sorted(Comparator.comparing(arquivo -> arquivo.getFileName().toString()))
                    .collect(java.util.stream.Collectors.toList());
        }

        int maiorVolume = VOLUMES[VOLUMES.length - 1];
        if (arquivos.size() < maiorVolume) {
            throw new IOException("o diretório tem " + arquivos.size()
                    + " documentos; são necessários pelo menos " + maiorVolume);
        }

        List<String> linhas = new ArrayList<>();
        linhas.add("volume,funcao_hash,estrutura,tempo_total_ms,tempo_insercao_ms,"
                + "tempo_busca_ms,colisoes,rot_simples,rot_duplas");

        FuncaoHash[] funcoes = {new HashMultiplicativo(), new HashPolinomial()};
        for (int volume : VOLUMES) {
            List<Path> primeiros = arquivos.subList(0, volume);
            for (FuncaoHash funcao : funcoes) {
                Resultado avl = medirCenario(primeiros, funcao, true);
                linhas.add(avl.linha(volume, funcao.nome(), "AVL"));

                Resultado lista = medirCenario(primeiros, funcao, false);
                linhas.add(lista.linha(volume, funcao.nome(), "ListaOrdenada"));
            }
            System.out.println("Benchmark concluído para " + volume + " documentos");
        }

        Path saida = Path.of(args[1]);
        Path pai = saida.toAbsolutePath().getParent();
        if (pai != null) Files.createDirectories(pai);
        Files.writeString(saida, String.join("\n", linhas) + "\n", StandardCharsets.UTF_8);
        System.out.println("Resultados gravados em " + saida);
    }

    private static Resultado medirCenario(List<Path> arquivos, FuncaoHash funcao, boolean usarAVL)
            throws IOException {
        for (int i = 0; i < AQUECIMENTO; i++) executarCenario(arquivos, funcao, usarAVL);

        double total = 0.0;
        double insercao = 0.0;
        double busca = 0.0;
        Resultado ultimo = null;
        for (int i = 0; i < REPETICOES; i++) {
            long ini = System.nanoTime();
            ultimo = executarCenario(arquivos, funcao, usarAVL);
            total += (System.nanoTime() - ini) / 1e6;
            insercao += ultimo.tempoInsercaoMs;
            busca += ultimo.tempoBuscaMs;
        }
        ultimo.tempoTotalMs = total / REPETICOES;
        ultimo.tempoInsercaoMs = insercao / REPETICOES;
        ultimo.tempoBuscaMs = busca / REPETICOES;
        return ultimo;
    }

    private static Resultado executarCenario(List<Path> arquivos, FuncaoHash funcao, boolean usarAVL)
            throws IOException {
        long inicioTotal = System.nanoTime();
        LeitorDocumentos leitor = new LeitorDocumentos(
                new Normalizador(new StopWords()),
                funcao,
                TabelaHashEncadeada.CAPACIDADE_PADRAO);

        List<Documento> documentos = new ArrayList<>(arquivos.size());
        int colisoes = 0;
        for (Path arquivo : arquivos) {
            Documento documento = leitor.lerArquivo(arquivo);
            documentos.add(documento);
            colisoes += documento.getFrequencias().estatisticas().colisoes();
        }

        IndiceSimilaridade indice = usarAVL ? new ArvoreAVL() : new ListaOrdenada();
        MetricaSimilaridade cosseno = new Cosseno();
        RegistroRotacoes rotacoes = new RegistroRotacoes();
        long tempoInsercaoNano = 0;
        for (int i = 0; i < documentos.size(); i++) {
            for (int j = i + 1; j < documentos.size(); j++) {
                double similaridade = cosseno.calcular(
                        documentos.get(i).getFrequencias(),
                        documentos.get(j).getFrequencias());
                ParDocumentos par = new ParDocumentos(
                        documentos.get(i).getNome(),
                        documentos.get(j).getNome(),
                        similaridade);
                long ini = System.nanoTime();
                rotacoes.somar(indice.inserir(par));
                tempoInsercaoNano += System.nanoTime() - ini;
            }
        }

        long iniBusca = System.nanoTime();
        indice.topK(Math.min(10, indice.tamanho()));
        double tempoBusca = (System.nanoTime() - iniBusca) / 1e6;
        double tempoTotal = (System.nanoTime() - inicioTotal) / 1e6;

        return new Resultado(
                tempoTotal,
                tempoInsercaoNano / 1e6,
                tempoBusca,
                colisoes,
                rotacoes.getSimplesEsquerda() + rotacoes.getSimplesDireita(),
                rotacoes.getDuplaEsquerdaDireita() + rotacoes.getDuplaDireitaEsquerda());
    }

    private static final class Resultado {
        private double tempoTotalMs;
        private double tempoInsercaoMs;
        private double tempoBuscaMs;
        private final int colisoes;
        private final int rotacoesSimples;
        private final int rotacoesDuplas;

        private Resultado(double tempoTotalMs, double tempoInsercaoMs, double tempoBuscaMs,
                          int colisoes, int rotacoesSimples, int rotacoesDuplas) {
            this.tempoTotalMs = tempoTotalMs;
            this.tempoInsercaoMs = tempoInsercaoMs;
            this.tempoBuscaMs = tempoBuscaMs;
            this.colisoes = colisoes;
            this.rotacoesSimples = rotacoesSimples;
            this.rotacoesDuplas = rotacoesDuplas;
        }

        private String linha(int volume, String funcaoHash, String estrutura) {
            return String.format(Locale.US,
                    "%d,%s,%s,%.3f,%.3f,%.3f,%d,%d,%d",
                    volume, funcaoHash, estrutura,
                    tempoTotalMs, tempoInsercaoMs, tempoBuscaMs,
                    colisoes, rotacoesSimples, rotacoesDuplas);
        }
    }

    /** Média em milissegundos de 'repeticoes' execuções, depois do aquecimento. (Pronto.) */
    public static double medirMs(Runnable tarefa, int repeticoes) {
        for (int i = 0; i < AQUECIMENTO; i++) tarefa.run();
        long total = 0;
        for (int i = 0; i < repeticoes; i++) {
            long ini = System.nanoTime();
            tarefa.run();
            total += System.nanoTime() - ini;
        }
        return total / 1e6 / repeticoes;
    }
}
