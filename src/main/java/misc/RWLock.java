package misc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RWLock {
	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	int nbReader = 0;
	boolean writter = false;
	
	public void writeLock() {
		lock.lock();
		
		while(writter) {
			try {
				condition.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		writter = true;
		
		while(nbReader>0 ) { //<---si ordre d'ecriture important : faire un while writter puis set le bool puis while reader VOIR UNLOCK READER
			try {
				condition.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lock.unlock();
	}
	
	public void writeUnLock() {
		lock.lock();
		writter = false;
		condition.signalAll();
		lock.unlock();
	}
	
	public void readLock() {
		lock.lock();
		while(writter)
			try {
				condition.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		nbReader++;
		lock.unlock();
	}
	
	public void readUnLock() {
		lock.lock();
		nbReader--;
		if(nbReader==0) {
			condition.signalAll(); //<--- signalAll si double boucle (evite de bloquer tt les writter en attente de reader ==0  si on reveil writter en attente de writter == false)
		}
		lock.unlock();
	}
}