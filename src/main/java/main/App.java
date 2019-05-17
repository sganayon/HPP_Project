package main;

import misc.Const;
import reader.Reader;
import reader.ThreadReaderComments;
import reader.ThreadReaderPost;
import writer.Output;

public class App {

	public static void main(String[] args) throws InterruptedException {
		Output.setFile(Const.Q1Basic);
		Output.clearOutput();
		
		Reader.makeInput(Const.Q1Basic);
		
	}
}
