package Presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {

    //CONSTRUCTOR DE CLASE (DEFINE COMPONENTES Y LOS COLOCA)
    public MenuPrincipal() {
        //LA IDEA ES QUE TENGA EL LOOK AND FEEL TIPO "NIMBUS", SI NO EXIESTE UTILIZA EL DEFAULT
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus no está disponible, usar el default
            System.out.println("Nimbus no disponible, usando look and feel por defecto");
        }

        //TAMAÑO DE LA VENTANA PRINCIPAL (DEFINICION DE JFRAME)
        setTitle("Sistema Bag of Words - Tickets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        //DEFINICION DE PANEL EN EL CUAL VAN A ESTAR LOS COMPONENTES
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(6, 1, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        //DEFINICION DE BOTONES
        JButton btnUsuarios = new JButton("GESTIÓN DE USUARIOS");
        JButton btnDepartamentos = new JButton("GESTIÓN DE DEPARTAMENTOS");
        JButton btnTickets = new JButton("GESTIÓN DE TICKETS");
        JButton btnAnalisis = new JButton("ANÁLISIS BAG OF WORDS");
        JButton btnDiccionarios = new JButton("GESTIÓN DE DICCIONARIOS");
        JButton btnInformacion = new JButton("INFORMACIÓN");

        //ESTILO DE LOS BOTONES
        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 14);
        btnUsuarios.setFont(fuenteBotones);
        btnDepartamentos.setFont(fuenteBotones);
        btnTickets.setFont(fuenteBotones);
        btnAnalisis.setFont(fuenteBotones);
        btnDiccionarios.setFont(fuenteBotones);
        btnInformacion.setFont(fuenteBotones);

        //AÑADIR BOTONES AL PANEL
        panelPrincipal.add(btnUsuarios);
        panelPrincipal.add(btnDepartamentos);
        panelPrincipal.add(btnTickets);
        panelPrincipal.add(btnAnalisis);
        panelPrincipal.add(btnDiccionarios);
        panelPrincipal.add(btnInformacion);

        //COLOCAR EL PANEL AL JFRAME
        add(panelPrincipal);

        //FUNCIONES DE CADA BOTON
        btnUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaUsuarios.abrirVentana();
            }
        });
        btnDepartamentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaDepartamentos.abrirVentana();
            }
        });
        btnTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaTickets.abrirVentana();
            }
        });
        btnAnalisis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaAnalisis.abrirVentana();
            }
        });
        btnDiccionarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaDiccionarios.abrirVentana();
            }
        });
        btnInformacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuPrincipal.this,
                        "Sistema de Análisis Bag of Words\n" +
                                "Curso: Programación Orientada a Objetos\n" +
                                "Desarrollado por: Arturo Gutiérrez\n" +
                                "Profesor: Juan de Dios Murillo\n" +
                                "Año: 2025",
                        "Información del Sistema",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
