package misc;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import modeles.Entree;
import modeles.Post;
import modeles.Top;

public class ThreadComputing3 implements Runnable{
	private BlockingQueue<Entree> events = null;
	private BlockingQueue<Top> tops = null;
	
	public ThreadComputing3(BlockingQueue<Entree> events, BlockingQueue<Top> tops) {
		super();
		this.events = events;
		this.tops = tops;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Entree e = null;
		try {
			e = events.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(e.getId() != -1) {
			List<Post> lst = Data3.getTop3AtNotMultiThreaded(e);
			Top top = new Top(lst,e.getTime());
			try {
				tops.put(top);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				e = events.take();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try {
			tops.put(new Top(null,null));
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("done computing");
	}
}
