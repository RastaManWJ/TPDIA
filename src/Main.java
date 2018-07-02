import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

public class Main {

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public static void main(String[] args) {
		
		//Listy zbiornik�w i dyspozytor�w
		Nozzle nozzle1 = new Nozzle(0);
		List<Nozzle> nozzleList = new ArrayList<Nozzle>();
		nozzleList.add(nozzle1);
		List<Tank> tankList = new ArrayList<Tank>();
		Tank tank1 = new Tank(0, nozzleList, 30000, 27000, 25, 0);
		tankList.add(tank1);
		

		
		//D�ugo�� symulacji	
		Date currentDate = new Date();
		Calendar currentDateSet = Calendar.getInstance();
		Date endDate = new Date();
		Calendar endDateSet = Calendar.getInstance();
		endDateSet.setTime(endDate);
		endDateSet.add(Calendar.DATE, 7);
		endDate = endDateSet.getTime();
		
		//Zmienne wykorzystywane do symulacji
		boolean isFueling = false;
		boolean alreadyArrived = false;
		double fuelNeeded = -1;
		int customerArrivingTime = -1;
		int fuelingTime = -1;
		int whichTank = -1;
		int whichNozzle = -1;
		
		while(currentDate.compareTo(endDate) < 0) {
			//Losowanie czasu oczekiwania na klienta
			if (!isFueling && !alreadyArrived) {
				customerArrivingTime = customerArrivingTime();
				alreadyArrived = true;
			} else if (alreadyArrived && !isFueling) {
				fuelNeeded = fuelNeeded();
				fuelingTime = fuelingTime(fuelNeeded);
				isFueling = true;
			}
			//Losowanie ilo�ci tankowanego paliwa
			if (alreadyArrived && isFueling && customerArrivingTime <= 0 && fuelingTime >= 0) {
				if (whichTank == -1 && whichNozzle == -1) {
					whichTank = drawTank();
					whichNozzle = drawNozzle();
				}
				//Zmiana currentVolume dla wylosowanego tanka
			}
			
			//Zatrzymujemy dekrementacje na warto�ciach -1
			if (customerArrivingTime >= 0) {
				--customerArrivingTime;
			}
			if (fuelingTime >= 0) {
				--fuelingTime;
			}
			
			//Wyniki symulacji co "minut�"
			currentDateSet.setTime(currentDate);
			currentDateSet.add(Calendar.MINUTE, 1);
			currentDate = currentDateSet.getTime();
		}

		
		
	}
	
	//Losowanie ile klient chce kupi� paliwa
	public static double fuelNeeded() {
		return 0;
	}
	//Losowanie z kt�rego zbiornika chcemy zatankowa�
	public static int drawTank() {
		return 0;
	}	
	//Losowanie kt�ry dystrybutor zostanie wykorzystany
	public static int drawNozzle() {
		return 0;
	}
	//Obliczanie ile iteracji musi przej�� do sko�czenia tankowania na podstawie wylosowanej warto�ci "fuelNeeded"
	public static int fuelingTime(double fuelNeeded) {
		return 0;
	}
	//Losowanie czasu oczekiwania na klienta
	public static int customerArrivingTime() {
		return 0;
	}

}
