package Reader;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import misc.Data;
import misc.TurnInto;
import modeles.Comments;
import modeles.Entree;
import modeles.Post;

public class TestReader {

	@Test
	public void testRead() {
		InputStream fluxComments = null;
		try {
			fluxComments = new FileInputStream(Reader.PATH+"Tests/Q1Basic/posts.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		InputStreamReader lectureComments=new InputStreamReader(fluxComments);
		BufferedReader buffComments=new BufferedReader(lectureComments);
		String[] motsTest = {"2010-02-01T05:12:32.921+0000","1039993","3981","","Lei Liu"};
		assertEquals(motsTest,Reader.readBuff(buffComments).split("\\|"));
	}

	@Test
	public void testToComment() {
		String[] motsTest = {"2010-02-10T04:05:20.777+0000","529590","2886","LOL","Baoping Wu","","1039993"};
		Comments C1 = Reader.toComment(motsTest);
		Comments C2 = new Comments(TurnInto.timeStamp("2010-02-10T04:05:20.777+0000"), 529590, 2886, -1, 1039993);
		C2.equals(C1);
		
		String[] motsTest2 = {"2010-02-10T04:05:20.777+0000","529590","2886","LOL","Baoping Wu","52"};
		Comments C3 = Reader.toComment(motsTest);
		Comments C4 = new Comments(TurnInto.timeStamp("2010-02-10T04:05:20.777+0000"), 529590, 2886, 52, -1);
		C3.equals(C4);
	}

	@Test
	public void testToPost() {
		String[] motsTest = {"2010-02-01T05:12:32.921+0000","1039993","3981","","Lei Liu"};
		Post C1 = Reader.toPost(motsTest);
		Post C2 = new Post(TurnInto.timeStamp("2010-02-01T05:12:32.921+0000"), 1039993,"Lei Liu");
		C2.equals(C1);
	}

	@Test
	public void testMakeInputStringString() {
		List<Post> A = new ArrayList<Post>();
		String[] m1={"2010-02-01T05:12:32.921+0000","1039993","3981","","Lei Liu"};
		String[] m2={"2010-02-02T19:53:43.226+0000","299101","4661","photo299101.jpg","Michael Wang"};
		String[] m3={"2010-02-09T04:05:10.421+0000","529360","2608","","Wei Zhu"};
		String[] m4= {"2010-02-10T04:05:20.777+0000","529590","2886","LOL","Baoping Wu","","1039993"};
		Post P1 = Reader.toPost(m1);
		P1.addComment(Reader.toComment(m4));
		P1.updateScore(TurnInto.timeStamp(m4[0]));
		Post P2 = Reader.toPost(m2);
		P2.updateScore(TurnInto.timeStamp(m4[0]));
		Post P3 = Reader.toPost(m3);
		P3.updateScore(TurnInto.timeStamp(m4[0]));
		A.add(P1);
		A.add(P2);
		A.add(P3);
		Reader.makeInput("Tests/Q1Basic2/posts.dat","Tests/Q1Basic2/comments.dat");
		assertEquals(Data.getData().size(),A.size());
		assertEquals(Data.getData().get(0).getId(), A.get(0).getId());
		assertEquals(Data.getData().get(0).getScore(), A.get(0).getScore());
		assertEquals(Data.getData().get(1).getId(), A.get(1).getId());
		assertEquals(Data.getData().get(1).getScore(), A.get(1).getScore());
		assertEquals(Data.getData().get(2).getId(), A.get(2).getId());
		assertEquals(Data.getData().get(2).getScore(), A.get(2).getScore());
	}

}
