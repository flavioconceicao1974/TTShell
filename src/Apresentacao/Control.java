package Apresentacao;

import java.io.IOException;
import java.util.List;
import System.HandlerTextFile;
import Pronto.IdentificaSentenca;
import ttshell.ItemLexical;
import System.LocalShell;
import java.util.LinkedList;
import ttshell.Sentenca;

public class Control {

    public String classifica(String txt) throws IOException {

        final LocalShell shell = new LocalShell();
        String retorno = "";

        // formatando o texto original para uso no POS e no textToList
        String texto = this.formateText(txt);

        // Pré-processamento do texto (1.etiquetando o texto com o TreeTagger)
        String textoPreProcess = this.preProcessText(texto);    // última formatação antes do POS tagger
        shell.executeCommand("sh scr.sh '" + textoPreProcess + "'"); // Acionando o shell que executa o POS tagger
        HandlerTextFile tf = new HandlerTextFile();             // 2.criação da Lista de Itens do TreeTagger   
        List<ItemLexical> l1 = tf.leArquivo("saida.txt");      // 2.criação da Lista de Itens do TreeTagger   

        //List<ItemLexical> l2 = textToList(texto);      // 2.Criação de lista auxiliar
        List<ItemLexical> l2 = textToList(textoPreProcess);      // 2.Criação de lista auxiliar

        //List<ItemLexical> lil = l1;
        List<ItemLexical> lil = desNormaliza(l1, l2);  // Lista definitiva

        // Identificação de sentenças
        IdentificaSentenca is = new IdentificaSentenca();
        List<Sentenca> ls = is.identificaSentenca(lil);

        // Identificação de orações 
        for (Sentenca s : ls) {
            s.identificaOracao(); //identificando as orações de cada sentença
        }

        // Classificação de orações 
        for (Sentenca s : ls) {
            s.classificaOracao();
        }

        // preparando o texto de retorno com as orações já classificadas
        for (Sentenca s : ls) {
            retorno += s;
        }

        return retorno;
    }

    /**
     * Elimina espaços em branco nas extremidades e no centro do texto, e
     * adiciona ponto final, caso o texto não possua um.
     *
     * @param txt String - o texto a ser formatado.
     * @return String txt - o texto já formatado.
     *
     */
    public String formateText(String txt) {

        String texto = txt;
        texto = texto.trim();
        char c = texto.charAt(texto.length() - 1);
        
        
        if (!isPontuacao(c)) {
            texto += ".";
        }
        
        // Eliminiando pontos à esquerda do texto, por erro do usuário.
        c = texto.charAt(0);
        while( isPontuacao(c) ){
            texto = texto.substring(1, texto.length());
            c = texto.charAt(0);
        }    
        
        // Eliminiando pontos à direita do texto, por erro do usuário.
        while( texto.length() > 2 && texto.charAt(texto.length()-2) == '.'){
            texto = texto.substring(0, texto.length()-2);
        }
        
        // Eliminando excesso de espaços em brancos. E.g "Ontem   ele   foi"
        // pois sem esse procedimento a função textToList()
        // criará itens lexicais com espaços em branco " "
        String retorno = "";
        for (int i = 0; i < texto.length(); i++) {
            c = texto.charAt(i);
            if ((c == ' ') && (i > 0) && (texto.charAt(i - 1) == ' ')) {
            } else {
                retorno += c;
            }
        }

        return retorno;
    }

    /**
     * inclui espaços em branco após pontuações, caso o texto original não os
     * possua.
     * <br><br>
     * <b>Eg.</b>"Carlos não queria,mas foi à festa de formatura."
     * <br> => "Carlos não queria, mas foi à festa de formatura."
     *
     * @param txt String - o texto a ser formatado.
     * @return String txt - o texto já formatado.
     *
     */
    private String preProcessText(String txt) {
        String texto = "";

        // incluindo espaço em branco após pontos finais, ou seja, reparando o erro do tipo: 
        //" ...e chegou em casa.Maria ..." 
        // =>  " ...e chegou em casa. Maria ..."                
        // pois, se isso não for feito, o POS Tagger vai etiquetar "casa.Maria" como um único lexema!
        for (int i = 0; i < txt.length(); i++) {
            char c = txt.charAt(i);
            texto += c;
            if ((c == ',' || c == '.' || c == ';' || c == ':' || c == '!' || c == '?') && (((i + 1) < txt.length()) && (txt.charAt(i + 1) != ' '))) {
                texto += " ";
            }
        }
        //System.out.println(texto);
        return texto;
    }

    /**
     * Cria uma lista com os itens lexicais originais do texto antes de eles
     * passarem pelo POS tagger, preservando assim crases e outras contrações de
     * dois lexemas.
     * <br><br>
     * <b>Eg.</b> no=em+o naquilo=em+aquilo dele=de+ele.
     *
     *
     * @param texto String - o texto a ser formatado.
     * @return List retorno - o texto já formatado.
     *
     */
    private List<ItemLexical> textToList(String texto) {

        String retorno = "";
        List<ItemLexical> lil = new LinkedList<>();
        ItemLexical il = new ItemLexical("", "");
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);

            if (caracter == ' ') {

                retorno.replace("\t", "");
                il = new ItemLexical(retorno.trim(), "");
                lil.add(il);
                retorno = "";
            } else if (isPausa(caracter)) {
                retorno.replace("\t", "");
                il = new ItemLexical(retorno.trim(), "");
                lil.add(il);
                retorno = "" + caracter;
            } else if (isPontuacao(caracter)) {
                retorno.replace("\t", "");
                il = new ItemLexical(retorno.trim(), "");
                lil.add(il);
                retorno = "" + caracter;
                retorno.replace("\t", "");
                il = new ItemLexical(retorno.trim(), "");
                lil.add(il);
                retorno = "";
            } else {
                retorno += caracter;
            }
        }

        // removendo itens vazios    
        for (int i = 0; i < lil.size(); i++) {

            if (lil.get(i).getItemLexical().isEmpty()) {
                lil.remove(i);
            }

        }
        return lil;
    }

    private boolean isPausa(char c) {
        if (c == ',' || c == ';' || c == ':') {
            return true;
        }
        return false;
    }

    private boolean isPontuacao(char c) {
        if (c == '.' || c == '!' || c == '?') {
            return true;
        }
        return false;
    }

    private List<ItemLexical> desNormaliza(List<ItemLexical> l1, List<ItemLexical> l2) {

        // L1 lista do TreeTagger
        // L2 lista original
        List<ItemLexical> lil = new LinkedList<>();

        for (int i = 0; i < l1.size(); i++) {
            String lexemaL1 = l1.get(i).getItemLexical();
            String lexemaL2 = l2.get(i).getItemLexical();
            //System.out.println("->" + lexemaL1);
            if (!lexemaL1.equals(lexemaL2)) {
                l1.get(i).setItemLexical(lexemaL2);
                //System.out.println("#" + lexemaL1 + ".." + lexemaL2);

                if (l1.size() > i + 1) {
                    l1.remove(i + 1); //<<======== atenção para o erro
                }
            }
            lil.add(l1.get(i));
        }
        return lil;
    }

}
