package reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.concurrent.BlockingQueue;

import misc.Data;
import misc.RWLock;
import misc.TurnInto;
import modeles.Comments;
import modeles.Entree;
import modeles.Post;

public class ThreadReader implements Runnable{
	private String path;
	private InputStream fluxPosts = null;
	private InputStream fluxComments = null;
	private InputStreamReader ReaderPosts = null;
	private InputStreamReader ReaderComments = null;
	private BufferedReader buffPosts = null;
	private BufferedReader buffComments = null;
	private BlockingQueue<Timestamp> Queue =null;
	private RWLock rwlock = null;

	public ThreadReader(String path, BlockingQueue<Timestamp> Queue, RWLock rwlock) {
		super();
		this.path = path;
		this.Queue = Queue;
		this.rwlock = rwlock;
	}

	public String readBuff(BufferedReader buff) {
		String ligne = null;
		try {
			ligne = buff.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ligne;
	}
	
	public void openFlux(String Path) {
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
	
	public void closeFlux() {
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
	
	public void makeInput(String Path){
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

			//compare the timestamp and add the older to the Data class, then read the next line
			if (TurnInto.timeStamp(motsPosts[0]).after(TurnInto.timeStamp(motsComments[0]))) {
				
				Comments C = toComment(motsComments);
				rwlock.writeLock();
				Data.addDataOnly(C);
				rwlock.writeUnLock();
				// M�thode pour envoyer le commentaire dans la chaine principale
				try {
					Queue.put(C.getTime());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ligneComment = readBuff(buffComments);		
			}
			else {
				Post P = toPost(motsPosts);
				rwlock.writeLock();
				Data.addDataOnly(P);
				rwlock.writeUnLock();
				// M�thode pour envoyer le post dans la chaine principale
				try {
					Queue.put(P.getTime());
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
				rwlock.writeLock();
				Data.addDataOnly(C);
				rwlock.writeUnLock();
				// M�thode pour envoyer le commentaire dans la chaine principale
				try {
					Queue.put(C.getTime());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ligneComment = readBuff(buffComments);
			}
		} else {
			while (lignePost != null) {
				Post P = toPost(lignePost.split("\\|"));
				rwlock.writeLock();
				Data.addDataOnly(P);
				rwlock.writeUnLock();
				// M�thode pour envoyer le post dans la chaine principale
				try {
					Queue.put(P.getTime());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lignePost = readBuff(buffPosts);
			}
		}
		
		try {
			Queue.put(new Timestamp(9000, 10, 2, 0, 0, 0, 0));
			Queue.put(new Timestamp(9000, 10, 2, 0, 0, 0, 0));
			Queue.put(new Timestamp(9000, 10, 2, 0, 0, 0, 0));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// close everything
		closeFlux();
		System.out.println("Done reading");
	}
	
	public static Comments toComment(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		int repId = (mots[5].isEmpty()) ? -1 : Integer.valueOf(mots[5]);
		int postId = (mots.length == 6) ? -1 : Integer.valueOf(mots[6]);
		return new Comments(t, Integer.valueOf(mots[1]), Integer.valueOf(mots[2]), repId, postId);
	}
	
	public static Post toPost(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		return new Post(t, Integer.valueOf(mots[1]), mots[4]);
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		makeInput(path);
	}
}
