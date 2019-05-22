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
import modeles.Top;
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
			posts.forEach(po->po.updateScore(p.getTime()));
			lastUpdate = p.getTime();
		}
		
		else if(o instanceof Comments){
			Comments c = (Comments) o;
			if(c.getRepId() == -1) {
				for(Post p : posts) {
					if(p.getId() == c.getPostId()) {
						p.addComment(c);
					}
					p.updateScore(c.getTime());
				}
			}else {
				for(Post p : posts) {
					for(Comments cmt: p.getComments()) {
						if(cmt.getId() == c.getRepId()) {
							p.addComment(c);
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
				}
				p.updateScore(c.getTime());
			}
		}else {
			for(Post p : posts) {
				for(Comments cmt: p.getComments()) {
					if(cmt.getId() == c.getRepId()) {
						p.addComment(c);
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
		posts.forEach(po->po.updateScore(p.getTime()));
		removeDeadPost();
	}
	
	public synchronized static List<Post> getTopScore() {
		List<Post> top3 = new ArrayList<Post>(3);
		List<Post> top1 = new ArrayList<Post>(3);
		Collections.sort(posts);
		int min = (posts.size()<3)?0:posts.size()-3;
		top1 = posts.subList(min, posts.size());
		Collections.reverse(top1);
		for(Post p : top1) {
			top3.add(p.clone());
		}
		return top3;
	}

	public synchronized static Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public static void removeDeadPost() {
		List<Post> deadPost = posts.stream().filter(p->p.getScore()==0).collect(Collectors.toList());
		posts.removeAll(deadPost);
	}

	public static void addDataOnly(Entree e) {
		if(e instanceof Post) {
			Post p = (Post) e;
			posts.add(p);
		}
		
		else if(e instanceof Comments){
			Comments c = (Comments) e;
			if(c.getRepId() == -1) {
				for(Post p : posts) {
					if(p.getId() == c.getPostId()) {
						p.addComment(c);
						return;
					}
				}
			}else {
				for(Post p : posts) {
					for(Comments cmt: p.getComments()) {
						if(cmt.getId() == c.getRepId()) {
							p.addComment(c);
							return;
						}
					}
				}
			}
		}
	}

	public static List<Post> getTop3at(Timestamp t){
		List<Post> best3 = new ArrayList<Post>(3);
		
		for(int i=0;i<3;i++) {
			best3.add(null);
		}
		for(Post p : posts) {
			if(!p.getTime().after(t)) {
				int scoreOfP = p.getScoreAt(t);
				
				if(scoreOfP != 0) {
					if(best3.get(0) == null || scoreOfP > best3.get(0).getScore() || (scoreOfP == best3.get(0).getScore() && p.getTime().after(best3.get(0).getTime()))) {
						
							best3.set(2,best3.get(1));
							best3.set(1,best3.get(0));
							Post p0 =p.clone();
							p0.setScore(scoreOfP);
							best3.set(0,p0);
						
					}else if(best3.get(1) == null || scoreOfP > best3.get(1).getScore() || (scoreOfP == best3.get(1).getScore() && p.getTime().after(best3.get(1).getTime()))) {
						
							best3.set(2,best3.get(1));
							Post p0 =p.clone();
							p0.setScore(scoreOfP);
							best3.set(1,p0);
						
					}else if (best3.get(2) == null || scoreOfP > best3.get(2).getScore() || (scoreOfP == best3.get(2).getScore() && p.getTime().after(best3.get(2).getTime()))) {
						
							Post p0 =p.clone();
							p0.setScore(scoreOfP);
							best3.set(2,p0);
						
					}
				}
			}
		}
		return best3;
	}

	public static void clearData() {
		posts.clear();
		lastUpdate=null;
	}
}
