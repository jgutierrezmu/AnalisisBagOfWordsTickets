package Presentacion;

import javax.swing.*;
import java.awt.*;

public class VentanaDiccionarios extends JFrame {

    public VentanaDiccionarios() {
        // Configuración general
        setTitle("Gestión de Diccionarios - Sistema Bag of Words");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(3, 1, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Botones principales
        JButton btnDicEmocional = new JButton("DICCIONARIO EMOCIONAL");
        JButton btnDicTecnico = new JButton("DICCIONARIO TÉCNICO");
        JButton btnVolver = new JButton("VOLVER AL MENÚ PRINCIPAL");

        // Estilo de botones
        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 14);
        btnDicEmocional.setFont(fuenteBotones);
        btnDicTecnico.setFont(fuenteBotones);
        btnVolver.setFont(fuenteBotones);

        // Agregar botones al panel
        panelPrincipal.add(btnDicEmocional);
        panelPrincipal.add(btnDicTecnico);
        panelPrincipal.add(btnVolver);

        // Añadir panel al frame
        add(panelPrincipal);

        // Acciones de botones
        btnDicEmocional.addActionListener(e -> {
            VentanaDiccionarioEmocional.abrirVentana();
        });

        btnDicTecnico.addActionListener(e -> {
            VentanaDiccionarioTecnico.abrirVentana();
        });

        btnVolver.addActionListener(e -> {
            dispose();
        });
    }

    //ABRIR VENTANA
    public static void abrirVentana() {
        SwingUtilities.invokeLater(() -> {
            new VentanaDiccionarios().setVisible(true);
        });
    }
}
