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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//website https://sourceforge.net/projects/finalangelsanddemons/rs2jar search and use table
import net.proteanit.sql.DbUtils;


/**
 *
 * @author Meire de Jesus Torres
 */
public class ScreenCustomer extends javax.swing.JInternalFrame {
    
    Connection cone = null;//conect databases
    ResultSet res = null;//prepare conection
    PreparedStatement Stat = null;//show or display result of conection

    /**
     * Creates new form ScreenCustomer
     */
    public ScreenCustomer() {
        initComponents();
        cone = Dataconnect.conector();
    }
   //////////////CREATE A NEW CUSTOMER////////////////////////  

    private void add(){//getting from the app inputed by user and registering in databe mySQL
        String sql = "insert into customers(name, address,phone,email)values(?,?,?,?)";
        try {
            Stat = cone.prepareStatement(sql);
            Stat.setString(1, txtcuname.getText());// insert name
            Stat.setString(2, txtcuaddress.getText());//insert address
            Stat.setString(3, txtcuphone.getText());//insert phone
            Stat.setString(4, txtcuemail.getText());//insert email
 
            //validate required fields
            if ((txtcuname.getText().isEmpty())||(txtcuphone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Can not be empty");
            } else {
            //confirm that input was successfuly
            int inputdata = Stat.executeUpdate();
            System.out.println(inputdata);//check if variable inputdata is greater then 0 if yes it working properly
            if (inputdata > 0) {
                JOptionPane.showMessageDialog(null, "Custumer registered successfully");
                cleancell();
             
                }
            }
            //  Stat.executeUpdate();//inset data
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
   ////////////////SEARCH CUSTOMER//////////////////
    //THIS SEARCH SYSTEM WITH FILTER
    private void SearchCus(){
        String sql = "select customerId as ID, name as Name, address as Address, phone as Phone, email as Email from customers  where name like ?";
        try {
            Stat = cone.prepareStatement(sql);
            //search in my database
            Stat.setString(1,txtcusearch.getText() + "%");
            res = Stat.executeQuery();
            //I am using rs2xml library to fullfill field table
            tblcu.setModel(DbUtils.resultSetToTableModel(res));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
       ////////////////LINK TABLE TO THE CUSTOMER FORM//////////////////
 
    public void tbllink(){
        int fotable = tblcu.getSelectedRow();
        
        txtcuid.setText(tblcu.getModel().getValueAt(fotable, 0).toString());       
        txtcuname.setText((String) tblcu.getModel().getValueAt(fotable, 1));
        txtcuaddress.setText((String) tblcu.getModel().getValueAt(fotable, 2));
        txtcuphone.setText((String) tblcu.getModel().getValueAt(fotable, 3));
        txtcuemail.setText((String) tblcu.getModel().getValueAt(fotable, 4)); 
        //BLOCK BUTTON CREATE AFTER SEARCH 
        btncucreate.setEnabled(false);
    }
    
    /////////////////////DELETE CUSTOMER////////////////////////////////////
     private void delet(){
        //confirm user deleted
        int confirm = JOptionPane.showConfirmDialog(null,"Do you want to delete this Customer?","warning",JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION){
            String sql = "delete from customers where customerId=?";
            try{
                Stat = cone.prepareStatement(sql);
                Stat.setString(1,txtcuid.getText());
                int removed = Stat.executeUpdate();
                if(removed>0){
                    JOptionPane.showMessageDialog(null, "Customer deleted successfully");
                    cleancell();
                    btncucreate.setEnabled(true);
                }
                
            }catch(Exception e){
                
            }
        }
    } 
     //////////////////////CLEAN FIELDS AND TABLE AFTER COMAND/////////////////////////////////////////////////
    private void cleancell(){
                txtcusearch.setText(null);
                txtcuid.setText(null);
                txtcuname.setText(null);
                txtcuaddress.setText(null);
                txtcuphone.setText(null);
                txtcuemail.setText(null);
                ((DefaultTableModel) tblcu.getModel()).setRowCount(0);//clean table cell
        
        
    }
    
    ////////////////////EDIT CUSTOMER//////////////////////////////////////
    
        private void edit(){
     String sql = "update customers set name=?,address=?,phone=?,email=? where customerId=?";   
        try {
            Stat = cone.prepareStatement(sql);
            Stat.setString(1, txtcuname.getText());
            Stat.setString(2, txtcuaddress.getText());
            Stat.setString(3, txtcuphone.getText());
            Stat.setString(4, txtcuemail.getText());
            Stat.setString(5, txtcuid.getText());//WHAT IS IN THE LAST STATEMENT WILL MATCH WITH TABLE WHERE
//THIS STATMENT DISPLAY A MESSAGE IF USER DOESN'T FILL ALL REQUIRED FIELDS
            if ((txtcuname.getText().isEmpty()) || (txtcuphone.getText().isEmpty()))
                {
                JOptionPane.showMessageDialog(null, "Fields Name and Phone cannot be empty, please enter required fields!");
            } else {
//THIS STATMENT DISPLAY A MESSAGE IF USER FULLFILL ALL REQUIRED FIELDS THE UPDATE WAS SUCCESSFUL AND CLEAN FORM FIELDS FOR NEXT ACTION
                int inputdata = Stat.executeUpdate();
                System.out.println(inputdata);//check if variable inputdata is > 0 if yes it working properly
                if (inputdata > 0) {
                    JOptionPane.showMessageDialog(null, "Update successfully");
                    cleancell();
                    btncucreate.setEnabled(true);
                }  
            }
        }catch(Exception e){
          JOptionPane.showMessageDialog(null, e);
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

        txtcuaddress = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtcuemail = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btncucreate = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btncudelete = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btncuedit = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtcuname = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtcuphone = new javax.swing.JTextField();
        txtcusearch = new javax.swing.JTextField();
        btncusearch = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblcu = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtcuid = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Customer");
        setPreferredSize(new java.awt.Dimension(699, 625));

        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setText("*");

        jLabel2.setText("Name");

        btncucreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/create.png"))); // NOI18N
        btncucreate.setToolTipText("Add");
        btncucreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btncucreate.setPreferredSize(new java.awt.Dimension(80, 80));
        btncucreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncucreateActionPerformed(evt);
            }
        });

        jLabel3.setText("Address");

        btncudelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/delete.png"))); // NOI18N
        btncudelete.setToolTipText("Delete");
        btncudelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btncudelete.setPreferredSize(new java.awt.Dimension(80, 80));
        btncudelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncudeleteActionPerformed(evt);
            }
        });

        jLabel4.setText("Email");

        btncuedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/edit.png"))); // NOI18N
        btncuedit.setToolTipText("Edit");
        btncuedit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btncuedit.setPreferredSize(new java.awt.Dimension(80, 80));
        btncuedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncueditActionPerformed(evt);
            }
        });

        jLabel6.setText("Phone");

        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("* Required");

        jLabel8.setForeground(new java.awt.Color(255, 0, 0));

        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");

        txtcusearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcusearchActionPerformed(evt);
            }
        });
        txtcusearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcusearchKeyReleased(evt);
            }
        });

        btncusearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/zoom.png"))); // NOI18N

        tblcu = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblcu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Address", "Phone", "Email"
            }
        ));
        tblcu.setFocusable(false);
        tblcu.getTableHeader().setReorderingAllowed(false);
        tblcu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblcuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblcu);

        jLabel1.setText("ID");

        txtcuid.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtcusearch, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btncusearch)
                        .addGap(254, 254, 254)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtcuphone, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtcuname)
                                .addComponent(txtcuaddress, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtcuemail, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btncucreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(btncudelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)
                                .addComponent(btncuedit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtcuid, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(671, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txtcusearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btncusearch, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtcuid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtcuname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtcuaddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel9)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(6, 6, 6))))
                        .addGap(18, 18, 18)
                        .addComponent(txtcuemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcuphone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(67, 67, 67)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btncudelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncuedit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncucreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel8)
                    .addContainerGap(585, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btncucreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncucreateActionPerformed
        // calling method add Customer
        add();
        
       
    }//GEN-LAST:event_btncucreateActionPerformed

    private void btncudeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncudeleteActionPerformed
        // call method delete
        delet();
        
    }//GEN-LAST:event_btncudeleteActionPerformed

    private void btncueditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncueditActionPerformed
        //CALL METHOD EDIT CUSTOMER
        edit();
    
    }//GEN-LAST:event_btncueditActionPerformed
//this method user type and automaticaly the table is filed
//textsearc/events/key/keyreleased
    private void txtcusearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcusearchKeyReleased
        // call method search in Customer menu
        SearchCus();
        
    }//GEN-LAST:event_txtcusearchKeyReleased

    private void txtcusearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcusearchActionPerformed
         
        
    }//GEN-LAST:event_txtcusearchActionPerformed
//user click on the user selected in the table and data go to form
    private void tblcuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcuMouseClicked
        // call link table with form 
        tbllink();
        
    }//GEN-LAST:event_tblcuMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncucreate;
    private javax.swing.JButton btncudelete;
    private javax.swing.JButton btncuedit;
    private javax.swing.JLabel btncusearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblcu;
    private javax.swing.JTextField txtcuaddress;
    private javax.swing.JTextField txtcuemail;
    private javax.swing.JTextField txtcuid;
    private javax.swing.JTextField txtcuname;
    private javax.swing.JTextField txtcuphone;
    private javax.swing.JTextField txtcusearch;
    // End of variables declaration//GEN-END:variables
}
