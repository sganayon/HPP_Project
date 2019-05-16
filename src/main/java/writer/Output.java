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

import misc.Data;
import modeles.Post;
import reader.Reader;

public class Output {
	private static List<Post> top=new ArrayList<Post>(3);
	private static File f = new File("output.txt");

	Output(){
		
	}
	
	public static void checkTopChanged(List<Post> top3Post) {
		if(top.size() != top3Post.size()) {
			top = top3Post;
			write();
		}else {
			for(int i=0;i<top.size();i++) {
				if(top.get(i).getId() != top3Post.get(i).getId()) {
					top = top3Post;
					write();
					break;
				}
			}
		}
	}
	
	public static void write() 
	{
		StringBuilder output = new StringBuilder();
		Timestamp t = Data.getLastUpdate();
		
		output.append(t.toString().replace(" ", "T")+"+0000");
		for (Post p : top)
		{
			output.append(","+String.valueOf(p.getId()));
			output.append(","+p.getUser());
			output.append(","+String.valueOf(p.getScore()));
			output.append(","+String.valueOf(p.getNbCommenteers()));	
		}
		if(top.size() != 3) {
			for(int i=0;i<3-top.size();i++) {
				output.append(",-");
				output.append(",-");
				output.append(",-");
				output.append(",-");
			}
		}
		output.append("\r\n");
		
		try {
			Path fichierglobal = Paths.get("output.txt");
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
