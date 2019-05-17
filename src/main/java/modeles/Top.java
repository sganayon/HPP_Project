package modeles;

import java.sql.Timestamp;
import java.util.List;

public class Top {
	private List<Post> posts = null;
	private Timestamp time = null;
	
	public Top(List<Post> posts, Timestamp time) {
		super();
		this.posts = posts;
		this.time = time;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public Timestamp getTime() {
		return time;
	}
	
	
}
