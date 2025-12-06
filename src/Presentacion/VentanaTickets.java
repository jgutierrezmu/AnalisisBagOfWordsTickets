package Presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Control.ControladorTicket;
import Control.ControladorUsuario;
import Control.ControladorDepartamento;
import LogicaDeNegocio.entidades.Ticket;
import LogicaDeNegocio.entidades.Usuario;
import LogicaDeNegocio.entidades.Departamento;
import java.util.Collection;

public class VentanaTickets extends JFrame {

    // PANEL CONTENEDOR CONTROLADO CON CARDLAYOUT
    private JPanel panelContenido;
    private CardLayout cardLayout;

    // CONSTRUCTOR: CONFIGURA LA VISTA PRINCIPAL Y MENÚ CRUD
    public VentanaTickets() {

        // CONFIGURACIÓN DE VENTANA PRINCIPAL
        setTitle("Gestión de Tickets - Sistema Bag of Words");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // PANEL PRINCIPAL CON BORDERLAYOUT Y MÁRGENES
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // PANEL SUPERIOR CON BOTONES CRUD
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));

        // BOTONES DE ACCIÓN
        JButton btnAgregar = new JButton("AGREGAR");
        JButton btnModificar = new JButton("MODIFICAR");
        JButton btnEliminar = new JButton("ELIMINAR");
        JButton btnMostrar = new JButton("MOSTRAR TODOS");

        // ESTILIZACIÓN DE BOTONES
        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 12);
        btnAgregar.setFont(fuenteBotones);
        btnModificar.setFont(fuenteBotones);
        btnEliminar.setFont(fuenteBotones);
        btnMostrar.setFont(fuenteBotones);

        // AGREGAR BOTONES AL PANEL
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnMostrar);

        // INICIALIZACIÓN CARDLAYOUT
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        // CREACIÓN DE PANELES CRUD
        crearPanelInicial();
        crearPanelAgregar();
        crearPanelModificar();
        crearPanelEliminar();
        crearPanelMostrar();

        // BOTÓN INFERIOR PARA VOLVER AL MENÚ PRINCIPAL
        JButton btnVolver = new JButton("VOLVER AL MENU PRINCIPAL");
        btnVolver.setFont(fuenteBotones);

        // ENSAMBLAR PANEL PRINCIPAL
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(btnVolver, BorderLayout.SOUTH);

        // AGREGAR PANEL PRINCIPAL A FRAME
        add(panelPrincipal);

        // MANEJO DE EVENTOS DE BOTONES CRUD
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

    // PANEL INICIAL
    private void crearPanelInicial() {
        JPanel panelInicial = new JPanel(new BorderLayout());
        JLabel lblInicial = new JLabel("Seleccione una opcion para gestionar los tickets", JLabel.CENTER);
        lblInicial.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panelInicial.add(lblInicial, BorderLayout.CENTER);
        panelContenido.add(panelInicial, "inicial");
    }

    // PANEL PARA REGISTRAR NUEVO TICKET
    private void crearPanelAgregar() {

        JPanel panelAgregar = new JPanel(new GridLayout(8, 2, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("AGREGAR NUEVO TICKET"));

        JLabel lblId = new JLabel("ID del Ticket:");
        JTextField txtId = new JTextField();

        JLabel lblUsuario = new JLabel("Usuario:");
        JComboBox<String> cmbUsuario = new JComboBox<>();
        cargarUsuariosEnComboBox(cmbUsuario);

        JLabel lblDepartamento = new JLabel("Departamento:");
        JComboBox<String> cmbDepartamento = new JComboBox<>();
        cargarDepartamentosEnComboBox(cmbDepartamento);

        JLabel lblAsunto = new JLabel("Asunto:");
        JTextField txtAsunto = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripcion:");
        JTextField txtDescripcion = new JTextField();

        JLabel lblEstado = new JLabel("Estado:");
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{
                "NUEVO", "EN_PROCESO", "RESUELTO"
        });

        JButton btnGuardar = new JButton("CREAR TICKET");
        btnGuardar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {

                String id = txtId.getText().trim();
                String usuarioSeleccionado = (String) cmbUsuario.getSelectedItem();
                String departamentoSeleccionado = (String) cmbDepartamento.getSelectedItem();
                String asunto = txtAsunto.getText().trim();
                String descripcion = txtDescripcion.getText().trim();
                String estado = (String) cmbEstado.getSelectedItem();

                if (id.isEmpty() || asunto.isEmpty() || descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Por favor complete los campos obligatorios: ID, Asunto y Descripcion",
                            "Error de Validacion",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (cmbUsuario.getItemCount() == 0 || usuarioSeleccionado.startsWith("NO HAY") || usuarioSeleccionado.startsWith("ERROR")) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Debe registrar primero usuarios.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (cmbDepartamento.getItemCount() == 0 || departamentoSeleccionado.startsWith("NO HAY") || departamentoSeleccionado.startsWith("ERROR")) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Debe registrar primero departamentos.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String usuarioId = usuarioSeleccionado.split(" - ")[0];
                    String departamentoId = departamentoSeleccionado.split(" - ")[0];

                    ControladorUsuario ctrlUsuario = new ControladorUsuario();
                    ControladorDepartamento ctrlDepto = new ControladorDepartamento();
                    Usuario usuario = ctrlUsuario.buscarPorId(usuarioId);
                    Departamento departamento = ctrlDepto.buscarPorId(departamentoId);

                    if (usuario == null || departamento == null) {
                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                "Error: Usuario o Departamento no encontrado",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Ticket nuevoTicket = new Ticket(id, usuario, departamento, asunto, descripcion, estado);

                    ControladorTicket controlador = new ControladorTicket();
                    boolean exito = controlador.crearTicket(nuevoTicket);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                "Ticket creado exitosamente!",
                                "Registro Exitoso",
                                JOptionPane.INFORMATION_MESSAGE);

                        txtId.setText("");
                        txtAsunto.setText("");
                        txtDescripcion.setText("");
                        cmbEstado.setSelectedIndex(0);

                    } else {
                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                "Error: El ID ya existe",
                                "Error de Registro",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Error inesperado:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelAgregar.add(lblId);
        panelAgregar.add(txtId);
        panelAgregar.add(lblUsuario);
        panelAgregar.add(cmbUsuario);
        panelAgregar.add(lblDepartamento);
        panelAgregar.add(cmbDepartamento);
        panelAgregar.add(lblAsunto);
        panelAgregar.add(txtAsunto);
        panelAgregar.add(lblDescripcion);
        panelAgregar.add(txtDescripcion);
        panelAgregar.add(lblEstado);
        panelAgregar.add(cmbEstado);
        panelAgregar.add(new JLabel());
        panelAgregar.add(btnGuardar);

        panelContenido.add(panelAgregar, "agregar");
    }

    // PANEL PARA MODIFICAR TICKET EXISTENTE
    private void crearPanelModificar() {

        JPanel panelModificar = new JPanel(new BorderLayout(10, 10));
        panelModificar.setBorder(BorderFactory.createTitledBorder("MODIFICAR TICKET"));

        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("Buscar por ID:");
        JTextField txtIdBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtIdBuscar);
        panelBuscar.add(btnBuscar);

        JPanel panelFormulario = new JPanel(new GridLayout(8, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Ticket"));

        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);

        JLabel lblUsuario = new JLabel("Usuario:");
        JComboBox<String> cmbUsuario = new JComboBox<>();

        JLabel lblDepartamento = new JLabel("Departamento:");
        JComboBox<String> cmbDepartamento = new JComboBox<>();

        JLabel lblAsunto = new JLabel("Asunto:");
        JTextField txtAsunto = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripcion:");
        JTextField txtDescripcion = new JTextField();

        JLabel lblEstado = new JLabel("Estado:");
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{
                "NUEVO", "EN_PROCESO", "RESUELTO"
        });

        JButton btnActualizar = new JButton("ACTUALIZAR TICKET");

        panelFormulario.add(lblId);
        panelFormulario.add(txtId);
        panelFormulario.add(lblUsuario);
        panelFormulario.add(cmbUsuario);
        panelFormulario.add(lblDepartamento);
        panelFormulario.add(cmbDepartamento);
        panelFormulario.add(lblAsunto);
        panelFormulario.add(txtAsunto);
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(lblEstado);
        panelFormulario.add(cmbEstado);
        panelFormulario.add(new JLabel());
        panelFormulario.add(btnActualizar);

        panelFormulario.setVisible(false);

        panelModificar.add(panelBuscar, BorderLayout.NORTH);
        panelModificar.add(panelFormulario, BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {

                String idBuscar = txtIdBuscar.getText().trim();

                if (idBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Ingrese un ID",
                            "Campo Vacio",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    ControladorTicket controlador = new ControladorTicket();
                    Ticket ticket = controlador.buscarPorId(idBuscar);

                    if (ticket != null) {
                        panelFormulario.setVisible(true);
                        txtId.setText(ticket.getId());
                        txtAsunto.setText(ticket.getAsunto());
                        txtDescripcion.setText(ticket.getDescripcion());
                        cmbEstado.setSelectedItem(ticket.getEstado());

                        cargarUsuariosEnComboBox(cmbUsuario);
                        cargarDepartamentosEnComboBox(cmbDepartamento);

                        seleccionarUsuarioEnComboBox(cmbUsuario, ticket.getUsuario().getId());
                        seleccionarDepartamentoEnComboBox(cmbDepartamento, ticket.getDepartamento().getId());

                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                "Ticket encontrado",
                                "Busqueda Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        panelFormulario.setVisible(false);
                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                "No existe ticket con ID: " + idBuscar + "\nTIENE QUE RESPETAR MAYUSCULAS Y MINUSCULAS",
                                "No Encontrado",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Error en busqueda:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {

                if (txtAsunto.getText().trim().isEmpty() || txtDescripcion.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Asunto y descripcion son obligatorios",
                            "Error de Validacion",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {

                    String usuarioId = ((String) cmbUsuario.getSelectedItem()).split(" - ")[0];
                    String departamentoId = ((String) cmbDepartamento.getSelectedItem()).split(" - ")[0];

                    ControladorUsuario ctrlUsuario = new ControladorUsuario();
                    ControladorDepartamento ctrlDepto = new ControladorDepartamento();
                    Usuario usuario = ctrlUsuario.buscarPorId(usuarioId);
                    Departamento departamento = ctrlDepto.buscarPorId(departamentoId);

                    Ticket ticketActualizado = new Ticket(
                            txtId.getText(), usuario, departamento,
                            txtAsunto.getText().trim(), txtDescripcion.getText().trim(),
                            (String) cmbEstado.getSelectedItem()
                    );

                    ControladorTicket controlador = new ControladorTicket();
                    boolean exito = controlador.actualizarTicket(ticketActualizado);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                "Ticket actualizado correctamente!",
                                "Actualizacion Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                        txtIdBuscar.setText("");
                        panelFormulario.setVisible(false);

                    } else {
                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                "Error al actualizar ticket",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Error inesperado:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelContenido.add(panelModificar, "modificar");
    }

    // PANEL PARA ELIMINAR TICKET
    private void crearPanelEliminar() {

        JPanel panelEliminar = new JPanel(new BorderLayout(10, 10));
        panelEliminar.setBorder(BorderFactory.createTitledBorder("ELIMINAR TICKET"));

        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("ID del ticket a eliminar:");
        JTextField txtId = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR TICKET");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtId);
        panelBuscar.add(btnBuscar);

        JTextArea areaInfo = new JTextArea(8, 50);
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaInfo.setText("Ingrese el ID y presione BUSCAR para ver la informacion...");
        JScrollPane scrollInfo = new JScrollPane(areaInfo);

        panelEliminar.add(panelBuscar, BorderLayout.NORTH);
        panelEliminar.add(scrollInfo, BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {

                String idBuscar = txtId.getText().trim();

                if (idBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Ingrese un ID",
                            "Campo Vacio",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    ControladorTicket controlador = new ControladorTicket();
                    Ticket ticket = controlador.buscarPorId(idBuscar);

                    if (ticket != null) {

                        areaInfo.setText("TICKET ENCONTRADO:\n\n" +
                                "ID: " + ticket.getId() + "\n" +
                                "Usuario: " + ticket.getUsuario().getNombre() + "\n" +
                                "Departamento: " + ticket.getDepartamento().getNombre() + "\n" +
                                "Asunto: " + ticket.getAsunto() + "\n" +
                                "Descripcion: " + ticket.getDescripcion() + "\n" +
                                "Estado: " + ticket.getEstado() + "\n\n" +
                                "Confirmar eliminacion permanente.");

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
                                        VentanaTickets.this,
                                        "Eliminar ticket permanentemente?\n\n" +
                                                "ID: " + ticket.getId() + "\n" +
                                                "Asunto: " + ticket.getAsunto(),
                                        "CONFIRMACION FINAL",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE);

                                if (confirmacion == JOptionPane.YES_OPTION) {

                                    try {
                                        ControladorTicket ctrlEliminar = new ControladorTicket();
                                        boolean exito = ctrlEliminar.eliminarTicket(ticket.getId());

                                        if (exito) {
                                            JOptionPane.showMessageDialog(VentanaTickets.this,
                                                    "Ticket eliminado correctamente",
                                                    "Eliminacion Exitosa",
                                                    JOptionPane.INFORMATION_MESSAGE);

                                            areaInfo.setText("Ticket eliminado.\nPuede buscar otro.");
                                            txtId.setText("");
                                            panelEliminar.remove(2);
                                            panelEliminar.revalidate();
                                            panelEliminar.repaint();

                                        } else {
                                            JOptionPane.showMessageDialog(VentanaTickets.this,
                                                    "Error al eliminar ticket",
                                                    "Error",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }

                                    } catch (Exception ex) {
                                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                                "Error inesperado:\n" + ex.getMessage(),
                                                "Error del Sistema",
                                                JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        });

                    } else {
                        areaInfo.setText("No existe ticket con el ID: " + idBuscar +
                                "\nVerifique e intente nuevamente." + "\nTIENE QUE RESPETAR MAYUSCULAS Y MINUSCULAS");

                        if (panelEliminar.getComponentCount() > 2) {
                            panelEliminar.remove(2);
                            panelEliminar.revalidate();
                            panelEliminar.repaint();
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Error al buscar ticket:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelContenido.add(panelEliminar, "eliminar");
    }

    // PANEL PARA MOSTRAR TODOS LOS TICKETS
    private void crearPanelMostrar() {

        JPanel panelMostrar = new JPanel(new BorderLayout());
        panelMostrar.setBorder(BorderFactory.createTitledBorder("LISTA DE TICKETS"));

        String[] columnas = {"ID", "Usuario", "Departamento", "Asunto", "Estado"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaTickets = new JTable(modeloTabla);
        tablaTickets.setFillsViewportHeight(true);
        tablaTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaTickets.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaTickets.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollTabla = new JScrollPane(tablaTickets);

        JButton btnActualizar = new JButton("ACTUALIZAR LISTA");
        btnActualizar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {

                try {
                    ControladorTicket controlador = new ControladorTicket();
                    Collection<Ticket> tickets = controlador.obtenerTodos();

                    if (tickets != null && !tickets.isEmpty()) {

                        modeloTabla.setRowCount(0);

                        for (Ticket ticket : tickets) {
                            Object[] fila = {
                                    ticket.getId(),
                                    ticket.getUsuario().getNombre(),
                                    ticket.getDepartamento().getNombre(),
                                    ticket.getAsunto(),
                                    ticket.getEstado()
                            };
                            modeloTabla.addRow(fila);
                        }

                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                "Lista cargada! Tickets encontrados: " + tickets.size(),
                                "Lista Actualizada",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(VentanaTickets.this,
                                "No hay tickets registrados.",
                                "Lista Vacia",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaTickets.this,
                            "Error al cargar tickets:\n" + ex.getMessage(),
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

    // MÉTODOS AUXILIARES DE COMBOBOX
    //CARGAR USUARIOS
    private void cargarUsuariosEnComboBox(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        try {
            ControladorUsuario controlador = new ControladorUsuario();
            Collection<Usuario> usuarios = controlador.obtenerTodos();
            if (usuarios != null && !usuarios.isEmpty()) {
                for (Usuario usuario : usuarios) {
                    comboBox.addItem(usuario.getId() + " - " + usuario.getNombre() + " (" + usuario.getEmail() + ")");
                }
            } else {
                comboBox.addItem("NO HAY USUARIOS REGISTRADOS");
            }
        } catch (Exception ex) {
            comboBox.addItem("ERROR AL CARGAR USUARIOS");
        }
    }

    //CARGAR DEPARTAMENTOS
    private void cargarDepartamentosEnComboBox(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        try {
            ControladorDepartamento controlador = new ControladorDepartamento();
            Collection<Departamento> departamentos = controlador.obtenerTodos();
            if (departamentos != null && !departamentos.isEmpty()) {
                for (Departamento depto : departamentos) {
                    comboBox.addItem(depto.getId() + " - " + depto.getNombre());
                }
            } else {
                comboBox.addItem("NO HAY DEPARTAMENTOS REGISTRADOS");
            }
        } catch (Exception ex) {
            comboBox.addItem("ERROR AL CARGAR DEPARTAMENTOS");
        }
    }

    //CREAR COMBOBOX DE USUARIOS
    private void seleccionarUsuarioEnComboBox(JComboBox<String> comboBox, String usuarioId) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            String item = comboBox.getItemAt(i);
            if (item.startsWith(usuarioId + " - ")) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    //CREAR COMBOBOX DE DEPARTAMENTOS
    private void seleccionarDepartamentoEnComboBox(JComboBox<String> comboBox, String departamentoId) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            String item = comboBox.getItemAt(i);
            if (item.startsWith(departamentoId + " - ")) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    //ABRIR VENTANA
    public static void abrirVentana() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new VentanaTickets().setVisible(true);
            }
        });
    }
}
