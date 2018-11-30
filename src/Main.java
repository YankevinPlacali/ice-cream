import java.awt.EventQueue;

import model.Model;
import presenter.Presenter;
import view.View;

/**
 * 
 */

/**
 * @author kevin.tchagwo
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    View window = new View();
                    window.setPresenter(new Presenter(window, new Model()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
