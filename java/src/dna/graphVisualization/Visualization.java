package dna.graphVisualization;

import argList.ArgList;
import argList.types.Arg;
import argList.types.array.StringArrayArg;
import argList.types.atomic.EnumArg;
import argList.types.atomic.IntArg;
import argList.types.atomic.LongArg;
import dna.graph.Graph;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.generators.GraphGenerator;
import dna.updates.batch.Batch;
import dna.updates.batch.BatchSanitization;
import dna.updates.generators.BatchGenerator;
import dna.util.Rand;
import dna.util.fromArgs.BatchGeneratorFromArgs;
import dna.util.fromArgs.BatchGeneratorFromArgs.BatchType;
import dna.util.fromArgs.GraphDataStructuresFromArgs;
import dna.util.fromArgs.GraphDataStructuresFromArgs.GdsType;
import dna.util.fromArgs.GraphGeneratorFromArgs;
import dna.util.fromArgs.GraphGeneratorFromArgs.GraphType;
import dna.visualization.graph.GraphVisualization;

public class Visualization {
	public GraphGenerator gg;
	public BatchGenerator bg;
	public long seed;
	public int batches;
	public long sleepBetween;

	@SuppressWarnings("rawtypes")
	protected static Arg[] args = new Arg[] {
			new EnumArg("gdsType", "type of gds to use", GdsType.values()),
			new StringArrayArg("gdsArgs", "arguments for the gds", ","),
			new EnumArg("graphType", "type of graph to generate",
					GraphType.values()),
			new StringArrayArg("graphArgs",
					"arguments for the graph generator", ","),
			new EnumArg("batchType", "type of batches to generate",
					BatchType.values()),
			new StringArrayArg("batchArgs",
					"arguments for the batch generator", ","),
			new LongArg("seed", "seed to initialize the PRNG with"),
			new IntArg("batches", "number of batches to generate"),
			new LongArg("sleepBetween", "time in ms to sleep between batches") };

	public Visualization(String gdsType, String[] gdsArgs, String graphType,
			String[] graphArgs, String batchType, String[] batchArgs,
			Long seed, Integer batches, Long sleepBetween) {
		GraphDataStructure gds = GraphDataStructuresFromArgs.parse(
				GdsType.valueOf(gdsType), gdsArgs);
		this.gg = GraphGeneratorFromArgs.parse(gds,
				GraphType.valueOf(graphType), graphArgs);
		this.bg = BatchGeneratorFromArgs.parse(this.gg,
				BatchType.valueOf(batchType), batchArgs);
		this.seed = seed;
		this.batches = batches;
		this.sleepBetween = sleepBetween;
	}

	public static void main(String[] args) throws Exception {
		ArgList<Visualization> argList = new ArgList<Visualization>(
				Visualization.class, Visualization.args);

		// args = new String[] { "Undirected", "-", "Ring", "50", "Random",
		// "0,0,1,0", "0", "20", "1000" };

		Visualization v = argList.getInstance(args);
		v.vis();
		System.exit(0);
	}

	public Graph vis() throws Exception {
		GraphVisualization.enable();

		Rand.seed = seed;
		Rand.init(seed);

		Graph g = this.generateGraph();
		for (int i = 0; i < this.batches; i++) {
			if (!this.bg.isFurtherBatchPossible(g)) {
				System.out.println("cannot generate further batch");
				break;
			}
			this.applyNextBatch(g);
		}

		return g;
	}

	protected Graph generateGraph() {
		Graph g = this.gg.generate();
		this.sleep(this.sleepBetween);
		return g;
	}

	protected Batch applyNextBatch(Graph g) {
		Batch b = this.bg.generate(g);
		BatchSanitization.sanitize(b);
		System.out.println(b);
		b.apply(g);
		this.sleep(this.sleepBetween);
		return b;
	}

	public void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
