package modelo;

import hash.TabelaHash;

/** Um documento lido do disco: nome do arquivo + tabela de frequências. (Pronto.) */
public class Documento {

    private final String nome;
    private final TabelaHash frequencias;
    private final int totalTokens;

    public Documento(String nome, TabelaHash frequencias, int totalTokens) {
        this.nome = nome;
        this.frequencias = frequencias;
        this.totalTokens = totalTokens;
    }

    /** Nome do arquivo, ex.: "doc01.txt". */
    public String getNome() { return nome; }

    public TabelaHash getFrequencias() { return frequencias; }

    /** Quantidade de tokens após a normalização (com repetições). */
    public int getTotalTokens() { return totalTokens; }

    @Override
    public String toString() { return nome; }
}
