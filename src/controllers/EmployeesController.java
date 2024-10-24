package controllers;

import models.Employees;
import models.EmployeesDao;
import views.SystemView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

import static models.EmployeesDao.id_user;
import static models.EmployeesDao.rol_user;

public class EmployeesController implements ActionListener, MouseListener, KeyListener {
    private Employees employee;
    private EmployeesDao employeeDao;
    private SystemView views;

    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public EmployeesController(Employees employee, EmployeesDao employeeDao, SystemView views) {
        this.employee = employee;
        this.employeeDao = employeeDao;
        this.views = views;
        //Boton de registrar empleado
        this.views.btn_register_employee.addActionListener(this);
        //Boton para modificar empleado
        this.views.btn_update_employee.addActionListener(this);
        //Boton para eliminar empleado
        this.views.btn_delete_employee.addActionListener(this);
        //Boton de cancelar
        this.views.btn_cancel_employee.addActionListener(this);
        //Boton para cambiar contraseña
        this.views.btn_modify_data.addActionListener(this);
        //Colocar label en escuha
        this.views.jLabelEmployees.addMouseListener(this);
        //Tabla para listar empleados
        this.views.employees_table.addMouseListener(this);
        this.views.txt_search_employee.addKeyListener(this);
        this.views.jLabelSettings.addMouseListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_employee) {
            if (views.txt_employee_id.getText().equals("")
                    || views.txt_employee_fullname.getText().equals("")
                    || views.txt_employee_username.getText().equals("")
                    || views.txt_employee_address.getText().equals("")
                    || views.txt_employee_telephone.getText().equals("")
                    || views.txt_employee_email.getText().equals("")
                    || views.cmb_rol.getSelectedItem().toString().equals("")
                    || String.valueOf(views.txt_employee_password.getPassword()).equals("")) {
                JOptionPane.showMessageDialog(null, "Debes llenar todos los campos");
            } else {
                employee.setId(Integer.parseInt(views.txt_employee_id.getText().trim()));
                employee.setFull_name(views.txt_employee_fullname.getText().trim());
                employee.setUsername(views.txt_employee_username.getText().trim());
                employee.setAddress(views.txt_employee_address.getText().trim());
                employee.setTelephone(views.txt_employee_telephone.getText().trim());
                employee.setEmail(views.txt_employee_email.getText().trim());
                employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
                employee.setRol(views.cmb_rol.getSelectedItem().toString());

                try {
                    if (employeeDao.registerEmployeeQuery(employee)){
                        JOptionPane.showMessageDialog(null, "Empleado registrado correctamente");
                        clearFields();
                        cleanTable();
                        listAllEmployees();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al registrar empleado " + ex);
                }
            }
        } else if (e.getSource() == views.btn_update_employee) {
            if (views.txt_employee_id.equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            } else {
                //Verificar si los campos estan vacios
                if (views.txt_employee_id.getText().equals("")
                        || views.txt_employee_fullname.getText().equals("")
                        || views.cmb_rol.getSelectedItem().toString().equals(""))
                {
                    JOptionPane.showMessageDialog(null, "Debes llenar todos los campos");
                } else {
                    employee.setId(Integer.parseInt(views.txt_employee_id.getText().trim()));
                    employee.setFull_name(views.txt_employee_fullname.getText().trim());
                    employee.setUsername(views.txt_employee_username.getText().trim());
                    employee.setAddress(views.txt_employee_address.getText().trim());
                    employee.setTelephone(views.txt_employee_telephone.getText().trim());
                    employee.setEmail(views.txt_employee_email.getText().trim());
                    employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
                    employee.setRol(views.cmb_rol.getSelectedItem().toString());
                    try {
                        if (employeeDao.updateEmployeeQuery(employee)) {
                            cleanTable();
                            listAllEmployees();
                            views.btn_register_employee.setEnabled(true);
                            clearFields();
                            JOptionPane.showMessageDialog(null, "Empleado actualizado correctamente");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al actualizar empleado " + ex);
                    }
                }
            }
        } else if (e.getSource() == views.btn_delete_employee) {
            int row = views.employees_table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Si deseas eliminar un empleado debes de seleccionarlo");
            } else if (views.employees_table.getValueAt(row, 0).equals(id_user)) {
                JOptionPane.showMessageDialog(null, "No puedes eliminar tu propio usuario");
            } else {
                int id = Integer.parseInt(views.employees_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este empleado");

                if (question == 0 && employeeDao.deleteEmployeeQuery(id) != false) {
                    clearFields();
                    cleanTable();
                    views.btn_register_employee.setEnabled(true);
                    listAllEmployees();
                    JOptionPane.showMessageDialog(null, "Empleado eliminado correctamente");
                }
            }
        }else if (e.getSource() == views.btn_cancel_employee) {
            clearFields();
            views.btn_register_employee.setEnabled(true);
        }else if (e.getSource() == views.btn_modify_data){
            String password = String.valueOf(views.txt_password_modify.getPassword());
            String confirm_password = String.valueOf(views.txt_password_modify_confirm.getPassword());

            if (!password.equals("") && !confirm_password.equals("")){
                if (password.equals(confirm_password)) {
                    employee.setPassword(String.valueOf(views.txt_password_modify.getPassword()));

                    if(employeeDao.updateEmployeePassword(employee) != false){
                        JOptionPane.showMessageDialog(null, "Contraseña actualizada correctamente");
                        views.txt_password_modify.setText("");
                        views.txt_password_modify_confirm.setText("");
                    }else{
                        JOptionPane.showMessageDialog(null, "Error al actualizar contraseña");
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
                }
            }else {
                JOptionPane.showMessageDialog(null, "Debes llenar todos los campos");
            }
        }
    }

    //Listar empleados
    public void listAllEmployees(){
        if (rol.equals("Administrador")) {
            List <Employees> List = employeeDao.listEmployeesQuery(views.txt_search_employee.getText());
            model = (DefaultTableModel) views.employees_table.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < List.size(); i++) {
                row[0] = List.get(i).getId();
                row[1] = List.get(i).getFull_name();
                row[2] = List.get(i).getUsername();
                row[3] = List.get(i).getAddress();
                row[4] = List.get(i).getTelephone();
                row[5] = List.get(i).getEmail();
                row[6] = List.get(i).getRol();
                model.addRow(row);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.employees_table) {
            int row = views.employees_table.rowAtPoint(e.getPoint());

            views.txt_employee_id.setText(views.employees_table.getValueAt(row, 0).toString());
            views.txt_employee_fullname.setText(views.employees_table.getValueAt(row, 1).toString());
            views.txt_employee_username.setText(views.employees_table.getValueAt(row, 2).toString());
            views.txt_employee_address.setText(views.employees_table.getValueAt(row, 3).toString());
            views.txt_employee_telephone.setText(views.employees_table.getValueAt(row, 4).toString());
            views.txt_employee_email.setText(views.employees_table.getValueAt(row, 5).toString());
            views.cmb_rol.setSelectedItem(views.employees_table.getValueAt(row, 6).toString());

            //Deshabilitar
            views.txt_employee_id.setEditable(false);
            views.txt_employee_password.setEnabled(false);
            views.btn_register_employee.setEnabled(false);
        } else if (e.getSource() == views.jLabelEmployees) {
            if (rol.equals("Administrador")) {
                views.jTabbedMenu.setSelectedIndex(3);
                cleanTable();
                clearFields();
                listAllEmployees();
            }else {
                views.jTabbedMenu.setEnabledAt(4, false);
                views.jLabelEmployees.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tienes permisos para acceder a esta sección");
            }
        }else if (e.getSource() == views.jLabelSettings){
            views.jTabbedMenu.setSelectedIndex(7);
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
        if (e.getSource() == views.txt_search_employee) {
            cleanTable();
            listAllEmployees();
        }
    }

    public void clearFields(){
        views.txt_employee_id.setText("");
        views.txt_employee_fullname.setText("");
        views.txt_employee_username.setText("");
        views.txt_employee_address.setText("");
        views.txt_employee_telephone.setText("");
        views.txt_employee_email.setText("");
        views.txt_employee_password.setText("");
        views.cmb_rol.setSelectedIndex(0);
        views.txt_employee_id.setEditable(true);
        views.txt_employee_password.setEnabled(true);
    }

    public void cleanTable(){
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
}
