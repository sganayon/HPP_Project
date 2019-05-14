package modeles;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Top {

	private Timestamp TS;
	private Integer postID;
	private String user;
	private Integer score;
	private Integer NbCommenters;
	
	Top(Timestamp T, int pID, String uID, int s, int c){
		TS=T;
		postID=pID;
		user=uID;
		score=s;
		NbCommenters=c;
	}
	
	Top(Timestamp T, int pID, String uID){
		TS=T;
		postID=pID;
		user=uID;
		score=10;
		NbCommenters=0;
	}
	Top(){
		score=0;
	}
	public Timestamp getTS() {
		return TS;
	}
	public void setTS(Timestamp tS) {
		TS = tS;
	}
	public Integer getPostID() {
		return postID;
	}
	public void setPostID(int postID) {
		this.postID = postID;
	}
	public String getUserID() {
		return user;
	}
	public void setUserID(String userID) {
		this.user = userID;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Integer getNbCommenters() {
		return NbCommenters;
	}
	public void setNbCommenters(int nbCommenters) {
		NbCommenters = nbCommenters;
	}
	
	public static void createTop(ArrayList<Post> posts,ArrayList<Top> top) {
		Top best=new Top();
		Top best2=new Top();
		Top best3=new Top();
		for(Post p : posts)
		{
			if (p.getScore()>best.getScore())
			{
			best3=best2;
			best2=best;
			best.setPostID(p.getId());
			best.setScore(p.getScore());
			best.setTS(p.getTime());
			best.setUserID(p.getUser());
			// Mettre la méthode d'axel best.setNbCommenters(p.getnb);
			
			}
			else
			{
				if (p.getScore()>best2.getScore())
				{
					best3=best2;
					best2.setScore(p.getScore());
					best2.setPostID(p.getId());
					best2.setTS(p.getTime());
					best2.setUserID(p.getUser());
					//best2.setNbCommenters(p.getnb);
				}
				else
				{
					if (p.getScore()>best3.getScore())
						{best3.setScore(p.getScore());
						best3.setPostID(p.getId());
						best3.setTS(p.getTime());
						best3.setUserID(p.getUser());
						//best3.setNbCommenters(p.getnb);
						}
				}
			}
		}
		
		top.add(best);
		top.add(best2);
		top.add(best3);
		
	}
	
	
	public void output(ArrayList<Top> top) 
	{
		List<String> output = new ArrayList<String>();
		Timestamp t;
		if (top.get(0).getTS().after(top.get(1).getTS()))
		{
			if (top.get(0).getTS().after(top.get(2).getTS()))
				t=top.get(0).getTS();
			else
				t=top.get(0).getTS();
		}
		else
		{
			if (top.get(1).getTS().after(top.get(2).getTS()))
				t=top.get(1).getTS();
			else
				t=top.get(2).getTS();
		}
		output.add(t.toString()+",");
		for (Top p : top)
		{
			output.add(p.getPostID().toString()+",");
			output.add(p.getUserID()+",");
			output.add(p.getScore().toString()+",");
			output.add(p.getNbCommenters().toString()+",");	
		}
		
		try {
			Path fichier = Paths.get("output.txt");
			Path fichierglobal = Paths.get("outputglobal.txt");
			Files.write(fichier, output, Charset.forName("UTF-8"));
			Files.write(fichierglobal, output, Charset.forName("UTF-8"), StandardOpenOption.APPEND);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
