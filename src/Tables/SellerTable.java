package Tables;

import Agents.TicketSellerAgent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * SellerTable is responsible for displaying the states of SellerAgent threads
 * in a separate JFrame. It provides methods to initialize the table and
 * update its contents dynamically.
 */
public class SellerTable {
    private final DefaultTableModel sellerTableModel;
    private final JFrame sellerFrame;

    /**
     * Constructor initializes the seller table GUI.
     */
    public SellerTable() {
        // Initialize the frame
        sellerFrame = new JFrame("Estados de SellerAgent Threads");
        sellerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sellerFrame.setSize(600, 400);
        sellerFrame.setLocationRelativeTo(null); // Centers the frame on the screen

        // Define column headers
        String[] sellerColumnHeaders = {"Nombre", "Estado"};

        // Initialize the table model with column headers and 0 rows
        sellerTableModel = new DefaultTableModel(sellerColumnHeaders, 0) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create the table with the model
        JTable sellerTable = new JTable(sellerTableModel);
        sellerTable.setFillsViewportHeight(true);
        sellerTable.setRowHeight(30);
        sellerTable.setGridColor(Color.LIGHT_GRAY);
        sellerTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(sellerTable);

        // Add the scroll pane to the frame
        sellerFrame.add(scrollPane, BorderLayout.CENTER);

        // Make the frame visible
        sellerFrame.setVisible(true);
    }

    /**
     * Updates the seller table with the latest states of SellerAgent threads.
     *
     * @param sellers List of TicketSellerAgent instances to display.
     */
    public void updateSellerTable(List<TicketSellerAgent> sellers) {
        // Ensure updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Clear existing rows
            sellerTableModel.setRowCount(0);

            // Populate the table with current seller states
            for (TicketSellerAgent seller : sellers) {
                sellerTableModel.addRow(new Object[]{seller.getName(), seller.getCurrentState()});
            }
        });
    }

    /**
     * Closes the seller table window.
     */
    public void close() {
        sellerFrame.dispose();
    }
}
