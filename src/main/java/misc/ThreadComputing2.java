package misc;

import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.Lock;

import modeles.Entree;
import modeles.Post;
import modeles.Top;

public class ThreadComputing2 implements Runnable{
	private Vector<Timestamp> times = null;
	private Vector<Top> tops = null;
	private Lock lock=null;
	private boolean isOver = false;
	
	public ThreadComputing2(Vector<Timestamp> times, Lock lock, Vector<Top> tops) {
		super();
		this.times = times;
		this.lock = lock;
		this.tops = tops;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Timestamp t=null;
		lock.lock();
		isOver = times.isEmpty();
		if(!isOver) {
			t = times.remove(0);
		}
		lock.unlock();
		
		while(!isOver) {
			List<Post> lst = Data2.getTop3At(t);
			System.out.println("top compute for "+t);
			Top top = new Top(lst,t);
			tops.add(top);
			
			lock.lock();
			isOver = times.isEmpty();
			if(!isOver) {
				t = times.remove(0);
			}
			lock.unlock();
		}
	}
}
