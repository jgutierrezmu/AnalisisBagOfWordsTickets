package Presentacion;

import Control.ControladorDiccionarioEmocional;
import LogicaDeNegocio.enums.Emocion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class VentanaDiccionarioEmocional extends JFrame {

    // PANELES PARA CADA FUNCIONALIDAD
    private JPanel panelContenido;
    private CardLayout cardLayout;

    // CONSTRUCTOR DE CLASE (DEFINE COMPONENTES Y LOS COLOCA)
    public VentanaDiccionarioEmocional() {
        // CONFIGURAR LA VENTANA DEL DICCIONARIO EMOCIONAL
        setTitle("Gestión del Diccionario Emocional - Sistema Bag of Words");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // PANEL PRINCIPAL CON BORDERLAYOUT
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // PANEL SUPERIOR CON BOTONES DE ACCIÓN
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));

        // DEFINICIÓN DE BOTONES CRUD
        JButton btnAgregar = new JButton("AGREGAR");
        JButton btnModificar = new JButton("MODIFICAR");
        JButton btnEliminar = new JButton("ELIMINAR");
        JButton btnMostrar = new JButton("MOSTRAR TODO");

        // ESTILO DE LOS BOTONES
        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 12);
        btnAgregar.setFont(fuenteBotones);
        btnModificar.setFont(fuenteBotones);
        btnEliminar.setFont(fuenteBotones);
        btnMostrar.setFont(fuenteBotones);

        // AÑADIR BOTONES AL PANEL DE BOTONES
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnMostrar);

        // PANEL DE CONTENIDO CON CARDLAYOUT (PARA CAMBIAR ENTRE FORMULARIOS)
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        // CREAR LOS DIFERENTES PANELES DE CONTENIDO
        crearPanelInicial();
        crearPanelAgregar();
        crearPanelModificar();
        crearPanelEliminar();
        crearPanelMostrar();

        // BOTÓN VOLVER
        JButton btnVolver = new JButton("VOLVER AL MENÚ PRINCIPAL");
        btnVolver.setFont(fuenteBotones);

        // AGREGAR COMPONENTES AL PANEL PRINCIPAL
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(btnVolver, BorderLayout.SOUTH);

        // COLOCAR EL PANEL AL JFRAME
        add(panelPrincipal);

        // FUNCIONES DE CADA BOTÓN (SOLO CAMBIAN LA VISTA)
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "agregar");
            }
        });
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "modificar");
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "eliminar");
            }
        });
        btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "mostrar");
            }
        });
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // PANEL INICIAL (CUANDO SE ABRE LA VENTANA)
    private void crearPanelInicial() {
        JPanel panelInicial = new JPanel(new BorderLayout());
        JLabel lblInicial = new JLabel("Seleccione una opción para gestionar el diccionario emocional", JLabel.CENTER);
        lblInicial.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panelInicial.add(lblInicial, BorderLayout.CENTER);
        panelContenido.add(panelInicial, "inicial");
    }

    // PANEL PARA AGREGAR PALABRA
    private void crearPanelAgregar() {
        JPanel panelAgregar = new JPanel(new GridLayout(4, 2, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("AGREGAR NUEVA PALABRA AL DICCIONARIO"));

        // CAMPOS DEL FORMULARIO
        JLabel lblPalabra = new JLabel("Palabra:");
        JTextField txtPalabra = new JTextField();

        JLabel lblEmocion = new JLabel("Emoción asociada:");
        JComboBox<Emocion> cmbEmocion = new JComboBox<>(Emocion.values());

        // BOTÓN GUARDAR
        JButton btnGuardar = new JButton("GUARDAR PALABRA");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // OBTENER DATOS DEL FORMULARIO
                String palabra = txtPalabra.getText().trim();
                Emocion emocion = (Emocion) cmbEmocion.getSelectedItem();

                // VALIDAR CAMPOS OBLIGATORIOS
                if (palabra.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                            "Por favor ingrese una palabra",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // CREAR CONTROLADOR Y LLAMAR PARA REGISTRAR
                    ControladorDiccionarioEmocional controlador = new ControladorDiccionarioEmocional();
                    boolean exito = controlador.registrar(palabra, emocion);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                "Palabra registrada exitosamente!\n\n" +
                                        "Palabra: " + palabra + "\n" +
                                        "Emoción: " + emocion.toString(),
                                "Registro Exitoso",
                                JOptionPane.INFORMATION_MESSAGE);

                        // LIMPIAR FORMULARIO
                        txtPalabra.setText("");
                        cmbEmocion.setSelectedIndex(0);

                    } else {
                        JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                "Error al registrar la palabra.\n" +
                                        "La palabra ya existe en el diccionario.",
                                "Error de Registro",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                            "Error inesperado al registrar palabra:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // AGREGAR COMPONENTES AL PANEL
        panelAgregar.add(lblPalabra);
        panelAgregar.add(txtPalabra);
        panelAgregar.add(lblEmocion);
        panelAgregar.add(cmbEmocion);
        panelAgregar.add(new JLabel());
        panelAgregar.add(btnGuardar);

        panelContenido.add(panelAgregar, "agregar");
    }

    // PANEL PARA MODIFICAR PALABRA
    private void crearPanelModificar() {
        JPanel panelModificar = new JPanel(new BorderLayout(10, 10));
        panelModificar.setBorder(BorderFactory.createTitledBorder("MODIFICAR PALABRA EXISTENTE"));

        // PANEL SUPERIOR PARA BUSCAR
        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("Buscar palabra:");
        JTextField txtPalabraBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtPalabraBuscar);
        panelBuscar.add(btnBuscar);

        // PANEL CENTRAL CON FORMULARIO
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la Palabra"));

        // CAMPO NO EDITABLE: PALABRA
        JLabel lblPalabra = new JLabel("Palabra:");
        JTextField txtPalabra = new JTextField();
        txtPalabra.setEditable(false);
        txtPalabra.setBackground(Color.LIGHT_GRAY);

        // CAMPO EDITABLE: EMOCIÓN
        JLabel lblEmocion = new JLabel("Emoción:");
        JComboBox<Emocion> cmbEmocion = new JComboBox<>(Emocion.values());

        JButton btnActualizar = new JButton("ACTUALIZAR PALABRA");

        // AGREGAR COMPONENTES AL FORMULARIO
        panelFormulario.add(lblPalabra);
        panelFormulario.add(txtPalabra);
        panelFormulario.add(lblEmocion);
        panelFormulario.add(cmbEmocion);
        panelFormulario.add(new JLabel());
        panelFormulario.add(btnActualizar);

        // INICIALMENTE OCULTAR EL FORMULARIO
        panelFormulario.setVisible(false);

        // AGREGAR COMPONENTES AL PANEL PRINCIPAL
        panelModificar.add(panelBuscar, BorderLayout.NORTH);
        panelModificar.add(panelFormulario, BorderLayout.CENTER);

        // ACCIONES DE BOTONES
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String palabraBuscar = txtPalabraBuscar.getText().trim();

                if (palabraBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                            "Por favor ingrese una palabra para buscar",
                            "Campo Vacío",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    ControladorDiccionarioEmocional controlador = new ControladorDiccionarioEmocional();
                    String[] palabraData = controlador.buscar(palabraBuscar);

                    if (palabraData != null) {
                        panelFormulario.setVisible(true);
                        txtPalabra.setText(palabraData[0]);

                        try {
                            Emocion emocion = Emocion.valueOf(palabraData[1]);
                            cmbEmocion.setSelectedItem(emocion);
                        } catch (IllegalArgumentException ex) {
                            cmbEmocion.setSelectedIndex(0);
                        }

                        JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                "Palabra encontrada!\n" +
                                        "Puede modificar la emoción asociada.",
                                "Búsqueda Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        panelFormulario.setVisible(false);
                        JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                "No se encontró la palabra: " + palabraBuscar,
                                "Palabra No Encontrada",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                            "Error al buscar palabra:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String palabra = txtPalabra.getText();
                    Emocion emocion = (Emocion) cmbEmocion.getSelectedItem();

                    ControladorDiccionarioEmocional controlador = new ControladorDiccionarioEmocional();
                    boolean exito = controlador.actualizar(palabra, emocion);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                "Palabra actualizada exitosamente!\n\n" +
                                        "Palabra: " + palabra + "\n" +
                                        "Nueva emoción: " + emocion.toString(),
                                "Actualización Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                        txtPalabraBuscar.setText("");
                        panelFormulario.setVisible(false);

                    } else {
                        JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                "Error al actualizar la palabra",
                                "Error de Actualización",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                            "Error inesperado al actualizar palabra:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        panelContenido.add(panelModificar, "modificar");
    }

    // PANEL PARA ELIMINAR PALABRA
    private void crearPanelEliminar() {
        JPanel panelEliminar = new JPanel(new BorderLayout(10, 10));
        panelEliminar.setBorder(BorderFactory.createTitledBorder("ELIMINAR PALABRA DEL DICCIONARIO"));

        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("Palabra a eliminar:");
        JTextField txtPalabra = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR PALABRA");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtPalabra);
        panelBuscar.add(btnBuscar);

        JTextArea areaInfo = new JTextArea(8, 50);
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaInfo.setText("Ingrese la palabra y haga clic en BUSCAR para ver la información...");
        JScrollPane scrollInfo = new JScrollPane(areaInfo);

        panelEliminar.add(panelBuscar, BorderLayout.NORTH);
        panelEliminar.add(scrollInfo, BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String palabraBuscar = txtPalabra.getText().trim();

                if (palabraBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                            "Por favor ingrese una palabra para buscar",
                            "Campo Vacío",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    ControladorDiccionarioEmocional controlador = new ControladorDiccionarioEmocional();
                    String[] palabraData = controlador.buscar(palabraBuscar);

                    if (palabraData != null) {
                        areaInfo.setText("PALABRA ENCONTRADA:\n\n" +
                                "• Palabra: " + palabraData[0] + "\n" +
                                "• Emoción: " + palabraData[1] + "\n\n" +
                                "¿Desea eliminar esta palabra permanentemente?\n\n" +
                                "ADVERTENCIA: Esta acción no se puede deshacer.");

                        JButton btnConfirmarEliminar = new JButton("CONFIRMAR ELIMINACIÓN");
                        btnConfirmarEliminar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        btnConfirmarEliminar.setBackground(Color.RED);
                        btnConfirmarEliminar.setForeground(Color.WHITE);

                        if (panelEliminar.getComponentCount() > 2) {
                            panelEliminar.remove(2);
                        }

                        JPanel panelConfirmacion = new JPanel(new FlowLayout());
                        panelConfirmacion.add(btnConfirmarEliminar);
                        panelEliminar.add(panelConfirmacion, BorderLayout.SOUTH);
                        panelEliminar.revalidate();
                        panelEliminar.repaint();

                        btnConfirmarEliminar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent ev) {
                                int confirmacion = JOptionPane.showConfirmDialog(
                                        VentanaDiccionarioEmocional.this,
                                        "¿ESTÁ ABSOLUTAMENTE SEGURO de eliminar esta palabra?\n\n" +
                                                "Palabra: " + palabraData[0] + "\n" +
                                                "Emoción: " + palabraData[1] + "\n\n" +
                                                "Esta acción NO se puede deshacer.",
                                        "CONFIRMAR ELIMINACIÓN DEFINITIVA",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE);

                                if (confirmacion == JOptionPane.YES_OPTION) {
                                    try {
                                        ControladorDiccionarioEmocional controladorEliminar = new ControladorDiccionarioEmocional();
                                        boolean exito = controladorEliminar.eliminar(palabraData[0]);

                                        if (exito) {
                                            JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                                    "Palabra eliminada exitosamente!\n\n" +
                                                            "Palabra: " + palabraData[0] + "\n" +
                                                            "La palabra ha sido removida permanentemente del diccionario.",
                                                    "Eliminación Exitosa",
                                                    JOptionPane.INFORMATION_MESSAGE);

                                            areaInfo.setText("Palabra eliminada correctamente.\n\n" +
                                                    "Puede buscar otra palabra para eliminar.");
                                            txtPalabra.setText("");
                                            panelEliminar.remove(2);
                                            panelEliminar.revalidate();
                                            panelEliminar.repaint();

                                        } else {
                                            JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                                    "Error al eliminar la palabra",
                                                    "Error de Eliminación",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }

                                    } catch (Exception ex) {
                                        JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                                "Error inesperado al eliminar palabra:\n" + ex.getMessage(),
                                                "Error del Sistema",
                                                JOptionPane.ERROR_MESSAGE);
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        });

                    } else {
                        areaInfo.setText("No se encontró la palabra: " + palabraBuscar + "\n\n" +
                                "Verifique que la palabra sea correcta y vuelva a intentar.");

                        if (panelEliminar.getComponentCount() > 2) {
                            panelEliminar.remove(2);
                            panelEliminar.revalidate();
                            panelEliminar.repaint();
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                            "Error al buscar palabra:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        panelContenido.add(panelEliminar, "eliminar");
    }

    // PANEL PARA MOSTRAR TODAS LAS PALABRAS
    private void crearPanelMostrar() {
        JPanel panelMostrar = new JPanel(new BorderLayout());
        panelMostrar.setBorder(BorderFactory.createTitledBorder("LISTA COMPLETA DEL DICCIONARIO EMOCIONAL"));

        // CREAR TABLA
        String[] columnas = {"Palabra", "Emoción"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla sea de solo lectura
            }
        };

        JTable tablaPalabras = new JTable(modeloTabla);
        tablaPalabras.setFillsViewportHeight(true);
        tablaPalabras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaPalabras.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaPalabras.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollTabla = new JScrollPane(tablaPalabras);

        // BOTÓN PARA ACTUALIZAR
        JButton btnActualizar = new JButton("ACTUALIZAR LISTA");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ControladorDiccionarioEmocional controlador = new ControladorDiccionarioEmocional();
                    Collection<String[]> palabras = controlador.obtenerTodos();

                    if (palabras != null && !palabras.isEmpty()) {
                        modeloTabla.setRowCount(0);

                        for (String[] palabraData : palabras) {
                            Object[] fila = {
                                    palabraData[0],
                                    palabraData[1]
                            };
                            modeloTabla.addRow(fila);
                        }

                        JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                "Lista actualizada correctamente.\n" +
                                        "Palabras en el diccionario: " + palabras.size(),
                                "Lista Actualizada",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                                "No hay palabras registradas en el diccionario.",
                                "Diccionario Vacío",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioEmocional.this,
                            "Error al cargar la lista de palabras:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(btnActualizar);

        panelMostrar.add(scrollTabla, BorderLayout.CENTER);
        panelMostrar.add(panelInferior, BorderLayout.SOUTH);

        panelContenido.add(panelMostrar, "mostrar");
    }

    // ABRIR VENTANA
    public static void abrirVentana() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaDiccionarioEmocional().setVisible(true);
            }
        });
    }
}
