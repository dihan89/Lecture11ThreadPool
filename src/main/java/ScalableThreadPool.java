import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

public class ScalableThreadPool implements ThreadPool {
    private Queue<Runnable> queueTasks;
    private LinkedList<MyThread> currentThreads;
    private int nThreadsMin;
    private int nThreadsMax;
    volatile private Integer nThreadsFree;
    volatile private boolean stopped = false;
    final private Object monitorQueuetasksThreadsFree = new Object();


    private final Runnable daemon = () -> {
        while (true) {
            try {
                System.out.println("Thread's count : " + currentThreads.size());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (monitorQueuetasksThreadsFree){
                     //if (stopped)
                      //   return;
                System.out.println("Thread's count : " + currentThreads.size());
                if (queueTasks.size() > nThreadsFree) {
                    int nAdd = Math.min(queueTasks.size() - nThreadsFree, nThreadsMax - currentThreads.size());
                    addThreads(nAdd);
                } else
                    if (nThreadsFree > 0) {
                        int nDeleted = Math.min(nThreadsFree, currentThreads.size() - nThreadsMin);
                        removeThreads(nDeleted);
                    }

            }
        }

    };

    @Override
    public void start(){
        for (Thread thr : currentThreads)
            thr.start();
       Thread daemonThread = new Thread(daemon);
       daemonThread.setDaemon(true);
       daemonThread.start();
    }

    @Override
    public void execute(Runnable runnable) {
        if(!stopped)
            synchronized (monitorQueuetasksThreadsFree) {
                queueTasks.add(runnable);
            }

    }

    public void finish() throws InterruptedException {
        stopped = true;
        MyThread thr;
        do{
            synchronized (monitorQueuetasksThreadsFree) {
                thr = currentThreads.isEmpty() ? null : currentThreads.poll();
            }
            if (thr != null)
                thr.join();
        } while (thr != null);
    }


    public ScalableThreadPool(int nThreadsMin, int nThreadsMax){
       if (nThreadsMin <= 0)
           throw new IllegalArgumentException("An argument cannot be less the zero");
        if (nThreadsMin > nThreadsMax )
            throw new IllegalArgumentException("First argument must be less than second or equal to it!");
        queueTasks = new LinkedList<>();
        this.nThreadsMin = nThreadsMin;
        this.nThreadsMax = nThreadsMax;
        nThreadsFree = nThreadsMin;
        currentThreads = new LinkedList<>();
        for (int i = 0; i < nThreadsMin; ++i)
            currentThreads.add(new MyThread());
    }
    private void addThreads(int n){
        for (int i = 0; i < n; ++i){
            MyThread thr = new MyThread();
            currentThreads.add(thr);
            thr.start();
        }
    }
    private void removeThreads(int n){
        if ( n<=0 )
            return;
        ListIterator<MyThread> it = currentThreads.listIterator();
        while (n>0 && it.hasNext()){
            if (it.next().free) {
                it.remove();
                n--;
            }
        }

    }
    class MyThread extends Thread{
        boolean free;
        public void run() {
            boolean continueTasks = true;
            Runnable task = null;
            while (continueTasks){
                synchronized (monitorQueuetasksThreadsFree) {
                   if (!queueTasks.isEmpty()) {
                        task = queueTasks.poll();
                        free = false;
                    }
                }

                if (task != null) {
                    task.run();

                    synchronized (monitorQueuetasksThreadsFree){
                        nThreadsFree += 1;
                    }
                    task = null;
                    free = true;
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
        }
        public MyThread(){
            free = true;
        }
    }
}


/*import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

public class ScalableThreadPool implements ThreadPool {
    private Queue<Runnable> queueTasks;
    private LinkedList<MyThread> currentThreads;
    private int nThreadsMin;
    private int nThreadsMax;
    volatile private Integer nThreadsFree;
    volatile private boolean stopped = false;
    final private Object monitorQueuetasksThreadsFree = new Object();


    private final Runnable daemon = () -> {
        while (true) {
            try {
                System.out.println("Thread's count : " + currentThreads.size());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (monitorQueuetasksThreadsFree){
                     if (stopped)
                         return;
                System.out.println("Thread's count : " + currentThreads.size());

                    if (queueTasks.size() > nThreadsFree) {
                        int nAdd = Math.min(queueTasks.size() - nThreadsFree, nThreadsMax - currentThreads.size());
                        addThreads(nAdd);
                    } else
                        if (nThreadsFree > 0) {
                           int nDeleted = Math.min(nThreadsFree, currentThreads.size() - nThreadsMin);
                            removeThreads(nDeleted);
                        }

            }
        }

    };



    @Override
    public void start(){
        for (Thread thr : currentThreads)
            thr.start();
       Thread daemonThread = new Thread(daemon);
       daemonThread.setDaemon(true);
       daemonThread.start();
    }

    @Override
    public void execute(Runnable runnable) {
        if(!stopped)
            synchronized (monitorQueuetasksThreadsFree) {
                queueTasks.add(runnable);
            }

    }

    public void finish() throws InterruptedException {
        stopped = true;
       // synchronized (monitorQueuetasksThreadsFree) {
            for (Thread thr : currentThreads)
                thr.join();
       // }
    }

    public ScalableThreadPool(int nThreadsMin, int nThreadsMax){
        queueTasks = new LinkedList<>();
        this.nThreadsMin = nThreadsMin;
        this.nThreadsMax = nThreadsMax;
        nThreadsFree = nThreadsMin;
        currentThreads = new LinkedList<>();
        for (int i = 0; i < nThreadsMin; ++i)
            currentThreads.add(new MyThread());
    }
    private void addThreads(int n){
        for (int i = 0; i < n; ++i){
            MyThread thr = new MyThread();
            currentThreads.add(thr);
            thr.start();
        }
    }
    private void removeThreads(int n){
        if ( n<=0 )
            return;
        ListIterator<MyThread> it = currentThreads.listIterator();
        while (n>0 && it.hasNext()){
            if (it.next().free) {
                it.remove();
                n--;
            }
        }

    }
    class MyThread extends Thread{
        boolean free;
        public void run() {
            boolean continueTasks = true;
            Runnable task = null;
            while (continueTasks){
                synchronized (monitorQueuetasksThreadsFree) {
                   if (!queueTasks.isEmpty()) {
                        task = queueTasks.poll();
                        free = false;
                    }
                }

                if (task != null) {
                    task.run();
                    synchronized (monitorQueuetasksThreadsFree){
                        nThreadsFree += 1;
                    }
                    task = null;
                    free = true;
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
        }
        public MyThread(){
            free = true;
        }
    }
}*/
