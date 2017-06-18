/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoinlotterywithgui;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Random;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author yippy
 */
public class FXMLDocumentController implements Initializable {


    @FXML
    private Label lblData;


    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;

    private Service<Void> backgroundThread;
    
    
    
 


    
    @FXML
    private void StopSearch(ActionEvent event) {

        
        backgroundThread.cancel();
       

        btnStop.setDisable(true);
        btnStart.setDisable(false);
    }

    @FXML
    private void StartSearch(ActionEvent event) throws IOException, InterruptedException {
        btnStart.setDisable(true);
        btnStop.setDisable(false);

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
                            
//                            BigInteger rand = new BigDecimal(Math.random()).toBigInteger();
//                            BigInteger pages = (new BigInteger("904625697166532776746648320380374280100293470930272690489102837043110636675"));
//                            
//                            
//                            BigInteger randomPage = rand.multiply(pages);
//                            
//                            System.out.println(randomPage);
//                            
                            
                            
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
                                        
                                        alert.show();
                                        
                                        
                                        found = true;
                                        TimeUnit.MINUTES.sleep(99999);
                                    }

                                  
                                
                                    
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
        btnStop.setDisable(true);
        
 
        
    }

}
