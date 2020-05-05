/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configds.pkg6210;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class JFrameConfigDS6210 extends javax.swing.JFrame {

    JSch jsch;
    Channel channel;
//    Channel execChannel;
    Session session;
//    OutputStream outStream;
//    InputStream inStream;
//    String SUDO_PASS = "123456";
//    String user;
//    String pass;
//    String host;

    /**
     * Creates new form JFrameConfigDS6210
     */
    public JFrameConfigDS6210() {
        initComponents();

    }

    void send() {
        String command = jTextFieldIn.getText();
        switch (command) {
            case "sudo su":
//                    command = "sudo -S -p '' su";
                exec(command);
//                    outStream.write(("su" + "\n").getBytes());
//                    outStream.flush();
                break;
            default:
                exec(command);
        }

//        command = "VOS-Cmd -s";
//        exec(command);
    }

//    void exec2(String command) {
//        try {
//            jTextAreaOut.append(command);
//            outStream.write(command.getBytes());
//            channel.sendSignal(command);
////            channel = session.openChannel("shell");
////            outStream = new MyOutputStream(jTextAreaOut);           
////            if (inStream != null) {
////                inStream.close();
////            }            
////            inStream = new ByteArrayInputStream(command.getBytes());
////            channel.setInputStream(System.in);
////            channel.setInputStream(inStream);
////            channel.setOutputStream(System.out);
////            channel.setOutputStream(outStream);
////            channel.start();
////            inStream.setText(command);
//        } catch (Exception ex) {
//            Logger.getLogger(JFrameConfigDS6210.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    void exec(String command) {
        // TODO add your handling code here:
        try {
            jTextAreaOut.append(command + "\n\r");
//            execChannel = session.openChannel("exec");
            if (channel == null) {
                return;
            }
//            ((ChannelExec) channel).setCommand(command);

//            
            channel.getOutputStream().write((command + ";\n\r").getBytes());
            channel.getOutputStream().flush();
//            inStream = channel.getInputStream();

            InputStreamReader isr = new InputStreamReader(channel, true);
            isr.start();
            channel.connect();

//            try {
//                Thread.sleep(1000);
//            } catch (Exception ee) {
//            }
//            execChannel.disconnect();
        } catch (Exception ex) {
            Logger.getLogger(JFrameConfigDS6210.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    void execSudo(String command) {
//        try {
//            String sudo_pass = pass;
//            channel = session.openChannel("exec");
//            jTextAreaOut.append("sudo -S -p '' " + command + "\r\n");
//            // man sudo
//            //   -S  The -S (stdin) option causes sudo to read the password from the
//            //       standard input instead of the terminal device.
//            //   -p  The -p (prompt) option allows you to override the default
//            //       password prompt and use a custom one.
//            ((ChannelExec) channel).setCommand("sudo -S -p '' " + command);
//
//            InputStream in = channel.getInputStream();
//            OutputStream out = channel.getOutputStream();
//            ((ChannelExec) channel).setErrStream(System.err);
//
//            channel.connect();
//
//            out.write((sudo_pass + "\n").getBytes());
//            out.flush();
//
//            byte[] tmp = new byte[1024];
//            while (true) {
//                while (in.available() > 0) {
//                    int i = in.read(tmp, 0, 1024);
//                    if (i < 0) {
//                        break;
//                    }
//                    jTextAreaOut.append(new String(tmp, 0, i));
//                }
//                if (channel.isClosed()) {
//                    jTextAreaOut.append("exit-status: " + channel.getExitStatus() + "\r\n");
//                    break;
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (Exception ee) {
//                }
//            }
//            channel.disconnect();
//        } catch (Exception ex) {
//            Logger.getLogger(JFrameConfigDS6210.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    void exit() {
        if (channel != null) {
            channel.disconnect();

        }
        if (session != null) {
            session.disconnect();
        }
        jsch = null;
    }

    public class ConnectionThread extends Thread {

        String hostname;
        String username;
        String password;

        public ConnectionThread(String hostname, String username, String password) {
            this.hostname = hostname;
            this.username = username;
            this.password = password;
        }

        @Override
        public void run() {
            try {
                jsch = new JSch();

                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session = jsch.getSession(username, hostname, 22);
                session.setConfig(config);
                
                session.setPassword(password);
                session.connect();
                if (session.isConnected()) {
                    jTextAreaOut.append("ClientVersion: " + session.getClientVersion() + "\n\r");
                    jTextAreaOut.append("Host: " + session.getHost() + "\n\r");
                    jTextAreaOut.append("HostKeyAlias: " + session.getHostKeyAlias() + "\n\r");
                    jTextAreaOut.append("ServerVersion: " + session.getServerVersion() + "\n\r");
                    jTextAreaOut.append("UserName: " + session.getUserName() + "\n\r");
                }
                channel = session.openChannel("shell");
//                outStream = channel.getOutputStream();
//                inStream = channel.getInputStream();
//                InputStreamReader isr = new InputStreamReader(channel, true);
//                isr.start();
//                
                channel.connect();
            } catch (Exception e) {
                jTextAreaOut.append("Ошибка: " + e.getMessage() + "\n\r");
//            StackTraceElement[] stackTraceElements = e.getStackTrace();
//
//            for (int i = 0; i < stackTraceElements.length; i++) {
//                System.out.println( i + ": " + stackTraceElements[i].toString());
//            }
            } finally {
                jButtonConnect.setEnabled(true);
                jButtonConnect.setText("Подключиться");
                jTextFieldIP.setEnabled(true);
                jTextFieldLogin.setEnabled(true);
                jPasswordFieldPass.setEnabled(true);
            }

        }

    }

    public class InputStreamReader extends Thread {

        byte[] tmp;
        Channel readChannel;
        boolean channelClose;

        public InputStreamReader(Channel readChannel, boolean channelClose) {
            super();
            this.readChannel = readChannel;
            this.channelClose = channelClose;
        }

        @Override
        public void run() {
            InputStream inStream = null;
            try {
                tmp = new byte[1024];
                inStream = channel.getInputStream();
                while (true) {
                    try {
//                    jTextAreaOut.append("in.available(): " + inStream.available() + "\r\n");
                        while (inStream.available() > 0) {
                            int i = inStream.read(tmp, 0, 1024);
                            if (i < 0) {
                                break;
                            }
                            jTextAreaOut.append(new String(tmp, 0, i));
                        }
                        if (readChannel.isClosed()) {
                            jTextAreaOut.append("channel.isClosed()\r\n");
                            jTextAreaOut.append("in.available(): " + inStream.available() + "\r\n");
                            if (inStream.available() > 0) {
                                continue;
                            }
                            jTextAreaOut.append("exit-status: " + readChannel.getExitStatus() + "\r\n");
                            jTextAreaOut.append(session.getUserName() + "@" + session.getHost() + ": ");

                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(JFrameConfigDS6210.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (channelClose) {
                    readChannel.disconnect();
                }
            } catch (IOException ex) {
                Logger.getLogger(JFrameConfigDS6210.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    inStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(JFrameConfigDS6210.class.getName()).log(Level.SEVERE, null, ex);
                }
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

        jLabel1 = new javax.swing.JLabel();
        jTextFieldLogin = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldIP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPasswordFieldPass = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaOut = new javax.swing.JTextArea();
        jButtonConnect = new javax.swing.JButton();
        jTextFieldIn = new javax.swing.JTextField();
        jButtonSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Config DS6210");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Логин:");

        jTextFieldLogin.setText("vos");

        jLabel2.setText("Пароль:");

        jTextFieldIP.setText("192.168.56.10");

        jLabel3.setText("Host:");

        jPasswordFieldPass.setText("vos");

        new SmartScroller(jScrollPane1);

        jTextAreaOut.setEditable(false);
        jTextAreaOut.setColumns(20);
        jTextAreaOut.setRows(5);
        jScrollPane1.setViewportView(jTextAreaOut);

        jButtonConnect.setText("Подключиться");
        jButtonConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConnectActionPerformed(evt);
            }
        });

        jTextFieldIn.setText("sudo su");
        jTextFieldIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldInActionPerformed(evt);
            }
        });

        jButtonSend.setText("Отправить");
        jButtonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jTextFieldIP)
                                            .addComponent(jPasswordFieldPass, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonConnect)))
                                .addGap(35, 35, 35))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldIn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jButtonSend)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordFieldPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonConnect))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSend))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConnectActionPerformed
        // TODO add your handling code here:
        String hostname = jTextFieldIP.getText().trim();
        String username = jTextFieldLogin.getText().trim();
        String password = new String(jPasswordFieldPass.getPassword());
        if ((hostname.length() == 0) || (username.length() == 0)) {
            JOptionPane.showMessageDialog(this, "Пожалуйста введите имя пользователя и имя хоста ");
            return;
        }

        jButtonConnect.setEnabled(false);
        jButtonConnect.setText("Подключение");
        jTextFieldIP.setEnabled(false);
        jTextFieldLogin.setEnabled(false);
        jPasswordFieldPass.setEnabled(false);
        ConnectionThread ct = new ConnectionThread(hostname, username, password);
        ct.start();
    }//GEN-LAST:event_jButtonConnectActionPerformed

    private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendActionPerformed
        send();

    }//GEN-LAST:event_jButtonSendActionPerformed

    private void jTextFieldInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldInActionPerformed
        // TODO add your handling code here:
        send();
    }//GEN-LAST:event_jTextFieldInActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        exit();
    }//GEN-LAST:event_formWindowClosing

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
                if ("Windows Classic".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameConfigDS6210.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameConfigDS6210.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameConfigDS6210.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameConfigDS6210.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameConfigDS6210().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonConnect;
    private javax.swing.JButton jButtonSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField jPasswordFieldPass;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaOut;
    private javax.swing.JTextField jTextFieldIP;
    private javax.swing.JTextField jTextFieldIn;
    private javax.swing.JTextField jTextFieldLogin;
    // End of variables declaration//GEN-END:variables
}
