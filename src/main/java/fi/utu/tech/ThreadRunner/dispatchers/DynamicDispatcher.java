package fi.utu.tech.ThreadRunner.dispatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import fi.utu.tech.ThreadRunner.tasks.Countable;
import fi.utu.tech.ThreadRunner.tasks.TaskFactory;

/*
 * Luokka, jossa toteutetaan dynaaminen tehtävien jako ts. työn tehtävä 2
 * 
* @version     1.0                 
* @since       1.0          
*/

public class DynamicDispatcher implements Dispatcher {

	/**
	 * Metodi, jossa on toteutetaan dynaamisen tehtäväjaon toiminnallisuus.
	 *
	 *
	 * @param ControlSet Käyttöliittymässä asetettu arvot välittyvät tässä oliossa
	 * @return void
	 * 
	 */
	public void dispatch(ControlSet controlSet) {
		try {
			// työtehtävä
			Countable co = TaskFactory.createTask(controlSet.getTaskType());
			ArrayList<Integer> ilist = co.generate(controlSet.getAmountTasks(), controlSet.getMaxTime());

			//työ
			String workerType = controlSet.getWorkerType();

			//Jaetaan kokonaistyömäärä säikeiden lukumäärää vastaaviin osiin.
			int tyomaaraOsa = ilist.size()/(controlSet.getThreadCount()*10);
			
			//Tehdään tehtävälista ThreadPoolia varten.
			int s = 0;
			ArrayList<ThreadWork> tehtavalista = new ArrayList<ThreadWork>();
			for (int i=0; i<controlSet.getThreadCount(); i++, s+=tyomaaraOsa) {
				List<Integer> tyomaara = ilist.subList(s, s+=tyomaaraOsa);
				
				ThreadWork alustus = new ThreadWork(tyomaara, workerType);
				tehtavalista.add(alustus);
				 
			}
			
			//Luodaan ThreadPool koneen säikeiden määrällä.
			ExecutorService pool = Executors.newFixedThreadPool(controlSet.getThreadCount());
			
			//Käynnistetään säikeet.
			for (int i=0; i<tehtavalista.size(); i++) {
				pool.execute(tehtavalista.get(i));
			}
			
			//Suljetaan allas.
			pool.shutdown();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}

}
