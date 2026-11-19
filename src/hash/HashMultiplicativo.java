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
        if (capacidade <= 0) {
            throw new IllegalArgumentException("capacidade deve ser > 0");
        }
        if (chave == null || chave.isEmpty()) {
            return 0;
        }
        int k = 0;
        for (int i = 0; i < chave.length(); i++) {
            k = k * 31 + chave.charAt(i);
        }
        long kPositivo = k & 0x7fffffffL;
        double prod = kPositivo * A;
        double frac = prod - Math.floor(prod);
        return (int) Math.floor(capacidade * frac);
    }

    @Override
    public String nome() {
        return "hashMultiplicativo";
    }
}
