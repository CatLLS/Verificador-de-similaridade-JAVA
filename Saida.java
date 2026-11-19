package app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Escreve cada linha no terminal E no arquivo resultado.txt (exigência do enunciado). (Pronto.)
 */
public class Saida implements AutoCloseable {

    private final PrintWriter arquivo;

    public Saida(String caminhoArquivo) throws IOException {
        this.arquivo = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(caminhoArquivo), StandardCharsets.UTF_8));
    }

    public void println(String linha) {
        System.out.println(linha);
        arquivo.println(linha);
    }

    public void println() {
        println("");
    }

    @Override
    public void close() {
        arquivo.flush();
        arquivo.close();
    }
}
