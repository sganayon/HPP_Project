package misc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;

import modeles.Post;
import modeles.Top;
import reader.ThreadReader;

public class ThreadComputing implements Runnable{
	private static ThreadComputing TH1 = null;
	private static ThreadComputing TH2 = null;
	private static ThreadComputing TH3 = null;
	
	private static File f=null;
	private static Object waitForThreads = new Object();
	private static Object waitForTimes = new Object();
	private static List<Post> oldTop = new ArrayList<Post>(3);
	
	private Lock lock;
	private BlockingQueue<Timestamp> queue=null;
	private RWLock rwLock;
	private Timestamp time =null;

	public ThreadComputing(BlockingQueue<Timestamp> queue, Lock lock, String Path, RWLock rwLock) {
		super();
		this.queue = queue;
		this.lock = lock;
		this.rwLock = rwLock;
		f=new File(Path+"output.txt");
		clearOutput();
		
		if(TH1 == null) {
			TH1 = this;
		}else if(TH2 == null){
			TH2 = this;
		}else {
			TH3 = this;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			time = queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(time.getYear() != 9000) {

			rwLock.readLock();
			List<Post> lst = Data.getTop3at(time);
			rwLock.readUnLock();
			
			//ordonne threads, nessecite qu'ils soient tous init
			synchronized (waitForTimes) {
				while(TH1.getTime() == null || TH2.getTime() == null || TH2.getTime() == null) {
					try {
						waitForTimes.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				waitForTimes.notifyAll();
			}
			
			synchronized (waitForThreads) {
			
				while(time.after(TH1.getTime()) && time.after(TH1.getTime()) && time.after(TH1.getTime())) {
					try {
						waitForThreads.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				lock.lock();
				if(checkTopChange(lst)) {
					write(time);
				}
				lock.unlock();
				
				try {
					time = queue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				waitForThreads.notifyAll();
			}
		}
		System.out.println("done witing");
	}
	
	public boolean checkTopChange(List<Post> newTop) {
		if(oldTop.size() != newTop.size()) {
			oldTop = newTop;
			return true;
		}else {
			for(int i=0;i<oldTop.size();i++) {
				if(oldTop.get(i) == null && newTop.get(i) != null || oldTop.get(i) != null && newTop.get(i) == null) {
					oldTop = newTop;
					return true;
				}
				if(oldTop.get(i) == null && newTop.get(i) == null) {
					
				}else if(oldTop.get(i).getId() != newTop.get(i).getId()) {
					oldTop = newTop;
					return true;
					
				}
			}
		}
		return false;
	}
	
	public void write(Timestamp t) {
		StringBuilder output = new StringBuilder();
		
		output.append(t.toString().replace(" ", "T")+"+0000");
		for (Post p : oldTop)
		{
			if(p != null) {
				output.append(","+String.valueOf(p.getId()));
				output.append(","+p.getUser());
				output.append(","+String.valueOf(p.getScoreAt(t)));
				output.append(","+String.valueOf(p.getNbCommenteers()));	
			}else {
				output.append(",-");
				output.append(",-");
				output.append(",-");
				output.append(",-");
			}
			
		}
		output.append("\r\n");
		
		try {
			Path fichierglobal = f.toPath();
			Files.write(fichierglobal, output.toString().getBytes(), StandardOpenOption.APPEND);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void clearOutput() {
		if(f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Timestamp getTime() {
		return this.time;
	}
}
