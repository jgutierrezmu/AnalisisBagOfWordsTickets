package Presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Control.ControladorTicket;
import LogicaDeNegocio.entidades.Ticket;
import LogicaDeNegocio.resultados.ResultadoAnalisis;
import LogicaDeNegocio.texto.BagOfWords;

import java.util.Collection;
import java.util.Map;

public class VentanaAnalisis extends JFrame {

    //COMPONENTES
    private JPanel panelResultados;
    private JTextArea areaTicketOriginal;
    private JTextArea areaAnalisisBoW;
    private JComboBox<String> cmbTickets;
    private DefaultComboBoxModel<String> modeloCombo;

    // CONSTRUCTOR
    public VentanaAnalisis() {
        // CONFIGURAR LA VENTANA DE ANÁLISIS
        setTitle("Análisis Bag of Words - Sistema de Tickets");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);

        // PANEL PRINCIPAL
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // PANEL SUPERIOR PARA SELECCIÓN
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("CONFIGURACIÓN DE ANÁLISIS"));

        JLabel lblTicket = new JLabel("Seleccionar Ticket:");

        // COMBOBOX CON TICKETS REALES
        modeloCombo = new DefaultComboBoxModel<>();
        cmbTickets = new JComboBox<>(modeloCombo);
        cmbTickets.setPreferredSize(new Dimension(400, 30));

        // CARGAR TICKETS REALES
        cargarTicketsReales();

        JButton btnAnalizar = new JButton("ANALIZAR TICKET");
        btnAnalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelSuperior.add(lblTicket);
        panelSuperior.add(cmbTickets);
        panelSuperior.add(btnAnalizar);

        // PANEL CENTRAL CON RESULTADOS
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBorder(BorderFactory.createTitledBorder("RESULTADOS DEL ANÁLISIS"));

        // CREAR LAS 2 SECCIONES: Ticket + Análisis BoW
        JPanel panelTicketOriginal = crearPanelTicketOriginal();
        JPanel panelAnalisis = crearPanelAnalisisBoW();

        // AGREGAR ESPACIADO
        panelTicketOriginal.setBorder(BorderFactory.createCompoundBorder(
                panelTicketOriginal.getBorder(),
                BorderFactory.createEmptyBorder(0, 0, 15, 0)
        ));

        panelAnalisis.setBorder(BorderFactory.createCompoundBorder(
                panelAnalisis.getBorder(),
                BorderFactory.createEmptyBorder(0, 0, 15, 0)
        ));

        panelResultados.add(panelTicketOriginal);
        panelResultados.add(panelAnalisis);

        // INICIALMENTE OCULTAR RESULTADOS
        panelResultados.setVisible(false);

        // BOTÓN VOLVER
        JButton btnVolver = new JButton("VOLVER AL MENÚ PRINCIPAL");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // AGREGAR COMPONENTES
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        JScrollPane scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollResultados.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelPrincipal.add(scrollResultados, BorderLayout.CENTER);
        panelPrincipal.add(btnVolver, BorderLayout.SOUTH);

        add(panelPrincipal);

        //FUNCIONES
        btnAnalizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ticketSeleccionado = (String) cmbTickets.getSelectedItem();

                if (ticketSeleccionado == null || ticketSeleccionado.isEmpty()
                        || ticketSeleccionado.startsWith("NO HAY")
                        || ticketSeleccionado.startsWith("ERROR")) {
                    JOptionPane.showMessageDialog(VentanaAnalisis.this,
                            "No hay tickets disponibles para analizar",
                            "Sin Tickets", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // EXTRAER ID DEL TICKET DEL COMBOBOX
                String ticketId = ticketSeleccionado.split(" - ")[0].trim();

                try {
                    // REALIZAR ANÁLISIS REAL
                    realizarAnalisisReal(ticketId);

                    // MOSTRAR RESULTADOS
                    panelResultados.setVisible(true);

                    JOptionPane.showMessageDialog(VentanaAnalisis.this,
                            "Análisis completado para el ticket: " + ticketId,
                            "Análisis Exitoso", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaAnalisis.this,
                            "Error al analizar ticket: " + ex.getMessage(),
                            "Error de Análisis", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // CARGAR TICKETS REALES DE LA BASE DE DATOS
    private void cargarTicketsReales() {
        modeloCombo.removeAllElements();
        try {
            ControladorTicket controlador = new ControladorTicket();
            Collection<Ticket> tickets = controlador.obtenerTodos();

            if (tickets != null && !tickets.isEmpty()) {
                for (Ticket ticket : tickets) {
                    String item = ticket.getId() + " - " +
                            ticket.getAsunto() + " (" + ticket.getEstado() + ")";
                    modeloCombo.addElement(item);
                }

                if (tickets.size() == 1) {
                    cmbTickets.setSelectedIndex(0);
                }
            } else {
                modeloCombo.addElement("NO HAY TICKETS REGISTRADOS");
            }
        } catch (Exception e) {
            System.out.println("Error cargando tickets: " + e.getMessage());
            modeloCombo.addElement("ERROR AL CARGAR TICKETS");
        }
    }

    // REALIZAR ANÁLISIS REAL CON BAG-OF-WORDS
    private void realizarAnalisisReal(String ticketId) {
        try {
            ControladorTicket controlador = new ControladorTicket();

            // 1. OBTENER TICKET COMPLETO
            Ticket ticket = controlador.buscarPorId(ticketId);

            if (ticket == null) {
                throw new Exception("Ticket no encontrado: " + ticketId);
            }

            // 2. ACTUALIZAR PANEL DE TICKET ORIGINAL
            actualizarPanelTicketOriginal(ticket);

            // 3. REALIZAR ANÁLISIS UNIFICADO BAG OF WORDS
            ResultadoAnalisis resultado = controlador.analizarTicket(ticketId);

            // 4. ACTUALIZAR PANEL DE ANÁLISIS
            actualizarPanelAnalisisBoW(resultado);
        } catch (Exception e) {
            throw new RuntimeException("Error en análisis: " + e.getMessage(), e);
        }
    }

    // ACTUALIZAR PANEL DE TICKET ORIGINAL
    private void actualizarPanelTicketOriginal(Ticket ticket) {
        StringBuilder sb = new StringBuilder();
        sb.append("TICKET: ").append(ticket.getId()).append("\n\n");
        sb.append("USUARIO: ").append(ticket.getUsuario().getNombre())
                .append(" (").append(ticket.getUsuario().getEmail()).append(")\n");
        sb.append("DEPARTAMENTO: ").append(ticket.getDepartamento().getNombre()).append("\n");
        sb.append("ESTADO: ").append(ticket.getEstado()).append("\n\n");
        sb.append("ASUNTO: ").append(ticket.getAsunto()).append("\n\n");
        sb.append("DESCRIPCIÓN COMPLETA:\n").append(ticket.getDescripcion()).append("\n\n");

        // Información básica del BagOfWords
        BagOfWords bag = ticket.getBag();
        if (bag != null) {
            sb.append("INFORMACIÓN BAG-OF-WORDS:\n");
            sb.append("• Texto normalizado: ").append(bag.getTextoNormalizado()).append("\n");
            sb.append("• Cantidad de palabras clave: ").append(bag.getPalabrasClave().size()).append("\n");
        } else {
            sb.append("INFORMACIÓN BAG-OF-WORDS:\n");
            sb.append("• No se ha generado el Bag of Words para este ticket.\n");
        }

        areaTicketOriginal.setText(sb.toString());
    }

    // ACTUALIZAR PANEL DE ANÁLISIS UNIFICADO (BoW + ResultadoAnalisis)
    private void actualizarPanelAnalisisBoW(ResultadoAnalisis resultado) {

        StringBuilder sb = new StringBuilder();
        sb.append("ANÁLISIS BAG OF WORDS - RESULTADOS\n");
        sb.append("=================================\n\n");

        if (resultado == null) {
            sb.append("No se pudo realizar el análisis del ticket.\n");
            areaAnalisisBoW.setText(sb.toString());
            return;
        }

        // 1. EMOCIÓN PREDOMINANTE
        sb.append("1) Emoción predominante\n");
        sb.append("-----------------------\n");

        if (resultado.getEmocionPredominante() != null) {
            sb.append("Emoción detectada: ")
                    .append(resultado.getEmocionPredominante().name())
                    .append("\n");

            if (!resultado.getPalabrasEncontradasEmocionales().isEmpty()) {
                sb.append("Palabras emocionales encontradas:\n");
                for (String p : resultado.getPalabrasEncontradasEmocionales()) {
                    sb.append("   - ").append(p).append("\n");
                }
            } else {
                sb.append("No se encontraron palabras emocionales.\n");
            }

        } else {
            sb.append("No se detectó ninguna emoción.\n");
        }

        sb.append("\n");

        // 2. CATEGORÍA TÉCNICA DETECTADA
        sb.append("2) Categoría técnica predominante\n");
        sb.append("---------------------------------\n");

        if (resultado.getCategoriaPredominante() != null) {
            sb.append("Categoría: ")
                    .append(resultado.getCategoriaPredominante().name())
                    .append("\n");

            if (!resultado.getPalabrasEncontradasTecnicas().isEmpty()) {
                sb.append("Palabras técnicas encontradas:\n");
                for (String p : resultado.getPalabrasEncontradasTecnicas()) {
                    sb.append("   - ").append(p).append("\n");
                }
            } else {
                sb.append("No se encontraron palabras técnicas.\n");
            }

        } else {
            sb.append("No se detectó ninguna categoría técnica.\n");
        }

        sb.append("\n");

        // 3. VECTOR DE FRECUENCIAS COMPLETO
        sb.append("3) Vector de frecuencias (todas las palabras relevantes)\n");
        sb.append("--------------------------------------------------------\n");

        if (resultado.getVectorFrecuencias() != null && !resultado.getVectorFrecuencias().isEmpty()) {
            for (Map.Entry<String, Integer> entry : resultado.getVectorFrecuencias().entrySet()) {
                sb.append("• ").append(entry.getKey())
                        .append(" → ").append(entry.getValue())
                        .append(" veces\n");
            }
        } else {
            sb.append("No hay palabras relevantes.\n");
        }

        sb.append("\n");

        // 4. TEXTO NORMALIZADO
        sb.append("4) Texto normalizado\n");
        sb.append("--------------------\n");
        sb.append(resultado.getTextoNormalizado()).append("\n");

        // PASAR TEXTO A LA UI
        areaAnalisisBoW.setText(sb.toString());
    }

    // PANEL PARA TICKET ORIGINAL
    private JPanel crearPanelTicketOriginal() {
        JPanel panelTicket = new JPanel(new BorderLayout());
        panelTicket.setBorder(BorderFactory.createTitledBorder("TICKET ORIGINAL"));

        areaTicketOriginal = new JTextArea(10, 60);
        areaTicketOriginal.setEditable(false);
        areaTicketOriginal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaTicketOriginal.setText("Seleccione un ticket y haga clic en ANALIZAR para ver la información...");

        JScrollPane scrollTicket = new JScrollPane(areaTicketOriginal);
        panelTicket.add(scrollTicket, BorderLayout.CENTER);

        return panelTicket;
    }

    // PANEL PARA ANÁLISIS UNIFICADO BAG OF WORDS
    private JPanel crearPanelAnalisisBoW() {
        JPanel panelAnalisis = new JPanel(new BorderLayout());
        panelAnalisis.setBorder(BorderFactory.createTitledBorder("ANÁLISIS BAG OF WORDS"));

        areaAnalisisBoW = new JTextArea(14, 60);
        areaAnalisisBoW.setEditable(false);
        areaAnalisisBoW.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaAnalisisBoW.setText("Los resultados del análisis Bag of Words aparecerán aquí...");

        JScrollPane scrollAnalisis = new JScrollPane(areaAnalisisBoW);
        panelAnalisis.add(scrollAnalisis, BorderLayout.CENTER);

        return panelAnalisis;
    }

    //ABRIR LA VENTANA
    public static void abrirVentana() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaAnalisis().setVisible(true);
            }
        });
    }
}
