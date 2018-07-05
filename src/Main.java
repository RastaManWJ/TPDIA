import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Calendar;
import java.util.Date;

public class Main {

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	//Paliwo kurcyz si� albo rozszerza o 0.12% * volume na ka�dy stopie� celsjusza (a diesel 0.08%)
	//Zmiana temp tanka wzgl�dem outside to 70%
	public static void main(String[] args) throws FileNotFoundException {
		//Tworzenie plik�w
		PrintWriter tankMeasures = new PrintWriter("tankMeasures.txt");
		PrintWriter nozzleMeasures = new PrintWriter("nozzleMeasures.txt");
		PrintWriter refuel = new PrintWriter("refuel.txt");
		
		//Listy zbiornik�w i dyspozytor�w i cystern dla zbiornik�w
		//Ka�da stacja ma nozzleAmount dystrybutor�w
		int nozzleAmount = 4;
		List<Nozzle> nozzleList = new ArrayList<Nozzle>();
		for (int i = 0; i < nozzleAmount; ++i) {
			nozzleList.add(new Nozzle(i));
		}

		
		//Stworzenie listy pustych cystern w ilo�ci r�wnej ilo�ci zbiornik�w
		List<Cistern> cisternList = new ArrayList<Cistern>();

		
		//D�ugo�� symulacji	
		Date currentDate = new Date();
		Calendar currentDateSet = Calendar.getInstance();
		Date endDate = new Date();
		Calendar endDateSet = Calendar.getInstance();
		endDateSet.setTime(endDate);
		endDateSet.add(Calendar.DATE, 7);				//Czas trwania symulacji: 7 dni
		endDate = endDateSet.getTime();
		
		//Zmienne wykorzystywane do symulacji
		double fuelNeeded = -1;
		int fuelingTime = -1;
		int whichNozzle = -1;					
		double customerProb = 0.2; 				//Prawdopodobie�stwo pojawienia si� klienta
		int fuelMin = 10;						//Minimum przedzia�u ilo�ci tankowanego paliwa
		int fuelMax = 100;						//Maximum przedzia�u ilo�ci tankowanego paliwa
		int fuelingSpeed = 15;					//Szybko�� tankowania w litrach na minut�
		List<Customer> customerList = new ArrayList<Customer>();
		double refuelingTankSpeed = 333.3333333;//Szybko�� zape�niania zbiornika paliwa przez cystern�
		double supplyFuelTreshold = 0.1;		//Pr�g dostaw paliwa
		
		//Temperatury symulacji
		double startAvgDayTemp = 16;
		double simDayTemp = calculateDayTemp(currentDateSet.get(Calendar.HOUR_OF_DAY) + (currentDateSet.get(Calendar.MINUTE)/60), startAvgDayTemp);
		double simTankTemp = simDayTemp * 0.7;
		
		//Lista zbiornik�w z manualnie wprowadzanymi danymi
		Tank tank1 = new Tank(0, nozzleList, 30000, 3000, simTankTemp, 0);
		Tank tank2 = new Tank(1, nozzleList, 30000, 27000, simTankTemp, 0);
		Tank tank3 = new Tank(2, nozzleList, 30000, 27000, simTankTemp, 0);
		Tank tank4 = new Tank(3, nozzleList, 30000, 27000, simTankTemp, 0);
		List<Tank> tankList = new ArrayList<Tank>();
		tankList.add(tank1);
		tankList.add(tank2);
		tankList.add(tank3);
		tankList.add(tank4);
		
		for (int i = 0; i < tankList.size(); ++i) {
			cisternList.add(new Cistern());
		}
		
		
		
		while(currentDate.compareTo(endDate) < 0) {
			for (Tank t : tankList) {
				//Edycja plik�w wyj�ciowych
				tankMeasures.println(dateFormat.format(currentDate) + ";" + t.get_ID() + ";" + t.get_height() + ";" + t.get_volumeCurrent() + ";" + t.get_temperature());
				for (Nozzle n : t.get_nozzleList()) {
					nozzleMeasures.println(dateFormat.format(currentDate) + ";" + n.get_ID() + ";" + t.get_ID() + ";" + n.get_transactionCurrent() + ";" + n.get_transactionTotal() + ";" + n.get_isUsed());
				}
	
				//Losowanie klienta je�eli jaki� dystrybutor jest wolny
				if (customerList.size() < t.get_nozzleList().size() && !t.get_tankRefuelNeeded()) {
					if (willCustomerArrive(customerProb)) {
						fuelNeeded = fuelNeeded(fuelMin, fuelMax);
						fuelingTime = fuelingTime(fuelNeeded, fuelingSpeed);
						whichNozzle = drawNozzle(t.get_nozzleList());
						customerList.add(new Customer(whichNozzle, fuelNeeded, fuelingTime));
						t.get_nozzleList().get(whichNozzle).set_isUsed(true);
					}		
				}
				//Tankowanie paliwa przez obecnych klient�w
				for(Customer x : customerList) {
					double fuelPerMinute = x.get_fuelNeeded() / x.get_fuelingTime();							//Ilo�� paliwa wyci�ganego ze zbiornika na minut�
					Nozzle n = t.get_nozzleList().get(x.get_nozzleUsed());						//Stworzenie kopii dystrybutora
					t.set_volumeCurrent(t.get_volumeCurrent() - fuelPerMinute);		//Wyssanie danej warto�ci ze zbiornika
					x.set_fuelNeeded(x.get_fuelNeeded() - fuelPerMinute);										//Aktualizacja zapotrzebowania na paliwo
					x.set_fuelingTime(x.get_fuelingTime() - 1);													//Dekrementacja ilo�ci iteracji tankowania
					n.set_transactionCurrent(n.get_transactionCurrent() + fuelPerMinute);						//Zmiana warto�ci aktualnie pobranego paliwa przez dystrybutor ze zbiornika
					t.get_nozzleList().set(x.get_nozzleUsed(), n);								//Nadpisanie elementu dystrybutora w li�cie
					//Po wykonaniu ostatniej iteracji tankowania
					if (x.get_fuelingTime() == 0) {
						n.set_transactionTotal(n.get_transactionTotal() + n.get_transactionCurrent());			//Dodanie ko�cowego wyniku aktualnego tankowania do sumy tankowa� danego dystrybutora
						n.set_transactionCurrent(0);															//Wyzerowanie licznika aktualnej transakcji
						n.set_isUsed(false);																	//Zwolnienie dystrybutora
						t.get_nozzleList().set(x.get_nozzleUsed(), n);							//Nadpisanie elementu dystrybutora w li�cie
					}
				}
				customerList.removeIf(y->y.get_fuelingTime() == 0);												//Usuni�cie klienta z listy
				
				//Sprawdzenie czy dany zbiornik potrzebuje dostawy
				if (t.get_volumeCurrent() < t.get_volumeMax()*supplyFuelTreshold && !t.get_tankRefuelNeeded()) {
					t.set_tankRefuelNeeded(true);
					double refuelNeeded = t.get_volumeMax() * 0.8;
					cisternList.set(t.get_ID(), new Cistern(t.get_ID(), refuelNeeded, refuelNeeded, 25));
					refuel.println(dateFormat.format(currentDate) + ";" + t.get_ID() + ";" + refuelNeeded + ";" + refuelingTankSpeed);
				}
				
				//Nape�nianie zbiornika
				if (t.get_tankRefuelNeeded() && customerList.size() < 1) {
					Cistern c = cisternList.get(t.get_ID());
					if (c.get_volumeCurrent() >= refuelingTankSpeed) {
						t.set_volumeCurrent(t.get_volumeCurrent() + refuelingTankSpeed);
						c.set_volumeCurrent(c.get_volumeCurrent() - refuelingTankSpeed);
						double TempBefore = t.get_temperature();
						t.set_temperature((t.get_volumeCurrent()*t.get_temperature() + refuelingTankSpeed * c.get_temperature())/(t.get_volumeCurrent() + refuelingTankSpeed));
						double TempAfter = t.get_temperature();
						t.set_volumeCurrent(t.get_volumeCurrent() + updateTankVolume(TempAfter - TempBefore, t.get_volumeCurrent()));
						cisternList.set(t.get_ID(), c);
					} else {
						double TempBefore = t.get_temperature();
						t.set_volumeCurrent(t.get_volumeCurrent() + c.get_volumeCurrent());
						t.set_temperature((t.get_volumeCurrent()*t.get_temperature() + c.get_volumeCurrent() * c.get_temperature())/(t.get_volumeCurrent() + c.get_volumeCurrent()));
						double TempAfter = t.get_temperature();
						t.set_volumeCurrent(t.get_volumeCurrent() + updateTankVolume(TempAfter - TempBefore, t.get_volumeCurrent()));
						cisternList.set(t.get_ID(), new Cistern());
						t.set_tankRefuelNeeded(false);
					}
				}
			}
			//Przej�cie do kolejnej minuty symulacji
			int DayBefore = currentDateSet.get(Calendar.DATE);
			int HourBefore = currentDateSet.get(Calendar.HOUR);
			currentDateSet.setTime(currentDate);
			currentDateSet.add(Calendar.MINUTE, 1);
			int DayAfter = currentDateSet.get(Calendar.DATE);
			int HourAfter = currentDateSet.get(Calendar.HOUR);
			currentDate = currentDateSet.getTime();
			if (DayBefore < DayAfter) {
				Random generator1 = new Random(System.nanoTime());
				double number = -2 + 4 * generator1.nextDouble();
				startAvgDayTemp = startAvgDayTemp + number;
			}
			simDayTemp = calculateDayTemp(currentDateSet.get(Calendar.HOUR_OF_DAY) + (currentDateSet.get(Calendar.MINUTE)/60), startAvgDayTemp);
			simTankTemp = simDayTemp * 0.7;
			if (HourBefore < HourAfter) {
				refuel.println(simDayTemp);
			}
			//TO DO:
			//Zmiana obj�to�ci paliwa wzgl�dem temperatury otoczenia
		}
		System.out.println("koniec");
		
		//Zamkni�cie plik�w
		tankMeasures.close();
		nozzleMeasures.close();
		refuel.close();
	}
	
	//Zmiana ilo�ci paliwa podczas nape�niania zbiornika
	public static double updateTankVolume(double temp, double tankVolume) {
		return temp*0.0012*tankVolume;
	}
	
	//Obliczanie temperatury w ci�gu dnia
	public static double calculateDayTemp(int hourandminutes, double avg) {
		return 5*Math.sin(3.14/12*(hourandminutes-8))+ avg;
	}
	
	
	//Losowanie liczby z przedzia�u od 0 do 1, czy klient si� pojawi
	public static boolean willCustomerArrive(double p) {
		Random generator = new Random(System.nanoTime());
		if (generator.nextDouble() < p)
			return true;
		else
			return false;		
	}
	
	//Losowanie ile klient chce kupi� paliwa
	public static double fuelNeeded(int fuelMin, int fuelMax) {
		Random generator = new Random(System.nanoTime());
		return fuelMin + (fuelMax - fuelMin) * generator.nextDouble();
	}
	
	//Obliczanie d�ugo�ci (iteracji) tankowania
	public static int fuelingTime(double fuelNeeded, int fuelingSpeed) {
		return (int) Math.ceil(fuelNeeded/fuelingSpeed);
	}
	
	//TO DO:
	//Losowanie kt�ry dystrybutor zostanie wykorzystany
	public static int drawNozzle(List<Nozzle> nozzleList) {
		//Sprawdzanie czy dany Nozzle nie jest zaj�ty i losowanie
		Random generator = new Random(System.nanoTime());
		while (true) {
			int wantedNozzle = generator.nextInt(nozzleList.size());
			if(!nozzleList.get(wantedNozzle).get_isUsed()) {
				return wantedNozzle;
			}
		}
	}

}
