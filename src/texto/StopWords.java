package texto;

import hash.HashPolinomial;
import hash.TabelaHash;
import hash.TabelaHashEncadeada;

/**
 * Lista pré-definida de stop words do português.
 *
 * Responsável: Cauê (Pessoa 1)
 *
 * A lista já está preenchida; falta implementar a consulta. Opções:
 *  (a) ordenar o array no construtor e usar busca binária, ou
 *  (b) reaproveitar a própria TabelaHashEncadeada (bom ponto para o relatório).
 * Evitem java.util.HashSet para não parecer que a hash manual foi contornada.
 *
 * Todas as palavras estão em minúsculas e com acento, porque a comparação é feita
 * depois de passar o texto para minúsculas. Se decidirem remover acentos na
 * normalização, removam aqui também.
 */
public class StopWords {

    public static final String[] LISTA_PADRAO = {
        "a", "à", "ao", "aos", "aquela", "aquelas", "aquele", "aqueles", "aquilo", "as", "às", "até",
        "com", "como", "da", "das", "de", "dela", "delas", "dele", "deles", "depois", "do", "dos",
        "e", "é", "ela", "elas", "ele", "eles", "em", "entre", "era", "eram", "essa", "essas", "esse",
        "esses", "esta", "está", "estão", "estas", "este", "estes", "eu", "foi", "foram", "há",
        "isso", "isto", "já", "lhe", "lhes", "mais", "mas", "me", "mesmo", "meu", "meus", "minha",
        "minhas", "muito", "na", "nas", "não", "nem", "no", "nos", "nós", "nossa", "nossas", "nosso",
        "nossos", "num", "numa", "o", "os", "ou", "para", "pela", "pelas", "pelo", "pelos", "por",
        "qual", "quando", "que", "quê", "quem", "se", "sem", "ser", "seu", "seus", "só", "sua", "suas",
        "também", "te", "tem", "têm", "teu", "teus", "tu", "tua", "tuas", "um", "uma", "umas", "uns",
        "você", "vocês", "vos", "então", "porque", "pois", "sobre", "ainda", "cada", "onde",
        "seja", "sido", "ter", "tinha", "são", "estar", "havia", "lá", "aqui"
    };

    private final String[] palavras;
    private final TabelaHash tabela;

    public StopWords() {
        this(LISTA_PADRAO);
    }

    public StopWords(String[] palavras) {
        this.palavras = palavras.clone();
        this.tabela = new TabelaHashEncadeada(new HashPolinomial(), 257);
        for (String p : this.palavras) {
            if (p != null && !p.isEmpty()) {
                this.tabela.incrementar(p);
            }
        }
    }

    /** @return true se o token (já em minúsculas) for stop word. */
    public boolean eStopWord(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return tabela.contem(token);
    }

    public int tamanho() {
        return palavras.length;
    }
}
