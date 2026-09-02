package BarberShop;

import java.sql.*;

public class CustomerRepository {
    private Connection connection;

    public CustomerRepository(Connection connection){
        this.connection=connection;
    }

    public int  addCustomer(Customer customer)throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into customers " +
                                                                                     "(first_name,phone,mail)" +
                                                                                     " values(?,?,?)");){

        preparedStatement.setString(1,customer.getFirst_name());
        preparedStatement.setString(2,customer.getPhone());
        preparedStatement.setString(3,customer.getMail());
        return preparedStatement.executeUpdate();
    }
}



        public void showCustomers()throws SQLException {
            try (Statement statement = connection.createStatement();) {
                ResultSet showcustomers = statement.executeQuery("select id_customer,first_name , phone, mail from customers");
                boolean markdown=false;
                while (showcustomers.next()) {
                    markdown=true;
                    System.out.println(showcustomers.getString("id_customer") + " " +
                            showcustomers.getString("first_name") + " " +
                            showcustomers.getString("phone") + " " +
                            showcustomers.getString("mail"));
                }if(!markdown){
                    System.out.println("La lista esta vacia");
                }

            }


        }

}
