package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.ufo.model.Anno;
import it.polito.tdp.ufo.model.Country;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public Set<Sighting> getSightings(int anno) {
		String sql = "SELECT * FROM sighting WHERE country = 'us' AND YEAR(DATETIME) = ? " ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			Set<Sighting> list = new HashSet<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Sighting(res.getInt("id"),
						res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), 
						res.getString("state"), 
						res.getString("country"),
						res.getString("shape"),
						res.getInt("duration"),
						res.getString("duration_hm"),
						res.getString("comments"),
						res.getDate("date_posted").toLocalDate(),
						res.getDouble("latitude"), 
						res.getDouble("longitude"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Anno> getAnni(){
		
		String sql = "SELECT YEAR(DATETIME) AS anno, COUNT(*) AS na " + 
				"FROM sighting " + 
				"WHERE country = 'us' " + 
				"GROUP BY YEAR(DATETIME) ORDER BY anno" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Anno> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Anno(res.getInt("anno"),
						res.getInt("na"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public void loadAllVertex(Graph<Country, DefaultEdge> grafo, Map<String, Country> countryIdMap, int anno) {
		
		String sql = "SELECT state FROM sighting WHERE country = 'us' AND YEAR(DATETIME) = ? GROUP BY state" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {

				Country c = new Country(res.getString("state"));
				if(!countryIdMap.containsKey(res.getString("state"))) {
					grafo.addVertex(c);
					countryIdMap.put(res.getString("state"),c);
				}
			}
			
			conn.close();
	

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}

	public void loadAllEdges(Graph<Country, DefaultEdge> grafo, Map<String, Country> countryIdMap, int anno) {

		String sql = "SELECT s1.state, s2.state " + 
				"FROM sighting s1, sighting s2 " + 
				"WHERE s1.country = s2.country AND s1.country = 'us' AND NOT s1.state=s2.state " + 
				"AND YEAR(s1.DATETIME) = YEAR(s2.DATETIME) AND YEAR(s1.DATETIME) =  ? " + 
				"AND s2.DATETIME>s1.DATETIME " + 
				"GROUP BY s1.state, s2.state" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {

				
				if(countryIdMap.containsKey(res.getString("s1.state")) && countryIdMap.containsKey(res.getString("s2.state")) ) {
					
					grafo.addEdge(countryIdMap.get(res.getString("s1.state")), countryIdMap.get(res.getString("s2.state")));
					
				}
				else
					System.out.println("Errore nel DB");
			}
			
			conn.close();
	

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
	}
	
	

}
