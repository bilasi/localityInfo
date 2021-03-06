package application;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.WordUtils;

import domain.Apartment;
import domain.Building;
import domain.Locality;
import domain.Person;
import domain.Room;
import service.DataOperation;
import service.FileOperation;

public class Starter {
	
	private static FileOperation fileService= new FileOperation();
	private static DataOperation dataService=new DataOperation();
	
	public static void main(String[] args) throws IOException {
		
		
		mainMenu();
		
	}
	
	public static void appInfo(){
		System.out.println("This program generates locality buildings and tenant info with random data \nYou can also search information based on tenant name, phone number."
				+ "\nFirst type 'init' command to initalize the program, this will populate random data");
	}
	
	public static void dataOperationInfo(){
		System.out.println("Data inited already exists on the file localityData.txt. If you want reintiate then please choose '0'.\n"
				+ "For data operation choose menu number from following list:\n1.Get Overview information of locality."
				+ "\n2.Search tenant with full Name.\n3.Search tenant with first or last Name\n4."
				+ "Search tenant with phone number.\n5.Get building overview information."
				+ "\n6.Edit tenant information.\n7.Remove tenant.\n8.Add new tenant to empty rooms.");
	}
	
	public static void mainMenu() throws IOException{
		
		Scanner scanner = new Scanner(System.in);
		
		if(fileService.checkFile()){
			dataOperationInfo();
			System.out.println("Choose Menu: ");
			int menuNo =scanner.nextInt();
			scanner.nextLine();
			switch(menuNo){
			case 0:new Starter().init();
			case 1: dataService.localityOverview();;
					break;
			case 2:System.out.println("Enter tenant full Name:");
					String fullName=scanner.nextLine();
					
					Person searchedPerson=dataService.searchTenantByName(fullName);
					System.out.println(searchedPerson.toString());
					break;
			case 3:System.out.println("Enter first or last name:");
					String name=scanner.nextLine();
					dataService.searchTenantByLnameFname(name);
					break;
			case 4:System.out.println("Enter phone number:");
					long phone=scanner.nextLong();
					dataService.searchTenantByPhone(phone);
					break;
			case 5:System.out.println("Enter block Letter: ");
					char block=(scanner.nextLine()).charAt(0);
					dataService.buildingOverview(block);
					break;
			case 6:System.out.println("Enter tenant full Name:");
					String tenantName=scanner.nextLine();
					Person tenant=dataService.searchTenantByName(tenantName);
					
					if(tenant!=null){
						System.out.println("Enter tenant new full Name:");
						String newName=scanner.nextLine();
						System.out.println("Enter tenant new phone:");
						long newPhone=scanner.nextLong();
						dataService.updateTenantInfo(tenantName, newName, newPhone);
					}
			case 7:System.out.println("Enter tenant full Name:");
					String tenantFullName=scanner.nextLine();
					Person person=dataService.searchTenantByName(tenantFullName);
					if(person!=null){
						dataService.removeTenant(person);
					}
			case 8:System.out.println("List of available rooms for tenants, please select one");
					dataService.listEmptyRooms();
					System.out.println("Enter your selection: ");
					String adds=scanner.nextLine();
					System.out.println("Enter tenant full name:");
					String personName=scanner.nextLine();
					System.out.println("Enter phone number");
					long phoneNumber=scanner.nextLong();
					dataService.addTenant(adds, personName, phoneNumber);
				
			}
			scanner.close();
		}
		else{
			appInfo();
			new Starter().init();
		}
	}
	
	public void init() throws IOException {
		boolean commandCorrect=false;
		Scanner scanner = new Scanner(System.in);
		
		while(commandCorrect!=true){
			System.out.println("Enter Command: ");
			String command = scanner.nextLine();
			if("init".equalsIgnoreCase(command)){
				commandCorrect=true;
			}
			else if("back".equalsIgnoreCase(command)){
				mainMenu();
			}
			else{ 
				System.out.println("Please Enter Correct Command!!!\nEnter 'init' for intalization.");
			}
		}
		scanner.close();
		
		String localityName="Siltakuja 2";
		int totalBuildings=5;
		char[] alphabets="ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(); //for buildings block name
		
		Locality locality=new Locality();
		locality.setAddress(localityName);;
		locality.setId(01);
		
		List<Building> buildings = new ArrayList<Building>();
		List<Apartment> apartments=new ArrayList<Apartment>();
		List<Room> rooms=new ArrayList<Room>();
		
		for(int i=0; i<totalBuildings; i++)
		{
			Building building=new Building();
			building.setBlock(alphabets[i]);
			int apartmentCount=ThreadLocalRandom.current().nextInt(15, 50); //generates random apartment number
			building.setTotalApartment(apartmentCount);
			
			for(int j=1; j<=apartmentCount;j++)
			{
				Apartment apartment=new Apartment();
				apartment.setApartmentNo(j);
				//System.out.println("i inside second for loop "+i);
				apartment.setApartmentAdds(localityName + " "+ alphabets[i]+" "+ j);
				int roomCount=ThreadLocalRandom.current().nextInt(1, 3+1); //generates random room number
				apartment.setTotalRoom(roomCount);
				
				for(int k=1;k<=roomCount;k++)
				{
					
					Room room=new Room();
					Person person=new Person();
					
					int FnameLength=ThreadLocalRandom.current().nextInt(5, 13);
					int LnameLength=ThreadLocalRandom.current().nextInt(5, 13);
					String firstName=WordUtils.capitalizeFully(RandomStringUtils.randomAlphabetic(FnameLength));
					String lastName=WordUtils.capitalizeFully(RandomStringUtils.randomAlphabetic(LnameLength));
					long phoneNumber = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
					String adds=localityName + " "+ alphabets[i]+" "+ j;
					
					person.setFirstName(firstName);
					person.setLastName(lastName);
					person.setPhone(phoneNumber);
					person.setAdds(adds);
					
					room.setRoomNo(k);
					room.setPerson(person);
					rooms.add(room);
				}
				apartment.setRooms(rooms);
				//System.out.print(apartment.toString());
				rooms=new ArrayList<Room>();
				apartments.add(apartment);
				
			}
			building.setApartments(apartments);
			//System.out.print(building.toString());
			apartments=new ArrayList<Apartment>();
			buildings.add(building);
		}
		locality.setBuildings(buildings);
		fileService.saveWithCustomFormat(locality);
	}
	
}
