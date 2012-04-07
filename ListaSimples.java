package editorgrafico;

/**
 * @author Luan
 */
public class ListaSimples {

    private NoLista primeiro, ultimo, atual, anterior;
    private int tamanho;

    public ListaSimples() {
        primeiro = ultimo = atual = anterior = null;
        tamanho = 0;
    }

    public boolean estaVazia() {
        return primeiro == null;
    }

    public int getTamanho() {
        return tamanho;
    }

    //inclusao
    public void incluirDadoAoFinal(Comparable novoDado) {
        inserirAposFim(new NoLista(novoDado, null));
    }

    public void incluirDadoNoInicio(Comparable novoDado) {
        inserirNoInicio(new NoLista(novoDado, null));
    }

    public boolean incluir(Comparable elemento) {
        NoLista novo = new NoLista(elemento);
        if (this.estaVazia() || this.primeiro.elemento.compareTo(elemento) > 0)
            this.inserirNoInicio(novo);
        else if (this.ultimo.elemento.compareTo(elemento) < 0)
            this.inserirAposFim(novo);
        else if (!existe(elemento)) {
            anterior.prox = novo;
            novo.prox = atual;
            if (atual == null)
                ultimo = novo;
            tamanho++;
        } else
            return false;
        return true;
    }

    //percurso
    public void iniciarPercurso() {
        anterior = null;
        atual = primeiro;
    }

    public boolean podeAvancar() {
        return atual != null;
    }

    public void avancar() {
        if (podeAvancar()) {
            anterior = atual;
            atual = atual.prox;
        }
    }

    public Comparable getAtual() throws Exception {
        if (atual == null)
            throw new Exception("NoPercurseInProgress");
        return atual.elemento;
    }

    public Comparable getAnterior() throws Exception {
        if (atual == null)
            throw new Exception("NoPreviousElement");
        return anterior.elemento;
    }

    public void finalizarPercurso() {
        atual = ultimo;
    }

    public Comparable getPrimeiroElemento() throws Exception {
        if (estaVazia())
            throw new Exception("EmptyList");
        return primeiro.elemento;
    }

    public Comparable getUltimoElemento() throws Exception {
        if (estaVazia())
            throw new Exception("EmptyList");
        return ultimo.elemento;
    }

    public Comparable getElemento(int pos) throws Exception {
        if (pos >= tamanho)
            throw new Exception("InvalidArgumentException");
        int i = 0;
        atual = primeiro;
        while (i < pos) {
            atual = atual.prox;
            i++;
        }
        return atual.elemento;
    }

    //exclusao
    public boolean excluir(Comparable elemento) {
        if (!existe(elemento))
            return false;
        excluirElementoAtual();
        return true;
    }

    public boolean excluir(int pos) throws Exception {
        return excluir(getElemento(pos));
    }

    // outras funções
    public void ordenar() {
        ListaSimples l = new ListaSimples();
        while (!estaVazia()) {
            NoLista menor = primeiro, antMenor = null; // pegar menor
            atual = primeiro;
            anterior = null;
            while (atual != null) {
                if (atual.elemento.compareTo(menor.elemento) < 0) {
                    menor = atual;
                    antMenor = anterior;
                }
                anterior = atual;
                atual = atual.prox;
            }
            l.incluir(menor.elemento);
            excluirElementoAtual();
        }
    }

    public static ListaSimples ordenar(ListaSimples l) {
        return l;
    }

    public boolean existe(Comparable elementoProcurado) {
        atual = primeiro;
        anterior = null;

        if (estaVazia())
            return false; // nesse caso, atual e anterior valem null

        if (primeiro.elemento.compareTo(elementoProcurado) > 0)
            return false; // nesse caso, anterior vale null e atual                                   // aponta o primeiro nó da lista

        if (ultimo.elemento.compareTo(elementoProcurado) < 0) {
            anterior = ultimo;
            atual = null;
            return false;
        }

        boolean achou, fim;
        achou = fim = false;
        while (atual != null && !achou && !fim)
            if (atual.elemento.compareTo(elementoProcurado) == 0)
                achou = true; // achou a chave procurada
            else if (atual.elemento.compareTo(elementoProcurado) > 0)
                fim = true; // achou chave maior que a procurada
            else {
                anterior = atual;
                atual = atual.prox;
            }
        return achou;
    }

    //privativos
    private void inserirNoInicio(NoLista novo) {
        if (estaVazia())
            ultimo = novo;
        novo.prox = primeiro;
        primeiro = novo;
        tamanho++;
    }

    private void inserirAposFim(NoLista novoNo) {
        if (estaVazia())
            primeiro = novoNo;
        else
            ultimo.prox = novoNo;
        ultimo = novoNo;
        tamanho++;
    }

    private void excluirElementoAtual() {
        if (atual == primeiro)
            primeiro = atual.prox;
        else
            anterior.prox = atual.prox;
        if (atual == ultimo)
            ultimo = anterior;
        tamanho--;
    }

    ///////////////////////////////////////////
    //NoLista: Cada elemento da lista ligada.//
    ///////////////////////////////////////////
    private class NoLista {

        private Comparable elemento;
        private NoLista prox;

        public NoLista(Comparable elemento, NoLista prox) {
            this.elemento = elemento;
            this.prox = prox;
        }

        public NoLista(Comparable elemento) {
            this(elemento, null);
        }

        public void setElemento(Comparable elemento) {
            this.elemento = elemento;
        }

        public void setProx(NoLista prox) {
            this.prox = prox;
        }

        public Comparable getElemento() {
            return elemento;
        }

        public NoLista getProx() {
            return prox;
        }
    }
}
