package view;

import controller.KeyController;
import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;
import java.io.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class JPainel extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(JPainel.class.getName());
    private static final String REMEMBER_ME_FILE = "remember_me.txt";  //guarda o nome do usuario
    private UsuarioController usuarioController = new UsuarioController();
    private KeyController keyController = new KeyController();

    private JTextField tfUsuarioLogin;
    private JPasswordField pfSenhaLogin;
    private JButton btnLogin;
    private JCheckBox cbSalvarLogin;
    private JButton btnTrocarSenha;

    private JTextField tfNomeRegistro;
    private JTextField tfUsuarioRegistro;
    private JPasswordField pfSenhaRegistro;
    private JTextField tfChaveRegistro;
    private JButton btnRegistrar;

    private JPanel painelLogin;

    public JPainel() {
            setTitle("Login e Registro");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(500, 350);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();

        painelLogin = new JPanel(new GridBagLayout());
        painelLogin.setPreferredSize(new Dimension(400, 250));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;

        tfUsuarioLogin = new JTextField(20);
        tfUsuarioLogin.setPreferredSize(new Dimension(200, 28));
        pfSenhaLogin = new JPasswordField(20);
        pfSenhaLogin.setPreferredSize(new Dimension(200, 28));
        btnLogin = new JButton("Login");
        cbSalvarLogin = new JCheckBox("Salvar Usuário");
        btnTrocarSenha = new JButton("Trocar Senha");

        c.gridx = 0; c.gridy = 0;
        painelLogin.add(new JLabel("Usuário:"), c);
        c.gridx = 1; c.gridy = 0;
        painelLogin.add(tfUsuarioLogin, c);

        c.gridx = 0; c.gridy = 1;
        painelLogin.add(new JLabel("Senha:"), c);
        c.gridx = 1; c.gridy = 1;
        painelLogin.add(pfSenhaLogin, c);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        painelLogin.add(cbSalvarLogin, c);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        painelLogin.add(btnLogin, c);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        painelLogin.add(btnTrocarSenha, c);

        btnLogin.addActionListener(e -> fazerLogin());
        btnTrocarSenha.addActionListener(e -> abrirTrocaSenha());

        carregarUsuarioSalvo();

        JPanel painelRegistro = new JPanel(new GridBagLayout());

        tfNomeRegistro = new JTextField();
        tfNomeRegistro.setPreferredSize(new Dimension(120, 28));

        tfUsuarioRegistro = new JTextField();
        tfUsuarioRegistro.setPreferredSize(new Dimension(120, 28));

        pfSenhaRegistro = new JPasswordField();
        pfSenhaRegistro.setPreferredSize(new Dimension(120, 28));

        tfChaveRegistro = new JTextField();
        tfChaveRegistro.setPreferredSize(new Dimension(120, 28));
        btnRegistrar = new JButton("Registrar");

        c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.NONE;

        c.gridx = 0; c.gridy = 0;
        painelRegistro.add(new JLabel("Nome:"), c);
        c.gridx = 1; c.gridy = 0;
        painelRegistro.add(tfNomeRegistro, c);

        c.gridx = 0; c.gridy = 1;
        painelRegistro.add(new JLabel("Usuário:"), c);
        c.gridx = 1; c.gridy = 1;
        painelRegistro.add(tfUsuarioRegistro, c);

        c.gridx = 0; c.gridy = 2;
        painelRegistro.add(new JLabel("Senha:"), c);
        c.gridx = 1; c.gridy = 2;
        painelRegistro.add(pfSenhaRegistro, c);

        c.gridx = 0; c.gridy = 3;
        painelRegistro.add(new JLabel("Key:"), c);
        c.gridx = 1; c.gridy = 3;
        painelRegistro.add(tfChaveRegistro, c);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        painelRegistro.add(btnRegistrar, c);

        btnRegistrar.addActionListener(e -> fazerRegistro());

        abas.add("Login", painelLogin);
        abas.add("Registro", painelRegistro);

        add(abas, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void fazerLogin() {
    String usuario = tfUsuarioLogin.getText().trim();
    String senha = new String(pfSenhaLogin.getPassword());

    if (usuario.isEmpty() || senha.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
        return;
    }

    boolean loginOk = usuarioController.login(usuario, senha);
    if (loginOk) {
        if (cbSalvarLogin.isSelected()) {
            salvarUsuario(usuario);
        } else {
            removerUsuarioSalvo();
        }

        boolean isAdmin = usuarioController.isAdmin(usuario, senha);
        if (isAdmin) {
            JOptionPane.showMessageDialog(this, "Login Admin realizado!");
            new JPainelAdmin(usuario).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login usuário realizado!");
            new JPainelUsuario(usuario).setVisible(true);
            dispose();
        }
    } else {
        JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!");
    }
}

    private void fazerRegistro() {
        String nome = tfNomeRegistro.getText().trim();
        String usuario = tfUsuarioRegistro.getText().trim();
        String senha = new String(pfSenhaRegistro.getPassword());
        String chave = tfChaveRegistro.getText().trim();

        if (nome.isEmpty() || usuario.isEmpty() || senha.isEmpty() || chave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        boolean chaveValida = keyController.validarChave(chave);
        if (!chaveValida) {
            JOptionPane.showMessageDialog(this, "Chave inválida ou já usada!");
            return;
        }

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setUsuario(usuario);
        u.setSenha(senha);
        u.setAdmin(false);

        boolean registrado = usuarioController.registrar(u);
        if (registrado) {
            keyController.usarChave(chave, usuario);
            JOptionPane.showMessageDialog(this, "Usuário registrado com sucesso!");
            tfNomeRegistro.setText("");
            tfUsuarioRegistro.setText("");
            pfSenhaRegistro.setText("");
            tfChaveRegistro.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao registrar usuário! Talvez o nome de usuário já exista.");
        }
    }

    private void abrirTrocaSenha() {
        JTextField usuarioField = new JTextField(tfUsuarioLogin.getText());
        usuarioField.setEditable(false);
        JPasswordField senhaAtualField = new JPasswordField();
        JPasswordField novaSenhaField = new JPasswordField();
        JPasswordField confirmarSenhaField = new JPasswordField();

        Object[] inputs = {
                "Usuário:", usuarioField,
                "Senha Atual:", senhaAtualField,
                "Nova Senha:", novaSenhaField,
                "Confirmar Nova Senha:", confirmarSenhaField
        };

        int result = JOptionPane.showConfirmDialog(
                this,
                inputs,
                "Trocar Senha",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String usuario = usuarioField.getText().trim();
            String senhaAtual = new String(senhaAtualField.getPassword());
            String novaSenha = new String(novaSenhaField.getPassword());
            String confirmarSenha = new String(confirmarSenhaField.getPassword());

            if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            if (!novaSenha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(this, "A nova senha e a confirmação não coincidem.");
                return;
            }

            boolean sucesso = usuarioController.trocarSenha(usuario, senhaAtual, novaSenha);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Senha alterada com sucesso!");
                pfSenhaLogin.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao trocar senha. Senha atual incorreta ou outro erro.");
            }
        }
    }

    private void salvarUsuario(String usuario) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REMEMBER_ME_FILE))) {
            writer.write(usuario);
        } catch (IOException e) {
            logger.severe("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    private void carregarUsuarioSalvo() {  
        File file = new File(REMEMBER_ME_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String usuarioSalvo = reader.readLine();
                if (usuarioSalvo != null && !usuarioSalvo.trim().isEmpty()) {
                    tfUsuarioLogin.setText(usuarioSalvo.trim());
                    cbSalvarLogin.setSelected(true);
                }
            } catch (IOException e) {
                logger.severe("Erro ao carregar usuário salvo: " + e.getMessage());
            }
        }
    }

    private void removerUsuarioSalvo() {
        File file = new File(REMEMBER_ME_FILE);
        if (file.exists()) {
            if (!file.delete()) {
                logger.warning("Não foi possível remover o arquivo de usuário salvo.");
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 481, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new JPainel().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
