package modeles;

import java.sql.Timestamp;

public class Comments {
	private Timestamp time;
	private int id;
	private int score = 10;
	@Override
	public String toString() {
		return "Comments [time=" + time + ", id=" + id + ", score=" + score + "]";
	}
	public Comments(Timestamp time, int id) {
		super();
		this.time = time;
		this.id = id;
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
}
