
package Pronto;

import java.util.LinkedList;
import java.util.List;
import ttshell.ItemLexical;
import ttshell.Sentenca;

/**
 *
 * @author flavio
 */
public class IdentificaSentenca {
    private List<Sentenca> ls = new LinkedList<>();
    
    public List<Sentenca> identificaSentenca( List<ItemLexical> lil ){
        
        Sentenca sentenca = new Sentenca();
        
        for (ItemLexical il : lil) {
            
            sentenca.add(il);
            if( il.isPontoFinal() ){
                ls.add(sentenca);
                sentenca = new Sentenca();
            }
        }
        return ls;
    }
}
