/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Forms;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DBException;
import org.javalite.activejdbc.LazyList;
import test.test.Helpers.ADHhelper;
import test.test.Models.BukuModel;
import test.test.Models.PeminjamanModel;
import test.test.Models.SiswaModel;
import test.test.Reports.Config;

/**
 *
 * @author user
 */
public class Peminjaman extends javax.swing.JFrame {
    private List<Integer> comboSiswaID = new ArrayList<Integer>();
    private int comboSiswaIndex;
    private int selectedComboSiswaIndex;
    
    private List<Integer> comboBukuID = new ArrayList<Integer>();
    private int comboBukuIndex;
    private int selectedComboBukuIndex;
    
    private DefaultTableModel model = new DefaultTableModel();
    private String ID;
    private String state;
    /**
     * Creates new form PangkatGol
     */
    public Peminjaman() {
        initComponents();
                
        loadTable();
                
        TextCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                cari();
            }

            public void removeUpdate(DocumentEvent e) {
                cari();
            }

            public void changedUpdate(DocumentEvent e) {
                cari();
            }
        });
        
        loadComboBox();
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void loadComboBox() {
        Siswa.removeAllItems();
        
        Base.open();
        LazyList<SiswaModel> siswas = SiswaModel.findAll();
        Base.close();
        
        Base.open();
        for(SiswaModel siswa : siswas) {
            Base.close();
            comboSiswaID.add(Integer.parseInt(siswa.getString("id")));
            Siswa.addItem(siswa.getString("nama"));
            Base.open();
        }
        Base.close();

        Buku.removeAllItems();
        
        Base.open();
        LazyList<BukuModel> bukus = BukuModel.findAll();
        Base.close();
        
        Base.open();
        for(BukuModel buku : bukus) {
            Base.close();
            comboBukuID.add(Integer.parseInt(buku.getString("id")));
            Buku.addItem(buku.getString("judul"));
            Base.open();
        }
        Base.close();
    }
    
    public void cari() {
        if (TextCari.getText().equals("")) {
            loadTable();
        } else {
            loadTable(TextCari.getText());
        }
    }
    
    private void loadTableHelper(LazyList<PeminjamanModel> peminjamans) {
        model = new DefaultTableModel();
                
        model.addColumn("#ID");
        model.addColumn("Nama");
        model.addColumn("Buku");
        model.addColumn("Tanggal");
        
        Base.open();
        
        try {
            for(PeminjamanModel peminjaman : peminjamans) {          
                SiswaModel siswa = peminjaman.parent(SiswaModel.class);
                BukuModel buku = peminjaman.parent(BukuModel.class);
                model.addRow(new Object[]{
                    peminjaman.getId(),
                    siswa.getString("nama"),
                    buku.getString("judul"),
                    ADHhelper.tanggalIndo(peminjaman.getString("tanggal_peminjaman")),
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
        
        TablePegawai.setModel(model);
        
        setState("index");
    }
    
    private void formatTextInteger() {
       
    }
    
    private void loadTable() {
        Base.open();
        LazyList<PeminjamanModel> peminjamans = PeminjamanModel.findAll();
        Base.close();
        
        loadTableHelper(peminjamans);
    }

    private void loadTable(String cari) {
        Base.open();
        LazyList<PeminjamanModel> peminjamans = PeminjamanModel.findBySQL("SELECT p.* FROM peminjaman p, siswa s, buku b WHERE p.id_siswa = s.id AND p.id_buku = b.id AND (s.nama like ? OR b.judul like ?)", '%' + cari + '%', '%' + cari + '%');
        Base.close();
        
        loadTableHelper(peminjamans);
    }

    
    private void hapusData() {
        Base.open();
        PeminjamanModel peminjaman = PeminjamanModel.findById(ID);
        try {
            peminjaman.delete();
        } catch (DBException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        Base.close();
    }
    
    private void setState(String IndexOrEdit) {
        if (IndexOrEdit.equals("index")) {
            ButtonTambahUbah.setText("Tambah");
            ButtonResetHapus.setText("Reset");
            
            state = IndexOrEdit;
        } else if (IndexOrEdit.equals("edit")) {
            ButtonTambahUbah.setText("Ubah");
            ButtonResetHapus.setText("Hapus");
            
            state = IndexOrEdit;
        } else {
            JOptionPane.showMessageDialog(null, "Index/Edit ?");
        }
    }
    
    private void tambahData() {
        Base.open();
        try {
            PeminjamanModel peminjaman = new PeminjamanModel();
            peminjaman.set("id_siswa", selectedComboSiswaIndex);
            peminjaman.set("id_buku", selectedComboBukuIndex);
            peminjaman.set("tanggal_peminjaman", ADHhelper.parseTanggal(Tanggal.getDate()));
            peminjaman.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }
    
    private void ubahData() {
        Base.open();
        try {
            PeminjamanModel peminjaman = PeminjamanModel.findById(ID);
            peminjaman.set("id_siswa", selectedComboSiswaIndex);
            peminjaman.set("id_buku", selectedComboBukuIndex);
            peminjaman.set("tanggal_peminjaman", ADHhelper.parseTanggal(Tanggal.getDate()));
            peminjaman.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }

    private void resetForm() {
        Siswa.setSelectedIndex(0);
        Buku.setSelectedIndex(0);
        Tanggal.setDate(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ScrollPane = new javax.swing.JScrollPane();
        TablePegawai = new javax.swing.JTable();
        TextCari = new javax.swing.JTextField();
        LabelCari = new javax.swing.JLabel();
        LabelCari1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ButtonRefresh = new javax.swing.JButton();
        ButtonTambahUbah = new javax.swing.JButton();
        ButtonResetHapus = new javax.swing.JButton();
        Siswa = new javax.swing.JComboBox<>();
        LabelCari2 = new javax.swing.JLabel();
        LabelCari4 = new javax.swing.JLabel();
        Tanggal = new com.toedter.calendar.JDateChooser();
        Buku = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Peminjaman");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        TablePegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TablePegawai.getTableHeader().setReorderingAllowed(false);
        TablePegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablePegawaiMouseClicked(evt);
            }
        });
        ScrollPane.setViewportView(TablePegawai);

        TextCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextCariActionPerformed(evt);
            }
        });

        LabelCari.setText("Cari");

        LabelCari1.setText("Siswa");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("PEMINJAMAN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(185, 185, 185)
                .addComponent(jLabel1)
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ButtonRefresh.setText("Refresh");
        ButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonRefreshActionPerformed(evt);
            }
        });

        ButtonTambahUbah.setText("Tambah");
        ButtonTambahUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonTambahUbahActionPerformed(evt);
            }
        });

        ButtonResetHapus.setText("Reset");
        ButtonResetHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonResetHapusActionPerformed(evt);
            }
        });

        Siswa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Siswa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SiswaItemStateChanged(evt);
            }
        });
        Siswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SiswaActionPerformed(evt);
            }
        });

        LabelCari2.setText("Buku");

        LabelCari4.setText("Tanggal");

        Tanggal.setDateFormatString("dd-MM-yyyy");

        Buku.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Buku.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                BukuItemStateChanged(evt);
            }
        });
        Buku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BukuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(ButtonTambahUbah)
                            .addGap(40, 40, 40)
                            .addComponent(ButtonRefresh)
                            .addGap(44, 44, 44)
                            .addComponent(ButtonResetHapus)
                            .addGap(131, 131, 131))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(LabelCari)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(TextCari))
                        .addComponent(ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(LabelCari2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LabelCari4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LabelCari1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Siswa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Buku, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCari1)
                    .addComponent(Siswa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCari2)
                    .addComponent(Buku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCari4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonTambahUbah)
                    .addComponent(ButtonRefresh)
                    .addComponent(ButtonResetHapus))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRefreshActionPerformed
        resetForm();
        loadTable();
        TextCari.setText("");
    }//GEN-LAST:event_ButtonRefreshActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        new MenuUtama().setVisible(true);
        
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void TablePegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablePegawaiMouseClicked
        int i =TablePegawai.getSelectedRow();
        if(i>=0){
            ID = model.getValueAt(i, 0).toString();

            Base.open();
            PeminjamanModel peminjaman = PeminjamanModel.findById(ID);
            Base.close();
            
            Siswa.setSelectedIndex(comboSiswaID.indexOf(Integer.parseInt(peminjaman.getString("id_siswa"))));
            Buku.setSelectedIndex(comboBukuID.indexOf(Integer.parseInt(peminjaman.getString("id_buku"))));
            try {
                Tanggal.setDate(ADHhelper.getTanggalFromDB(peminjaman.getString("tanggal_peminjaman")));
            } catch (ParseException ex) {
                Logger.getLogger(Peminjaman.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            setState("edit");
        }
    }//GEN-LAST:event_TablePegawaiMouseClicked

    private void ButtonTambahUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonTambahUbahActionPerformed
        if (state.equals("index")) {
            if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else {
                tambahData();
                resetForm();
                loadTable();
            }
        } else {
            if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else {
                ubahData();
                resetForm();
                loadTable();
            }
        }
    }//GEN-LAST:event_ButtonTambahUbahActionPerformed

    private void ButtonResetHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonResetHapusActionPerformed
        if (state.equals("edit")) {
            hapusData();
            loadTable();
        }

        resetForm();
    }//GEN-LAST:event_ButtonResetHapusActionPerformed

    private void TextCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextCariActionPerformed
        cari();
    }//GEN-LAST:event_TextCariActionPerformed

    private void SiswaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SiswaItemStateChanged

    }//GEN-LAST:event_SiswaItemStateChanged

    private void SiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SiswaActionPerformed
        comboSiswaIndex = Siswa.getSelectedIndex();
        if (comboSiswaIndex >= 0) {
            selectedComboSiswaIndex = comboSiswaID.get(comboSiswaIndex);
        }
    }//GEN-LAST:event_SiswaActionPerformed

    private void BukuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_BukuItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_BukuItemStateChanged

    private void BukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BukuActionPerformed
        comboBukuIndex = Buku.getSelectedIndex();
        if (comboBukuIndex >= 0) {
            selectedComboBukuIndex = comboBukuID.get(comboBukuIndex);
        }
    }//GEN-LAST:event_BukuActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Peminjaman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Buku;
    private javax.swing.JButton ButtonRefresh;
    private javax.swing.JButton ButtonResetHapus;
    private javax.swing.JButton ButtonTambahUbah;
    private javax.swing.JLabel LabelCari;
    private javax.swing.JLabel LabelCari1;
    private javax.swing.JLabel LabelCari2;
    private javax.swing.JLabel LabelCari4;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JComboBox<String> Siswa;
    private javax.swing.JTable TablePegawai;
    private com.toedter.calendar.JDateChooser Tanggal;
    private javax.swing.JTextField TextCari;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
