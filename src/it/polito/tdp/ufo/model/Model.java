package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	
	private SightingsDAO dao;
	private Graph<Country, DefaultEdge> grafo;
	private Map<String, Country> countryIdMap;
	private List<Country> camminoAlieni;
	private Set<Country> visitati;
	

	
	public Model() {
		this.dao = new SightingsDAO();
	}
	
	public List<Anno> getAnni(){
		return dao.getAnni();
	}
	
	public void creaGrafo(int anno) {
		
		//creo il grafo
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
        this.countryIdMap = new HashMap<String, Country>();		
		//carico i vertici
		dao.loadAllVertex(grafo, countryIdMap, anno);
		
		//carico gli archi
	
		
		/*
		 * Funzionante ma lenta per un numero grande di eventi
		 */
		
		
		//	dao.loadAllEdges(grafo, countryIdMap, anno);
		
		
		
		//altrimenti
		
		
		/*
		 * Più veloce 
		 */
		
		Set<Sighting> avvistamenti = dao.getSightings(anno);
			
	     //per ogni avvistamento	
		for(Sighting avv1 : avvistamenti) {
			
			for(Sighting avv2 : avvistamenti) {
				
				//cerco un avvistamento in uno stato diverso avvenuto dopo
				
			   if(!avv1.getState().equals(avv2.getState()) && avv1.getDatetime().compareTo(avv2.getDatetime())<0) {
				   
				   //vediamo se è già stato inserito 
			
				  if(countryIdMap.containsKey(avv1.getState()) && countryIdMap.containsKey(avv2.getState())) {
				   
					  //se lo trovo inserisco un arco
					  
				   if(!grafo.containsEdge(countryIdMap.get(avv1.getState()), countryIdMap.get(avv2.getState()))) 
					     grafo.addEdge(countryIdMap.get(avv1.getState()), countryIdMap.get(avv2.getState()));
				   
				   
			   }
			  }
			}
			
		}
		
	}

	public List<Country> getCountries() {
		
		if(grafo!=null) {
		
		List<Country> ris = new LinkedList<>(grafo.vertexSet());
		Collections.sort(ris);
		
		return ris;
    	 }
	return null;
     }

	public List<Country> getPrecedenti(Country country) {
		
		if(grafo!=null) {
			
		    Set<DefaultEdge> incoming =  grafo.incomingEdgesOf(country);	
		    
		    List<Country> ris = new ArrayList<>();
		    
		    for(DefaultEdge de : incoming) {
		    	ris.add(grafo.getEdgeSource(de));
		    }
		    
		    Collections.sort(ris);
		    return ris;
		}
		
		return null;
	}

	public List<Country> getSuccessivi(Country country) {
            
		    if(grafo!=null) {
			
		    Set<DefaultEdge> incoming =  grafo.outgoingEdgesOf(country);	
		    
		    List<Country> ris = new ArrayList<>();
		    
		    for(DefaultEdge de : incoming) {
		    	ris.add(grafo.getEdgeTarget(de));
		    }
		    
		    Collections.sort(ris);
		    return ris;
		}
		
		return null;
	}
	

	public List<Country> statiRaggiungibili(Country source) {

		List<Country> result = new ArrayList<Country>();
		

		//GraphIterator<Fermata, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, source);
		GraphIterator<Country, DefaultEdge> it = new DepthFirstIterator<>(this.grafo, source);
	
		
		while (it.hasNext()) {
			result.add(it.next());
		}

	
		
		return result;

	}

	public List<Country> cercaCammino(Country partenza){
		
		 //Creiamo il parziale
		  
		    
		    List<Country> parziale = new ArrayList<Country>();
		    parziale.add(partenza);
		    this.camminoAlieni = new ArrayList<Country>(parziale);
		    
		    //all'inizio non ho visitato nessun country
		    this.visitati = new HashSet<>();
		    
		    cerca(parziale);
			
			return camminoAlieni;
		}

		
		
		private void cerca(List<Country> parziale) {

			//il concetto di base è che quando si toglie la partenza ho concluso perchè ho provato tutto
			while(!parziale.isEmpty()) {
			
			//Arrivo in un nuovo paese (mai visitato prima (?) ) e mi chiedo dove possa andare
			Country paese = parziale.get(parziale.size()-1);
			
		
			while(visitaInProfondita(visitati,parziale));
			
			//ritorna false se l'aereoporta sopra considerato è stato completamente esplorato!
			//in tal caso è necessario toglierlo dalla dal parziale e re-iterare
			
			if(parziale.size()>camminoAlieni.size()) {
				camminoAlieni = new ArrayList<>(parziale);
				System.out.format("max : %d\n",  parziale.size());
			}
			
			parziale.remove(paese);
			//siamo tornati indietro di un paese
			
			//aggiungo ai visitati solo quelli che rimuovo e da cui dunque non devo più passare
			// dal momento che esploro in profondità
			visitati.add(paese);
			
			}
			
		} 
	      			
		// metodo a cui passo il parziale e una lista di visitati e mi dice dove posso ancora visitare
		
		private boolean visitaInProfondita(Set<Country> visitati, List<Country> parziale ) {
			
			
			
			//chiamo questo metodo per vedere tra tutti i vicini quali sia quello dove posso andare 
			
			//Questo è il metod di visita in profondita
			
			for(Country vicino : getSuccessivi(parziale.get(parziale.size()-1))) {
				
				
				//non posso tornare indietro e se ho già visitato un vicino non torno a visitarlo
		           
				      if(!parziale.contains(vicino) && !visitati.contains(vicino)) {
		        	   
		        	  
		        		//se posso visitare il vicino lo aggiungo   
		                parziale.add(vicino);
		        	        
		        	   // vado in profondità
		        	   cerca(parziale);
		        	   
		        	
		        	   //se non posso andare avanti in profondità cerco un altro vicino attraverso il quale possa andare in profondità
		        	   
		           }
		         }
			
			//se non ho più vicini disponibili devo tornare indietro..
			
			return false;
			
		}
		
		

}
