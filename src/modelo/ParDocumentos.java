package modelo;

import java.util.Locale;

/** Par de documentos e sua similaridade. Guardado dentro dos nós da AVL. (Pronto.) */
public class ParDocumentos {

    private final String documentoA;
    private final String documentoB;
    private final double similaridade;

    public ParDocumentos(String documentoA, String documentoB, double similaridade) {
        this.documentoA = documentoA;
        this.documentoB = documentoB;
        this.similaridade = similaridade;
    }

    public String getDocumentoA() { return documentoA; }

    public String getDocumentoB() { return documentoB; }

    public double getSimilaridade() { return similaridade; }

    /** Formato da saída: "doc1.txt <-> doc2.txt = 0.82" (ponto decimal, 2 casas). */
    @Override
    public String toString() {
        return String.format(Locale.US, "%s <-> %s = %.2f", documentoA, documentoB, similaridade);
    }
}
