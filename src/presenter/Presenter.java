package presenter;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
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

    public void prepareDataForList(DefaultListModel<String> listValues, JTextField varianz_value, JDateChooser datum_value) {
        if (listValues != null) {
            listValues.clear();
        }
        String[] stationen = laodStationenForList();
        for (int i = 0; i < stationen.length; i++) {
            String[] parts = stationen[i].split(" ");
            String station = parts[0];
            if (!station.equals("")) {
                listValues.addElement(station);
            }
        }

    }

    public void getSelectedValueFromList(JList<String> list, JTextField stationId_value, MouseEvent evt, JTextField targetAnsetzen_value, JTextField varianz_value) {
        ArrayList<String> lines = new ArrayList<String>();
        String line = null;
        targetAnsetzen_value.setText("");
        list = (JList)evt.getSource();
        if (evt.getClickCount() == 1) {
            String selected = list.getSelectedValue();
            File file = null;
            try {
                file = loadFileData();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (file != null) {
                String[] parts = null;
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                    fr.close();
                    br.close();

                    FileWriter fw = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(fw);
                    for (String s : lines) {
                        out.newLine();
                        out.write(s);
                        if (s.contains(stationId_value.getText())) {
                            parts = s.split(" ");;
                        }
                    }
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (0 >= parts.length) {
                    //index not exists
                } else {
                    stationId_value.setText(parts[0]);
                }
                if (1 >= parts.length) {
                    //index not exists
                } else {
                    varianz_value.setText(parts[1]);
                }
            }
        }

    }

    public void setTargetValue(JTextField ausgewhlteStation_value, String targetValue) {
        JOptionPane messagePane = new JOptionPane();
        if (!ausgewhlteStation_value.getText().isEmpty()) {
            int target = Integer.parseInt(targetValue);
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
        } else {
            messagePane.showMessageDialog(null, "Bitte eine Station selektieren", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadTargetValue(JTextField target_value) {
        target_value.setText("42");
    }

    public void calculateVariance(JTextField aktuellWert_value, JTextField target_value, JTextField varianz_value, JTextField stationId_value, JDateChooser datum_value) {
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
            saveVarianceAndDate(stationId_value.getText(), variance, datum_value);
        } else {
            message.showMessageDialog(null, "Bitte Station auswählen und Aktueller Wert eingeben", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveVarianceAndDate(String station, int variance, JDateChooser datum_value) {
        List<String> lines = new ArrayList<String>();
        String line = null;
        File fileModel = null;
        try {
            fileModel = loadFileData();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (fileModel != null) {
            try {
                FileReader fr = new FileReader(fileModel);
                BufferedReader br = new BufferedReader(fr);
                while ((line = br.readLine()) != null) {
                    if (line.contains(station))
                        line = line.replace(station, station + " " + variance + " " + ((JTextField)datum_value.getDateEditor().getUiComponent()).getText());
                    lines.add(line);
                }
                fr.close();
                br.close();

                FileWriter fw = new FileWriter(fileModel);
                BufferedWriter out = new BufferedWriter(fw);
                for (String s : lines) {
                    out.newLine();
                    out.write(s);
                }
                out.flush();
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
        if (!(varianz_value.getText().isEmpty())) {
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
        } else {
            JOptionPane message = new JOptionPane();
            message.showMessageDialog(null, "Bitte Varianz berechnen", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addStationRandomly(DefaultListModel<String> listValues_auswertung, JLabel annoucement) {
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
                        annoucement.setText("Eine neue Station wurde hinzugefügt: " + randomStationen[rnd]);
                        annoucement.setForeground(Color.BLUE);
                        updateFileModel(randomStationen[rnd]);
                    }
                } else {
                    //do nothing
                }
            }

        };

        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 5 * 1000;

        // schedules the task to be run in an interval 
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);

    }

    private void updateFileModel(String newStation) {
        File modelFile = null;
        try {
            modelFile = loadFileData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (modelFile != null) {
            updateDataInModel(modelFile, newStation);
        }

    }

    private void updateDataInModel(File modelFile, String newStation) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(modelFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bw != null) {
            try {
                // APPEND MODE SET HERE
                bw = new BufferedWriter(new FileWriter(modelFile, true));
                bw.newLine();
                bw.write(newStation);
                bw.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally { // always close the file
                if (bw != null)
                    try {
                        bw.close();
                    } catch (IOException ioe2) {
                        // just ignore it
                    }
            }

        }

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

    public void getSelectedValueMainFromList(JList<String> list, JTextField stationId_value, MouseEvent evt, JTextField aktuellWert_value, JDateChooser datum_value, JTextField varianz_value) {
        ArrayList<String> lines = new ArrayList<String>();
        String line = null;
        list = (JList)evt.getSource();
        if (evt.getClickCount() == 1) {
            String selected = list.getSelectedValue();
            File file = null;
            try {
                file = loadFileData();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (file != null) {
                String[] parts = null;
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                    fr.close();
                    br.close();

                    FileWriter fw = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(fw);
                    for (String s : lines) {
                        out.newLine();
                        out.write(s);
                        if (s.contains(selected)) {
                            parts = s.split(" ");;
                        }
                    }
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (0 >= parts.length) {
                    //index not exists
                } else {
                    stationId_value.setText(parts[0]);
                }
                if (1 >= parts.length) {
                    //index not exists
                } else {
                    varianz_value.setText(parts[1]);
                }
            }
        }

    }

}
