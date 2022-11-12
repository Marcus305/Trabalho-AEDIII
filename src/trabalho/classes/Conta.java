package trabalho.classes;
import java.io.*;

//classe para métodos de get e set das contas
public class Conta {
    private int idConta;
    private String nomePessoa;
    private String[] email;
    private String nomeUsuario;
    private String senha;
    private String cpf;
    private String cidade;
    private int transferenciasRealizadas = 0;
    private float saldoConta;

    public Conta(int idConta) {
        this.idConta = idConta;
    }

    public Conta() {
    }

    public Conta(int idConta, String nomePessoa, String[] email, String nomeUsuario, String senha, String cpf, String cidade, float saldoConta) {
        this.idConta = idConta;
        this.nomePessoa = nomePessoa;
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.cpf = cpf;
        this.cidade = cidade;
        this.saldoConta = saldoConta;
    }

    public Conta(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.setIdConta(dis.readInt());
        this.setNomePessoa(dis.readUTF());
        String[] vet = new String[dis.readInt()];
        for(int i=0; i<vet.length; i++) {
            vet[i] = dis.readUTF();
        }
        this.setEmail(vet);
        this.setNomeUsuario(dis.readUTF());
        this.setSenha(dis.readUTF());
        this.setCpf(dis.readUTF());
        this.setCidade(dis.readUTF());
        this.setTransferenciasRealizadas(dis.readInt());
        this.setSaldoConta(dis.readFloat());
    }

    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getTransferenciasRealizadas() {
        return transferenciasRealizadas;
    }

    public void setTransferenciasRealizadas(int transferenciasRealizadas) {
        this.transferenciasRealizadas = transferenciasRealizadas;
    }

    public float getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(float saldoConta) {
        this.saldoConta = saldoConta;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(baos);
        data.writeInt(this.getIdConta());
        data.writeUTF(this.getNomePessoa());
        data.writeInt(this.getEmail().length);
        for(int i=0; i<this.getEmail().length; i++) {
            data.writeUTF(this.getEmail()[i]);
        }
        data.writeUTF(this.getNomeUsuario());
        data.writeUTF(this.getSenha());
        data.writeUTF(this.getCpf());
        data.writeUTF(this.getCidade());
        data.writeInt(this.getTransferenciasRealizadas());
        data.writeFloat(this.getSaldoConta());
        return baos.toByteArray();
    }
}
