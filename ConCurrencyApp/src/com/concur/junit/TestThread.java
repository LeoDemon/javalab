/*
 * Test for java Thread concurrency programe
 *
 * @author: Demon.Lee
 * @date: 2016/9/26 17:08:18
 */
package com.concur.junit;

public class TestThread extends Thread{
    public TestThread(String thread_name){
        super(thread_name);
    }

    public void run(){
        for(int i=0; i<5; i++){
            for(int k=0; k<5; k++){
                System.out.println(this.getName()+":("+i+")");
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        Thread t1 = new TestThread("xiaotao");
        Thread t2 = new TestThread("wangwu");
        Thread t3 = new TestThread("ttran");
        t1.start();
        t2.start();
        t3.start();
    }
}

