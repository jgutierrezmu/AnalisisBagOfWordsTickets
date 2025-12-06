package Presentacion;

import Control.ControladorDiccionarioTecnico;
import LogicaDeNegocio.enums.CategoriaTec;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class VentanaDiccionarioTecnico extends JFrame {

    // PANELES PARA CADA FUNCIONALIDAD
    private JPanel panelContenido;
    private CardLayout cardLayout;

    // CONSTRUCTOR DE CLASE (DEFINE COMPONENTES Y LOS COLOCA)
    public VentanaDiccionarioTecnico() {
        setTitle("Gestión del Diccionario Técnico - Sistema Bag of Words");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));

        JButton btnAgregar = new JButton("AGREGAR");
        JButton btnModificar = new JButton("MODIFICAR");
        JButton btnEliminar = new JButton("ELIMINAR");
        JButton btnMostrar = new JButton("MOSTRAR TODO");

        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 12);
        btnAgregar.setFont(fuenteBotones);
        btnModificar.setFont(fuenteBotones);
        btnEliminar.setFont(fuenteBotones);
        btnMostrar.setFont(fuenteBotones);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnMostrar);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        crearPanelInicial();
        crearPanelAgregar();
        crearPanelModificar();
        crearPanelEliminar();
        crearPanelMostrar();

        JButton btnVolver = new JButton("VOLVER AL MENU PRINCIPAL");
        btnVolver.setFont(fuenteBotones);

        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(btnVolver, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnAgregar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "agregar");
            }
        });
        btnModificar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "modificar");
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "eliminar");
            }
        });
        btnMostrar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "mostrar");
            }
        });
        btnVolver.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    //PANEL INICIAL (CUANDO SE ABRE LA VENTANA)
    private void crearPanelInicial() {
        JPanel panelInicial = new JPanel(new BorderLayout());
        JLabel lblInicial = new JLabel("Seleccione una opcion para gestionar el diccionario tecnico", JLabel.CENTER);
        lblInicial.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panelInicial.add(lblInicial, BorderLayout.CENTER);
        panelContenido.add(panelInicial, "inicial");
    }

    //PANEL PARA AGREGAR PALABRA
    private void crearPanelAgregar() {
        JPanel panelAgregar = new JPanel(new GridLayout(4, 2, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("AGREGAR NUEVA PALABRA TECNICA"));

        JLabel lblPalabra = new JLabel("Palabra Tecnica:");
        JTextField txtPalabra = new JTextField();

        JLabel lblCategoria = new JLabel("Categoria Tecnica:");
        JComboBox<CategoriaTec> cmbCategoria = new JComboBox<>(CategoriaTec.values());

        JButton btnGuardar = new JButton("GUARDAR PALABRA");
        btnGuardar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String palabra = txtPalabra.getText().trim();
                CategoriaTec categoria = (CategoriaTec) cmbCategoria.getSelectedItem();

                if (palabra.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                            "Por favor ingrese una palabra tecnica",
                            "Error de Validacion",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    ControladorDiccionarioTecnico controlador = new ControladorDiccionarioTecnico();
                    boolean exito = controlador.registrar(palabra, categoria);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                "Palabra tecnica registrada exitosamente!\n\n" +
                                        "Palabra: " + palabra + "\n" +
                                        "Categoria: " + categoria.toString(),
                                "Registro Exitoso",
                                JOptionPane.INFORMATION_MESSAGE);

                        txtPalabra.setText("");
                        cmbCategoria.setSelectedIndex(0);

                    } else {
                        JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                "Error al registrar la palabra tecnica\n" +
                                        "La palabra ya existe en el diccionario.",
                                "Error de Registro",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                            "Error inesperado al registrar palabra tecnica:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelAgregar.add(lblPalabra);
        panelAgregar.add(txtPalabra);
        panelAgregar.add(lblCategoria);
        panelAgregar.add(cmbCategoria);
        panelAgregar.add(new JLabel());
        panelAgregar.add(btnGuardar);

        panelContenido.add(panelAgregar, "agregar");
    }

    //PANEL PARA MODIFICAR PALABRA
    private void crearPanelModificar() {
        JPanel panelModificar = new JPanel(new BorderLayout(10, 10));
        panelModificar.setBorder(BorderFactory.createTitledBorder("MODIFICAR PALABRA TECNICA"));

        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("Buscar palabra tecnica:");
        JTextField txtPalabraBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtPalabraBuscar);
        panelBuscar.add(btnBuscar);

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la Palabra Tecnica"));

        JLabel lblPalabra = new JLabel("Palabra:");
        JTextField txtPalabra = new JTextField();
        txtPalabra.setEditable(false);
        txtPalabra.setBackground(Color.LIGHT_GRAY);

        JLabel lblCategoria = new JLabel("Categoria:");
        JComboBox<CategoriaTec> cmbCategoria = new JComboBox<>(CategoriaTec.values());

        JButton btnActualizar = new JButton("ACTUALIZAR PALABRA");

        panelFormulario.add(lblPalabra);
        panelFormulario.add(txtPalabra);
        panelFormulario.add(lblCategoria);
        panelFormulario.add(cmbCategoria);
        panelFormulario.add(new JLabel());
        panelFormulario.add(btnActualizar);

        panelFormulario.setVisible(false);

        panelModificar.add(panelBuscar, BorderLayout.NORTH);
        panelModificar.add(panelFormulario, BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String palabraBuscar = txtPalabraBuscar.getText().trim();

                if (palabraBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                            "Por favor ingrese una palabra para buscar",
                            "Campo Vacio",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    ControladorDiccionarioTecnico controlador = new ControladorDiccionarioTecnico();
                    String[] palabraData = controlador.buscar(palabraBuscar);

                    if (palabraData != null) {
                        panelFormulario.setVisible(true);
                        txtPalabra.setText(palabraData[0]);

                        try {
                            CategoriaTec categoria = CategoriaTec.valueOf(palabraData[1]);
                            cmbCategoria.setSelectedItem(categoria);
                        } catch (IllegalArgumentException ex) {
                            cmbCategoria.setSelectedIndex(0);
                        }

                        JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                "Palabra tecnica encontrada\nPuede modificar la categoria.",
                                "Busqueda Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        panelFormulario.setVisible(false);
                        JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                "No se encontro la palabra tecnica: " + palabraBuscar,
                                "Palabra No Encontrada",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                            "Error al buscar palabra tecnica:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    String palabra = txtPalabra.getText();
                    CategoriaTec categoria = (CategoriaTec) cmbCategoria.getSelectedItem();

                    ControladorDiccionarioTecnico controlador = new ControladorDiccionarioTecnico();
                    boolean exito = controlador.actualizar(palabra, categoria);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                "Palabra tecnica actualizada exitosamente!\n\n" +
                                        "Palabra: " + palabra + "\n" +
                                        "Nueva categoria: " + categoria.toString(),
                                "Actualizacion Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                        txtPalabraBuscar.setText("");
                        panelFormulario.setVisible(false);

                    } else {
                        JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                "Error al actualizar la palabra tecnica",
                                "Error de Actualizacion",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                            "Error inesperado al actualizar palabra tecnica:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelContenido.add(panelModificar, "modificar");
    }

    //PANEL PARA ELIMINAR PALABRA
    private void crearPanelEliminar() {
        JPanel panelEliminar = new JPanel(new BorderLayout(10, 10));
        panelEliminar.setBorder(BorderFactory.createTitledBorder("ELIMINAR PALABRA TECNICA"));

        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("Palabra tecnica a eliminar:");
        JTextField txtPalabra = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR PALABRA");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtPalabra);
        panelBuscar.add(btnBuscar);

        JTextArea areaInfo = new JTextArea(8, 50);
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaInfo.setText("Ingrese la palabra tecnica y haga clic en BUSCAR para ver la informacion...");
        JScrollPane scrollInfo = new JScrollPane(areaInfo);

        panelEliminar.add(panelBuscar, BorderLayout.NORTH);
        panelEliminar.add(scrollInfo, BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String palabraBuscar = txtPalabra.getText().trim();

                if (palabraBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                            "Por favor ingrese una palabra tecnica para buscar",
                            "Campo Vacio",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    ControladorDiccionarioTecnico controlador = new ControladorDiccionarioTecnico();
                    String[] palabraData = controlador.buscar(palabraBuscar);

                    if (palabraData != null) {
                        areaInfo.setText("PALABRA TECNICA ENCONTRADA:\n\n" +
                                "Palabra: " + palabraData[0] + "\n" +
                                "Categoria: " + palabraData[1] + "\n\n" +
                                "Desea eliminar esta palabra permanentemente?\n\n" +
                                "ADVERTENCIA: Esta accion no se puede deshacer.");

                        JButton btnConfirmarEliminar = new JButton("CONFIRMAR ELIMINACION");
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
                            @Override public void actionPerformed(ActionEvent ev) {
                                int confirmacion = JOptionPane.showConfirmDialog(
                                        VentanaDiccionarioTecnico.this,
                                        "Esta seguro de eliminar esta palabra tecnica?\n\n" +
                                                "Palabra: " + palabraData[0] + "\n" +
                                                "Categoria: " + palabraData[1] + "\n\n" +
                                                "Esta accion no se puede deshacer.",
                                        "CONFIRMAR ELIMINACION",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE);

                                if (confirmacion == JOptionPane.YES_OPTION) {
                                    try {
                                        ControladorDiccionarioTecnico controladorEliminar = new ControladorDiccionarioTecnico();
                                        boolean exito = controladorEliminar.eliminar(palabraData[0]);

                                        if (exito) {
                                            JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                                    "Palabra tecnica eliminada exitosamente!\n\n" +
                                                            "Palabra: " + palabraData[0] + "\n",
                                                    "Eliminacion Exitosa",
                                                    JOptionPane.INFORMATION_MESSAGE);

                                            areaInfo.setText("Palabra tecnica eliminada correctamente.\n\n" +
                                                    "Puede buscar otra palabra para eliminar.");
                                            txtPalabra.setText("");
                                            panelEliminar.remove(2);
                                            panelEliminar.revalidate();
                                            panelEliminar.repaint();

                                        } else {
                                            JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                                    "Error al eliminar la palabra tecnica",
                                                    "Error de Eliminacion",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }

                                    } catch (Exception ex) {
                                        JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                                "Error inesperado al eliminar palabra tecnica:\n" + ex.getMessage(),
                                                "Error del Sistema",
                                                JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        });

                    } else {
                        areaInfo.setText("No se encontro la palabra tecnica: " + palabraBuscar + "\n\n" +
                                "Verifique que la palabra sea correcta e intente nuevamente.");

                        if (panelEliminar.getComponentCount() > 2) {
                            panelEliminar.remove(2);
                            panelEliminar.revalidate();
                            panelEliminar.repaint();
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                            "Error al buscar palabra tecnica:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelContenido.add(panelEliminar, "eliminar");
    }

    //PANEL PARA MOSTRAR PALABRA
    private void crearPanelMostrar() {
        JPanel panelMostrar = new JPanel(new BorderLayout());
        panelMostrar.setBorder(BorderFactory.createTitledBorder("LISTA COMPLETA DEL DICCIONARIO TECNICO"));

        String[] columnas = {"Palabra Tecnica", "Categoria"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaPalabras = new JTable(modeloTabla);
        tablaPalabras.setFillsViewportHeight(true);
        tablaPalabras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaPalabras.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaPalabras.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollTabla = new JScrollPane(tablaPalabras);

        JButton btnActualizar = new JButton("ACTUALIZAR LISTA");
        btnActualizar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    ControladorDiccionarioTecnico controlador = new ControladorDiccionarioTecnico();
                    Collection<String[]> palabras = controlador.obtenerTodos();

                    if (palabras != null && !palabras.isEmpty()) {
                        modeloTabla.setRowCount(0);

                        for (String[] palabraData : palabras) {
                            Object[] fila = {palabraData[0], palabraData[1]};
                            modeloTabla.addRow(fila);
                        }

                        JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                "Lista actualizada correctamente.\n" +
                                        "Palabras tecnicas registradas: " + palabras.size(),
                                "Lista Actualizada",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                                "No hay palabras tecnicas registradas en el diccionario.",
                                "Diccionario Vacio",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDiccionarioTecnico.this,
                            "Error al cargar la lista de palabras tecnicas:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
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
            @Override public void run() {
                new VentanaDiccionarioTecnico().setVisible(true);
            }
        });
    }
}
