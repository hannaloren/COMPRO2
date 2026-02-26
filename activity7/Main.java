package com.phonebook;

import com.phonebook.models.*;
import com.phonebook.services.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filename = "data/sample_contacts.csv";
        System.out.println("Welcome to the Phonebook Application!");
        PhonebookService phonebook = new PhonebookService();
        phonebook.LoadFromCSV(filename);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add");
            System.out.println("2. Search");
            System.out.println("3. Remove");
            System.out.println("4. Display All");
            System.out.println("5. Save to CSV");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 0:
                    System.out.println("Exiting application. Goodbye!");
                    sc.close();
                    return;
                case 1:
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Phone Number: ");
                    String phoneNumber = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    Contact newContact = new Contact(name, phoneNumber, email);
                    phonebook.addContact(newContact);
                    break;
                case 2:
                    System.out.print("Enter name to search: ");
                    String searchName = sc.nextLine();
                    phonebook.searchContact(searchName);
                    break;
                case 3:
                    System.out.print("Enter name to remove: ");
                    String removeName = sc.nextLine();
                    phonebook.removeContact(removeName);
                    break;
                case 4:
                    System.out.println("All Contacts:");
                    phonebook.getContacts();
                    break;
                case 5:
                    try {
                        phonebook.saveToCSV(filename);
                    } catch (Exception e) {
                        System.out.println("Error saving contacts: " + e.getMessage());
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

    }
}
