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

import misc.BenchmarkPathing;
import misc.Const;
import reader.Reader;
import writer.Output;

@State(Scope.Benchmark)
public class BenchmarkNaive {

	@Param({ BenchmarkPathing.Q1Basic, BenchmarkPathing.Q1Basic2, BenchmarkPathing.Q1BigTest, BenchmarkPathing.Q1Case1,
			BenchmarkPathing.Q1Case2, BenchmarkPathing.Q1Case3, BenchmarkPathing.Q1Case4, BenchmarkPathing.Q1Case5,
			BenchmarkPathing.Q1CommentCount, BenchmarkPathing.Q1PostExpiredComment,
			BenchmarkPathing.Q1PostExpiredComment2 })
	private String path;

	/**
	 * the benchmark compute every operations that is in this method. We are testing
	 * the iteration of an array.
	 * 
	 * @param Fork          change the number of real test and warmup test
	 * @param Warmup        change the number of iteration the warmup test make and
	 *                      the time an iteration last.
	 * @param Measurement   same as Warmup but for real test
	 * @param BenchmarkMode the test that are measured (here averageTime and
	 *                      throughput)
	 * @return an int that ensure the compiler won't remove the code in this method
	 */
	@Benchmark
	@Fork(value = 1, warmups = 2)
	@Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
	@Measurement(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
	@BenchmarkMode({ Mode.Throughput, Mode.AverageTime })
	public void testMethod() {
		Output.setFile(path);
		Output.clearOutput();

		Reader.makeInput(path);
	}

	/**
	 * initialize the list with random int
	 * 
	 * @param Setup indicate that this method will be called before every test, it
	 *              won't be included in the measurement
	 */
	@Setup(Level.Invocation)
	public void setUp() {

	}
}
