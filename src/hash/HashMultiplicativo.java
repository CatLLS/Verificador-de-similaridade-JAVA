package hash;

/**
 * Função de dispersão pelo método da multiplicação (Knuth).
 *
 * Responsável: Cauê (Pessoa 1)
 *
 * Ideia: converter a string em um inteiro k e calcular
 *     h(k) = floor(m * frac(k * A)),  com 0 < A < 1  (Knuth sugere A = (sqrt(5) - 1) / 2)
 */
public class HashMultiplicativo implements FuncaoHash {

    /** Constante de Knuth: (sqrt(5) - 1) / 2. */
    public static final double A = 0.6180339887498949;

    @Override
    public int indice(String chave, int capacidade) {
        // TODO [Cauê - P1]: implementar.
        //  1. Converter a string em um inteiro k (ex.: somar/combinar os códigos dos caracteres).
        //  2. Calcular a parte fracionária de k * A.
        //  3. Multiplicar por capacidade e truncar.
        //  Cuidado com k negativo (overflow): usar Math.abs ou (k & 0x7fffffff).
        //  Justificar a escolha no relatório (seção 2.1).
        throw new UnsupportedOperationException("TODO [Cauê - P1]: HashMultiplicativo.indice");
    }

    @Override
    public String nome() {
        return "hashMultiplicativo";
    }
}
