package Crypt.DES;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by cephalgia on 20.03.17.
 */

public class DESkeygen {

    private final File file = new File("DESkey.txt");
    private final int KEY_LENGTH = 16;
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private StringBuffer key = new StringBuffer();

    public DESkeygen() {

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            for (int i = 0 ; i < KEY_LENGTH ; i++) {
                double random = Math.random();
                int index = (int) (random * 16);
                key.append(hexArray[index]);
                fileWriter.append(hexArray[index]);
                fileWriter.flush();
            }
            fileWriter.close();
            System.out.println("Key generated and saved in " + file.getName());
        } catch(Exception exp) {
            exp.printStackTrace();
        }
    }

    public String getKey() {
        String sKey = key.toString();
        return sKey;
    }
}
