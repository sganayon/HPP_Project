package Output;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import modeles.Post;
import modeles.Top;

public class Output {
	private ArrayList<Top> top3=new ArrayList<Top>(3);
	
	
	Output(ArrayList<Post> p){
		Top.createTop(p, top3);
	}
	
	
	public void write() 
	{
		List<String> output = new ArrayList<String>();
		Timestamp t;
		if (top3.get(0).getTS().after(top3.get(1).getTS()))
		{
			if (top3.get(0).getTS().after(top3.get(2).getTS()))
				t=top3.get(0).getTS();
			else
				t=top3.get(0).getTS();
		}
		else
		{
			if (top3.get(1).getTS().after(top3.get(2).getTS()))
				t=top3.get(1).getTS();
			else
				t=top3.get(2).getTS();
		}
		output.add(t.toString()+",");
		for (Top p : top3)
		{
			output.add(p.getPostID().toString()+",");
			output.add(p.getUserID()+",");
			output.add(p.getScore().toString()+",");
			output.add(p.getNbCommenters().toString()+",");	
		}
		
		try {
			Path fichier = Paths.get("output.txt");
			Path fichierglobal = Paths.get("outputglobal.txt");
			Files.write(fichier, output, Charset.forName("UTF-8"));
			Files.write(fichierglobal, output, Charset.forName("UTF-8"), StandardOpenOption.APPEND);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
