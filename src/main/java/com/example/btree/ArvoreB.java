package com.example.btree;

import java.util.ArrayList;
import java.util.List;

public class ArvoreB {
    private NoArvoreB raiz;
    private final int ordemMinima;

    public ArvoreB(int ordemMinima) {
        this.ordemMinima = ordemMinima;
        this.raiz = new NoArvoreB(ordemMinima, true);
    }

    public void inserir(String chave) {
        NoArvoreB noRaiz = raiz;
        if (noRaiz.estaCheio()) {
            NoArvoreB novoNo = new NoArvoreB(ordemMinima, false);
            novoNo.filhos.add(noRaiz);
            novoNo.dividirFilho(0, noRaiz);
            novoNo.inserirNaoCheio(chave);
            raiz = novoNo;
        } else {
            noRaiz.inserirNaoCheio(chave);
        }
    }

    public String buscar(String chave) {
        return raiz.buscar(chave);
    }

    public NoArvoreB obterRaiz() {
        return raiz;
    }
}

class NoArvoreB {
    protected List<String> chaves;
    protected List<NoArvoreB> filhos;
    protected int numeroChaves; // Número de chaves
    protected boolean folha;
    protected final int ordemMinima; // Ordem mínima

    public NoArvoreB(int ordemMinima, boolean folha) {
        this.ordemMinima = ordemMinima;
        this.folha = folha;
        // Um nó pode ter no máximo 2t-1 chaves e 2t filhos.
        this.chaves = new ArrayList<>(2 * ordemMinima - 1);
        this.filhos = new ArrayList<>(2 * ordemMinima);
        this.numeroChaves = 0;
    }

    public boolean estaCheio() {
        return numeroChaves == 2 * ordemMinima - 1;
    }

    public void inserirNaoCheio(String chave) {
        int indice = numeroChaves - 1;
        if (folha) {
            // Encontra a posição para a nova chave
            while (indice >= 0 && chave.compareTo(chaves.get(indice)) < 0) {
                indice--;
            }
            // Insere a nova chave
            chaves.add(indice + 1, chave);
            numeroChaves++;
        } else {
            // Encontra o filho para onde a nova chave deve ir
            while (indice >= 0 && chave.compareTo(chaves.get(indice)) < 0) {
                indice--;
            }
            indice++; // indice é o índice do filho

            NoArvoreB filho = filhos.get(indice);

            if (filho.estaCheio()) {
                dividirFilho(indice, filho);
                // Decide em qual dos dois filhos a chave deve ir
                if (chave.compareTo(chaves.get(indice)) > 0) {
                    indice++;
                }
            }
            filhos.get(indice).inserirNaoCheio(chave);
        }
    }

    public void dividirFilho(int indice, NoArvoreB noFilho) {
        NoArvoreB novoNoFilho = new NoArvoreB(ordemMinima, noFilho.folha);

        // Move a metade superior das chaves de noFilho para novoNoFilho
        // Começa do índice t (ordemMinima) até 2t-2
        for (int j = ordemMinima; j < 2 * ordemMinima - 1; j++) {
            novoNoFilho.chaves.add(noFilho.chaves.get(j));
        }
        novoNoFilho.numeroChaves = ordemMinima - 1;
        
        // Remove as chaves movidas de noFilho
        for (int j = 2 * ordemMinima - 2; j >= ordemMinima; j--) {
            noFilho.chaves.remove(j);
        }
        noFilho.numeroChaves = ordemMinima - 1;

        // Se noFilho não for folha, move a metade superior dos filhos de noFilho para novoNoFilho
        if (!noFilho.folha) {
            // Move os filhos de índice t até 2t-1
            for (int j = ordemMinima; j < 2 * ordemMinima; j++) {
                novoNoFilho.filhos.add(noFilho.filhos.get(j));
            }
            // Remove os filhos movidos de noFilho
            for (int j = 2 * ordemMinima - 1; j >= ordemMinima; j--) {
                noFilho.filhos.remove(j);
            }
        }

        // Insere o novo filho novoNoFilho no nó atual
        filhos.add(indice + 1, novoNoFilho);

        // Move a chave mediana de noFilho para o nó atual
        chaves.add(indice, noFilho.chaves.remove(ordemMinima - 1));
        numeroChaves++;
    }

    public String buscar(String chave) {
        int indice = 0;
        // Encontra a primeira chave maior ou igual a chave
        while (indice < numeroChaves && chave.compareTo(chaves.get(indice)) > 0) {
            indice++;
        }

        // Se a chave for encontrada
        if (indice < numeroChaves && chave.equals(chaves.get(indice))) {
            return chaves.get(indice);
        }

        // Se for folha, a chave não está na árvore
        if (folha) {
            return null;
        } else {
            // Busca no filho apropriado
            return filhos.get(indice).buscar(chave);
        }
    }
}

