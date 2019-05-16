package writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import misc.Data;
import modeles.Post;

public class ThreadWriting implements Runnable {
	private  List<Post> top=new ArrayList<Post>(3);
	private  File f;

	ThreadWriting(List<Post> list, File file){
		f=file;
		top=list;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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

}
