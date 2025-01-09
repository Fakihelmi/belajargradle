package org.example.controller;

import org.example.model.DatabaseConnection;
import org.example.model.User;
import org.example.util.EmailService;
import org.example.util.OTPGenerator;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    public boolean registerUser(User user, String otp) {
        if (verifyOTP(user.getEmail(),otp)){
            String query = "UPDATE users SET is_verified = 1 WHERE email = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.getEmail());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean saveUser(User user, String otp) {
        if(isEmailRegistered(user.getEmail())){
            return false;
        }
        String query = "INSERT INTO users (username, password, email, is_verified, otp) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getEmail());
            stmt.setBoolean(4, user.isVerified());
            stmt.setString(5, otp);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User user) {
        String query = "UPDATE users SET profile_picture = ?, address = ?, date_of_birth = ?  WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getProfilePicture());
            stmt.setString(2, user.getAddress());
            stmt.setString(3, user.getDateOfBirth());
            stmt.setString(4, user.getEmail());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendOTP(User user) {
        String otp = OTPGenerator.generateOTP();
        String subject = "Verifikasi Registrasi";
        String body = "Kode OTP anda adalah: " + otp;
        user.setOtp(otp);
        if (EmailService.sendEmail(user.getEmail(), subject, body)) {
            return true;
        }
        return false;
    }
    // Method khusus untuk lupa kata sandi
    public boolean sendResetPasswordOTP(String email) {
        String otp = OTPGenerator.generateOTP();
        String subject = "Reset Kata Sandi";
        String body = "Kode OTP anda adalah: " + otp;

        // Simpan OTP ke dalam database.
        String query = "UPDATE users SET otp = ? WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, otp);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        // Kirim email OTP
        if (EmailService.sendEmail(email, subject, body)) {
            return true;
        }
        return false;
    }


    // Perubahan untuk verifikasi OTP
    private boolean verifyOTP(String email, String otp) {
        String query = "SELECT otp FROM users WHERE email = ? ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String savedOTP = rs.getString("otp");
                return savedOTP.equals(otp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    // Method baru untuk verifikasi OTP lupa kata sandi
    public boolean verifyResetPasswordOTP(String email, String otp) {
        return verifyOTP(email, otp);
    }

    // Method baru untuk reset password
    public boolean resetPassword(String email, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailRegistered(String email){
        String query = "SELECT COUNT(*) FROM users WHERE email = ? ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public String loginUser(String email, String password) {
        String query = "SELECT password, is_verified FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean isVerified = rs.getBoolean("is_verified");
                if (!isVerified) {
                    return "Account not verified";
                }
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return "Login Successful";
                } else {
                    return "Invalid Password";
                }
            } else {
                return "Invalid Email";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error Database";
        }
    }
}