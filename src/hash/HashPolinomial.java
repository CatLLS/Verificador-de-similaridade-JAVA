package hash;

/**
 * Função de dispersão polinomial (rolling hash / regra de Horner).
 *
 * Responsável: Cauê (Pessoa 1)
 *
 * Ideia: h(s) = (s[0]*B^(n-1) + s[1]*B^(n-2) + ... + s[n-1]) mod m,
 * calculado com Horner: h = (h * B + c) mod m, com B primo pequeno (31, 37...).
 */
public class HashPolinomial implements FuncaoHash {

    /** Base do polinômio. Testar outras (31, 37, 53) e comentar no relatório. */
    public static final int BASE = 31;

    @Override
    public int indice(String chave, int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("capacidade deve ser > 0");
        }
        if (chave == null || chave.isEmpty()) {
            return 0;
        }
        long h = 0;
        for (int i = 0; i < chave.length(); i++) {
            h = (h * BASE + chave.charAt(i)) % capacidade;
        }
        if (h < 0) {
            h += capacidade;
        }
        return (int) h;
    }

    @Override
    public String nome() {
        return "hashPolinomial";
    }
}
