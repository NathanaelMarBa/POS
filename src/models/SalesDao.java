package models;

import javax.swing.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class SalesDao {

    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn; //Sirve para la realizacion de las consultas
    PreparedStatement pst;
    ResultSet rs; //Nos trae el resultado de las consultas

    //Registrar venta
    public boolean registerSaleQuery(int employee_id, double total) {

        String query = "INSERT INTO sales (employee_id, total, sale_date)" + "VALUES (?,?,?)";
        Timestamp datetime = new Timestamp(new java.util.Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, employee_id);
            pst.setDouble(2, total);
            pst.setTimestamp(3, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar la venta " + e);
            return false;
        }
    }

    //Detalles de la venta
    public boolean registerSaleDetailQuery(int product_id, double sale_id, int sale_quantity, double sale_price, double sale_subtotal) {

        String query = "INSERT INTO sale_details (product_id, sale_id, sale_quantity, sale_price, sale_subtotal)" + "VALUES (?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new java.util.Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product_id);
            pst.setDouble(2, sale_id);
            pst.setInt(3, sale_quantity);
            pst.setDouble(4, sale_price);
            pst.setDouble(5, sale_subtotal);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar el detalle de la venta " + e);
            return false;
        }
    }

    //Obtener id de la venta
    public int saleId() {
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM sales";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al obtener el id de la venta " + e);
        }
        return id;
    }

    //Obtener todas las ventas
    public List listAllSalesQuery() {
        List<Sales> list_sales = new ArrayList();
        String query = "SELECT s.id AS invoice, e.full_name AS employee, s.total, s.sale_date FROM sales s INNER JOIN employees e ON s.employee_id = e.id ORDER BY s.id ASC";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                Sales sale = new Sales();
                sale.setId(rs.getInt("invoice"));
                sale.setEmployee_name(rs.getString("employee"));
                sale.setTotal_to_pay(rs.getDouble("total"));
                sale.setSale_date(rs.getString("sale_date"));
                list_sales.add(sale);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error en ventas " + e);
        }
        return list_sales;
    }
}
