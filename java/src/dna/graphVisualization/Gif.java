package dna.graphVisualization;

import java.io.File;

import argList.ArgList;
import argList.types.atomic.IntArg;
import argList.types.atomic.StringArg;
import dna.util.Execute;

public class Gif extends Screenshots {

	public String gifFilename;
	public int gifDelay;

	public Gif(String gdsType, String[] gdsArgs, String graphType,
			String[] graphArgs, String batchType, String[] batchArgs,
			Long seed, Integer batches, Long sleepBetween,
			String screenshotsDir, String gifFilename, Integer gifDelay) {
		super(gdsType, gdsArgs, graphType, graphArgs, batchType, batchArgs,
				seed, batches, sleepBetween, screenshotsDir);
		this.gifFilename = gifFilename;
		this.gifDelay = gifDelay;
	}

	public static void main(String[] args) throws Exception {
		ArgList<Gif> argList = new ArgList<Gif>(
				Gif.class,
				Visualization.args,
				new StringArg("screenshotsDir",
						"dir where to store the screenshots"),
				new StringArg("gifFilename",
						"filename of the gif to generate (without .gif at the end)"),
				new IntArg("gifDelay",
						"delay between images, 1 for 0.01 sec, 10 for 0.1 sec, 100 for 1 sec, etc."));

		// args = new String[] { "Undirected", "-", "Ring", "20", "Random",
		// "0,0,1,0", "0", "50", "0", "data/gvis/", "_animated", "10" };

		Gif g = argList.getInstance(args);
		(new File(g.screenshotsDir)).mkdirs();
		g.vis();
		Execute.exec("/opt/local/bin/convert -delay " + g.gifDelay + " *.png "
				+ g.gifFilename + ".gif", g.screenshotsDir);
		System.exit(0);
	}

}
