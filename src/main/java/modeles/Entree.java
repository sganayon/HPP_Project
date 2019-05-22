package modeles;

import java.sql.Timestamp;

public class Entree {
	private long id;
	private Timestamp time;
	
	public Entree(long id, Timestamp time) {
		super();
		this.id = id;
		this.time = time;
	}
	public long getId() {
		return id;
	}
	public Timestamp getTime() {
		return time;
	}

	
}
