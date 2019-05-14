package Reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import modeles.Comments;
import modeles.Post;


public class Reader {
	
	public Reader() {
		
	}
	public String[] read(BufferedReader buff) {
		try{
			String ligne;			
			ligne=buff.readLine();
			System.out.println(ligne);
			String[] mots = ligne.split("\\|");
			for (int i=0;i<mots.length;i++) {
				System.out.println(mots[i]);
			}			
			return mots;
			}					
			catch (Exception e){
			//System.out.println(e.toString());
			return null;
			}
		
	}
	
	public Date toDate(String S) {
		Date date=null;
		return date;
	}
	
	public Post toPost(String[] S) {
		Post date=null;
		return date;
	}
	public Comments toComment(String[] S) {
		Comments date=null;
		return date;
	}
	
	public void makeInput() throws IOException {
		InputStream fluxPosts=new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/posts.dat"); 
		InputStreamReader lecturePosts=new InputStreamReader(fluxPosts);
		BufferedReader buffPosts=new BufferedReader(lecturePosts);
		InputStream fluxComments=new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/posts.dat"); 
		InputStreamReader lectureComments=new InputStreamReader(fluxComments);
		BufferedReader buffComments=new BufferedReader(lectureComments);
		String[] motsPosts = read(buffPosts);
		String[] motsComments = read(buffComments);
		//do {
		for(int i=0;i<10;i++) {
			if (toDate(motsPosts[0]).before(toDate(motsComments[0]))) {
				Post P = toPost(motsPosts);
				// Méthode pour envoyer le post dans la chaine principale
				//send(P);
				
				motsPosts = read(buffPosts);				
			}
			else {
				Comments C = toComment(motsComments);
				// Méthode pour envoyer le commentaire dans la chaine principale
				//send(C);
				motsComments = read(buffComments);
			}
			
		}
		//}while(motsPosts!=null&&motsComments!=null);
		
		buffPosts.close();
		buffComments.close();
		
	}
	
	
}
