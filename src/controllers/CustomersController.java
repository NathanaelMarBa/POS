package controllers;

import models.CustomerDao;
import models.Customers;
import views.SystemView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class CustomersController implements ActionListener, MouseListener, KeyListener {
    private Customers customers;
    private CustomerDao customerDao;
    private SystemView views;

    DefaultTableModel model = new DefaultTableModel();

    public CustomersController(Customers customers, CustomerDao customerDao, SystemView views) {
        this.customers = customers;
        this.customerDao = customerDao;
        this.views = views;
        //Boton para agregar un nuevo cliente
        this.views.btn_register_customer.addActionListener(this);
        //Boton para editar un cliente
        this.views.btn_update_customer.addActionListener(this);
        //Boton de eliminar un cliente
        this.views.btn_delete_customer.addActionListener(this);
        //Boton de cancelar
        this.views.btn_cancel_customer.addActionListener(this);
        //Buscador
        this.views.txt_search_customer.addKeyListener(this);
        this.views.jLabelCustomers.addMouseListener(this);
        this.views.customers_table.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_customer){
            //Verificar si los campos estan vacios
            if (views.txt_customer_id.getText().equals("")
                || views.txt_customer_fullname.getText().equals("")
                || views.txt_customer_address.getText().equals("")
                || views.txt_customer_telephone.getText().equals("")
                || views.txt_customer_email.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Todos los campos son requeridos");
            }else {
                customers.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                customers.setFull_name(views.txt_customer_fullname.getText().trim());
                customers.setAddress(views.txt_customer_address.getText().trim());
                customers.setTelephone(views.txt_customer_telephone.getText().trim());
                customers.setEmail(views.txt_customer_email.getText().trim());

                if (customerDao.registerCustomerQuery(customers)){
                    cleanTable();
                    listAllCustomers();
                    cleanFields();
                    JOptionPane.showMessageDialog(null, "Cliente registrado correctamente");
                }else {
                    JOptionPane.showMessageDialog(null, "Error al registrar el cliente");
                }
            }
        }else if (e.getSource() == views.btn_update_customer){
            if (views.txt_customer_id.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Selecciona un cliente para editar");
            }else {
                if (views.txt_customer_id.getText().equals("")
                        || views.txt_customer_fullname.getText().equals("")
                        || views.txt_customer_address.getText().equals("")
                        || views.txt_customer_telephone.getText().equals("")
                        || views.txt_customer_email.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Todos los campos son requeridos");
                }else {
                    customers.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                    customers.setFull_name(views.txt_customer_fullname.getText().trim());
                    customers.setAddress(views.txt_customer_address.getText().trim());
                    customers.setTelephone(views.txt_customer_telephone.getText().trim());
                    customers.setEmail(views.txt_customer_email.getText().trim());

                    if (customerDao.updateCustomerQuery(customers)){
                        cleanTable();
                        listAllCustomers();
                        cleanFields();
                        views.btn_register_customer.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente");
                    }else {
                        JOptionPane.showMessageDialog(null, "Error al actualizar el cliente");
                    }
                }
            }
        }else if (e.getSource() == views.btn_delete_customer) {
            int row = views.customers_table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(null, "Selecciona un cliente para eliminar");
            }else {
                int id = Integer.parseInt(views.customers_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el cliente?");

                if (question == 0 && customerDao.deleteCustomerQuery(id) != false){
                    cleanTable();
                    listAllCustomers();
                    cleanFields();
                    views.btn_register_customer.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente");
                }
            }
        } else if (e.getSource() == views.btn_cancel_customer){
            cleanFields();
            views.btn_register_customer.setEnabled(true);
        }
    }

    //Listar clientes
    public void listAllCustomers(){
        List<Customers> list = customerDao.listCustomerQuery(views.txt_search_customer.getText());
        model = (DefaultTableModel) views.customers_table.getModel();

        Object[] row = new Object[5];

        for (int i = 0; i < list.size(); i++){
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFull_name();
            row[2] = list.get(i).getAddress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();
            model.addRow(row);
        }
        views.customers_table.setModel(model);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.customers_table){
            int row = views.customers_table.rowAtPoint(e.getPoint());
            views.txt_customer_id.setText(views.customers_table.getValueAt(row, 0).toString());
            views.txt_customer_fullname.setText(views.customers_table.getValueAt(row, 1).toString());
            views.txt_customer_address.setText(views.customers_table.getValueAt(row, 2).toString());
            views.txt_customer_telephone.setText(views.customers_table.getValueAt(row, 3).toString());
            views.txt_customer_email.setText(views.customers_table.getValueAt(row, 4).toString());
            //Desabilitar boton de registrar
            views.btn_register_customer.setEnabled(false);
            views.txt_customer_id.setEditable(false);
        }else if (e.getSource() == views.jLabelCustomers){
            views.jTabbedMenu.setSelectedIndex(3);
            cleanTable();
            cleanFields();
            listAllCustomers();
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
        if (e.getSource() == views.txt_search_customer){
            cleanTable();
            model.setRowCount(0);
            listAllCustomers();
        }
    }

    public void cleanTable(){
        for (int i = 0; i < model.getRowCount(); i++){
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void cleanFields(){
        views.txt_customer_id.setText("");
        views.txt_customer_id.setEditable(true);
        views.txt_customer_fullname.setText("");
        views.txt_customer_address.setText("");
        views.txt_customer_telephone.setText("");
        views.txt_customer_email.setText("");
    }
}
