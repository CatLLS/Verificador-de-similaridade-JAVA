package experimentos;

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
        // TODO [Catarina - P3]: para cada volume em VOLUMES, usar os N primeiros documentos
        //  do diretório (gerados com 'make gerar'), medir com medirMs(...) e gravar o CSV:
        //  volume,funcao_hash,estrutura,tempo_total_ms,tempo_insercao_ms,tempo_busca_ms,colisoes,rot_simples,rot_duplas
        throw new UnsupportedOperationException("TODO [Catarina - P3]: Benchmark.main");
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
