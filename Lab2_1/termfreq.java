import java.io.Serializable;

public class termfreq implements Serializable{
 int doc_id;
 int term_freq;

public termfreq(int doc_id){
	this.doc_id=doc_id;
	term_freq=1;

	}

}
