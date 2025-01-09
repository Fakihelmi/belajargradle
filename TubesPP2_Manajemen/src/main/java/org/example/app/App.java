package org.example.app;

import org.example.controller.UserController;
import org.example.view.ForgotPasswordView;
import org.example.view.ProfileView;
import org.example.view.RegistrationView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("E-Waste App");
            frame.setSize(350, 380);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 0, 5, 0);

            // Email Field
            JLabel emailLabel = new JLabel("Alamat Email");
            emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(emailLabel, gbc);

            gbc.gridy++;
            JTextField emailField = new JTextField(20);
            emailField.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(10, 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(emailField, gbc);

            // Password Field
            gbc.gridy++;
            JLabel passwordLabel = new JLabel("Kata Sandi");
            passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(passwordLabel, gbc);

            gbc.gridy++;
            JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            JPasswordField passwordField = new JPasswordField(20);
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(10, 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
            passwordPanel.add(passwordField);

            JButton forgotPasswordButton = new JButton("Lupa kata sandi");
            forgotPasswordButton.setBorder(null);
            forgotPasswordButton.setBackground(null);
            forgotPasswordButton.setFocusPainted(false);
            forgotPasswordButton.setForeground(Color.BLUE);
            forgotPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            forgotPasswordButton.addActionListener(e -> new ForgotPasswordView().display());

            JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            forgotPanel.add(forgotPasswordButton);

            gbc.weightx = 0.9;
            mainPanel.add(passwordPanel, gbc);
            gbc.gridy++;
            gbc.weightx = 0.1;
            mainPanel.add(forgotPanel, gbc);

            // Login Button
            gbc.gridy++;
            gbc.weightx = 1;
            JButton loginButton = new JButton("Masuk");
            loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            loginButton.setBackground(new Color(0, 128, 0));
            loginButton.setForeground(Color.WHITE);
            loginButton.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(10, 1),
                    new EmptyBorder(10, 10, 10, 10)));

            UserController userController = new UserController();
            loginButton.addActionListener(e -> {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String loginResult = userController.loginUser(email, password);
                if (loginResult.equals("Login Successful")) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    System.out.println("Login Successfull!");
                    ProfileView profileView = new ProfileView(email);
                    profileView.display();

                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Login failed: " + loginResult);
                }
            });
            mainPanel.add(loginButton, gbc);

            // Register Text and Button
            gbc.gridy++;
            JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            JLabel registerLabel = new JLabel("Belum punya akun ? ");
            JButton registerButton = new JButton("Klik disini untuk daftar");
            registerButton.setBorder(null);
            registerButton.setBackground(null);
            registerButton.setFocusPainted(false);
            registerButton.setForeground(Color.BLUE);
            registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            registerButton.addActionListener(e -> new RegistrationView().display());

            registerPanel.add(registerLabel);
            registerPanel.add(registerButton);
            mainPanel.add(registerPanel, gbc);

            frame.add(mainPanel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
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
}