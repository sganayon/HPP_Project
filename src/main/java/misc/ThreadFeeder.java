package misc;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import modeles.Comments;
import modeles.Post;

public class ThreadFeeder implements Runnable {

	private BlockingQueue<Post> postQueue = null;
	private BlockingQueue<Comments> commentsQueue = null;
	private BlockingQueue<Boolean> outQueue = null;
	
	
	public ThreadFeeder(BlockingQueue<Post> postQueue, BlockingQueue<Comments> commentsQueue,BlockingQueue<Boolean> outQueue) {
		super();
		this.postQueue = postQueue;
		this.commentsQueue = commentsQueue;
		this.outQueue = outQueue;
	}


	@Override
	public void run() {
		Post postTmp = null;
		Comments comTmp = null;
		while((postTmp = postQueue.peek()).getId() != -1 && (comTmp = commentsQueue.peek()).getId() != -1) {
			if(postTmp !=null && comTmp != null) {
				if(postTmp.getTime().after(comTmp.getTime())) {
					Data.addComment(comTmp);
				}else {
					Data.addPost(postTmp);
				}
			}
			try {
				outQueue.put(true);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			outQueue.put(false);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}