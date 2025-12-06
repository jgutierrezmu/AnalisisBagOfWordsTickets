package Presentacion;

import Control.ControladorUsuario;
import LogicaDeNegocio.entidades.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class VentanaUsuarios extends JFrame {

    //PANELES PARA CADA FUNCIONALIDAD
    private JPanel panelContenido;
    private CardLayout cardLayout;

    //CONSTRUCTOR DE CLASE (DEFINE COMPONENTES Y LOS COLOCA)
    public VentanaUsuarios() {
        //CONFIGURAR LA VENTANA DE USUARIOS
        setTitle("Gestión de Usuarios - Sistema Bag of Words");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        //PANEL PRINCIPAL CON BORDERLAYOUT
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //PANEL SUPERIOR CON BOTONES DE ACCIÓN
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));

        //DEFINICIÓN DE BOTONES CRUD
        JButton btnAgregar = new JButton("AGREGAR");
        JButton btnModificar = new JButton("MODIFICAR");
        JButton btnEliminar = new JButton("ELIMINAR");
        JButton btnMostrar = new JButton("MOSTRAR TODOS");

        //ESTILO DE LOS BOTONES
        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 12);
        btnAgregar.setFont(fuenteBotones);
        btnModificar.setFont(fuenteBotones);
        btnEliminar.setFont(fuenteBotones);
        btnMostrar.setFont(fuenteBotones);

        //AÑADIR BOTONES AL PANEL DE BOTONES
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnMostrar);

        //PANEL DE CONTENIDO CON CARDLAYOUT (PARA CAMBIAR ENTRE FORMULARIOS)
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        //CREAR LOS DIFERENTES PANELES DE CONTENIDO
        crearPanelInicial();
        crearPanelAgregar();
        crearPanelModificar();
        crearPanelEliminar();
        crearPanelMostrar();

        //BOTÓN VOLVER AL MENÚ
        JButton btnVolver = new JButton("VOLVER AL MENÚ PRINCIPAL");
        btnVolver.setFont(fuenteBotones);

        //AGREGAR COMPONENTES AL PANEL PRINCIPAL
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(btnVolver, BorderLayout.SOUTH);

        //COLOCAR EL PANEL AL JFRAME
        add(panelPrincipal);

        //FUNCIONES DE CADA BOTÓN (SOLO CAMBIAN LA VISTA)
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

    //PANEL INICIAL (CUANDO SE ABRE LA VENTANA)
    private void crearPanelInicial() {
        JPanel panelInicial = new JPanel(new BorderLayout());
        JLabel lblInicial = new JLabel("Seleccione una opción para gestionar los usuarios", JLabel.CENTER);
        lblInicial.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panelInicial.add(lblInicial, BorderLayout.CENTER);
        panelContenido.add(panelInicial, "inicial");
    }

    //PANEL PARA AGREGAR USUARIO
    private void crearPanelAgregar() {
        JPanel panelAgregar = new JPanel(new GridLayout(7, 2, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("AGREGAR NUEVO USUARIO"));

        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField();

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();

        JLabel lblRol = new JLabel("Rol:");
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{
                "Estudiante",
                "Funcionario",
                "Administrador"
        });

        JButton btnGuardar = new JButton("GUARDAR USUARIO");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String id = txtId.getText().trim();
                String nombre = txtNombre.getText().trim();
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword());
                String telefono = txtTelefono.getText().trim();
                String rol = (String) cmbRol.getSelectedItem();

                if (id.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaUsuarios.this,
                            "Por favor complete todos los campos obligatorios: ID, Nombre, Email y Contraseña",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {

                    Usuario nuevoUsuario = new Usuario(id, nombre, email, password, telefono);
                    nuevoUsuario.setRol(rol);

                    ControladorUsuario controlador = new ControladorUsuario();
                    boolean exito = controlador.registrar(nuevoUsuario);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                "Usuario registrado exitosamente",
                                "Registro Exitoso",
                                JOptionPane.INFORMATION_MESSAGE);

                        txtId.setText("");
                        txtNombre.setText("");
                        txtEmail.setText("");
                        txtPassword.setText("");
                        txtTelefono.setText("");
                        cmbRol.setSelectedIndex(0);

                    } else {
                        JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                "Error al registrar el usuario.\n" +
                                        "El ID o el Email ya existen en el sistema.",
                                "Error de Registro",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaUsuarios.this,
                            "Error inesperado al registrar usuario:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelAgregar.add(lblId);
        panelAgregar.add(txtId);
        panelAgregar.add(lblNombre);
        panelAgregar.add(txtNombre);
        panelAgregar.add(lblEmail);
        panelAgregar.add(txtEmail);
        panelAgregar.add(lblPassword);
        panelAgregar.add(txtPassword);
        panelAgregar.add(lblTelefono);
        panelAgregar.add(txtTelefono);
        panelAgregar.add(lblRol);
        panelAgregar.add(cmbRol);
        panelAgregar.add(new JLabel());
        panelAgregar.add(btnGuardar);

        panelContenido.add(panelAgregar, "agregar");
    }

    //PANEL PARA MODIFICAR USUARIO
    private void crearPanelModificar() {
        JPanel panelModificar = new JPanel(new BorderLayout(10, 10));
        panelModificar.setBorder(BorderFactory.createTitledBorder("MODIFICAR USUARIO EXISTENTE"));

        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("Buscar por ID:");
        JTextField txtIdBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtIdBuscar);
        panelBuscar.add(btnBuscar);

        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));

        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();
        txtEmail.setEditable(false);
        txtEmail.setBackground(Color.LIGHT_GRAY);

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        JTextField txtPassword = new JTextField();

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();

        JLabel lblRol = new JLabel("Rol:");
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Estudiante", "Funcionario", "Administrador"});

        JButton btnActualizar = new JButton("ACTUALIZAR USUARIO");

        panelFormulario.add(lblId);
        panelFormulario.add(txtId);
        panelFormulario.add(lblEmail);
        panelFormulario.add(txtEmail);
        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblPassword);
        panelFormulario.add(txtPassword);
        panelFormulario.add(lblTelefono);
        panelFormulario.add(txtTelefono);
        panelFormulario.add(lblRol);
        panelFormulario.add(cmbRol);
        panelFormulario.add(new JLabel());
        panelFormulario.add(btnActualizar);

        panelFormulario.setVisible(false);

        panelModificar.add(panelBuscar, BorderLayout.NORTH);
        panelModificar.add(panelFormulario, BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String idBuscar = txtIdBuscar.getText().trim();

                if (idBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaUsuarios.this,
                            "Por favor ingrese un ID para buscar",
                            "Campo Vacío",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {

                    ControladorUsuario controlador = new ControladorUsuario();
                    Usuario usuario = controlador.buscarPorId(idBuscar);

                    if (usuario != null) {

                        panelFormulario.setVisible(true);
                        txtId.setText(usuario.getId());
                        txtEmail.setText(usuario.getEmail());
                        txtNombre.setText(usuario.getNombre());
                        txtPassword.setText(usuario.getPassword());
                        txtTelefono.setText(usuario.getTelefono() != null ? usuario.getTelefono() : "");
                        cmbRol.setSelectedItem(usuario.getRol());

                        JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                "Usuario encontrado",
                                "Búsqueda Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        panelFormulario.setVisible(false);
                        JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                "No se encontró ningún usuario con el ID: " + idBuscar + "\nTIENE QUE RESPETAR MAYUSCULAS Y MINUSCULAS",
                                "Usuario No Encontrado",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaUsuarios.this,
                            "Error al buscar usuario:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (txtNombre.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaUsuarios.this,
                            "Los campos Nombre y Contraseña son obligatorios",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {

                    Usuario usuarioActualizado = new Usuario(
                            txtId.getText(),
                            txtNombre.getText().trim(),
                            txtEmail.getText(),
                            txtPassword.getText().trim(),
                            txtTelefono.getText().trim()
                    );
                    usuarioActualizado.setRol((String) cmbRol.getSelectedItem());

                    ControladorUsuario controlador = new ControladorUsuario();
                    boolean exito = controlador.actualizar(usuarioActualizado);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                "Usuario actualizado exitosamente",
                                "Actualización Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                        txtIdBuscar.setText("");
                        txtPassword.setText("");
                        panelFormulario.setVisible(false);

                    } else {
                        JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                "Error al actualizar el usuario",
                                "Error de Actualización",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaUsuarios.this,
                            "Error inesperado al actualizar usuario:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelContenido.add(panelModificar, "modificar");
    }

    //PANEL PARA ELIMINAR USUARIO
    private void crearPanelEliminar() {
        JPanel panelEliminar = new JPanel(new BorderLayout(10, 10));
        panelEliminar.setBorder(BorderFactory.createTitledBorder("ELIMINAR USUARIO"));

        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("ID del usuario a eliminar:");
        JTextField txtId = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR USUARIO");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtId);
        panelBuscar.add(btnBuscar);

        JTextArea areaInfo = new JTextArea(8, 50);
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaInfo.setText("Ingrese el ID del usuario y haga clic en BUSCAR para ver la información...");
        JScrollPane scrollInfo = new JScrollPane(areaInfo);

        panelEliminar.add(panelBuscar, BorderLayout.NORTH);
        panelEliminar.add(scrollInfo, BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String idBuscar = txtId.getText().trim();

                if (idBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaUsuarios.this,
                            "Por favor ingrese un ID para buscar",
                            "Campo Vacío",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {

                    ControladorUsuario controlador = new ControladorUsuario();
                    Usuario usuario = controlador.buscarPorId(idBuscar);

                    if (usuario != null) {

                        areaInfo.setText("USUARIO ENCONTRADO:\n\n" +
                                "• ID: " + usuario.getId() + "\n" +
                                "• Nombre: " + usuario.getNombre() + "\n" +
                                "• Email: " + usuario.getEmail() + "\n" +
                                "• Teléfono: " + (usuario.getTelefono() != null ? usuario.getTelefono() : "No registrado") + "\n" +
                                "• Rol: " + usuario.getRol() + "\n\n" +
                                "¿Desea eliminar este usuario permanentemente?\n\n" +
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
                                        VentanaUsuarios.this,
                                        "¿Está seguro de eliminar este usuario?\n\n" +
                                                "ID: " + usuario.getId() + "\n" +
                                                "Nombre: " + usuario.getNombre() + "\n" +
                                                "Email: " + usuario.getEmail() + "\n",
                                        "CONFIRMAR ELIMINACIÓN",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE);

                                if (confirmacion == JOptionPane.YES_OPTION) {

                                    try {
                                        boolean exito = controlador.eliminar(usuario.getId());
                                        if (exito) {
                                            JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                                    "Usuario eliminado correctamente",
                                                    "Eliminación Exitosa",
                                                    JOptionPane.INFORMATION_MESSAGE);

                                            areaInfo.setText("Usuario eliminado.\nPuede buscar otro.");
                                            txtId.setText("");
                                            panelEliminar.remove(2);
                                            panelEliminar.revalidate();
                                            panelEliminar.repaint();

                                        } else {
                                            JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                                    "Error al eliminar el usuario." + "\nNO SE PUEDEN ELIMINAR USUARIOS ASOCIADOS A TICKETS" ,
                                                    "Error de Eliminación",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }

                                    } catch (Exception ex) {
                                        JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                                "Error inesperado:\n" + ex.getMessage(),
                                                "Error del Sistema",
                                                JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        });

                    } else {

                        areaInfo.setText("No se encontró ningún usuario con el ID: " + idBuscar +
                                "\nVerifique que el ID sea correcto e intente nuevamente." + "\nTIENE QUE RESPETAR MAYUSCULAS Y MINUSCULAS");

                        if (panelEliminar.getComponentCount() > 2) {
                            panelEliminar.remove(2);
                            panelEliminar.revalidate();
                            panelEliminar.repaint();
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaUsuarios.this,
                            "Error al buscar usuario:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelContenido.add(panelEliminar, "eliminar");
    }

    //PANEL PARA MOSTRAR TODOS LOS USUARIOS
    private void crearPanelMostrar() {
        JPanel panelMostrar = new JPanel(new BorderLayout());
        panelMostrar.setBorder(BorderFactory.createTitledBorder("LISTA DE TODOS LOS USUARIOS"));

        String[] columnas = {"ID", "Nombre", "Email", "Teléfono", "Rol"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFillsViewportHeight(true);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);

        JButton btnActualizar = new JButton("ACTUALIZAR LISTA");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    ControladorUsuario controlador = new ControladorUsuario();
                    Collection<Usuario> usuarios = controlador.obtenerTodos();

                    if (usuarios != null && !usuarios.isEmpty()) {

                        modeloTabla.setRowCount(0);

                        for (Usuario usuario : usuarios) {
                            Object[] fila = {
                                    usuario.getId(),
                                    usuario.getNombre(),
                                    usuario.getEmail(),
                                    usuario.getTelefono(),
                                    usuario.getRol()
                            };
                            modeloTabla.addRow(fila);
                        }

                        JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                "Lista actualizada correctamente.\n" +
                                        "Usuarios encontrados: " + usuarios.size(),
                                "Lista Actualizada",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(VentanaUsuarios.this,
                                "No hay usuarios registrados en el sistema.",
                                "Lista Vacía",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaUsuarios.this,
                            "Error al cargar la lista de usuarios:\n" + ex.getMessage(),
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

    //ABRIR VENTANA
    public static void abrirVentana() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaUsuarios().setVisible(true);
            }
        });
    }
}
