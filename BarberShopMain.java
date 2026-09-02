package BarberShop;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BarberShopMain {
    public static void main(String[] args) {
        var menuSuperior = 0;

        Scanner teclado = new Scanner(System.in);

        try {
            System.out.println("Introduzca usuario");
            String login =teclado.next();
            System.out.println("Introduzca contraseña");
            String pass =teclado.next();

            DatabaseConnection databaseConnection = new DatabaseConnection();
            boolean logPass =databaseConnection.login(login,pass);

            if (logPass) {
                Connection conexion = databaseConnection.connect();


                System.out.println("Conectado\n");
                AppointmentRepository appointmentRepository = new AppointmentRepository(conexion);
                CustomerRepository customerRepository = new CustomerRepository(conexion);



                while (menuSuperior != 5) {
                System.out.println("\n1º Create\n" +
                        "2º Read \n" +
                        "3º Update\n" +
                        "4º Delete\n" +
                        "5º Salir\n");

                System.out.println("Por favor Elija una opcion");
                menuSuperior = teclado.nextInt();

                try {

                    switch (menuSuperior) {
                        case (1): {
                            create(teclado, appointmentRepository, customerRepository);
                            break;
                        }
                        case (2): {
                            read(teclado, appointmentRepository, customerRepository);
                            break;

                        }
                        case (3): {
                            update(teclado,appointmentRepository);
                            break;

                        }
                        case (4): {
                            delete(teclado, appointmentRepository);
                            break;
                        }
                        case (5): {
                            System.out.println("Gracias por usar nuestro sistema");
                            return;
                        }

                    }

                } catch (SQLException e) {
                    System.out.println("Error al realizar la operación en la base de datos");
                }catch (DateTimeParseException e){
                    System.out.println("\nError el formato introducido no es correcto\n");
                }
            }}else {
                System.out.println("Contraseña o usuario incorrecto ");
            }
        } catch (SQLException e) {
            System.out.println("Fallo en la comunicacion con el servidor , verifique que el servidor esta encendido");
        }
    }

    public static void create(Scanner teclado, AppointmentRepository appointmentRepository, CustomerRepository customerRepository) throws SQLException {
        var add = 0;

        while (add != 3) {
            System.out.println("\n1º Agregar Cita \n" +
                    "2º Agregar CLiente\n" +
                    "3º Volver al menu");

            add = teclado.nextInt();
            System.out.println("Por favor elige una opcion\n");

            switch (add) {

                case (1): {
                    try {
                        System.out.println("Introduce el ID_usuario : \n");
                        String idCustomer = teclado.next();
                        int idCustomerInt = Integer.parseInt(idCustomer);

                        System.out.println("Introduce la fecha : || Ejemplo [24/11/2026]");
                        String date = teclado.next();
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate dateAppointment = LocalDate.parse(date, dateTimeFormatter);

                        System.out.println("Introduce la hora : || Ejemplo [12:30]");
                        String hour = teclado.next();
                        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("HH:mm");
                        LocalTime hourAppointment = LocalTime.parse(hour, dateTimeFormatter1);

                        teclado.nextLine();

                        System.out.println("Introduce la Descripcion del servicio");
                        String service = teclado.nextLine();

                        Appointment newappointment = new Appointment(idCustomerInt, dateAppointment, hourAppointment, service);
                        appointmentRepository.addAppointment(newappointment);
                    } catch (NumberFormatException e) {
                        System.out.println("Error : Solo se admiten numeros");
                    } catch (DateTimeParseException e) {
                        System.out.println("Error al introducir el dato mire los ejemplos");
                    } catch (SQLIntegrityConstraintViolationException e) {
                        System.out.println("No se creo la cita por que esa ID no corresponde a nuestra base de datos");
                    }
                    break;

                }case (2): {
                    try {
                        teclado.nextLine();
                        System.out.println("Introduce el nombre del usuario");

                        String first_name = teclado.nextLine();
                        var validName=true;
                        if (first_name.isBlank()){
                            validName=false;
                        }else {
                            for (int i = 0; i < first_name.length(); ++i) {
                                char letra = first_name.charAt(i);
                                if (!Character.isLetter(letra) && !Character.isSpaceChar(letra)) {
                                    validName=false;
                                    break;
                                }
                            }
                        }
                        if (!validName){
                            System.out.println("Nombre Incorrecto");
                            break;
                        }else {
                            System.out.println("Introduce el telefono del usuario");
                            String phone = teclado.next();
                            if (phone.length()< 9){
                                System.out.println("Telefono invalido");
                            }else{
                                System.out.println("Introduce el mail del usuario");
                                String mail = teclado.next();
                                teclado.nextLine();
                                if (!mail.contains("@")){
                                    System.out.println("\nError usuario no añadido");
                                    System.out.println("\nDebe de introducir un correo electronico correcto\n");
                                    break;
                                }

                                Customer newCustomer = new Customer(first_name, "+34 " + phone, mail);
                                customerRepository.addCustomer(newCustomer);
                            }

                        }break;


                    }catch (Exception e){
                        System.out.println("Error : "+ e);
                    }

                }
            }break;
        }
    }


    public static void read (Scanner teclado, AppointmentRepository appointmentRepository, CustomerRepository customerRepository) throws SQLException {
        int show = 0;

        while (show != 4) {

            System.out.println("1º Mostrar Citas\n" +
                    "2º Mostrar clientes\n" +
                    "3º Buscar Citas por telefono\n" +
                    "4º Volver al menu ");

            show = teclado.nextInt();
            switch (show) {
                case (1): {

                    appointmentRepository.showAppointments();
                    System.out.println("\n");
                    break;

                }
                case (2): {


                    customerRepository.showCustomers();
                    System.out.println("\n");
                    break;

                }
                case (3): {

                    System.out.println("Introduzca el numero de telefono : ");
                    String phone = teclado.next();
                    teclado.nextLine();
                    appointmentRepository.findPhone(phone);

                    break;

                }
                case (4): {
                    break;
                }

            }break;
        }

    }

    public static void update(Scanner teclado,AppointmentRepository appointmentRepository)throws SQLException {


        int update = 0;

        while (update != 4) {
            System.out.println("1º Actualizar fecha mediante ID de la cita \n" +
                    "2º Actualizar hora mediante telefono\n" +
                    "3º Cancelar cita \n"+
                    "4º volver al menu principal");
            update = teclado.nextInt();

            switch (update) {


                case (1): {
                    try{
                    System.out.println("Introduce la Id_cita");
                    int idChAppointment = teclado.nextInt();
                    teclado.nextLine();

                    System.out.println("Introduce el cambio de fecha || Ejemplo 24/11/1986");
                    String date = teclado.nextLine();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);

                    appointmentRepository.modifyAppointment(localDate, idChAppointment);

                    break;
                } catch (InputMismatchException e) {
                        System.out.println("Los datos introducidos no son validos");
                    }
                }
                case (2): {

                    System.out.println("Introduce el cambio de hora ");
                    String chHour =teclado.next();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime hour = LocalTime.parse(chHour,dateTimeFormatter);

                    teclado.nextLine();
                    System.out.println("Selecciona el numero de telefono ");
                    String phone= teclado.nextLine();

                    appointmentRepository.modifyAppointmentphone(hour,"+34 "+phone);
                    break;
                }
                case (3): {
                    try {

                        System.out.println("Selecciona la ID de la cita para cancelar");
                        int id = teclado.nextInt();
                        appointmentRepository.cancelledAppointment("cancelled", id);
                        break;

                    }catch (InputMismatchException e){
                        teclado.nextLine();
                        System.out.println("Los datos introducidos no son validos");
                    }

                } case (4):{

                }
            }
            return;
        }
    }

    public static void delete (Scanner teclado, AppointmentRepository appointmentRepository)throws SQLException {
        int delete = 0;

        while (delete != 3) {

            System.out.println("1º Limpiar de  citas canceladas\n" +
                    "2º Eliminar cliente\n" +
                    "3º Volver al menu\n");
            delete = teclado.nextInt();

            switch (delete) {


                case (1): {
                    appointmentRepository.eraseAppointmentsCancelled();
                    System.out.println("Todas las citas que estaban canceladas se han eliminado\n");
                    break;
                }
                case (2): {
                    System.out.println("Introduce el ID del usuario para eliminar todo referente a el ");
                    int eraseUserAppointments = teclado.nextInt();
                    teclado.nextLine();
                    appointmentRepository.eraseAll(eraseUserAppointments);
                    break;
                }
                case (3): {

                }
            }
            break;
        }
    }



}
