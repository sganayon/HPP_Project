package misc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modeles.Comments;
import modeles.Entree;
import modeles.Post;
import writer.Output;

public class Data {
	
	private static List<Post> posts = new ArrayList<Post>();
	private static Timestamp lastUpdate =null;
	
	public static List<Post> getData(){
		return posts;
	}
	
	public static void addData(Entree o) {
		if(o instanceof Post) {
			Post p = (Post) o;
			posts.add(p);
			System.out.println(p.getTime()+" add post of "+p.getUser());
			posts.forEach(po->po.updateScore(p.getTime()));
			lastUpdate = p.getTime();
		}
		
		else if(o instanceof Comments){
			Comments c = (Comments) o;
			if(c.getRepId() == -1) {
				for(Post p : posts) {
					if(p.getId() == c.getPostId()) {
						p.addComment(c);
						System.out.println(c.getTime()+" add comment to "+p.getUser());
					}
					p.updateScore(c.getTime());
				}
			}else {
				for(Post p : posts) {
					for(Comments cmt: p.getComments()) {
						if(cmt.getId() == c.getRepId()) {
							p.addComment(c);
							System.out.println(c.getTime()+" add comment to a comment "+p.getUser());
							break;
						}
					}
					p.updateScore(c.getTime());
				}
			}
			lastUpdate = c.getTime();
		}
		Output.checkTopChanged(getTopScore());
	}
	
	public static List<Post> getTopScore() {
		List<Post> top3 = new ArrayList<Post>(3);
		
		Collections.sort(posts);
		Collections.reverse(posts);
		int max = (3>posts.size())?posts.size():3;
		
		top3.addAll(posts.subList(0, max));
		return top3;
	}

	public static Timestamp getLastUpdate() {
		return lastUpdate;
	}
}
