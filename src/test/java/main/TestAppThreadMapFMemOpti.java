package main;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

import misc.Const;
import misc.Data3;
import misc.ThreadComputing3;
import modeles.Entree;
import modeles.Top;
import reader.Reader3;
import writer.Output3;

public class TestAppThreadMapFMemOpti {
	
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
	
	@Test //fail -> ne decompte pas les scores si aucun event
	public void testQ1Case1() {
		test(Const.Q1Case1);
	}
	
	@Test //fail -> ne decompte pas les scores si aucun event
	public void testQ1Case2() {
		test(Const.Q1Case2);
	}
	
	@Test //fail -> ne decompte pas les scores si aucun event
	public void testQ1Case3() {
		test(Const.Q1Case3);
	}
	
	@Test //fail -> idem pour case3
	public void testQ1Case4() {
		test(Const.Q1Case4);
	}
	
	@Test //fail-> idem + top intermediaire (mm time)
	public void testQ1Case5() {
		test(Const.Q1Case5);
	}
	@Test //fail -> idem + score intermediaire
	public void testQ1CommentCount() {
		test(Const.Q1CommentCount);
	}
	@Test 
	public void testQ1PostExpiredComment() {
		test(Const.Q1PostExpiredComment);
	}
	@Test //fail -> score indermediaire
	public void testQ1PostExpiredComment2() {
		test(Const.Q1PostExpiredComment2);
	}
	
	public void test(String PATH) {
		
		BlockingQueue<Entree> events = new ArrayBlockingQueue<Entree>(100);
		BlockingQueue<Top> tops = new ArrayBlockingQueue<Top>(100);
		Data3.clearData();
		
		Thread threadReader = new Thread(new Reader3(PATH,events));
		threadReader.start();
		
		Thread threadComputing = new Thread(new ThreadComputing3(events,tops));
		threadComputing.start();
		
		Thread threadWriter = new Thread(new Output3(PATH,tops));
		threadWriter.start();
		
		try {
			threadReader.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			threadComputing.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			threadWriter.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		checkOutput(PATH);
	}
	
	public void checkOutput(String PATH) {
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
		if((ligneExpected != null && ligneOutput == null) || (ligneExpected == null && ligneOutput != null)) {
			assertEquals(true,false);
		}
	}
}
