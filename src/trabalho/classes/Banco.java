package trabalho.classes;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

//Classe para manipular as contas de maneira indexada
public class Banco {

    public int gerarId() {
        int lastId;
        try (RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/banco_de_dados.txt", "r")) {
            try {
                lastId = raf.readInt();
            } catch (Exception e) {
                lastId = -1;
            }
            if (lastId == -1) {
                return 1;
            }
        } catch (Exception e) {
            return 1;
        }
        return lastId + 1;
    }

    //Verifica se já possui um determinado nome de usuário no banco
    public boolean checkUser(String test) {
        try (RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/banco_de_dados.txt", "r")) {
            raf.readInt(); //last id
            do {
                byte lapide = raf.readByte();
                int tam = raf.readInt();
                byte[] bytes = new byte[tam];
                raf.read(bytes);
                if (lapide != 1) {
                    Conta c = new Conta(bytes);
                    if (test.equals(c.getNomeUsuario()))
                        return true;
                }
            } while (raf.getFilePointer() != raf.length());
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public Conta criarConta() {
        Conta conta = new Conta(this.gerarId());
        Scanner sc = new Scanner(System.in);
        System.out.println("Informe o seu nome:");
        conta.setNomePessoa(sc.nextLine());
        int op;
        do {
            System.out.println("Informe quantos e-mails deseja cadastrar:");
            op = sc.nextInt();
            if (op < 0)
                System.out.println("Digite um valor válido!");
        } while (op == 0);
        String[] emails = new String[op];
        sc.nextLine();
        for (int i = 0; i < op; i++) {
            System.out.println("Digite o e-mail " + (i + 1) + ":");
            emails[i] = sc.nextLine();
        }
        conta.setEmail(emails);
        boolean check = false;
        do {
            System.out.println("Digite seu nome de usuário:");
            String user = sc.nextLine();
            if (checkUser(user)) {
                System.out.println("Nome de usuário indisponível");
            } else {
                check = true;
                conta.setNomeUsuario(user);
            }
        } while (!check);
        System.out.println("Digite o nome de sua cidade:");
        conta.setCidade(sc.nextLine());
        System.out.println("Digite seu CPF:");
        conta.setCpf(sc.nextLine());
        System.out.println("Digite sua senha:");
        conta.setSenha(sc.nextLine());
        System.out.println("Digite seu saldo:");
        conta.setSaldoConta(sc.nextFloat());
        return conta;
    }

    //Método somente para checar contas na memória secundária
    public void imprimir() {
        //File path = new File("src/trabalho/main/output/banco_de_dados.txt");
        try (RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/banco_de_dados.txt", "r")) {
            System.out.println("---------------------------------");
            raf.readInt(); //last id
            int i = 1;
            do {
                byte lapide = raf.readByte();
                int tam = raf.readInt();
                byte[] bytes = new byte[tam];
                raf.read(bytes);
                if (lapide != 1) {
                    Conta c = new Conta(bytes);
                    System.out.println("Conta " + i + ":");
                    System.out.println("Id: " + c.getIdConta());
                    System.out.println("Nome: " + c.getNomePessoa());
                    for (int j = 0; j < c.getEmail().length; j++) {
                        System.out.println("Email " + (j + 1) + ": " + c.getEmail()[j]);
                    }
                    System.out.println("Usuário: " + c.getNomeUsuario());
                    System.out.println("Senha: " + c.getSenha());
                    System.out.println("Cidade: " + c.getCidade());
                    System.out.println("CPF: " + c.getCpf());
                    System.out.println("Transferências realizadas: " + c.getTransferenciasRealizadas());
                    System.out.println("Saldo da conta: " + c.getSaldoConta());
                    //System.out.println("Pos: "+this.findPosBin(c.getNomeUsuario()));
                    System.out.println("---------------------------------");
                    i++;
                }
            } while (raf.getFilePointer() != raf.length());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void realizarTransferencia() {
        Index ind = new Index();
        Scanner sc = new Scanner(System.in);
        int debito, credito;
        boolean check = true;
        float valor;
        System.out.println("Informe o id do usuário da conta de debito: ");
        debito = sc.nextInt();
        System.out.println("Informe o id do usuário da conta de credito: ");
        credito = sc.nextInt();
        System.out.println("Informe o valor de transferencia:");
        valor = sc.nextFloat();


        //procura posição dos registros
        long posDeb = ind.read(debito);
        long posCre = ind.read(credito);

        //verifica se foi encontrado
        if (posDeb < 0) {
            System.out.println("Nome de usuário para débito não encontrado");
            check = false;
        }
        if (posCre < 0) {
            System.out.println("Nome de usuário para credito não encontrado");
            check = false;
        }

        if (check) {
            //atualiza no arquivo
            try (RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/banco_de_dados.txt", "rw")) {
                //altera no arquivo a conta de débito
                raf.seek(posDeb);
                raf.readByte();
                int tamPassado = raf.readInt();
                byte[] bytesPassado = new byte[tamPassado];
                raf.read(bytesPassado);
                Conta c = new Conta(bytesPassado);
                c.setTransferenciasRealizadas(c.getTransferenciasRealizadas() + 1);
                c.setSaldoConta(c.getSaldoConta() - valor);
                byte[] bytesNovo = c.toByteArray();
                if (tamPassado >= bytesNovo.length) {
                    raf.seek(posDeb + 1);
                    raf.writeInt(tamPassado);
                    raf.write(bytesNovo);
                } else {
                    raf.seek(posDeb);
                    raf.writeByte(1);
                    raf.seek(raf.length());
                    raf.writeByte(0);
                    raf.writeInt(bytesNovo.length);
                    raf.write(bytesNovo);
                }


                //atualiza no arquivo a conta de crédito
                raf.seek(posCre);
                raf.readByte();
                int tamPassado2 = raf.readInt();
                byte[] bytesPassado2 = new byte[tamPassado2];
                raf.read(bytesPassado2);
                Conta c2 = new Conta(bytesPassado2);
                c2.setTransferenciasRealizadas(c2.getTransferenciasRealizadas() + 1);
                c2.setSaldoConta(c2.getSaldoConta() + valor);
                byte[] bytesNovo2 = c2.toByteArray();
                if (tamPassado2 >= bytesNovo2.length) {
                    raf.seek(posCre + 1);
                    raf.writeInt(tamPassado2);
                    raf.write(bytesNovo2);
                } else {
                    raf.seek(posCre);
                    raf.writeByte(1);
                    raf.seek(raf.length());
                    raf.writeByte(0);
                    raf.writeInt(bytesNovo2.length);
                    raf.write(bytesNovo2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Salva utilizando random accessfile para uma única contra no final do arquivo
    public long salvarRandom(Conta c, int id) {
        long pos = -1;
        try(RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/banco_de_dados.txt", "rw")) {
            raf.writeInt(id);
            raf.seek(raf.length());
            pos = raf.getFilePointer();
            byte[] ba;
            ba = c.toByteArray();
            raf.writeByte(0);
            raf.writeInt(ba.length);
            raf.write(ba);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pos;
    }

    //pesquisa sequencial de registros
    public void pesquisarReg(Long pos) {
        try (RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/banco_de_dados.txt", "r")) {
            boolean check = false;
            raf.seek(pos);
            byte lapide = raf.readByte();
            int tam = raf.readInt();
            byte[] bytes = new byte[tam];
            raf.read(bytes);
            if (lapide != 1) {
                Conta c = new Conta(bytes);

                System.out.println("Conta:");
                System.out.println("Id: " + c.getIdConta());
                System.out.println("Nome: " + c.getNomePessoa());
                for (int j = 0; j < c.getEmail().length; j++) {
                    System.out.println("Email " + (j + 1) + ": " + c.getEmail()[j]);
                }
                System.out.println("Usuário: " + c.getNomeUsuario());
                System.out.println("Senha: " + c.getSenha());
                System.out.println("Cidade: " + c.getCidade());
                System.out.println("CPF: " + c.getCpf());
                System.out.println("Transferências realizadas: " + c.getTransferenciasRealizadas());
                System.out.println("Saldo da conta: " + c.getSaldoConta() + "\n");
                check = true;

            }
            System.out.println("Valor do ponteiro: " + raf.getFilePointer());
            System.out.println("Tamanho: " + raf.length());

            if (!check) {
                System.out.println("Usuário não encontrado!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //atualiza registro
    public void atualizarReg() {
        try (RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/banco_de_dados.txt", "rw")) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite um id de usuário para atualizar registro:");
            int id = sc.nextInt();
            Index ind = new Index();
            long pos = ind.read(id);
            boolean check = false;
            Conta c;
            if (pos >= 0) {
                raf.seek(pos); //last id

                byte lapide = raf.readByte();
                int tam = raf.readInt();
                byte[] bytes = new byte[tam];
                raf.read(bytes);
                if (lapide != 1) {
                    c = new Conta(bytes);

                    System.out.println("Conta:");
                    System.out.println("Id: " + c.getIdConta());
                    System.out.println("Nome: " + c.getNomePessoa());
                    for (int j = 0; j < c.getEmail().length; j++) {
                        System.out.println("Email " + (j + 1) + ": " + c.getEmail()[j]);
                    }
                    System.out.println("Usuário: " + c.getNomeUsuario());
                    System.out.println("Senha: " + c.getSenha());
                    System.out.println("Cidade: " + c.getCidade());
                    System.out.println("CPF: " + c.getCpf());
                    System.out.println("Transferências realizadas: " + c.getTransferenciasRealizadas());
                    System.out.println("Saldo da conta: " + c.getSaldoConta() + "\n");

                    sc.nextLine(); //tirar o /n da stream
                    System.out.println("Informe o novo nome:");
                    c.setNomePessoa(sc.nextLine());
                    int op;
                    do {
                        System.out.println("Informe quantos e-mails deseja cadastrar:");
                        op = sc.nextInt();
                        if (op < 0)
                            System.out.println("Digite um valor válido!");
                    } while (op == 0);
                    String[] emails = new String[op];
                    sc.nextLine();
                    for (int k = 0; k < op; k++) {
                        System.out.println("Digite o e-mail " + (k + 1) + ":");
                        emails[k] = sc.nextLine();
                    }
                    c.setEmail(emails);
                    do {
                        System.out.println("Digite seu novo nome de usuário:");
                        String usuario = sc.nextLine();
                        if (checkUser(usuario)) {
                            System.out.println("Nome de usuário indisponível");
                        } else {
                            check = true;
                            c.setNomeUsuario(usuario);
                        }
                    } while (!check);
                    System.out.println("Digite o nome de sua cidade:");
                    c.setCidade(sc.nextLine());
                    System.out.println("Digite seu CPF:");
                    c.setCpf(sc.nextLine());
                    System.out.println("Digite sua senha:");
                    c.setSenha(sc.nextLine());
                    System.out.println("Digite seu saldo:");
                    c.setSaldoConta(sc.nextFloat());
                    c.setTransferenciasRealizadas(0);
                    //procura a posição no arquivo para sobreescrever ou criar novo registro
                    raf.seek(pos + 1);
                    int tamPassado = raf.readInt();
                    byte[] bytesNovo = c.toByteArray();
                    //caso o tamanho seja menor ou igual ao anterior, ele mantém
                    if (bytesNovo.length <= tamPassado) {
                        raf.seek(pos + 1);
                        raf.writeInt(tamPassado);
                        raf.write(bytesNovo);
                    } else {
                        //marca a lápide e vai para o final do arquivo
                        raf.seek(pos);
                        raf.writeByte(1);
                        long newPos = raf.length();
                        raf.seek(newPos);
                        raf.writeByte(0);
                        raf.writeInt(bytesNovo.length);
                        ind.update(id, newPos);
                        raf.write(bytesNovo);
                    }


                } else {
                    //caso encontre é solicitado os novos dados
                    System.out.println("Usuário não encontrado!\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletar() {
        try (RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/banco_de_dados.txt", "rw")) {
            Scanner sc = new Scanner(System.in);
            Index ind = new Index();
            System.out.println("Digite um id de usuário para deletar registro:");
            int id = sc.nextInt();
            long pos = ind.read(id);
            if (pos >= 0) {
                raf.seek(pos); //last id

                byte lapide = raf.readByte();
                int tam = raf.readInt();
                byte[] bytes = new byte[tam];
                raf.read(bytes);
                if (lapide != 1) {
                    Conta c = new Conta(bytes);

                    System.out.println("Conta deletada:");
                    System.out.println("Id: " + c.getIdConta());
                    System.out.println("Nome: " + c.getNomePessoa());
                    for (int j = 0; j < c.getEmail().length; j++) {
                        System.out.println("Email " + (j + 1) + ": " + c.getEmail()[j]);
                    }
                    System.out.println("Usuário: " + c.getNomeUsuario());
                    System.out.println("Senha: " + c.getSenha());
                    System.out.println("Cidade: " + c.getCidade());
                    System.out.println("CPF: " + c.getCpf());
                    System.out.println("Transferências realizadas: " + c.getTransferenciasRealizadas());
                    System.out.println("Saldo da conta: " + c.getSaldoConta() + "\n");

                    raf.seek(pos);
                    raf.writeByte(1);
                }

            } else {

                System.out.println("Usuário não encontrado!\n");
            }
            //acha posição do registro
            //marca lápide

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void comprimir() throws IOException{
        Lzw lzw = new Lzw();
        Scanner sc = new Scanner(System.in);
        String nomeArq;
        System.out.println("Digite o nome do arquivo comprimido:");
        nomeArq = sc.nextLine();
        System.out.println("Informe a versão:");
        nomeArq = nomeArq+"Compressao"+sc.nextLine();
        nomeArq += ".lzw";

        File base_dados = new File("src/trabalho/main/output/banco_de_dados.txt");
        byte[] bytes = Files.readAllBytes(base_dados.toPath());

        ArrayList<Integer> vet_codificado;

        long tempoInicial = System.currentTimeMillis();
        vet_codificado = lzw.cod_vet(bytes);
        long tempoFinal = System.currentTimeMillis();

        int[] arr = vet_codificado.stream().mapToInt(i -> i).toArray();
        long arrTam = (long)arr.length * 32;

        DataOutputStream dos;

        try(FileOutputStream fos = new FileOutputStream("src/trabalho/main/output/"+nomeArq)) {
            dos = new DataOutputStream(fos);
            for (int j : arr) dos.writeInt(j);

        } catch(Exception e) {
            e.printStackTrace();
        }
        long bytesSize = Files.size(base_dados.toPath()) * 32;
        double percent = 1-(double)(bytesSize/arrTam);

        System.out.println("Tempo de execução: "+(tempoFinal-tempoInicial)+"ms | Porcentagem de ganho: "+(percent*100)+"%");
    }

    public void descomprimir() throws IOException{
        Lzw lzw = new Lzw();
        Scanner sc = new Scanner(System.in);
        String nomeArq;
        System.out.println("Digite o nome do arquivo para descomprimir:");
        nomeArq = sc.nextLine();
        System.out.println("Informe a versão:");
        nomeArq = nomeArq+"Compressao"+sc.nextLine();
        nomeArq += ".lzw";

        DataInputStream dis;
        ArrayList<Byte> vet_decod = new ArrayList<Byte>();

        try(FileInputStream fis = new FileInputStream("src/trabalho/main/output/"+nomeArq)) {
            dis = new DataInputStream(fis);
            ArrayList<Integer> vet_cod = new ArrayList<Integer>();

            while(true) {
                try{
                    vet_cod.add(dis.readInt());
                } catch (EOFException fim) {
                    dis.close();
                    break;
                }
            }


            vet_decod = lzw.decod_vet(vet_cod);



        } catch(Exception e) {
            e.printStackTrace();
        }

        try(RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/banco_de_dados.txt", "rw")) {
            raf.writeByte(vet_decod.get(0)); //last id

            for(int i=1; i<vet_decod.size(); i++) {
                raf.writeByte(vet_decod.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
