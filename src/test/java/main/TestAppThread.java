package main;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

import misc.Const;
import misc.Data;
import misc.ThreadFeeder;
import modeles.Comments;
import modeles.Post;
import modeles.Top;
import reader.Reader;
import reader.ThreadReaderComments;
import reader.ThreadReaderPost;
import writer.Output;
import writer.ThreadWriting;

public class TestAppThread {
	
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
		
		BlockingQueue<Post> postsQueue = new ArrayBlockingQueue<Post>(10);
		BlockingQueue<Comments> commentsQueue = new ArrayBlockingQueue<Comments>(10);
		BlockingQueue<Top> OutputQueue = new ArrayBlockingQueue<Top>(100);
		
		Thread commentsProducer = new Thread(new ThreadReaderComments(commentsQueue,PATH));
		Thread postsProducer = new Thread(new ThreadReaderPost(postsQueue, PATH));
		Thread threadfeeder = new Thread(new ThreadFeeder(postsQueue,commentsQueue,OutputQueue));
		Thread writterConsumer = new Thread(new ThreadWriting(OutputQueue, PATH));
		
		commentsProducer.start();
		postsProducer.start();
		threadfeeder.start();
		writterConsumer.start();
		
		try {
			commentsProducer.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			postsProducer.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			threadfeeder.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writterConsumer.join();
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
