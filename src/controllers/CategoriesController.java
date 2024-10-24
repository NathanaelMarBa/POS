package controllers;

import models.Categories;
import models.CategoriesDao;
import models.DynamicCombobox;
import views.SystemView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

import static models.EmployeesDao.rol_user;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class CategoriesController implements ActionListener, MouseListener, KeyListener {
    private Categories category;
    private CategoriesDao categoryDao;
    private SystemView views;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public CategoriesController(Categories categories, CategoriesDao categoriesDao, SystemView views) {
        this.category = categories;
        this.categoryDao = categoriesDao;
        this.views = views;
        //Boton para regitrar una nueva categoria
        this.views.btn_register_category.addActionListener(this);
        //Modificar una categoria
        this.views.btn_update_category.addActionListener(this);
        //Eliminar categoria
        this.views.btn_delete_category.addActionListener(this);
        //Cancelar
        this.views.btn_cancel_category.addActionListener(this);
        this.views.category_table.addMouseListener(this);
        this.views.txt_search_category.addKeyListener(this);
        this.views.jLabelCategories.addMouseListener(this);
        getCategoryName();
        AutoCompleteDecorator.decorate(views.cmb_product_category);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_category) {
           if (views.txt_category_name.getText().equals("")) {
               JOptionPane.showMessageDialog(null, "Debes de llenar todos los campos");
            } else {
                category.setName(views.txt_category_name.getText().trim());

                if (categoryDao.registerCategoryQuery(category)) {
                    cleanTable();
                    cleanFields();
                    listAllCategories();
                    JOptionPane.showMessageDialog(null, "Categoria registrada con exito");
                    views.txt_category_name.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar la categoria");
                }
           }
        } else if (e.getSource() == views.btn_update_category) {
            if (views.txt_category_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila");
            } else {
                if (views.txt_category_name.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Debes de llenar todos los campos");
                } else {
                    if (views.txt_category_id.getText().equals("")
                    || views.txt_category_name.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "Debes de llenar todos los campos");
                    } else {
                        category.setId(Integer.parseInt(views.txt_category_id.getText()));
                        category.setName(views.txt_category_name.getText().trim());
                        if (categoryDao.updateCategoryQuery(category)) {
                            cleanTable();
                            cleanFields();
                            views.btn_register_category.setEnabled(true);
                            listAllCategories();
                            JOptionPane.showMessageDialog(null, "Categoria actualizada con exito");

                        } else {
                            JOptionPane.showMessageDialog(null, "Error al actualizar la categoria");
                        }
                    }
                }
            }
        }else if (e.getSource() == views.btn_delete_category) {
            int row = views.category_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila");
            } else {
                int id = Integer.parseInt(views.category_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar la categoria?");
                if (question == 0 && categoryDao.deleteCategoryQuery(id) != false) {
                    if (categoryDao.deleteCategoryQuery(id)) {
                        cleanTable();
                        cleanFields();
                        views.btn_register_category.setEnabled(true);
                        listAllCategories();
                        JOptionPane.showMessageDialog(null, "Categoria eliminada con exito");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar la categoria");
                    }
                }
            }
        } else if (e.getSource() == views.btn_cancel_category) {
            cleanFields();
            views.btn_register_category.setEnabled(true);

        }
    }

    //Listar categorias
    public void listAllCategories() {
        if (rol.equals("Administrador")){
            List<Categories> categories = categoryDao.listCategoriesQuery(views.txt_search_category.getText());
            model = (DefaultTableModel) views.category_table.getModel();
            Object[] row = new Object[2];
            for (int i = 0; i < categories.size(); i++) {
                row[0] = categories.get(i).getId();
                row[1] = categories.get(i).getName();
                model.addRow(row);
            }
            views.category_table.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.category_table) {
            int row = views.category_table.rowAtPoint(e.getPoint());
            views.txt_category_id.setText(views.category_table.getValueAt(row, 0).toString());
            views.txt_category_name.setText(views.category_table.getValueAt(row, 1).toString());
            views.btn_register_category.setEnabled(false);
        }else if(e.getSource() == views.jLabelCategories){
            if (rol.equals("Administrador")) {
                views.jTabbedMenu.setSelectedIndex(4);
                cleanTable();
                cleanFields();
                listAllCategories();
            }else {
                views.jTabbedMenu.setEnabledAt(4, false);
                views.jLabelCategories.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tienes permisos para acceder a esta seccion");
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
        if (e.getSource() == views.txt_search_category) {
            cleanTable();
            listAllCategories();
        }
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void cleanFields() {
        views.txt_category_id.setText("");
        views.txt_category_name.setText("");
    }

    //Metodo que muestre las categorias
    public void getCategoryName(){
        List<Categories> list = categoryDao.listCategoriesQuery(views.txt_search_category.getText());
        for (int i = 0; i < list.size(); i++) {
           int id = list.get(i).getId();
           String name = list.get(i).getName();
           views.cmb_product_category.addItem(new DynamicCombobox(id, name));
        }
    }
}
