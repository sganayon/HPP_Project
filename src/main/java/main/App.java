package main;

import reader.Reader;
import writer.Output;

public class App {

	public static void main(String[] args) {
		
		Output.clearOutput();
		
		Reader.makeInput("Tests\\Q1BigTest\\posts.dat","Tests\\Q1BigTest\\comments.dat");
		
	}
}
