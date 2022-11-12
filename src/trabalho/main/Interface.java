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
        banco.loadUsers();
        do {
            System.out.println("Bem vindo ao sistem bancário!");
            System.out.println("Digite a opção desejada:\n1)Criar Conta bancária\n2)Realizar transferencia\n3)Pesquisar Registro\n4)Atualizar um registro\n5)Deletar registro\n0)Sair");
            op = sc.nextInt();
            switch (op) {
                case 0:
                    break;
                case 1:
                    Conta c = banco.criarConta();
                    banco.salvarRandom(c);
                    banco.imprimir();
                    break;
                case 2:
                    banco.realizarTransferencia();
                    banco.imprimir();
                    break;
                case 3:
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
                    banco.teste();
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while(op != 0);
    }
}
