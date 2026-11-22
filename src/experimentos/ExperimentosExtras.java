package experimentos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import app.LeitorDocumentos;
import avl.ArvoreAVL;
import avl.ListaOrdenada;
import avl.RegistroRotacoes;
import hash.HashMultiplicativo;
import hash.TabelaHashEncadeada;
import modelo.Documento;
import modelo.ParDocumentos;
import similaridade.Cosseno;
import texto.Normalizador;
import texto.StopWords;

/**
 * Experimentos complementares do relatório (seções 4.4, 4.5 e 5.3).
 *
 * Uso: make extras    (requer 'make gerar N=200' antes)
 *
 *  1. Efeito da ordem de inserção nas rotações da AVL (i<j, aleatória, crescente)
 *  2. Busca por chave: AVL x ListaOrdenada (100 mil consultas)
 *  3. Similaridade com e sem remoção de stop words
 */
public class ExperimentosExtras {

    public static void main(String[] args) throws Exception {
        String base = args.length > 0 ? args[0] : "documentos";
        String gerados = args.length > 1 ? args[1] : "documentos_gerados";

        for (String dir : new String[]{base, gerados}) {
            List<ParDocumentos> pares = calcularPares(dir, new StopWords());
            System.out.println("=== " + dir + ": " + pares.size() + " pares ===");
            ordemDeInsercao(pares);
            buscaPorChave(pares);
            System.out.println();
        }

        System.out.println("=== Stop words: similaridade com x sem remoção (" + base + ") ===");
        List<Documento> com = ler(base, new StopWords());
        List<Documento> sem = ler(base, new StopWords(new String[0]));
        String[][] casos = {
            {"doc05.txt", "doc07.txt"}, {"doc04.txt", "doc08.txt"}, {"doc01.txt", "doc09.txt"},
            {"doc11.txt", "doc01.txt"}, {"doc01.txt", "doc02.txt"}, {"doc01.txt", "doc03.txt"}
        };
        System.out.println("par                    com     sem");
        Cosseno cos = new Cosseno();
        for (String[] c : casos) {
            double s1 = cos.calcular(achar(com, c[0]).getFrequencias(), achar(com, c[1]).getFrequencias());
            double s2 = cos.calcular(achar(sem, c[0]).getFrequencias(), achar(sem, c[1]).getFrequencias());
            System.out.printf(Locale.US, "%s x %s   %.2f    %.2f%n", c[0], c[1], s1, s2);
        }
    }

    private static List<Documento> ler(String dir, StopWords sw) throws Exception {
        return new LeitorDocumentos(new Normalizador(sw), new HashMultiplicativo(),
                TabelaHashEncadeada.CAPACIDADE_PADRAO).lerDiretorio(dir);
    }

    private static Documento achar(List<Documento> docs, String nome) {
        for (Documento d : docs) if (d.getNome().equals(nome)) return d;
        throw new IllegalArgumentException("documento não encontrado: " + nome);
    }

    private static List<ParDocumentos> calcularPares(String dir, StopWords sw) throws Exception {
        List<Documento> d = ler(dir, sw);
        Cosseno c = new Cosseno();
        List<ParDocumentos> pares = new ArrayList<>();
        for (int i = 0; i < d.size(); i++)
            for (int j = i + 1; j < d.size(); j++)
                pares.add(new ParDocumentos(d.get(i).getNome(), d.get(j).getNome(),
                        c.calcular(d.get(i).getFrequencias(), d.get(j).getFrequencias())));
        return pares;
    }

    private static void ordemDeInsercao(List<ParDocumentos> pares) {
        List<ParDocumentos> crescente = new ArrayList<>(pares);
        crescente.sort(Comparator.comparingDouble(ParDocumentos::getSimilaridade));
        List<ParDocumentos> aleatoria = new ArrayList<>(pares);
        Collections.shuffle(aleatoria, new Random(7));

        String[] nomes = {"programa (i<j)", "aleatoria     ", "crescente     "};
        List<List<ParDocumentos>> ordens = List.of(pares, aleatoria, crescente);
        System.out.println("Ordem de inserção  -> rotações (nós / altura)");
        for (int o = 0; o < 3; o++) {
            ArvoreAVL t = new ArvoreAVL();
            RegistroRotacoes r = new RegistroRotacoes();
            for (ParDocumentos p : ordens.get(o)) r.somar(t.inserir(p));
            System.out.println("  " + nomes[o] + ": " + r + "  (nos=" + t.numeroDeNos()
                    + ", altura=" + t.altura() + ")");
        }
    }

    private static void buscaPorChave(List<ParDocumentos> pares) {
        ArvoreAVL avl = new ArvoreAVL();
        ListaOrdenada lista = new ListaOrdenada();
        for (ParDocumentos p : pares) { avl.inserir(p); lista.inserir(p); }

        Random rnd = new Random(3);
        double[] consultas = new double[100_000];
        for (int i = 0; i < consultas.length; i++)
            consultas[i] = pares.get(rnd.nextInt(pares.size())).getSimilaridade();

        for (int w = 0; w < 3; w++) for (double q : consultas) { avl.buscar(q); lista.buscar(q); }

        long ini = System.nanoTime();
        for (double q : consultas) avl.buscar(q);
        double tAvl = (System.nanoTime() - ini) / 1e6;
        ini = System.nanoTime();
        for (double q : consultas) lista.buscar(q);
        double tLista = (System.nanoTime() - ini) / 1e6;
        System.out.printf(Locale.US, "Busca por chave (100 mil consultas): AVL = %.1f ms | Lista = %.1f ms%n",
                tAvl, tLista);
    }
}
