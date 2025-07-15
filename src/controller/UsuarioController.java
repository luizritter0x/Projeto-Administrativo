package controller;

import dao.UsuarioDAO;
import model.Usuario;
import java.util.List;

public class UsuarioController {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();       //metodos

    public boolean login(String usuario, String senha) {
        return usuarioDAO.autenticarUsuario(usuario, senha);
    }

    public boolean registrar(Usuario usuario) {
        return usuarioDAO.inserirUsuario(usuario);
    }

    public boolean isAdmin(String usuario, String senha) {
        return usuarioDAO.verificarAdmin(usuario, senha);
    }

    public List<Usuario> listarTodosUsuarios(String adminUsuarioLogado) {
        return usuarioDAO.listarTodosUsuarios(adminUsuarioLogado);
    }

    public boolean atualizarAdminStatus(String usuario, boolean isAdmin) {
        return usuarioDAO.atualizarAdminStatus(usuario, isAdmin);
    }

    public boolean removerUsuario(String usuario) {
        return usuarioDAO.removerUsuario(usuario);
    }

    public boolean verificarUsuarioExiste(String usuario) {
        return usuarioDAO.verificarUsuarioExiste(usuario);
    }

    public boolean redefinirSenha(String usuario, String novaSenha) {
        return usuarioDAO.redefinirSenha(usuario, novaSenha);
    }

    public boolean trocarSenha(String usuario, String senhaAtual, String novaSenha) {
        return usuarioDAO.trocarSenha(usuario, senhaAtual, novaSenha);
    }
    
}
