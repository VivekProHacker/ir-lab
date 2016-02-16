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
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Input the Doc Id which you think is the relevant document following space between them");
		String query2=keyboard.nextLine();//capture the relevant Doc_id
		
		String token1[]=query2.split(" ");
		//sort an array.
		Arrays.sort(token1);
		Integer[] token2=new Integer[token1.length];
		token2=parseInt(token1);//Parse the String into Integer
		double[] optQuery=new double[treeSize(frequencyData)];
		
		String[] token=getIntialQuery();//Get Intial Query vector
		//Find the qptimal Query.
		optQuery=optimalQuery(token2,token,frequencyData,flist.length );
		String fQuery=finalquery(optQuery,frequencyData,flist.length);
		System.out.println("The final query is :" + fQuery);
		
		System.out.println("=============New Rank List is: =================");
		String newtoken[]=fQuery.split(" ");
		//sort an array.
		Arrays.sort(newtoken);
		
		rank(frequencyData,doclist,newtoken,flist);
		
}
	
	public static int treeSize(TreeMap<String,ArrayList > frequencyData){
		return frequencyData.size();
		}
	

	public static Integer[] parseInt(String[] token1){
		Integer[] token=new Integer[token1.length];
		for(int i=0;i<token1.length;i++)
		token[i]=Integer.parseInt(token1[i]);
		return token;
		}
	
	public static String finalquery(double[] optQuery,TreeMap<String,ArrayList > frequencyData,int fileSize){
		
		String fQuery="";
		
		
		double[] temp=optQuery;
		Integer[] temp2= new Integer[temp.length];
		
		
		
		for(int i=0;i<temp.length;i++)
		temp2[i]=i;
		Arrays.sort(temp2,new Comparator<Integer>(){
			public int compare(final Integer o1,final Integer o2){
				return Double.compare(temp[o1],temp[o2]);
				}
			});	
		
			
		//double[] temp=optQuery;
		//double[] temp2= new double[10];
		/*
		int tempMax=0;
		double nextMax=9999999.00;
		for(int x=0;x<10;x++){
		tempMax=0;
		for(int i=0;i<fileSize;i++){
			if(temp[i]>temp[tempMax]&& temp[i]<nextMax){
				tempMax=i;
				
				}
			
			}
			System.out.println("index: "+tempMax);
			temp2[x]=tempMax;
			nextMax=temp[tempMax];
			
		}
		
			Arrays.sort(temp2);
			*/
			Integer[] temp3=new Integer[20];
			for(int i=0;i<20;i++)
			temp3[i]=temp2[temp2.length-i-1];
			int in=0;
			Arrays.sort(temp3);
			int index=0;
		for(String word : frequencyData.keySet()){
					if(temp3[in]==index){
				fQuery=fQuery+" "+word;
				in++;
			
				}
				if(in==20){
					break;
					}
			index++;
			}
		return fQuery;
		
		
		
	/*
				String fQuery="";
			int index=0;
		for(String word : frequencyData.keySet( )){
			if(optQuery[index]>0.5){
				fQuery=fQuery+" "+word;
				}
			index++;
			}
		return fQuery;
*/
		}	
	public static double[] optimalQuery( Integer[] relevant,String[] token,TreeMap<String,ArrayList > frequencyData ,int size){
		double[] optQuery=new double[treeSize(frequencyData)];
		double[] relMatrix=new double[treeSize(frequencyData)];
		//double[] nonRelMatrix=new double[treeSize(frequencyData)];
		Arrays.fill(optQuery,0.0);
		Arrays.fill(relMatrix,0.0);
		//Arrays.fill(nonRelMatrix,0.0);
		optQuery=IntialQueryVector(token,frequencyData);
		
		// relevant array contains Id of relevant documents
		for(int j=0;j<size;j++){
		for(int i=0;i<relevant.length;i++){
			if( j==relevant[i] ){// if the id match
			double[] temp=new double[treeSize(frequencyData)];
			temp=RelevantMatrix(relevant[i],frequencyData);	
			//System.out.println(relMatrix.length+"  "+treeSize(frequencyData));
			for(int x=0;x<treeSize(frequencyData);x++){	
			relMatrix[x]=relMatrix[x]+temp[x];
			
			}
			}else{
			/*	
				double[] temp=new double[treeSize(frequencyData)];	
				temp=RelevantMatrix(j,frequencyData);
			for(int x=0;x<treeSize(frequencyData);x++){	
			nonRelMatrix[x]=nonRelMatrix[x]+temp[x];
		}
		*/
		}
			}
		}
		double[] temp=new double[treeSize(frequencyData)];
		temp=IntialQueryVector(token,frequencyData);	
			for(int x=0;x<treeSize(frequencyData);x++){	
			optQuery[x]=temp[x]+0.75*(relMatrix[x]/relevant.length)/relevant.length;
		}
		//printing
		
		
return optQuery;
}
	
	public static double[] RelevantMatrix(int index,TreeMap<String,ArrayList > frequencyData){
		//index contains id of the document. 
		double[] relvanceMatrix= new double[treeSize(frequencyData)];
		int in=0;
		Arrays.fill(relvanceMatrix,0.0);
		for(String word : frequencyData.keySet()){
		ArrayList<termfreq> obj1=new ArrayList<termfreq>();
		obj1=frequencyData.get(word);
		for(int i=0;i<obj1.size();i++){
		if(obj1.get(i).doc_id==(index)){
		relvanceMatrix[in]=obj1.get(i).term_freq;
		//System.out.print(in+"&"+obj1.get(i).term_freq+" ");
		}
		}
		in++;
	
	}
	return relvanceMatrix;
}
	public static Integer[] summation( Integer[] matrix,TreeMap<String,ArrayList > frequencyData){
		Integer[] Matrix= new Integer[treeSize(frequencyData)];
		Arrays.fill(Matrix,0);
		for(int i=0;i<matrix.length;i++)
		Matrix[i]=Matrix[i]+matrix[i];
		return Matrix;
		}
	public static double[] IntialQueryVector(String[] token ,TreeMap<String,ArrayList > frequencyData ){
		
		double[] queryMatrix= new double[treeSize(frequencyData)];
		int index=0;
		Arrays.fill(queryMatrix,0.0);
		for(String word : frequencyData.keySet()){
			
		for(int i=0;i<token.length;i++){
			if(word.equals(token[i])){
			queryMatrix[index]=1;
			
			//System.out.print(index);	
		}
		
			}
			index++;
		
		}
	return queryMatrix;
	}
	

}

//होश 
//होश

//होटल
//मुल्ला  नसरुद्दीन
//1059
//1051
//38905
