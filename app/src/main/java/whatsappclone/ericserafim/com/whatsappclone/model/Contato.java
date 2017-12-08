package whatsappclone.ericserafim.com.whatsappclone.model;


import java.io.Serializable;

public class Contato implements Serializable {

    private String identificadorUsuario;
    private String nome;
    private String email;

    public Contato() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentificadorUsuario() {
        return identificadorUsuario;
    }

    public void setIdentificadorUsuario(String identificadorUsuario) {
        this.identificadorUsuario = identificadorUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
