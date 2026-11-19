package texto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        if (texto == null || texto.isEmpty()) {
            return new String[0];
        }

        String minusculo = texto.toLowerCase(new Locale("pt", "BR"));
        StringBuilder limpo = new StringBuilder(minusculo.length());
        for (int i = 0; i < minusculo.length(); i++) {
            char c = minusculo.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                limpo.append(c);
            } else {
                limpo.append(' ');
            }
        }

        String[] brutos = limpo.toString().split("\\s+");
        List<String> filtrados = new ArrayList<>();
        for (String token : brutos) {
            if (!token.isEmpty() && (stopWords == null || !stopWords.eStopWord(token))) {
                filtrados.add(token);
            }
        }

        return filtrados.toArray(new String[0]);
    }
}
