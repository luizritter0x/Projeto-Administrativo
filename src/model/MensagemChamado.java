package model;

import java.io.Serializable; // (serialização)
import java.time.LocalDateTime; //  data e hora

// Classe que representa uma mensagem dentro de um chamado
public class MensagemChamado implements Serializable {

    private int id; 
    private String autor; 
    private String texto; 
    private LocalDateTime dataHora; 

    public MensagemChamado() {
    }

    public MensagemChamado(String autor, String texto, LocalDateTime dataHora) {
        this.autor = autor;
        this.texto = texto;
        this.dataHora = dataHora;
    }

    // getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
