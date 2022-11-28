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
        int op;
        Banco banco = new Banco();
        Index ind = new Index();
        do {
            System.out.println("Bem vindo ao sistem bancário!");
            System.out.println("Digite a opção desejada:\n1)Criar Conta bancária\n2)Realizar transferencia\n3)Pesquisar Registro\n4)Atualizar um registro\n5)Deletar registro\n6)Comprimir a base de dados\n7)Descomprimir uma base de dados\n0)Sair");
            op = sc.nextInt();
            switch (op) {
                case 0:
                    break;
                case 1:
                    Conta c = banco.criarConta();
                    long pos = banco.salvarRandom(c, c.getIdConta());
                    ind.create(c.getIdConta(), pos);
                    banco.imprimir();
                    ind.imprimir();
                    break;
                case 2:
                    banco.realizarTransferencia();
                    banco.imprimir();
                    break;
                case 3:
                    System.out.println("Digite um id de usuário para pesquisar:");
                    int id = sc.nextInt();
                    banco.pesquisarReg(ind.read(id));
                    break;
                case 4:
                    banco.atualizarReg();
                    break;
                case 5:
                    banco.deletar();
                    banco.imprimir();
                    break;
                case 6:
                    banco.comprimir();
                    break;
                case 7:
                    banco.descomprimir();
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while(op != 0);
    }
}
