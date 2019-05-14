package Reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import misc.TurnInto;
import modeles.Comments;
import modeles.Post;


public class Reader {
	
	
	public String[] Reader() {
		try{
			InputStream flux=new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/comments.dat"); 
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
	
	public static Comments toComment(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		int repId = (mots[5].isEmpty())?-1:Integer.valueOf(mots[5]);
		int postId = (mots[6].isEmpty())?-1:Integer.valueOf(mots[6]);
		return new Comments(t,Integer.valueOf(mots[1]),Integer.valueOf(mots[2]), repId, postId);
	}
	
	public static Post toPost(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		return new Post(t,Integer.valueOf(mots[1]),mots[4]);
	}
}
