/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configds.pkg6210;

import java.io.ByteArrayInputStream;

/**
 *
 * @author User
 */
public class MyInputStream extends ByteArrayInputStream{
    
    public MyInputStream(byte[] buf) {
        super(buf);
    }
    
    public void setText (String str) {
        buf = str.getBytes();
    }    
}
