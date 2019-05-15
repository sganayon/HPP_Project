package Reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import modeles.Comments;
import modeles.Entree;
import misc.Data;
import misc.TurnInto;
import modeles.Comments;
import modeles.Post;


public class Reader {
	
	public Reader() {
		
	}
	public static String[] read(BufferedReader buff) {
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
	
	public static Comments toComment(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		int repId = (mots[5].isEmpty())?-1:Integer.valueOf(mots[5]);
		int postId = (mots[6].isEmpty())?-1:Integer.valueOf(mots[6]);
		return new Comments(t,Integer.valueOf(mots[1]),Integer.valueOf(mots[2]), repId, postId);
	}
	
	
	public static void makeInput(){
		InputStream fluxPosts = null;
		try {
			fluxPosts = new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/Tests/Q1Basic2/posts.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		InputStreamReader lecturePosts=new InputStreamReader(fluxPosts);
		BufferedReader buffPosts=new BufferedReader(lecturePosts);
		
		InputStream fluxComments = null;
		try {
			fluxComments = new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/Tests/Q1Basic2/comments.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		InputStreamReader lectureComments=new InputStreamReader(fluxComments);
		BufferedReader buffComments=new BufferedReader(lectureComments);
		String[] motsPosts = read(buffPosts);
		String[] motsComments = read(buffComments);
		while(motsPosts!=null&&motsComments!=null){
			if (TurnInto.timeStamp(motsPosts[0]).before(TurnInto.timeStamp(motsComments[0]))) {
				Post P = toPost(motsPosts);
				// Mï¿½thode pour envoyer le post dans la chaine principale
				Data.addData(P);
				motsPosts = read(buffPosts);				
			}
			else {
				Comments C = toComment(motsComments);
				// Mï¿½thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				motsComments = read(buffComments);
			}
			
		//}
		}
		
		if(motsPosts==null) {
			while(motsComments!=null) {
				Comments C = toComment(motsComments);
				// Mï¿½thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				motsComments = read(buffComments);
			}
		}
		else {
			while(motsPosts!=null) {
				Post P = toPost(motsPosts);
				// Mï¿½thode pour envoyer le post dans la chaine principale
				Data.addData(P);
				motsPosts = read(buffPosts);
			}
		}
		try {
			buffPosts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			buffComments.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done");
	}	

	
	public static Post toPost(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		return new Post(t,Integer.valueOf(mots[1]),mots[4]);
	}
	
	public static void makeInput(String Post, String Comment){
		InputStream fluxPosts = null;
		try {
			fluxPosts = new FileInputStream(Post);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		InputStreamReader lecturePosts=new InputStreamReader(fluxPosts);
		BufferedReader buffPosts=new BufferedReader(lecturePosts);
		
		InputStream fluxComments = null;
		try {
			fluxComments = new FileInputStream(Comment);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		InputStreamReader lectureComments=new InputStreamReader(fluxComments);
		BufferedReader buffComments=new BufferedReader(lectureComments);
		String[] motsPosts = read(buffPosts);
		String[] motsComments = read(buffComments);
		do {
			if (TurnInto.timeStamp(motsPosts[0]).before(TurnInto.timeStamp(motsComments[0]))) {
				Post P = toPost(motsPosts);
				// Mï¿½thode pour envoyer le post dans la chaine principale
				Data.addData(P);
				motsPosts = read(buffPosts);				
			}
			else {
				Comments C = toComment(motsComments);
				// Mï¿½thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				motsComments = read(buffComments);
			}
			
		//}
		}while(motsPosts!=null&&motsComments!=null);
		
		if(motsPosts==null) {
			while(motsComments!=null) {
				Comments C = toComment(motsComments);
				// Mï¿½thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				motsComments = read(buffComments);
			}
		}
		else {
			while(motsPosts!=null) {
				Post P = toPost(motsPosts);
				// Méthode pour envoyer le post dans la chaine principale
				Data.addData(P);
				motsPosts = read(buffPosts);
			}
		}
		
		try {
			buffPosts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			buffComments.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done");
	}	

	
	
}
