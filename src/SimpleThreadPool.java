/*
 * @author Tian Luan 1899271
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;


public class SimpleThreadPool implements ISimpleThreadPool {
	
	public final static int NUM_THREADS = 5;
	
	private LinkedBlockingQueue<ISimpleTask> tasksQueue;
	private ArrayList<Thread> threadsList = new ArrayList<> (NUM_THREADS);
	
	public SimpleThreadPool() {
		tasksQueue = new LinkedBlockingQueue<>();
		
	}


	@Override
	public void start() {
		for (int i=0; i<NUM_THREADS; i++) {
			Thread t = new Thread(new SimplePoolThread(tasksQueue));
			threadsList.add(t);
			t.start();
		}
	}

	
	@Override
	public void stop() {
		for (int i=0; i<NUM_THREADS; i++) {
			threadsList.get(i).interrupt();
		}
		
	}

	
	@Override
	public void addTask(ISimpleTask task) {
		try {
			tasksQueue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
