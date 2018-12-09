/**
 * 
 */
package model;

import java.io.File;
import java.io.IOException;

/**
 * @author  kevin.tchagwo, Viehmann Benjamin, Oeppert Luise, Allani Mohammed, Sandrine Müller
 *
 */
public interface IModel {

    public File loadStationFileData() throws IOException;

    public File loadStationFileDataForRandom() throws IOException;

}
