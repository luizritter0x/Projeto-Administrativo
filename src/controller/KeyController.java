package controller;

import dao.KeyDAO;
import model.KeyRegistro;
import java.util.List;

public class KeyController {
    private KeyDAO keyDAO = new KeyDAO();

    public boolean inserirKey(String chave) {
        return keyDAO.inserirKey(chave);
    }

    public boolean validarChave(String chave) {
        return keyDAO.validarChave(chave);
    }

    public boolean usarChave(String chave, String usuarioQueUsou) {
        return keyDAO.usarChave(chave);
    }

    public List<KeyRegistro> listarKeys() {
        return keyDAO.listarKeys();
    }

    public boolean removerKey(String chave) {
        return keyDAO.removerKey(chave);
    }
}
