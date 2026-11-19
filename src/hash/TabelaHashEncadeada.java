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
        // TODO [Cauê - P1]:
        //  1. i = funcaoHash.indice(palavra, buckets.length)
        //  2. Percorrer buckets[i]: se achar a chave, frequencia++ e retornar.
        //  3. Se não achar, inserir um novo No no início da lista e fazer tamanho++.
        throw new UnsupportedOperationException("TODO [Cauê - P1]: TabelaHashEncadeada.incrementar");
    }

    @Override
    public int getFrequencia(String palavra) {
        // TODO [Cauê - P1]: buscar no bucket; retornar 0 se não existir.
        throw new UnsupportedOperationException("TODO [Cauê - P1]: TabelaHashEncadeada.getFrequencia");
    }

    @Override
    public boolean contem(String palavra) {
        return getFrequencia(palavra) > 0;
    }

    @Override
    public String[] getPalavras() {
        // TODO [Cauê - P1]: percorrer todos os buckets e listas, preenchendo um array de tamanho 'tamanho'.
        throw new UnsupportedOperationException("TODO [Cauê - P1]: TabelaHashEncadeada.getPalavras");
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
        // TODO [Cauê - P1]: contar o comprimento da lista de cada bucket em um int[]
        //  e retornar new EstatisticasHash(contagens).
        throw new UnsupportedOperationException("TODO [Cauê - P1]: TabelaHashEncadeada.estatisticas");
    }
}
