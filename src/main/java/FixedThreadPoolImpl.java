import java.util.LinkedList;
import java.util.Queue;

public class FixedThreadPoolImpl implements ThreadPool {
    private Queue<Runnable> queueTasks;
    final private Thread[] currentThreads;
    final private int nThreads;
    volatile private boolean stopped = false;
    Runnable runnable = ()->{
        boolean continueTasks = true;
        Runnable task = null;
        while (continueTasks){
            synchronized (queueTasks) {
                if (!queueTasks.isEmpty()) {
                    task = queueTasks.poll();
                }
            }
            if (task != null) {
                task.run();
                task = null;
            }
            else {
                continueTasks = !stopped;
                if (continueTasks) {
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    @Override
    public void start(){
        for (Thread thr: currentThreads)
            thr.start();
    }

    @Override
    public void execute(Runnable runnable) {
       if(!stopped)
          synchronized (queueTasks) {
              queueTasks.add(runnable);
          }
    }

    public void finish() throws InterruptedException {
        stopped = true;
        for (Thread thr : currentThreads)
            thr.join();
    }

    public FixedThreadPoolImpl(int nThreads){
        queueTasks = new LinkedList<>();
        this.nThreads = nThreads;
        currentThreads = new Thread[nThreads];
        for(int i = 0; i < nThreads; ++i)
            currentThreads[i] = new Thread(runnable);
    }
}

