package presenter;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Model;
import view.View;

/**
 * 
 */

/**
 * @author kevin.tchagwo
 *
 */
public class Presenter {

    private View view;
    private Model model;

    public Presenter(View window, Model model) {
        this.view = window;
        this.model = model;
    }

    public String[] laodStationenForList() {
        File file = null;
        try {
            file = loadFileData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] stationen = storeDataForList(file);
        return stationen;

    }

    public File loadFileData() throws IOException {
        File stationFile = null;
        stationFile = model.loadStationFileData();
        if (stationFile == null) {
            System.out.println("failed to load the file");
            return null;
        }
        return stationFile;
    }

    public String[] storeDataForList(File file) {
        String[] stationen = null;
        if (file != null) {
            List<String> dataStationen = new ArrayList<>();
            BufferedReader br = null;
            FileReader fr = null;
            try {
                //br = new BufferedReader(new FileReader(file));
                fr = new FileReader(file);
                br = new BufferedReader(fr);

                String sCurrentLine;

                while ((sCurrentLine = br.readLine()) != null) {
                    System.out.println(sCurrentLine);
                    dataStationen.add(sCurrentLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null)
                        br.close();
                    if (fr != null)
                        fr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            stationen = dataStationen.toArray(new String[dataStationen.size()]);
        } else {
            System.out.println("failed to start with the file - the file is null");
            return null;
        }
        return stationen;
    }

    public void login(String pass) {
        String result = "Incorrect password";
        //
        //        if (model.getPassword().equals(pass)) {
        //            result = "Correct password";
        //        }
        view.updateStatusLabel(result);
    }

    /**
     * @param panelCont
     * @param cl
     */
    public void loadView(JPanel panelCont, CardLayout cl, String id) {
        cl.show(panelCont, id);
    }

    public void setNumberRestriction(KeyEvent wert) {
        char vChar = wert.getKeyChar();
        if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
            wert.consume();
        }
    }

    public void prepareDataForList(DefaultListModel<String> listValues) {
        String[] stationen = laodStationenForList();
        for (int i = 0; i < stationen.length; i++) {
            listValues.addElement(stationen[i]);
        }

    }

    public void getSelectedValueFromList(JList<String> list, JTextField stationId_value, MouseEvent evt) {

        list = (JList)evt.getSource();
        if (evt.getClickCount() == 1) {
            String selected = list.getSelectedValue();
            stationId_value.setText(selected);
        }

    }

    public void setTargetValue(JTextField ausgewhlteStation_value, String targetValue) {
        int target = Integer.parseInt(targetValue);
        JOptionPane messagePane = new JOptionPane();
        if (target > 100) {
            messagePane.showMessageDialog(null, "Bitte ein Wert zwischen 0 und 100 eingeben", null, JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                File file = loadFileData();
                //                updateFile(file, targetValue, ausgewhlteStation_value);
            } catch (IOException e) {
                e.printStackTrace();
            }
            messagePane.showMessageDialog(null, "Target wurde gespeichert", null, JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void loadDefaultTargetValue(JTextField target_value) {
        target_value.setText("42");
    }

    public void calculateVariance(JTextField aktuellWert_value, JTextField target_value, JTextField varianz_value, JTextField stationId_value) {
        JOptionPane message = new JOptionPane();
        if (!aktuellWert_value.getText().isEmpty() && !target_value.getText().isEmpty() && !stationId_value.getText().isEmpty()) {
            int aktuellWert = Integer.parseInt(aktuellWert_value.getText());
            int targetValue = Integer.parseInt(target_value.getText());
            int variance = aktuellWert - targetValue;
            varianz_value.setText(Integer.toString(variance));
            double targetZehnProzent = targetValue * 10 / 100;
            double targetFunfProzent = targetValue * 5 / 100;
            if (variance <= (targetValue - targetZehnProzent)) {
                varianz_value.setForeground(Color.RED);
            } else if (variance >= (targetValue - targetFunfProzent)) {
                varianz_value.setForeground(Color.GREEN);
            }
        } else {
            message.showMessageDialog(null, "Bitte Station auswählen und Aktueller Wert eingeben", null, JOptionPane.ERROR_MESSAGE);
        }
    }



    //TODO should be checked
    //    private void updateFile(File file, String targetValue, JTextField ausgewhlteStation_value) {
    //        String[] stationen = null;
    //        if (file != null) {
    //            List<String> dataStationen = new ArrayList<>();
    //            BufferedReader br = null;
    //            FileReader fr = null;
    //            FileWriter wr = null;
    //            try {
    //                //br = new BufferedReader(new FileReader(file));
    //                fr = new FileReader(file);
    //                br = new BufferedReader(fr);
    //                wr = new FileWriter(file);
    //
    //                String sCurrentLine;
    //
    //                while ((sCurrentLine = br.readLine()) != null) {
    //                    String stationId = ausgewhlteStation_value.getText();
    //                    for (String station : dataStationen) {
    //                        if (station.equals(stationId)) {
    //                            station += " = " + targetValue;
    //                            wr.write(station);
    //                        }
    //                    }
    //                    dataStationen.add(sCurrentLine);
    //                }
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            } finally {
    //                try {
    //                    if (br != null)
    //                        br.close();
    //                    if (fr != null)
    //                        fr.close();
    //                } catch (IOException ex) {
    //                    ex.printStackTrace();
    //                }
    //            }
    //            stationen = dataStationen.toArray(new String[dataStationen.size()]);
    //        } else {
    //            System.out.println("failed to start with the file - the file is null");
    //            return;
    //        }
    //    }

}
