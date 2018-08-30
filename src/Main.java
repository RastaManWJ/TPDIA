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
	//Paliwo kurcyz siê albo rozszerza o 0.12% * volume na ka¿dy stopieñ celsjusza (a diesel 0.08%)
	//Zmiana temp tanka wzglêdem outside to 70%
	public static void main(String[] args) throws FileNotFoundException {
		//Tworzenie plików
		PrintWriter tankMeasures = new PrintWriter("tankMeasures.txt");
		PrintWriter nozzleMeasures = new PrintWriter("nozzleMeasures.txt");
		PrintWriter refuel = new PrintWriter("refuel.txt");
		
		//Listy zbiorników i dyspozytorów i cystern dla zbiorników
		//Ka¿da stacja ma nozzleAmount dystrybutorów
		int nozzleAmount = 4;
		List<Nozzle> nozzleList1 = new ArrayList<Nozzle>();
		for (int i = 0; i < nozzleAmount; ++i) {
			nozzleList1.add(new Nozzle(i));
		}
		List<Nozzle> nozzleList2 = new ArrayList<Nozzle>();
		for (int i = 0; i < nozzleAmount; ++i) {
			nozzleList2.add(new Nozzle(i));
		}
		List<Nozzle> nozzleList3 = new ArrayList<Nozzle>();
		for (int i = 0; i < nozzleAmount; ++i) {
			nozzleList3.add(new Nozzle(i));
		}
		List<Nozzle> nozzleList4 = new ArrayList<Nozzle>();
		for (int i = 0; i < nozzleAmount; ++i) {
			nozzleList4.add(new Nozzle(i));
		}

		
		//Stworzenie listy pustych cystern w iloœci równej iloœci zbiorników
		List<Cistern> cisternList = new ArrayList<Cistern>();

		
		//D³ugoœæ symulacji	
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
		double customerProb = 0.01; 				//Prawdopodobieñstwo pojawienia siê klienta
		int fuelMin = 10;						//Minimum przedzia³u iloœci tankowanego paliwa
		int fuelMax = 100;						//Maximum przedzia³u iloœci tankowanego paliwa
		int fuelingSpeed = 15;					//Szybkoœæ tankowania w litrach na minutê
		
		//Lista klientów dla ka¿dego zbiornika
		List<Customer> customerList1 = new ArrayList<Customer>();
		List<Customer> customerList2 = new ArrayList<Customer>();
		List<Customer> customerList3 = new ArrayList<Customer>();
		List<Customer> customerList4 = new ArrayList<Customer>();
		
		double refuelingTankSpeed = 333.3333333;//Szybkoœæ zape³niania zbiornika paliwa przez cysternê
		double supplyFuelTreshold = 0.1;		//Próg dostaw paliwa
		
		//Temperatury symulacji
		double startAvgDayTemp = 16;
		double simDayTemp = calculateDayTemp(currentDateSet.get(Calendar.HOUR_OF_DAY) + (currentDateSet.get(Calendar.MINUTE)/60), startAvgDayTemp);
		double simTankTemp = simDayTemp * 0.7;
		
		//Lista zbiorników z manualnie wprowadzanymi danymi
		Tank tank1 = new Tank(0, nozzleList1, 30000, 3000, simTankTemp, 0, customerList1); //Dodaæ listê customerów dla danego tanka
		Tank tank2 = new Tank(1, nozzleList2, 30000, 27000, simTankTemp, 0, customerList2);
		Tank tank3 = new Tank(2, nozzleList3, 30000, 27000, simTankTemp, 0, customerList3);
		Tank tank4 = new Tank(3, nozzleList4, 30000, 27000, simTankTemp, 0, customerList4);
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
				//Edycja plików wyjœciowych
				tankMeasures.println(dateFormat.format(currentDate) + ";" + t.get_ID() + ";" + t.get_height() + ";" + t.get_volumeCurrent() + ";" + t.get_temperature());
				for (Nozzle n : t.get_nozzleList()) {
					nozzleMeasures.println(dateFormat.format(currentDate) + ";" + n.get_ID() + ";" + t.get_ID() + ";" + n.get_transactionCurrent() + ";" + n.get_transactionTotal() + ";" + n.get_isUsed());
				}
	
				//Losowanie klienta je¿eli jakiœ dystrybutor jest wolny
				if (t.get_customerList().size() < t.get_nozzleList().size()) {
					if (willCustomerArrive(customerProb)) {
						fuelNeeded = fuelNeeded(fuelMin, fuelMax);
						fuelingTime = fuelingTime(fuelNeeded, fuelingSpeed);
						whichNozzle = drawNozzle(t.get_nozzleList());
						t.get_customerList().add(new Customer(whichNozzle, fuelNeeded, fuelingTime));
						t.get_nozzleList().get(whichNozzle).set_isUsed(true);
					}		
				}
				//Tankowanie paliwa przez obecnych klientów
				for(Customer x : t.get_customerList()) {
					double fuelPerMinute = x.get_fuelNeeded() / x.get_fuelingTime();							//Iloœæ paliwa wyci¹ganego ze zbiornika na minutê
					Nozzle n = t.get_nozzleList().get(x.get_nozzleUsed());						//Stworzenie kopii dystrybutora
					t.set_volumeCurrent(t.get_volumeCurrent() - fuelPerMinute);		//Wyssanie danej wartoœci ze zbiornika
					x.set_fuelNeeded(x.get_fuelNeeded() - fuelPerMinute);										//Aktualizacja zapotrzebowania na paliwo
					x.set_fuelingTime(x.get_fuelingTime() - 1);													//Dekrementacja iloœci iteracji tankowania
					n.set_transactionCurrent(n.get_transactionCurrent() + fuelPerMinute);						//Zmiana wartoœci aktualnie pobranego paliwa przez dystrybutor ze zbiornika
					t.get_nozzleList().set(x.get_nozzleUsed(), n);								//Nadpisanie elementu dystrybutora w liœcie
					//Po wykonaniu ostatniej iteracji tankowania
					if (x.get_fuelingTime() == 0) {
						n.set_transactionTotal(n.get_transactionTotal() + n.get_transactionCurrent());			//Dodanie koñcowego wyniku aktualnego tankowania do sumy tankowañ danego dystrybutora
						n.set_transactionCurrent(0);															//Wyzerowanie licznika aktualnej transakcji
						n.set_isUsed(false);																	//Zwolnienie dystrybutora
						t.get_nozzleList().set(x.get_nozzleUsed(), n);							//Nadpisanie elementu dystrybutora w liœcie
					}
				}
				t.get_customerList().removeIf(y->y.get_fuelingTime() == 0);												//Usuniêcie klienta z listy
				
				//Sprawdzenie czy dany zbiornik potrzebuje dostawy
				if (t.get_volumeCurrent() < t.get_volumeMax()*supplyFuelTreshold && !t.get_tankRefuelNeeded()) {
					t.set_tankRefuelNeeded(true);
					double refuelNeeded = t.get_volumeMax() * 0.8;
					cisternList.set(t.get_ID(), new Cistern(t.get_ID(), refuelNeeded, refuelNeeded, 25));
					refuel.println(dateFormat.format(currentDate) + ";" + t.get_ID() + ";" + refuelNeeded + ";" + refuelingTankSpeed);
				}
				
				//Nape³nianie zbiornika
				if (t.get_tankRefuelNeeded()) {
					Cistern c = cisternList.get(t.get_ID());
					if (c.get_volumeCurrent() >= refuelingTankSpeed) {
						t.set_volumeCurrent(t.get_volumeCurrent() + refuelingTankSpeed);
						c.set_volumeCurrent(c.get_volumeCurrent() - refuelingTankSpeed);
						//Zwiêkszanie i zmniejszanie objêtoœci paliwa pod wp³ywem zmiany temperatury
						double TempBefore = t.get_temperature();							
						t.set_temperature((t.get_volumeCurrent()*t.get_temperature() + refuelingTankSpeed * c.get_temperature())/(t.get_volumeCurrent() + refuelingTankSpeed)); //Nowa temperatura
						double TempAfter = t.get_temperature();
						t.set_volumeCurrent(t.get_volumeCurrent() + updateTankVolume(TempAfter - TempBefore, t.get_volumeCurrent()));
						cisternList.set(t.get_ID(), c);
					} else {
						//Zwiêkszanie i zmniejszanie objêtoœci paliwa pod wp³ywem zmiany temperatury
						double TempBefore = t.get_temperature();
						t.set_volumeCurrent(t.get_volumeCurrent() + c.get_volumeCurrent());
						t.set_temperature((t.get_volumeCurrent()*t.get_temperature() + c.get_volumeCurrent() * c.get_temperature())/(t.get_volumeCurrent() + c.get_volumeCurrent()));
						double TempAfter = t.get_temperature();
						t.set_volumeCurrent(t.get_volumeCurrent() + updateTankVolume(TempAfter - TempBefore, t.get_volumeCurrent()));
						cisternList.set(t.get_ID(), new Cistern());
						t.set_tankRefuelNeeded(false);
					}
				}
				
				//Zmiana temperatury i objêtoœci paliwa w zbiorniku ze wzglêdu na temperaturê otoczenia
				double TempBefore = t.get_temperature();
				t.set_temperature(changeTankTempByDay(t.get_temperature(), simTankTemp));
				double TempAfter = t.get_temperature();
				t.set_volumeCurrent(t.get_volumeCurrent() + updateTankVolume(TempAfter - TempBefore, t.get_volumeCurrent()));
			}
			//Przejœcie do kolejnej minuty symulacji
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
		}
		System.out.println("koniec");
		
		//Zamkniêcie plików
		tankMeasures.close();
		nozzleMeasures.close();
		refuel.close();
	}
	
	//Zmiana iloœci paliwa podczas nape³niania zbiornika
	public static double updateTankVolume(double temp, double tankVolume) {
		return temp*0.0012*tankVolume;
	}
	
	//Obliczanie temperatury w ci¹gu dnia
	public static double calculateDayTemp(int hourandminutes, double avg) {
		return 5*Math.sin(3.14/12*(hourandminutes-8))+ avg;
	}
	
	
	//Losowanie liczby z przedzia³u od 0 do 1, czy klient siê pojawi
	public static boolean willCustomerArrive(double p) {
		Random generator = new Random(System.nanoTime());
		if (generator.nextDouble() < p)
			return true;
		else
			return false;		
	}
	
	//Losowanie ile klient chce kupiæ paliwa
	public static double fuelNeeded(int fuelMin, int fuelMax) {
		Random generator = new Random(System.nanoTime());
		return fuelMin + (fuelMax - fuelMin) * generator.nextDouble();
	}
	
	//Obliczanie d³ugoœci (iteracji) tankowania
	public static int fuelingTime(double fuelNeeded, int fuelingSpeed) {
		return (int) Math.ceil(fuelNeeded/fuelingSpeed);
	}
	
	//Losowanie który dystrybutor zostanie wykorzystany
	public static int drawNozzle(List<Nozzle> nozzleList) {
		//Sprawdzanie czy dany Nozzle nie jest zajêty i losowanie
		Random generator = new Random(System.nanoTime());
		while (true) {
			int wantedNozzle = generator.nextInt(nozzleList.size());
			if(!nozzleList.get(wantedNozzle).get_isUsed()) {
				return wantedNozzle;
			}
		}
	}
	
	//Liniowa zmiana temperatury pod wzglêdem temperatury otoczenia
	public static double changeTankTempByDay(double currentTemp, double targetTemp) {
		double newTemp = 0;
		if((targetTemp - currentTemp) < 0) {
			newTemp = currentTemp - 0.0001;
		}else if((targetTemp - currentTemp) > 0) {
			newTemp = currentTemp + 0.0001;
		}else {
			newTemp = currentTemp;
		}
		return newTemp;
	}

}
