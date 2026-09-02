package BarberShop;

public class Customer {
    private String first_name;
    private String phone;
    private String mail;


    public Customer(String first_name,String phone, String mail){
        this.first_name=first_name;
        this.phone=phone;
        this.mail=mail;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getPhone(){
        return phone;
    }
    public String getMail() {
        return mail;
    }



}
