package Windows;

import Handlers.SystemHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class InputWindow {
    private static InputWindow instance;
    private JFrame frame;
    private final Map<String, JTextField> inputFields = new HashMap<>();
    private final Map<String, Integer> inputValues = new HashMap<>();

    public InputWindow(){
        frame = new JFrame("Datos de entrada");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Use a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Panel for input fields using GridBagLayout for flexibility
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Map<String, Integer> variables = SystemHandler.getInstance().getInputVariables();
        int row = 0;

        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            String key = entry.getKey();
            Integer defaultValue = entry.getValue();

            // Label constraints
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.3;
            JLabel label = new JLabel(key + ":");
            inputPanel.add(label, gbc);

            // TextField constraints
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            JTextField textField = new JTextField(15);
            textField.setText(String.valueOf(defaultValue));
            inputFields.put(key, textField);
            inputPanel.add(textField, gbc);

            row++;
        }

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Panel for the submit button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Confirmar");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput();
            }
        });
        buttonPanel.add(submitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.pack(); // Let the frame size itself based on components
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true);
    }

    public static InputWindow getInstance() {
        if (instance == null) {
            instance = new InputWindow();
        }
        return instance;
    }

    private void handleInput() {
        try {
            // Get input values from Map
            for (Map.Entry<String, JTextField> entry : inputFields.entrySet()) {
                String key = entry.getKey();
                JTextField textField = entry.getValue();
                int value = Integer.parseInt(textField.getText());
                inputValues.put(key, value);
            }

            System.out.println("Input values saved successfully:");
            for (Map.Entry<String, Integer> entry : inputValues.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            SystemHandler.getInstance().updateVariables(inputValues);
            SystemHandler.getInstance().instantiateAgents();

            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
