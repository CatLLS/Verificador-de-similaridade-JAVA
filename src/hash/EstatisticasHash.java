package hash;

/**
 * Estatísticas de distribuição das chaves nos buckets (para a Análise Experimental).
 * Classe já pronta: recebe o comprimento de cada bucket e calcula as métricas.
 *
 * Usada por: Cauê (Pessoa 1) no gráfico de colisões por bucket das duas funções hash.
 */
public class EstatisticasHash {

    private final int[] tamanhoPorBucket;
    private final int totalChaves;

    public EstatisticasHash(int[] tamanhoPorBucket) {
        this.tamanhoPorBucket = tamanhoPorBucket.clone();
        int t = 0;
        for (int n : tamanhoPorBucket) t += n;
        this.totalChaves = t;
    }

    /** Soma, bucket a bucket, as estatísticas de várias tabelas de mesma capacidade. */
    public static EstatisticasHash combinar(EstatisticasHash[] lista) {
        if (lista.length == 0) return new EstatisticasHash(new int[0]);
        int cap = lista[0].capacidade();
        int[] soma = new int[cap];
        for (EstatisticasHash e : lista) {
            if (e.capacidade() != cap) throw new IllegalArgumentException("capacidades diferentes");
            for (int i = 0; i < cap; i++) soma[i] += e.tamanhoPorBucket[i];
        }
        return new EstatisticasHash(soma);
    }

    public int[] getTamanhoPorBucket() { return tamanhoPorBucket.clone(); }

    public int capacidade() { return tamanhoPorBucket.length; }

    public int totalChaves() { return totalChaves; }

    public int bucketsVazios() {
        int v = 0;
        for (int n : tamanhoPorBucket) if (n == 0) v++;
        return v;
    }

    public int bucketsOcupados() { return capacidade() - bucketsVazios(); }

    /** Chaves que caíram em um bucket já ocupado: soma de max(0, n - 1). */
    public int colisoes() {
        int c = 0;
        for (int n : tamanhoPorBucket) if (n > 1) c += n - 1;
        return c;
    }

    public int maiorCadeia() {
        int m = 0;
        for (int n : tamanhoPorBucket) m = Math.max(m, n);
        return m;
    }

    public double fatorCarga() {
        return capacidade() == 0 ? 0 : (double) totalChaves / capacidade();
    }

    public double mediaCadeiaOcupados() {
        int oc = bucketsOcupados();
        return oc == 0 ? 0 : (double) totalChaves / oc;
    }

    /** Linha resumida para o terminal (modo --stats). */
    public String resumo() {
        return String.format(java.util.Locale.US,
                "chaves=%d capacidade=%d fatorCarga=%.3f vazios=%d colisoes=%d maiorCadeia=%d mediaCadeia=%.2f",
                totalChaves, capacidade(), fatorCarga(), bucketsVazios(), colisoes(), maiorCadeia(),
                mediaCadeiaOcupados());
    }

    /** CSV "bucket,tamanho" para gerar gráficos (planilha, Python, etc.). */
    public String histogramaCsv() {
        StringBuilder sb = new StringBuilder("bucket,tamanho\n");
        for (int i = 0; i < tamanhoPorBucket.length; i++) {
            sb.append(i).append(',').append(tamanhoPorBucket[i]).append('\n');
        }
        return sb.toString();
    }
}
