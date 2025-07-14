/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.KeyController;
import controller.UsuarioController;
import model.KeyRegistro;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.Random;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import model.Chamado;
import model.MensagemChamado;

public class JPainelAdmin extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(JPainelAdmin.class.getName());
    private KeyController keyController = new KeyController();
    private UsuarioController usuarioController = new UsuarioController();
    private String adminUsuarioLogado;

    private JTabbedPane abasAdmin;

    private JTable tableKeys;
    private DefaultTableModel tableModelKeys;
    private JTextField tfKeyGerada;
    private JButton btnCopiar;
    private JButton btnGerarKey;
    private JButton btnVoltar;
    private JPopupMenu popupMenuKeys;
    private JMenuItem removerMenuItem;

    private JTable tableUsuarios;
    private DefaultTableModel tableModelUsuarios;
    private JButton btnAlternarAdminStatus;
    private JPopupMenu popupMenuUsuarios;
    private JMenuItem removerUsuarioMenuItem;
    private controller.ChamadoController chamadoController = new controller.ChamadoController();

    private JTable tableChamados;
    private DefaultTableModel tableModelChamados;
    private JButton btnCarregarChamados;
    private JButton btnAbrirChamado;
    private JButton btnNovoChamado;
    
    public JPainelAdmin(String adminUsuarioLogado) {
        this.adminUsuarioLogado = adminUsuarioLogado;
        setTitle("Painel do Administrador - Logado como: " + adminUsuarioLogado);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        abasAdmin = new JTabbedPane();
        getContentPane().add(abasAdmin, BorderLayout.CENTER);

        initKeysPanel();
        initUsuariosPanel();
        initChamadosPanel();

        carregarKeys();
        carregarUsuarios();
        initAvisosPanel(); 
    }

    public JPainelAdmin() {
        this("admin");
    }

    private void initKeysPanel() {
        JPanel painelKeys = new JPanel(new BorderLayout());
        abasAdmin.addTab("Gerenciar Keys", painelKeys);

        String[] colunasKeys = {"Chave", "Usada"};
        tableModelKeys = new DefaultTableModel(colunasKeys, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableKeys = new JTable(tableModelKeys);
        painelKeys.add(new JScrollPane(tableKeys), BorderLayout.CENTER);

        popupMenuKeys = new JPopupMenu();
        removerMenuItem = new JMenuItem("Remover Chave");
        popupMenuKeys.add(removerMenuItem);

        removerMenuItem.addActionListener(e -> {
            int row = tableKeys.getSelectedRow();
            if (row != -1) {
                String chave = (String) tableModelKeys.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Remover chave " + chave + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (keyController.removerKey(chave)) {
                        JOptionPane.showMessageDialog(this, "Chave removida com sucesso!");
                        carregarKeys();
                    }
                }
            }
        });

        tableKeys.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            private void showPopup(MouseEvent e) {
                int row = tableKeys.rowAtPoint(e.getPoint());
                if (row != -1) {
                    tableKeys.setRowSelectionInterval(row, row);
                    popupMenuKeys.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        JPanel painelBaixo = new JPanel(new FlowLayout());
        tfKeyGerada = new JTextField(15); tfKeyGerada.setEditable(false);
        btnCopiar = new JButton("Copiar");
        btnCopiar.addActionListener(e -> {
            StringSelection selecao = new StringSelection(tfKeyGerada.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selecao, null);
            JOptionPane.showMessageDialog(this, "Key copiada!");
        });
        btnGerarKey = new JButton("Gerar Key");
        btnGerarKey.addActionListener(e -> {
            String novaKey = gerarKeyAleatoria();
            if (keyController.inserirKey(novaKey)) {
                tfKeyGerada.setText(novaKey);
                carregarKeys();
                JOptionPane.showMessageDialog(this, "Key gerada com sucesso!");
            }
        });
        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            new JPainel().setVisible(true);
            dispose();
        });

        painelBaixo.add(new JLabel("Key Gerada:"));
        painelBaixo.add(tfKeyGerada);
        painelBaixo.add(btnCopiar);
        painelBaixo.add(btnGerarKey);
        painelBaixo.add(btnVoltar);
        painelKeys.add(painelBaixo, BorderLayout.SOUTH);
    }

    private void initUsuariosPanel() {
        JPanel painelUsuarios = new JPanel(new BorderLayout());
        abasAdmin.addTab("Gerenciar Usuários", painelUsuarios);

        String[] colunas = {"Nome", "Usuário", "Admin"};
        tableModelUsuarios = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }
        };
        tableUsuarios = new JTable(tableModelUsuarios);
        painelUsuarios.add(new JScrollPane(tableUsuarios), BorderLayout.CENTER);

        popupMenuUsuarios = new JPopupMenu();
        removerUsuarioMenuItem = new JMenuItem("Remover Usuário");
        popupMenuUsuarios.add(removerUsuarioMenuItem);

        removerUsuarioMenuItem.addActionListener(e -> {
            int row = tableUsuarios.getSelectedRow();
            if (row != -1) {
                String usuario = (String) tableModelUsuarios.getValueAt(row, 1);
                if (JOptionPane.showConfirmDialog(this, "Remover usuário " + usuario + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (usuarioController.removerUsuario(usuario)) {
                        JOptionPane.showMessageDialog(this, "Usuário removido!");
                        carregarUsuarios();
                    }
                }
            }
        });

        tableUsuarios.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            private void showPopup(MouseEvent e) {
                int row = tableUsuarios.rowAtPoint(e.getPoint());
                if (row != -1) {
                    tableUsuarios.setRowSelectionInterval(row, row);
                    popupMenuUsuarios.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        btnAlternarAdminStatus = new JButton("Alternar Admin");
        btnAlternarAdminStatus.addActionListener(e -> {
            int row = tableUsuarios.getSelectedRow();
            if (row != -1) {
                String usuario = (String) tableModelUsuarios.getValueAt(row, 1);
                boolean isAdmin = (Boolean) tableModelUsuarios.getValueAt(row, 2);
                if (usuarioController.atualizarAdminStatus(usuario, !isAdmin)) {
                    JOptionPane.showMessageDialog(this, "Status alterado!");
                    carregarUsuarios();
                }
            }
        });

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAlternarAdminStatus);
        painelUsuarios.add(painelBotoes, BorderLayout.SOUTH);
    }

    private void initChamadosPanel() {
        JPanel painelChamados = new JPanel(new BorderLayout());
        abasAdmin.addTab("Gerenciar Chamados", painelChamados);

        String[] colunas = {"ID", "Usuário", "Assunto", "Status"};
        tableModelChamados = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableChamados = new JTable(tableModelChamados);
        painelChamados.add(new JScrollPane(tableChamados), BorderLayout.CENTER);

        btnCarregarChamados = new JButton("Carregar Chamados");
        btnCarregarChamados.addActionListener(e -> carregarChamados());

        tableChamados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableChamados.getSelectedRow();
                    if (row != -1) {
                        int id = (Integer) tableModelChamados.getValueAt(row, 0);
                        Chamado chamado = chamadoController.buscarChamadoPorId(id);
                        if (chamado != null) abrirTelaChat(chamado);
                    }
                }
            }
        });

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnCarregarChamados);
        painelChamados.add(painelBotoes, BorderLayout.SOUTH);
    }

    private void abrirTelaChat(Chamado chamado) {
    JFrame frame = new JFrame("Chat Chamado #" + chamado.getId());
    frame.setSize(600, 400);
    frame.setLayout(new BorderLayout());
    frame.setLocationRelativeTo(this);

    JTextArea areaChat = new JTextArea();
    areaChat.setEditable(false);

    java.time.format.DateTimeFormatter formatter =
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    chamado.getMensagens().forEach(m ->
        areaChat.append("[" + m.getDataHora().format(formatter) + "] " + m.getAutor() + ": " + m.getTexto() + "\n")
    );

    JTextField campo = new JTextField();
    JButton enviar = new JButton("Enviar");
    enviar.addActionListener(e -> {
        String texto = campo.getText().trim();
        if (!texto.isEmpty()) {
            MensagemChamado m = new MensagemChamado(adminUsuarioLogado, texto, LocalDateTime.now());
            chamado.addMensagem(m);
            chamadoController.salvarChamado(chamado);
            areaChat.append("[" + m.getDataHora().format(formatter) + "] " + m.getAutor() + ": " + m.getTexto() + "\n");
            campo.setText("");
        }
    });

    JLabel lblStatus = new JLabel("Status atual: " + chamado.getStatus());

    JButton alterarStatus = new JButton("Alterar Status");
    alterarStatus.addActionListener(e -> {
        String[] opcoes = {"Aberto", "Em Andamento", "Finalizado"};
        String novoStatus = (String) JOptionPane.showInputDialog(
            frame,
            "Selecione o novo status:",
            "Alterar Status",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opcoes,
            chamado.getStatus()
        );

        if (novoStatus != null && !novoStatus.equals(chamado.getStatus())) {
            chamado.setStatus(novoStatus);
            chamadoController.salvarChamado(chamado);
            lblStatus.setText("Status atual: " + chamado.getStatus());
            JOptionPane.showMessageDialog(frame, "Status atualizado!");
            carregarChamados();
        }
    });

    JButton excluirChamado = new JButton("Excluir Chamado");
    excluirChamado.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Deseja realmente excluir o chamado #" + chamado.getId() + "?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            chamadoController.removerChamado(chamado.getId());
            JOptionPane.showMessageDialog(frame, "Chamado excluído!");
            frame.dispose();
            carregarChamados();
        }
    });

    JPanel topo = new JPanel();
    topo.add(lblStatus);
    topo.add(alterarStatus);
    topo.add(excluirChamado);

    JPanel envio = new JPanel(new BorderLayout());
    envio.add(campo, BorderLayout.CENTER);
    envio.add(enviar, BorderLayout.EAST);

    frame.add(topo, BorderLayout.NORTH);
    frame.add(new JScrollPane(areaChat), BorderLayout.CENTER);
    frame.add(envio, BorderLayout.SOUTH);
    frame.setVisible(true);
}
    
    private void initAvisosPanel() {
    JPanel painelAvisos = new JPanel(new BorderLayout());
    abasAdmin.addTab("Criar Aviso", painelAvisos);

    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel lblTitulo = new JLabel("Título:");
    JTextField tfTitulo = new JTextField(20);

    JLabel lblMensagem = new JLabel("Mensagem:");
    JTextArea taMensagem = new JTextArea(5, 20);
    taMensagem.setLineWrap(true);
    taMensagem.setWrapStyleWord(true);

    JButton btnSalvar = new JButton("Salvar Aviso");

    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(lblTitulo, gbc);
    gbc.gridx = 1;
    formPanel.add(tfTitulo, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    formPanel.add(lblMensagem, gbc);
    gbc.gridx = 1;
    JScrollPane scrollMensagem = new JScrollPane(taMensagem);
    formPanel.add(scrollMensagem, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    formPanel.add(btnSalvar, gbc);

    painelAvisos.add(formPanel, BorderLayout.CENTER);

    btnSalvar.addActionListener(e -> {
        String titulo = tfTitulo.getText().trim();
        String mensagem = taMensagem.getText().trim();

        if (titulo.isEmpty() || mensagem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        model.Aviso aviso = new model.Aviso();
        aviso.setTitulo(titulo);
        aviso.setMensagem(mensagem);
        aviso.setCriadoPor(adminUsuarioLogado);

        controller.AvisoController avisoController = new controller.AvisoController();
        avisoController.salvarAviso(aviso);

        JOptionPane.showMessageDialog(this, "Aviso salvo com sucesso!");
        tfTitulo.setText("");
        taMensagem.setText("");
    });
}




    private void carregarKeys() {
        tableModelKeys.setRowCount(0);
        for (KeyRegistro k : keyController.listarKeys()) {
            tableModelKeys.addRow(new Object[]{k.getChave(), k.isUsada() ? "Sim" : "Não"});
        }
    }

    private void carregarUsuarios() {
        tableModelUsuarios.setRowCount(0);
        for (Usuario u : usuarioController.listarTodosUsuarios(adminUsuarioLogado)) {
            tableModelUsuarios.addRow(new Object[]{u.getNome(), u.getUsuario(), u.isAdmin()});
        }
    }

    private void carregarChamados() {
        tableModelChamados.setRowCount(0);
        for (Chamado c : chamadoController.listarChamados()) {
            tableModelChamados.addRow(new Object[]{c.getId(), c.getUsuario(), c.getTitulo(), c.getStatus()});
        }
    }

    private String gerarKeyAleatoria() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder key = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 8; i++) {
            if (i == 4) key.append("-");
            key.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return key.toString();
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
            .addGap(0, 400, Short.MAX_VALUE)
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
        java.awt.EventQueue.invokeLater(() -> new JPainelAdmin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
