import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.Argumentos;
import app.LeitorDocumentos;
import app.Saida;
import avl.ArvoreAVL;
import avl.RegistroRotacoes;
import hash.EstatisticasHash;
import hash.FuncaoHash;
import hash.HashMultiplicativo;
import hash.HashPolinomial;
import hash.TabelaHashEncadeada;
import modelo.Documento;
import modelo.ParDocumentos;
import similaridade.Cosseno;
import similaridade.MetricaSimilaridade;
import texto.Normalizador;
import texto.StopWords;

/**
 * Verificador de Similaridade de Textos — Projeto e Análise de Algoritmos II.
 *
 * Fluxo: ler os documentos -> tabela hash por documento -> cosseno para todos os pares
 * -> inserir na AVL -> consultar conforme o modo -> terminal + resultado.txt
 *
 * Responsável pela integração: Catarina (Pessoa 3)
 * Este arquivo fica no pacote padrão porque o enunciado exige "java Main ...".
 */
public class Main {

    public static final String ARQUIVO_SAIDA = "resultado.txt";
    public static final String CABECALHO = "=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===";

    public static void main(String[] args) {
        Argumentos cfg;
        try {
            cfg = Argumentos.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            System.err.println(Argumentos.uso());
            System.exit(1);
            return;
        }

        try (Saida saida = new Saida(ARQUIVO_SAIDA)) {
            executar(cfg, saida);
        } catch (IOException e) {
            System.err.println("Erro de leitura/escrita: " + e.getMessage());
            System.exit(1);
        } catch (UnsupportedOperationException e) {
            // Some quando todos os TODOs estiverem implementados.
            System.err.println("Parte ainda não implementada -> " + e.getMessage());
            System.exit(2);
        }
    }

    static FuncaoHash escolherFuncaoHash(String nome) {
        switch (nome) {
            case "multiplicativo": return new HashMultiplicativo();
            case "polinomial":     return new HashPolinomial();
            default: throw new IllegalArgumentException("função hash desconhecida: " + nome);
        }
    }

    static void executar(Argumentos cfg, Saida saida) throws IOException {
        FuncaoHash funcaoHash = escolherFuncaoHash(cfg.funcaoHash);
        MetricaSimilaridade metrica = new Cosseno();
        LeitorDocumentos leitor = new LeitorDocumentos(
                new Normalizador(new StopWords()), funcaoHash, TabelaHashEncadeada.CAPACIDADE_PADRAO);

        if (cfg.modo == Argumentos.Modo.BUSCA) {
            Documento a = leitor.lerArquivo(Path.of(cfg.diretorio, cfg.arquivoA));
            Documento b = leitor.lerArquivo(Path.of(cfg.diretorio, cfg.arquivoB));
            double s = metrica.calcular(a.getFrequencias(), b.getFrequencias());
            saida.println(CABECALHO);
            saida.println("Comparando: " + a.getNome() + " <-> " + b.getNome());
            saida.println(String.format(Locale.US, "Similaridade calculada: %.2f", s));
            saida.println("Métrica utilizada: " + metrica.nome());
            return;
        }

        List<Documento> docs = leitor.lerDiretorio(cfg.diretorio);

        // Compara todos os pares (i < j) e insere na AVL, acumulando as rotações.
        ArvoreAVL arvore = new ArvoreAVL();
        RegistroRotacoes rotacoes = new RegistroRotacoes();
        int paresComparados = 0;
        for (int i = 0; i < docs.size(); i++) {
            for (int j = i + 1; j < docs.size(); j++) {
                Documento a = docs.get(i), b = docs.get(j);
                double s = metrica.calcular(a.getFrequencias(), b.getFrequencias());
                rotacoes.somar(arvore.inserir(new ParDocumentos(a.getNome(), b.getNome(), s)));
                paresComparados++;
            }
        }

        saida.println(CABECALHO);
        saida.println("Total de documentos processados: " + docs.size());
        saida.println("Total de pares comparados: " + paresComparados);
        saida.println("Função hash utilizada: " + funcaoHash.nome());
        saida.println("Métrica de similaridade: " + metrica.nome());
        saida.println();

        String limiarTxt = String.format(Locale.US, "%.2f", cfg.limiar);
        if (cfg.modo == Argumentos.Modo.LISTA) {
            saida.println("Pares com similaridade >= " + limiarTxt + ":");
            imprimirPares(saida, arvore.acimaDoLimiar(cfg.limiar));
            saida.println();
            saida.println("Pares com menor similaridade:");
            imprimirPares(saida, arvore.menosSimilares(1));
        } else { // TOPK
            // Decisão de projeto (justificar no relatório): o topK também respeita o limiar.
            List<ParDocumentos> top = new ArrayList<>();
            for (ParDocumentos p : arvore.topK(cfg.k)) {
                if (p.getSimilaridade() >= cfg.limiar) top.add(p);
            }
            saida.println("Top " + cfg.k + " pares com similaridade >= " + limiarTxt + ":");
            imprimirPares(saida, top);
        }

        if (cfg.estatisticas) {
            imprimirEstatisticas(docs, arvore, rotacoes);
        }
    }

    private static void imprimirPares(Saida saida, List<ParDocumentos> pares) {
        if (pares.isEmpty()) {
            saida.println("(nenhum par)");
            return;
        }
        for (ParDocumentos p : pares) saida.println(p.toString());
    }

    /** Dados para o relatório. Vai só para o terminal (stderr), não para o resultado.txt. */
    private static void imprimirEstatisticas(List<Documento> docs, ArvoreAVL arvore, RegistroRotacoes rot) {
        EstatisticasHash[] todas = new EstatisticasHash[docs.size()];
        for (int i = 0; i < docs.size(); i++) todas[i] = docs.get(i).getFrequencias().estatisticas();
        System.err.println();
        System.err.println("--- ESTATÍSTICAS (--stats) ---");
        System.err.println("Hash (soma de todos os documentos): " + EstatisticasHash.combinar(todas).resumo());
        System.err.println("AVL: pares=" + arvore.tamanho() + " nos=" + arvore.numeroDeNos()
                + " altura=" + arvore.altura());
        System.err.println("Rotações: " + rot);
    }
}
