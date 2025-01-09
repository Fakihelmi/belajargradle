package org.example.view;

import org.example.controller.UserController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class ChangePasswordView extends JFrame {

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmNewPasswordField;
    private JLabel feedbackLabel;
    private String userEmail;
    private final UserController userController = new UserController();
    private static final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private static final Color DISABLED_BG = new Color(240, 240, 240);
    private final Font labelFont = new Font("Arial", Font.BOLD, 14);
    private final Font buttonFont = new Font("Arial", Font.PLAIN, 14);

    public ChangePasswordView(String email) {
        this.userEmail = email;
        setTitle("Change Password");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        add(mainPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 10, 0);


        // Judul Panel
        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_GREEN);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the title
        gbc.gridy++;
        mainPanel.add(titleLabel, gbc);

        // Feedback Label
        feedbackLabel = new JLabel("");
        feedbackLabel.setForeground(Color.RED);
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        mainPanel.add(feedbackLabel, gbc);

        // Old Password
        JLabel oldPasswordLabel = new JLabel("Old Password:");
        oldPasswordLabel.setFont(labelFont);
        gbc.gridy++;
        mainPanel.add(oldPasswordLabel, gbc);
        gbc.gridy++;
        oldPasswordField = new JPasswordField();
        oldPasswordField.setPreferredSize(new Dimension(0,35));
        oldPasswordField.setBorder(new RoundBorder(10, 1));
        mainPanel.add(oldPasswordField, gbc);

        // New Password
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(labelFont);
        gbc.gridy++;
        mainPanel.add(newPasswordLabel, gbc);
        gbc.gridy++;
        newPasswordField = new JPasswordField();
        newPasswordField.setPreferredSize(new Dimension(0,35));
        newPasswordField.setBorder(new RoundBorder(10, 1));
        mainPanel.add(newPasswordField, gbc);


        // Confirm New Password
        JLabel confirmPasswordLabel = new JLabel("Confirm New Password:");
        confirmPasswordLabel.setFont(labelFont);
        gbc.gridy++;
        mainPanel.add(confirmPasswordLabel, gbc);
        gbc.gridy++;
        confirmNewPasswordField = new JPasswordField();
        confirmNewPasswordField.setPreferredSize(new Dimension(0,35));
        confirmNewPasswordField.setBorder(new RoundBorder(10, 1));
        mainPanel.add(confirmNewPasswordField, gbc);

        // Button Panel
        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainPanel.add(buttonPanel, gbc);


        // Change Password Button
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setFont(buttonFont);
        changePasswordButton.setBackground(PRIMARY_GREEN);
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        changePasswordButton.addActionListener(new ChangePasswordListener());
        buttonPanel.add(changePasswordButton);

        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(buttonFont);
        cancelButton.setBackground(PRIMARY_GREEN);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        pack();
    }


    private class ChangePasswordListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmNewPasswordField.getPassword());

            if(!newPassword.equals(confirmPassword)){
                feedbackLabel.setText("New password and Confirm password must be the same");
                return;
            } else if (newPassword.isEmpty() || confirmPassword.isEmpty() || oldPassword.isEmpty()){
                feedbackLabel.setText("All password fields must be filled");
                return;
            } else {
                feedbackLabel.setText("");
            }


            if (userController.loginUser(userEmail, oldPassword).equals("Login Successful")){
                if(userController.resetPassword(userEmail, newPassword)){
                    JOptionPane.showMessageDialog(ChangePasswordView.this, "Password changed successfully.");
                    dispose();
                } else{
                    feedbackLabel.setText("Failed to change password, please try again!");
                }
            } else{
                feedbackLabel.setText("Old password is incorrect");
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

            Shape round = new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius);
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