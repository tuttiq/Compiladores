package Compilador.GUI;

import Compilador.Sintatico;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFileChooser;


public class Compilador extends javax.swing.JFrame {


    private JFileChooser chooser = new JFileChooser();
    private File sourceFile;
    private Sintatico analisadorSintatico;

    /** Creates new form Compilador */
    public Compilador() {
        initComponents();
        setTitle("Compilador =D");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblPath = new javax.swing.JLabel();
        btnLoad = new javax.swing.JButton();
        btnCompile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSaida = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSourceFile = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblPath.setText("Carregue o arquivo fonte.");

        btnLoad.setText("Carregar");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        btnCompile.setText("Compilar");
        btnCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompileActionPerformed(evt);
            }
        });

        txtSaida.setColumns(20);
        txtSaida.setRows(5);
        jScrollPane1.setViewportView(txtSaida);

        txtSourceFile.setBackground(new java.awt.Color(211, 211, 211));
        txtSourceFile.setColumns(20);
        txtSourceFile.setEditable(false);
        txtSourceFile.setFont(new java.awt.Font("Ubuntu Mono", 0, 15)); // NOI18N
        txtSourceFile.setRows(5);
        txtSourceFile.setDisabledTextColor(new java.awt.Color(1, 4, 78));
        jScrollPane2.setViewportView(txtSourceFile);

        jLabel1.setText("Output:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(lblPath, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 310, Short.MAX_VALUE)
                            .addComponent(btnLoad))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(btnCompile)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoad)
                    .addComponent(lblPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCompile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed

        chooser.setDialogTitle("Abrir...");

        if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
            sourceFile = chooser.getSelectedFile();
        else
            return;
        
        try {
            
        txtSourceFile.setText("");
        BufferedReader in = new BufferedReader(new FileReader(sourceFile));
        int n=1;
        while(in.ready())
        {
            txtSourceFile.append((n>9?n++:("0"+n++)) + " " + in.readLine() + "\n");
        }
        
        }catch(Exception ex) {
            txtSaida.setText("Erro ao ler o arquivo!\n" + ex.getClass() + ": " + ex.getMessage());
        }
        
        lblPath.setText(sourceFile.getPath());
        lblPath.updateUI();
     
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnCompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompileActionPerformed
        try{
        analisadorSintatico = new Sintatico(sourceFile);
        
            analisadorSintatico.execute();
            txtSaida.setText("Compilado com sucesso!");
        }
        catch(Exception erro)
        {
            txtSaida.setText( erro.getClass() + " " + erro.getMessage() );
            erro.printStackTrace();
        }
    }//GEN-LAST:event_btnCompileActionPerformed


        
  
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Compilador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCompile;
    private javax.swing.JButton btnLoad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPath;
    private javax.swing.JTextArea txtSaida;
    private javax.swing.JTextArea txtSourceFile;
    // End of variables declaration//GEN-END:variables

}
