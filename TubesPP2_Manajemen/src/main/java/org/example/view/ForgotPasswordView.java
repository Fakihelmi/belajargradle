package org.example.view;

import org.example.controller.UserController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class ForgotPasswordView {

    private JFrame frame;
    private JTextField emailField;
    private JTextField otpField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JPanel mainPanel;
    private JPanel otpPanel;
    private State currentState = State.EMAIL_ENTERED;
    private String email;
    private UserController userController = new UserController();
    private enum State {
        EMAIL_ENTERED,
        OTP_ENTERED,
        PASSWORD_RESET
    }

    public ForgotPasswordView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Lupa Kata Sandi");
        frame.setSize(350, 320);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridBagLayout());
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
        emailField = new JTextField(20);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(emailField, gbc);

        // Submit Email Button
        gbc.gridy++;
        JButton submitEmailButton = new JButton("Kirim OTP");
        submitEmailButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitEmailButton.setBackground(new Color(0, 128, 0));
        submitEmailButton.setForeground(Color.WHITE);
        submitEmailButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)));

        submitEmailButton.addActionListener(this::submitEmail);
        mainPanel.add(submitEmailButton, gbc);

        // OTP Panel (Hidden initially)
        otpPanel = new JPanel(new GridBagLayout());

        // OTP Field
        gbc.gridy++;
        JLabel otpLabel = new JLabel("Kode OTP");
        otpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        otpPanel.add(otpLabel, gbc);

        gbc.gridy++;
        otpField = new JTextField(6);
        otpField.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        otpField.setAlignmentX(Component.LEFT_ALIGNMENT);
        otpPanel.add(otpField, gbc);

        gbc.gridy++;
        JButton verifyOtpButton = new JButton("Verifikasi OTP");
        verifyOtpButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        verifyOtpButton.setBackground(new Color(0, 128, 0));
        verifyOtpButton.setForeground(Color.WHITE);
        verifyOtpButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)));
        verifyOtpButton.addActionListener(this::verifyOtp);
        otpPanel.add(verifyOtpButton,gbc);


        // Password Panel (Hidden initially)
        JPanel passwordPanel = new JPanel(new GridBagLayout());

        // New Password
        gbc.gridy++;
        JLabel newPasswordLabel = new JLabel("Kata Sandi Baru");
        newPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordPanel.add(newPasswordLabel, gbc);

        gbc.gridy++;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        newPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordPanel.add(newPasswordField, gbc);

        // Confirm New Password
        gbc.gridy++;
        JLabel confirmPasswordLabel = new JLabel("Konfirmasi Kata Sandi Baru");
        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordPanel.add(confirmPasswordLabel, gbc);

        gbc.gridy++;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        confirmPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordPanel.add(confirmPasswordField, gbc);


        gbc.gridy++;
        JButton resetPasswordButton = new JButton("Reset Kata Sandi");
        resetPasswordButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetPasswordButton.setBackground(new Color(0, 128, 0));
        resetPasswordButton.setForeground(Color.WHITE);
        resetPasswordButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)));
        resetPasswordButton.addActionListener(this::resetPassword);
        passwordPanel.add(resetPasswordButton,gbc);

        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(new JPanel(), BorderLayout.CENTER);
    }

    private void submitEmail(ActionEvent e) {
        this.email = emailField.getText();
        // Implementasikan disini logika untuk kirim OTP ke Email
        if (userController.sendResetPasswordOTP(email)){
            JOptionPane.showMessageDialog(frame, "OTP telah dikirim ke email anda.");
            mainPanel.removeAll();
            frame.remove(mainPanel);
            frame.setSize(350, 350);
            frame.add(otpPanel, BorderLayout.NORTH);
            frame.revalidate();
            frame.repaint();
            this.currentState = State.OTP_ENTERED;
        } else {
            JOptionPane.showMessageDialog(frame, "Gagal mengirimkan OTP.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verifyOtp(ActionEvent e){
        String otp = otpField.getText();
        if(userController.verifyResetPasswordOTP(email, otp)){
            JOptionPane.showMessageDialog(frame, "Kode OTP valid!");
            frame.remove(otpPanel);
            frame.setSize(350, 450);
            JPanel passwordPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 0, 5, 0);

            // New Password
            JLabel newPasswordLabel = new JLabel("Kata Sandi Baru");
            newPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            passwordPanel.add(newPasswordLabel, gbc);

            gbc.gridy++;
            newPasswordField = new JPasswordField(20);
            newPasswordField.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(10, 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            newPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
            passwordPanel.add(newPasswordField, gbc);

            // Confirm New Password
            gbc.gridy++;
            JLabel confirmPasswordLabel = new JLabel("Konfirmasi Kata Sandi Baru");
            confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            passwordPanel.add(confirmPasswordLabel, gbc);

            gbc.gridy++;
            confirmPasswordField = new JPasswordField(20);
            confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(10, 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            confirmPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
            passwordPanel.add(confirmPasswordField, gbc);

            gbc.gridy++;
            JButton resetPasswordButton = new JButton("Reset Kata Sandi");
            resetPasswordButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            resetPasswordButton.setBackground(new Color(0, 128, 0));
            resetPasswordButton.setForeground(Color.WHITE);
            resetPasswordButton.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(10, 1),
                    new EmptyBorder(10, 10, 10, 10)));
            resetPasswordButton.addActionListener(this::resetPassword);
            passwordPanel.add(resetPasswordButton,gbc);

            frame.add(passwordPanel, BorderLayout.NORTH);
            frame.revalidate();
            frame.repaint();
            this.currentState = State.PASSWORD_RESET;

        } else {
            JOptionPane.showMessageDialog(frame, "Kode OTP tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPassword(ActionEvent e) {
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if(!newPassword.equals(confirmPassword)){
            JOptionPane.showMessageDialog(frame, "Konfirmasi kata sandi tidak sesuai.", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            //Implementasikan disini logic untuk update password
            if (userController.resetPassword(email, newPassword)){
                JOptionPane.showMessageDialog(frame, "Kata sandi berhasil diubah.");
                frame.dispose();
            }else{
                JOptionPane.showMessageDialog(frame, "Gagal mereset password.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    public void display() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
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