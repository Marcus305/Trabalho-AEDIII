package trabalho.main;
import java.io.IOException;
import java.util.Scanner;
import trabalho.classes.Banco;
import trabalho.classes.Conta;
import trabalho.classes.Index;

//A classe interface é responsável por I/O
public class Interface {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int op=0;
        Banco banco = new Banco();
        Index ind = new Index();
        do {
            System.out.println("Bem vindo ao sistem bancário!");
            System.out.println("Digite a opção desejada:\n1)Criar Conta bancária\n2)Realizar transferencia\n3)Pesquisar Registro\n4)Atualizar um registro\n5)Deletar registro\n0)Sair");
            op = sc.nextInt();
            switch (op) {
                case 0:
                    break;
                case 1:
                    Conta c = banco.criarConta();
                    banco.salvarRandom(c, c.getIdConta());
                    ind.create(c.getIdConta(), banco.findPosBin(c.getNomeUsuario()));
                    banco.imprimir();
                    ind.imprimir();
                    break;
                case 2:
                    banco.realizarTransferencia();
                    banco.imprimir();
                    break;
                case 3:
                    System.out.println("Digite um nome de usuário para pesquisar:");
                    S
                    banco.pesquisarReg();
                    break;
                case 4:
                    banco.atualizarReg();
                    break;
                case 5:
                    banco.deletar();
                    banco.imprimir();
                    break;
                case 6:
                    banco.imprimir();
                    //banco.teste();
                    break;
                case 7:
                    Index test = new Index();
                    test.create(1, 10);
                    test.create(2, 10);
                    test.create(3, 10);
                    test.create(4, 10);
                    test.create(5, 10);
                    test.create(6, 10);
                    test.create(7, 10);
                    test.create(8, 10);
                    test.create(9, 10);
                    test.create(10, 10);
                    test.create(11, 10);
                    test.create(12, 10);
                    test.create(22, 10);
                    test.create(23, 10);
                    break;
                case 8:
                    Index test2 = new Index();
                    System.out.println(test2.read(1));
                    System.out.println(test2.read(2));
                    System.out.println(test2.read(3));
                    System.out.println(test2.read(4));
                    System.out.println(test2.read(5));
                    System.out.println(test2.read(6));
                    System.out.println(test2.read(7));
                    System.out.println(test2.read(8));
                    System.out.println(test2.read(9));
                    System.out.println(test2.read(10));
                    System.out.println(test2.read(11));
                    System.out.println(test2.read(12));
                    System.out.println(test2.read(13));
                    System.out.println(test2.read(22));
                    System.out.println(test2.read(23));
                    break;
                case 9:
                    Index test3 = new Index();
                    test3.update(4, 20);
                    System.out.println(test3.read(4));
                    test3.update(22, 30);
                    System.out.println(test3.read(22));
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while(op != 0);
    }
}
