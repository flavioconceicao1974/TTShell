package ttshell;

public class Conjuncao {

    private String conjuncao, complemento;
    private String tipo;
    private int caso; // 1.Normal; 
                      // 2.Falsos conectivos [e] [nem]; 
                      // 3.ora...ora, seja...seja; 
                      // 4.que (=porque) 
                      // 5. Locução conjuntiva (no entanto) 
                      // 6.não só...mas também; 
                      // 7.não...mas sim; // A civilização não se mede pelo aperfeiçoamento material, mas sim pela elevação moral.
    

    public Conjuncao(String conjuncao, String tipo) {
        this.conjuncao = conjuncao;
        this.tipo = tipo;
        this.caso = 1;
        this.complemento = "";
    }

    public String getConjuncao() {
        return conjuncao;
    }

    public void setConjuncao(String conjuncao) {
        this.conjuncao = conjuncao;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCaso() {
        return caso;
    }

    public void setCaso(int caso) {
        this.caso = caso;
    }
}
