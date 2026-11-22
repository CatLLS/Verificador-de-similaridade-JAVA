package similaridade;

import hash.TabelaHash;

/**
 * Similaridade do cosseno entre os vetores de frequência.
 *
 * Responsável: Catarina (Pessoa 3)
 *
 *            sum_w  fA(w) * fB(w)
 * cos(A,B) = -------------------------------
 *            sqrt(sum fA(w)^2) * sqrt(sum fB(w)^2)
 */
public class Cosseno implements MetricaSimilaridade {

    @Override
    public double calcular(TabelaHash a, TabelaHash b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("tabelas não podem ser nulas");
        }

        double somaQuadradosA = 0.0;
        double somaQuadradosB = 0.0;
        for (String palavra : a.getPalavras()) {
            int frequencia = a.getFrequencia(palavra);
            somaQuadradosA += (double) frequencia * frequencia;
        }
        for (String palavra : b.getPalavras()) {
            int frequencia = b.getFrequencia(palavra);
            somaQuadradosB += (double) frequencia * frequencia;
        }
        if (somaQuadradosA == 0.0 || somaQuadradosB == 0.0) {
            return 0.0;
        }

        double produtoEscalar = 0.0;
        TabelaHash menor = a.tamanho() <= b.tamanho() ? a : b;
        TabelaHash maior = menor == a ? b : a;
        for (String palavra : menor.getPalavras()) {
            int frequenciaMenor = menor.getFrequencia(palavra);
            int frequenciaMaior = maior.getFrequencia(palavra);
            produtoEscalar += (double) frequenciaMenor * frequenciaMaior;
        }

        double resultado = produtoEscalar
                / (Math.sqrt(somaQuadradosA) * Math.sqrt(somaQuadradosB));
        if (resultado > 1.0) return 1.0;
        if (resultado < 0.0) return 0.0;
        return resultado;
    }

    @Override
    public String nome() {
        return "Cosseno";
    }
}
