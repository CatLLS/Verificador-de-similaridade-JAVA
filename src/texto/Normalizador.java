package texto;

/**
 * Normalização obrigatória do texto.
 *
 * Responsável: Cauê (Pessoa 1)
 *
 * Passos mínimos exigidos pelo enunciado:
 *  1. converter para minúsculas
 *  2. remover pontuação e caracteres não alfanuméricos
 *  3. tokenizar (separar em palavras)
 *  4. remover stop words ({@link StopWords})
 */
public class Normalizador {

    private final StopWords stopWords;

    public Normalizador(StopWords stopWords) {
        this.stopWords = stopWords;
    }

    /**
     * @param texto conteúdo bruto do arquivo
     * @return tokens já normalizados, sem stop words (pode ser vazio)
     */
    public String[] normalizar(String texto) {
        // TODO [Cauê - P1]:
        //  - texto.toLowerCase(new java.util.Locale("pt", "BR"))
        //  - trocar todo caractere que NÃO for letra ou dígito por espaço.
        //    Character.isLetterOrDigit mantém letras acentuadas (á, ç, õ...).
        //    Decidir (e justificar no relatório) se os acentos serão removidos.
        //  - split por espaços ("\\s+"), descartando tokens vazios
        //  - descartar stop words com stopWords.eStopWord(token)
        throw new UnsupportedOperationException("TODO [Cauê - P1]: Normalizador.normalizar");
    }
}
