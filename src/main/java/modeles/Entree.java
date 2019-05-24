package modeles;

import java.sql.Timestamp;

public class Entree {
	protected long id;
	protected Timestamp time;
	
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
