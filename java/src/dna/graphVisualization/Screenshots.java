package dna.graphVisualization;

import java.io.File;

import argList.ArgList;
import argList.types.atomic.StringArg;
import dna.graph.Graph;
import dna.updates.batch.Batch;
import dna.visualization.graph.GraphVisualization;

public class Screenshots extends Visualization {

	public String screenshotsDir;

	public Screenshots(String gdsType, String[] gdsArgs, String graphType,
			String[] graphArgs, String batchType, String[] batchArgs,
			Long seed, Integer batches, Long sleepBetween, String screenshotsDir) {
		super(gdsType, gdsArgs, graphType, graphArgs, batchType, batchArgs,
				seed, batches, sleepBetween);
		this.screenshotsDir = screenshotsDir;
	}

	public static void main(String[] args) throws Exception {
		ArgList<Screenshots> argList = new ArgList<Screenshots>(
				Screenshots.class, Visualization.args, new StringArg(
						"screenshotsDir", "dir where to store the screenshots"));

		// args = new String[] { "Undirected", "-", "Ring", "20", "Random",
		// "0,0,1,0", "0", "20", "0", "data/gvis/" };

		Screenshots s = argList.getInstance(args);
		(new File(s.screenshotsDir)).mkdirs();
		s.vis();
	}

	public void takeScreenshot(Graph g) {
		GraphVisualization.getGraphPanel(g).captureScreenshot(true,
				this.screenshotsDir, getName(g.getTimestamp()));
	}

	public static String getName(long timestamp) {
		if (timestamp < 10) {
			return "0000000" + timestamp;
		} else if (timestamp < 100) {
			return "000000" + timestamp;
		} else if (timestamp < 1000) {
			return "00000" + timestamp;
		} else if (timestamp < 10000) {
			return "0000" + timestamp;
		} else if (timestamp < 100000) {
			return "000" + timestamp;
		} else if (timestamp < 1000000) {
			return "00" + timestamp;
		} else if (timestamp < 10000000) {
			return "0" + timestamp;
		} else {
			return "" + timestamp;
		}
	}

	@Override
	public Graph generateGraph() {
		Graph g = super.generateGraph();
		this.takeScreenshot(g);
		return g;
	}

	@Override
	public Batch applyNextBatch(Graph g) {
		Batch b = super.applyNextBatch(g);
		this.takeScreenshot(g);
		return b;
	}

}
