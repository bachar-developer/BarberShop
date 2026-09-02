package BarberShop;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public  class AppointmentRepository {

    private Connection connection;

    public AppointmentRepository(Connection connection) {

        this.connection = connection;

    }


    public void showAppointments() throws SQLException {
        try (Statement statement = connection.createStatement()) {

            ResultSet showList = statement.executeQuery("select a.id_appointment as 'Nº Cita'," +
                    " c.first_name as Nombre,c.phone as Telefono," +
                    " a.date_appointment as fecha," +
                    " a.hour_appointment as hora, a.service as Servicio ," +
                    " a.state as Estado from appointments as a" +
                    " inner join customers as c on c.id_customer = a.id_customer ");

            //System.out.println("Nº Cita"+"Nombre"+"Telefono"+"Fecha"+"Hora"+"Descripcion"+"Estado");
            boolean markDown=false;

                while (showList.next()) {
                    markDown=true;
                    System.out.println(showList.getInt("Nº Cita") + " " +
                            showList.getString("Nombre") + " " +
                            showList.getString("Telefono") + " " +
                            showList.getString("fecha") + " " +
                            showList.getString("hora") + " " +
                            showList.getString("Servicio") + " " +
                            showList.getString("Estado"));
                }if (!markDown){
                    System.out.println("La lista esta vacia ");
            }
        }


    }

    public int addAppointment(Appointment appointment) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into appointments (id_customer,date_appointment,hour_appointment,service )" +
                " values\n" +
                "(?,?,?,?)")) {

            preparedStatement.setInt(1, appointment.getIdCustomer());
            preparedStatement.setObject(2, appointment.getDateAppointment());
            preparedStatement.setObject(3, appointment.getHourAppointment());
            preparedStatement.setString(4, appointment.getService());
            return preparedStatement.executeUpdate();
        }
    }

    public int cancelledAppointment(String state, int id_appointment) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE appointments " +
                "set state = ? " +
                "where id_appointment = ?")) {
            preparedStatement.setString(1, state);
            preparedStatement.setInt(2, id_appointment);


            return preparedStatement.executeUpdate();

        }
    }

    public void eraseAppointmentsCancelled() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("delete \n" +
                    "from appointments \n" +
                    "where   state = 'cancelled'");
        }


    }

    public void findPhone(String phone) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select c.first_name ," +
                "a.id_appointment, " +
                "a.date_appointment, " +
                "a.state " +
                "from appointments as a " +
                "inner join customers as c " +
                "on a.id_customer= c.id_customer " +
                "where c.phone = ? ")) {


            preparedStatement.setString(1, "+34 " + phone);
            ResultSet listAppointmentsID = preparedStatement.executeQuery();
            boolean markdown=false;

            //System.out.println("Nombre | Nº Cita | Fecha cita | Estado ");

                while (listAppointmentsID.next()) {
                    markdown=true;
                    System.out.println(listAppointmentsID.getString("first_name") + " " +
                            listAppointmentsID.getString("id_appointment") + " " +
                            listAppointmentsID.getString("date_appointment") + " " +
                            listAppointmentsID.getString("state"));

                }if (!markdown){
                    System.out.println("El cliente referido con su telefono no tiene citas\n");

            }
        }

    }




    public void eraseAll(int id_customer)throws SQLException {

        try (PreparedStatement preparedEraseAppointments = connection.prepareStatement(
                "delete from appointments where id_customer =? ");

             PreparedStatement preparedEraseCustomer = connection.prepareStatement(
                     "delete from customers where id_customer =? ")) {
            connection.setAutoCommit(false);
            try {
                preparedEraseAppointments.setInt(1, id_customer);
                preparedEraseCustomer.setInt(1, id_customer);

                preparedEraseAppointments.executeUpdate();
                preparedEraseCustomer.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Error : " + e);

            } finally {
                connection.setAutoCommit(true);
            }


        }
    }

    public  int  modifyAppointment(LocalDate date_appointment,int id_appointment)throws SQLException {
        try (PreparedStatement preparedModifyAppointment = connection.prepareStatement("update appointments\n" +
                "set date_appointment = ?" +
                "where id_appointment = ?")) {
            preparedModifyAppointment.setObject(1, date_appointment);
            preparedModifyAppointment.setInt(2, id_appointment);
            return preparedModifyAppointment.executeUpdate();

        }

    }



    public int  modifyAppointmentphone(LocalTime chHour,String phone)throws SQLException{
        try(PreparedStatement preparedStatement = connection.prepareStatement("update appointments set hour_appointment = ?" +
                " where id_customer = (select id_customer from customers where phone = ?) ")) {


            preparedStatement.setObject(1, chHour);
            preparedStatement.setString(2, phone);

            return preparedStatement.executeUpdate();
        }
    }

}


