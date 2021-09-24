import org.junit.jupiter.api.Test;

public class ThreadPoolImplTest {
    String sep = System.getProperty("line.separator");

    @Test
    public void testFixedPoolThread() throws InterruptedException {

        FixedThreadPoolImpl poolThread = new FixedThreadPoolImpl(4);
        for (int i = 0; i < 14; ++i)
            poolThread.execute(new MyRun(i));
        poolThread.start();
        for (int i = 55; i < 60; ++i)
            poolThread.execute(new MyRun(i));
        poolThread.finish();
        for (int i = 135; i < 240; ++i)
          poolThread.execute(new MyRun(i));

    }

    @Test
    public void testFixedPoolThread2() throws InterruptedException {

        FixedThreadPoolImpl2 poolThread = new FixedThreadPoolImpl2(4);
        for (int i = 0; i < 14; ++i)
            poolThread.execute(new MyRun(i));
        poolThread.start();
        for (int i = 55; i < 60; ++i)
            poolThread.execute(new MyRun(i));
        poolThread.finish();
        for (int i = 135; i < 240; ++i)
            poolThread.execute(new MyRun(i));

    }

    @Test
    public void testScalablePoolThread() throws InterruptedException {

            ScalableThreadPool poolThread = new ScalableThreadPool(2, 100);
            for (int i = 0; i < 14; ++i)
                poolThread.execute(new MyRun(i));
            poolThread.start();
            Thread.sleep(50_000);
            poolThread.execute(new MyRun(1000));
            poolThread.execute(new MyRun(1000));
            //Thread.sleep(10_000);
            for (int i = 55; i < 80; ++i)
                poolThread.execute(new MyRun(i));
            //Thread.sleep(50_000);
           poolThread.finish();
          for (int i = 135; i < 240; ++i)
                poolThread.execute(new MyRun(i));

        }

    @Test
    public void testScalablePoolThread2() throws InterruptedException {

        ScalableThreadPool2 poolThread = new ScalableThreadPool2(2, 100);
        for (int i = 0; i < 14; ++i)
            poolThread.execute(new MyRun(i));
        poolThread.start();
        for (int i = 0; i < 10; i++)
            Thread.sleep(1_000);
        poolThread.execute(new MyRun(1000));
        poolThread.execute(new MyRun(1000));
        Thread.sleep(10_000);
        for (int i = 14; i < 80; ++i)
            poolThread.execute(new MyRun(i));
       // Thread.sleep(50_000);
        poolThread.interrupt();
     //   for (int i = 135; i < 240; ++i)
         //   poolThread.execute(new MyRun(i));

    }

    class MyRun implements Runnable {
        private int number;

        MyRun(int i) {
            number = i;
        }

        public void run() {
            try {
                Thread.sleep(10 + (int) Math.random() * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
            System.out.printf("%s  Hello-%d%s", Thread.currentThread().getName(), number, sep);
        }


    }


}

