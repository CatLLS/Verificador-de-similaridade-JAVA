package app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import hash.FuncaoHash;
import hash.TabelaHashEncadeada;
import modelo.Documento;
import texto.Normalizador;

/**
 * Lê todos os .txt de um diretório e monta um {@link Documento} para cada um.
 *
 * Responsável: Catarina (Pessoa 3)
 */
public class LeitorDocumentos {

    private final Normalizador normalizador;
    private final FuncaoHash funcaoHash;
    private final int capacidadeTabela;

    public LeitorDocumentos(Normalizador normalizador, FuncaoHash funcaoHash, int capacidadeTabela) {
        this.normalizador = normalizador;
        this.funcaoHash = funcaoHash;
        this.capacidadeTabela = capacidadeTabela;
    }

    /**
     * @return documentos ordenados pelo nome do arquivo (saída determinística)
     * @throws IOException se o diretório não existir ou não puder ser lido
     */
    public List<Documento> lerDiretorio(String caminho) throws IOException {
        Path diretorio = Path.of(caminho);
        if (!Files.exists(diretorio)) {
            throw new IOException("diretório inexistente: " + diretorio);
        }
        if (!Files.isDirectory(diretorio)) {
            throw new IOException("caminho não é um diretório: " + diretorio);
        }
        if (!Files.isReadable(diretorio)) {
            throw new IOException("diretório não pode ser lido: " + diretorio);
        }

        List<Path> arquivos = new ArrayList<>();
        try (Stream<Path> arquivosStream = Files.list(diretorio)) {
            arquivosStream
                    .filter(Files::isRegularFile)
                    .filter(arquivo -> arquivo.getFileName().toString().endsWith(".txt"))
                    .sorted(Comparator.comparing(arquivo -> arquivo.getFileName().toString()))
                    .forEach(arquivos::add);
        }

        List<Documento> documentos = new ArrayList<>(arquivos.size());
        for (Path arquivo : arquivos) {
            documentos.add(lerArquivo(arquivo));
        }
        return documentos;
    }

    /** Lê um único arquivo (usado também pelo modo busca). */
    public Documento lerArquivo(java.nio.file.Path arquivo) throws IOException {
        String texto = Files.readString(arquivo, StandardCharsets.UTF_8);
        String[] tokens = normalizador.normalizar(texto);
        TabelaHashEncadeada tabela = new TabelaHashEncadeada(funcaoHash, capacidadeTabela);
        for (String token : tokens) {
            tabela.incrementar(token);
        }
        return new Documento(arquivo.getFileName().toString(), tabela, tokens.length);
    }
}
