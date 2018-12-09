package model;

import java.io.File;
import java.io.IOException;

/**
 * 
 */

/**
 * @author  kevin.tchagwo, Viehmann Benjamin, Oeppert Luise, Allani Mohammed, Sandrine Müller
 *
 */
public class Model implements IModel {
    //TODO to should be removed
    private File file;

    public Model() {
        try {
            file = loadStationFileData();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public File loadStationFileData() throws IOException {
        File list1 = new File("C:\\Users\\kevin.tchagwo\\eclipse-workspace\\MvpDemo\\testFile1.txt");
        return list1;
    }

    public File loadStationFileDataForRandom() throws IOException {
        File list2 = new File("C:\\Users\\kevin.tchagwo\\eclipse-workspace\\MvpDemo\\testFile2.txt");
        return list2;
    }

}
