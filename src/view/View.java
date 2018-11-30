package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import presenter.Presenter;

public class View {

    private JFrame frame = new JFrame("Ice-Cream");
    private Presenter presenter;
    private DefaultListModel<String> listValues_auswertung = new DefaultListModel<>();
    private DefaultListModel<String> listValues_admin = new DefaultListModel<>();
    private JList<String> stationen_list_auswertung = new JList(listValues_auswertung);
    private JList<String> station_list_admin = new JList(listValues_admin);
    private String target = null;
    private JTextField target_value;

    /**
     * Create the application.
     */
    public View() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        JPanel panelCont = new JPanel();
        CardLayout cl = new CardLayout();
        panelCont.setLayout(cl);

        JPanel sessionPanel = new JPanel();
        sessionPanel.setLayout(null);
        panelCont.add(sessionPanel, "session");

        JButton adminPortal = new JButton("Admin Portal");
        adminPortal.setBounds(164, 106, 154, 64);
        sessionPanel.add(adminPortal);

        adminPortal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                station_list_admin = new JList(listValues_admin);
                getPresenter().prepareDataForList(listValues_admin);
                getPresenter().loadView(panelCont, cl, "adminP");
            }
        });

        JButton auswertungPortal = new JButton("Auswertung Portal");
        auswertungPortal.setBounds(164, 209, 154, 64);
        sessionPanel.add(auswertungPortal);

        auswertungPortal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stationen_list_auswertung = new JList(listValues_auswertung);
                getPresenter().prepareDataForList(listValues_auswertung);
                getPresenter().loadView(panelCont, cl, "auswertungP");
                getPresenter().loadDefaultTargetValue(target_value);
            }
        });

        JLabel selectSession_label = new JLabel("Bitte Sitzung ausw\u00E4hlen");
        selectSession_label.setBounds(164, 68, 171, 14);
        sessionPanel.add(selectSession_label);

        //wir bauen die main view hier
        JPanel mainView = new JPanel();
        JButton backButtonAuswertung = new JButton("Zur\u00FCck");
        buildMainView(mainView, panelCont, backButtonAuswertung);



        //wir bauen die admin view
        JPanel adminView = new JPanel();
        JButton backButtonAdmin = new JButton("Zur\u00FCck");
        buildAdminView(adminView, panelCont, backButtonAdmin);

        backButtonAuswertung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                getPresenter().loadView(panelCont, cl, "session");
            }
        });

        backButtonAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                getPresenter().loadView(panelCont, cl, "session");
            }
        });

        frame.getContentPane().add(panelCont);
        frame.setBounds(100, 100, 476, 417);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * @param adminView 
     * @param panelCont 
     * @param backButtonAdmin 
     * 
     */
    private void buildAdminView(JPanel adminView, JPanel panelCont, JButton backButtonAdmin) {
        adminView.setLayout(null);
        backButtonAdmin.setBounds(17, 325, 113, 24);
        adminView.add(backButtonAdmin);
        adminView.setBackground(Color.LIGHT_GRAY);
        JTextField ausgewhlteStation_value;
        ausgewhlteStation_value = new JTextField();
        ausgewhlteStation_value.setColumns(10);
        ausgewhlteStation_value.setBackground(Color.LIGHT_GRAY);
        ausgewhlteStation_value.setBounds(283, 42, 157, 22);
        //TODO to presenter
        ausgewhlteStation_value.setEditable(false);

        station_list_admin.setBounds(17, 34, 113, 258);
        JScrollPane scroll = new JScrollPane(station_list_admin);
        scroll.setBounds(10, 27, 114, 232);
        adminView.add(scroll);


        JLabel ausgewhlteStation_label = new JLabel("Ausgew\u00E4hlte Station:");
        ausgewhlteStation_label.setBounds(140, 46, 133, 14);
        adminView.add(ausgewhlteStation_label);

        JLabel targetAnsetzen_label = new JLabel("Target ansetzen:");
        targetAnsetzen_label.setBounds(140, 70, 97, 25);
        adminView.add(targetAnsetzen_label);

        JTextField targetAnsetzen_value;
        targetAnsetzen_value = new JTextField();
        targetAnsetzen_value.setColumns(10);
        targetAnsetzen_value.setBounds(283, 71, 157, 22);
        targetAnsetzen_value.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent wert) {
                getPresenter().setNumberRestriction(wert);
            }
        });
        adminView.add(targetAnsetzen_value);

        JButton validateTaget = new JButton("Best\u00E4tigen");
        validateTaget.setBounds(343, 324, 97, 25);
        adminView.add(validateTaget);

        validateTaget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                getPresenter().setTargetValue(ausgewhlteStation_value, targetAnsetzen_value.getText());
            }
        });

        station_list_admin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                getPresenter().getSelectedValueFromList(station_list_admin, ausgewhlteStation_value, evt);
            }
        });

        adminView.add(ausgewhlteStation_value);


        JLabel label = new JLabel("Stationen");
        label.setBounds(16, 11, 114, 14);
        adminView.add(label);
        panelCont.add(adminView, "adminP");
    }

    /**
     * @param mainView 
     * @param panelCont 
     * @param goToAdminView 
     * @param targetValue 
     * @param stationen_list 
     * 
     */
    private void buildMainView(JPanel mainView, JPanel panelCont, JButton goToAdminView) {

        JTextField stationId_value;
        mainView.setBackground(Color.WHITE);
        mainView.setLayout(null);
        goToAdminView.setBounds(10, 344, 114, 23);

        mainView.add(goToAdminView);
        mainView.setBackground(Color.LIGHT_GRAY);

        panelCont.add(mainView, "auswertungP");

        JLabel station_label = new JLabel("Stationen");
        station_label.setBounds(10, 11, 114, 14);
        mainView.add(station_label);

        stationId_value = new JTextField();
        stationId_value.setColumns(10);
        stationId_value.setEditable(false);
        stationId_value.setBackground(Color.LIGHT_GRAY);
        stationId_value.setBounds(262, 27, 157, 22);
        mainView.add(stationId_value);

        stationen_list_auswertung.setBounds(10, 27, 114, 232);

        stationen_list_auswertung.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                getPresenter().getSelectedValueFromList(stationen_list_auswertung, stationId_value, evt);
            }
        });

        JScrollPane scroll = new JScrollPane(stationen_list_auswertung);
        scroll.setBounds(10, 27, 114, 232);
        mainView.add(scroll);

        JLabel stationId_label = new JLabel("Station ID:");
        stationId_label.setBounds(166, 33, 70, 16);
        mainView.add(stationId_label);

        JLabel datum_label = new JLabel("Datum:");
        datum_label.setBounds(166, 60, 56, 16);
        mainView.add(datum_label);

        JLabel target_label = new JLabel("Target:");
        target_label.setBounds(166, 87, 56, 16);
        mainView.add(target_label);

        JLabel aktuellWert_label = new JLabel("Aktuell Wert:");
        aktuellWert_label.setBounds(166, 114, 86, 16);
        mainView.add(aktuellWert_label);

        JLabel varianz_label = new JLabel("Varianz:");
        varianz_label.setBounds(166, 141, 56, 16);
        mainView.add(varianz_label);

        JTextField datum_value;
        datum_value = new JTextField();
        datum_value.setColumns(10);
        datum_value.setBounds(262, 54, 157, 22);
        mainView.add(datum_value);

        target_value = new JTextField();
        target_value.setColumns(10);
        target_value.setEditable(false);
        target_value.setText(target);
        target_value.setBackground(Color.LIGHT_GRAY);
        target_value.setBounds(262, 81, 157, 22);
        mainView.add(target_value);

        JTextField aktuellWert_value;
        aktuellWert_value = new JTextField();
        aktuellWert_value.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent wert) {
                getPresenter().setNumberRestriction(wert);
            }
        });
        aktuellWert_value.setColumns(10);
        aktuellWert_value.setBounds(262, 108, 157, 22);
        mainView.add(aktuellWert_value);

        JTextField varianz_value;
        varianz_value = new JTextField();
        varianz_value.setColumns(10);
        varianz_value.setBounds(262, 138, 157, 22);
        mainView.add(varianz_value);

        JButton calculateVarianz = new JButton("Varianz berechnen");
        calculateVarianz.setBounds(262, 173, 157, 23);
        mainView.add(calculateVarianz);

        calculateVarianz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                getPresenter().calculateVariance(aktuellWert_value, target_value, varianz_value, stationId_value);
            }
        });

        JCheckBox showDiagram = new JCheckBox("Diagramm anzeigen");
        showDiagram.setBounds(262, 215, 157, 23);
        mainView.add(showDiagram);



    }

    public void setPresenter(Presenter pres) {
        presenter = pres;
    }

    public Presenter getPresenter() {
        return presenter;
    }

    //TODO to be remove
    public void updateStatusLabel(String result) {
        // TODO Auto-generated method stub

    }
}
