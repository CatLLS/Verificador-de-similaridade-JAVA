package avl;

import java.util.ArrayList;
import java.util.List;

import modelo.ParDocumentos;

/**
 * Árvore AVL ordenada pela similaridade.
 */
public class ArvoreAVL implements IndiceSimilaridade {

    public static final double EPSILON = 1e-9;

    private NoAVL raiz;
    private int totalPares;

    @Override
    public RegistroRotacoes inserir(ParDocumentos par) {
        RegistroRotacoes registro = new RegistroRotacoes();
        raiz = inserir(raiz, par, registro);
        totalPares++;
        return registro;
    }

    private NoAVL inserir(NoAVL no, ParDocumentos par, RegistroRotacoes registro) {
        if (no == null) {
            return new NoAVL(par);
        }

        int cmp = comparar(par.getSimilaridade(), no.chave);
        if (cmp < 0) {
            no.esquerda = inserir(no.esquerda, par, registro);
        } else if (cmp > 0) {
            no.direita = inserir(no.direita, par, registro);
        } else {
            no.pares.add(par);
            return no;
        }

        atualizarAltura(no);
        return balancear(no, registro);
    }

    private NoAVL balancear(NoAVL no, RegistroRotacoes registro) {
        int fb = fatorBalanceamento(no);

        if (fb > 1) {
            if (fatorBalanceamento(no.esquerda) >= 0) {
                registro.registrarSimplesDireita();
                return rotacaoDireita(no);
            } else {
                registro.registrarDuplaEsquerdaDireita();
                no.esquerda = rotacaoEsquerda(no.esquerda);
                return rotacaoDireita(no);
            }
        }

        if (fb < -1) {
            if (fatorBalanceamento(no.direita) <= 0) {
                registro.registrarSimplesEsquerda();
                return rotacaoEsquerda(no);
            } else {
                registro.registrarDuplaDireitaEsquerda();
                no.direita = rotacaoDireita(no.direita);
                return rotacaoEsquerda(no);
            }
        }

        return no;
    }

    private NoAVL rotacaoDireita(NoAVL y) {
        NoAVL x = y.esquerda;
        NoAVL t2 = x.direita;

        x.direita = y;
        y.esquerda = t2;

        atualizarAltura(y);
        atualizarAltura(x);

        return x;
    }

    private NoAVL rotacaoEsquerda(NoAVL x) {
        NoAVL y = x.direita;
        NoAVL t2 = y.esquerda;

        y.esquerda = x;
        x.direita = t2;

        atualizarAltura(x);
        atualizarAltura(y);

        return y;
    }

    private static int altura(NoAVL no) {
        return no == null ? 0 : no.altura;
    }

    private static void atualizarAltura(NoAVL no) {
        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
    }

    private static int fatorBalanceamento(NoAVL no) {
        return no == null ? 0 : altura(no.esquerda) - altura(no.direita);
    }

    /** Compara duas chaves considerando a tolerância EPSILON. */
    static int comparar(double a, double b) {
        if (Math.abs(a - b) <= EPSILON) return 0;
        return a < b ? -1 : 1;
    }

    @Override
    public List<ParDocumentos> buscar(double similaridade) {
        NoAVL atual = raiz;
        while (atual != null) {
            int cmp = comparar(similaridade, atual.chave);
            if (cmp < 0) {
                atual = atual.esquerda;
            } else if (cmp > 0) {
                atual = atual.direita;
            } else {
                return new ArrayList<>(atual.pares);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<ParDocumentos> acimaDoLimiar(double limiar) {
        List<ParDocumentos> saida = new ArrayList<>();
        acimaDoLimiar(raiz, limiar, saida);
        return saida;
    }

    private void acimaDoLimiar(NoAVL no, double limiar, List<ParDocumentos> saida) {
        if (no == null) return;

        if (comparar(no.chave, limiar) >= 0) {
            acimaDoLimiar(no.direita, limiar, saida);
            saida.addAll(no.pares);
            acimaDoLimiar(no.esquerda, limiar, saida);
        } else {
            acimaDoLimiar(no.direita, limiar, saida);
        }
    }

    @Override
    public List<ParDocumentos> topK(int k) {
        List<ParDocumentos> saida = new ArrayList<>();
        if (k <= 0) return saida;
        topK(raiz, k, saida);
        return saida;
    }

    private void topK(NoAVL no, int k, List<ParDocumentos> saida) {
        if (no == null || saida.size() >= k) return;

        topK(no.direita, k, saida);

        for (ParDocumentos par : no.pares) {
            if (saida.size() >= k) return;
            saida.add(par);
        }

        topK(no.esquerda, k, saida);
    }

    @Override
    public List<ParDocumentos> menosSimilares(int n) {
        List<ParDocumentos> saida = new ArrayList<>();
        if (n <= 0) return saida;
        menosSimilares(raiz, n, saida);
        return saida;
    }

    private void menosSimilares(NoAVL no, int n, List<ParDocumentos> saida) {
        if (no == null || saida.size() >= n) return;

        menosSimilares(no.esquerda, n, saida);

        for (ParDocumentos par : no.pares) {
            if (saida.size() >= n) return;
            saida.add(par);
        }

        menosSimilares(no.direita, n, saida);
    }

    @Override
    public int tamanho() {
        return totalPares;
    }

    /** Altura da árvore (útil no relatório: comparar com log2(n)). */
    public int altura() {
        return altura(raiz);
    }

    /** Número de nós (chaves distintas). */
    public int numeroDeNos() {
        return contarNos(raiz);
    }

    private int contarNos(NoAVL no) {
        return no == null ? 0 : 1 + contarNos(no.esquerda) + contarNos(no.direita);
    }

    /** Auxiliar para os testes: lista de todos os pares em ordem crescente. */
    public List<ParDocumentos> emOrdem() {
        List<ParDocumentos> saida = new ArrayList<>();
        emOrdem(raiz, saida);
        return saida;
    }

    private void emOrdem(NoAVL no, List<ParDocumentos> saida) {
        if (no == null) return;
        emOrdem(no.esquerda, saida);
        saida.addAll(no.pares);
        emOrdem(no.direita, saida);
    }
}