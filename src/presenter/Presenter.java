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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import com.toedter.calendar.JDateChooser;

import model.Model;
import view.View;

/**
 * 
 */

/**
 * @author kevin.tchagwo, Viehmann Benjamin, Oeppert Luise, Allani Mohammed, Sandrine Müller
 *
 */
public class Presenter implements IPresenter {

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

    public void getSelectedValueFromList(JList<String> list, JTextField stationId_value, MouseEvent evt, JTextField targetAnsetzen_value) {

        targetAnsetzen_value.setText("");
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
                updateFile(file, targetValue, ausgewhlteStation_value);
            } catch (IOException e) {
                e.printStackTrace();
            }
            messagePane.showMessageDialog(null, "Target wurde gespeichert", null, JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void loadTargetValue(JTextField target_value) {
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
            } else {
                varianz_value.setForeground(Color.BLACK);
            }
        } else {
            message.showMessageDialog(null, "Bitte Station auswählen und Aktueller Wert eingeben", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setValueForDiagram(DefaultCategoryDataset valueDiagram, String varianz_value, String aktuellWert, String target) {
        valueDiagram.setValue(Integer.parseInt(target), "Varianz Diagramm", "Target");
        valueDiagram.setValue(Integer.parseInt(aktuellWert), "Varianz Diagramm", "Aktueller Wert");
        valueDiagram.setValue(Integer.parseInt(varianz_value), "Varianz Diagramm", "Varianz");
    }

    /**
     * @param mainView
     * @param aktuellWert_value
     * @param varianz_value
     * @param target_value 
     */
    public void createVarianceDiagramm(JPanel mainView, JTextField aktuellWert_value, JTextField varianz_value, JTextField target_value) {
        String categoryAxisLabel = "Target";
        String valueAxisLabel = "Aktueller Wert";
        DefaultCategoryDataset valueDiagram = new DefaultCategoryDataset();
        setValueForDiagram(valueDiagram, varianz_value.getText(), aktuellWert_value.getText(), target_value.getText());
        JFreeChart diagramm = ChartFactory.createLineChart("Varianz Diagramm", categoryAxisLabel, valueAxisLabel, valueDiagram);
        CategoryPlot plot = diagramm.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.BLACK);
        ChartFrame chartFrm = new ChartFrame("Varianz Diagramm", diagramm);
        chartFrm.setVisible(true);
        chartFrm.setSize(500, 500);
        ChartPanel panelDiag = new ChartPanel(diagramm);
        mainView.add(panelDiag);
    }

    public void addStationRandomly(DefaultListModel<String> listValues_auswertung) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // task to run goes here
                File randomFile = null;
                try {
                    randomFile = model.loadStationFileDataForRandom();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] randomStationen = loadRandomStationen(randomFile);
                int rnd = new Random().nextInt(randomStationen.length);
                Random randomno = new Random();
                if ((randomno.nextInt() * randomno.nextInt()) % 2 == 0) {
                    if (!listValues_auswertung.contains(randomStationen[rnd])) {
                        listValues_auswertung.addElement(randomStationen[rnd]);
                    }
                } else {

                }
                System.out.println("Hello !!!");
            }

        };

        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 5 * 1000;

        // schedules the task to be run in an interval 
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);

    }

    public String[] loadRandomStationen(File randomFile) {
        String[] stationen = null;
        if (randomFile != null) {
            List<String> dataStationen = new ArrayList<>();
            BufferedReader br = null;
            FileReader fr = null;
            try {
                //br = new BufferedReader(new FileReader(file));
                fr = new FileReader(randomFile);
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

    //TODO should be checked
    public void updateFile(File file, String targetValue, JTextField ausgewhlteStation_value) {
        String[] stationen = null;
        if (file != null) {
            List<String> dataStationen = new ArrayList<>();
            BufferedReader br = null;
            FileReader fr = null;
            try {
                //br = new BufferedReader(new FileReader(file));
                fr = new FileReader(file);
                br = new BufferedReader(fr);

                fr = new FileReader(file);
                br = new BufferedReader(fr);

                String sCurrentLine;

                while ((sCurrentLine = br.readLine()) != null) {
                    dataStationen.add(sCurrentLine);
                }
                String stationId = ausgewhlteStation_value.getText();
                int i = 0;
                for (String station : dataStationen) {
                    if (station.equals(stationId)) {
                        dataStationen.set(i, station + " " + targetValue);
                        break;
                    }
                    i++;
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
            
            //            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            //            for(String)
            //            writer.write(str);
            //             
            //            writer.close();
            
            stationen = dataStationen.toArray(new String[dataStationen.size()]);
        } else {
            System.out.println("failed to start with the file - the file is null");
            return;
        }
    }

    public void getSelectedValueMainFromList(JList<String> list, JTextField stationId_value, MouseEvent evt, JTextField aktuellWert_value, JDateChooser datum_value,
            JTextField varianz_value) {

        aktuellWert_value.setText("");
        datum_value.setDate(null);;
        varianz_value.setText("");
        list = (JList)evt.getSource();
        if (evt.getClickCount() == 1) {
            String selected = list.getSelectedValue();
            stationId_value.setText(selected);
        }

    }

}
