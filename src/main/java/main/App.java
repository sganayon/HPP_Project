package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Reader.Reader;

public class App {

	public static void main(String[] args) {
		Reader R = new Reader();
		try {
			R.makeInput();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	

}
