package main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import misc.Const;
import modeles.Comments;
import modeles.Post;
import reader.Reader;
import reader.ThreadReaderComments;
import reader.ThreadReaderPost;
import writer.Output;

public class App {

	public static void main(String[] args) throws InterruptedException {
		Output.setFile(Const.Q1Basic);
		Output.clearOutput();
		
		Reader.makeInput(Const.PATH);
		/*
		long startn = System.nanoTime();
		long startm =System.currentTimeMillis();
		BlockingQueue<Post> P = new ArrayBlockingQueue<>(10);
		BlockingQueue<Comments> C = new ArrayBlockingQueue<>(10);
		Thread R1 = new Thread(new ThreadReaderPost(P,Const.Q1Basic + "/posts.dat"));
		Thread R2 = new Thread(new ThreadReaderComments(C,Const.Q1Basic + "/comments.dat"));
		R1.start();
		R2.start();
		R1.join();
		R2.join();
		long endn = System.nanoTime() - startn;
		long endm = System.currentTimeMillis() - startm;
		System.out.println(endn);
		System.out.println(endm);*/
	}
}
