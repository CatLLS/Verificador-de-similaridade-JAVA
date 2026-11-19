# Verificador de Similaridade de Textos — PAA II (Mackenzie)
# Requer JDK 17+ e make.

SRC_DIR   := src
BIN_DIR   := bin
SOURCES   := $(shell find $(SRC_DIR) -name '*.java')
JAVAC     := javac
JAVA      := java
JFLAGS    := --release 17 -encoding UTF-8 -Xlint:unchecked

# Parâmetros padrão (podem ser sobrescritos: make lista LIMIAR=0.3)
DIR       ?= documentos
LIMIAR    ?= 0.5
K         ?= 5
A         ?= doc01.txt
B         ?= doc03.txt
HASH      ?= multiplicativo
N         ?= 200
GERADOS   ?= documentos_gerados

.PHONY: all compile lista topk busca stats gerar benchmark entrega clean help

all: compile

compile: $(BIN_DIR)/.compilado

$(BIN_DIR)/.compilado: $(SOURCES)
	@mkdir -p $(BIN_DIR)
	$(JAVAC) $(JFLAGS) -d $(BIN_DIR) $(SOURCES)
	@touch $@

## Os três modos do enunciado
lista: compile
	$(JAVA) -cp $(BIN_DIR) Main $(DIR) $(LIMIAR) lista --hash=$(HASH)

topk: compile
	$(JAVA) -cp $(BIN_DIR) Main $(DIR) $(LIMIAR) topk $(K) --hash=$(HASH)

busca: compile
	$(JAVA) -cp $(BIN_DIR) Main $(DIR) 0.0 busca $(A) $(B)

## Estatísticas de hash e AVL para o relatório (as duas funções hash)
stats: compile
	$(JAVA) -cp $(BIN_DIR) Main $(DIR) 0.0 lista --hash=multiplicativo --stats
	$(JAVA) -cp $(BIN_DIR) Main $(DIR) 0.0 lista --hash=polinomial --stats

## Corpus sintético para os testes de desempenho
gerar: compile
	$(JAVA) -cp $(BIN_DIR) experimentos.GeradorDocumentos $(DIR) $(GERADOS) $(N)

benchmark: compile
	$(JAVA) -cp $(BIN_DIR) experimentos.Benchmark $(GERADOS) resultados_benchmark.csv

## Pasta com Main.class na raiz, para rodar exatamente "java Main ..."
entrega: compile
	@rm -rf entrega && mkdir -p entrega
	cp -r $(BIN_DIR)/. entrega/
	@rm -f entrega/.compilado
	@echo "Pronto: cd entrega && java Main ../documentos 0.75 lista"

clean:
	rm -rf $(BIN_DIR) entrega resultado.txt resultados_benchmark.csv $(GERADOS)

help:
	@echo "make            compila"
	@echo "make lista      [DIR=documentos LIMIAR=0.5 HASH=multiplicativo|polinomial]"
	@echo "make topk       [K=5 LIMIAR=0.5]"
	@echo "make busca      [A=doc01.txt B=doc03.txt]"
	@echo "make stats      estatísticas das duas funções hash + rotações da AVL"
	@echo "make gerar      [N=200] gera corpus sintético em $(GERADOS)/"
	@echo "make benchmark  roda os testes de desempenho"
	@echo "make entrega    copia os .class para entrega/ (java Main ...)"
	@echo "make clean      apaga os arquivos gerados"
