package hash;

/**
 * Tabela hash de frequências: palavra -> número de ocorrências.
 * Existe uma tabela por documento.
 *
 * Responsável: Cauê (Pessoa 1)
 *
 * Implementação oficial: {@link TabelaHashEncadeada} (encadeamento externo, feita à mão,
 * sem java.util.HashMap / HashSet / Hashtable).
 */
public interface TabelaHash {

    /** Soma 1 à frequência da palavra (insere com frequência 1 se ainda não existir). */
    void incrementar(String palavra);

    /** Frequência da palavra; 0 se não existir. */
    int getFrequencia(String palavra);

    boolean contem(String palavra);

    /** Todas as palavras distintas armazenadas (ordem não importa). Usado pelo cálculo do cosseno. */
    String[] getPalavras();

    /** Número de palavras distintas. */
    int tamanho();

    /** Número de buckets. */
    int capacidade();

    /** Nome da função de dispersão usada (vai para a saída do programa). */
    String nomeFuncaoHash();

    /** Estatísticas de distribuição (colisões por bucket), usadas no relatório. */
    EstatisticasHash estatisticas();
}
