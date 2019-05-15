package Reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Timestamp;
import modeles.Comments;
import misc.Data;
import misc.TurnInto;
import modeles.Post;

public class Reader {

	
	// Lit une ligne d'un fichier
	public static String[] read(BufferedReader buff) {
		try {

			String ligne;
			// Lit la prochaine ligne du document
			ligne = buff.readLine();
			// System.out.println(ligne);

			// S�pare les �l�ments de l'entr�e
			String[] mots = ligne.split("\\|");
			/*
			 * for (int i=0;i<mots.length;i++) { System.out.println(mots[i]); }
			 */

			return mots;
			
			// Retoune null si il n'y a plus de lignes
		} catch (Exception e) {
			// System.out.println(e.toString());
			return null;
		}

	}
	
	// Convertit une liste de String en Comment
	public static Comments toComment(String[] mots) {
		//R�cup�re et convertit le premier �l�ment de la liste en Timestamp
		Timestamp t = TurnInto.timeStamp(mots[0]);
		//R�cup�re et convertit le quatri�me �l�ment de la liste en l'entier de l'id du commentaire comment�
		int repId = (mots[5].isEmpty()) ? -1 : Integer.valueOf(mots[5]);
		//R�cup�re et convertit le cinqui�me �l�ment de la liste en l'entier de l'id du post comment�
		int postId = (mots[6].isEmpty()) ? -1 : Integer.valueOf(mots[6]);
		
		return new Comments(t, Integer.valueOf(mots[1]), Integer.valueOf(mots[2]), repId, postId);
	}

	public static void makeInput() {
		
		//R�cup�re le chemin du fichier de posts
		InputStream fluxPosts = null;
		try {
			fluxPosts = new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data"
					+ "/HPP_Project/Tests/Q1Basic2/posts.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader lecturePosts = new InputStreamReader(fluxPosts);
		BufferedReader buffPosts = new BufferedReader(lecturePosts);
		
		//R�cup�re le chemin du fichier de Commentaires
		InputStream fluxComments = null;
		try {
			fluxComments = new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data"
					+ "/HPP_Project/Tests/Q1Basic2/comments.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader lectureComments = new InputStreamReader(fluxComments);
		BufferedReader buffComments = new BufferedReader(lectureComments);
		
		//Cr�e les liste de String contenant les lignes de posts et de commentaires
		String[] motsPosts = read(buffPosts);
		String[] motsComments = read(buffComments);
		
		//Boucle lisant les fichier de posts et de commentaire jusqu'� la fin de l'un des deux
		while (motsPosts != null && motsComments != null) {
			// R�cup�re la ligne dont l'entr�e est la plus r�cente
			if (TurnInto.timeStamp(motsPosts[0]).before(TurnInto.timeStamp(motsComments[0]))) {
				Post P = toPost(motsPosts);
				// M�thode pour envoyer le post dans la chaine principale
				Data.addData(P);
				//Lecture de la prochaine ligne de post
				motsPosts = read(buffPosts);
			} else {
				Comments C = toComment(motsComments);
				// M�thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				//Lecture de la prochaine liste de Commentaires
				motsComments = read(buffComments);
			}

			// }
		}
		
		// Finit de lire le fichier restant
		if (motsPosts == null) {
			while (motsComments != null) {
				Comments C = toComment(motsComments);
				// M�thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				motsComments = read(buffComments);
			}
		} else {
			while (motsPosts != null) {
				Post P = toPost(motsPosts);
				// M�thode pour envoyer le post dans la chaine principale
				Data.addData(P);
				motsPosts = read(buffPosts);
			}
		}
		// Fermeture des fichiers
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
	
	// Convertit une liste de Strings en Post
	public static Post toPost(String[] mots) {
		Timestamp t = TurnInto.timeStamp(mots[0]);
		return new Post(t, Integer.valueOf(mots[1]), mots[4]);
	}
	
	//Meme fonction avec les emplacements de fichiers en parametre
	public static void makeInput(String Post, String Comment) {
		InputStream fluxPosts = null;
		try {
			fluxPosts = new FileInputStream(Post);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader lecturePosts = new InputStreamReader(fluxPosts);
		BufferedReader buffPosts = new BufferedReader(lecturePosts);

		InputStream fluxComments = null;
		try {
			fluxComments = new FileInputStream(Comment);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader lectureComments = new InputStreamReader(fluxComments);
		BufferedReader buffComments = new BufferedReader(lectureComments);
		String[] motsPosts = read(buffPosts);
		String[] motsComments = read(buffComments);
		do {
			if (TurnInto.timeStamp(motsPosts[0]).before(TurnInto.timeStamp(motsComments[0]))) {
				Post P = toPost(motsPosts);
				// M�thode pour envoyer le post dans la chaine principale
				Data.addData(P);
				motsPosts = read(buffPosts);
			} else {
				Comments C = toComment(motsComments);
				// M�thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				motsComments = read(buffComments);
			}

			// }
		} while (motsPosts != null && motsComments != null);

		if (motsPosts == null) {
			while (motsComments != null) {
				Comments C = toComment(motsComments);
				// M�thode pour envoyer le commentaire dans la chaine principale
				Data.addData(C);
				motsComments = read(buffComments);
			}
		} else {
			while (motsPosts != null) {
				Post P = toPost(motsPosts);
				// M�thode pour envoyer le post dans la chaine principale
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
