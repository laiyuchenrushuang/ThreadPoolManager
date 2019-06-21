package com.ly.toolthreadmanager;

import android.net.wifi.aware.DiscoverySession;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    String countFlag = null;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initEvent();
    }

    private void initEvent() {

        findViewById(R.id.bt_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
                ThreadPoolManager.getInstence().putExecutableTasks(new Runner(count++));
            }
        });

        findViewById(R.id.bt_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t1 = new Thread(new Runner1("t1"));
                Thread t2 = new Thread(new Runner1("t2"));
                Thread t3 = new Thread(new Runner1("t3"));
                executorService.submit(t1);
                executorService.submit(t2);
                executorService.submit(t3);
//                executorService.shutdown();//  可以线程start()接join()

            }
        });

        findViewById(R.id.bt_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object lock = new Object();
                ThreadA a = new ThreadA(lock);
                ThreadB b = new ThreadB(lock);
                a.start();
                b.start();
                count = 0;

            }
        });

    }

    private class Runner1 implements Runnable {
        String msg;

        public Runner1(String s) {
            this.msg = s;
        }

        @Override
        public void run() {
            showToast(msg);
        }
    }


    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    int count = 0;

    private class ThreadA extends Thread {
        Object lock;

        public ThreadA(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    lock.wait();
                    count = count + 10;
                    showToast(count + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ThreadB extends Thread {
        Object lock;

        public ThreadB(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                lock.notify();
                count = count + 10;
            }
        }
    }

    private class Runner implements Runnable {
        int i;

        public Runner(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            msg.arg1 = i;
            mHandler.sendMessage(msg);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    count = msg.arg1;
                    showToast(count + "");
                    break;
            }
        }
    };

}
