// File: src/InputWindow.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Buffers.BathroomBuffer; // Import BathroomBuffer

public class InputWindow {
    private JFrame frame;
    private JTextField fansInput, capacidadEstadioInput, vendedoresBolestosInput, vendedoresComidaInput, jugadoresInput, capacidadBañosInput, ticketSellerTimeInput, foodSellerTimeInput;
    public static int nfans, capacidadEstadio, vendedoresBolestos, vendedoresComida, njugadores, capacidadBaños, ticketSellerTime, foodSellerTime; //tiempo de boletos, tiempo de comida

    public InputWindow(){
        frame = new JFrame("Datos de entrada");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 450); // Increased size to accommodate additional fields
        frame.setLayout(new GridLayout(11, 2, 10, 10));

        JLabel fansLabel = new JLabel("Numero de fans:");
        fansInput = new JTextField("20");

        JLabel stadiumCapacityLabel = new JLabel("Capacidad de estadio:");
        capacidadEstadioInput = new JTextField("100");

        JLabel ticketSellersLabel = new JLabel("Vendedores de tickets:");
        vendedoresBolestosInput = new JTextField("5");

        JLabel foodSellersLabel = new JLabel("Vendedores de comida:");
        vendedoresComidaInput = new JTextField("5");

        JLabel playersLabel = new JLabel("Jugadores:");
        jugadoresInput = new JTextField("5");

        JLabel bathroomCapacityLabel = new JLabel("Capacidad de los baños:");
        capacidadBañosInput = new JTextField("6");

        JLabel ticketSellerTimeAttending = new JLabel("Tiempo de venta de boletos (ms):");
        ticketSellerTimeInput = new JTextField("3000");

        JLabel foodSellerTimeAttending = new JLabel("Tiempo de venta de comida (ms):");
        foodSellerTimeInput = new JTextField("3000");

        frame.add(fansLabel);
        frame.add(fansInput);
        frame.add(stadiumCapacityLabel);
        frame.add(capacidadEstadioInput);
        frame.add(ticketSellersLabel);
        frame.add(vendedoresBolestosInput);
        frame.add(foodSellersLabel);
        frame.add(vendedoresComidaInput);
        frame.add(playersLabel);
        frame.add(jugadoresInput);
        frame.add(bathroomCapacityLabel);
        frame.add(capacidadBañosInput);
        frame.add(ticketSellerTimeAttending);
        frame.add(ticketSellerTimeInput);
        frame.add(foodSellerTimeAttending);
        frame.add(foodSellerTimeInput);

        JButton submitButton = new JButton("Confirmar");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput();
            }
        });
        frame.add(new JLabel());
        frame.add(submitButton);

        frame.setVisible(true);
    }

    private void handleInput() {
        try {
            nfans = Integer.parseInt(fansInput.getText());
            capacidadEstadio = Integer.parseInt(capacidadEstadioInput.getText());
            vendedoresBolestos = Integer.parseInt(vendedoresBolestosInput.getText());
            vendedoresComida = Integer.parseInt(vendedoresComidaInput.getText());
            njugadores = Integer.parseInt(jugadoresInput.getText());
            capacidadBaños = Integer.parseInt(capacidadBañosInput.getText());
            foodSellerTime = Integer.parseInt(foodSellerTimeInput.getText());
            ticketSellerTime = Integer.parseInt(ticketSellerTimeInput.getText());

            // Initialize the BathroomBuffer singleton with the specified capacity
            BathroomBuffer.getInstance(capacidadBaños);

            TableStates tableStates = new TableStates();
            // tableStates.InstantiateAgents(); // Instantiation handled in TableStates constructor

            System.out.println("Fans: " + nfans);
            System.out.println("Stadium Capacity: " + capacidadEstadio);
            System.out.println("Ticket Sellers: " + vendedoresBolestos);
            System.out.println("Food Sellers: " + vendedoresComida);
            System.out.println("Players: " + njugadores);
            System.out.println("Bathroom Capacity: " + capacidadBaños);
            System.out.println("Food Seller time attending: " + foodSellerTime + " ms");
            System.out.println("Ticket Seller time attending: " + ticketSellerTime + " ms");

            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
