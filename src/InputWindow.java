import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class InputWindow {
    private JFrame frame;
    private JTextField fansInput, capacidadEstadioInput, vendedoresBolestosInput, vendedoresComidaInput, capacidadBañosInput, SellerTimeInput;
    public static int nfans, capacidadEstadio, vendedoresBolestos, vendedoresComida, capacidadBaños, sellerTime;


    public InputWindow(){
        frame = new JFrame("Datos de entrada");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(10, 2, 10, 10));

        JLabel fansLabel = new JLabel("Numero de fans:");
        fansInput = new JTextField("20");

        JLabel stadiumCapacityLabel = new JLabel("Capacidad de estadio:");
        capacidadEstadioInput = new JTextField("100");

        JLabel ticketSellersLabel = new JLabel("Vendedores de tickets:");
        vendedoresBolestosInput = new JTextField("5");

        JLabel foodSellersLabel = new JLabel("Vendedores de comida:");
        vendedoresComidaInput = new JTextField("5");

        JLabel bathroomCapacityLabel = new JLabel("Capacidad de los baños:");
        capacidadBañosInput = new JTextField("6");

        JLabel SellerTimeAtending = new JLabel("Tiempo de venta de boletos");
        SellerTimeInput = new JTextField("3000");


        frame.add(fansLabel);
        frame.add(fansInput);
        frame.add(stadiumCapacityLabel);
        frame.add(capacidadEstadioInput);
        frame.add(ticketSellersLabel);
        frame.add(vendedoresBolestosInput);
        frame.add(foodSellersLabel);
        frame.add(vendedoresComidaInput);
        frame.add(bathroomCapacityLabel);
        frame.add(capacidadBañosInput);
        frame.add(SellerTimeAtending);
        frame.add(SellerTimeInput);

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
            capacidadBaños = Integer.parseInt(capacidadBañosInput.getText());
            sellerTime = Integer.parseInt(SellerTimeInput.getText());

            TableStates tableStates = new TableStates();
//            tableStates.InstantiateAgents();

            System.out.println("Fans: " + nfans);
            System.out.println("Stadium Capacity: " + capacidadEstadio);
            System.out.println("Ticket Sellers: " + vendedoresBolestos);
            System.out.println("Food Sellers: " + vendedoresComida);
            System.out.println("Bathroom Capacity: " + capacidadBaños);
            System.out.println("Seller time attending: " + sellerTime);

            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


