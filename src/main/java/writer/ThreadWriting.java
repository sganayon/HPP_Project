package writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import misc.Data;
import modeles.Post;

public class ThreadWriting implements Runnable {
	private static List<Post> top=new ArrayList<Post>(3);
	private static File f=null;
	private BlockingQueue<Boolean> queue=null;

	ThreadWriting(BlockingQueue<Boolean> queue, String path){
		f=new File(path+"output.txt");
		this.queue=queue;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			while(queue.take())
			{
				
				if(checkTopChanged(Data.getTopScore()))
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
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static boolean checkTopChanged(List<Post> top3Post) {
		if(top.size() != top3Post.size()) {
			top = top3Post;
			return true;
		}else {
			for(int i=0;i<top.size();i++) {
				if(top.get(i).getId() != top3Post.get(i).getId()) {
					top = top3Post;
					return true;
					
				}
			}
		}
		return false;
	}

}
