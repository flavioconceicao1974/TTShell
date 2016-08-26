package ttshell;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author flavio
 */
public class HandlerConjuncao {

    private List<Conjuncao> lc;

    public HandlerConjuncao() {
        this.lc = new LinkedList<>();
        popula();
    }

    public void popula() {

        Conjuncao c;

        c = new Conjuncao("e", "OCSd");
        c.setCaso(2);
        lc.add(c);
        
        c = new Conjuncao("nem", "OCSd");
        c.setComplemento("não");
        c.setCaso(2);
        lc.add(c);

        c = new Conjuncao("tampouco", "OCSd");
        c.setCaso(3);
        c.setComplemento("não");
        lc.add(c);
        
        
        lc.add(new Conjuncao("outrossim", "OCSd"));
        // Cabe ressaltar que o réu é pessoa de boa índole e honesta, Outrossim, cumpre salientar que possui bons antecedentes e jamais participou em qualquer delito
        
        c = new Conjuncao("mas", "OCSv");
        c.setCaso(6);
        lc.add(c);
        
        c = new Conjuncao("senão", "OCSv");
        c.setCaso(6);
        lc.add(c);
             
        

        c = new Conjuncao("como", "O?");
        c.setCaso(6);
        lc.add(c);

        c = new Conjuncao("mas também", "OCSd");
        c.setComplemento("não só somente apenas");
        lc.add(c);

        c = new Conjuncao("mas ainda", "OCSd");
        c.setComplemento("não só somente apenas");
        lc.add(c);

        c = new Conjuncao("como também", "OCSd");
        c.setComplemento("não só somente apenas");
        lc.add(c);
        
        c = new Conjuncao("senão também", "OCSd");
        c.setComplemento("não só somente apenas");
        lc.add(c);
        
        c = new Conjuncao("mas sim", "OCSv");
        c.setComplemento("não");
        c.setCaso(7); 
        lc.add(c);
        

        lc.add(new Conjuncao("entretanto", "OCSv"));
        lc.add(new Conjuncao("porém", "OCSv"));
        lc.add(new Conjuncao("todavia", "OCSv"));
        lc.add(new Conjuncao("contudo", "OCSv"));

        lc.add(new Conjuncao("ou", "OCSt"));

        c = new Conjuncao("ora", "OCSt");
        c.setCaso(3);
        c.setComplemento("ora");
        lc.add(c);
        
        c = new Conjuncao("antes", "OCSv");
        c.setCaso(3);
        c.setComplemento("não");
        lc.add(c);
        
        lc.add(new Conjuncao("portanto", "OCSc"));
        lc.add(new Conjuncao("logo", "OCSc"));
        lc.add(new Conjuncao("assim", "OCSc"));
        lc.add(new Conjuncao("consequentemente", "OCSc"));
        lc.add(new Conjuncao("destarte", "OCSc"));
        lc.add(new Conjuncao("então", "OCSc"));

        lc.add(new Conjuncao("porque", "OCSe"));
        lc.add(new Conjuncao("porquanto", "OCSe"));
        lc.add(new Conjuncao("pois", "OCSe"));

        c = new Conjuncao("que", "OCSe");
        c.setCaso(4);
        lc.add(c);

        //  *------------------------------
        //  *   Locuções conjuntivas (1 ordem )
        //  *------------------------------
        //introduzidas por preposições
        lc.add(new Conjuncao("no entanto", "OCSv"));
        lc.add(new Conjuncao("sem embargo", "OCSv"));

        lc.add(new Conjuncao("por conseguinte", "OCSc"));
        lc.add(new Conjuncao("por consequência", "OCSc"));
        lc.add(new Conjuncao("por consequencia", "OCSc"));
        lc.add(new Conjuncao("por isso", "OCSc"));

        //introduzidas por advérbios
        lc.add(new Conjuncao("bem como", "OCSd"));

        lc.add(new Conjuncao("não obstante", "OCSv"));

        lc.add(new Conjuncao("já que", "OCSe"));    //==> RESOLVER, TEM SOMENTE NO LIVRO DA VESTCOM

        //introduzidas por conjunções
        lc.add(new Conjuncao("apesar disso", "OCSv"));

        //introduzidas por outros elementos
        lc.add(new Conjuncao("visto que", "OCSe")); //==> RESOLVER!!

        //  *------------------------------
        //  *   Locuções conjuntivas (2 ordem ) ==> RESOLVER!!
        //  *------------------------------
        lc.add(new Conjuncao("ao passo que", "OCSv"));
        lc.add(new Conjuncao("em todo caso", "OCSv"));

        //lc.add(new Conjuncao("de modo que", "OCSe")); // =(que) ?????
        //lc.add(new Conjuncao("de sorte que", "OCSe"));
        lc.add(new Conjuncao("de maneira que", "OCSe"));
        lc.add(new Conjuncao("uma vez que", "OCSe"));
        lc.add(new Conjuncao("em virtude de", "OCSe"));

        //  *------------------------------
        //  *   Conjunções subordinativas
        //  *------------------------------
        lc.add(new Conjuncao("embora", "O?"));
        lc.add(new Conjuncao("quando", "O?"));
        lc.add(new Conjuncao("que", "O?"));

    }

    public Conjuncao reconhece(String conjuncao) {

        for (Conjuncao c : lc) {
            if (c.getConjuncao().equals(conjuncao.toLowerCase())) { // trabalhando com a função LowerCase para evitar problemas de case sensitive

                return c;
            }
        }
        return null;
    }

}
