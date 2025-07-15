package view;

import controller.ChamadoController;
import model.Chamado;
import model.MensagemChamado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaChamadosUsuario extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TelaChamadosUsuario.class.getName());

    private String usuarioLogado;
    private ChamadoController chamadoController;
    private JTable tableChamados;
    private DefaultTableModel tableModelChamados;

    public TelaChamadosUsuario(String usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.chamadoController = new ChamadoController();

        setTitle("Painel do Usuário - Logado como: " + usuarioLogado);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Assunto", "Status"};
        tableModelChamados = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // impede edição direta na tabela
            }
        };
        tableChamados = new JTable(tableModelChamados);
        JScrollPane scrollPane = new JScrollPane(tableChamados);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnNovoChamado = new JButton("Novo Chamado");
        JButton btnVoltar = new JButton("Voltar");

        btnVoltar.addActionListener(e -> {
            new JPainelUsuario(usuarioLogado).setVisible(true);
            dispose();
        });

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnNovoChamado);
        painelBotoes.add(btnVoltar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnNovoChamado.addActionListener(e -> criarNovoChamado());

        tableChamados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirChamadoSelecionado();
                }
            }
        });

        carregarChamadosDoUsuario();
    }

    private void criarNovoChamado() {
        // aq pede a msg inicial
        String assunto = JOptionPane.showInputDialog(this, "Assunto do chamado:");
        if (assunto != null && !assunto.isBlank()) {
            String mensagem = JOptionPane.showInputDialog(this, "Mensagem inicial:");
            if (mensagem != null && !mensagem.isBlank()) {
                chamadoController.criarChamado(assunto, usuarioLogado, mensagem);
                carregarChamadosDoUsuario();
                JOptionPane.showMessageDialog(this, "Chamado criado com sucesso!");
            }
        }
    }

    private void abrirChamadoSelecionado() {
        int row = tableChamados.getSelectedRow();
        if (row != -1) {
            //busca o id do usuario
            int id = (Integer) tableModelChamados.getValueAt(row, 0);
            Chamado chamado = chamadoController.buscarChamadoPorId(id);
            if (chamado != null) {
                abrirTelaChat(chamado);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um chamado para abrir!");
        }
    }

    private void abrirTelaChat(Chamado chamado) {
        JFrame chatFrame = new JFrame("Chat do Chamado #" + chamado.getId());
        chatFrame.setSize(500, 400);
        chatFrame.setLocationRelativeTo(this);
        chatFrame.setLayout(new BorderLayout());

        JTextArea areaChat = new JTextArea();
        areaChat.setEditable(false);

        // carrega/atualiza todas as mensagens já existentes no chamado
        List<MensagemChamado> mensagens = chamado.getMensagens();
        if (mensagens != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (MensagemChamado m : mensagens) {
                // formato de como aparece no "chat"
                areaChat.append("[" + m.getDataHora().format(formatter) + "] "
                        + m.getAutor() + ": " + m.getTexto() + "\n");
            }
        }

        JTextField campoMensagem = new JTextField();
        JButton btnEnviar = new JButton("Enviar");

        btnEnviar.addActionListener(e -> {
            String texto = campoMensagem.getText().trim();
            if (!texto.isEmpty()) {
                MensagemChamado novaMensagem = new MensagemChamado(
                        usuarioLogado,
                        texto,
                        LocalDateTime.now()
                );

                chamado.addMensagem(novaMensagem);

                chamadoController.salvarChamado(chamado);

                // atualiza o chat na tela
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                areaChat.append("[" + novaMensagem.getDataHora().format(formatter) + "] "
                        + usuarioLogado + ": " + texto + "\n");

                campoMensagem.setText("");
            }
        });

        JPanel painelEnvio = new JPanel(new BorderLayout());
        painelEnvio.add(campoMensagem, BorderLayout.CENTER);
        painelEnvio.add(btnEnviar, BorderLayout.EAST);

        chatFrame.add(new JScrollPane(areaChat), BorderLayout.CENTER);
        chatFrame.add(painelEnvio, BorderLayout.SOUTH);
        chatFrame.setVisible(true);
    }

    private void carregarChamadosDoUsuario() {
        tableModelChamados.setRowCount(0);
        //carrega todos os chamados
        List<Chamado> chamados = chamadoController.listarChamadosPorUsuario(usuarioLogado);
        for (Chamado c : chamados) {
            tableModelChamados.addRow(new Object[]{
                    c.getId(),
                    c.getTitulo(),
                    c.getStatus()
            });
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
        java.awt.EventQueue.invokeLater(() -> new TelaChamadosUsuario("usuario_teste").setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
