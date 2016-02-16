

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

public class Indexer {

    public static ArrayList<termfreq> doclist=new ArrayList<termfreq>();
    public static TreeMap<String,ArrayList > frequencyData = new TreeMap<String, ArrayList>();;
    public static HindiStemmerLight stemObj=new HindiStemmerLight();
    private static String RESULT_FNAME = "result.txt";
    private static String STOPWORD_INDEX = "StopwordIndex.txt";
    public static String[] sampleToken;
    public static File[] fileList;
	
    public Indexer(){
    	 doclist=new ArrayList<termfreq>();
         frequencyData = new TreeMap<String, ArrayList>();

         stemObj=new HindiStemmerLight();

    }

//-----  	@Contains all the method for Indexing the Documents.
    		public static void findIndexer() throws IOException, ClassNotFoundException{
    		Scanner keyboard = new Scanner(System.in);
			System.out.println("Enter The path of the collecetion");
			String filePath=keyboard.nextLine();
			File dir = new File(filePath);

			//Store the file name in an array.
			File[] flist=dir.listFiles();
			fileList=new File[flist.length];
			fileList=flist;

		System.out.println("Enter 1 to Construct the Indexer and 2 if the Indexer is already constructed");
		int option=keyboard.nextInt();
		switch(option){
		    case 1 :
       			createIndexFile(frequencyData, doclist,flist);
		       break;
		    case 2 :
       			frequencyData=readIndexFile();
		       break;

		    default : createIndexFile(frequencyData,doclist,flist);

		}



		System.out.println("Enter 1 if you want to print the posting List, 2 to Skip");
		int option2=keyboard.nextInt();
		switch(option2){
		    case 1 :
       			postingList(frequencyData,doclist);
		       break;
		    case 2:
			   break;

		}

		
    }

public static String[] getIntialQuery(){
	return sampleToken;
	}
//--------------------- @read the stop words
public static TreeMap<String,Integer > readStopWords(String stopWordsFilename)
    {
	TreeMap<String,Integer > stopWords = new TreeMap<String,Integer >();

	try
	    {
		Scanner stopWordsFile = new Scanner(new File(stopWordsFilename));
		int numStopWords = stopWordsFile.nextInt();

		for (int i = 0; i < numStopWords; i++){
		    stopWords.put( stopWordsFile.next(),1);


	}
		stopWordsFile.close();
	    }
	catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }

	return stopWords;
    }

//------          @read Index file from Disk
public static TreeMap<String,ArrayList> readIndexFile() throws IOException{
	FileInputStream fis = new FileInputStream("TreeMap");
		ObjectInputStream ois = new ObjectInputStream(fis);
		@SuppressWarnings("unchecked")

		TreeMap<String,ArrayList > list1 = new TreeMap<String,ArrayList >();

		try {
			list1=(TreeMap<String,ArrayList >) ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		ois.close();
		System.out.println("The leght is :" + list1.size());



	return list1;
	}
//------         @Create Index and Store it Into a disk
public static void createIndexFile(TreeMap<String,ArrayList > frequencyData,ArrayList<termfreq> doclist,File[] flist) throws FileNotFoundException, IOException{
		Scanner keyboard = new Scanner(System.in);

		int fileSize=flist.length;

/*call the function readStopWord which will read all the stopword
 * and store it into array
 */

		TreeMap<String,Integer> stopWords= readStopWords("Hindistopword.txt");


		/* For getting the number of threads required based on number of file
		 * Thread will remove the stopwords and Index all the files
		 * */
		int length =Integer.toString(fileSize).length();
		length=length-1; //
		int dividend=fileSize/(int)Math.pow(10,length);
		int remainder=fileSize% (int)Math.pow(10,length);

		if(remainder>0){
		Index[] obj=new Index[dividend+1];
		Thread[] thread=new Thread[dividend+1];

		for(int i=0;i<dividend;i++){
		 obj[i]= new Index( i*((int)Math.pow(10,length))+1,(i+1)*((int)Math.pow(10,length)),flist,stopWords,frequencyData,doclist );
		  thread[i]=new Thread(obj[i]);

		}
		obj[dividend]= new Index( dividend*((int)Math.pow(10,length))+1,(dividend+1)*((int)Math.pow(10,length))+remainder,flist,stopWords,frequencyData,doclist);
		thread[dividend]=new Thread(obj[dividend]);
		//start the threads
		for(int i=0;i<dividend+1;i++){
		thread[i].start();
		}
		//join the threads
		for(int i=0;i<dividend+1;i++){
		try{
		thread[i].join();
		} catch( InterruptedException e){
		System.out.println("main hread");
		}

		}
		}else{
		Index[] obj=new Index[dividend];
		Thread[] thread=new Thread[dividend+1];
		for(int i=0;i<dividend;i++){
		 obj[i]= new Index( i*(10^dividend)+1,(i+1)*(10^dividend),flist,stopWords,frequencyData,doclist );
		 thread[i]=new Thread(obj[i]);
		}

		//start the thread
		for(int i=0;i<dividend+1;i++){
		thread[i].start();
		}
		//join the threads
		for(int i=0;i<dividend;i++){
		thread[i]=new Thread(obj[i]);

		}

		}


		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TreeMap"));
		oos.writeObject(frequencyData);
		oos.flush();
		oos.close();


	}

	public static void postingList( TreeMap<String,ArrayList > frequencyData,ArrayList<termfreq> doclist){
		System.out.println("======================Posting List=======================");
		System.out.println("Term                     [Doc num,term frequency]");
		for(String word : frequencyData.keySet( )){
			System.out.printf("%15s  ", word);
			for(int x=0;x<frequencyData.get(word).size();x++){
				ArrayList<termfreq> obj1=frequencyData.get(word);
				if(obj1.get(x)!=null)
				System.out.printf("[ %d ,%d ]",obj1.get(x).doc_id,obj1.get(x).term_freq);
			}
			System.out.println();
			}

		}
public static File[] getFileList(){
	return fileList; 
	}		

}
class Index implements Runnable
{
	//int id,File file, String[] stopWords,ArrayList<stopWordIndex> stopWordIndx
	int id1;
	int id2;
	File[] flist;
	TreeMap<String,Integer> stopWords;

	TreeMap<String,ArrayList > frequencyData ;
	ArrayList<termfreq> doclist;

	public Index(int id1,int id2,File[] flist, TreeMap<String,Integer> stopWords,TreeMap<String,ArrayList > frequencyData,ArrayList<termfreq> doclist ){
		this.id1=id1;//start of the file
		this.id2=id2;//end of the file
		this.flist=flist;//array of file name
		this.stopWords=stopWords;

		this.frequencyData=frequencyData;
		this.doclist=doclist;
		}
	public TreeMap getTree(){
		return frequencyData;
		}
//thread will start from here
	public void run()
	{
	for(int id=id1;id<=id2;id++){
	String word;
	File file=flist[id];// read file by file
	String textFilename=file.getName();
	try
	    {
		Scanner textFile = new Scanner(new FileReader(file));

		System.out.println("Reading the file :"+id+"   name  "+textFilename);
		textFile.useDelimiter(Pattern.compile("<title>(.+?)</title>"));
		textFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));

	

		while (textFile.hasNext())
		    {
			//read word from the file
			word = textFile.next();
			//remove the "faltu " word.			
			word=word.replaceAll("[<>()?:!।/.,`'-;॥{}a-zA-z0-9]","के");
			
			//word=stemObj.stem(word);
			if(stopWords.containsKey(word)){
			    		}
			else{
						//Check if the word already is in Dictionary
						if (frequencyData.containsKey(word))
						{  // The word has occurred before, so get its count from the map
						@SuppressWarnings("unchecked")
						ArrayList<termfreq> obj1=frequencyData.get(word);
						int flag=0;
							if(obj1!=null)
							for(int x=0;x<obj1.size();x++){

								if(obj1.get(x)!=null)
								if(obj1.get(x).doc_id==id){
									obj1.get(x).term_freq++;
									flag=1;
									break;
									}

								}
								if(flag==0){
									
							termfreq obj=new termfreq(id);

							frequencyData.get(word).add(obj);
									}

						

						 // Auto-unboxed
						}else{//if its a new word in the collection

							doclist=new ArrayList<termfreq>();
							termfreq obj=new termfreq(id);
							doclist.add(obj);
							if(doclist!=null)
							frequencyData.put(word,doclist);

							}





				//end of the logic
			    //outFile.print(word + " ");
		    }
		    }
		//System.out.println("\n\nText after removing stop words is in " + RESULT_FNAME);
		//outFile.println();

		textFile.close();
		//outFile.close();
	    }
	catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }

}

	}


}
