package model;

import java.util.ArrayList;
import java.util.List;

public class Chamado {

    private int id;
    private String titulo;
    private String descricao;
    private String status;
    private String usuario;
    private List<MensagemChamado> mensagens = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<MensagemChamado> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<MensagemChamado> mensagens) {
        this.mensagens = mensagens;
    }

    public void addMensagem(MensagemChamado mensagem) {
        this.mensagens.add(mensagem);
    }
    
}
