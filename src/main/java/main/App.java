package main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import misc.Const;
import misc.Data3;
import misc.ThreadComputing3;
import modeles.Entree;
import modeles.Top;
import reader.Reader3;
import writer.Output3;


public class App {

	public static void main(String[] args) throws InterruptedException {

	long deb = System.nanoTime();
	
	String PATH = Const.AllData;
	BlockingQueue<Entree> events = new ArrayBlockingQueue<Entree>(100);
	BlockingQueue<Top> tops = new ArrayBlockingQueue<Top>(100);
	Data3.clearData();
	
	Thread threadReader = new Thread(new Reader3(PATH,events));
	threadReader.start();
	
	Thread threadComputing = new Thread(new ThreadComputing3(events,tops));
	threadComputing.start();
	
	Thread threadWriter = new Thread(new Output3(PATH,tops));
	threadWriter.start();
	

	threadWriter.join();
	long fin = System.nanoTime();
	long Totalsecondes = (fin -deb)/1000000000;
	int min = (int) Totalsecondes/60;
	int sec = (int) Totalsecondes - min*60;
	System.out.println("execution time : "+min+" min "+sec+" sec.");
	}
}
