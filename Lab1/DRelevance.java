//package vivekProHacker;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;


public class DRelevance extends Indexer {


	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
	
		findIndexer();
		File[] flist=getFileList();
		System.out.println("Number of Documents in the Collection is: " + flist.length);
		System.out.println("Number of Terms in the Collection is: " + frequencyData.size());
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter the Term to know its term frequency");
		String query2=keyboard.next();//capture the relevant Doc_id
		if(frequencyData.containsKey(query2)){
			ArrayList<termfreq> obj1=frequencyData.get(query2);
					for(int x=0;x<frequencyData.get(query2).size();x++){
						System.out.println("Term freq of word- "+query2+"in doc_id "+obj1.get(x).doc_id+"is :"+ obj1.get(x).term_freq);
						}
			System.out.println("Document frequency of term "+query2+"is :"+obj1.size());
			}
}
	

}

//होश 
//होश

//होटल
//मुल्ला  नसरुद्दीन
//1059
//1051
//38905
