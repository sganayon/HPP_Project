package misc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Vector;

import modeles.Comments;
import modeles.Entree;
import modeles.Post;
import writer.Output;

public class HashMapVersion {
	private static HashMap<Integer, Post> posts = new HashMap<Integer, Post>();
	private static Vector<Post> postscopy = new Vector<Post>();
	private static Timestamp lastUpdate = null;

	public static HashMap<Integer, Post> getData() {
		return posts;
	}

	public static void addData(Entree o) {
		if (o instanceof Post) {
			Post p = (Post) o;
			posts.put(p.getId(), p);
			System.out.println(p.getTime() + " add post of " + p.getUser());
			for (Map.Entry<Integer, Post> entry : posts.entrySet()) {
				entry.getValue().updateScore(p.getTime());
			}
			lastUpdate = p.getTime();
		}

		else if (o instanceof Comments) {
			Comments c = (Comments) o;
			if (c.getRepId() == -1) {
				posts.get(c.getPostId()).addComment(c);
				System.out.println(c.getTime() + " add comment to " + posts.get(c.getPostId()).getId());
				posts.get(c.getPostId()).updateScore(c.getTime());
			} else {
				for (Map.Entry<Integer, Post> entry : posts.entrySet()) {
					for (Comments cmt : entry.getValue().getComments()) {
						if (cmt.getId() == c.getRepId())
							entry.getValue().addComment(c);
						System.out.println(c.getTime() + " add comment to a comment " + entry.getValue().getUser());
						break;
					}
					entry.getValue().updateScore(c.getTime());
				}
			}
			lastUpdate = c.getTime();
		}
	removeDeadPost();
	Output.checkTopChanged(getTopScore());
	}

	public static void addComment(Comments c) {
		if (c.getRepId() == -1) {
			posts.get(c.getPostId()).addComment(c);
			posts.get(c.getPostId()).updateScore(c.getTime());
			System.out.println(c.getTime() + " add comment to " + posts.get(c.getPostId()).getUser());
			
		} else {
			for (Map.Entry<Integer, Post> entry : posts.entrySet()) {
				for (Comments cmt : entry.getValue().getComments()) {
					if (cmt.getId() == c.getRepId())
						entry.getValue().addComment(c);
					System.out.println(c.getTime() + " add comment to a comment " + entry.getValue().getUser());
					break;
				}
				entry.getValue().updateScore(c.getTime());
			}
		}
		lastUpdate = c.getTime();
		removeDeadPost();
	}

	public static void addPost(Post p) {
		posts.put(p.getId(),p);
		System.out.println(p.getTime() + " add post of " + p.getUser());
		for (Map.Entry<Integer, Post> entry : posts.entrySet()) {
			entry.getValue().updateScore(p.getTime());
		}
		lastUpdate = p.getTime();
		removeDeadPost();
	}

	public static List<Post> getTopScore() {
		List<Post> top3 = new Vector<Post>(3);
		for (Map.Entry<Integer, Post> entry : posts.entrySet()) {
			postscopy.addElement(entry.getValue());
			}
		Collections.sort(postscopy);
		Collections.reverse(postscopy);
		int max = (3 > postscopy.size()) ? postscopy.size() : 3;

		top3.addAll(postscopy.subList(0, max));
		postscopy.removeAllElements();
		return top3;
	}

	public static Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public static void removeDeadPost() {
		List<Post> deadPost = postscopy.stream().filter(p -> p.getScore() == 0).collect(Collectors.toList());
		postscopy.removeAll(deadPost);
		for (Post p : postscopy)
		{
			posts.remove(p.getId());
		}
	}
}
