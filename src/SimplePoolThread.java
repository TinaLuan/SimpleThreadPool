/*
 * @author Tian Luan 1899271
 */
import java.util.concurrent.LinkedBlockingQueue;

public class SimplePoolThread implements ISimplePoolThread {
	
	private LinkedBlockingQueue<ISimpleTask> tasksQueue;
	
	
	public SimplePoolThread(LinkedBlockingQueue<ISimpleTask> tasksQueue) {
		this.tasksQueue = tasksQueue;
	}


	@Override
	public void run() {
		while (true) {
			try {
				tasksQueue.take().run();
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			
		}

	}

}
