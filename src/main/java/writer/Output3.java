package writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.concurrent.BlockingQueue;
import modeles.Post;
import modeles.Top;


public class Output3 implements Runnable{
	private static BlockingQueue<Top> tops = null;
	private static File f = null;
	private static Top top = null;

	public Output3(String Path, BlockingQueue<Top> tops){
		this.tops = tops;
		this.f = new File(Path+"output.txt");
		clearOutput();
	}
	
	public static boolean checkTopChanged(Top newTop) {
		if(top == null || top.getPosts().size() != newTop.getPosts().size()) {
			top = newTop;
			return true;
		}else {
			for(int i=0;i<top.getPosts().size();i++) {
				if(top.getPosts().get(i) == null && newTop.getPosts().get(i) != null || top.getPosts().get(i) != null && newTop.getPosts().get(i) == null) {
					top = newTop;
					return true;
				}
				if(top.getPosts().get(i) == null && newTop.getPosts().get(i) == null) {
					
				}else if(top.getPosts().get(i).getId() != newTop.getPosts().get(i).getId()) {
					top = newTop;
					return true;
					
				}
			}
		}
		return false;
	}
	
	public static void makeOutput() {
		Top newTop = null;
		try {
			newTop = tops.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(newTop.getTime() != null) {
			if(checkTopChanged(newTop)) {
				write();
			}
			try {
				newTop = tops.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("done writing");
	}
	
	public static void write() {
		StringBuilder output = new StringBuilder();
		Timestamp t = top.getTime();
		
		output.append(t.toString().replace(" ", "T")+"+0000");
		
		for (Post p : top.getPosts())
		{
			if(p != null) {
				output.append(","+String.valueOf(p.getId()));
				output.append(","+p.getUser());
				output.append(","+String.valueOf(p.getScore()));
				output.append(","+String.valueOf(p.getNbComm()));
			}else {
				output.append(",-");
				output.append(",-");
				output.append(",-");
				output.append(",-");
			}
				
		}
		output.append("\r\n");
		
		try {
			Path fichierglobal = f.toPath();
			Files.write(fichierglobal, output.toString().getBytes(), StandardOpenOption.APPEND);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void clearOutput() {
		if(f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		makeOutput();
	}
}

