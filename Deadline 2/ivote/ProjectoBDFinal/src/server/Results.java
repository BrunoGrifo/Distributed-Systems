package server;

import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
public class Results implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String,HashMap<Lista_candidata,HashMap<Integer,Double>>> results;
	HashMap<Integer,Double> blanc;
	
	Results(HashMap<String,HashMap<Lista_candidata,HashMap<Integer,Double>>> results,HashMap<Integer,Double> blanc){
		this.results=results;
		this.blanc=blanc;
	}
	
	public String toString() {
		String results2="";
		for(HashMap.Entry<String,HashMap<Lista_candidata,HashMap<Integer,Double>>> entry : results.entrySet()) {
			results2+=entry.getKey()+"\n";
			for(HashMap.Entry<Lista_candidata,HashMap<Integer,Double>> entry2 : entry.getValue().entrySet()) {
				results2+=entry2.getKey()+"\t";
				for(HashMap.Entry<Integer,Double> entry3 : entry2.getValue().entrySet()) {
					results2+="votes of list:"+entry3.getKey() + "\tpercentace of votes:"+entry3.getValue()+"%\n";
				}
			}
		}
		for(HashMap.Entry<Integer,Double> entry:blanc.entrySet()) {
			results2+="Blanc votes:"+entry.getKey()+"\tpercentage of null votes:"+entry.getValue()+"%\n";
		}
		return results2;
	}
}
