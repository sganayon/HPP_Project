package main;

import misc.Const;
import reader.Reader;
import reader.ThreadReaderComments;
import reader.ThreadReaderPost;
import writer.Output;

public class App {

	public static void main(String[] args) throws InterruptedException {
		
		Thread readP = new Thread(new ThreadReaderPost());
		Thread readC = new Thread(new ThreadReaderComments());
		readP.start();
		readC.start();
		
		readP.join();
		readC.join();

	}
}
