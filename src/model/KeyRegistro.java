package model;

public class KeyRegistro {
    private int id;
    private String chave;
    private boolean usada;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getChave() { return chave; }
    public void setChave(String chave) { this.chave = chave; }

    public boolean isUsada() { return usada; }
    public void setUsada(boolean usada) { this.usada = usada; }
}