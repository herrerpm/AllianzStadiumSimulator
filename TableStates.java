import javax.swing.*;
import java.awt.*;

public class TableStates {

    public TableStates(){
        JFrame ventanaDeTabla = new JFrame("Tabla de estados ");
        ventanaDeTabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaDeTabla.setSize(800, 600);
        JPanel panel = new JPanel(new GridLayout(15,15));
        String[] columnHeaders = {
                "Categoría", "Nombre", "Cantidad"
        };


        Object[][] tableData = {
                //Estados de Threads
                {"Estados de Threads", "Threads en Runnable", 0},
                {"Estados de Threads", "Threads en Timed Waiting", 0},
                {"Estados de Threads", "Threads en Blocked", 0},
                {"Estados de Threads", "Threads en Terminated", 0},
                //Estados de agente
                //Jugador
                {"Estados de Jugador", "Threads jugando", 0},
                {"Estados de Jugador", "Threads en banca", 0},
                //Vendedor de boletos
                {"Estados de vendedor Boletos", "Threads atendiendo", 0},
                {"Estados de Vendedor Boletos", "Threads esperando clientes", 0},
                //Vendedor de comida
                {"Estados de vendedor comida", "Threads atendiendo", 0},
                {"Estados de Vendedor comida", "Threads esperando clientes", 0},
                //Aficionado
                {"Estados de Aficionado", "Threads Caminando a taquilla", 0},
                {"Estados de Aficionado", "Threads Comprando Boleto", 0},
                {"Estados de Aficionado", "Threads Registrando Boleto", 0},
                {"Estados de Aficionado", "Threads en el baño", 0},
                {"Estados de Aficionado", "Threads viendo partido", 0},
                {"Estados de Aficionado", "Threads comprando comida", 0},
                //Buffers
                {"Buffers", "Entrada de taquilla", 0},
                {"Buffers", "Baños", 0},
                {"Buffers", "Gradas", 0},
                {"Buffers", "En el estadio", 0},
                //Zonas criticas
                {"Zonas Críticas", "Compra de comida", 0},
                {"Zonas Críticas", "Compra de boleto", 0},
                {"Zonas Críticas", "Registro de boleto", 0},
        };

        JTable table = new JTable(tableData, columnHeaders);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);

        ventanaDeTabla.add(scrollPane, BorderLayout.CENTER);
        ventanaDeTabla.setVisible(true);

    }

}
