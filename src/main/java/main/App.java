package main;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import misc.Const;
import misc.ThreadFeeder;
import modeles.Comments;
import modeles.Post;
import reader.Reader;
import reader.ThreadReaderComments;
import reader.ThreadReaderPost;
import writer.Output;
import writer.ThreadWriting;

public class App {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<Post> postsQueue = new ArrayBlockingQueue<Post>(10);
		BlockingQueue<Comments> commentsQueue = new ArrayBlockingQueue<Comments>(10);
		BlockingQueue<List<Post>> OutputQueue = new ArrayBlockingQueue<List<Post>>(100);
		
		Thread commentsProducer = new Thread(new ThreadReaderComments(commentsQueue, Const.Q1Basic2));
		Thread postsProducer = new Thread(new ThreadReaderPost(postsQueue, Const.Q1Basic2));
		Thread threadfeeder = new Thread(new ThreadFeeder(postsQueue,commentsQueue,OutputQueue));
		Thread writterConsumer = new Thread(new ThreadWriting(OutputQueue, Const.Q1Basic2));
		
		commentsProducer.start();
		postsProducer.start();
		threadfeeder.start();
		writterConsumer.start();
		
	}
}
