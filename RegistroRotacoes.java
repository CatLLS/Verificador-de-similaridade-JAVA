package avl;

/**
 * Quantifica as rotações feitas em uma inserção da AVL (exigência do enunciado).
 * (Pronto. Usado por: Mateus, Pessoa 2.)
 */
public class RegistroRotacoes {

    private int simplesEsquerda;
    private int simplesDireita;
    private int duplaEsquerdaDireita; // caso LR: rotação à esquerda no filho e à direita no nó
    private int duplaDireitaEsquerda; // caso RL: rotação à direita no filho e à esquerda no nó

    public void registrarSimplesEsquerda() { simplesEsquerda++; }
    public void registrarSimplesDireita() { simplesDireita++; }
    public void registrarDuplaEsquerdaDireita() { duplaEsquerdaDireita++; }
    public void registrarDuplaDireitaEsquerda() { duplaDireitaEsquerda++; }

    public int getSimplesEsquerda() { return simplesEsquerda; }
    public int getSimplesDireita() { return simplesDireita; }
    public int getDuplaEsquerdaDireita() { return duplaEsquerdaDireita; }
    public int getDuplaDireitaEsquerda() { return duplaDireitaEsquerda; }

    public int totalSimples() { return simplesEsquerda + simplesDireita; }
    public int totalDuplas() { return duplaEsquerdaDireita + duplaDireitaEsquerda; }
    public int total() { return totalSimples() + totalDuplas(); }

    /** Acumula outro registro neste (para o total de todas as inserções). */
    public void somar(RegistroRotacoes outro) {
        simplesEsquerda += outro.simplesEsquerda;
        simplesDireita += outro.simplesDireita;
        duplaEsquerdaDireita += outro.duplaEsquerdaDireita;
        duplaDireitaEsquerda += outro.duplaDireitaEsquerda;
    }

    @Override
    public String toString() {
        return "simplesEsq=" + simplesEsquerda + " simplesDir=" + simplesDireita
                + " duplaEsqDir=" + duplaEsquerdaDireita + " duplaDirEsq=" + duplaDireitaEsquerda
                + " total=" + total();
    }
}
