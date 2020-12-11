package fi.utu.tech.ThreadRunner.dispatchers;

import java.util.ArrayList;
import java.util.List;

import fi.utu.tech.ThreadRunner.tasks.Countable;
import fi.utu.tech.ThreadRunner.tasks.TaskFactory;
import fi.utu.tech.ThreadRunner.workers.WorkerFactory;

/*
 * Luokka, jossa toteutetaan staattinen tehtävien jako ts. työn tehtävä 1
 * 
* @version     1.0                 
* @since       1.0          
*/

public class StaticDispatcher implements Dispatcher {

	/**
	 * Metodi, jossa on toteutettu staattinen tehtäväjako toiminnallisuus.
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
			int tyomaaraOsa = ilist.size()/controlSet.getThreadCount();
			
			//Asetetaan jaetut työmääräosuudet säikeille.
			int s = 0;
			ArrayList<Thread> saielista = new ArrayList<Thread>();
			for (int i=0; i<controlSet.getThreadCount(); i++, s+=tyomaaraOsa) {
				List<Integer> tyomaara = ilist.subList(s, s+tyomaaraOsa);
				
				ThreadWork alustus = new ThreadWork(tyomaara, workerType);
				Thread säie = new Thread(alustus);
				saielista.add(säie);
				 
			}
			
			//Käynnistetään säikeet.
			for (int i=0; i<saielista.size(); i++) {
				saielista.get(i).start();
			}
			
			//Main-säie odottaa staattisen menetelmän säikeiden laskennan valmistumista.
			for (Thread t : saielista) {
				  t.join();
				}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}

}
