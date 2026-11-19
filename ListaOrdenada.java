package avl;

import java.util.ArrayList;
import java.util.List;

import modelo.ParDocumentos;

/**
 * Estrutura de comparação (baseline): lista mantida ordenada por similaridade.
 * Exigida pela Análise Experimental ("comparar a busca na AVL com uma estrutura mais simples").
 *
 * Responsável: Mateus (Pessoa 2)
 *
 * Complexidade esperada: inserção O(n) (deslocar elementos) e busca O(log n) (binária)
 * ou O(n) (linear). Escolham uma versão e comparem com a AVL.
 */
public class ListaOrdenada implements IndiceSimilaridade {

    private final List<ParDocumentos> pares = new ArrayList<>();

    @Override
    public RegistroRotacoes inserir(ParDocumentos par) {
        // TODO [Mateus - P2]: achar a posição (linear ou binária) e usar pares.add(posicao, par).
        //  Não usar Collections.sort a cada inserção.
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ListaOrdenada.inserir");
    }

    @Override
    public List<ParDocumentos> buscar(double similaridade) {
        // TODO [Mateus - P2]
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ListaOrdenada.buscar");
    }

    @Override
    public List<ParDocumentos> acimaDoLimiar(double limiar) {
        // TODO [Mateus - P2]
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ListaOrdenada.acimaDoLimiar");
    }

    @Override
    public List<ParDocumentos> topK(int k) {
        // TODO [Mateus - P2]
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ListaOrdenada.topK");
    }

    @Override
    public List<ParDocumentos> menosSimilares(int n) {
        // TODO [Mateus - P2]
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ListaOrdenada.menosSimilares");
    }

    @Override
    public int tamanho() {
        return pares.size();
    }
}
