package dna.graphVisualization;

import java.io.File;
import java.io.IOException;

import argList.ArgList;
import argList.types.atomic.StringArg;
import dna.graph.Graph;
import dna.util.Config;
import dna.visualization.graph.GraphVisualization;

public class Video extends Visualization {

	public String videoDir;
	public String videoFilename;

	public Video(String gdsType, String[] gdsArgs, String graphType,
			String[] graphArgs, String batchType, String[] batchArgs,
			Long seed, Integer batches, Long sleepBetween, String videoDir,
			String videoFilename) {
		super(gdsType, gdsArgs, graphType, graphArgs, batchType, batchArgs,
				seed, batches, sleepBetween);
		this.videoDir = videoDir;
		this.videoFilename = videoFilename;
	}

	public static void main(String[] args) throws Exception {
		ArgList<Video> argList = new ArgList<Video>(Video.class,
				Visualization.args, new StringArg("videoDir",
						"dir where to store the video"), new StringArg(
						"videoFilename",
						"filename of the video (without suffix)"));

		// args = new String[] { "Undirected", "-", "Ring", "20", "Random",
		// "0,0,1,0", "0", "20", "1000", "data/video/", "video" };

		Video v = argList.getInstance(args);
		(new File(v.videoDir)).mkdirs();
		Config.overwrite("GRAPH_VIS_VIDEO_AUTO_RECORD", "true");
		Config.overwrite("GRAPH_VIS_VIDEO_DIR", v.videoDir);
		Graph g = v.vis();
		GraphVisualization.getGraphPanel(g).stopVideo();
	}

	@Override
	public Graph generateGraph() {
		Graph g = super.generateGraph();
		try {
			GraphVisualization.getGraphPanel(g).captureVideo();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return g;
	}
}
