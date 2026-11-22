# Verificador de Similaridade de Textos

Projeto 2 de **Projeto e Análise de Algoritmos II**, Universidade Presbiteriana Mackenzie.

| Integrante | RA | Frente |
|---|---|---|
| Catarina Silva e Meirelles | 10239324 | **P3**: integração, cosseno, leitura, CLI, testes de desempenho |
| Cauê Santana | 10736791 | **P1**: normalização de texto + tabela hash (2 funções) |
| Mateus Sarmento | 10741262 | **P2**: árvore AVL + lista ordenada (comparação) |

Decisões já tomadas: **similaridade do cosseno** e **tabela hash com encadeamento externo**
(capacidade fixa e prima, sem rehash).

## Comandos para copiar e colar

### Compilar

```bash
make
```

### Executar os modos

```bash
java -cp bin Main documentos 0.75 lista
```

```bash
java -cp bin Main documentos 0.3 topk 5
```

```bash
java -cp bin Main documentos 0.0 busca doc01.txt doc03.txt
```

### Escolher a função hash

```bash
java -cp bin Main documentos 0.75 lista --hash=multiplicativo
```

```bash
java -cp bin Main documentos 0.75 lista --hash=polinomial
```

### Mostrar estatísticas

```bash
java -cp bin Main documentos 0.0 lista --stats
```

```bash
make stats
```

### Comandos Make

```bash
make lista LIMIAR=0.75
make topk K=5 LIMIAR=0.3
make busca A=doc01.txt B=doc03.txt
make gerar N=200
make benchmark
make entrega
make clean
make help
```

Requer **JDK 17+** e `make`.

A saída aparece no terminal e também é gravada em `resultado.txt`, no diretório onde o
programa foi executado. Argumentos opcionais: `--hash=multiplicativo|polinomial` e `--stats`.

## Estrutura

```
src/
├── Main.java                      fluxo principal (pacote padrão por causa do "java Main")   [P3]
├── app/
│   ├── Argumentos.java            leitura dos argumentos da linha de comando (pronto)        [P3]
│   ├── LeitorDocumentos.java      lê os .txt e monta os Documentos                           [P3]
│   └── Saida.java                 terminal + resultado.txt (pronto)
├── texto/
│   ├── Normalizador.java          minúsculas, pontuação, tokens, stop words                  [P1]
│   └── StopWords.java             lista em português (pronta) + consulta                     [P1]
├── hash/
│   ├── FuncaoHash.java            interface
│   ├── HashMultiplicativo.java    função hash 1                                              [P1]
│   ├── HashPolinomial.java        função hash 2                                              [P1]
│   ├── TabelaHash.java            interface
│   ├── TabelaHashEncadeada.java   implementação manual com encadeamento externo              [P1]
│   └── EstatisticasHash.java      colisões por bucket (pronto)
├── similaridade/
│   ├── MetricaSimilaridade.java   interface
│   └── Cosseno.java                                                                          [P3]
├── avl/
│   ├── IndiceSimilaridade.java    interface comum (AVL e lista)
│   ├── ArvoreAVL.java             AVL com lista de pares por nó + contagem de rotações       [P2]
│   ├── NoAVL.java
│   ├── ListaOrdenada.java         baseline para comparação                                   [P2]
│   └── RegistroRotacoes.java      contador de rotações (pronto)
├── modelo/
│   ├── Documento.java             (pronto)
│   └── ParDocumentos.java         (pronto; formato "a <-> b = 0.82")
└── experimentos/
    ├── Benchmark.java             medições para o relatório                                  [P3]
    └── GeradorDocumentos.java     gera corpus sintético (pronto)
documentos/                        12 textos de teste + GABARITO.md
```

## Como trabalhar

- Procurem o próprio nome: `grep -rn "TODO \[Cauê" src/`, e o mesmo com `Mateus` e `Catarina`.
- Enquanto uma parte não está pronta, o programa compila normalmente e, ao rodar, avisa
  qual método ainda falta (`Parte ainda não implementada -> ...`).
- Ordem sugerida para testar: P1 (hash) → `make busca` funciona com o Cosseno da P3 →
  P2 (AVL) → `make lista` e `make topk` funcionam.
- Cada um usa uma branch (`p1-hash`, `p2-avl`, `p3-integracao`) e abre PR para a `main`.
- **Anotem os bugs enquanto acontecem** (sintoma → investigação → solução). O relatório
  pede o "bug mais desafiador" de cada um, com detalhes.

## Checklist de entrega

- [ ] `make clean && make` compila sem erros (se não compilar, a nota é 0)
- [ ] Os três modos funcionam com `java Main ...` e geram `resultado.txt`
- [ ] Nenhum `HashMap`/`HashSet`/`Hashtable` no projeto: `grep -rn "HashMap\|HashSet\|Hashtable" src/`
- [ ] Todos os `TODO` foram resolvidos: `grep -rn TODO src/`
- [ ] O relatório tem referências, prints e gráficos
