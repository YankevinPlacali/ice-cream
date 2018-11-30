package model;

import java.io.File;
import java.io.IOException;

/**
 * 
 */

/**
 * @author kevin.tchagwo
 *
 */
public class Model {
    //TODO to should be removed
    private String password;

    public Model() {
        password = "password"; //just set a default password.
    }

    public void setPassword(String pass) {
        password = pass;
    }

    public String getPassword() {
        return password;
    }

    public File loadStationFileData() throws IOException {
        File file = new File("C:\\Users\\kevin.tchagwo\\eclipse-workspace\\MvpDemo\\testFile1.txt");
        return file;
    }

}
