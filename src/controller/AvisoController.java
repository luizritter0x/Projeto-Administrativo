package controller;

import dao.AvisoDAO;
import model.Aviso;

import java.util.List;

public class AvisoController {

    private AvisoDAO avisoDAO;

    public AvisoController() {
        this.avisoDAO = new AvisoDAO();
    }

    public List<Aviso> listarAvisos() {
        return avisoDAO.listarTodos();
    }

    public void salvarAviso(Aviso aviso) {
        avisoDAO.salvar(aviso);
    }

    public void deletarAviso(int id) {
        avisoDAO.deletar(id);
    }
}
