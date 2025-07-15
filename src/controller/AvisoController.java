package controller;

import dao.AvisoDAO;
import model.Aviso;

import java.util.List;

public class AvisoController {

    private AvisoDAO avisoDAO;   //construtores

    public AvisoController() {
        this.avisoDAO = new AvisoDAO();  
    }

    public void salvarAviso(Aviso aviso) {
        avisoDAO.salvar(aviso);
    }

    public List<Aviso> listarAvisos() {   //retorna a lista dos avisos
        return avisoDAO.listarTodos();
    }

    public void deletarAviso(int id) {
        avisoDAO.deletar(id);
    }
}
