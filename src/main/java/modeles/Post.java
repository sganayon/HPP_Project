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
	private int nbComm = 0;
	private Timestamp lastUpdate;
	
	
	public Post(Timestamp time, long id, String user) {
		super(id,time);
		this.time = time;
		this.lastUpdate = time;
		this.id = id;
		this.user = user;
	}
	
	public Post(Timestamp time, long id, String user, int score) {
		super(id,time);
		this.time = time;
		this.lastUpdate = time;
		this.id = id;
		this.user = user;
		this.score = score;
	}
	
	public Post(Timestamp time, long id, String user, int score,int nbComm) {
		super(id,time);
		this.time = time;
		this.lastUpdate = time;
		this.id = id;
		this.user = user;
		this.score = score;
		this.nbComm = nbComm;
	}
	
	@Override
	public String toString() {
		return "Post [time=" + time + ", id=" + id + ", user=" + user + ", score=" + score + "]";
	}
	public String getUser() {
		return user;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int s) {
		this.score = s;
	}
	public int getNbComm() {
		return nbComm;
	}
	public void setNbComm(int n) {
		this.nbComm = n;
	}
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public int getScoreAt(Timestamp t) {
		
		if(time.after(t)) {
			return 0;
		}
		
		int scoreAtT = 0;
		//obtient la difference en miliseconde des deux date et divise pour avoir la difference en jours
		int dayElapsed = (int) ((t.getTime() - time.getTime())/(24 * 60 * 60 * 1000));
		if(dayElapsed > 10) {
			dayElapsed = 10;
		}
		scoreAtT = 10-dayElapsed;
		
		if(scoreAtT <0) {
			return 0;
		}
		return scoreAtT;
	}
	
	//pas besoin de comparer le temps du dernier commentaire car il sera l'event en cours, Data3 remplace si le score est >= 
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
					return 0;
				}
			}
		}else {
			return 1;
		}
	}
	
	@Override
	public Post clone() {
		return new Post((Timestamp)time.clone(),id,user, score,nbComm);
	}
	
}
