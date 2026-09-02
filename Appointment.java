package BarberShop;


import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {


    private int idAppointment;
    private int idCustomer;
    private LocalDate dateAppointment ;
    private LocalTime hourAppointment ;
    private String service;
    private String state;


    public Appointment(int idCustomer,LocalDate dateAppointment,LocalTime hourAppointment, String service){
        this.idCustomer=idCustomer;
        this.dateAppointment=dateAppointment;
        this.hourAppointment=hourAppointment;
        this.service=service;

    }

    public int  getIdCustomer() {
        return this.idCustomer;
    }

    public LocalDate getDateAppointment() {
        return this.dateAppointment;
    }
    public LocalTime getHourAppointment() {
        return this.hourAppointment;
    }
    public String getService() {
        return this.service;
    }

}
