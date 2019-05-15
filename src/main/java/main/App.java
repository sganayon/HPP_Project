package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Reader.Reader;

public class App {

	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		Reader R = new Reader();
		try {
			R.makeInput();
		} catch (IOException e) {

		
		try{
			InputStream flux=new FileInputStream(System.getProperty("user.home") + "\\Local Settings\\Application Data" + "/HPP_Project/comments.dat"); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;			
			ligne=buff.readLine();
			System.out.println(ligne);
			String[] mots = ligne.split("\\|");
			for (int i=0;i<mots.length;i++) {
				Reader.toComment(mots);
			}
			buff.close();

			e.printStackTrace();
		}
		

			}					
			catch (Exception e){
				e.printStackTrace();
			
			}
		long end = System.currentTimeMillis()-start;
		System.out.println(end);
	}

}
