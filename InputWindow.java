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
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(10, 2, 10, 10));

       //Create inputs and text fields
        Map<String, Integer> variables = SystemHandler.getInstance().getInputVariables();
        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            String key = entry.getKey();
            Integer defaultValue = entry.getValue();

            JLabel label = new JLabel(key + ":");
            JTextField textField = new JTextField(String.valueOf(defaultValue));

            inputFields.put(key, textField);
            frame.add(label);
            frame.add(textField);
        }



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

    public static InputWindow getInstance() {
        if (instance == null) {
            instance = new InputWindow();
        }
        return instance;
    }

    private void handleInput() {
        try {
            //Get input values from Map
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

