package org.example.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ProfileView extends JFrame {
    private String userEmail;
    public ProfileView(String email) {
        this.userEmail = email;
        setTitle("Profile");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);

        JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.setFont(buttonFont);
        editProfileButton.setBackground(new Color(0, 128, 0));
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        mainPanel.add(editProfileButton, gbc);

        gbc.gridy++;
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setFont(buttonFont);
        changePasswordButton.setBackground(new Color(0, 128, 0));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        mainPanel.add(changePasswordButton, gbc);

        gbc.gridy++;
        JButton categoryButton = new JButton("Category");
        categoryButton.setFont(buttonFont);
        categoryButton.setBackground(new Color(0, 128, 0));
        categoryButton.setForeground(Color.WHITE);
        categoryButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        mainPanel.add(categoryButton, gbc);

        editProfileButton.addActionListener(e -> {
            EditProfileView editProfileView = new EditProfileView(userEmail); // Send email to constructor
            editProfileView.display();
        });

        changePasswordButton.addActionListener(e -> {
            ChangePasswordView changePasswordView = new ChangePasswordView(userEmail);
            changePasswordView.display();
        });

        categoryButton.addActionListener(e -> {
            CategoryView categoryView = new CategoryView();
            categoryView.display();
        });

        add(mainPanel, BorderLayout.CENTER);
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

            Shape round = new RoundRectangle2D.Float(x, y, width-1, height-1, radius, radius);
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