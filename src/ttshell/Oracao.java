package ttshell;

import java.util.LinkedList;
import java.util.List;
import static ttshell.Tipo.*;

/**
 *
 * @author flavio
 */
public class Oracao {

    private boolean verbo;
    private boolean conjuncao;
    private String classe;
    private Tipo tipo; // mudar para enumeracao 1-coordenada 2-subordinada

    private List<ItemLexical> lil;

    public Oracao() {
        lil = new LinkedList<>();
        this.verbo = false;
        this.conjuncao = false;
        this.classe = null;
        this.tipo = NAO_DEFINIDA;
    }

    public void add(ItemLexical il) {
        lil.add(il);
    }

    public ItemLexical remove(int posicao) {
        return lil.remove(posicao);
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public ItemLexical getItemLexical(int posicao) {
        return lil.get(posicao);
    }

    public void setConjuncao(boolean conjuncao) {
        this.conjuncao = conjuncao;
    }

    public boolean isConjuncao() {
        return conjuncao;
    }

    public boolean isVerbo() {
        return verbo;
    }

    public void setVerbo(boolean verbo) {
        this.verbo = verbo;
    }

    public int size() {
        return lil.size();
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
        this.defineTipo();
    }

    public void resetClasse() {
        this.classe = null;
        this.tipo = NAO_DEFINIDA;
    }

    public void defineTipo() {
        if (this.getClasse().equals("O?")) {
            this.setTipo(SUBORDINADA);
        } else {
            this.setTipo(COORDENADA);
        }
    }

    /**
     * Verifica se a oração é uma falsa adjetiva, ou seja, uma oração coodenada
     * explicativa.
     *
     * E.g. Abram-me estas portas, que eu a trarei.
     *
     */
    public boolean isFalsaAdjetiva() {
        int count = 0;
        for (ItemLexical il : lil) {
            if (il.isPronome() || il.isSubstantivo()) {
                count++;
            }
        }
        if (count > 2) { // se a oração possuir mais de dois "nomes", ela será uma coordenada explicativa, ou seja, uma "falsa" adjetiva;
            // Eg. Abram-me estas portas, que eu a trarei.

            return true;
        }
        return false;
    }

    public boolean isOCA() { // Mudar este nome

        // verificando se a oração atual é coordenada
        if (lil.size() < 2) { // Ela é rica, poderia exibir roupas finas, visto que veste-se com simplicidade.
            return false;
        }

        if (lil.get(lil.size() - 2).isVerbo() && lil.get(lil.size() - 1).isVirgula()) {
            return true;
        }
        return false;
    }

    public boolean isSubordinada() { // Mudar este nome

        for (ItemLexical il : lil) {
            if (il.isConjuncaoSubordinada()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {

        String s = "";

        for (ItemLexical il : lil) {

            // eliminando os espaços em branco antes da pontuação
            // E.g. "... chegou , depois..."
            if (il.isPontoFinal() || il.isVirgula()) {
                s = s.substring(0, s.length() - 1);
            }
            s += il.getItemLexical() + " ";
        }

        s += "[" + this.getClasse() + "]";

        return s;

    }

}
