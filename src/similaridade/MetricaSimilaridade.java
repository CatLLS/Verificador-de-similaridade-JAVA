package similaridade;

import hash.TabelaHash;

/** Contrato de uma métrica de similaridade entre dois documentos. */
public interface MetricaSimilaridade {

    /** @return valor em [0, 1]; 1 = vocabulários com a mesma distribuição. */
    double calcular(TabelaHash a, TabelaHash b);

    /** Nome exibido na saída, ex.: "Cosseno". */
    String nome();
}
