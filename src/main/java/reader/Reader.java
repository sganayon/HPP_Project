package reader;

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
	public static final String PATH = System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/";
	private static InputStream fluxPosts = null;
	private static InputStream fluxComments = null;
	private static InputStreamReader ReaderPosts = null;
	private static InputStreamReader ReaderComments = null;
	private static BufferedReader buffPosts = null;
	private static BufferedReader buffComments = null;
	
	public Reader() {
		
	}
	
	/**
	 * read a ligne from a bufferedReader
	 * @param buff
	 * @return the ligne or null if there are no more lignes
	 */
	public static String readBuff(BufferedReader buff) {
		String ligne = null;			
		try {
			ligne=buff.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ligne;
	}
	
	/**
	 * open all flux, create streamReader, bufferedRead
	 * @param postPath the path to the posts file
	 * @param commentPath the path to the comments file
	 */
	public static void openFlux(String postPath, String commentPath) {
		try {
			fluxPosts = new FileInputStream(PATH+postPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			fluxComments = new FileInputStream(PATH+commentPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		ReaderPosts=new InputStreamReader(fluxPosts);
		buffPosts=new BufferedReader(ReaderPosts);
		
		
		ReaderComments=new InputStreamReader(fluxComments);
		buffComments=new BufferedReader(ReaderComments);
	}
	
	/**
	 * close all flux, streamReader, BufferedReader
	 */
	public static void closeFlux() {
		try {
			fluxPosts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fluxComments.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ReaderPosts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ReaderComments.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	}
	
	/**
	 * read the files and add the data into Data class
	 * @param postsPath the path to the posts file
	 * @param commentsPath the path to the comments file
	 */
	public static void makeInput(String postsPath, String commentsPath){
		//first open and create all things we need
		openFlux(postsPath, commentsPath);
		
		//read the first line of the files
		String lignePost = readBuff(buffPosts);
		String ligneComment = readBuff(buffComments);			
		
		//while they are not null and not empty
		while(lignePost!=null&&ligneComment!=null&&!lignePost.isEmpty()&&!ligneComment.isEmpty()){
			
			//split the data
			String[] motsPosts = lignePost.split("\\|");
			String[] motsComments = ligneComment.split("\\|");
			
			//compare the timestamp and add the older to the Data class, then read the next line
			if (TurnInto.timeStamp(motsPosts[0]).before(TurnInto.timeStamp(motsComments[0]))) {
				Post P = toPost(motsPosts);
				// M�thode pour envoyer le post dans la chaine principale
				Data.addData(P);
				lignePost = readBuff(buffPosts);				
			}
			else {
				Comments C = toComment(motsComments);
				// M�thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				ligneComment = readBuff(buffComments);
			}
		}
		
		//one of the file is completely red, finish the reading on the other file
		if(lignePost==null) {
			while(ligneComment!=null) {
				Comments C = toComment(ligneComment.split("\\|"));
				// M�thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				ligneComment = readBuff(buffComments);
			}
		}
		else {
			while(lignePost!=null) {
				Post P = toPost(lignePost.split("\\|"));
				// M�thode pour envoyer le post dans la chaine principale
				Data.addData(P);
				lignePost = readBuff(buffPosts);
			}
		}
		
		//close everything
		closeFlux();
		System.out.println("Done");
	}	

	/**
	 * turn several strings into one class
	 * @param mots the splited line of a comment
	 * @return class that keep the data
	 */
	public static Comments toComment(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		int repId = (mots[5].isEmpty())?-1:Integer.valueOf(mots[5]);
		int postId = (mots.length==6)?-1:Integer.valueOf(mots[6]);
		return new Comments(t,Integer.valueOf(mots[1]),Integer.valueOf(mots[2]), repId, postId);
	}
	
	/**
	 * turn several strings into one class
	 * @param mots the splited line of a post
	 * @return class that keep the data
	 */
	public static Post toPost(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		return new Post(t,Integer.valueOf(mots[1]),mots[4]);
	}
}
