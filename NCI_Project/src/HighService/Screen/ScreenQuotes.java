/*
 * The MIT License
 *
 * Copyright 2022 Meire de Jesus Torres.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package HighService.Screen;

import java.sql.*;
import HighService.data.Dataconnect;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//website https://sourceforge.net/projects/finalangelsanddemons/rs2jar search and use table
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
//this is libraries printer i-report
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Meire de Jesus Torres
 */
public class ScreenQuotes extends javax.swing.JInternalFrame {

    Connection cone = null;//conect databases
    ResultSet res = null;//prepare conection
    PreparedStatement Stat = null;//show or display result of conection
    //store text radion button selected
    private String type;

    /**
     * Creates new form ScreeenQuotes
     *///constructor
    public ScreenQuotes() {
        initComponents();
        cone = Dataconnect.conector();
    }
    
///////////////////////////////SEARCH SHORTCUT QUOTES///////////////////////////////////
    private void searchshortcut() {
        String sql = "select customerId as ID, name as Name, phone as Phone from customers  where name like ?";
        try {
            Stat = cone.prepareStatement(sql);
            Stat.setString(1, txtCusearch.getText() + "%");
            res = Stat.executeQuery();
            tbtcushortcut.setModel(DbUtils.resultSetToTableModel(res));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
///////////////////////////////////////SETFIELD/////////////////////////////////////////    
    private void setField() {
        int fotable = tbtcushortcut.getSelectedRow();

        txtcuid.setText(tbtcushortcut.getModel().getValueAt(fotable, 0).toString());
    }
///////////////////////////////////ISSUE QUOTE///////////////////////////////////////
    private void IssueQuote() {
        String sql = "insert into quote(type, status,description,staff,net,rate,customerId)values(?,?,?,?,?,?,?)";
        try {
            Stat = cone.prepareStatement(sql);
            Stat.setString(1, type);
            Stat.setString(2, cbstatus.getSelectedItem().toString());
            Stat.setString(3, txtqudescr.getText());
            Stat.setString(4, txtqusales.getText());
            Stat.setString(5, txtnet.getText());
            Stat.setString(6, cbrate.getSelectedItem().toString());
            Stat.setString(7, txtcuid.getText());
            if ((txtcuid.getText().isEmpty()) || (txtqudescr.getText().isEmpty()) || cbstatus.getSelectedItem().equals(" "))               {
                JOptionPane.showMessageDialog(null, "Please, fill required fields!");
            } else {
                
                int add = Stat.executeUpdate();//variable add
                if (add > 0) {//cheque if all fields were field and clean field
                    JOptionPane.showMessageDialog(null, "Invoice or Estimate was create successfuly!");
                    //last invoice number
                    lastinvoice();
                    btnqucreate.setEnabled(false);//disabling create invoice
                    btnqusearch.setEnabled(true);//
                    btnquprint.setEnabled(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

//////////////////////////////////SEARCH QUOTES///////////////////////////////////////////    
    private void searchQuote() {
        String quotenumber = JOptionPane.showInputDialog(null, "Enter invoice or estimate number!");
        String sql = "select quotation,date_format(date,'%d/%m/%Y - %H:%i'), type,status,description,staff,net,rate,vat,total,customerId from quote where quotation= " + quotenumber;
        try {
            Stat = cone.prepareStatement(sql);
            res = Stat.executeQuery();//show the response in 
            if (res.next()) {
                txtquotenum.setText(res.getString(1));//1 is quote i mySQl
                txtdate.setText(res.getString(2));//get datE from mysql
         
                //radion button
                String rbt = res.getString(3);//here i create a new variable rbt to set the ratio button because there is no input just click
                if (rbt.equals("Quote")) {
                    rbtquote.setSelected(true);
                    type = "Quote";
                } else {
                    rbtest.setSelected(true);
                    type = "Estimate";
                }
                cbstatus.setSelectedItem(res.getString(4));
                txtqudescr.setText(res.getString(5));
                txtqusales.setText(res.getString(6));
                txtnet.setText(res.getString(7));
                cbrate.setSelectedItem(res.getString(8)); 
                txtvat.setText(res.getString(9));                
                txtqutotal.setText(res.getString(10));               
                txtcuid.setText(res.getString(11));
                btnqucreate.setEnabled(false);//enable enable 
                btnqusearch.setEnabled(true);
                txtCusearch.setEnabled(false);//enable txt search
                tbtcushortcut.setVisible(false);//enable table
                btnquedit.setEnabled(true);
                btnquprint.setEnabled(true);
                btnqudelete.setEnabled(true);

            } else {
                JOptionPane.showMessageDialog(null, "Quote is no entered yet!!");
            }
        } catch (java.sql.SQLSyntaxErrorException e) {//WHERE IS EXCEPTION ERROR SHOW MESSAGE BELOW
            JOptionPane.showMessageDialog(null, "Invalid Invoice or Estimate!");

        } catch (Exception o) {
            JOptionPane.showMessageDialog(null, o);
        }
    }
    
//////////////////////////////////////EDIT INVOICE///////////////////////////////////////
    private void editquote() {
        String sql = "update quote set type=?,status=?,description=?,staff=?,net=?,rate=? where quotation =?";
        try {
            Stat = cone.prepareStatement(sql);
            Stat.setString(1, type);
            Stat.setString(2, cbstatus.getSelectedItem().toString());
            Stat.setString(3, txtqudescr.getText());
            Stat.setString(4, txtqusales.getText());
            Stat.setString(5, txtnet.getText());
            Stat.setString(6, cbrate.getSelectedItem().toString());
 //           Stat.setString(5, txtqutotal.getText());            
            Stat.setString(7, txtquotenum.getText());
            if ((txtcuid.getText().isEmpty()) || (txtqudescr.getText().isEmpty()) || cbstatus.getSelectedItem().equals(" ")) {
                JOptionPane.showMessageDialog(null, "Please, fill required fields");
            } else {
                int add = Stat.executeUpdate();//variable add
                if (add > 0) {//cheque if all fields were field and clean field
                    JOptionPane.showMessageDialog(null, "Updated Successfuly!");
                    cleancell();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
////////////////////////////////////////DELETE INVOICE//////////////////////////////////////    
    private void deletequote() {
        int confirm = JOptionPane.showConfirmDialog(null, "Do you want to delete this quotation?", "warning", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "delete from quote where quotation=?";
            try {
                Stat = cone.prepareStatement(sql);
                Stat.setString(1, txtquotenum.getText());
                int removed = Stat.executeUpdate();
                if (removed > 0) {
                    JOptionPane.showMessageDialog(null, "Deleted successfully!");
                    cleancell();
                }

            } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
   ////////////////////////////LAST INVOICE////////////////////////////////////////////////////
    private void lastinvoice(){
        String sql = "select max(quotation) from quote";
        try {
            Stat=cone.prepareStatement(sql);
            res = Stat.executeQuery();
            if(res.next()){
                txtquotenum.setText(res.getString(1));
            }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
        }
        
    }
   ///////////////////////////PRINT INVOICE/////////////////////////////////////////////////////
    
    private void print(){
        
        int printing = JOptionPane.showConfirmDialog(null, "Do you want to issue Invoice report?", "Warning",JOptionPane.YES_NO_OPTION);
        if(printing == JOptionPane.YES_OPTION){
            try {
                HashMap quotenumber = new HashMap();
                quotenumber.put("quotation",Integer.parseInt(txtquotenum.getText()));
               JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/Reports/report1.jasper"),quotenumber,cone);
//C:/Final Project/NCI_Project/src/Reports
               JasperViewer.viewReport(print,false);
                
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
  ////////////////////////CLEAN FIELDS COMAND/////////////////////////////////////////////////

    private void cleancell() {
        txtquotenum.setText(null);
        txtCusearch.setText(null);
        ((DefaultTableModel) tbtcushortcut.getModel()).setRowCount(0);//clean table cell
        cbstatus.setSelectedItem(" ");
        txtdate.setText(null);
        txtcuid.setText(null);
        txtqudescr.setText(null);
        txtqusales.setText(null);
        txtnet.setText(null);  
        cbrate.setSelectedItem(" ");
        txtvat.setText(null);
        txtqutotal.setText(null);
        btnqusearch.setEnabled(true);
        btnqucreate.setEnabled(true);//Enabling create new quote buton
        txtCusearch.setEnabled(true);//Enabling create new search shortcut customer
        tbtcushortcut.setVisible(true);//making the table customer visible again
        btnquedit.setEnabled(false);//i am enableing button edit quote
        btnqudelete.setEnabled(false);//i am enableing button delete quote
        btnquprint.setEnabled(false);//i am enableing button printer quote

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtquotenum = new javax.swing.JTextField();
        txtdate = new javax.swing.JTextField();
        rbtest = new javax.swing.JRadioButton();
        rbtquote = new javax.swing.JRadioButton();
        jTextField3 = new javax.swing.JTextField();
        cbstatus = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txtCusearch = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtcuid = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbtcushortcut = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtqusales = new javax.swing.JTextField();
        txtqutotal = new javax.swing.JTextField();
        btnqucreate = new javax.swing.JButton();
        btnqudelete = new javax.swing.JButton();
        btnquedit = new javax.swing.JButton();
        btnqusearch = new javax.swing.JButton();
        btnquprint = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtqudescr = new javax.swing.JTextArea();
        txtnet = new javax.swing.JTextField();
        txtvat = new javax.swing.JTextField();
        cbrate = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Invoices");
        setPreferredSize(new java.awt.Dimension(699, 625));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Invoice Nº");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Date");

        txtquotenum.setEnabled(false);
        txtquotenum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtquotenumActionPerformed(evt);
            }
        });

        txtdate.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtdate.setEnabled(false);
        txtdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdateActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtest);
        rbtest.setText("Estimate");
        rbtest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtestActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtquote);
        rbtquote.setText("Invoice");
        rbtquote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtquoteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtest)
                        .addGap(18, 18, 18)
                        .addComponent(rbtquote, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtquotenum, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 58, Short.MAX_VALUE))
                            .addComponent(txtdate))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtquotenum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtest)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addComponent(rbtquote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTextField3.setText("Status");

        cbstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Entered", "Ready for collection", "Not eady for collection", "Returned", "Forgotten" }));
        cbstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbstatusActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel2.setToolTipText("");

        txtCusearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCusearchActionPerformed(evt);
            }
        });
        txtCusearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCusearchKeyReleased(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/zoom1.png"))); // NOI18N

        jLabel4.setText("*ID");

        txtcuid.setEditable(false);
        txtcuid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcuidActionPerformed(evt);
            }
        });

        tbtcushortcut.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Name", "Phone"
            }
        ));
        tbtcushortcut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbtcushortcutMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbtcushortcut);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtCusearch, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtcuid, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtCusearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtcuid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel5.setText("* Description");

        jLabel6.setText("Representative");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Total");

        txtqutotal.setText("0");
        txtqutotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtqutotalActionPerformed(evt);
            }
        });

        btnqucreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/create.png"))); // NOI18N
        btnqucreate.setToolTipText("Add");
        btnqucreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnqucreate.setPreferredSize(new java.awt.Dimension(80, 80));
        btnqucreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnqucreateActionPerformed(evt);
            }
        });

        btnqudelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/delete.png"))); // NOI18N
        btnqudelete.setToolTipText("Delete");
        btnqudelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnqudelete.setEnabled(false);
        btnqudelete.setPreferredSize(new java.awt.Dimension(80, 80));
        btnqudelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnqudeleteActionPerformed(evt);
            }
        });

        btnquedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/edit.png"))); // NOI18N
        btnquedit.setToolTipText("Edit");
        btnquedit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnquedit.setEnabled(false);
        btnquedit.setPreferredSize(new java.awt.Dimension(80, 80));
        btnquedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnqueditActionPerformed(evt);
            }
        });

        btnqusearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/search.png"))); // NOI18N
        btnqusearch.setToolTipText("Search");
        btnqusearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnqusearch.setPreferredSize(new java.awt.Dimension(80, 80));
        btnqusearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnqusearchActionPerformed(evt);
            }
        });

        btnquprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/printer.png"))); // NOI18N
        btnquprint.setToolTipText("Print");
        btnquprint.setEnabled(false);
        btnquprint.setPreferredSize(new java.awt.Dimension(80, 80));
        btnquprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnquprintActionPerformed(evt);
            }
        });

        txtqudescr.setColumns(20);
        txtqudescr.setRows(5);
        jScrollPane2.setViewportView(txtqudescr);

        txtnet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnetActionPerformed(evt);
            }
        });

        txtvat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtvatMouseClicked(evt);
            }
        });
        txtvat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtvatActionPerformed(evt);
            }
        });

        cbrate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "0.23", "0.135", "0.09" }));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Nett");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Vat");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnqucreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(41, 41, 41)
                                        .addComponent(btnqusearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(btnquedit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)
                                        .addComponent(btnqudelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)
                                        .addComponent(btnquprint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cbrate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(txtvat, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap(334, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(58, 58, 58)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtnet, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtqutotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))))))
                        .addGap(14, 14, 14))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 52, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(496, 496, 496))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(13, 13, 13))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(36, 36, 36)
                                        .addComponent(cbstatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtqusales, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtqusales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtnet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtvat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbrate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtqutotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(69, 69, 69)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnqusearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnqucreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnquprint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnqudelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnquedit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(94, Short.MAX_VALUE))
        );

        setBounds(0, 0, 699, 625);
    }// </editor-fold>//GEN-END:initComponents

    private void btnqucreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnqucreateActionPerformed
        // calling method add
        IssueQuote();

    }//GEN-LAST:event_btnqucreateActionPerformed

    private void btnqudeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnqudeleteActionPerformed
        // call method delete
        deletequote();

    }//GEN-LAST:event_btnqudeleteActionPerformed

    private void btnqueditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnqueditActionPerformed
        //call method update
        editquote();

    }//GEN-LAST:event_btnqueditActionPerformed

    private void btnqusearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnqusearchActionPerformed
        // call search method
        searchQuote();

    }//GEN-LAST:event_btnqusearchActionPerformed

    private void txtCusearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCusearchActionPerformed
        // CALL METHOD SEARCH CUSTOMER SHORTCUT
        // searchshortcut();
    }//GEN-LAST:event_txtCusearchActionPerformed

    private void txtCusearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCusearchKeyReleased
        // CALL METHOD SEARCH CUSTOMER SHORTCUT GETS CUSTOMER ID AND SET IN ID FIELD.
        searchshortcut();
    }//GEN-LAST:event_txtCusearchKeyReleased

    private void tbtcushortcutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbtcushortcutMouseClicked
        // CALL METHOD SET FIELD THE USER CAN CLICK IN THE LINE 
        setField();
    }//GEN-LAST:event_tbtcushortcutMouseClicked

    private void rbtestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtestActionPerformed
        // TODO add your handling code here:
        //if radion button is selected then acctivate variable type String = text
        type = "Estimate";
    }//GEN-LAST:event_rbtestActionPerformed

    private void rbtquoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtquoteActionPerformed
        // TODO add your handling code here:
        //if radion button is selected then acctivate variable type String = text
        type = "Quote";
    }//GEN-LAST:event_rbtquoteActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // when quote page is open radion button estimate has to be selected.
        rbtest.setSelected(true);
        type = "Estimate";
    }//GEN-LAST:event_formInternalFrameOpened

    private void cbstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbstatusActionPerformed

    private void txtdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdateActionPerformed

    private void txtcuidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcuidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcuidActionPerformed

    private void btnquprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnquprintActionPerformed
        // method print report
        print();
    }//GEN-LAST:event_btnquprintActionPerformed

    private void txtnetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnetActionPerformed

    private void txtqutotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtqutotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtqutotalActionPerformed

    private void txtvatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtvatActionPerformed
        // TODO add your handling code here:

        

    }//GEN-LAST:event_txtvatActionPerformed

    private void txtquotenumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtquotenumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtquotenumActionPerformed

    private void txtvatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtvatMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_txtvatMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnqucreate;
    private javax.swing.JButton btnqudelete;
    private javax.swing.JButton btnquedit;
    private javax.swing.JButton btnquprint;
    private javax.swing.JButton btnqusearch;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbrate;
    private javax.swing.JComboBox<String> cbstatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JRadioButton rbtest;
    private javax.swing.JRadioButton rbtquote;
    private javax.swing.JTable tbtcushortcut;
    private javax.swing.JTextField txtCusearch;
    private javax.swing.JTextField txtcuid;
    private javax.swing.JTextField txtdate;
    private javax.swing.JTextField txtnet;
    private javax.swing.JTextArea txtqudescr;
    private javax.swing.JTextField txtquotenum;
    private javax.swing.JTextField txtqusales;
    private javax.swing.JTextField txtqutotal;
    private javax.swing.JTextField txtvat;
    // End of variables declaration//GEN-END:variables
}
