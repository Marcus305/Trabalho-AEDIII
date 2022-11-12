package trabalho.classes;

import java.io.*;
import java.util.Scanner;

//Classe para manipular as contas de maneira indexada
public class Banco {
    private String[] users;
    private int lastId = -1;

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public void setOneUsers(String user) {
        if (this.getUsers() != null) {
            String[] temp = new String[this.getUsers().length + 1];
            for (int i = 0; i < this.users.length; i++) {
                temp[i] = this.getUsers()[i];
            }
            temp[temp.length - 1] = user;
            this.setUsers(temp);
        } else {
            String[] temp = new String[1];
            temp[0] = user;
            this.setUsers(temp);
        }
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public int gerarId() {
        if (this.lastId == -1) {
            return 1;
        } else {
            return this.lastId + 1;
        }
    }

    public void loadUsers() {
        try(RandomAccessFile dis = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt", "r")) {
            int lastid = dis.readInt();
            this.setLastId(lastid);
            String[] usuarios = new String[lastid];
            int i=0;
            do {
                byte lapide = dis.readByte();
                int tam = dis.readInt();
                byte[] bytes = new byte[tam];
                dis.read(bytes);
                if (lapide != 1) {
                    Conta c = new Conta(bytes);
                    usuarios[i] = c.getNomeUsuario();
                    i++;
                }
            } while (dis.getFilePointer() != dis.length());
            this.setUsers(usuarios);
        } catch (Exception e) {
            //System.out.println(e);
            return;
        }
    }


    //Verifica se já possui um determinado nome de usuário no banco
    public boolean checkUser(String test) {
        if (this.getUsers() != null) {
            for (int i = 0; i < this.getUsers().length; i++) {
                if (this.getUsers()[i].equals(test)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Conta criarConta() throws IOException {
        Conta conta = new Conta(this.gerarId());
        this.setLastId(conta.getIdConta());
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
        byte bit = 0;
        byte[] ba = conta.toByteArray();
        return conta;
    }

    //Método somente para checar contas na memória secundária
    public void imprimir() {
        try(RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt", "r")) {
            System.out.println("---------------------------------");
            raf.readInt(); //last id
            int i=1;
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
                    System.out.println("Pos: "+this.findPosBin(c.getNomeUsuario()));
                    System.out.println("---------------------------------");
                    i++;
                }
            } while (raf.getFilePointer() != raf.length());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //lê os dados para memória principal
    /*public void ler() {
        try {
            FileInputStream arq2 = new FileInputStream("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt");
            DataInputStream dis = new DataInputStream(arq2);
            int lastid = dis.readInt();
            System.out.println(lastid);
            this.setLastId(lastid);
            for (int i = 0, j = 0; i <= lastid; i++) {

                byte lapide = dis.readByte();
                System.out.println(lapide);
                if (lapide != 1) {
                    int tam = dis.readInt();
                    System.out.println(tam);
                    byte[] bytes = new byte[tam];
                    dis.read(bytes);
                    Conta c = new Conta(bytes);
                    c.setLapide(lapide);
                    c.setTamReg(tam);
                    this.setOneContas(c);
                    j++;
                } else {
                    int tam = dis.readInt();
                    byte[] temp = new byte[tam];
                    dis.read(temp);
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/

    //Checa se existe arquivo
    public boolean checkFile() {
        try {
            FileInputStream arq2 = new FileInputStream("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Acha o começo de um registro e retorna sua posição no arquivo
    public long findPosBin(String user) {
        try (RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt", "r")) {
            raf.readInt(); //last id
            do {
                long pos = raf.getFilePointer();
                byte lapide = raf.readByte();
                int tam = raf.readInt();
                byte[] bytes = new byte[tam];
                raf.read(bytes);
                if (lapide != 1) {
                    Conta c = new Conta(bytes);
                    if (c.getNomeUsuario().equals(user)) {
                        return pos;
                    }
                }
            } while (raf.getFilePointer() != raf.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void realizarTransferencia() {
        Scanner sc = new Scanner(System.in);
        String debito, credito;
        boolean check = true;
        float valor;
        boolean checkDeb = false, checkCre = false;
        System.out.println("Informe o nome de usuário da conta de debito: ");
        debito = sc.nextLine();
        System.out.println("Informe o nome de usuário da conta de credito: ");
        credito = sc.nextLine();
        System.out.println("Informe o valor de transferencia:");
        valor = sc.nextFloat();


        //procura posição dos registros
        long posDeb = this.findPosBin(debito);
        long posCre = this.findPosBin(credito);

        //verifica se foi encontrado
        if (posDeb == -1) {
            System.out.println("Nome de usuário para débito não encontrado");
            check = false;
        }
        if (posCre == -1) {
            System.out.println("Nome de usuário para credito não encontrado");
            check = false;
        }

        if (check) {
            //atualiza no arquivo
            try (RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt", "rw")) {
                //altera no arquivo a conta de débito
                raf.seek(posDeb);
                raf.readByte();
                int tamPassado = raf.readInt();
                byte[] bytesPassado = new byte[tamPassado];
                raf.read(bytesPassado);
                Conta c= new Conta(bytesPassado);
                c.setTransferenciasRealizadas(c.getTransferenciasRealizadas()+1);
                c.setSaldoConta(c.getSaldoConta()-valor);
                byte[] bytesNovo = c.toByteArray();
                if (tamPassado >= bytesNovo.length) {
                    raf.seek(posDeb+1);
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
                Conta c2= new Conta(bytesPassado2);
                c2.setTransferenciasRealizadas(c2.getTransferenciasRealizadas()+1);
                c2.setSaldoConta(c2.getSaldoConta()+valor);
                byte[] bytesNovo2 = c2.toByteArray();
                if (tamPassado2 >= bytesNovo2.length) {
                    raf.seek(posCre+1);
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
    public void salvarRandom(Conta c) {
        try {
            RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt", "rw");
            raf.writeInt(this.getLastId());
            raf.seek(raf.length());
            byte[] ba;
            ba = c.toByteArray();
            raf.writeByte(0);
            raf.writeInt(ba.length);
            raf.write(ba);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //pesquisa sequencial de registros
    public void pesquisarReg() {
        try(RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt", "r")) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite um nome de usuário para pesquisar:");
            String user = sc.nextLine();
            boolean check = false;
            raf.readInt(); //last id
            do {
                byte lapide = raf.readByte();
                int tam = raf.readInt();
                byte[] bytes = new byte[tam];
                raf.read(bytes);
                if (lapide != 1) {
                    Conta c = new Conta(bytes);
                    if (c.getNomeUsuario().equals(user)) {
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
                }
                System.out.println("Valor do ponteiro: " +raf.getFilePointer());
                System.out.println("Tamanho: " +raf.length());
            } while (raf.getFilePointer() != raf.length());
            if (!check) {
                System.out.println("Usuário não encontrado!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //atualiza registro
    public void atualizarReg() {
        try(RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt", "rw")) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite um nome de usuário para atualizar registro:");
            String user = sc.nextLine();
            boolean check = false;
            Conta c = new Conta();
            raf.readInt(); //last id
            do {
                byte lapide = raf.readByte();
                int tam = raf.readInt();
                byte[] bytes = new byte[tam];
                raf.read(bytes);
                if (lapide != 1) {
                    c = new Conta(bytes);
                    if (c.getNomeUsuario().equals(user)) {
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
                }
            } while (raf.getFilePointer() != raf.length());

            //caso encontre é solicitado os novos dados
            if (!check) {
                System.out.println("Usuário não encontrado!\n");
            } else {
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
                check = false;
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
                long pos = this.findPosBin(user);
                //procura a posição no arquivo para sobreescrever ou criar novo registro
                if (pos != -1) {
                    raf.seek(pos + 1);
                    int tamPassado = raf.readInt();
                    byte[] bytesNovo = c.toByteArray();
                    //caso o tamanho seja menor ou igual ao anterior, ele sobreescreve
                    if (bytesNovo.length <= tamPassado) {
                        raf.seek(pos + 1);
                        raf.writeInt(tamPassado);
                        raf.write(bytesNovo);
                    } else {
                        //marca a lápide e vai para o final do arquivo
                        raf.seek(pos);
                        raf.writeByte(1);
                        raf.seek(raf.length());
                        raf.writeByte(0);
                        raf.writeInt(bytesNovo.length);
                        raf.write(bytesNovo);
                    }
                } else {
                    System.out.println("Conta não encontrada");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletar() {
        try(RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt", "rw")) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite um nome de usuário para deletar registro:");
            String user = sc.nextLine();
            boolean check = false;

            //pesquisa sequencial para encontrar o registro
            raf.readInt(); //last id
            do {
                byte lapide = raf.readByte();
                int tam = raf.readInt();
                byte[] bytes = new byte[tam];
                raf.read(bytes);
                if (lapide != 1) {
                    Conta c = new Conta(bytes);
                    if (c.getNomeUsuario().equals(user)) {
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
                }
            } while (raf.getFilePointer() != raf.length());

            if (!check) {
                System.out.println("Usuário não encontrado!\n");
            } else {
                //acha posição do registro
                long pos = this.findPosBin(user);
                //marca lápide
                if (pos != -1) {
                    raf.seek(pos);
                    raf.writeByte(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void teste() {
        Index ind = new Index();
        ind.create(1, 4);
        ind.create(2, 88);
        ind.create(4, 253);
        long pos = ind.find(2);
        try(RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\output0.txt", "r")) {
            raf.seek(pos);
            System.out.println("Lápide: "+raf.readByte());
            int tamPassado = raf.readInt();
            byte[] bytesPassado = new byte[tamPassado];
            raf.read(bytesPassado);
            Conta c= new Conta(bytesPassado);
            System.out.println("Nome: "+c.getNomePessoa());
            System.out.println("User: "+c.getNomeUsuario());
            System.out.println("cpf: "+c.getCpf());
            System.out.println("cidade: "+c.getCidade());
            System.out.println("senha: "+c.getSenha());
            System.out.println("id: "+c.getIdConta());
            System.out.println("saldo: "+c.getSaldoConta());
            System.out.println("trans: "+c.getTransferenciasRealizadas());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
