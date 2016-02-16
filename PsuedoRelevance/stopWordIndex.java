public class stopWordIndex{
	public String word;
	private int to;
	public int from;
	
	public stopWordIndex(String word,int to, int from){
		this.word=word;
		this.to = to;
		this.from=from;
		
		}
	public String getWord(){
		return word;
	
		}
	public int getTo(){
		return to;
		}
	public int getFrom(){
		return from;
		}		
	
	
	}
