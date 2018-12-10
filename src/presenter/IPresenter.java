/**
 * 
 */
package presenter;

import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author kevin.tchagwo
 *
 */
public interface IPresenter {

    public String[] laodStationenForList();

    public File loadFileData() throws IOException;

    public String[] storeDataForList(File file);

    public void loadView(JPanel panelCont, CardLayout cl, String id);

    public void setNumberRestriction(KeyEvent wert);

    public void prepareDataForList(DefaultListModel<String> listValues);

    public void getSelectedValueFromList(JList<String> list, JTextField stationId_value, MouseEvent evt, JTextField targetAnsetzen_value);

    public void setTargetValue(JTextField ausgewhlteStation_value, String targetValue);

    public void loadTargetValue(JTextField target_value);

    public void calculateVariance(JTextField aktuellWert_value, JTextField target_value, JTextField varianz_value, JTextField stationId_value);

    public void setValueForDiagram(DefaultCategoryDataset valueDiagram, String varianz_value, String aktuellWert, String target);

    public void createVarianceDiagramm(JPanel mainView, JTextField aktuellWert_value, JTextField varianz_value, JTextField target_value);

    public void addStationRandomly(DefaultListModel<String> listValues_auswertung, JLabel annoucement);

    public String[] loadRandomStationen(File randomFile);
}
