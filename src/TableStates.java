// File: TableStates.java

import Agents.FanAgent;
import Agents.TicketSellerAgent;
import Handlers.FanHandler;
import Handlers.SellingHandler;
import Managers.ThreadManager;
import Managers.TransactionManager;
import Tables.AgentTable;
import Tables.AgentTableFactory;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;
import javax.swing.table.DefaultTableModel;

/**
 * TableStates is responsible for displaying the overall system states,
 * including thread states, agent states, buffers, and critical zones.
 * It integrates separate tables for fan agents and seller agents.
 */
public class TableStates {
    private final DefaultTableModel tableModel;
    private final SellingHandler sellingHandler;
    private final FanHandler fanHandler;
    private JFrame ventanaDeTabla;

    private AgentTable<FanAgent.AgentState, FanAgent> fanTable;
    private AgentTable<TicketSellerAgent.AgentState, TicketSellerAgent> sellerTable;

    /**
     * Constructor initializes the main table and integrates FanTable and SellerTable.
     */
    public TableStates(){

        sellingHandler = SellingHandler.getInstance();
        fanHandler = FanHandler.getInstance();
        System.out.println("Number of Fans before instantiation: " + InputWindow.nfans);

        // Instantiate agents **before** configuring the TransactionManager
        InstantiateAgents();

        // Configure the TransactionManager with dependencies and configurations
        TransactionManager.getInstance().configure(
                fanHandler,
                sellingHandler,
                InputWindow.sellerTime
        );

        System.out.println("Number of Ticket Sellers after instantiation: " + sellingHandler.getAgents().size());

        // Initialize the main table frame
        ventanaDeTabla = new JFrame("Tabla de estados");
        ventanaDeTabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaDeTabla.setSize(800, 600);
        ventanaDeTabla.setLocationRelativeTo(null); // Centers the frame on the screen

        // Define column headers
        String[] columnHeaders = {
                "Categoría", "Nombre", "Cantidad"
        };

        // Initialize table data
        Object[][] tableData = {
                // Estados de Threads
                {"Estados de Threads", "Threads en Runnable", ThreadManager.getInstance().getThreadCountByState(Thread.State.RUNNABLE)},
                {"Estados de Threads", "Threads en Timed Waiting", ThreadManager.getInstance().getThreadCountByState(Thread.State.TIMED_WAITING)},
                {"Estados de Threads", "Threads en Waiting", ThreadManager.getInstance().getThreadCountByState(Thread.State.WAITING)},
                {"Estados de Threads", "Threads en Blocked", ThreadManager.getInstance().getThreadCountByState(Thread.State.BLOCKED)},
                {"Estados de Threads", "Threads en Terminated", ThreadManager.getInstance().getThreadCountByState(Thread.State.TERMINATED)},
                // Estados de agente
                // Jugador
                {"Estados de Jugador", "Threads jugando", 0},
                {"Estados de Jugador", "Threads en banca", 0},
                // Vendedor de boletos
                {"Estados de vendedor Boletos", "Threads atendiendo", sellingHandler.getAgentCountByState(TicketSellerAgent.AgentState.SELLING)},
                {"Estados de Vendedor Boletos", "Threads esperando clientes", sellingHandler.getAgentCountByState(TicketSellerAgent.AgentState.WAITING)},
                // Vendedor de comida
                {"Estados de vendedor comida", "Threads atendiendo", InputWindow.vendedoresComida},
                {"Estados de Vendedor comida", "Threads esperando clientes", 0},
                // Aficionado
                {"Estados de Aficionado", "Threads entrando a estadio", 0},
                {"Estados de Aficionado", "Threads Comprando Boleto", fanHandler.getAgentCountByState(FanAgent.AgentState.BUYING_TICKET)},
                {"Estados de Aficionado", "Threads en zona General", fanHandler.getAgentCountByState(FanAgent.AgentState.GENERAL_ZONE)},
                {"Estados de Aficionado", "Threads en el baño", fanHandler.getAgentCountByState(FanAgent.AgentState.BATHROOM)},
                {"Estados de Aficionado", "Threads viendo partido", fanHandler.getAgentCountByState(FanAgent.AgentState.WATCHING_GAME)},
                {"Estados de Aficionado", "Threads comprando comida", fanHandler.getAgentCountByState(FanAgent.AgentState.BUYING_FOOD)},
                // Buffers
                {"Buffers", "Entrada de taquilla", 0},
                {"Buffers", "Baños", InputWindow.capacidadBaños},
                {"Buffers", "Gradas", 0},
                {"Buffers", "En el estadio", 0},
                // Zonas criticas
                {"Zonas Críticas", "Compra de comida", 0},
                {"Zonas Críticas", "Compra de boleto", 0},
                {"Zonas Críticas", "Registro de boleto", 0},
        };

        // Initialize the main table model
        tableModel = new DefaultTableModel(tableData, columnHeaders) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create the main table with the model
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        ventanaDeTabla.add(scrollPane, BorderLayout.CENTER);

        // Make the main frame visible
        ventanaDeTabla.setVisible(true);


        this.fanTable =
                AgentTableFactory.createAgentTableFromHandler(FanHandler.getInstance(), "Fan Agents Status");

        // Initialize and display the separate SellerTable (optional)
        sellerTable = AgentTableFactory.createAgentTableFromHandler(SellingHandler.getInstance(), "Seller Agents Status");;

        // Start periodic updates
        startTableUpdate();
    }

    /**
     * Instantiates agents by creating fan agents and ticket seller agents.
     */
    public void InstantiateAgents(){
        FanHandler.getInstance().createAgents(InputWindow.nfans);
        SellingHandler.getInstance().createAgents(InputWindow.vendedoresBolestos);
        System.out.println("Agents instantiated: " + InputWindow.nfans + " Fans and " + InputWindow.vendedoresBolestos + " Ticket Sellers.");
    }

    /**
     * Starts the timer to update the tables at fixed intervals.
     */
    private void startTableUpdate() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Actualizado");
                SwingUtilities.invokeLater(() -> {
                    // Update the separate SellerTable
                    sellerTable.updateTable(SellingHandler.getInstance().getAgents(), SellingHandler.getInstance().getAgentThreads());

                    // Update the separate FanTable
                    fanTable.updateTable(FanHandler.getInstance().getAgents(), FanHandler.getInstance().getAgentThreads());

                    // Update the main table

                    // Update thread state counts
                    tableModel.setValueAt(ThreadManager.getInstance().getThreadCountByState(Thread.State.RUNNABLE), 0, 2);
                    tableModel.setValueAt(ThreadManager.getInstance().getThreadCountByState(Thread.State.TIMED_WAITING), 1, 2);
                    tableModel.setValueAt(ThreadManager.getInstance().getThreadCountByState(Thread.State.WAITING), 2, 2);
                    tableModel.setValueAt(ThreadManager.getInstance().getThreadCountByState(Thread.State.BLOCKED), 3, 2);
                    tableModel.setValueAt(ThreadManager.getInstance().getThreadCountByState(Thread.State.TERMINATED), 4, 2);

                    // Update other categories
                    // Jugador
                    tableModel.setValueAt(0, 5, 2); // Threads jugando
                    tableModel.setValueAt(0, 6, 2); // Threads en banca

                    // Vendedor de boletos
                    tableModel.setValueAt(sellingHandler.getAgentCountByState(TicketSellerAgent.AgentState.SELLING), 7, 2);
                    tableModel.setValueAt(sellingHandler.getAgentCountByState(TicketSellerAgent.AgentState.WAITING), 8, 2);

                    // Vendedor de comida
                    tableModel.setValueAt(InputWindow.vendedoresComida, 9, 2);
                    tableModel.setValueAt(0, 10, 2); // Threads esperando clientes

                    // Aficionado
                    tableModel.setValueAt("1", 11, 2);
                    tableModel.setValueAt(fanHandler.getAgentCountByState(FanAgent.AgentState.BUYING_TICKET), 12, 2);
                    tableModel.setValueAt(fanHandler.getAgentCountByState(FanAgent.AgentState.GENERAL_ZONE), 13, 2);
                    tableModel.setValueAt(fanHandler.getAgentCountByState(FanAgent.AgentState.BATHROOM), 14, 2);
                    tableModel.setValueAt(fanHandler.getAgentCountByState(FanAgent.AgentState.WATCHING_GAME), 15, 2);
                    tableModel.setValueAt(fanHandler.getAgentCountByState(FanAgent.AgentState.BUYING_FOOD), 16, 2);

                    // Buffers
                    tableModel.setValueAt(0, 17, 2); // Entrada de taquilla
                    tableModel.setValueAt(InputWindow.capacidadBaños, 18, 2); // Baños
                    tableModel.setValueAt(0, 19, 2); // Gradas
                    tableModel.setValueAt(0, 20, 2); // En el estadio

                    // Zonas críticas
                    tableModel.setValueAt(0, 21, 2); // Compra de comida
                    tableModel.setValueAt(0, 22, 2); // Compra de boleto
                    tableModel.setValueAt(0, 23, 2); // Registro de boleto
                });
            }
        }, 0, 1000); // Update every 1000 milliseconds (1 second)
    }

    /**
     * Closes all tables and performs cleanup.
     */
    public void close() {
        fanTable.close();
        sellerTable.close();
        ventanaDeTabla.dispose();
        // Cancel timers or perform other cleanup if necessary
    }
}
