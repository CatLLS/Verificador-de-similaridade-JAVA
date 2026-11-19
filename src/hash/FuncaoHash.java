package hash;

/**
 * Contrato de uma função de dispersão (hash).
 *
 * Responsável: Cauê (Pessoa 1)
 *
 * O projeto exige DUAS implementações distintas para comparação no relatório:
 *  - {@link HashMultiplicativo}
 *  - {@link HashPolinomial}
 */
public interface FuncaoHash {

    /**
     * Calcula o índice do bucket para a chave.
     *
     * @param chave       palavra já normalizada (minúscula, sem pontuação)
     * @param capacidade  número de buckets da tabela
     * @return índice no intervalo [0, capacidade)
     */
    int indice(String chave, int capacidade);

    /** Nome exibido na saída, ex.: "hashMultiplicativo". */
    String nome();
}
