package misc;

import java.util.ArrayList;
import java.util.List;

import modeles.Comments;
import modeles.Entree;
import modeles.Post;

public class Data {
	
	private static List<Post> posts = new ArrayList<Post>();
	
	public static List<Post> getData(){
		return posts;
	}
	
	public static void addData(Entree o) {
		if(o instanceof Post) {
			Post p = (Post) o;
			posts.add(p);
			posts.forEach(po->po.updateScore(p.getTime()));
		}
		
		else if(o instanceof Comments){
			Comments c = (Comments) o;
			for(Post p : posts) {
				if(p.getId() == c.getPostId()) {
					p.addComment(c);
				}
				p.updateScore(c.getTime());
			}
		}
	}
	
	
}
