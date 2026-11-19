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
        // TODO [Cauê - P1]: implementar com a regra de Horner,
        //  aplicando o "mod capacidade" a cada passo para evitar overflow.
        throw new UnsupportedOperationException("TODO [Cauê - P1]: HashPolinomial.indice");
    }

    @Override
    public String nome() {
        return "hashPolinomial";
    }
}
