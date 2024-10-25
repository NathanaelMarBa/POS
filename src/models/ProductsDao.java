package models;

import javax.swing.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ProductsDao {

    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn; //Sirve para la realizacion de las consultas
    PreparedStatement pst;
    ResultSet rs; //Nos trae el resultado de las consultas

    //Registrar Producto
    public boolean registerProductQuery(Products product){
        String query = "INSERT INTO products (code, name, description, unit_price, created, updated, category_id) VALUES (?,?,?,?,?,?,?)";

        Timestamp datetime = new Timestamp(new java.util.Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product.getCode());
            pst.setString(2, product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4, product.getUnit_price());
            pst.setTimestamp(5, datetime);
            pst.setTimestamp(6, datetime);
            pst.setInt(7, product.getCategory_id());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar el producto " + e);
            return false;
        }
    }

    //Listar productos
    public List listProductsQuery(String value){
        List<Products> list_products = new ArrayList();
        String query = "SELECT pro.*, ca.name AS category_name FROM products pro, categories ca WHERE pro.category_id = ca.id";
        String query_search_product = "Select pro.*, ca.name AS category_name FROM products pro INNER JOIN categories ca ON pro.category_id = ca.id WHERE pro.name LIKE '%" + value + "%'";
        try {
            conn = cn.getConnection();
            if(value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);
            }else{
                pst = conn.prepareStatement(query_search_product);
            }
            rs = pst.executeQuery();
            while(rs.next()){
                Products product = new Products();
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
                product.setCategory_name(rs.getString("category_name"));
                list_products.add(product);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al listar los productos " + e);
        }
        return list_products;
    }

    //Actualizar producto
    public boolean updateProductQuery(Products product){
        String query = "UPDATE products SET code=?, name=?, description=?, unit_price=?, updated=?, category_id=? WHERE id=?";
        Timestamp datetime = new Timestamp(new java.util.Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product.getCode());
            pst.setString(2, product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4, product.getUnit_price());
            pst.setTimestamp(5, datetime);
            pst.setInt(6, product.getCategory_id());
            pst.setInt(7, product.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al actualizar el producto " + e);
            return false;
        }
    }

    //Eliminar producto
    public boolean deleteProductQuery(int id){
        String query = "DELETE FROM products WHERE id=?";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al eliminar el producto relacionado con otra tabla" + e);
            return false;
        }
    }

    //Buscar productos
    public Products searchProductQuery(int id){
        String query = "SELECT pro.*, ca.name AS category_name FROM products pro "
                + "INNER JOIN categories ca ON pro.category_id = ca.id WHERE pro.id = ?";
        Products product = new Products();
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()){
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setCategory_id(rs.getInt("category_id"));
                product.setCategory_name(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar el producto " + e);
        }
        return product;
    }

    //Buscar productos por codigo
    public Products searchCodeQuery(int code){
        String query = "SELECT pro.id, pro.name, pro.unit_price, pro.product_quantity FROM products pro WHERE code = ?";
        Products product = new Products();
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, code);
            rs = pst.executeQuery();
            if (rs.next()){
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar el producto " + e);
        }
        return product;
    }

    //Traer el stock de un producto
    public Products searchId(int id){
        String query = "SELECT pro.product_quantity FROM products pro WHERE id = ?";
        Products product = new Products();
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()){
                product.setProduct_quantity(rs.getInt("product_quantity"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar el producto " + e);
        }
        return product;
    }

    //Actualizar stock
    public boolean updateStockQuery(int amount, int product_id){
        String query = "UPDATE products SET product_quantity = ? WHERE id = ?";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, amount);
            pst.setInt(2, product_id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al actualizar el stock " + e);
            return false;
        }
    }

    //listCategoriesQuery
    public List listCategoriesQuery(){
        List<DynamicCombobox> list_categories = new ArrayList();
        String query = "SELECT * FROM categories";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                DynamicCombobox category = new DynamicCombobox(rs.getInt("id"), rs.getString("name"));
                list_categories.add(category);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al listar las categorias " + e);
        }
        return list_categories;
    }
}
