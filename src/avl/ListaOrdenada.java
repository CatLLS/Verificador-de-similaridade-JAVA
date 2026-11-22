package avl;

import java.util.ArrayList;
import java.util.List;

import modelo.ParDocumentos;

/**
 * Estrutura de comparação (baseline): lista mantida ordenada por similaridade.
 */
public class ListaOrdenada implements IndiceSimilaridade {

    private final List<ParDocumentos> pares = new ArrayList<>();

    @Override
    public RegistroRotacoes inserir(ParDocumentos par) {
        int pos = upperBound(par.getSimilaridade());
        pares.add(pos, par);
        return new RegistroRotacoes();
    }

    @Override
    public List<ParDocumentos> buscar(double similaridade) {
        List<ParDocumentos> saida = new ArrayList<>();
        int inicio = lowerBound(similaridade);
        for (int i = inicio; i < pares.size(); i++) {
            if (ArvoreAVL.comparar(pares.get(i).getSimilaridade(), similaridade) != 0) {
                break;
            }
            saida.add(pares.get(i));
        }
        return saida;
    }

    @Override
    public List<ParDocumentos> acimaDoLimiar(double limiar) {
        List<ParDocumentos> saida = new ArrayList<>();
        int inicio = lowerBound(limiar);
        for (int i = pares.size() - 1; i >= inicio; i--) {
            saida.add(pares.get(i));
        }
        return saida;
    }

    @Override
    public List<ParDocumentos> topK(int k) {
        List<ParDocumentos> saida = new ArrayList<>();
        if (k <= 0) return saida;
        int limite = Math.max(0, pares.size() - k);
        for (int i = pares.size() - 1; i >= limite; i--) {
            saida.add(pares.get(i));
        }
        return saida;
    }

    @Override
    public List<ParDocumentos> menosSimilares(int n) {
        List<ParDocumentos> saida = new ArrayList<>();
        if (n <= 0) return saida;
        int limite = Math.min(n, pares.size());
        for (int i = 0; i < limite; i++) {
            saida.add(pares.get(i));
        }
        return saida;
    }

    @Override
    public int tamanho() {
        return pares.size();
    }

    /** Primeiro índice cujo elemento tem similaridade >= alvo. */
    private int lowerBound(double alvo) {
        int esq = 0;
        int dir = pares.size();
        while (esq < dir) {
            int meio = esq + (dir - esq) / 2;
            if (ArvoreAVL.comparar(pares.get(meio).getSimilaridade(), alvo) < 0) {
                esq = meio + 1;
            } else {
                dir = meio;
            }
        }
        return esq;
    }

    /** Primeiro índice cujo elemento tem similaridade > alvo (preserva ordem de inserção em empates). */
    private int upperBound(double alvo) {
        int esq = 0;
        int dir = pares.size();
        while (esq < dir) {
            int meio = esq + (dir - esq) / 2;
            if (ArvoreAVL.comparar(pares.get(meio).getSimilaridade(), alvo) <= 0) {
                esq = meio + 1;
            } else {
                dir = meio;
            }
        }
        return esq;
    }
}