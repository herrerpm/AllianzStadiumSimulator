import Agents.FanAgent;
import Agents.FanHandler;
import Agents.SellingHandler;
import Agents.TicketSellerAgent;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import javax.swing.table.DefaultTableModel;

public class TableStates {
    private final DefaultTableModel tableModel;
    private final DefaultTableModel fanTableModel;
    private final DefaultTableModel sellerTableModel;

    private List<Thread> fanThreads = new ArrayList<>();
    private List<Thread> sellerThreads = new ArrayList<>();

    private final SellingHandler sellingHandler;
    private final FanHandler fanHandler;
    private JFrame fanFrame;
    private JFrame sellerFrame;

    public TableStates(){
        sellingHandler = SellingHandler.getInstance(InputWindow.vendedoresBolestos);
        fanHandler = FanHandler.getInstance();
        System.out.println(InputWindow.nfans);
        JFrame ventanaDeTabla = new JFrame("Tabla de estados");
        JFrame ventanaDeFan = new JFrame("Tabla de Estados de Fan");
        ventanaDeTabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaDeFan.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            FanHandler fans;
        ventanaDeTabla.setSize(800, 600);
        ventanaDeFan.setSize(600,600);
        JPanel panel = new JPanel(new GridLayout(15,15));
        String[] columnHeaders = {
                "Categoría", "Nombre", "Cantidad"
        };



        Object[][] tableData = {
                //Estados de Threads
                {"Estados de Threads", "Threads en Runnable", Thread.activeCount()},
                {"Estados de Threads", "Threads en Timed Waiting", 0},
                {"Estados de Threads", "Threads en Waiting", 0},
                {"Estados de Threads", "Threads en Blocked", 0},
                {"Estados de Threads", "Threads en Terminated", 0},
                //Estados de agente
                //Jugador
                {"Estados de Jugador", "Threads jugando", 0},
                {"Estados de Jugador", "Threads en banca", 0},
                //Vendedor de boletos
                {"Estados de vendedor Boletos", "Threads atendiendo", sellingHandler.getAvailableSellers().size()},
                {"Estados de Vendedor Boletos", "Threads esperando clientes", sellingHandler.getSellingSellers().size()},
                //Vendedor de comida
                {"Estados de vendedor comida", "Threads atendiendo", InputWindow.vendedoresComida},
                {"Estados de Vendedor comida", "Threads esperando clientes", 0},
                //Aficionado
                {"Estados de Aficionado", "Threads entrando a estadio", },
                {"Estados de Aficionado", "Threads Comprando Boleto", fanHandler.getFanBuyingTicketCount()},
                {"Estados de Aficionado", "Threads en zona General", fanHandler.getFanGeneralZoneCount()},
                {"Estados de Aficionado", "Threads en el baño", fanHandler.getFanBathroomCount()},
                {"Estados de Aficionado", "Threads viendo partido", fanHandler.getFanWatchingGameCount()},
                {"Estados de Aficionado", "Threads comprando comida", fanHandler.getFanBuyingFoodCount()},
                //Buffers
                {"Buffers", "Entrada de taquilla", 0},
                {"Buffers", "Baños", InputWindow.capacidadBaños},
                {"Buffers", "Gradas", 0},
                {"Buffers", "En el estadio", 0},
                //Zonas criticas
                {"Zonas Críticas", "Compra de comida", 0},
                {"Zonas Críticas", "Compra de boleto", 0},
                {"Zonas Críticas", "Registro de boleto", 0},
        };

        tableModel = new DefaultTableModel(tableData, columnHeaders);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);

        ventanaDeTabla.add(scrollPane, BorderLayout.CENTER);
        ventanaDeTabla.setVisible(true);

        fanFrame = new JFrame("Estados de FanAgent Threads");
        fanFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fanFrame.setSize(600, 400);
        String[] fanColumnHeaders = {"Nombre", "Estado"};
        fanTableModel = new DefaultTableModel(fanColumnHeaders, 0);
        JTable fanTable = new JTable(fanTableModel);
        fanFrame.add(new JScrollPane(fanTable), BorderLayout.CENTER);
        fanFrame.setVisible(true);

        sellerFrame = new JFrame("Estados de SellerAgent Threads");
        sellerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sellerFrame.setSize(600, 400);
        String[] sellerColumnHeaders = {"Nombre", "Estado"};
        sellerTableModel = new DefaultTableModel(sellerColumnHeaders, 0);
        JTable sellerTable = new JTable(sellerTableModel);
        sellerFrame.add(new JScrollPane(sellerTable), BorderLayout.CENTER);
        sellerFrame.setVisible(true);


        startTableUpdate();



    }

    private void updateFanTable() {
        fanTableModel.setRowCount(0);
        for (FanAgent fan : fanHandler.getFanAgents()) {
            fanTableModel.addRow(new Object[]{fan.getName(), fan.getCurrentState()});
        }
    }

    private void updateSellerTable() {
        sellerTableModel.setRowCount(0);
        for (TicketSellerAgent seller : sellingHandler.getSellers()) {
            sellerTableModel.addRow(new Object[]{seller.getName(), seller.getCurrentState()});
        }
    }


    public void InstantiateAgents(){

        FanHandler.getInstance().createFans(InputWindow.nfans, sellingHandler, 5);

    }

    private void startTableUpdate() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Actualizado");
                SwingUtilities.invokeLater(() -> {
                    updateSellerTable();
                    updateFanTable();
                    int[] stateCounts = {0, 0, 0, 0, 0};
                    Set<Thread> threads = Thread.getAllStackTraces().keySet();

                    for (Thread thread : threads) {
                        if (thread != null) {
                            System.out.println("Thread state: " + thread.getState() + " - Thread: " + thread.getName());
                            switch (thread.getState()) {
                                case RUNNABLE: stateCounts[0]++; break;
                                case TIMED_WAITING: stateCounts[1]++; break;
                                case WAITING:stateCounts[2]++;break;
                                case BLOCKED: stateCounts[3]++; break;
                                case TERMINATED: stateCounts[4]++; break;
                                default: break;
                            }
                        }
                    }

                    tableModel.setValueAt(stateCounts[0], 0, 2);
                    tableModel.setValueAt(stateCounts[1], 1, 2);
                    tableModel.setValueAt(stateCounts[2], 2, 2);
                    tableModel.setValueAt(stateCounts[3], 3, 2);
                    tableModel.setValueAt(sellingHandler.getSellingSellers().size(), 6, 2);
                    tableModel.setValueAt(sellingHandler.getAvailableSellers().size(), 7, 2);

                    tableModel.setValueAt(sellingHandler.getAvailableSellers().size(), 10, 2);
                    tableModel.setValueAt(fanHandler.getFanBuyingTicketCount(), 11, 2);
                    tableModel.setValueAt(fanHandler.getFanGeneralZoneCount(), 12, 2);
                    tableModel.setValueAt(fanHandler.getFanBathroomCount(), 13, 2);
                    tableModel.setValueAt(fanHandler.getFanWatchingGameCount(), 14, 2);
                    tableModel.setValueAt(fanHandler.getFanBuyingFoodCount(), 15, 2);

                });
            }
        }, 0, 1000);
    }


}
