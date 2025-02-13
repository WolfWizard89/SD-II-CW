import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

public class Ticket {
    private char row;
    private int seat;
    private double price;
    private Person person;

    public Ticket(char row, int seat, double price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    public char getRow() {
        return row;
    }

    public void setRow(char row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void printTicketInformation() {
        System.out.println("Ticket Information:");
        System.out.println("Row: " + row);
        System.out.println("Seat: " + seat);
        System.out.println("Price: £" + price);
        if (person != null) {
            System.out.println("Person Information:");
            System.out.println("Name: " + person.getName());
            System.out.println("Surname: " + person.getSurname());
            System.out.println("Email: " + person.getEmail());
        } else {
            System.out.println("No person information available.");
        }
    }

    public void save() {
        String fileName = row + String.valueOf(seat) + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Ticket Information:");
            writer.println("Row: " + row);
            writer.println("Seat: " + seat);
            writer.println("Price: $" + price);

            if (person != null) {
                writer.println("Person Information:");
                writer.println("Name: " + person.getName());
                writer.println("Surname: " + person.getSurname());
                writer.println("Email: " + person.getEmail());
            } else {
                writer.println("No person information available.");
            }

            System.out.println("Ticket information saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving ticket information to file.");
            e.printStackTrace();
        }
    }

}
