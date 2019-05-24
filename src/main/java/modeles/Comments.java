package modeles;

import java.sql.Timestamp;

public class Comments extends Entree {
	private int score = 10;
	private long userId;
	private long repId;
	private long postId;
	@Override
	public String toString() {
		return "Comments [time=" + time + ", id=" + id + ", score=" + score + ", userId=" + userId + "]";
	}
	public Comments(Timestamp time, long id, long userId, long repId, long PostId) {
		super(id,time);
		this.userId = userId;
		this.repId = repId;
		this.postId = PostId;
	}
	
	public Comments(Timestamp time, long id, long userId, long repId, long PostId, int score) {
		super(id,time);
		this.userId = userId;
		this.repId = repId;
		this.postId = PostId;
		this.score = score;
	}
	public int getScore() {
		return score;
	}
	public long getUserId() {
		return userId;
	}
	public long getRepId() {
		return repId;
	}
	public long getPostId() {
		return postId;
	}

	public int getScoreAt(Timestamp t) {
		if(time.after(t)) {
			return 0;
		}
		
		int scoreTmp = (int) ((t.getTime() - time.getTime())/(24 * 60 * 60 * 1000));
		
		int scoreAtT = 10-scoreTmp;
		if(scoreAtT < 0) {
			return 0;
		}
		return scoreAtT;
	}
	
	@Override
	public Comments clone() {
		return new Comments((Timestamp)this.time.clone(),this.id, this.userId,this.repId,this.postId, this.score);
	}
}
