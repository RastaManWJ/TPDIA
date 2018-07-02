import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

public class Main {

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public static void main(String[] args) {
		Nozzle nozzle1 = new Nozzle(0);
		List<Nozzle> nozzleList = new ArrayList<Nozzle>();
		nozzleList.add(nozzle1);
		Tank tank1 = new Tank(0, nozzleList, 30000, 27000, 25, 0);
		
		Date currentDate = new Date();
		Calendar currentDateSet = Calendar.getInstance();
		
		//D³ugoœæ symulacji
		Date endDate = new Date();
		Calendar endDateSet = Calendar.getInstance();
		endDateSet.setTime(endDate);
		endDateSet.add(Calendar.DATE, 7);
		endDate = endDateSet.getTime();
		
		boolean isFueling = false;
		boolean alreadyArrived = false;
		int customerArrivingTime = 0;
		int fuelingTime = 0;
		
		while(currentDate.compareTo(endDate) < 0) {
			//Losowanie czasu oczekiwania na klienta
			if (!isFueling && !alreadyArrived) {
				customerArrivingTime = customerArrivingTime();
				alreadyArrived = true;
			} else if (alreadyArrived && !isFueling) {
				fuelingTime = fuelingTime();
				isFueling = true;
			}
			//Losowanie iloœci tankowanego paliwa
			
			
			--customerArrivingTime;
			--fuelingTime;
			currentDateSet.setTime(currentDate);
			currentDateSet.add(Calendar.MINUTE, 1);
			currentDate = currentDateSet.getTime();
		}

		
		
	}
	
	public static int fuelingTime() {
		return 0;
	}
	public static int customerArrivingTime() {
		return 0;
	}

}
