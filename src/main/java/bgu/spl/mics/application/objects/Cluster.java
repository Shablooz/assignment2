package bgu.spl.mics.application.objects;


import bgu.spl.mics.MessageBusImpl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private ArrayList<GPU> GPUs;
	private ArrayList<CPU> CPUs;
	private HashMap<GPU, Queue<DataBatch>> ProcessedBatches;

	private static final class InstanceHolder {
		static final Cluster instance = new Cluster();
	}
	private Cluster(){
		GPUs=new ArrayList<>();
		CPUs=new ArrayList<>();
	}
	public static Cluster getInstance(){
		return Cluster.InstanceHolder.instance;
	}
	public DataBatch ProcessBatch(GPU gpu, DataBatch dataBatch){
		return null;
	}
	public void addGPU(GPU gpu){
		GPUs.add(gpu);
	}
	public void addCPU(CPU cpu){
		CPUs.add(cpu);
	}
}
