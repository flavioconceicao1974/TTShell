package ttshell;

/**
 *
 * @author flavio
 */
public class ItemLexical {

    private String lexema;
    private String classeGramatical;

    public ItemLexical(String lexema, String classeGramatical) {
        this.lexema = lexema;
        this.classeGramatical = classeGramatical;
    }

    public String getItemLexical() {
        return lexema;
    }

    public void setItemLexical(String lexema) {
        this.lexema = lexema;
    }

    public String getClasseGramatical() {
        return classeGramatical;
    }

    public void setClasseGramatical(String classeGramatical) {
        this.classeGramatical = classeGramatical;
    }

    public boolean isPontoFinal() {

        if ((this.getClasseGramatical().equals("Fp")) || // ponto final.
                (this.getClasseGramatical().equals("Fat")) || // ponto de exclamação! 
                (this.getClasseGramatical().equals("Fit")) // ponto de interrogação! 
                ) {
            return true;
        }
        return false;
    }

    /**
     * Função especial. Retorna o objeto conjuncao correspondente ao Item
     * Lexical ou NULL caso o Item Lexical não for uma conjunção!
     */
    public Conjuncao isConjuncao() {

        HandlerConjuncao hc = new HandlerConjuncao();

        switch (this.getClasseGramatical().charAt(0)) {
            case 'C': //conjunção
                return hc.reconhece(this.getItemLexical());
            case 'R': // advérbio (às vezes, o TreeTagger classifica conjunções como advérbios!) 
                // Eg. As horas passam destarte os homens caem. (Destarte)
                // Eg. Voce vai me ajudar quando chegar em casa.(Quando)
                // Eg. O Grêmio perdeu o grenal, assim o Inter é o novo campeão! (assim)
                // Eg. O Grêmio perdeu o grenal, todavia o Inter é o novo campeão! (Todavia)
                return hc.reconhece(this.getItemLexical());
            case 'P': // pronome relativo Eg. Não te queixes, que há outros mais infelizes.
                if (this.getClasseGramatical().equals("PR0")) {
                    return hc.reconhece(this.getItemLexical());
                }
                break;
            case 'N': // substantivo ( corrigindo erro do TreeTagger)
                if (this.getClasseGramatical().equals("NP0")) {
                    return hc.reconhece(this.getItemLexical());
                }
                break;
        }
        return null;
    }

    public boolean isVerbo() {
        if ((this.getClasseGramatical().charAt(0)) == 'V') {
            return true;
        }
        return false;
    }

    public boolean isVirgula() {

        if ((this.getClasseGramatical().equals("Fc")) || // vírgula.
                (this.getClasseGramatical().equals("Fd")) || // dois pontos.    
                (this.getClasseGramatical().equals("Fx"))) {  // ponto-e-vírgula.

            return true;
        }
        return false;
    }

    public boolean isPronomeRelativo() {

        if (this.getClasseGramatical().equals("PR0")) {
            return true;
        }
        return false;
    }

    public boolean isAdjetivo() {
        if ((this.getClasseGramatical().charAt(0)) == 'A') {
            return true;
        }
        return false;
    }

    public boolean isAdverbio() {
        if ((this.getClasseGramatical().charAt(0)) == 'R') {
            return true;
        }
        return false;
    }

    public boolean isDeterminante() {
        if ((this.getClasseGramatical().charAt(0)) == 'D') {
            return true;
        }
        return false;
    }

    public boolean isSubstantivo() {
        if ((this.getClasseGramatical().charAt(0)) == 'N') {
            return true;
        }
        return false;
    }

    public boolean isPronome() {
        if ((this.getClasseGramatical().charAt(0)) == 'P') {
            return true;
        }
        return false;
    }

    public boolean isPreposicao() {
        if ((this.getClasseGramatical().charAt(0)) == 'S') {
            return true;
        }
        return false;
    }

    public boolean isConjuncaoSubordinada() {

        if (this.getClasseGramatical().equals("CS")) {
            return true;
        }
        return false;
    }

    public boolean isConjuncaoCoordenada() {

        if (this.getClasseGramatical().equals("CC")) {
            return true;
        }
        return false;
    }
    
    
    @Override
    public String toString() {
        return "ItemLexical : " + lexema + " - " + classeGramatical;
    }

    boolean isArtigoIndefinido() {
        return this.getClasseGramatical().equals("DI0");
    }

}
