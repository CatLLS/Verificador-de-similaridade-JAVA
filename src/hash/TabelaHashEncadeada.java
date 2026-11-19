package hash;

/**
 * Tabela hash com tratamento de colisões por ENCADEAMENTO EXTERNO.
 *
 * Responsável: Cauê (Pessoa 1)
 *
 * Cada bucket aponta para uma lista ligada simples de {@link No}. Como a lista
 * cresce dentro do próprio bucket, não é obrigatório redimensionar a tabela
 * (rehash). A capacidade fica fixa e é melhor usar um número primo.
 *
 * Proibido usar java.util.HashMap / HashSet / Hashtable aqui.
 */
public class TabelaHashEncadeada implements TabelaHash {

    /** Nó da lista ligada de um bucket. */
    private static class No {
        final String chave;
        int frequencia;
        No proximo;

        No(String chave, No proximo) {
            this.chave = chave;
            this.frequencia = 1;
            this.proximo = proximo;
        }
    }

    public static final int CAPACIDADE_PADRAO = 1009; // primo

    private final No[] buckets;
    private final FuncaoHash funcaoHash;
    private int tamanho; // palavras distintas

    public TabelaHashEncadeada(FuncaoHash funcaoHash) {
        this(funcaoHash, CAPACIDADE_PADRAO);
    }

    public TabelaHashEncadeada(FuncaoHash funcaoHash, int capacidade) {
        if (capacidade <= 0) throw new IllegalArgumentException("capacidade deve ser > 0");
        this.funcaoHash = funcaoHash;
        this.buckets = new No[capacidade];
        this.tamanho = 0;
    }

    @Override
    public void incrementar(String palavra) {
        if (palavra == null) {
            throw new IllegalArgumentException("palavra não pode ser nula");
        }
        int i = funcaoHash.indice(palavra, buckets.length);
        No atual = buckets[i];
        while (atual != null) {
            if (atual.chave.equals(palavra)) {
                atual.frequencia++;
                return;
            }
            atual = atual.proximo;
        }
        buckets[i] = new No(palavra, buckets[i]);
        tamanho++;
    }

    @Override
    public int getFrequencia(String palavra) {
        if (palavra == null) {
            return 0;
        }
        int i = funcaoHash.indice(palavra, buckets.length);
        No atual = buckets[i];
        while (atual != null) {
            if (atual.chave.equals(palavra)) {
                return atual.frequencia;
            }
            atual = atual.proximo;
        }
        return 0;
    }

    @Override
    public boolean contem(String palavra) {
        return getFrequencia(palavra) > 0;
    }

    @Override
    public String[] getPalavras() {
        String[] resultado = new String[tamanho];
        int idx = 0;
        for (int i = 0; i < buckets.length; i++) {
            No atual = buckets[i];
            while (atual != null) {
                resultado[idx++] = atual.chave;
                atual = atual.proximo;
            }
        }
        return resultado;
    }

    @Override
    public int tamanho() {
        return tamanho;
    }

    @Override
    public int capacidade() {
        return buckets.length;
    }

    @Override
    public String nomeFuncaoHash() {
        return funcaoHash.nome();
    }

    @Override
    public EstatisticasHash estatisticas() {
        int[] contagens = new int[buckets.length];
        for (int i = 0; i < buckets.length; i++) {
            int c = 0;
            No atual = buckets[i];
            while (atual != null) {
                c++;
                atual = atual.proximo;
            }
            contagens[i] = c;
        }
        return new EstatisticasHash(contagens);
    }
}
