package writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import misc.Const;
import misc.Data;
import modeles.Post;
import modeles.Top;
import reader.Reader;


public class Output2 {
	private static Vector<Top> tops = null;
	private static File f = null;
	private static Top top = null;

	public Output2(){
		
	}
	
	public static void setFile(String path) {
		f = new File(path+"output.txt");
	}
	public static void setVector(Vector<Top> topvect) {
		tops = topvect;
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
		for(Top top3 : tops) {
			if(checkTopChanged(top3)) {
				write();
			}
		}
		System.out.println("done writing");
	}
	
	public static void write() 
	{
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
}

