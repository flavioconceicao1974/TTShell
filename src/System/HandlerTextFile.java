package System;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import ttshell.ItemLexical;

/**
 *
 * @author flavio
 */
public class HandlerTextFile {

    public List<ItemLexical> leArquivo(String nomeArq) {
        
        List<ItemLexical> lil = new LinkedList<>();

        try {
            // Abre o arquivo
            FileReader fr = new FileReader(nomeArq);
            BufferedReader br = new BufferedReader(fr);
            try {
                // Le as informacoes do arquivo (apenas uma linha)
                String line = br.readLine();
                while (line != null) {
                    String[] str = line.split("\\t"); // usando o <TAB> como delimitador
                    ItemLexical il = new ItemLexical(str[0],str[1]);
                    lil.add(il);                  
                    line = br.readLine();
                }

            } finally {
                // Fecha o arquivo
                fr.close();
            }
        } catch (IOException e) {
            return lil;
        }
        return lil;
    }
}