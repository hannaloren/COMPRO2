package com.phonebook.models;

import java.util.Scanner;

public class Contact {
    private String name;
    private String phoneNumber;
    private String email;

    static Scanner sc = new Scanner(System.in);

    public Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // GETTERS
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    // SETTERS
    public void setName(String name) {
        System.out.print("Name: ");
        name = sc.nextLine();
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        System.out.print("Phone Number: ");
        phoneNumber = sc.nextLine();
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        System.out.print("Email: ");
        email = sc.nextLine();
        this.email = email;
    }

    // METHOD
    public String toCsvString() {
        return String.format("%s,%s,%s", name, phoneNumber, email);
    }

}
