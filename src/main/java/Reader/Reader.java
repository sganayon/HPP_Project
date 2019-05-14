package Reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;

import modeles.Post;


public class Reader {
	
	
	public String[] Reader() {
		try{
			InputStream flux=new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/posts.dat"); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;			
			ligne=buff.readLine();
			System.out.println(ligne);
			String[] mots = ligne.split("\\|");
			for (int i=0;i<mots.length;i++) {
				System.out.println(mots[i]);
			}
			buff.close();
			return mots;
			}					
			catch (Exception e){
			//System.out.println(e.toString());
			return null;
			}
		
	}
	
	public static Post toPost(String[] mots) {
		Timestamp t;
		
		return null;
	}
}
