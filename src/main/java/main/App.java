package main;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import misc.Const;
import misc.Data2;
import misc.Data3;
import misc.RWLock;
import misc.ThreadComputing;
import misc.ThreadComputing2;
import misc.ThreadComputing3;
import misc.ThreadFeeder;
import modeles.Comments;
import modeles.Entree;
import modeles.Post;
import modeles.Top;
import reader.Reader;
import reader.Reader2;
import reader.Reader3;
import reader.ThreadReader;
import reader.ThreadReaderComments;
import reader.ThreadReaderPost;
import writer.Output;
import writer.Output2;
import writer.Output3;
import writer.ThreadWriting;

public class App {

	public static void main(String[] args) throws InterruptedException {

	long deb = System.nanoTime();
	
	String PATH = Const.PATH;
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
	long time = (fin -deb)/1000000000;
	System.out.println("execution time : "+time+"s");
	}
}
