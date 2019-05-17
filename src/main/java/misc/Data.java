package misc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Vector;

import modeles.Comments;
import modeles.Entree;
import modeles.Post;
import writer.Output;

public class Data {
	private static Vector<Post> posts = new Vector<Post>();
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
		removeDeadPost();
		Output.checkTopChanged(getTopScore());
	}
	
	public synchronized static void addComment(Comments c) {
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
						System.out.println(c.getTime()+" add comment to a comment to "+p.getUser());
						break;
					}
				}
				p.updateScore(c.getTime());
			}
		}
		removeDeadPost();
	}
	
	public synchronized static void addPost(Post p) {
		posts.add(p);
		System.out.println(p.getTime()+" add post of "+p.getUser());
		posts.forEach(po->po.updateScore(p.getTime()));
		removeDeadPost();
	}
	
	public synchronized static List<Post> getTopScore() {
		List<Post> top3 = new ArrayList<Post>(3);
		List<Post> top1 = new ArrayList<Post>(3);
		Collections.sort(posts);
		Collections.reverse(posts);
		int max = (3>posts.size())?posts.size():3;
		top1 = posts.subList(0, max);
		
		for(Post p : top1) {
			top3.add(p.clone());
		}

		System.out.println(top3);
		return top3;
	}

	public synchronized static Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public static void removeDeadPost() {
		List<Post> deadPost = posts.stream().filter(p->p.getScore()==0).collect(Collectors.toList());
		posts.removeAll(deadPost);
	}
}
