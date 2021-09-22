import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class FixedThreadPool implements ThreadPool {
//    private Queue<Runnable> queueTasks;
//    final private Thread[] currentThreads;
//    volatile private int nCurrentTasks;
//    final private int nThreads;
//    private boolean started = false;

    @Override
    public void start(){
//        started = true;
//        int count = Math.min(nThreads, queueTasks.size());
//        for (int i =0; i< count; i++){
//            currentThreads[i] = new Thread(queueTasks.poll());
//        }
    }

    @Override
    public void execute(Runnable runnable) {
//        queueTasks.add(runnable);
//        if (!started)
//            return;
//        if (nCurrentTasks < nThreads) {
//            currentThreads[nCurrentTasks++] = new Thread(queueTasks.poll());
//            return;
//        }
//
//        for (int i = 0; i < nThreads; ++i){
//            if (!currentThreads[i].isAlive()){
//                currentThreads[i] = new Thread(queueTasks.poll());
//                return;
//            }
//        }
    }

//    public void finish() throws InterruptedException {
//        for (int i = 0; i < nThreads; ++i){
//            if (currentThreads[i] != null && currentThreads[i].isAlive()){
//                currentThreads[i].join();
//            }
//        }
//
//    }
//
//    public FixedThreadPool(int nThreads){
//        queueTasks = new LinkedList<>();
//        nCurrentTasks = 0;
//        this.nThreads = nThreads;
//        currentThreads = new Thread[nThreads];
//
//    }
}
