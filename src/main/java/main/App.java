package main;

import misc.Const;
import reader.Reader;
import writer.Output;

public class App {

	public static void main(String[] args) {
		
		Output.setFile(Const.Q1Basic);
		Output.clearOutput();
		
		Reader.makeInput(Const.Q1Basic);
		
	}
}
