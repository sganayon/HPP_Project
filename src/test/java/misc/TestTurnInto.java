package misc;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

public class TestTurnInto {

	@Test
	public void test() {
		Timestamp t1 = TurnInto.timeStamp("2010-02-10T04:05:21.777+0000");
		Timestamp t2 = new Timestamp(2010-1900,2,10,4,5,21,777);
		assertEquals(t2, t1);
	}

}
