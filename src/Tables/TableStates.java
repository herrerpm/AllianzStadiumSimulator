
package Tables;// File: Tables.TableStates.java

import Agents.FanAgent;
import Agents.FoodSellerAgent;
import Agents.PlayerAgent;
import Agents.TicketSellerAgent;
import Handlers.*;
import Managers.ThreadManager;
import Agents.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class TableStates {

    // Declare your handlers and managers
    private final PlayerHandler playerHandler;
    private final TicketSellingHandler ticketSellerHandler;
    private final FoodSellingHandler foodSellerHandler;
    private final FanHandler fanHandler;
    private final ThreadManager threadManager;
    private final SystemHandler systemHandler;

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField filterField;
    private TableRowSorter<DefaultTableModel> sorter;

    private AgentTable<FanAgent.AgentState, FanAgent> fanTable;
    private AgentTable<TicketSellerAgent.AgentState, TicketSellerAgent> ticketSellerTable;
    private AgentTable<PlayerAgent.AgentState, PlayerAgent> playerTable;
    private AgentTable<FoodSellerAgent.AgentState, FoodSellerAgent> foodSellerTable;

    public TableStates() {
        // Initialize your handlers and managers
        playerHandler = PlayerHandler.getInstance();
        ticketSellerHandler = TicketSellingHandler.getInstance();
        foodSellerHandler = FoodSellingHandler.getInstance();
        fanHandler = FanHandler.getInstance();
        threadManager = ThreadManager.getInstance();
        systemHandler = SystemHandler.getInstance();

        // Initialize your table and model
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        // Set up table columns
        tableModel.addColumn("Categoría");
        tableModel.addColumn("Estado");
        tableModel.addColumn("Cantidad");
        tableModel.addColumn("Color");


        // Initialize the filter field
        filterField = new JTextField();
        filterField.setPreferredSize(new Dimension(200, 30));

        // Set up the TableRowSorter and assign it to the table
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Add a DocumentListener to the filter field to filter the table dynamically
        filterField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }
        });

        // Set up the GUI
        JFrame frame = new JFrame("Estado de Agentes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Centers the frame on the screen

        // Panel for the filter field
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Category: "));
        filterPanel.add(filterField);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add components to the frame
        frame.add(filterPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Make the main frame visible
        frame.setVisible(true);

        fanTable = AgentTableFactory.createAgentTableFromHandler(FanHandler.getInstance(), "Fan Agents Status");
        ticketSellerTable = AgentTableFactory.createAgentTableFromHandler(TicketSellingHandler.getInstance(), "Ticket Seller Agents Status");
        playerTable = AgentTableFactory.createAgentTableFromHandler(PlayerHandler.getInstance(), "Player Agents Status");
        foodSellerTable = AgentTableFactory.createAgentTableFromHandler(FoodSellingHandler.getInstance(), "Food Seller Agent Status");

        // Start the table update process
        startTableUpdate();
    }

    private void applyFilter() {
        String text = filterField.getText();
        if (text.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            // Filter based on the "Categoría" column (index 0)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0));
        }
    }

    private void startTableUpdate() {
        // Create a timer to update the table periodically (every second)
        Timer timer = new Timer(1000, e -> updateTableData());
        timer.start();

    }

    private void updateTableData() {

        // Update the separate SellerTable
        ticketSellerTable.updateTable(TicketSellingHandler.getInstance().getAgents(), TicketSellingHandler.getInstance().getAgentThreads());

        // Update the separate FanTable
        fanTable.updateTable(FanHandler.getInstance().getAgents(), FanHandler.getInstance().getAgentThreads());

        //Update player Table
        playerTable.updateTable(PlayerHandler.getInstance().getAgents(), PlayerHandler.getInstance().getAgentThreads());

        //Update food seller table
        foodSellerTable.updateTable(FoodSellingHandler.getInstance().getAgents(), FoodSellingHandler.getInstance().getAgentThreads());
        // Clear existing data
        tableModel.setRowCount(0);

        // Initialize a list to hold the table data
        List<Object[]> tableDataList = new ArrayList<>();

        // Thread States
        for (Thread.State state : Thread.State.values()) {
            tableDataList.add(new Object[]{
                    "Threads",
                    state.toString(),
                    threadManager.getThreadCountByState(state),
                    null
            });
        }

        // PlayerAgent States
        for (PlayerAgent.AgentState state : PlayerAgent.AgentState.values()) {
            tableDataList.add(new Object[]{
                    "Player",
                    state.toString(),
                    playerHandler.getAgentCountByState(state),
                    playerHandler.getAgentColorForState(state)
            });
        }

        // TicketSellerAgent States
        for (TicketSellerAgent.AgentState state : TicketSellerAgent.AgentState.values()) {
            tableDataList.add(new Object[]{
                    "TicketSeller",
                    state.toString(),
                    ticketSellerHandler.getAgentCountByState(state),
                    ticketSellerHandler.getAgentColorForState(state)
            });
        }

        // FoodSellerAgent States
        for (FoodSellerAgent.AgentState state : FoodSellerAgent.AgentState.values()) {
            tableDataList.add(new Object[]{
                    "FoodSeller",
                    state.toString(),
                    foodSellerHandler.getAgentCountByState(state)
            });
        }

        // FanAgent States
        for (FanAgent.AgentState state : FanAgent.AgentState.values()) {

            tableDataList.add(new Object[]{
                    "Fan",
                    state.toString(),
                    fanHandler.getAgentCountByState(state),
                    fanHandler.getAgentColorForState(state)
            });
        }

        // Buffers
        tableDataList.add(new Object[]{
                "Buffers",
                "Baños",
                systemHandler.getInputVariable("capacidadBaños") - fanHandler.getAgentCountByState(FanAgent.AgentState.BATHROOM)
        });
        tableDataList.add(new Object[]{
                "Buffers",
                "En el estadio",
                SystemHandler.getInstance().getInputVariable("capacidadEstadio") - fanHandler.getAgents().size()
        });

        // Critical Zones (Zonas Críticas)
        FanAgent.AgentState[] criticalStates = {
                FanAgent.AgentState.INLINE_TOBUY_FOOD,
                FanAgent.AgentState.INLINE_TOBUY
        };
        for (FanAgent.AgentState state : criticalStates) {
            tableDataList.add(new Object[]{
                    "Critical Zones",
                    formatStateName(state.toString()),
                    fanHandler.getAgentCountByState(state)
            });
        }

        // Update the table model with new data
        for (Object[] rowData : tableDataList) {
            tableModel.addRow(rowData);
        }
        table.setDefaultRenderer(Object.class, new ColorRenderer());

    }

    private static class ColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 3) {
                Color color = (Color) value;
                c.setBackground(color != null ? color : Color.WHITE);
                setText("");
            } else {
                c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            }
            c.setForeground(Color.BLACK);
            return c;
        }
    }

    private String formatStateName(String stateName) {
        // Format the state name to be more readable
        return stateName.toLowerCase().replace("_", " ");
    }
}