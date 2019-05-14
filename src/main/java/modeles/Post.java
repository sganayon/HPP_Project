package modeles;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Post {
	private Timestamp time;
	private int id;
	private String user;
	private int score;
	private List<Comments> comments = new ArrayList<Comments>();
	
	
	public Post(Timestamp time, int id, String user, int score) {
		super();
		this.time = time;
		this.id = id;
		this.user = user;
		this.score = score;
	}
	@Override
	public String toString() {
		return "Post [time=" + time + ", id=" + id + ", user=" + user + ", score=" + score + "]";
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	//ajoute un nouveau commentaire
	public void addComment(Comments c) {
		this.comments.add(c);
	}
	
	//supprime le commentaire tombé à 0
	public void removeDeadComment(int indexe) {
		this.comments.remove(indexe);
	}
}
