package bgu.spl.mics.application.objects;


import bgu.spl.mics.MessageBusImpl;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private  ArrayList<GPU> GPUs;
	private PriorityBlockingQueue<GPU> activeGPUs; //faster gpus come first
	private PriorityBlockingQueue<CPU> CPUs;
	//private HashMap<CPU,Deque<DataBatch>>() ;
	private HashMap<GPU, Queue<DataBatch>> processedBatches;
	private HashMap<GPU, Queue<DataBatch>> toProcessBatches;



	private static final class InstanceHolder {
		static final Cluster instance = new Cluster();
	}
	private Cluster(){
		GPUs=new ArrayList<>();
		activeGPUs=new PriorityBlockingQueue<>();
		CPUs=new PriorityBlockingQueue<>();
		processedBatches=new HashMap<>();
	}
	public static Cluster getInstance(){
		return Cluster.InstanceHolder.instance;
	}
	public synchronized DataBatch ProcessBatch(GPU gpu, DataBatch dataBatch) {
		boolean processed = false;
		//while (!processed) {
		//	for(CPU cpu: CPUs){
		//	//	if()
		//	}
		//}
		return null;
	}
	public DataBatch getNextBatch(CPU cpu) {
		return null;
	}
	public void finishBatch(CPU cpu) {
	//	batch Batch=
	}

	public void SetActiveGPU(GPU gpu){
		activeGPUs.add(gpu);
	}
	public void SetInactiveGPU(GPU gpu){
		activeGPUs.remove(gpu);
	}
	public void addGPU(GPU gpu){
		GPUs.add(gpu);
	}
	public void addCPU(CPU cpu){
		CPUs.add(cpu);
	}
}
