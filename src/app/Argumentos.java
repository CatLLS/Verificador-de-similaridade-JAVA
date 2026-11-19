package app;

import java.util.Locale;

/**
 * Leitura e validação dos argumentos da linha de comando. (Pronto; revisar.)
 *
 * Responsável: Catarina (Pessoa 3)
 *
 *   java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]
 *
 *   modo: lista | topk K | busca arqA arqB
 *   opcionais (fora do enunciado, para os experimentos):
 *     --hash=multiplicativo|polinomial   (padrão: multiplicativo)
 *     --stats                            imprime estatísticas de hash e rotações
 */
public class Argumentos {

    public enum Modo { LISTA, TOPK, BUSCA }

    public final String diretorio;
    public final double limiar;
    public final Modo modo;
    public final int k;
    public final String arquivoA;
    public final String arquivoB;
    public final String funcaoHash;
    public final boolean estatisticas;

    private Argumentos(String diretorio, double limiar, Modo modo, int k,
                       String arquivoA, String arquivoB, String funcaoHash, boolean estatisticas) {
        this.diretorio = diretorio;
        this.limiar = limiar;
        this.modo = modo;
        this.k = k;
        this.arquivoA = arquivoA;
        this.arquivoB = arquivoB;
        this.funcaoHash = funcaoHash;
        this.estatisticas = estatisticas;
    }

    public static Argumentos parse(String[] args) {
        if (args.length < 3) throw new IllegalArgumentException("argumentos insuficientes");

        String diretorio = args[0];

        double limiar;
        try {
            limiar = Double.parseDouble(args[1].replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("limiar inválido: " + args[1]);
        }
        if (limiar < 0 || limiar > 1) throw new IllegalArgumentException("limiar deve estar entre 0 e 1");

        Modo modo;
        int k = 0;
        String arqA = null, arqB = null;
        int proximo; // índice do primeiro argumento opcional

        switch (args[2].toLowerCase(Locale.ROOT)) {
            case "lista":
                modo = Modo.LISTA;
                proximo = 3;
                break;
            case "topk":
                modo = Modo.TOPK;
                if (args.length < 4) throw new IllegalArgumentException("modo topk precisa de K");
                try {
                    k = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("K inválido: " + args[3]);
                }
                if (k <= 0) throw new IllegalArgumentException("K deve ser > 0");
                proximo = 4;
                break;
            case "busca":
                modo = Modo.BUSCA;
                if (args.length < 5) throw new IllegalArgumentException("modo busca precisa de dois arquivos");
                arqA = args[3];
                arqB = args[4];
                proximo = 5;
                break;
            default:
                throw new IllegalArgumentException("modo desconhecido: " + args[2]);
        }

        String hash = "multiplicativo";
        boolean stats = false;
        for (int i = proximo; i < args.length; i++) {
            String a = args[i];
            if (a.startsWith("--hash=")) hash = a.substring("--hash=".length()).toLowerCase(Locale.ROOT);
            else if (a.equals("--stats")) stats = true;
            else throw new IllegalArgumentException("argumento opcional desconhecido: " + a);
        }

        return new Argumentos(diretorio, limiar, modo, k, arqA, arqB, hash, stats);
    }

    public static String uso() {
        return "Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]\n"
             + "  java Main ./documentos 0.75 lista\n"
             + "  java Main ./documentos 0.8 topk 5\n"
             + "  java Main ./documentos 0.0 busca doc01.txt doc02.txt\n"
             + "Opcionais: --hash=multiplicativo|polinomial  --stats";
    }
}
