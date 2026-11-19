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
        // TODO [Catarina - P3]:
        //  - produto escalar: percorrer a.getPalavras() e somar a.getFrequencia(w) * b.getFrequencia(w)
        //    (dica: percorrer a tabela MENOR deixa o cálculo mais rápido)
        //  - normas: raiz da soma dos quadrados das frequências de cada tabela
        //  - se alguma norma for 0 (documento vazio depois da normalização) -> retornar 0.0
        //    (o doc11.txt de teste cobre esse caso)
        throw new UnsupportedOperationException("TODO [Catarina - P3]: Cosseno.calcular");
    }

    @Override
    public String nome() {
        return "Cosseno";
    }
}
