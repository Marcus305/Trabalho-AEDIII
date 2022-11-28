package trabalho.classes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.io.IOException;

public class Lzw {
    private static final int LIM = 1000;
    private int tam_line;
    private int[] tam_col;
    //Declara vetor de bytes para ser o dicionario
    private byte[][] dict = new byte[100000][LIM];

    //Contrutor do dicionario
    Lzw(){
        byte b = -128;
        this.tam_line = 0;
        this.tam_col = new int[10000];
        //Inseri todos os caracteres simples no dicionário
        for(int i = 0; i<256; i++){
            this.dict[i][0]= b;
            b = (byte) ((byte) b+1);
            this.tam_col[this.tam_line]++;
            this.tam_line++;
        }
    }

    //add no fim do dicionário
    void add_dict(ArrayList<Byte> seq){
        Lzw lzw  = new Lzw();
        for(int i=0; i<seq.size();i++){
            lzw.dict[lzw.tam_line][i] = seq.get(i);
            lzw.tam_col[lzw.tam_line]++;
        }
        lzw.tam_line++;
    }

    //Comprimi um vetor de bytes em LZW
    ArrayList<Integer> cod_vet(byte[] vet_ori) throws IOException {
        //Cria um dicionario para o método
        Lzw lzw  = new Lzw();
        //Declaração de variaveis
        int j = 0, indice = -1, p = 0, aux=0, maior_seq=-1;
        ArrayList<Integer> vet_cod = new ArrayList<Integer>();
        ArrayList<Byte> seq = new ArrayList<Byte>();

        //While para percorrer o vetor a ser codificado
        while(p<vet_ori.length){

            //Percorre dicionario até encontrar a maior sequencia
            for(int i=0;i<lzw.tam_line;i++){
                aux=0;
                for(j=0; j<(vet_ori.length-p) && j<=lzw.tam_col[i]; j++){
                    if((vet_ori[p+j])== lzw.dict[i][j]){
                        aux++;
                    }
                }
                if(aux>maior_seq){
                    indice = i;
                    maior_seq = aux;
                }
            }

            // Adicona ao ArrayList "seq" a maior sequencia encontrada
            for(int i=0; i<lzw.tam_col[indice]; i++)
                seq.add(lzw.dict[indice][i]);

            //Adiciona ao vetor "vet_cod" a posição que está a sequencia encontrada
            vet_cod.add(indice);
            p=p+lzw.tam_col[indice];

            //Adiciona ao dicionário a maior sequência encontrada
            if(p<vet_ori.length){
                seq.add((vet_ori[p]));
                lzw.add_dict(seq);
                seq.clear();
                maior_seq=-1;
            }
        }
        return vet_cod;
    }

    //Descompressão do vetor
    ArrayList<Byte> decod_vet(ArrayList<Integer> vet_cod){
        //Cria um dicionário para o método
        Lzw lzw  = new Lzw();

        //Declaração de Arrays
        ArrayList<Byte> seq = new ArrayList<Byte>();
        ArrayList<Byte> vet_decode = new ArrayList<Byte>();

        //Adicionando primeiro caracter no vetor decodificado
        seq.add(lzw.dict[vet_cod.get(0)][0]);
        lzw.add_dict(seq);
        vet_decode.add(lzw.dict[vet_cod.get(0)][0]);
        seq.clear();

        //Adicionando o restante dos dados no dicionário e no vetor
        for(int i=1; i<vet_cod.size();i++){
            lzw.dict[lzw.tam_line-1][lzw.tam_col[lzw.tam_line-1]] = lzw.dict[vet_cod.get(i)][0];
            lzw.tam_col[lzw.tam_line-1]++;
            for(int j=0;j<lzw.tam_col[vet_cod.get(i)];j++){
                seq.add(lzw.dict[vet_cod.get(i)][j]);
                vet_decode.add(lzw.dict[vet_cod.get(i)][j]);
            }
            lzw.add_dict(seq);
            seq.clear();
        }
        return vet_decode;
    }

}