package facadePattern;


import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HotelApp {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        FrontDesk receptionist = new FrontDesk();

        System.out.println("Welcome to Hotel 626!");

        boolean loop = true;

        while (loop) {

            try {
                System.out.println("Type in the service you need:\n\t[1] Valet\n\t[2] Housekeeping\n\t[3] Cart\n\t[4] Exit");
                System.out.print("Type the number or text: ");
                String serviceInput = input.next().toLowerCase();  
                int service = 0;

                switch (serviceInput) {
                    case "1":
                    case "valet":
                        service = 1;
                        break;
                    case "2":
                    case "housekeeping":
                        service = 2;
                        break;
                    case "3":
                    case "cart":
                        service = 3;
                        break;
                    case "4":
                    case "exit":
                        service = 4;
                        break;
                    default:
                        throw new InputMismatchException("Invalid input! Please enter the choices below only.");
                }

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> {
                    System.out.print("");
                    for (int i = 0; i < 2; i++) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                            System.out.print(".");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println();
                });
                executor.shutdown();
                executor.awaitTermination(5, TimeUnit.SECONDS); 

                switch (service) {
                    case 1:
                        System.out.print("Enter your plate number for pickup.: ");
                        String plateNumber = input.next();
                        Valet.setPlateNumber(plateNumber);
                        HotelService valet = new Valet(plateNumber);
                        FrontDesk.clientService(valet);
                        break;
                    case 2:
                        System.out.print("Enter your room number for clean up,: ");
                        String roomNumber = input.next();
                        HouseKeeping.setRoomNumber(roomNumber);
                        HotelService housekeeping = new HouseKeeping(roomNumber);
                        FrontDesk.clientService(housekeeping);
                        break;
                    case 3:
                        System.out.print("Enter the number of your luggages for cart request(s)?: ");
                        int numberOfCarts = input.nextInt();
                        Cart.setNumberOfLuggages(numberOfCarts);
                        HotelService cart = new Cart(numberOfCarts);
                        FrontDesk.clientService(cart);
                        break;
                    case 4:
                        System.out.println("Thank you, next.");
                        loop = false;
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Error: " + e.getMessage());
                input.nextLine(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Loading was interrupted.");
            }
        }

        input.close();
    }
}
