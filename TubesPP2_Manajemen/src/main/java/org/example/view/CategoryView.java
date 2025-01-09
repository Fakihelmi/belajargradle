package org.example.view;

import org.example.controller.CategoryController;
import org.example.model.WasteCategory;
import org.example.model.WasteType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class CategoryView extends JFrame {

    private final CategoryController categoryController = new CategoryController();
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private static final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private JTextField categoryNameField;
    private JTextField typeNameField;
    private JComboBox<Object> itemComboBox;
    private JComboBox<WasteType> typeComboBox;
    private enum Mode {
        CATEGORY,
        TYPE
    }
    private Mode currentMode = Mode.CATEGORY;

    public CategoryView() {
        setTitle("Kategori Sampah");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // Panel untuk tombol CRUD
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(crudPanel, BorderLayout.NORTH);

        // Button Panel
        JButton addButton = new JButton("Tambah");
        addButton.setBackground(PRIMARY_GREEN);
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        addButton.addActionListener(e -> showAddDialog());
        crudPanel.add(addButton);

        JButton updateButton = new JButton("Ubah");
        updateButton.setBackground(PRIMARY_GREEN);
        updateButton.setForeground(Color.WHITE);
        updateButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        updateButton.addActionListener(e -> showUpdateDialog());
        crudPanel.add(updateButton);

        JButton deleteButton = new JButton("Hapus");
        deleteButton.setBackground(PRIMARY_GREEN);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        deleteButton.addActionListener(e -> showDeleteDialog());
        crudPanel.add(deleteButton);

        // Radio Buttons untuk mode
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.add(modePanel, BorderLayout.SOUTH);

        ButtonGroup modeGroup = new ButtonGroup();

        JRadioButton categoryModeButton = new JRadioButton("Kategori", true);
        categoryModeButton.addActionListener(e -> {
            currentMode = Mode.CATEGORY;
            loadCategoryData();
        });
        modeGroup.add(categoryModeButton);
        modePanel.add(categoryModeButton);

        JRadioButton typeModeButton = new JRadioButton("Jenis Sampah");
        typeModeButton.addActionListener(e -> {
            currentMode = Mode.TYPE;
            loadCategoryData();
        });
        modeGroup.add(typeModeButton);
        modePanel.add(typeModeButton);

        // Judul kolom tabel
        String[] columnNames = {"Kategori", "Jenis Sampah"};
        tableModel = new DefaultTableModel(null, columnNames);
        categoryTable = new JTable(tableModel);

        // Header
        JTableHeader header = categoryTable.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.BOLD, 12));
                c.setForeground(Color.WHITE);
                c.setBackground(PRIMARY_GREEN);
                setBorder(new LineBorder(PRIMARY_GREEN));
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        categoryTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        categoryTable.setShowGrid(true);
        categoryTable.setGridColor(Color.LIGHT_GRAY);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        loadCategoryData(); // load data to table
        pack();
    }

    private void loadCategoryData() {
        tableModel.setRowCount(0); // Hapus data sebelumnya
        List<WasteCategory> categories = categoryController.getAllWasteCategories();
        if (categories.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data kategori tidak ditemukan", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        for (WasteCategory category : categories) {
            List<WasteType> types = categoryController.getWasteTypesByCategory(category.getCategoryId());

            if(!types.isEmpty()){
                for (WasteType type: types){
                    tableModel.addRow(new Object[]{category.getCategoryName(), type.getTypeName()});
                }
            } else {
                tableModel.addRow(new Object[]{category.getCategoryName(), "Tidak ada jenis sampah dalam kategori ini"});
            }
        }
    }


    private void showAddDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel label = new JLabel(currentMode == Mode.CATEGORY ? "Nama Kategori:" : "Jenis Sampah:");
        panel.add(label, gbc);
        gbc.gridy++;

        if(currentMode == Mode.CATEGORY){
            categoryNameField = new JTextField(20);
            panel.add(categoryNameField, gbc);
        }else{
            List<WasteCategory> categories = categoryController.getAllWasteCategories();
            itemComboBox = new JComboBox<>(categories.toArray());
            panel.add(itemComboBox, gbc);
            gbc.gridy++;
            typeNameField = new JTextField(20);
            panel.add(typeNameField, gbc);
        }



        int result = JOptionPane.showConfirmDialog(this, panel, "Tambah " + (currentMode == Mode.CATEGORY ? "Kategori" : "Jenis Sampah"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if(currentMode == Mode.CATEGORY){
                String categoryName = categoryNameField.getText();
                if(categoryName == null || categoryName.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Nama kategori tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                WasteCategory newCategory = new WasteCategory(0, categoryName);
                if (categoryController.addWasteCategory(newCategory)){
                    JOptionPane.showMessageDialog(this, "Kategori berhasil ditambahkan", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCategoryData(); // Reload the data
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan kategori", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else {
                WasteCategory selectedCategory = (WasteCategory) itemComboBox.getSelectedItem();
                String typeName = typeNameField.getText();
                if(selectedCategory == null){
                    JOptionPane.showMessageDialog(this, "Pilih kategori terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(typeName == null || typeName.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Jenis sampah tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                WasteType newType = new WasteType(0, typeName, selectedCategory.getCategoryId());
                if (categoryController.addWasteType(newType)){
                    JOptionPane.showMessageDialog(this, "Jenis Sampah berhasil ditambahkan", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCategoryData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan jenis sampah", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showUpdateDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel label = new JLabel(currentMode == Mode.CATEGORY ? "Pilih Kategori:" : "Pilih Jenis Sampah:");
        panel.add(label, gbc);
        gbc.gridy++;
        if(currentMode == Mode.CATEGORY){
            List<WasteCategory> categories = categoryController.getAllWasteCategories();
            itemComboBox = new JComboBox<>(categories.toArray());
            panel.add(itemComboBox, gbc);
            gbc.gridy++;
            JLabel newCategoryLabel = new JLabel("Nama Kategori Baru:");
            panel.add(newCategoryLabel, gbc);
            gbc.gridy++;
            categoryNameField = new JTextField(20);
            panel.add(categoryNameField, gbc);

        }else{
            List<WasteCategory> categories = categoryController.getAllWasteCategories();
            itemComboBox = new JComboBox<>(categories.toArray());
            panel.add(itemComboBox, gbc);
            gbc.gridy++;
            WasteCategory selectedCategory = (WasteCategory) itemComboBox.getSelectedItem();
            List<WasteType> types = categoryController.getWasteTypesByCategory(selectedCategory.getCategoryId());
            typeComboBox = new JComboBox<>(types.toArray(new WasteType[0]));
            panel.add(typeComboBox, gbc);
            gbc.gridy++;
            JLabel newTypeLabel = new JLabel("Nama Jenis Sampah Baru:");
            panel.add(newTypeLabel, gbc);
            gbc.gridy++;
            typeNameField = new JTextField(20);
            panel.add(typeNameField, gbc);
        }
        String[] options = {"Ubah", "Batal"};
        int result = JOptionPane.showOptionDialog(this, panel, "Ubah " + (currentMode == Mode.CATEGORY ? "Kategori" : "Jenis Sampah"),
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, "Batal");

        if (result == JOptionPane.YES_OPTION) {
            if(currentMode == Mode.CATEGORY){
                WasteCategory selectedCategory = (WasteCategory) itemComboBox.getSelectedItem();
                String newCategoryName = categoryNameField.getText();

                if(selectedCategory == null){
                    JOptionPane.showMessageDialog(this, "Pilih kategori yang akan diubah", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(newCategoryName == null || newCategoryName.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Nama kategori tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                WasteCategory updatedCategory = new WasteCategory(selectedCategory.getCategoryId(), newCategoryName);
                if (categoryController.updateWasteCategory(updatedCategory)){
                    JOptionPane.showMessageDialog(this, "Kategori berhasil diubah", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCategoryData();
                } else{
                    JOptionPane.showMessageDialog(this, "Gagal mengubah kategori", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else {
                WasteType selectedType = (WasteType) typeComboBox.getSelectedItem();
                String newTypeName = typeNameField.getText();
                if(selectedType == null){
                    JOptionPane.showMessageDialog(this, "Pilih jenis sampah yang akan diubah", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(newTypeName == null || newTypeName.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Jenis sampah tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                WasteType updatedType = new WasteType(selectedType.getTypeId(), newTypeName, selectedType.getCategoryId());
                if(categoryController.updateWasteType(updatedType)){
                    JOptionPane.showMessageDialog(this, "Jenis sampah berhasil diubah", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCategoryData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengubah jenis sampah", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showDeleteDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel label = new JLabel(currentMode == Mode.CATEGORY ? "Pilih Kategori:" : "Pilih Jenis Sampah:");
        panel.add(label, gbc);
        gbc.gridy++;

        if(currentMode == Mode.CATEGORY){
            List<WasteCategory> categories = categoryController.getAllWasteCategories();
            itemComboBox = new JComboBox<>(categories.toArray());
            panel.add(itemComboBox, gbc);
        }else{
            List<WasteCategory> categories = categoryController.getAllWasteCategories();
            itemComboBox = new JComboBox<>(categories.toArray());
            panel.add(itemComboBox, gbc);
            gbc.gridy++;
            WasteCategory selectedCategory = (WasteCategory) itemComboBox.getSelectedItem();
            List<WasteType> types = categoryController.getWasteTypesByCategory(selectedCategory.getCategoryId());
            typeComboBox = new JComboBox<>(types.toArray(new WasteType[0]));
            panel.add(typeComboBox, gbc);
        }


        int result = JOptionPane.showConfirmDialog(this, panel, "Hapus " + (currentMode == Mode.CATEGORY ? "Kategori" : "Jenis Sampah"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if(currentMode == Mode.CATEGORY){
                WasteCategory selectedCategory = (WasteCategory) itemComboBox.getSelectedItem();
                if(selectedCategory == null){
                    JOptionPane.showMessageDialog(this, "Pilih kategori yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (categoryController.deleteWasteCategory(selectedCategory.getCategoryId())){
                    JOptionPane.showMessageDialog(this, "Kategori berhasil dihapus", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCategoryData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus kategori", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                WasteType selectedType = (WasteType) typeComboBox.getSelectedItem();
                if(selectedType == null){
                    JOptionPane.showMessageDialog(this, "Pilih jenis sampah yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (categoryController.deleteWasteType(selectedType.getTypeId())){
                    JOptionPane.showMessageDialog(this, "Jenis Sampah berhasil dihapus", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCategoryData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus jenis sampah", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    static class RoundBorder extends LineBorder {
        private final int radius;

        public RoundBorder(int radius, int thickness) {
            super(Color.LIGHT_GRAY, thickness, true);
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape round = new java.awt.geom.RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius);
            g2d.setColor(super.getLineColor());
            g2d.setStroke(new BasicStroke(super.getThickness()));
            g2d.draw(round);

            g2d.dispose();
        }
    }

    public void display() {
        setVisible(true);
    }
}