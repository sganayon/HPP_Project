package misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import modeles.Comments;
import modeles.Post;
import modeles.Top;

public class ThreadFeeder implements Runnable {

	private BlockingQueue<Post> postQueue = null;
	private BlockingQueue<Comments> commentsQueue = null;
	private BlockingQueue<Top> outQueue = null;
	
	
	public ThreadFeeder(BlockingQueue<Post> postQueue, BlockingQueue<Comments> commentsQueue,BlockingQueue<Top> outQueue) {
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
			
			Top t = null;
			if(postTmp.getTime().after(comTmp.getTime())) {
				Data.addComment(comTmp);
				t = new Top(Data.getTopScore(), comTmp.getTime());
				comTmp = null;
			}else {
				Data.addPost(postTmp);
				t = new Top(Data.getTopScore(), postTmp.getTime());
				postTmp = null;
			}
			
			
			try {
				outQueue.put(t);
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
				Top t = new Top(Data.getTopScore(), comTmp.getTime());
				try {
					comTmp = commentsQueue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outQueue.put(t);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			while(postTmp.getId() != -1) {
				Data.addPost(postTmp);
				Top t = new Top(Data.getTopScore(), postTmp.getTime());
				try {
					postTmp = postQueue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outQueue.put(t);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		try {
			List<Post> poisounous = new ArrayList<Post>();
			poisounous.add(null);
			Top t = new Top(poisounous, null);
			outQueue.put(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done feeding");
	}

}