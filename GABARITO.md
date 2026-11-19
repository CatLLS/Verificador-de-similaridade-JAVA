# Conjunto de teste — o que se espera de cada par

Este arquivo não é lido pelo programa, que só lê `*.txt`. Ele serve para conferir
se os resultados fazem sentido e para usar na seção 5 do relatório.

| Documentos | Tema | Relação planejada | Similaridade esperada* |
|---|---|---|---|
| doc01 × doc03 | Estruturas de dados | Cópia quase literal: poucas palavras trocadas (plágio) | **muito alta** (~0,9+) |
| doc09 × doc12 | Sistema solar | Mesmas frases em ordem inversa | **1,00**: limitação do modelo *bag of words*, bom para discutir |
| doc01 × doc02 | Estruturas de dados | Paráfrase: mesma ideia, palavras diferentes | média (~0,4): bem abaixo da cópia |
| doc02 × doc03 | Estruturas de dados | Paráfrase × cópia | média (~0,4) |
| doc01 × doc04 | Computação | Mesmo assunto amplo, conteúdo diferente | baixa (~0,1) |
| doc05 × doc06 | Futebol | Duas notícias diferentes sobre o mesmo campeonato | baixa/média (~0,3) |
| doc07 × doc08 | Culinária | Receita × texto sobre culinária mineira (pão de queijo nos dois) | baixa/média |
| doc09 × doc10 | Astronomia | Sistema solar × exoplanetas | baixa/média |
| doc11 × qualquer | — | Texto feito só de stop words e pontuação: fica **vazio** depois da normalização | **0,00** (testa a divisão por zero no cosseno) |
| temas diferentes | — | Ex.: doc05 × doc07 | ~0 |

\*Valores aproximados, medidos numa implementação de teste; os de vocês podem variar um pouco conforme a normalização (acentos, stop words).

Ideias para a discussão sobre **falsos positivos** (seção 5):
- Rodem uma vez **sem** remover stop words. Pares de temas diferentes (ex.: doc05 × doc07)
  passam a ter similaridade maior só por causa de "de", "o", "a", "que".
- doc01 × doc02 mostra o oposto, um **falso negativo**: "tabela hash" × "tabela de dispersão",
  "inserção" × "inserir". Stemming ou lematização ajudariam.

Corpus grande para os testes de desempenho: `make gerar N=200` cria `documentos_gerados/`.
