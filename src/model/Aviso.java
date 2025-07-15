package model;

import java.time.LocalDateTime;

public class Aviso {  
    private int id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataPublicacao;
    private String criadoPor;

    public int getId() { return id; }       //getter e setters
    public void setId(int id) { this.id = id; }
   
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
   
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
   
    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }
    
    public String getCriadoPor() { return criadoPor; }
    public void setCriadoPor(String criadoPor) { this.criadoPor = criadoPor; }
}
