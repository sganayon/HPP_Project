package reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import modeles.Comments;
import modeles.Entree;
import misc.Data3;
import misc.TurnInto;
import modeles.Post;

public class Reader3 implements Runnable{
	private static InputStream fluxPosts = null;
	private static InputStream fluxComments = null;
	private static InputStreamReader ReaderPosts = null;
	private static InputStreamReader ReaderComments = null;
	private static BufferedReader buffPosts = null;
	private static BufferedReader buffComments = null;
	private static String Path=null;
	private static BlockingQueue<Entree> events=null;

	public Reader3(String Path, BlockingQueue<Entree> events) {
		this.Path = Path;
		this.events = events;
	}

	/**
	 * read a ligne from a bufferedReader
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
	 * @param Path the path to the files
	 */
	public static void openFlux(String Path) {
		try {
			fluxPosts = new FileInputStream(Path+"posts.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			fluxComments = new FileInputStream(Path+"comments.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ReaderPosts = new InputStreamReader(fluxPosts);
		buffPosts = new BufferedReader(ReaderPosts);

		ReaderComments = new InputStreamReader(fluxComments);
		buffComments = new BufferedReader(ReaderComments);
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
	 * @param Path the path to the files
	 * 
	 */

	public static void makeInput(){
		//first open and create all things we need
		openFlux(Path);

		// read the first line of the files
		String lignePost = readBuff(buffPosts);
		String ligneComment = readBuff(buffComments);

		// while they are not null and not empty
		while (lignePost != null && ligneComment != null && !lignePost.isEmpty() && !ligneComment.isEmpty()) {
			// split the data
			String[] motsPosts = lignePost.split("\\|");
			String[] motsComments = ligneComment.split("\\|");

			Calendar cp = TurnInto.date(motsPosts[0]);
			Calendar cc = TurnInto.date(motsComments[0]);
			
			Duration duration = Duration.between(cp.toInstant(), cc.toInstant());
			
			//compare the date and add the older to the Data class, then read the next line
			if (duration.isNegative()) {
				
				Comments C = toComment(motsComments);
//				if(C.getPostId() == Long.valueOf("962098904011")) {
//					
//					System.out.println();
//				}
				
				// M�thode pour envoyer le commentaire dans la chaine principale
				long id = Data3.addComment(C);
				try {
					events.put(new Entree(id,C.getTime()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ligneComment = readBuff(buffComments);		
			}
			else {
				Post P = toPost(motsPosts);
				// M�thode pour envoyer le post dans la chaine principale
				Data3.addPost(P);
				try {
					events.put(new Entree(P.getId(),P.getTime()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lignePost = readBuff(buffPosts);	
			}
		}

		// one of the file is completely red, finish the reading on the other file
		if (lignePost == null) {
			while (ligneComment != null) {
				Comments C = toComment(ligneComment.split("\\|"));
				// M�thode pour envoyer le commentaire dans la chaine principale
				long id = Data3.addComment(C);
				try {
					events.put(new Entree(id,C.getTime()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ligneComment = readBuff(buffComments);
			}
		} else {
			while (lignePost != null) {
				Post P = toPost(lignePost.split("\\|"));
				// M�thode pour envoyer le post dans la chaine principale
				Data3.addPost(P);
				try {
					events.put(new Entree(P.getId(),P.getTime()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lignePost = readBuff(buffPosts);
			}
		}
		
		try {
			events.put(new Entree(-1,null));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// close everything
		closeFlux();
		System.out.println("Done Reading");
	}

	/**
	 * turn several strings into one class
	 * @param mots the splited line of a comment
	 * @return class that keep the data
	 */
	public static Comments toComment(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		long repId = (mots[5].isEmpty()) ? -1 : Long.valueOf(mots[5]);
		long postId = (mots.length == 6) ? -1 : Long.valueOf(mots[6]);
		return new Comments(t, Long.valueOf(mots[1]), Long.valueOf(mots[2]), repId, postId);
	}

	/**
	 * turn several strings into one class
	 * @param mots the splited line of a post
	 * @return class that keep the data
	 */
	public static Post toPost(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		return new Post(t, Long.valueOf(mots[1]), mots[4]);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		makeInput();
	}
}
