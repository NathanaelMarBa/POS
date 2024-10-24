package models;

import javax.swing.*;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class SuppliersDao {

    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn; //Sirve para la realizacion de las consultas
    PreparedStatement pst;
    ResultSet rs; //Nos trae el resultado de las consultas

    //Registrar proveedor
    public boolean registerSupplierQuery(Suppliers supplier) {
        String query = "INSERT INTO suppliers (name, description, address, telephone, email, city, created, updated)" + "VALUES (?,?,?,?,?,?,?,?)";

        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, supplier.getName());
            pst.setString(2, supplier.getDescription());
            pst.setString(3, supplier.getAddress());
            pst.setString(4, supplier.getTelephone());
            pst.setString(5, supplier.getEmail());
            pst.setString(6, supplier.getCity());
            pst.setTimestamp(7, datetime);
            pst.setTimestamp(8, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar el proveedor " + e);
            return false;
        }
    }

    //Listar proveedor
    public List listSupplierQuery(String value) {
        List<Suppliers> list_suppliers = new ArrayList();

        String query = "SELECT * FROM suppliers ";
        String query_search_supplier = "SELECT * FROM suppliers WHERE name LIKE '%" + value + "%'";

        try {
            conn = cn.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_search_supplier);
                rs = pst.executeQuery();
            }
            while (rs.next()) {
                Suppliers supplier = new Suppliers();
                supplier.setId(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                supplier.setDescription(rs.getString("description"));
                supplier.setAddress(rs.getString("address"));
                supplier.setTelephone(rs.getString("telephone"));
                supplier.setEmail(rs.getString("email"));
                supplier.setCity(rs.getString("city"));
                list_suppliers.add(supplier);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al listar el proveedor " + e);
        }
        return list_suppliers;
    }

    //Actualizar proveedor
    public boolean updateSupplierQuery(Suppliers supplier) {
        String query = "UPDATE suppliers SET name = ?, description = ?, address = ?, telephone = ?, email = ?, city = ?, updated = ? WHERE id = ?";

        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, supplier.getName());
            pst.setString(2, supplier.getDescription());
            pst.setString(3, supplier.getAddress());
            pst.setString(4, supplier.getTelephone());
            pst.setString(5, supplier.getEmail());
            pst.setString(6, supplier.getCity());
            pst.setTimestamp(7, datetime);
            pst.setInt(8, supplier.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al actualizar el proveedor " + e);
            return false;
        }
    }

    //Eliminar proveedor
    public boolean deleteSupplierQuery(int id) {
        String query = "DELETE FROM suppliers WHERE id = ?";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al eliminar el proveedor relacionado con otra tabla" + e);
            return false;
        }
    }
}
