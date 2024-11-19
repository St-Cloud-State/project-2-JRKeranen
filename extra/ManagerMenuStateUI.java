package WarehouseStage1;

import java.util.*;
import WarehouseStage1.WareState;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ManagerMenuStateUI extends WareState {
    // Static password for manager authentication
    private static final String MANAGER_PASSWORD = "admin123";

    // Shared warehouse instance
    private static Warehouse warehouse;

    // Frame for the GUI
    private JFrame frame;

    // Constructor: Initialize the manager state with the given JFrame
    public ManagerMenuStateUI(JFrame frame) {
        this.frame = frame;
        warehouse = new Warehouse.instance(); // Initialize the warehouse instance
        initializeUI();              // Set up the GUI components
    }

    // Method to initialize the user interface for the Manager panel
    private void initializeUI() {
        frame.getContentPane().removeAll();  // Clear any existing UI elements
        frame.setLayout(new BorderLayout()); // Use BorderLayout for flexibility

        // Create a panel for buttons and labels
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1)); // Arrange components in a grid

        // Add a label for the Manager Panel
        JLabel label = new JLabel("Manager Panel");
        label.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        panel.add(label);

        // Add a button for modifying product prices
        JButton modifyPriceButton = new JButton("Modify Product Price");
        modifyPriceButton.addActionListener(e -> modifyProductPrice());
        panel.add(modifyPriceButton);

        // Add a button for receiving shipments
        JButton receiveShipmentButton = new JButton("Receive Shipment");
        receiveShipmentButton.addActionListener(e -> receiveShipment());
        panel.add(receiveShipmentButton);

        // Add a button for freezing or unfreezing client accounts
        JButton freezeUnfreezeButton = new JButton("Freeze/Unfreeze Client");
        freezeUnfreezeButton.addActionListener(e -> freezeUnfreezeClient());
        panel.add(freezeUnfreezeButton);

        // Add a button for logging out
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton);

        // Add the panel to the frame and refresh the UI
        frame.add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // Method to modify the price of a product
    private void modifyProductPrice() {
        // Prompt the user for the product ID
        String productId = JOptionPane.showInputDialog(frame, "Enter Product ID:");
        Product product = warehouse.searchProductByID(productId); // Search for the product

        if (product != null) {
            // Prompt for the new price if the product is found
            String newPriceStr = JOptionPane.showInputDialog(frame, "Enter New Price:");
            try {
                float newPrice = Float.parseFloat(newPriceStr);  // Parse the price
                product.setPrice(newPrice);                      // Update the product's price
                JOptionPane.showMessageDialog(frame, "Price updated successfully!");
            } catch (NumberFormatException e) {
                // Handle invalid price input
                JOptionPane.showMessageDialog(frame, "Invalid price entered!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Inform the user if the product is not found
            JOptionPane.showMessageDialog(frame, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to receive a shipment and update product quantities
    private void receiveShipment() {
        // Prompt for the product ID and quantity
        String productId = JOptionPane.showInputDialog(frame, "Enter Product ID:");
        String quantityStr = JOptionPane.showInputDialog(frame, "Enter Quantity:");

        try {
            int quantity = Integer.parseInt(quantityStr);   // Parse the quantity
            warehouse.ReceiveShipment(productId, quantity); // Update the warehouse
            JOptionPane.showMessageDialog(frame, "Shipment received successfully!");
        } catch (NumberFormatException e) {
            // Handle invalid quantity input
            JOptionPane.showMessageDialog(frame, "Invalid quantity entered!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to freeze or unfreeze a client account
    private void freezeUnfreezeClient() {
        // Prompt for the client ID
        String clientId = JOptionPane.showInputDialog(frame, "Enter Client ID:");
        Client client = warehouse.searchClientByID(clientId); // Search for the client

        if (client != null) {
            // Show the current status and ask for confirmation
            int option = JOptionPane.showConfirmDialog(frame, 
                "Do you want to freeze/unfreeze the client?\nCurrent status: " + 
                (client.isFrozen() ? "Frozen" : "Active"),
                "Freeze/Unfreeze Client",
                JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Toggle the frozen status
                client.setFrozen(!client.isFrozen());
                JOptionPane.showMessageDialog(frame, 
                    "Client status updated to: " + (client.isFrozen() ? "Frozen" : "Active"));
            }
        } else {
            // Inform the user if the client is not found
            JOptionPane.showMessageDialog(frame, "Client not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to log out of the Manager state and return to the login screen
    private void logout() {
        new LoginState(frame); // Transition back to the LoginState (assuming it's implemented)
    }

    // Static method to authenticate the manager using a password
    public static boolean authenticateManager(JFrame frame) {
        // Prompt for the manager password
        String password = JOptionPane.showInputDialog(frame, "Enter Manager Password:");
        return MANAGER_PASSWORD.equals(password); // Check if the password matches
    }

    // Method to display the Manager panel if authentication is successful
    public void display() {
        if (authenticateManager(frame)) {
            initializeUI(); // Show the Manager panel
        } else {
            JOptionPane.showMessageDialog(frame, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
            logout(); // Log out on failed authentication
        }
    }
}