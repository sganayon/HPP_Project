package benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import misc.Const;
import reader.Reader;

@State(Scope.Benchmark)
public class BenchmarkNaive {

	/**
	 * the size of the array. it will change for every test
	 */
	@Param({ "Tests/Q1Basic2/posts.dat", "posts.dat", "dataDebs/posts.dat"})
	private String postsFile;
	
	@Param({ "Tests/Q1Basic2/comments.dat", "comments.dat", "dataDebs/comments.dat"})
	private String commentsFile;
	
	/**
	 * the benchmark compute every operations that is in this method.
	 * We are testing the iteration of an array.
	 * @param Fork change the number of real test and warmup test 
	 * @param Warmup change the number of iteration the warmup test make and the time an iteration last. 
	 * @param Measurement same as Warmup but for real test
	 * @param BenchmarkMode the test that are measured (here averageTime and throughput)
	 * @return an int that ensure the compiler won't remove the code in this method
	 */
    @Benchmark
    @Fork(value=1,warmups=2)
    @Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    public void testMethod() {
    	Reader.makeInput(Const.Q1BigTest);
    }
    
    /**
     * initialize the list with random int
     * @param Setup indicate that this method will be called before every test, it won't be included in the measurement
     */
    @Setup(Level.Invocation)
    public void setUp() {
    	
    }
}
