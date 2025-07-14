/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.AvisoController;
import model.Aviso;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaAvisos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TelaAvisos.class.getName());
    private AvisoController avisoController;

    private JTextArea areaAvisos;
    private String usuarioLogado;
    
    private JList<Aviso> listaAvisos;
    private DefaultListModel<Aviso> modeloAvisos;   
    
    public TelaAvisos(String usuarioLogado) {
    initComponents(); 
    avisoController = new AvisoController(); 
    configurarLayout(); 
    carregarAvisos(); 
    }
    
    private void configurarLayout() {
        setTitle("Central de Avisos");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modeloAvisos = new DefaultListModel<>();
        listaAvisos = new JList<>(modeloAvisos);
        listaAvisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaAvisos.setFont(new Font("Arial", Font.PLAIN, 14));
        
     listaAvisos.setCellRenderer(new ListCellRenderer<Aviso>() {
    @Override
    public Component getListCellRendererComponent(
            JList<? extends Aviso> list,
            Aviso aviso,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        java.time.format.DateTimeFormatter fmt =
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        JLabel lblTitulo = new JLabel(aviso.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblData = new JLabel(aviso.getDataPublicacao().format(fmt));
        lblData.setFont(new Font("Arial", Font.PLAIN, 12));
        lblData.setForeground(Color.DARK_GRAY);

        JTextArea lblMensagem = new JTextArea(aviso.getMensagem());
        lblMensagem.setFont(new Font("Arial", Font.PLAIN, 13));
        lblMensagem.setLineWrap(true);
        lblMensagem.setWrapStyleWord(true);
        lblMensagem.setEditable(false);
        lblMensagem.setOpaque(false);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblMensagem, BorderLayout.CENTER);
        panel.add(lblData, BorderLayout.SOUTH);

        if (isSelected) {
            panel.setBackground(new Color(220, 240, 255));
        } else {
            panel.setBackground(Color.WHITE);
        }

        return panel;
    }
});

        listaAvisos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Aviso selecionado = listaAvisos.getSelectedValue();
                    if (selecionado != null) {
                        abrirDetalhesAviso(selecionado);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(listaAvisos);
        getContentPane().add(scroll, BorderLayout.CENTER);
        
        JButton btnVoltar = new JButton("Voltar");
btnVoltar.addActionListener(e -> {
    new JPainelUsuario(usuarioLogado).setVisible(true);
    dispose();
});

JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
painelBotoes.add(btnVoltar);

getContentPane().add(painelBotoes, BorderLayout.SOUTH);

    }

    private void carregarAvisos() {
    modeloAvisos.clear();
    List<Aviso> avisos = avisoController.listarAvisos();
    for (Aviso aviso : avisos) {
        modeloAvisos.addElement(aviso);
    }
    if (modeloAvisos.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nenhum aviso disponível.", "Avisos", JOptionPane.INFORMATION_MESSAGE);
    }
}

    private void abrirDetalhesAviso(Aviso aviso) {
    JFrame detalhes = new JFrame("Aviso - " + aviso.getTitulo());
    detalhes.setSize(500, 300);
    detalhes.setLocationRelativeTo(this);
    detalhes.setLayout(new BorderLayout());

    JTextArea detalhesArea = new JTextArea();
    detalhesArea.setEditable(false);
    detalhesArea.setFont(new Font("Arial", Font.PLAIN, 14));
    detalhesArea.setLineWrap(true);
    detalhesArea.setWrapStyleWord(true);

    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    String dataFormatada = aviso.getDataPublicacao().format(formatter);

    detalhesArea.setText(
        "Título: " + aviso.getTitulo() + "\n\n" +
        aviso.getMensagem() + "\n\n" +
        "Publicado por: " + aviso.getCriadoPor() +
        " em " + dataFormatada
    );

    JButton btnVoltar = new JButton("Voltar");
    btnVoltar.addActionListener(e -> detalhes.dispose());

    detalhes.add(new JScrollPane(detalhesArea), BorderLayout.CENTER);
    detalhes.add(btnVoltar, BorderLayout.SOUTH);
    detalhes.setVisible(true);
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
        java.awt.EventQueue.invokeLater(() -> new TelaAvisos("usuario_teste").setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
