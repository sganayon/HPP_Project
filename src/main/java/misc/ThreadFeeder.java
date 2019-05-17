package misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import modeles.Comments;
import modeles.Post;

public class ThreadFeeder implements Runnable {

	private BlockingQueue<Post> postQueue = null;
	private BlockingQueue<Comments> commentsQueue = null;
	private BlockingQueue<List<Post>> outQueue = null;
	
	
	public ThreadFeeder(BlockingQueue<Post> postQueue, BlockingQueue<Comments> commentsQueue,BlockingQueue<List<Post>> outQueue) {
		super();
		this.postQueue = postQueue;
		this.commentsQueue = commentsQueue;
		this.outQueue = outQueue;
	}


	@Override
	public void run() {
		
		Post postTmp = null;
		try {
			postTmp = postQueue.take();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Comments comTmp = null;
		try {
			comTmp = commentsQueue.take();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		while(postTmp.getId() != -1 && comTmp.getId() != -1) {
			
			if(postTmp.getTime().after(comTmp.getTime())) {
				Data.addComment(comTmp);
				comTmp = null;
			}else {
				Data.addPost(postTmp);
				postTmp = null;
			}
			
			try {
				outQueue.put(Data.getTopScore());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(comTmp == null) {
				try {
					comTmp = commentsQueue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				try {
					postTmp = postQueue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		if(postTmp.getId() == -1) {
			while(comTmp.getId() != -1) {
				Data.addComment(comTmp);
				try {
					comTmp = commentsQueue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outQueue.put(Data.getTopScore());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			while(postTmp.getId() != -1) {
				Data.addPost(postTmp);
				try {
					postTmp = postQueue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outQueue.put(Data.getTopScore());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		try {
			List<Post> poisounous = new ArrayList<Post>();
			poisounous.add(null);
			outQueue.put(poisounous);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done feeding");
	}

}