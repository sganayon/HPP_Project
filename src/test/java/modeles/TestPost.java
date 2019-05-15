package modeles;

import java.sql.Timestamp;

import org.junit.Test;

import junit.framework.TestCase;
import misc.TurnInto;

public class TestPost extends TestCase {
	
	@Test
	public void testUpdateScore() {
		Timestamp t1 = TurnInto.timeStamp("2010-02-09T04:05:20.777+0000");
		Post p1 = new Post(t1,1,"Axel");
		assertEquals(10, p1.getScore());
		
		Timestamp t2 = TurnInto.timeStamp("2010-02-10T04:05:21.777+0000");
		Post p2 = new Post(t2,2,"Axel2");
		p1.updateScore(t2);
		assertEquals(9, p1.getScore());
		
		Timestamp t3 = TurnInto.timeStamp("2010-02-10T04:05:22.777+0000");
		Comments c1 = new Comments(t3,1,1,-1,2);
		p2.addComment(c1);
		p1.updateScore(t3);
		p2.updateScore(t3);
		assertEquals(9, p1.getScore());
		assertEquals(20, p2.getScore());
	}
}
