package models;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import  java.util.Date;
import java.util.List;

public class CustomerDao {

    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn; //Sirve para la realizacion de las consultas
    PreparedStatement pst;
    ResultSet rs; //Nos trae el resultado de las consultas

    //Registrar cliente
    public boolean registerCustomerQuery(Customers customer) {
        String query = "INSERT INTO customers (id, full_name, address, telephone, email, created, updated)" + "VALUES (?,?,?,?,?,?,?)";

        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, customer.getId());
            pst.setString(2, customer.getFull_name());
            pst.setString(3, customer.getAddress());
            pst.setString(4, customer.getTelephone());
            pst.setString(5, customer.getEmail());
            pst.setTimestamp(6, datetime);
            pst.setTimestamp(7, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar el cliente " + e);
            return false;
        }
    }

    //Listar cliente
    public List listCustomerQuery(String value) {
        List<Customers> listCustomers = new ArrayList();

        String query = "SELECT * FROM customers ";
        String query_search_customer = "SELECT * FROM customers WHERE id LIKE '%" + value + "%'";

        try {
            conn = cn.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_search_customer);
                rs = pst.executeQuery();
            }
            while (rs.next()) {
                Customers customer = new Customers();
                customer.setId(rs.getInt("id"));
                customer.setFull_name(rs.getString("full_name"));
                customer.setAddress(rs.getString("address"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                listCustomers.add(customer);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al listar los clientes " + e);
        }
        return listCustomers;
    }

    //Actualizar cliente
    public boolean updateCustomerQuery(Customers customer) {
        String query = "UPDATE customers SET full_name = ?, address = ?, telephone = ?, email = ?, updated = ? WHERE id = ?";

        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, customer.getFull_name());
            pst.setString(2, customer.getAddress());
            pst.setString(3, customer.getTelephone());
            pst.setString(4, customer.getEmail());
            pst.setTimestamp(5, datetime);
            pst.setInt(6, customer.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
          JOptionPane.showMessageDialog(null,"Error al actualizar el cliente " + e);
            return false;
        }
    }

    //Eliminar cliente
    public boolean deleteCustomerQuery(int id) {
        String query = "DELETE FROM customers WHERE id = ?";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al eliminar el cliente " + e);
            return false;
        }
    }

    //Buscar cliente
    public Customers searchCustomerQuery(int id) {
        Customers customer = new Customers();
        String query = "SELECT * FROM customers WHERE id = ?";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                customer.setId(rs.getInt("id"));
                customer.setFull_name(rs.getString("full_name"));
                customer.setAddress(rs.getString("address"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar el cliente " + e);
        }
        return customer;
    }
}
