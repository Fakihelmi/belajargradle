package org.example.view;

import org.example.controller.UserController;
import org.example.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class RegistrationView extends JFrame {

    private final UserController userController = new UserController();
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField otpField;
    private String savedOtp;
    private JLabel feedbackLabel;

    private final Font labelFont = new Font("Arial", Font.BOLD, 16);
    private final Font buttonFont = new Font("Arial", Font.PLAIN, 16);
    private final Color primaryColor = new Color(0, 128, 0);


    public RegistrationView() {
        setTitle("Registration");
        setSize(450, 550); // Sedikit diperbesar
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("src/main/java/org/example/img/icon.png");
        setIconImage(icon.getImage());


        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 10, 0);


        // Judul Panel
        JLabel titleLabel = new JLabel("Account Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the title
        gbc.gridy++;
        mainPanel.add(titleLabel, gbc);

        // Feedback Label
        feedbackLabel = new JLabel("");
        feedbackLabel.setForeground(Color.RED);
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        mainPanel.add(feedbackLabel, gbc);

        //Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        gbc.gridy++;
        mainPanel.add(usernameLabel, gbc);
        gbc.gridy++;
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(0, 35));
        usernameField.setBorder(new RoundBorder(5, 1));
        mainPanel.add(usernameField, gbc);

        //Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        gbc.gridy++;
        mainPanel.add(passwordLabel, gbc);
        gbc.gridy++;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(0, 35));
        passwordField.setBorder(new RoundBorder(5, 1));
        mainPanel.add(passwordField, gbc);

        //Konfirmasi Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(labelFont);
        gbc.gridy++;
        mainPanel.add(confirmPasswordLabel, gbc);
        gbc.gridy++;
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(0, 35));
        confirmPasswordField.setBorder(new RoundBorder(5, 1));
        mainPanel.add(confirmPasswordField, gbc);

        //Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        gbc.gridy++;
        mainPanel.add(emailLabel, gbc);
        gbc.gridy++;
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(0, 35));
        emailField.setBorder(new RoundBorder(5, 1));
        mainPanel.add(emailField, gbc);

        //OTP
        JLabel otpLabel = new JLabel("OTP Code:");
        otpLabel.setFont(labelFont);
        gbc.gridy++;
        mainPanel.add(otpLabel, gbc);
        gbc.gridy++;
        otpField = new JTextField();
        otpField.setPreferredSize(new Dimension(0, 35));
        otpField.setBorder(new RoundBorder(5, 1));
        otpField.setEnabled(false);
        mainPanel.add(otpField, gbc);

        //Kirim OTP Button
        gbc.gridy++;
        JButton sendOtpButton = new JButton("Kirim OTP");
        sendOtpButton.setFont(buttonFont);
        sendOtpButton.setBackground(primaryColor);
        sendOtpButton.setForeground(Color.WHITE);
        sendOtpButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        mainPanel.add(sendOtpButton, gbc);

        //Register Button
        gbc.gridy++;
        JButton registerButton = new JButton("Register");
        registerButton.setFont(buttonFont);
        registerButton.setBackground(primaryColor);
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        registerButton.setEnabled(false);
        mainPanel.add(registerButton, gbc);

        sendOtpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String email = emailField.getText();

                if (!password.equals(confirmPassword)) {
                    feedbackLabel.setText("Password dan Konfirmasi Password tidak sama!");
                    return;
                } else {
                    feedbackLabel.setText("");
                }

                User user = new User(username, password, email);
                if (userController.sendOTP(user)) {
                    if (userController.saveUser(user, user.getOtp())) {
                        JOptionPane.showMessageDialog(RegistrationView.this, "OTP Send To Email, Please Check Your Email");
                        otpField.setEnabled(true);
                        registerButton.setEnabled(true);
                        sendOtpButton.setEnabled(false);
                        savedOtp = user.getOtp();
                    } else {
                        feedbackLabel.setText("Save User Failed!");
                    }
                } else {
                    feedbackLabel.setText("Send OTP Failed!");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String email = emailField.getText();
                String otp = otpField.getText();


                if (!password.equals(confirmPassword)) {
                    feedbackLabel.setText("Password dan Konfirmasi Password tidak sama!");
                    return;
                } else {
                    feedbackLabel.setText("");
                }

                User user = new User(username, password, email);
                if (userController.registerUser(user, otp)) {
                    JOptionPane.showMessageDialog(RegistrationView.this, "Registration successful!");
                    dispose();
                } else {
                    feedbackLabel.setText("Registration failed, OTP Invalid or Something wrong!");
                }
            }
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


    public void display() {
        setVisible(true);
    }
}