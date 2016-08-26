package ttshell;

import java.util.ArrayList;
import java.util.List;
import static ttshell.Tipo.*;

/**
 *
 * @author flavio
 */
public class Sentenca {

    List<ItemLexical> lil;
    List<Oracao> lo;

    public Sentenca() {
        this.lil = new ArrayList<>();
        this.lo = new ArrayList<>();
    }

    public void add(ItemLexical il) {
        lil.add(il);
    }

    public void identificaOracao() {

        Oracao o = new Oracao();

        int i;

        for (i = 0; i < lil.size(); i++) {

            ItemLexical il = lil.get(i);
            boolean flagAddLexema = true;
            Conjuncao c;
            //System.out.println(o);
            if (il.isVirgula() && o.isVerbo()) {
                //System.out.println(il.getItemLexical());
                o.add(il);
                lo.add(o);
                o = new Oracao();
                flagAddLexema = false;

            } else if ((c = il.isConjuncao()) != null) { // se o retorno for !=  de null, então o il é uma conjunção

                boolean flag_insere = true;

                if (c.getCaso() == 2) { // [nem] e [e] podem ser "falsos" conectivos -> e.g. O homem depende do solo e da flora; deve, pois, preservá-los.
                    if (this.isFalsaConjuncao(i)) {
                        flag_insere = false;
                    } else if (!c.getComplemento().isEmpty()) {
                        // conjunção que exigem complemento => não ... nem
                        // resolver o nada ... nem
                        c.setCaso(3);
                    }
                    //}else if (!( c.getCaso() == 4 && i>0 && lil.get(i).isVirgula())){ //
                    //  flag_insere = false;
                }

                if (flag_insere) {
                    if (o.isVerbo()) {
                        lo.add(o);
                        o = new Oracao();
                    }
                    if( !lo.isEmpty() || c.getTipo() == "OCSt"|| c.getTipo() == "O?" ){ 
                        //System.out.println(c.getConjuncao());
                        // para ser definido um tipo, a oração coordenada sindética não
                        // pode ser a primeira do período (sentença), ou seja,
                        // a lista de orações da sentença não pode ser vazia, caso contrário,
                        // serão classificadas erroneamente orações do tipo do exemplo a seguir:
                        // Cabe ressaltar que o réu é pessoa de boa índole e honesta. Outrossim, cumpre salientar que possui bons antecedentes e jamais participou em qualquer delito
                        
                        // As exceções aqui são somente "OCA" ou orações subordinadas como a do
                        // exemplo a seguir:
                        //Quando era menino, João era pobre, porém era feliz!

                        o.setClasse(c.getTipo());
                        
                    }
                    o.setConjuncao(true);
                }

                if (c.getCaso() == 3 && lo.size() > 0) { // O rio se estreitava, ora se alargava caprichosamente.
                    classificaCasoTres(c, o);
                } else if (c.getCaso() == 4) {
                    System.out.println(o);
                    o.setTipo(COORDENADA_FALSA_ADJETIVA);
                    if (i > 0 && !lil.get(i - 1).isVirgula()) {
                        o.setClasse("O?");
                    }
                } else if (c.getCaso() >= 6) {  // 
                                                // A civilização não se mede pelo aperfeiçoamento material, mas sim pela elevação moral. ==> caso 7
                    if (i + 1 < lil.size()) {
                        HandlerConjuncao hc = new HandlerConjuncao();
                        String locucaoConjuntiva = il.getItemLexical() + " " + lil.get(i + 1).getItemLexical();
                        c = hc.reconhece(locucaoConjuntiva);
                        if (c != null) {
                            o.setClasse(c.getTipo());

                            { // tudo isso devido a locuções do tipo ...que, por o termo <que> pode desfazer
                                // esse procedimento no próximo laço!
                                flagAddLexema = false;

                                // Adicionando a locução completa para dentro da oração
                                o.add(il);
                                o.add(lil.get(i + 1));
                                i++;
                            }
                            if( c.getCaso() == 7){
                                // caso 7 = A civilização não se mede pelo aperfeiçoamento material, mas sim pela elevação moral. ==> caso 7
                                classificaCasoTres(c, o);
                                if( !o.getClasse().equals("O?")){
                                    o.setVerbo(true);
                                }
                            }else{ 
                                //Ele não só estuda, mas também trabalha. ==> caso 6
                                classificaCasoSeis(c, o);
                            }
                        }
                    }
                }
                // pode ser uma locução conjuntiva
                // E.g. Ela é rica, poderia exibir roupas finas, no entanto veste-se com simplicidade.
                // Contempla as locuções (no entanto, por conseguinte
            } else if (il.isPreposicao() || // no entanto, por isso, por conseguinte
                    il.isAdverbio() || // não obstante, bem como           
                    il.isConjuncaoCoordenada()) // apesar disso
            {

                // locuçao com tres elementos (de modo que, em todo caso, ... )
                if (i + 2 < lil.size()) {
                    //System.out.println("==>"+il.getItemLexical());
                    HandlerConjuncao hc = new HandlerConjuncao();
                    String locucaoConjuntiva = il.getItemLexical() + " " + lil.get(i + 1).getItemLexical() + " " + lil.get(i + 2).getItemLexical();
                    c = hc.reconhece(locucaoConjuntiva);
                    if (c != null) {
                        if (o.isVerbo()) {
                            lo.add(o);
                            o = new Oracao();
                        }
                        o.setConjuncao(true);
                        o.setClasse(c.getTipo());

                        { // tudo isso devido a locuções do tipo ...que, por o termo <que> pode desfazer
                            // esse procedimento no próximo laço!
                            flagAddLexema = false;

                            // Adicionando a locução completa para dentro da oração
                            o.add(il);
                            o.add(lil.get(i + 1));
                            o.add(lil.get(i + 2));
                            i += 2;
                        }

                    } else {
                        // locuçao com dois elementos (não obstante, assim como,... )
                        locucaoConjuntiva = il.getItemLexical() + " " + lil.get(i + 1).getItemLexical();
                        c = hc.reconhece(locucaoConjuntiva);
                        if (c != null) {
                            if (o.isVerbo()) {
                                lo.add(o);
                                o = new Oracao();
                            }
                            o.setConjuncao(true);
                            o.setClasse(c.getTipo());

                            { // tudo isso devido a locuções do tipo ...que, por o termo <que> pode desfazer
                                // esse procedimento no próximo laço!
                                flagAddLexema = false;

                                // Adicionando a locução completa para dentro da oração
                                o.add(il);
                                o.add(lil.get(i + 1));
                                i++;
                            }

                        }
                    }
                }

            } else if (il.isPontoFinal() && !o.isVerbo()) { // A civilização não se mede pelo aperfeiçoamento material, mas sim pela elevação moral.
                // Criada por mim => Os elefantes são animais muito grandes, porém têm medo, medo de ratos.  
                o = reverteOracao(o);
            } else if (il.isVerbo()) {
                   //System.out.println(il);
                // Ela é rica, poderia exibir roupas finas, visto que veste-se com simplicidade.
                //VERIFICAR MELHOR ESTA PARTE DO CÓDIGO
                if (i + 1 < lil.size()) {
                    HandlerConjuncao hc = new HandlerConjuncao();
                    String locucaoConjuntiva = il.getItemLexical() + " " + lil.get(i + 1).getItemLexical();
                    c = hc.reconhece(locucaoConjuntiva);
                    if (c != null) {
                        if (o.isVerbo()) {
                            lo.add(o);
                            o = new Oracao();
                        }
                        o.setConjuncao(true);
                        o.setClasse(c.getTipo());

                        { // tudo isso devido a locuções do tipo ...que, por o termo <que> pode desfazer
                            // esse procedimento no próximo laço!
                            flagAddLexema = false;

                            // Adicionando a locução completa para dentro da oração
                            o.add(il);
                            o.add(lil.get(i + 1));
                            i++;
                        }
                    }
                }

                if (flagAddLexema) {
                    //VERIFICAR MELHOR ESTA PARTE DO CÓDIGO
                    //System.out.println(il);
                    o.setVerbo(true);
                }
            } else if (il.isArtigoIndefinido()) {
                //VERIFICAR MELHOR ESTA PARTE DO CÓDIGO ****REPETIÇÃO***
                if (i + 2 < lil.size()) {
                    // Ela é rica, uma vez que veste-se com simplicidade.
                    HandlerConjuncao hc = new HandlerConjuncao();
                    String locucaoConjuntiva = il.getItemLexical() + " " + lil.get(i + 1).getItemLexical() + " " + lil.get(i + 2).getItemLexical();
                    c = hc.reconhece(locucaoConjuntiva);
                    if (c != null) {
                        if (o.isVerbo()) {
                            lo.add(o);
                            o = new Oracao();
                        }
                        o.setConjuncao(true);
                        o.setClasse(c.getTipo());

                        { // tudo isso devido a locuções do tipo ...que, por o termo <que> pode desfazer
                            // esse procedimento no próximo laço!
                            flagAddLexema = false;

                            // Adicionando a locução completa para dentro da oração
                            o.add(il);
                            o.add(lil.get(i + 1));
                            o.add(lil.get(i + 2));
                            i += 2;
                        }
                    }
                }
            }
            if (flagAddLexema) {
                o.add(il);
            }
        }
        lo.add(o);
    }

    /**
     * Adiciona os itens lexicais restantes da senteça (oração candidata) à
     * última oração adicionada na lista de orações
     *
     * @param o a candidata a oração, ou seja, os itens lexicais restantes dela.
     * @return aux: a oração, agora, completa.
     *
     */
    public Oracao reverteOracao(Oracao o) {

        Conjuncao c;

        if (lo.isEmpty()) { // se a lista de orações estiver vazia, então não há em que adicionar!
            return o;
        }
        Oracao aux = lo.remove(lo.size() - 1);

        // se a oração provinda da lista (aux) não contiver conjunção, mas a pseudo-oração contiver
        // então ela é uma oração conclusiva, já que é a única que possui a conjunção após o verbo
        // e.g. O homem depende do solo e da flora; deve, pois, preservá-los.
        if (!aux.isConjuncao() && o.isConjuncao()) {
            aux.setConjuncao(true);
            aux.setClasse("OCSc");
        }

        for (int i = 0; i < o.size(); i++) {
            aux.add(o.getItemLexical(i));
        }
        return aux;
    }

    /**
     * Verifica se a conjunção realmente liga duas orações, ou outros tipos de
     * elementos
     *
     * E.g. Os operários reclamam e exigem explicações. (true); O homem depende
     * do solo e da flora. (false)
     *
     * @param i o índice corrente do item lexical.
     * @return a confirmação ou não de que a conjunção é verdadeira.
     *
     */
    public boolean isFalsaConjuncao(int i) {

        if (i == 0) {
            // Nem joão nem Maria foram ao desfile.
            return false;
        }
        ItemLexical ilAnterior = lil.get(i - 1);
        ItemLexical ilPosterior = lil.get(i + 1);

        if ((ilAnterior.isAdjetivo() // E.g. Carlos e André / Nem maria nem João / etc...
                                     // O homem depende do solo e da flora; deve, pois, preservá-los.
                || ilAnterior.isDeterminante()
                || ilAnterior.isPreposicao()
                || ilAnterior.isPronome()
                || ilAnterior.isSubstantivo())
                && (ilPosterior.isAdjetivo()
                || ilPosterior.isDeterminante()
                || ilPosterior.isPreposicao()
                || ( ilPosterior.isPronome() &&
                     !(ilPosterior.getItemLexical().toLowerCase().equals("a")) &&
                     !(ilPosterior.getItemLexical().toLowerCase().equals("o")) )
                     // Ele não concordou com a menina e a deixou.
                || ilPosterior.isSubstantivo())) {
                
            return true;
        }
        return false;
    }

    /**
     * Verifica se há, na oração anterior à oração corrente, o complemento da
     * conjunção alternativa
     *
     * E.g. O rio ora se estreitava, ora se alargava caprichosamente.
     *
     * @param Conjucao c : a conjunção que contém o conectivo e seu respectivo
     * complemento.
     * @param Oração corr : a oração corrente.
     *
     */
    public void classificaCasoTres(Conjuncao c, Oracao corr) {
        Oracao o = lo.get(lo.size() - 1);
        for (int i = 0; i < o.size(); i++) {
            String lexema = o.getItemLexical(i).getItemLexical();
            if (c.getComplemento().equals(lexema.toLowerCase())) {  // verifica se encontra a conjunção (ora...ora, quer...quer) na oração anterior
                return; // ok => oração completa Eg. O rio ora se estreitava, ora se alargava caprichosamente.
            }
        }
        //  => oração INcompleta Eg. O rio se estreitava, ora se alargava caprichosamente.
        o.setClasse("O?");
        corr.setClasse("O?");
    }

    public void classificaCasoSeis(Conjuncao c, Oracao corr) {
        Oracao o = lo.get(lo.size() - 1);
        String[] complemento = c.getComplemento().split(" ");
        for (int i = 0; i < o.size(); i++) {
            String lexema = o.getItemLexical(i).getItemLexical();
            if (complemento[0].equals(lexema)) {  // verifica se encontra a conjunção (ora...ora, quer...quer) na oração anterior
                if (i + 1 < o.size()) {
                    lexema = o.getItemLexical(i + 1).getItemLexical();
                    for (int j = 1; j < complemento.length; j++) {
                        if (complemento[j].equals(lexema)) { // verifica se encontra a conjunção (ora...ora, quer...quer) na oração anterior
                            return; // ok => oração completa Eg. O rio ora se estreitava, ora se alargava caprichosamente.
                        }
                    }
                }
            }
        }
        //  => oração INcompleta Eg. O rio se estreitava, ora se alargava caprichosamente.
        o.setClasse("O?");
        corr.setClasse("O?");
    }

    public void classificaOracao() {

        boolean flagTemOracaoCoordenada = false;
        boolean flagTemOracaoSubordinada = false;

        // Verificando se a oração é absoluta, ou seja, a sentença possui uma única oração.
        if (lo.size() == 1) { // oracao absoluta  => E.g. O homem é um ser racional.
            lo.get(0).setClasse("O?");
            return;
        }

        // Setando os flagues de orações        
        for (int i = 0; i < lo.size(); i++) {
            Oracao o = lo.get(i);

            if (o.getTipo() == COORDENADA) {
                //System.out.println("coordenada");
                flagTemOracaoCoordenada = true;
            } else if (o.getTipo() == SUBORDINADA) {
                flagTemOracaoSubordinada = true;
                //System.out.println("subordinada");
            } else if (o.getTipo() == COORDENADA_FALSA_ADJETIVA) {
                if (o.isFalsaAdjetiva()) { // Eg. Abram-me estas portas, que eu a trarei. 
                    // = Olhou a caatiga amarela, que o poente avermelhava.
                    flagTemOracaoCoordenada = true;
                } else if (i > 0 && lo.get(i - 1).isOCA() && !o.isSubordinada()) { // Eg.Não te queixes, que há outros mais infelizes.
                    flagTemOracaoCoordenada = true;
                } else { // A oração não é coordenada!
                    //flagTemOracaoSubordinada = true;
                    o.setClasse("O?");
                    if (i > 0) {
                        String classe = lo.get(i - 1).getClasse();
                        if (classe == null || classe.equals("OCA") || i == 1) {
                            lo.get(i - 1).setClasse("O?");
                        }
                    }
                }
            }
        }

        // Somente oracoes coordenadas
        if (!flagTemOracaoSubordinada) { // Eg.Não tinha experiência, mas boa vontade não lhe faltava.
            for (int i = 0; i < lo.size(); i++) {
                Oracao o = lo.get(i);
                if (o.getClasse() == null) {
                    o.setClasse("OCA");
                    //} else if ((o.getClasse() == "O?") && (lo.size() > i + 1 )&& lo.get(i + 1).getClasse() != null && (lo.get(i + 1)).getClasse().charAt(1) == 'C') {
                    //} else if ( (lo.size() > i + 1) && lo.get(i + 1).getClasse() != null && (lo.get(i + 1)).getClasse().charAt(1) == 'C') {
                    //  o.setClasse("OCA");
                    //Eu Sabia que ela vinha, mas não esperava que fosse hoje.
                }
            }

        }

        // Somente oracoes subordinadas
        if (!flagTemOracaoCoordenada) { //Eg."Eles foram à festa, embora não quisessem ir.";
            for (Oracao o : lo) {
                if (o.getClasse() == null) {
                    o.setClasse("O?");
                }
            }
        }

        // orações mistas
        if (flagTemOracaoCoordenada && flagTemOracaoSubordinada) {

            for (int i = 0; i < lo.size(); i++) {
                Oracao o = lo.get(i);
                if (o.getClasse() == null) {
                    if (lo.size() > i + 1 && lo.get(i + 1).getClasse() != null && (lo.get(i + 1)).getClasse().charAt(1) == 'C') {
                        o.setClasse("OCA");
                    } else {
                        o.setClasse("O?");
                    }
                }
            }
        }

        // Em qualquer caso??? Revisar
        for (int i = 0; i < lo.size(); i++) {
            Oracao o = lo.get(i);
            if ((o.getClasse() == "O?") && 
                    lo.size() > i + 1 && 
                    lo.get(i + 1).getClasse() != null && 
                    lo.get(i + 1).getClasse() != "O?" && 
                    lo.get(i + 1).getClasse().charAt(2) == 'S') {
                //Eu Sabia que ela vinha, mas não esperava que fosse hoje.
                o.setClasse("OCA");
            }
            
            if (o.getClasse() == "O?" && 
                    !o.isConjuncao()  &&
                    lo.size() > i + 1 && 
                    !lo.get(i + 1).isConjuncao()) {
                //Horácio a amava sem dúvida: já lhe tinha dado provas de que sentia uma paixão veemente.

                o.setClasse("OCA");
            }
            
        }

    }

    @Override
    public String toString() {

        String s = "";

        for (Oracao o : lo) {
            s += o + "\n";
        }

        return s;
    }
}
