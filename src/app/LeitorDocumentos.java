package app;

import java.io.IOException;
import java.util.List;

import hash.FuncaoHash;
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
        // TODO [Catarina - P3]:
        //  - java.nio.file.Files.list(Path.of(caminho)), filtrando os arquivos que terminam em ".txt"
        //  - ordenar por nome
        //  - para cada um: lerDocumento(path)
        //  - diretório inexistente -> lançar IOException com mensagem clara
        throw new UnsupportedOperationException("TODO [Catarina - P3]: LeitorDocumentos.lerDiretorio");
    }

    /** Lê um único arquivo (usado também pelo modo busca). */
    public Documento lerArquivo(java.nio.file.Path arquivo) throws IOException {
        // TODO [Catarina - P3]:
        //  - Files.readString(arquivo, StandardCharsets.UTF_8)
        //  - tokens = normalizador.normalizar(texto)
        //  - tabela = new hash.TabelaHashEncadeada(funcaoHash, capacidadeTabela)
        //  - tabela.incrementar(t) para cada token
        //  - return new Documento(arquivo.getFileName().toString(), tabela, tokens.length)
        throw new UnsupportedOperationException("TODO [Catarina - P3]: LeitorDocumentos.lerArquivo");
    }
}
