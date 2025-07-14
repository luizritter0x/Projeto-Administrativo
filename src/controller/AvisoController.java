package controller;

import dao.AvisoDAO;
import model.Aviso;

import java.util.List;

public class AvisoController {

    private AvisoDAO avisoDAO;

    public AvisoController() {
        this.avisoDAO = new AvisoDAO();
    }

    public void salvarAviso(Aviso aviso) {
        avisoDAO.salvar(aviso);
    }

    public List<Aviso> listarAvisos() {
        return avisoDAO.listarTodos();
    }
}
