package modeles;

import java.sql.Timestamp;

public class Comments extends Entree {
	private Timestamp time;
	private int id;
	private int score = 10;
	private int userId;
	private int repId;
	private int postId;
	@Override
	public String toString() {
		return "Comments [time=" + time + ", id=" + id + ", score=" + score + ", userId=" + userId + "]";
	}
	public Comments(Timestamp time, int id, int userId, int repId, int PostId) {
		super();
		this.time = time;
		this.id = id;
		this.userId = userId;
		this.repId = repId;
		this.postId = PostId;
	}
	
	public Comments(Timestamp time, int id, int userId, int repId, int PostId, int score) {
		super();
		this.time = time;
		this.id = id;
		this.userId = userId;
		this.repId = repId;
		this.postId = PostId;
		this.score = score;
	}
	public Timestamp getTime() {
		return time;
	}
	public int getId() {
		return id;
	}
	public int getScore() {
		return score;
	}
	public int getUserId() {
		return userId;
	}
	public int getRepId() {
		return repId;
	}
	public int getPostId() {
		return postId;
	}
	public void updateScore(Timestamp t) {
		//si le score est null pas besoin de le calculer de nouveau
		if(score ==0) {return;}
		
		//obtient la difference en miliseconde des deux date et divise pour avoir la difference en jours
		int dayElapsed = (int) ((t.getTime() - time.getTime())/(24 * 60 * 60 * 1000));
		score = 10-dayElapsed;
		
		if(score <0) {
			score = 0;
		}
	}
	
	@Override
	public Comments clone() {
		return new Comments((Timestamp)this.time.clone(),this.id, this.userId,this.repId,this.postId, this.score);
	}
}
