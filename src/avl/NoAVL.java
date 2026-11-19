package avl;

import java.util.ArrayList;
import java.util.List;

import modelo.ParDocumentos;

/**
 * Nó da AVL. A chave é a similaridade; cada nó guarda uma LISTA de pares,
 * porque vários pares podem ter a mesma similaridade.
 *
 * Responsável: Mateus (Pessoa 2)
 */
class NoAVL {

    final double chave;
    final List<ParDocumentos> pares = new ArrayList<>();
    int altura = 1;
    NoAVL esquerda;
    NoAVL direita;

    NoAVL(ParDocumentos primeiro) {
        this.chave = primeiro.getSimilaridade();
        this.pares.add(primeiro);
    }
}
