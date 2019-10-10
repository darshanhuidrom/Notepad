package com.example.integra.notepad.model;

public class User {
    private  String firstName;
    private String lastName;
    private String address;
    private String emailId;
    private String phoneNo;
    private int id;

    public User(String firstName, String lastName, String address, String emailId, String phoneNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.emailId = emailId;
        this.phoneNo = phoneNo;
    }
    public User(int id,String firstName, String lastName, String address, String emailId, String phoneNo) {
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.emailId = emailId;
        this.phoneNo = phoneNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id="+id+"|"+"FirstName="+firstName+"|"
                +"LastName="+lastName+"|Address="+address
                +"|EmailId="+emailId+"|PhoneNo="+phoneNo;
    }
}
