package fi.utu.tech.ThreadRunner.dispatchers;

import java.util.List;

import fi.utu.tech.ThreadRunner.workers.Worker;
import fi.utu.tech.ThreadRunner.workers.WorkerFactory;

public class ThreadWork implements Runnable {
    private List<Integer> runtimes;
    private String workerType;

    public static void main(String[] args) {
       
        }

    //Run-metodi laskee, kuinka kauan säie tekee työtä. Aika määräytyy työlistasta jaetun osalistan arvojen mukaan.
    public void run(){
    	Worker worker = null;
		try {
			worker = WorkerFactory.createWorker(workerType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int time : runtimes) {
			try {
				worker.count(time);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }

    //Konstruktori
    public ThreadWork(List<Integer> runtimes, String workerType) {
    	this.runtimes=runtimes;
    	this.workerType=workerType;
        
    }
}