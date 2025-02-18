import java.util.InputMismatchException;
import java.util.Scanner;

public class w2052039_PlaneManagement {
    private static char[][] seatArray; //Seat array
    private static final char[] rows = {'A', 'B', 'C', 'D'}; //Seat row array
    private static Ticket[] ticketArray = new Ticket[52]; //Ticket array
    private static int ticketCount = 0; //Initialized ticket count

    public static void main (String[] args) {
        seatArray = seat_array();
        Scanner input = new Scanner(System.in);
        printMenu();
        while (true) {
            try {
                System.out.print("Please select an option: ");
                int userInput = input.nextInt(); //Get the user input

                if (userInput == 0) {
                    System.out.println("Thank you!");
                    break; // Exit the loop & end the program
                } else {
                    option(userInput, input);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Menu number. Please enter a valid menu number.");
                // Clear the input buffer
                input.next();

            }
        }


    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Welcome to the Plane Management application");
        System.out.println();
        String stars = "*".repeat(50);
        String space = " ".repeat(18);
        System.out.println(stars);
        System.out.println("*" + space + " Menu Option" + space + "*");
        System.out.println(stars);
        System.out.println("1) Buy a seat");
        System.out.println("2) Cancel a seat");
        System.out.println("3) Find first available seat");
        System.out.println("4) Show seating plan");
        System.out.println("5) Print ticket information and total sales");
        System.out.println("6) Search ticket");
        System.out.println("0) Quit");
        System.out.println(stars);
    }

    private static void option(int userInput, Scanner input) {
        switch (userInput) {
            case 1:
                buy_seat(input, seatArray, rows);
                break;
            case 2:
                cancel_seat(input, seatArray, rows);
                break;
            case 3:
                find_first_available(seatArray, rows);
                break;
            case 4:
                show_seating_plan(seatArray, rows);
                break;
            case 5:
                print_tickets_info();
                break;
            case 6:
                search_ticket();
                break;
        }
    }
    private static char[][] seat_array() {
        // Initialize the seating plan
        char[][] seatArray = new char[rows.length][14];

        // Remove B13, B14, C13, C14
        seatArray[1] = new char[12];
        seatArray[2] = new char[12];

        // Initialize all other seats
        for (int i = 0; i < seatArray.length; i++) {
            for (int j = 0; j < seatArray[i].length; j++) {
                seatArray[i][j] = 'O'; // 'O' represents an empty seat
            }
        }

        // Return the initialized seating plan
        return seatArray;
    }



    private static void buy_seat(Scanner input, char[][] seat, char[] rows) {
        System.out.println("Enter 'X' to exit the buy a seat option"); //if the user wants to exit before entering data
        while (true) {
            System.out.println();
            System.out.print("Enter a row letter and Seat number: ");
            String buying_seat = input.next().toUpperCase(); //Get seat row letter and number as input
            if (buying_seat.equals("X")){    //Exit if condition
                break;

            }
            if (buying_seat.length() >= 2) {
                char rowChar = buying_seat.charAt(0); //Getting the row letter
                String seatNumberStr = buying_seat.substring(1); //Getting seat number as a string

                try {
                    int column = Integer.parseInt(seatNumberStr);  //Converting seat number to integer

                    int rowIndex = -1; // set -1 as value because in array, index starts form 0
                    //This for loop is used to get the corresponding to row letter
                    for (int i = 0; i < rows.length; i++) {
                        if (rows[i] == rowChar) {
                            rowIndex = i;
                            break;
                        }
                    }

                    if (rowIndex != -1 && column >= 1 && column <= seat[rowIndex].length) {
                        seat[rowIndex][column - 1] = 'X';               //Updating booked seat as X in array
                        System.out.println("Updated seat: " + rowChar + column);  //Printing booked seat

                        Person person = createPerson();//Create a Person object
                        System.out.println("Personal information saved");
                        double ticketPrice = calculateTicketPrice(buying_seat); // Assuming buying_seat is a valid seat and calculate the ticket price

                        // Create a new Ticket and add it to the ticketArray
                        Ticket ticket = new Ticket(rowChar, column, ticketPrice, person);
                        if (ticketCount < ticketArray.length) {
                            ticketArray[ticketCount] = ticket;
                            ticketCount++;
                            System.out.println("Ticket purchased successfully!");
                            ticket.save();
                            break;
                        } else {
                            System.out.println("Ticket limit reached. Unable to purchase.");
                        }

                    } else {
                        System.out.println("Invalid input. Seat not updated.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid seat number. Please enter a valid seat number.");
                }
            } else {
                System.out.println("Invalid input. Please enter a row letter and seat number.");
            }
        }
    }
    private static void cancel_seat(Scanner input, char[][] seat, char[] rows) {
        System.out.println("Enter 'X' to exit the buy a seat option"); //if the user wants to exit before entering data
        while (true) {
            try {
                System.out.println();
                System.out.print("Enter a row letter and Seat number to cancel: ");
                String canceling_seat = input.next().toUpperCase(); //Get seat row letter and number as input
                if (canceling_seat.equals("X")){   //Exit if condition
                    break;
                }
                if (canceling_seat.length() >= 2) {
                    char rowChar = canceling_seat.charAt(0); //Getting the row letter
                    String seatNumberStr = canceling_seat.substring(1); //Getting seat number as a string

                    int column = Integer.parseInt(seatNumberStr); //Converting seat number to integer

                    int rowIndex = -1; // set -1 as value because in array, index starts form 0
                    //This for loop is used to get the corresponding to row letter
                    for (int i = 0; i < rows.length; i++) {
                        if (rows[i] == rowChar) {
                            rowIndex = i;
                            break;
                        }
                    }
                    if (rowIndex != -1 && column >= 1 && column <= seat[rowIndex].length) {
                        if (seat[rowIndex][column - 1] == 'X') {
                            seat[rowIndex][column - 1] = 'O'; // Setting value as 0 according to user input
                            System.out.println("Cancelled seat " + rowChar + column);
                            for (int i = 0; i < ticketCount; i++) {
                                if (ticketArray[i].getRow() == rowChar && ticketArray[i].getSeat() == column) {
                                    // Shift elements to remove the ticket
                                    for (int j = i; j < ticketCount - 1; j++) {
                                        ticketArray[j] = ticketArray[j + 1];
                                    }
                                    ticketCount--;
                                    System.out.println("Cancelled ticket for seat " + rowChar + column);
                                    return;
                                }
                            }
                            break;
                        } else {
                            System.out.println("Seat is not booked. Cannot cancel.");
                        }
                    } else {
                        System.out.println("Invalid input. Seat not cancelled.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a row letter and seat number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid seat number. Please enter a valid seat number.");
            }
        }
    }
    private static void find_first_available(char[][] seat, char[] rows) {
        System.out.println("First available seats:");

        for (char row : rows) {
            int rowIndex = findRowIndex(row); //getting row using findRowIndex method
            if (rowIndex != -1) {
                int columnIndex = findFirstAvailableSeat(seat[rowIndex]); //getting row using findFirstAvailableSeat method
                if (columnIndex != -1) {
                    System.out.println(row + String.valueOf(columnIndex + 1));
                    return; // Only print the first available seat
                }
            }
        }

        System.out.println("No available seats found.");
    }

    //Using this for loop for iterates through each row
    private static int findRowIndex(char row) {
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == row) {
                return i;
            }
        }
        return -1; // Row not found
    }
    //Using this for loop for iterates through every seats
    private static int findFirstAvailableSeat(char[] seats) {
        for (int i = 0; i < seats.length; i++) {
            if (seats[i] == 'O') {
                return i;
            }
        }
        return -1; // No available seat in this row
    }
    private static void show_seating_plan(char[][] seat, char[] rows) {
        for (int i = 0; i < seat.length; i++) {                 //Using nested for loop for print seating plan
            for (int j = 0; j < seat[i].length; j++) {
                System.out.print(seat[i][j] + " ");
            }
            System.out.println(); // Move to the next line after printing each row

            // Insert a blank line after row B
            if (rows[i] == 'B') {
                System.out.println();
            }
        }
    }

    private static void print_tickets_info() {
        double totalAmount = 0; // Initialize total amount variable

        for (int i = 0; i < ticketCount; i++) {
            Ticket ticket = ticketArray[i]; // Get current ticket object
            ticket.printTicketInformation(); // Print details of the current ticket
            totalAmount += ticket.getPrice(); // Add ticket price to total amount
        }

        System.out.println("Total Amount for all tickets: £" + totalAmount);
    }
    private static double calculateTicketPrice(String seat) {
        // Extract row letter and seat number from the seat location
        char row = seat.charAt(0);
        int seatNumber = Integer.parseInt(seat.substring(1));
        // Determine ticket price based on row and seat number
        if ((row >= 'A' && row <= 'D') && (seatNumber >= 1 && seatNumber <= 5)) {
            return 200.0;
        }
        else if ((row >= 'A' && row <= 'D') && (seatNumber >= 6 && seatNumber <= 9)) {
            return 150.0;
        }
        else if ((row >= 'A' && row <= 'D') && (seatNumber >= 10 && seatNumber <= 14)) {
            return 180.0;
        }
        else {
            System.out.println("Invalid seat. Unable to calculate ticket price.");
            return 0.0; // Invalid seat, return 0.0 as price
        }
    }
    private static Person createPerson() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter person's name: ");
        String name = input.next();

        System.out.print("Enter person's surname: ");
        String surname = input.next();

        System.out.print("Enter person's email: ");
        String email = input.next();

        // Create a new Person object with the entered information
        return new Person(name, surname, email);
    }
    private static void search_ticket() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter a row letter and Seat number to search: ");
        String searching_seat = input.next().toUpperCase();
        // Extract row letter and seat number from the seat location
        char rowChar = searching_seat.charAt(0);
        String seatNumberStr = searching_seat.substring(1);

        try {
            int column = Integer.parseInt(seatNumberStr);

            // Search for the ticket in the ticketArray
            for (int i = 0; i < ticketCount; i++) {
                Ticket ticket = ticketArray[i];
                if (ticket.getRow() == rowChar && ticket.getSeat() == column) {
                    // Ticket found, print information
                    ticket.printTicketInformation();
                    return;
                }
            }

            // If no ticket found, display message
            System.out.println("This seat is available.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid seat number. Please enter a valid seat number.");
        }
    }

}



