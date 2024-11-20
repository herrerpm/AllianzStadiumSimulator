package Tables;

import Agents.FanAgent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * FanTable is responsible for displaying the states of FanAgent threads
 * in a separate JFrame. It provides methods to initialize the table and
 * update its contents dynamically.
 */
public class FanTable {
    private final DefaultTableModel fanTableModel;
    private final JFrame fanFrame;

    /**
     * Constructor initializes the fan table GUI.
     */
    public FanTable() {
        // Initialize the frame
        fanFrame = new JFrame("Estados de FanAgent Threads");
        fanFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fanFrame.setSize(600, 400);
        fanFrame.setLocationRelativeTo(null); // Centers the frame on the screen

        // Define column headers
        String[] fanColumnHeaders = {"Nombre", "Estado"};

        // Initialize the table model with column headers and 0 rows
        fanTableModel = new DefaultTableModel(fanColumnHeaders, 0) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create the table with the model
        JTable fanTable = new JTable(fanTableModel);
        fanTable.setFillsViewportHeight(true);
        fanTable.setRowHeight(30);
        fanTable.setGridColor(Color.LIGHT_GRAY);
        fanTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(fanTable);

        // Add the scroll pane to the frame
        fanFrame.add(scrollPane, BorderLayout.CENTER);

        // Make the frame visible
        fanFrame.setVisible(true);
    }

    /**
     * Updates the fan table with the latest states of FanAgent threads.
     *
     * @param fans List of FanAgent instances to display.
     */
    public void updateFanTable(List<FanAgent> fans) {
        // Ensure updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Clear existing rows
            fanTableModel.setRowCount(0);

            // Populate the table with current fan states
            for (FanAgent fan : fans) {
                fanTableModel.addRow(new Object[]{fan.getName(), fan.getCurrentState()});
            }
        });
    }

    /**
     * Closes the fan table window.
     */
    public void close() {
        fanFrame.dispose();
    }
}
