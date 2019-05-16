package reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import misc.Data;
import misc.TurnInto;
import modeles.Comments;
import modeles.Post;

public class ThreadReaderPost implements Runnable {

	public static final String PATH = System.getProperty("user.home") + "\\Local Settings\\Application Data"
			+ "/HPP_Project/";
	private static InputStream flux = null;
	private static InputStreamReader Reader = null;
	private static BufferedReader buff = null;

	@Override
	public void run() {
		makeInputPost("\\Tests\\Q1BigTest\\posts.dat");

	}

	public ThreadReaderPost() {

	}

	/**
	 * read a ligne from a bufferedReader
	 * 
	 * @param buff
	 * @return the ligne or null if there are no more lignes
	 */
	public static String readBuff(BufferedReader buff) {
		String ligne = null;
		try {
			ligne = buff.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ligne;
	}

	/**
	 * open all flux, create streamReader, bufferedRead
	 * 
	 * @param postPath    the path to the posts file
	 * @param commentPath the path to the comments file
	 */
	public static void openFlux(String Path) {

		try {
			flux = new FileInputStream(PATH + Path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Reader = new InputStreamReader(flux);
		buff = new BufferedReader(Reader);
	}

	/**
	 * close all flux, streamReader, BufferedReader
	 */
	public static void closeFlux() {
		try {
			flux.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * read the files and add the data into Data class
	 * 
	 * @param postsPath    the path to the posts file
	 * @param commentsPath the path to the comments file
	 */
	public static void makeInputPost(String Path) {
		// first open and create all things we need
		openFlux(Path);

		// read the first line of the files
		String ligne = readBuff(buff);

		// while they are not null and not empty
		while (ligne != null && !ligne.isEmpty()) {

			// split the data
			String[] motsPosts = ligne.split("\\|");

			Post P = toPost(motsPosts);
			// M�thode pour envoyer le post dans la chaine principale
			Data.addData(P);
			ligne = readBuff(buff);

		}
		Post Pfin = new Post(null, -1, "");
		Data.addData(Pfin);
		// close everything
		closeFlux();
		System.out.println("Done");
	}
	
	

	/**
	 * turn several strings into one class
	 * 
	 * @param mots the splited line of a comment
	 * @return class that keep the data
	 */
	public static Comments toComment(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		int repId = (mots[5].isEmpty()) ? -1 : Integer.valueOf(mots[5]);
		int postId = (mots.length == 6) ? -1 : Integer.valueOf(mots[6]);
		return new Comments(t, Integer.valueOf(mots[1]), Integer.valueOf(mots[2]), repId, postId);
	}

	/**
	 * turn several strings into one class
	 * 
	 * @param mots the splited line of a post
	 * @return class that keep the data
	 */
	public static Post toPost(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		return new Post(t, Integer.valueOf(mots[1]), mots[4]);
	}

}
