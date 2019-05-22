package misc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import modeles.Comments;
import modeles.Entree;
import modeles.Post;
import modeles.Top;

public class Data2 {
	//Id post - post
	private static Map<Long,Post> posts = new HashMap<Long,Post>(500000);
	//Id post - list de comments
	private static Map<Long,List<Comments>> comments = new HashMap<Long,List<Comments>>(100000);
	//Id comments - Id posts
	private static Map<Long,Long> indexCP = new HashMap<Long,Long>(800000);
	
	private static List<Post> bestPosts = new ArrayList<Post>(3);
	private static RWLock lock = new RWLock();
	private static Vector<Long> oldData = new Vector<Long>();
	
	
	public static void addPost(Post p) {
		posts.put(p.getId(), p);
	}
	
	public static long addComment(Comments c) {
		//comment to a post
		if(c.getPostId() != -1) {
			List<Comments> lst = comments.get(c.getPostId());
			lock.writeLock();
			if(lst != null) {
				lst.add(c);
			}else {
				lst = new ArrayList<Comments>();
				lst.add(c);
				comments.put(c.getPostId(),lst);
			}
			lock.writeUnLock();
			indexCP.put(c.getId(),c.getPostId());
			return c.getPostId();
		}else { //comment to a comment 
			List<Comments> lst = comments.get(indexCP.get(c.getRepId()));
			lock.writeLock();
			lst.add(c);
			lock.writeUnLock();
			indexCP.put(c.getId(),lst.get(0).getPostId());
			return lst.get(0).getPostId();
		}
		
	}

	public static int getScoreOfPostAt(Post p, Timestamp t) {
		int scoreComm = 0;
		List<Comments> lst = comments.get(p.getId());
		if(lst != null) {
			lock.readLock();
			for(Comments c :lst) {
				scoreComm+= c.getScoreAt(t);
			}
			lock.readUnLock();
		}
		
		int scoreTot = p.getScoreAt(t)+scoreComm;
		
		return scoreTot;
	}
	
	public static List<Post> getTop3At(Timestamp t) {
		List<Post> best3 = new ArrayList<Post>(3);
		
		removeOldData();
		
		for(int i=0;i<3;i++) {
			best3.add(null);
		}
		
		lock.readLock();
		for(Entry<Long, Post> entry : posts.entrySet()) {
			Post p = entry.getValue();
			if(!p.getTime().after(t)) {
				int scoreOfP = getScoreOfPostAt(p,t);
				if(scoreOfP != 0) {
					if(best3.get(0) == null || scoreOfP > best3.get(0).getScore() || (scoreOfP == best3.get(0).getScore() && p.getTime().after(best3.get(0).getTime()))) {
						
						best3.set(2,best3.get(1));
						best3.set(1,best3.get(0));
						Post p0 =p.clone();
						p0.setScore(scoreOfP);
						p0.setNbComm(getNbCommofPostAt(p,t));
						best3.set(0,p0);
					
					}else if(best3.get(1) == null || scoreOfP > best3.get(1).getScore() || (scoreOfP == best3.get(1).getScore() && p.getTime().after(best3.get(1).getTime()))) {
					
						best3.set(2,best3.get(1));
						Post p0 =p.clone();
						p0.setScore(scoreOfP);
						p0.setNbComm(getNbCommofPostAt(p,t));
						best3.set(1,p0);
					
					}else if (best3.get(2) == null || scoreOfP > best3.get(2).getScore() || (scoreOfP == best3.get(2).getScore() && p.getTime().after(best3.get(2).getTime()))) {
					
						Post p0 =p.clone();
						p0.setScore(scoreOfP);
						p0.setNbComm(getNbCommofPostAt(p,t));
						best3.set(2,p0);
					
					}
				}else {
					oldData.add(p.getId());
				}
			}
		}
		lock.readUnLock();
		return best3;
	}

	public static List<Post> getTop3AtNotMultiThreaded(Entree e){
		List<Post> lst = new ArrayList<Post>(3);
		
		Post p = posts.get(e.getId());
		if(p == null) {
			for(int i=0; i<3;i++) {
				if(bestPosts.get(i) == null) {
					lst.add(null);
				}else {
					lst.add(bestPosts.get(i).clone());
				}
			}
			return lst;
		}
		int scoreOfP = getScoreOfPostAt(p, e.getTime());
		boolean isAlreadyIn = false;
		for(int i=0;i<3;i++) {
			if(bestPosts.get(i) != null) {
				bestPosts.get(i).setScore(getScoreOfPostAt(bestPosts.get(i),e.getTime()));
				bestPosts.get(i).setNbComm(getNbCommofPostAt(bestPosts.get(i),e.getTime()));
				if(bestPosts.get(i).getId() == p.getId()) {
					isAlreadyIn = true;
				}
			}
		}
		
		if(!isAlreadyIn) {
			if(bestPosts.get(0) == null || scoreOfP >= bestPosts.get(0).getScore()) {
				bestPosts.set(2,bestPosts.get(1));
				bestPosts.set(1,bestPosts.get(0));
				Post p0 =p.clone();
				p0.setScore(scoreOfP);
				p0.setNbComm(getNbCommofPostAt(p,e.getTime()));
				bestPosts.set(0,p0);
				
			}else if(bestPosts.get(1) == null || scoreOfP >= bestPosts.get(1).getScore()) {
				bestPosts.set(2,bestPosts.get(1));
				Post p0 =p.clone();
				p0.setScore(scoreOfP);
				p0.setNbComm(getNbCommofPostAt(p,e.getTime()));
				bestPosts.set(1,p0);
				
			}else if(bestPosts.get(2) == null || scoreOfP >= bestPosts.get(2).getScore()) {
				Post p0 =p.clone();
				p0.setScore(scoreOfP);
				p0.setNbComm(getNbCommofPostAt(p,e.getTime()));
				bestPosts.set(2,p0);
				
			}
		}
		
		for(int i=0; i<3;i++) {
			if(bestPosts.get(i) == null) {
				bestPosts.set(i,new Post(null,-1,"uer",-i-1,new ArrayList<Comments>()));
			}
		}
		Collections.sort(bestPosts);
		Collections.reverse(bestPosts);
		for(int i=0; i<3;i++) {
			if(bestPosts.get(i).getId() == -1) {
				bestPosts.set(i,null);
				lst.add(null);
			}else {
				lst.add(bestPosts.get(i).clone());
			}
		}
		return lst;
	}
	
	public static void removeOldData() {
		if(oldData.size() > 10) {
			lock.writeLock();
			for(Long id : oldData) {
				posts.remove(id);
				comments.remove(id);
			}
			oldData.clear();
			lock.writeUnLock();
			System.out.println("clearing 10 tops");
		}
	}
	
	public static int getNbCommofPostAt(Post p, Timestamp t) {
		List<Comments> lst = comments.get(p.getId());
		if(lst != null){
			lock.readLock();
			int nbComm = (int) lst.stream().filter(c->!c.getTime().after(t)).mapToLong(c->c.getUserId()).distinct().count();
			lock.readUnLock();
			return nbComm;
		}else {
			return 0;
		}
		
	}

	public static void clearData() {
		posts.clear();
		comments.clear();
		indexCP.clear();
		oldData.clear();
		bestPosts.clear();
		bestPosts.add(null);
		bestPosts.add(null);
		bestPosts.add(null);
		
	}
}
