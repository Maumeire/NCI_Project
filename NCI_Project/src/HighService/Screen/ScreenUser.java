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

/**
 *
 * @author Meire de Jesus Torres
 */
/**
 * Creates new form ScreenUser
 * 
 */


public class ScreenUser extends javax.swing.JInternalFrame {

    Connection cone = null;//conect databases
    ResultSet res = null;//prepare conection
    PreparedStatement Stat = null;//show or display result of conection
//if doesnt have void is a constructor
//stat conection with database            

    public ScreenUser() {
        initComponents();
        cone = Dataconnect.conector();
    }
    ////////////////SEARCH USER////////////////////////////////
    //private is executed only in this class
    private void search() {
    //searching in mySQL userID given by the user

        String sql = "select*from tableuser where userID=?";
        try {
            //this statement is try find userID if found prepare conection
            //bring data else bring error
            Stat = cone.prepareStatement(sql);
            Stat.setString(1, txtnuid.getText());//get userId typed and compare with table then bring info below.
            res = Stat.executeQuery();
            if (res.next()) {
                txtnuname.setText(res.getString(2));//bring user name
                txtnuphone.setText(res.getString(3));//bring user phone
                txtnulogin.setText(res.getString(4)); //bring user username
                txtnupassword.setText(res.getString(5));  //bring user password
                cbousertype.setSelectedItem(res.getString(6)); //bring user user type                     
            } else {
                JOptionPane.showMessageDialog(null, "User is not registered");
                txtnuname.setText(null);
                txtnuphone.setText(null);
                txtnulogin.setText(null);
                txtnupassword.setText(null);
             //   cbousertype.setSelectedItem(null); I got an error when i was validating because of that
            }
        } catch (Exception e) {//it show error or exception
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //////////////CREATE A NEW USER////////////////////////  

    private void add(){
//getting from the app inputed by user and registering in databe mySQL
        String sql = "insert into tableuser(userId,username,phone,login,password,typeuser)values(?,?,?,?,?,?)";
        try {
            Stat = cone.prepareStatement(sql);
            Stat.setString(1, txtnuid.getText());//1 is id user
            Stat.setString(2, txtnuname.getText());//2 insert name
            Stat.setString(3, txtnuphone.getText());//insert phone
            Stat.setString(4, txtnulogin.getText());//insert login
            Stat.setString(5, txtnupassword.getText());//insert password
            Stat.setString(6, cbousertype.getSelectedItem().toString()); //insert user type   
            //validate required fields
            if ((txtnuid.getText().isEmpty())||(txtnuname.getText().isEmpty())||(txtnulogin.getText().isEmpty())||(txtnupassword.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Can not be empty");
            } else {
            //confirm that input was successfuly
            int inputdata = Stat.executeUpdate();
            System.out.println(inputdata);//check if variable inputdata is > 0 if yes it working properly
            if (inputdata > 0) {
                JOptionPane.showMessageDialog(null, "User registered successfully");
                txtnuid.setText(null);
                txtnuname.setText(null);
                txtnuphone.setText(null);
                txtnulogin.setText(null);
                txtnupassword.setText(null);
               // cbousertype.setSelectedItem(null);
                }
            }
            //  Stat.executeUpdate();//inset data
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"User is already registered");
        }
    }
/////////////////////DELETE USER//////////////////////////////////////
    private void delet(){
        //confirm user deleted
        int confirm = JOptionPane.showConfirmDialog(null,"Do you want to delete this user","warning",JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION){
            String sql = "delete from tableuser where userID=?";
            try{
                Stat = cone.prepareStatement(sql);
                Stat.setString(1,txtnuid.getText());
                int removed = Stat.executeUpdate();
                if(removed>0){
                    JOptionPane.showMessageDialog(null, "User deleted successfully");
                    txtnuid.setText(null);
                    txtnuname.setText(null);
                    txtnuphone.setText(null);
                    txtnulogin.setText(null);
                    txtnupassword.setText(null);
                }
                
            }catch(Exception e){
                
            }
        }
    }
    
///////////////////////UPDATE USER////////////////////////////////////
    
    private void update(){
     String sql = "update tableuser set username=?,phone=?,login=?,password=?,typeuser=? where userID=?";   
        try {
            Stat = cone.prepareStatement(sql);
            Stat.setString(1, txtnuname.getText());
            Stat.setString(2, txtnuphone.getText());
            Stat.setString(3, txtnulogin.getText());
            Stat.setString(4, txtnupassword.getText());
            Stat.setString(5, cbousertype.getSelectedItem().toString());
            Stat.setString(6, txtnuid.getText());

            if ((txtnuid.getText().isEmpty()) || (txtnuname.getText().isEmpty()) || 
                (txtnulogin.getText().isEmpty()) || (txtnupassword.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Can not be empty");
            } else {
                //confirm that update was successfuly
                int inputdata = Stat.executeUpdate();
                System.out.println(inputdata);//check if variable inputdata is > 0 if yes it working properly
                if (inputdata > 0) {
                    JOptionPane.showMessageDialog(null, "Update successfully");
                    txtnuid.setText(null);
                    txtnuname.setText(null);
                    txtnuphone.setText(null);
                    txtnulogin.setText(null);
                    txtnupassword.setText(null);
                    // cbousertype.setSelectedItem(null);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtnuid = new javax.swing.JTextField();
        txtnuname = new javax.swing.JTextField();
        txtnuphone = new javax.swing.JTextField();
        txtnulogin = new javax.swing.JTextField();
        cbousertype = new javax.swing.JComboBox<>();
        txtnupassword = new javax.swing.JTextField();
        btnnucreate = new javax.swing.JButton();
        btnnudelete = new javax.swing.JButton();
        btnnuedit = new javax.swing.JButton();
        btnnusearch = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtnuid1 = new javax.swing.JTextField();
        txtnuname1 = new javax.swing.JTextField();
        txtnuphone1 = new javax.swing.JTextField();
        txtnulogin1 = new javax.swing.JTextField();
        cbousertype1 = new javax.swing.JComboBox<>();
        txtnupassword1 = new javax.swing.JTextField();
        btnnucreate1 = new javax.swing.JButton();
        btnnudelete1 = new javax.swing.JButton();
        btnnuedit1 = new javax.swing.JButton();
        btnnusearch1 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("User");
        setPreferredSize(new java.awt.Dimension(699, 625));

        jLabel1.setText("ID");

        jLabel2.setText("Name");

        jLabel3.setText("Login");

        jLabel4.setText("Password");

        jLabel5.setText("User Type");

        jLabel6.setText("Phone");

        cbousertype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));

        btnnucreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/create.png"))); // NOI18N
        btnnucreate.setToolTipText("Add");
        btnnucreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnucreate.setPreferredSize(new java.awt.Dimension(80, 80));
        btnnucreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnucreateActionPerformed(evt);
            }
        });

        btnnudelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/delete.png"))); // NOI18N
        btnnudelete.setToolTipText("Delete");
        btnnudelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnudelete.setPreferredSize(new java.awt.Dimension(80, 80));
        btnnudelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnudeleteActionPerformed(evt);
            }
        });

        btnnuedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/edit.png"))); // NOI18N
        btnnuedit.setToolTipText("Edit");
        btnnuedit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnuedit.setPreferredSize(new java.awt.Dimension(80, 80));
        btnnuedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnueditActionPerformed(evt);
            }
        });

        btnnusearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/search.png"))); // NOI18N
        btnnusearch.setToolTipText("Search");
        btnnusearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnusearch.setPreferredSize(new java.awt.Dimension(80, 80));
        btnnusearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnusearchActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("* Required");

        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("*");

        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");

        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("*");

        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setText("*");

        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("*");

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setTitle("New User");
        jInternalFrame1.setPreferredSize(new java.awt.Dimension(624, 625));

        jLabel13.setText("ID");

        jLabel14.setText("Name");

        jLabel15.setText("Login");

        jLabel16.setText("Password");

        jLabel17.setText("User Type");

        jLabel18.setText("Phone");

        cbousertype1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));

        btnnucreate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/create.png"))); // NOI18N
        btnnucreate1.setToolTipText("Add");
        btnnucreate1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnucreate1.setPreferredSize(new java.awt.Dimension(80, 80));
        btnnucreate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnucreate1ActionPerformed(evt);
            }
        });

        btnnudelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/delete.png"))); // NOI18N
        btnnudelete1.setToolTipText("Delete");
        btnnudelete1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnudelete1.setPreferredSize(new java.awt.Dimension(80, 80));

        btnnuedit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/edit.png"))); // NOI18N
        btnnuedit1.setToolTipText("Edit");
        btnnuedit1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnuedit1.setPreferredSize(new java.awt.Dimension(80, 80));

        btnnusearch1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HighService/icon/search.png"))); // NOI18N
        btnnusearch1.setToolTipText("Search");
        btnnusearch1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnusearch1.setPreferredSize(new java.awt.Dimension(80, 80));
        btnnusearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnusearch1ActionPerformed(evt);
            }
        });

        jLabel19.setForeground(new java.awt.Color(255, 0, 0));
        jLabel19.setText("* Required");

        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("*");

        jLabel21.setForeground(new java.awt.Color(255, 0, 0));
        jLabel21.setText("*");

        jLabel22.setForeground(new java.awt.Color(255, 0, 0));
        jLabel22.setText("*");

        jLabel23.setForeground(new java.awt.Color(255, 0, 0));
        jLabel23.setText("*");

        jLabel24.setForeground(new java.awt.Color(255, 0, 0));
        jLabel24.setText("*");

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnnucreate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(btnnudelete1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(btnnuedit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(btnnusearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel17))
                            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16))
                            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtnupassword1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtnulogin1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                .addComponent(txtnuid1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(299, 299, 299)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtnuphone1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnuname1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbousertype1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(24, 24, 24)
                                .addComponent(jLabel21)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14)
                                .addGap(44, 44, 44)))
                        .addGap(18, 18, 18)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(jLabel17))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtnuid1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(txtnuname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtnuphone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtnulogin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtnupassword1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbousertype1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnnudelete1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnnuedit1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnnucreate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnnusearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(129, 129, 129))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel5))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtnupassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtnulogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtnuid, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(299, 299, 299)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtnuphone, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnuname, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbousertype, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnnucreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(btnnudelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(btnnuedit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(btnnusearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtnuid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8)
                    .addComponent(jLabel1))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtnuname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtnuphone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(3, 3, 3)))
                        .addGap(25, 25, 25)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtnulogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(jLabel4))
                            .addComponent(txtnupassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(jLabel3)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbousertype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnnudelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnnuedit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnnucreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnnusearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(61, 61, 61))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 240, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 240, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnnusearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnusearchActionPerformed
        // call search method
        search();
    }//GEN-LAST:event_btnnusearchActionPerformed

    private void btnnucreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnucreateActionPerformed
        // calling method add
        add();
    }//GEN-LAST:event_btnnucreateActionPerformed

    private void btnnucreate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnucreate1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnnucreate1ActionPerformed

    private void btnnusearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnusearch1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnnusearch1ActionPerformed

    private void btnnueditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnueditActionPerformed
        //call method update
        update();
    }//GEN-LAST:event_btnnueditActionPerformed

    private void btnnudeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnudeleteActionPerformed
        // call method delete
        delet();
    }//GEN-LAST:event_btnnudeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnnucreate;
    private javax.swing.JButton btnnucreate1;
    private javax.swing.JButton btnnudelete;
    private javax.swing.JButton btnnudelete1;
    private javax.swing.JButton btnnuedit;
    private javax.swing.JButton btnnuedit1;
    private javax.swing.JButton btnnusearch;
    private javax.swing.JButton btnnusearch1;
    private javax.swing.JComboBox<String> cbousertype;
    private javax.swing.JComboBox<String> cbousertype1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtnuid;
    private javax.swing.JTextField txtnuid1;
    private javax.swing.JTextField txtnulogin;
    private javax.swing.JTextField txtnulogin1;
    private javax.swing.JTextField txtnuname;
    private javax.swing.JTextField txtnuname1;
    private javax.swing.JTextField txtnupassword;
    private javax.swing.JTextField txtnupassword1;
    private javax.swing.JTextField txtnuphone;
    private javax.swing.JTextField txtnuphone1;
    // End of variables declaration//GEN-END:variables
}
