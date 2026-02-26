package com.phonebook.services;

import com.phonebook.models.Contact;
import java.io.*;
import java.util.*;

public class PhonebookService {
    private HashMap<String, Contact> contacts;

    // METHODS
    public void addContact(Contact c) {
        contacts = this.contacts == null ? new HashMap<>() : this.contacts;
        contacts.put(c.getName(), c);
    }

    public void searchContact(String name) {
        if (contacts.containsKey(name)) {
            Contact c = contacts.get(name);
            System.out.println("Name: " + c.getName());
            System.out.println("Phone Number: " + c.getPhoneNumber());
            System.out.println("Email: " + c.getEmail());
        } else {
            System.out.println("Contact not found.");
        }
    }

    public void removeContact(String name) {
        if (contacts.containsKey(name)) {
            contacts.remove(name);
            System.out.println("Contact removed.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    public void saveToCSV(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new PrintWriter(new File(filename)))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Name,Phone Number,Email\n");
            for (Contact c : contacts.values()) {
                sb.append(c.toCsvString()).append("\n");
            }
            writer.write(sb.toString());
            System.out.println("Contacts saved to " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    public void getContacts() {
        for (Contact c : contacts.values()) {
            System.out.println("Name: " + c.getName());
            System.out.println("Phone Number: " + c.getPhoneNumber());
            System.out.println("Email: " + c.getEmail());
            System.out.println();
        }
    }

    public void LoadFromCSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Contact c = new Contact(parts[0], parts[1], parts[2]);
                    addContact(c);
                }
            }
            System.out.println("Contacts loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }

}
