package bgu.spl.mics.application.objects;


import bgu.spl.mics.MessageBusImpl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

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

	private  static  final class InstanceHolder {
		static final Cluster instance = new Cluster();
	}
	private Cluster(){
	}

	public Cluster getInstence(){
		return Cluster.InstanceHolder.instance;
	}

	public DataBatch ProcessBatch(GPU gpu, DataBatch dataBatch){
		return null;
	}
}
