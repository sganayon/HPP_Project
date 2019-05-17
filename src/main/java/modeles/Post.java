package modeles;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Post extends Entree implements Comparable<Post>{
	private Timestamp time;
	private long id;
	private String user;
	private int score =10;
	private List<Comments> comments = new ArrayList<Comments>();
	
	
	public Post(Timestamp time, long id, String user) {
		super();
		this.time = time;
		this.id = id;
		this.user = user;
	}
	
	public Post(Timestamp time, long id, String user, int score, List<Comments> comments) {
		super();
		this.time = time;
		this.id = id;
		this.user = user;
		this.score = score;
		this.comments = comments;
	}
	
	@Override
	public String toString() {
		return "Post [time=" + time + ", id=" + id + ", user=" + user + ", score=" + score + "]";
	}
	public Timestamp getTime() {
		return time;
	}
	public long getId() {
		return id;
	}
	public String getUser() {
		return user;
	}
	public int getScore() {
		return score;
	}
	public int getNbCommenteers() {
		return (int) comments.stream().mapToLong(c->c.getUserId()).distinct().count();
	}
	
	public List<Comments> getComments(){
		return comments;
	}
	
	//ajoute un nouveau commentaire
	public void addComment(Comments c) {
		this.comments.add(c);
	}
	
	//supprime le commentaire tombé à 0
	public void removeDeadComment() {
		//filtre tout les commentaire ayant un score null
		List<Comments> toRemove = comments.stream().filter(c-> c.getScore()==0).collect(Collectors.toList());
		comments.removeAll(toRemove);
	}
	
	public void updateScore(Timestamp t) {
		//si le score est null pas besoin de le calculer de nouveau
		if(score ==0) {return;}
		
		//calcule le score des commentaires
		int subScore = 0;
		for (Comments c:comments) {
			c.updateScore(t);
		}

		
		//Supprimme les commentaires ancient et calcule le score
		removeDeadComment();
		subScore = comments.stream().mapToInt(c->c.getScore()).sum();
		
		//obtient la difference en miliseconde des deux date et divise pour avoir la difference en jours
		int dayElapsed = (int) ((t.getTime() - time.getTime())/(24 * 60 * 60 * 1000));
		score = 10-dayElapsed + subScore;
		if(score <0) {
			score = 0;
		}
	}
	@Override
	public int compareTo(Post o) {
		if(this.score <= o.score) {
			if(this.score < o.score) {
				return -1;
			}else {
				if(this.time.before(o.time)) {
					return -1;
				}else if(this.time.after(o.time)){
					return 1;
				}else {
					if(!this.comments.isEmpty() && !o.comments.isEmpty()) {
						if(this.comments.get(this.comments.size()-1).getTime().before(o.comments.get(o.comments.size()-1).getTime())) {
							return -1;
						}else {
							return 1;
						}
					}else {
						return 1;
					}
					
				}
			}
		}else {
			return 1;
		}
	}
	
	@Override
	public Post clone() {
		List<Comments> lst = new ArrayList<Comments>();
		for(Comments c : comments) {
			lst.add(c.clone());
		}
		return new Post((Timestamp)time.clone(),id,user, score, lst);
	}
	
}
