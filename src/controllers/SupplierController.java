package controllers;

import models.*;
import views.SystemView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

import static models.EmployeesDao.rol_user;


public class SupplierController implements ActionListener, MouseListener, KeyListener {

    private Suppliers supplier;
    private SuppliersDao supplierDao;
    private SystemView views;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();

    public SupplierController(Suppliers supplier, SuppliersDao suppliesDao, SystemView views) {
        this.supplier = supplier;
        this.supplierDao = suppliesDao;
        this.views = views;
        //Boton de agregar proveedor
        this.views.btn_register_supplier.addActionListener(this);
        //Modificar proveedor
        this.views.btn_update_supplier.addActionListener(this);
        //Boton de eliminar proveedor
        this.views.btn_delete_supplier.addActionListener(this);
        //Boton de cancelar
        this.views.btn_cancel_supplier.addActionListener(this);
        this.views.suppliers_table.addMouseListener(this);
        this.views.txt_search_supplier.addKeyListener(this);
        this.views.jLabelSuppliers.addMouseListener(this);
        getSupplierName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_supplier) {
            if (views.txt_supplier_name.getText().equals("")
                    || views.txt_supplier_description.getText().equals("")
                    || views.txt_supplier_address.getText().equals("")
                    || views.txt_supplier_telephone.getText().equals("")
                    || views.txt_supplier_email.getText().equals("")
                    || views.cmb_supplier_city.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Por favor llene todos los campos");
            } else {
                supplier.setName(views.txt_supplier_name.getText().trim());
                supplier.setDescription(views.txt_supplier_description.getText().trim());
                supplier.setAddress(views.txt_supplier_address.getText().trim());
                supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                supplier.setEmail(views.txt_supplier_email.getText().trim());
                supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
                if (supplierDao.registerSupplierQuery(supplier)) {
                    cleanTable();
                    cleanFields();
                    listAllSuppliers();
                    JOptionPane.showMessageDialog(null, "Proveedor registrado correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar proveedor");
                }
            }
        } else if (e.getSource() == views.btn_update_supplier) {
            if (views.txt_supplier_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Por favor seleccione un proveedor");
            } else {
                if (views.txt_supplier_name.getText().equals("")
                        || views.txt_supplier_address.getText().equals("")
                        || views.txt_supplier_telephone.getText().equals("")
                        || views.txt_supplier_email.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Por favor llene todos los campos");
                } else {
                    supplier.setName(views.txt_supplier_name.getText().trim());
                    supplier.setDescription(views.txt_supplier_description.getText().trim());
                    supplier.setAddress(views.txt_supplier_address.getText().trim());
                    supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                    supplier.setEmail(views.txt_supplier_email.getText().trim());
                    supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
                    supplier.setId(Integer.parseInt(views.txt_supplier_id.getText()));

                    if (supplierDao.updateSupplierQuery(supplier)) {
                        cleanTable();
                        cleanFields();
                        listAllSuppliers();
                        views.btn_register_supplier.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Proveedor actualizado correctamente");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al actualizar proveedor");
                    }
                }
            }
        }else if (e.getSource() == views.btn_delete_supplier){
            int row = views.suppliers_table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(null, "Por favor seleccione un proveedor");
            }else {
                int id = Integer.parseInt(views.suppliers_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el proveedor?");
                if (question == 0 && supplierDao.deleteSupplierQuery(id) != false){
                    cleanTable();
                    cleanFields();
                    listAllSuppliers();
                    JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente");
                }
            }
        } else if (e.getSource() == views.btn_cancel_supplier){
            cleanFields();
            views.btn_register_supplier.setEnabled(true);
        }
    }

    //Listar proveedores
    public void listAllSuppliers(){
        if (rol.equals("Administrador")) {
            List<Suppliers> List = supplierDao.listSupplierQuery(views.txt_search_supplier.getText());
            model = (DefaultTableModel) views.suppliers_table.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < List.size(); i++) {
                row[0] = List.get(i).getId();
                row[1] = List.get(i).getName();
                row[2] = List.get(i).getDescription();
                row[3] = List.get(i).getAddress();
                row[4] = List.get(i).getTelephone();
                row[5] = List.get(i).getEmail();
                row[6] = List.get(i).getCity();
                model.addRow(row);
            }
            views.suppliers_table.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.suppliers_table) {
            int row = views.suppliers_table.rowAtPoint(e.getPoint());
            views.txt_supplier_id.setText(views.suppliers_table.getValueAt(row, 0).toString());
            views.txt_supplier_name.setText(views.suppliers_table.getValueAt(row, 1).toString());
            views.txt_supplier_description.setText(views.suppliers_table.getValueAt(row, 2).toString());
            views.txt_supplier_address.setText(views.suppliers_table.getValueAt(row, 3).toString());
            views.txt_supplier_telephone.setText(views.suppliers_table.getValueAt(row, 4).toString());
            views.txt_supplier_email.setText(views.suppliers_table.getValueAt(row, 5).toString());
            views.cmb_supplier_city.setSelectedItem(views.suppliers_table.getValueAt(row, 6).toString());
            //Desactivar boton de agregar
            views.btn_register_supplier.setEnabled(false);
            views.txt_supplier_id.setEditable(false);
        }else if (e.getSource() == views.jLabelSuppliers){
            if (rol.equals("Administrador")) {
                views.jTabbedMenu.setSelectedIndex(4);
                cleanTable();
                cleanFields();
                listAllSuppliers();
            }else{
                views.jTabbedMenu.setEnabledAt(5, false);
                views.jLabelSuppliers.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tienes permisos para acceder a esta sección");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_search_supplier) {
            cleanTable();
            listAllSuppliers();
        }
    }

    public void cleanTable(){
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void cleanFields(){
        views.txt_supplier_id.setText("");
        views.txt_supplier_name.setText("");
        views.txt_supplier_description.setText("");
        views.txt_supplier_address.setText("");
        views.txt_supplier_telephone.setText("");
        views.txt_supplier_email.setText("");
        views.cmb_supplier_city.setSelectedIndex(0);
        views.btn_register_supplier.setEnabled(true);
    }

    //Mostrar nombre del proveedor
    public void getSupplierName(){
        List<Suppliers> list = supplierDao.listSupplierQuery(views.txt_search_supplier.getText());
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            views.cmb_purchase_supplier.addItem(new DynamicCombobox(id, name));
        }
    }
}
