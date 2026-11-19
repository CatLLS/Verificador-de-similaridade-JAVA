package avl;

import java.util.List;

import modelo.ParDocumentos;

/**
 * Estrutura que guarda os pares ordenados por similaridade.
 * Duas implementações, para comparar na Análise Experimental:
 *  - {@link ArvoreAVL}     (oficial)
 *  - {@link ListaOrdenada} (baseline simples)
 *
 * Responsável: Mateus (Pessoa 2)
 */
public interface IndiceSimilaridade {

    /**
     * Insere o par usando a similaridade como chave. Chaves iguais vão para a
     * mesma lista dentro do nó.
     *
     * @return rotações feitas nesta inserção (a ListaOrdenada retorna um registro zerado)
     */
    RegistroRotacoes inserir(ParDocumentos par);

    /** Pares com exatamente essa similaridade (lista vazia se não houver). */
    List<ParDocumentos> buscar(double similaridade);

    /** Pares com similaridade >= limiar, em ordem DECRESCENTE. */
    List<ParDocumentos> acimaDoLimiar(double limiar);

    /** Os k pares mais similares, em ordem decrescente. */
    List<ParDocumentos> topK(int k);

    /** Os n pares menos similares, em ordem crescente. */
    List<ParDocumentos> menosSimilares(int n);

    /** Total de pares armazenados. */
    int tamanho();
}
