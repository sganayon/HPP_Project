package main;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import misc.Const;
import misc.Data;
import misc.RWLock;
import misc.ThreadComputing;
import misc.ThreadFeeder;
import modeles.Comments;
import modeles.Post;
import modeles.Top;
import reader.ThreadReader;
import reader.ThreadReaderComments;
import reader.ThreadReaderPost;
import writer.ThreadWriting;

public class TestAppThread_1_3 {

	@Test
	public void testQ1Basic() {
		test(Const.Q1Basic);
	}
	
	@Test
	public void testQ1Basic2() {
		test(Const.Q1Basic2);
	}
	
	@Test
	public void testQ1BigTest() {
		test(Const.Q1BigTest);
	}
	
	@Test
	public void testQ1Case1() {
		test(Const.Q1Case1);
	}
	
	@Test
	public void testQ1Case2() {
		test(Const.Q1Case2);
	}
	
	@Test
	public void testQ1Case3() {
		test(Const.Q1Case3);
	}
	
	@Test
	public void testQ1Case4() {
		test(Const.Q1Case4);
	}
	
	@Test //fail
	public void testQ1Case5() {
		test(Const.Q1Case5);
	}
	@Test //fail
	public void testQ1CommentCount() {
		test(Const.Q1CommentCount);
	}
	@Test //fail
	public void testQ1PostExpiredComment() {
		test(Const.Q1PostExpiredComment);
	}
	@Test //fail
	public void testQ1PostExpiredComment2() {
		test(Const.Q1PostExpiredComment2);
	}
	
	public void test(String PATH) {
		Data.getData().clear();
		
		BlockingQueue<Timestamp> timeQueue = new ArrayBlockingQueue<Timestamp>(100);
		Lock lock = new ReentrantLock();
		RWLock rwlock = new RWLock();
		Thread reader = new Thread(new ThreadReader(PATH,timeQueue, rwlock));
		Thread compute1 = new Thread(new ThreadComputing(timeQueue, lock, PATH, rwlock));
		Thread compute2 = new Thread(new ThreadComputing(timeQueue, lock, PATH, rwlock));
		Thread compute3 = new Thread(new ThreadComputing(timeQueue, lock, PATH, rwlock));
		
		reader.start();
		compute1.start();
		compute2.start();
		compute3.start();
		
		try {
			reader.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			compute1.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			compute2.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			compute3.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InputStream ExpectedStream = null;
		try {
			ExpectedStream = new FileInputStream(PATH+"_expectedQ1.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStream OutputStream = null;
		try {
			OutputStream = new FileInputStream(PATH+"output.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStreamReader ExpectedReader = new InputStreamReader(ExpectedStream);
		InputStreamReader OutputReader = new InputStreamReader(OutputStream);
		BufferedReader ExpectedBuff = new BufferedReader(ExpectedReader);
		BufferedReader OutputBuff = new BufferedReader(OutputReader);
		
		String ligneExpected = null;
		try {
			ligneExpected=ExpectedBuff.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ligneOutput =null;
		try {
			ligneOutput=OutputBuff.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(ligneExpected==null||ligneOutput==null) {
			assertEquals(true, false);
		}
		
		while(ligneExpected!=null&&ligneOutput!=null) {
			assertEquals(ligneExpected, ligneOutput);
			try {
				ligneExpected=ExpectedBuff.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ligneOutput=OutputBuff.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
