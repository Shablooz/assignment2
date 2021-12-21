package bgu.spl.mics.application.objects;



import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private final ArrayList<GPU> GPUs;
	private final PriorityBlockingQueue<GPU> activeGPUs; //faster gpus come first
	private final PriorityBlockingQueue<CPU> CPUs;
	private final HashMap<GPU, Deque<DataBatch>> processedBatches;
	private final ConcurrentHashMap<CPU, DataBatch> toProcessBatches;
	private final HashMap<GPU,Integer> inProcessing;
	private final HashMap<CPU,GPU> batchProcessingAllocation;



	private static final class InstanceHolder {
		static final Cluster instance = new Cluster();
	}
	private Cluster(){
		GPUs=new ArrayList<>();
		activeGPUs=new PriorityBlockingQueue<>();
		CPUs=new PriorityBlockingQueue<>();
		toProcessBatches=new ConcurrentHashMap<>();
		processedBatches=new HashMap<>();
		batchProcessingAllocation=new HashMap<>();
		inProcessing=new HashMap<>();
	}
	public static Cluster getInstance(){
		return Cluster.InstanceHolder.instance;
	}
	public DataBatch getProcessedBatch(GPU gpu){
		ArrayDeque<DataBatch> a= (ArrayDeque<DataBatch>) processedBatches.get(gpu);
		if(!a.isEmpty()){
			return a.getFirst();
		}
		return null;
	}
	public DataBatch ProcessBatch(CPU cpu) {
		return toProcessBatches.get(cpu);
	}
	public DataBatch getNextBatch(CPU cpu) {
		synchronized (GPUs) {
			DataBatch batch = null;
			if (!activeGPUs.isEmpty()) {
				GPU chosenGPU = null;
				boolean foundGPU = false;
				for (GPU gpu : activeGPUs) {
					if ((gpu.getInProcessing() + inProcessing.get(gpu)) <= gpu.getVRAM()) {
						chosenGPU = gpu;
						foundGPU = true;
						break;
					}
				}
				if (!foundGPU) {
					for (GPU gpu : activeGPUs) {
						if (!gpu.isFull()) {
							chosenGPU = gpu;
							foundGPU = true;
							break;
						}
					}
				}
			if (!foundGPU) {
				chosenGPU = activeGPUs.peek(); //quickest gpu, if no more fitting gpu is found
			}
				inProcessing.merge(chosenGPU,1,Integer::sum); //if there is no value, it becomes 1, else it grows by one
				assert chosenGPU != null;
				batch = chosenGPU.getBatchToProcess();
				if(batch==null)
					chosenGPU.deactivate();
				toProcessBatches.put(cpu, batch);
				batchProcessingAllocation.put(cpu,chosenGPU);
			}
			return batch;
		}
	}
	public void finishBatch(CPU cpu) {
		DataBatch batch=toProcessBatches.get(cpu);
		GPU gpu=batchProcessingAllocation.get(cpu);
			processedBatches.get(gpu).addLast(batch);
			inProcessing.merge(gpu,-1,Integer::sum);
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
	public void initialize(){
		for(GPU gpu: GPUs){
			inProcessing.put(gpu, 0);
			processedBatches.put(gpu,new ArrayDeque<>());
		}
	//	for(CPU cpu: CPUs){
	//		toProcessBatches.put(cpu,new ConcurrentHashMap<>());
	//	}
	}
}
