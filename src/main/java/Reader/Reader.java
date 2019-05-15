package Reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import modeles.Comments;

import misc.TurnInto;
import modeles.Post;


public class Reader {
	
	private Entree input;
	
	public Reader(Entree input) {
		this.input = input;
		
	}
	public String[] read(BufferedReader buff) {
		try{
			String ligne;			
			ligne=buff.readLine();
			//System.out.println(ligne);
			String[] mots = ligne.split("\\|");
			for (int i=0;i<mots.length;i++) {
				//System.out.println(mots[i]);
			}			
			return mots;
			}					
			catch (Exception e){
			//System.out.println(e.toString());
			return null;
			}
		
	}
	
	
	
	public void makeInput() throws IOException {
		InputStream fluxPosts=new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/dataDebs/posts.dat"); 
		InputStreamReader lecturePosts=new InputStreamReader(fluxPosts);
		BufferedReader buffPosts=new BufferedReader(lecturePosts);
		InputStream fluxComments=new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/dataDebs/comments.dat"); 
		InputStreamReader lectureComments=new InputStreamReader(fluxComments);
		BufferedReader buffComments=new BufferedReader(lectureComments);
		String[] motsPosts = read(buffPosts);
		String[] motsComments = read(buffComments);
		do {
		//for(int i=0;i<10;i++) {
			if (TurnInto.timeStamp(motsPosts[0]).before(TurnInto.timeStamp(motsComments[0]))) {
				Post P = toPost(motsPosts);
				// M�thode pour envoyer le post dans la chaine principale
				//input
				
				motsPosts = read(buffPosts);				
			}
			else {
				//Comments C = toComment(motsComments);
				// M�thode pour envoyer le commentaire dans la chaine principale
				//send(C);
				motsComments = read(buffComments);
			}
			
		//}
		}while(motsPosts!=null&&motsComments!=null);
		
		buffPosts.close();
		buffComments.close();
		System.out.println("Done");
	}	

	
	public static Post toPost(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		return new Post(t,Integer.valueOf(mots[1]),mots[4],10);

	}
	
	
}
