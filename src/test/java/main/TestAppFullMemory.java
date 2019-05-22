package main;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import misc.Const;
import misc.Data;
import misc.ThreadComputing2;
import modeles.Top;
import reader.Reader2;
import writer.Output2;

public class TestAppFullMemory {

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
		
		Vector<Timestamp> timeLst = new Vector<Timestamp>();
		Vector<Top> topLst = new Vector<Top>();
		Lock lock = new ReentrantLock();
		
		Output2.setFile(PATH);
		Output2.setVector(topLst);
		Output2.clearOutput();
		Data.clearData();
		
		Reader2.makeInput(PATH, timeLst);
		
		Thread compute1 = new Thread(new ThreadComputing2(timeLst,lock,topLst));
		Thread compute2 = new Thread(new ThreadComputing2(timeLst,lock,topLst));
		Thread compute3 = new Thread(new ThreadComputing2(timeLst,lock,topLst));
		Thread compute4 = new Thread(new ThreadComputing2(timeLst,lock,topLst));
		
		compute1.start();
		compute2.start();
		compute3.start();
		compute4.start();
		
		try {
			compute1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			compute2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			compute3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			compute4.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Comparator<Top> comp = new Comparator<Top>() {

			@Override
			public int compare(Top o1, Top o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		};
		
		topLst.sort(comp);
		
		Output2.makeOutput();
		
		
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
