package avl;

import java.util.ArrayList;
import java.util.List;

import modelo.ParDocumentos;

/**
 * Árvore AVL ordenada pela similaridade.
 *
 * Responsável: Mateus (Pessoa 2)
 *
 * Requisitos do enunciado:
 *  - a chave é a similaridade; cada nó guarda uma lista de pares (chaves repetidas)
 *  - a inserção retorna um {@link RegistroRotacoes} com as rotações feitas
 *
 * Atenção com double: 0.1 + 0.2 != 0.3. Decidam como comparar chaves "iguais"
 * (usar uma tolerância EPSILON ou arredondar a similaridade, por exemplo para
 * 4 casas, antes de inserir) e expliquem no relatório.
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
        // TODO [Mateus - P2]:
        //  1. no == null -> return new NoAVL(par)
        //  2. comparar(par.getSimilaridade(), no.chave):
        //       < 0 -> no.esquerda = inserir(no.esquerda, ...)
        //       > 0 -> no.direita  = inserir(no.direita, ...)
        //      == 0 -> no.pares.add(par); return no  (não muda a altura)
        //  3. atualizarAltura(no)
        //  4. return balancear(no, registro)
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ArvoreAVL.inserir");
    }

    private NoAVL balancear(NoAVL no, RegistroRotacoes registro) {
        // TODO [Mateus - P2]: pelo fator de balanceamento, identificar o caso e registrar:
        //  LL -> rotacaoDireita                                 registro.registrarSimplesDireita()
        //  RR -> rotacaoEsquerda                                registro.registrarSimplesEsquerda()
        //  LR -> esquerda no filho esq. + direita no nó         registro.registrarDuplaEsquerdaDireita()
        //  RL -> direita no filho dir. + esquerda no nó         registro.registrarDuplaDireitaEsquerda()
        //  Contem a rotação dupla como UMA dupla, não como duas simples (e expliquem no relatório).
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ArvoreAVL.balancear");
    }

    private NoAVL rotacaoDireita(NoAVL y) {
        // TODO [Mateus - P2]
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ArvoreAVL.rotacaoDireita");
    }

    private NoAVL rotacaoEsquerda(NoAVL x) {
        // TODO [Mateus - P2]
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ArvoreAVL.rotacaoEsquerda");
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
        // TODO [Mateus - P2]: busca binária normal na árvore; retornar cópia da lista do nó ou lista vazia.
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ArvoreAVL.buscar");
    }

    @Override
    public List<ParDocumentos> acimaDoLimiar(double limiar) {
        // TODO [Mateus - P2]: percurso em ordem REVERSA (direita, nó, esquerda),
        //  cortando as subárvores esquerdas quando chave < limiar.
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ArvoreAVL.acimaDoLimiar");
    }

    @Override
    public List<ParDocumentos> topK(int k) {
        // TODO [Mateus - P2]: percurso em ordem reversa, parando ao juntar k pares.
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ArvoreAVL.topK");
    }

    @Override
    public List<ParDocumentos> menosSimilares(int n) {
        // TODO [Mateus - P2]: percurso em ordem normal, parando ao juntar n pares.
        throw new UnsupportedOperationException("TODO [Mateus - P2]: ArvoreAVL.menosSimilares");
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
