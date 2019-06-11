/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.Anno;
import it.polito.tdp.ufo.model.Country;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Anno> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<Country> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;

	public void caricaAnni() {
		boxAnno.getItems().addAll(model.getAnni());
	}
	
    @FXML
    void handleAnalizza(ActionEvent event) {

    	txtResult.clear();
    	
    	Country country = boxStato.getValue();
    	
    	if(country!=null) {
    		
    		txtResult.appendText("Stati precedenti\n\n");
    	    List<Country> prec = model.getPrecedenti(country);
    	    for(Country c : prec)
                txtResult.appendText(c.getState()+"\n");   

    	    txtResult.appendText("\n\nStati successivi\n\n");
    	   
    	    List<Country> succ = model.getSuccessivi(country);
    	    for(Country c : succ)
                txtResult.appendText(c.getState()+"\n");
    	    
    	    List<Country> raggiungibili = model.statiRaggiungibili(country);
    	    
    	    txtResult.appendText("\n\nSono raggiungibili i seguenti "+raggiungibili.size()+" stati: \n\n");
    	    for(Country c : raggiungibili)
                txtResult.appendText(c.getState()+"\n");
    	}
    	else
    		txtResult.appendText("Si prega di inserire un paese");
    	
    	
    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {

    	//creo il grafo
    	
    	txtResult.clear();
    	
    	Anno anno =  boxAnno.getValue();
    	if(anno!=null) {
    		
    		model.creaGrafo(anno.getAnno());
    		boxStato.getItems().addAll(model.getCountries());
    		txtResult.appendText("Non cambiare anno, seleziona un paese per continuare");
    	}
    	else {
    		txtResult.appendText("Si prega di inserire un anno");
    	}
    	
    }

    @FXML
    void handleSequenza(ActionEvent event) {

    	txtResult.clear();
    	Country country = boxStato.getValue();
    	
    	if(country!=null) {
    		
    		txtResult.appendText("Il cammino più probabile partendo da "+country.toString().toUpperCase()+" è:\n");
    	
    		List<Country> cammino = model.cercaCammino(country);
    		 for(Country c : cammino)
                 txtResult.appendText(c.getState()+"\n");
    		
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

       
    }

	public void setModel(Model model) {
		
       this.model = model;
		
	}
}
