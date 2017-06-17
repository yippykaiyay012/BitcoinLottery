/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinlotterywithgui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javax.swing.SwingWorker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author yippy
 */
public class FXMLDocumentController implements Initializable {

//public class mainForm implements Runnable {
//    private JPanel pnl1;
//    private JButton btnStop;
//    private JButton btnStart;
    @FXML
    private Label lblData;


    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;

    private Service<Void> backgroundThread;

    // private JLabel label = new JLabel("Watch this space", SwingConstants.CENTER);
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Bitcoin Lottery");
//
//       // Thread thread = new Thread();
//
//        frame.setContentPane(new mainForm().pnl1);
//
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//
//    }
//    public mainForm() {
//        btnStart.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                new Thread(new mainForm()).start();
//
//                }
//
//        });
//
//
//        btnStop.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                lblData.setText("pls");
//
//
//
//            }
//        });
//    }
    @FXML
    private void StopSearch(ActionEvent event) {

        // if(worker.)
        backgroundThread.cancel();
       

//        btnStop.setDisable(true);
//        btnStart.setDisable(false);
    }

    @FXML
    private void StartSearch(ActionEvent event) throws IOException, InterruptedException {
        //btnStart.setDisable(true);

        backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        long page = 1;
                        boolean found = false;
                        String addressElement = "";

                        System.out.println("Running...");

                        while (!found) {

                            long randomPage = (long) (Math.random() * 99999999 + 1) * (long) (Math.random() * 999999999 + 1) * (long) (Math.random() * 9 + 1);

                            String theURL = "http://directory.io/" + randomPage;

                            Document d = Jsoup.connect(theURL).timeout(6000).get();

                            Elements e = d.select("a");
                            for (Element element : e.select("a")) {
                                Elements info = element.select("a");
                                String test = info.toString();
                                Document doc = Jsoup.parse(test);
                                addressElement = doc.text();

                                if (!addressElement.contains("next") && !addressElement.contains("previous") && !addressElement.contains("+") && !addressElement.contains("database")) {

                                    // String searchURL = "https://bitref.com/" + addressElement;
                                    String searchURL = "https://blockchain.info/address/" + addressElement;

                                    //System.out.println(searchURL);
                                    Document balanceDoc = Jsoup.connect(searchURL).timeout(6000).get();
                                    Elements balanceElement = balanceDoc.select("td#final_balance");
                                    String balanceStringPhase1 = balanceElement.toString();
                                    Document yoloDoc = Jsoup.parse(balanceStringPhase1);
                                    String balanceStringLegit = yoloDoc.text();
                                    String balanceStringRefined = balanceStringLegit.replace("BTC", "");

                                    double balanceDouble = Double.parseDouble(balanceStringRefined);
                                    if (balanceDouble > 0.000) {
                                        Alert alert = new Alert(AlertType.CONFIRMATION);
                                        alert.setTitle("Balance Found");
                                        alert.setHeaderText("Balance Found on page: " + randomPage + "  In address: " + addressElement);
                                        System.out.println("Balance Found on page: " + randomPage + "  In address: " + addressElement);
                                        found = true;
                                        TimeUnit.MINUTES.sleep(99999);
                                    }

                                    //displaying.........or not
                                    //System.out.println(addressElement);
                                    //            lblData.setText(addressElement);
                                    //System.out.println(balanceStringLegit);
                                    //            lblBalance.setText(balanceStringLegit);
                                    //          System.out.println("                                        page:" + page + "     (" + randomPage + ")");
                                    //            lblPage.setText("" + randomPage);
                                    //publish (new DataObject(addressElement, (""+page), balanceStringRefined));
                                
                                    
                                    updateMessage(new DataObject(addressElement, (""+randomPage), balanceStringRefined).toString());
                                }

                            }
                            page++;
                        }

                        return null;
                    }

                };
            }

        };

        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                lblData.textProperty().unbind();
            }
        });

        lblData.textProperty().bind(backgroundThread.messageProperty());
        backgroundThread.start();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //btnStop.setDisable(true);
    }

}
