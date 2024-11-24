package Tables;

import Agents.AbstractAgent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * AgentTable is a generic table for displaying the states of agents extending AbstractAgent,
 * using the names of their managing threads.
 *
 * @param <S> Enum type representing the state of the agent.
 * @param <A> Type of agent extending AbstractAgent<S>.
 */
public class AgentTable<S extends Enum<S>, A extends AbstractAgent<S>> {
    private final DefaultTableModel tableModel;
    private final JFrame frame;
    private final String[] columnHeaders;

    /**
     * Constructor initializes the agent table GUI with specified column headers.
     *
     * @param title          The title of the JFrame.
     * @param columnHeaders  Array of column header names.
     */
    public AgentTable(String title, String[] columnHeaders) {
        this.columnHeaders = columnHeaders;

        // Initialize the frame
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Centers the frame on the screen

        // Initialize the table model with column headers and 0 rows
        tableModel = new DefaultTableModel(columnHeaders, 0) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create the table with the model
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }

    /**
     * Updates the agent table with the latest states of agents, using thread names as identifiers.
     *
     * @param agents  List of agent instances to display.
     * @param threads List of threads corresponding to the agents.
     */
    public void updateTable(List<A> agents, List<Thread> threads) {
        // Ensure updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Clear existing rows
            tableModel.setRowCount(0);

            // Verify that agents and threads lists are of the same size
            if (agents.size() != threads.size()) {
                throw new IllegalArgumentException("Agents and threads lists must be of the same size.");
            }

            // Populate the table with current agent states and thread names
            int size = agents.size();
            for (int i = 0; i < size; i++) {
                A agent = agents.get(i);
                Thread thread = threads.get(i);
                tableModel.addRow(new Object[]{thread.getName(), agent.getCurrentState()});
            }
        });
    }

    /**
     * Closes the agent table window.
     */
    public void close() {
        frame.dispose();
    }
}
