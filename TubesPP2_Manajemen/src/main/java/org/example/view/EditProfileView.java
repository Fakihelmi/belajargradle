package org.example.view;

import com.toedter.calendar.JDateChooser;
import org.example.controller.UserController;
import org.example.model.DatabaseConnection;
import org.example.model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditProfileView extends JFrame {
    private JTextField addressField;
    private JTextField usernameField;
    private JDateChooser dateOfBirthField;
    private String userEmail;
    private String profilePicturePath;
    private JLabel imagePreview;
    private static final String DEFAULT_PROFILE_PIC_PATH = "/default_profile.png";
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 550;
    private static final int PREVIEW_SIZE = 150;
    private static final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private static final Color LIGHT_GREEN = new Color(144, 238, 144, 50);
    private static final Color DISABLED_BG = new Color(240, 240, 240);
    private String originalUsername;
    private String originalAddress;
    private String originalDateOfBirth;
    private String originalProfilePicturePath;


    public EditProfileView(String email) {
        this.userEmail = email;
        initializeFrame();
        JPanel mainPanel = createMainPanel();
        add(mainPanel);
        loadUserProfileData(email); // Load data here
        pack();
    }

    private void initializeFrame() {
        setTitle("My Profile");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Title
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_GREEN);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        // Image Section
        JPanel imageSection = createImageSection();
        gbc.gridy = 1;
        mainPanel.add(imageSection, gbc);

        // Form Section
        JPanel formSection = createFormSection();
        gbc.gridy = 2;
        mainPanel.add(formSection, gbc);

        // Button Section
        JPanel buttonSection = createButtonSection();
        gbc.gridy = 3;
        mainPanel.add(buttonSection, gbc);

        return mainPanel;
    }

    private JPanel createImageSection() {
        JPanel imageSection = new JPanel(new GridBagLayout());
        imageSection.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Image Preview
        imagePreview = new JLabel();
        imagePreview.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
        imagePreview.setBorder(BorderFactory.createLineBorder(PRIMARY_GREEN, 1));
        loadDefaultImage();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        imageSection.add(imagePreview, gbc);

        // Choose Photo Button
        JButton browseButton = createStyledButton("Choose Photo");
        browseButton.setPreferredSize(new Dimension(100, 25));
        browseButton.addActionListener(e -> handleImageSelection());
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        imageSection.add(browseButton, gbc);

        return imageSection;
    }

    private JPanel createFormSection() {
        JPanel formSection = new JPanel(new GridBagLayout());
        formSection.setBackground(Color.WHITE);
        formSection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_GREEN, 1),
                new EmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Username Field (Non-editable)
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(PRIMARY_GREEN);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 0;
        formSection.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setEditable(false);
        usernameField.setBackground(DISABLED_BG);
        usernameField.setPreferredSize(new Dimension(0, 30));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        gbc.gridy = 1;
        formSection.add(usernameField, gbc);

        // Address Field
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(PRIMARY_GREEN);
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2;
        formSection.add(addressLabel, gbc);

        addressField = new JTextField(20);
        addressField.setPreferredSize(new Dimension(0, 30));
        gbc.gridy = 3;
        formSection.add(addressField, gbc);

        // Date of Birth Field
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setForeground(PRIMARY_GREEN);
        dobLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 4;
        formSection.add(dobLabel, gbc);

        dateOfBirthField = new JDateChooser();
        dateOfBirthField.setDateFormatString("yyyy-MM-dd");
        dateOfBirthField.setPreferredSize(new Dimension(0, 30));
        gbc.gridy = 5;
        formSection.add(dateOfBirthField, gbc);

        return formSection;
    }

    private JPanel createButtonSection() {
        JPanel buttonSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonSection.setBackground(Color.WHITE);

        JButton saveButton = createStyledButton("Save Changes");
        saveButton.addActionListener(e -> handleSave());

        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonSection.add(saveButton);
        buttonSection.add(cancelButton);

        return buttonSection;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 35));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_GREEN);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 100, 0));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_GREEN);
            }
        });

        return button;
    }

    private void loadDefaultImage() {
        try (InputStream is = getClass().getResourceAsStream(DEFAULT_PROFILE_PIC_PATH)) {
            if (is != null) {
                BufferedImage defaultImage = ImageIO.read(is);
                ImageIcon defaultIcon = new ImageIcon(defaultImage.getScaledInstance(
                        PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_SMOOTH));
                imagePreview.setIcon(defaultIcon);
            } else {
                imagePreview.setText("No Image");
            }
        } catch (IOException e) {
            e.printStackTrace();
            imagePreview.setText("Error loading image");
        }
    }


    private void handleImageSelection() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            profilePicturePath = selectedFile.getAbsolutePath();
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                ImageIcon icon = new ImageIcon(
                        image.getScaledInstance(PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_SMOOTH));
                imagePreview.setIcon(icon);
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorMessage("Error loading image!");
            }
        }
    }

    private void handleSave() {
        String address = addressField.getText();
        Date selectedDate = dateOfBirthField.getDate();
        String dob = null;

        if (selectedDate != null) {
            dob = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
        }
        System.out.println("Profile Picture Path: " + profilePicturePath);


        boolean isChanged = false;
        if(!address.equals(originalAddress) || (dob!=null && !dob.equals(originalDateOfBirth)) || (profilePicturePath != null && !profilePicturePath.equals(originalProfilePicturePath) ) ){
            isChanged = true;
        }

        if (isChanged) {
            User user = new User(usernameField.getText(),"",userEmail,"",true,profilePicturePath, address, dob);
            UserController userController = new UserController();

            if (userController.updateUser(user)) {
                showSuccessMessage("Profile successfully updated!");
                dispose();
            } else {
                showErrorMessage("Failed to update profile. Please try again.");
            }

        } else {
            showSuccessMessage("No changes detected");
            dispose();
        }
    }


    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void setEmail(String email) {
        this.userEmail = email;
    }


    public void display() {
        setVisible(true);
    }

    private void loadUserProfileData(String email) {
        String query = "SELECT username, address, date_of_birth, profile_picture FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String address = rs.getString("address");
                java.sql.Date dob = rs.getDate("date_of_birth");
                String profilePicture = rs.getString("profile_picture");


                usernameField.setText(username);
                addressField.setText(address);
                originalUsername = username;
                originalAddress = address;
                originalProfilePicturePath = profilePicture;

                if (dob != null) {
                    Date date = new Date(dob.getTime());
                    dateOfBirthField.setDate(date);
                    originalDateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(date);
                }
                if (profilePicture != null){
                    profilePicturePath = profilePicture;
                    try {
                        File imageFile = new File(profilePicture);
                        if(imageFile.exists()){
                            BufferedImage image = ImageIO.read(imageFile);
                            ImageIcon icon = new ImageIcon(
                                    image.getScaledInstance(PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_SMOOTH));
                            imagePreview.setIcon(icon);
                        } else{
                            loadDefaultImage();
                        }

                    }catch (IOException ex) {
                        ex.printStackTrace();
                        loadDefaultImage();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}