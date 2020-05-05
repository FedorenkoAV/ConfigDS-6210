/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configds.pkg6210;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 *
 * @author User
 */
public class MyOutputStream extends OutputStream{
    JTextArea textArea;

    public MyOutputStream(JTextArea tA) {
        textArea = tA;        
    }   

    @Override
    public void write(int b) throws IOException {
        char ch = (char) b;
        String str = Character.toString(ch);
        textArea.append(str);
    }
    
}
