/**
 * 
 */
package view;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author kevin.tchagwo
 *
 */
public interface IView {

    public void buildAdminView(JPanel adminView, JPanel panelCont, JButton backButtonAdmin);

    public void buildMainView(JPanel mainView, JPanel panelCont, JButton goToAdminView);

}
