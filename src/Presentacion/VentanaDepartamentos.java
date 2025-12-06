package Presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Control.ControladorDepartamento;
import LogicaDeNegocio.entidades.Departamento;
import java.util.Collection;

public class VentanaDepartamentos extends JFrame {

    //PANELES PARA CADA FUNCIONALIDAD
    private JPanel panelContenido;
    private CardLayout cardLayout;

    //CONSTRUCTOR DE CLASE (DEFINE COMPONENTES Y LOS COLOCA)
    public VentanaDepartamentos() {
        //CONFIGURAR LA VENTANA DE DEPARTAMENTOS
        setTitle("Gestión de Departamentos - Sistema Bag of Words");
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
                // NO cargar datos automáticamente - solo mostrar pestaña vacía
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
        JLabel lblInicial = new JLabel("Seleccione una opción para gestionar los departamentos", JLabel.CENTER);
        lblInicial.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panelInicial.add(lblInicial, BorderLayout.CENTER);
        panelContenido.add(panelInicial, "inicial");
    }

    //PANEL PARA AGREGAR DEPARTAMENTO
    private void crearPanelAgregar() {
        JPanel panelAgregar = new JPanel(new GridLayout(5, 2, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("AGREGAR NUEVO DEPARTAMENTO"));

        //CAMPOS DEL FORMULARIO (ADAPTADOS A DEPARTAMENTO)
        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField();

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextField txtDescripcion = new JTextField();

        JLabel lblContacto = new JLabel("Contacto (Email):");
        JTextField txtContacto = new JTextField();

        //BOTÓN GUARDAR (AHORA CON FUNCIONALIDAD REAL)
        JButton btnGuardar = new JButton("GUARDAR DEPARTAMENTO");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // OBTENER DATOS DEL FORMULARIO
                String id = txtId.getText().trim();
                String nombre = txtNombre.getText().trim();
                String descripcion = txtDescripcion.getText().trim();
                String contacto = txtContacto.getText().trim();

                // VALIDAR CAMPOS OBLIGATORIOS
                if (id.isEmpty() || nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                            "Por favor complete los campos obligatorios: ID y Nombre",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // CREAR OBJETO DEPARTAMENTO
                    Departamento nuevoDepto = new Departamento(id, nombre, descripcion, contacto);

                    // LLAMAR AL CONTROLADOR PARA GUARDAR
                    ControladorDepartamento controlador = new ControladorDepartamento();
                    boolean exito = controlador.crearDepartamento(nuevoDepto);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                "Departamento registrado exitosamente!\n" +
                                        "ID: " + id + "\n" +
                                        "Nombre: " + nombre,
                                "Registro Exitoso",
                                JOptionPane.INFORMATION_MESSAGE);

                        // LIMPIAR FORMULARIO
                        txtId.setText("");
                        txtNombre.setText("");
                        txtDescripcion.setText("");
                        txtContacto.setText("");

                    } else {
                        JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                "Error al registrar el departamento.\n" +
                                        "El ID ya existe en el sistema.",
                                "Error de Registro",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                            "Error inesperado al registrar departamento:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        //AGREGAR COMPONENTES AL PANEL
        panelAgregar.add(lblId);
        panelAgregar.add(txtId);
        panelAgregar.add(lblNombre);
        panelAgregar.add(txtNombre);
        panelAgregar.add(lblDescripcion);
        panelAgregar.add(txtDescripcion);
        panelAgregar.add(lblContacto);
        panelAgregar.add(txtContacto);
        panelAgregar.add(new JLabel()); // Espacio vacío
        panelAgregar.add(btnGuardar);

        panelContenido.add(panelAgregar, "agregar");
    }

    //PANEL PARA MODIFICAR DEPARTAMENTO
    private void crearPanelModificar() {
        JPanel panelModificar = new JPanel(new BorderLayout(10, 10));
        panelModificar.setBorder(BorderFactory.createTitledBorder("MODIFICAR DEPARTAMENTO EXISTENTE"));

        //PANEL SUPERIOR PARA BUSCAR
        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("Buscar por ID:");
        JTextField txtIdBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtIdBuscar);
        panelBuscar.add(btnBuscar);

        //PANEL CENTRAL CON FORMULARIO (INICIALMENTE OCULTO)
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Departamento"));

        //CAMPOS NO EDITABLES: ID
        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);

        //CAMPOS EDITABLES
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextField txtDescripcion = new JTextField();

        JLabel lblContacto = new JLabel("Contacto (Email):");
        JTextField txtContacto = new JTextField();

        JButton btnActualizar = new JButton("ACTUALIZAR DEPARTAMENTO");

        //AGREGAR COMPONENTES AL FORMULARIO
        panelFormulario.add(lblId);
        panelFormulario.add(txtId);
        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(lblContacto);
        panelFormulario.add(txtContacto);
        panelFormulario.add(new JLabel());
        panelFormulario.add(btnActualizar);

        //INICIALMENTE OCULTAR EL FORMULARIO
        panelFormulario.setVisible(false);

        //AGREGAR COMPONENTES AL PANEL PRINCIPAL
        panelModificar.add(panelBuscar, BorderLayout.NORTH);
        panelModificar.add(panelFormulario, BorderLayout.CENTER);

        //ACCIONES DE BOTONES (AHORA CON FUNCIONALIDAD REAL)
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idBuscar = txtIdBuscar.getText().trim();

                if (idBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                            "Por favor ingrese un ID para buscar",
                            "Campo Vacío",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    // BUSCAR DEPARTAMENTO EN LA BASE DE DATOS
                    ControladorDepartamento controlador = new ControladorDepartamento();
                    Departamento departamento = controlador.buscarPorId(idBuscar);

                    if (departamento != null) {
                        // MOSTRAR FORMULARIO Y CARGAR DATOS
                        panelFormulario.setVisible(true);
                        txtId.setText(departamento.getId());
                        txtNombre.setText(departamento.getNombre());
                        txtDescripcion.setText(departamento.getDescripcion());
                        txtContacto.setText(departamento.getContacto());

                        JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                "Departamento encontrado!\n" +
                                        "Puede modificar los campos editables.",
                                "Búsqueda Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        panelFormulario.setVisible(false);
                        JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                "No se encontró ningún departamento con el ID: " + idBuscar + "\nTIENE QUE RESPETAR MAYUSCULAS Y MINUSCULAS",
                                "Departamento No Encontrado",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                            "Error al buscar departamento:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // VALIDAR CAMPOS OBLIGATORIOS
                if (txtNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                            "El campo Nombre es obligatorio",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // CREAR OBJETO DEPARTAMENTO ACTUALIZADO
                    Departamento departamentoActualizado = new Departamento(
                            txtId.getText(),
                            txtNombre.getText().trim(),
                            txtDescripcion.getText().trim(),
                            txtContacto.getText().trim()
                    );

                    // LLAMAR AL CONTROLADOR PARA ACTUALIZAR
                    ControladorDepartamento controlador = new ControladorDepartamento();
                    boolean exito = controlador.actualizarDepartamento(departamentoActualizado);

                    if (exito) {
                        JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                "Departamento actualizado exitosamente!\n" +
                                        "ID: " + departamentoActualizado.getId() + "\n" +
                                        "Nombre: " + departamentoActualizado.getNombre(),
                                "Actualización Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);

                        // LIMPIAR FORMULARIO
                        txtIdBuscar.setText("");
                        panelFormulario.setVisible(false);

                    } else {
                        JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                "Error al actualizar el departamento",
                                "Error de Actualización",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                            "Error inesperado al actualizar departamento:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        panelContenido.add(panelModificar, "modificar");
    }

    //PANEL PARA ELIMINAR DEPARTAMENTO
    private void crearPanelEliminar() {
        JPanel panelEliminar = new JPanel(new BorderLayout(10, 10));
        panelEliminar.setBorder(BorderFactory.createTitledBorder("ELIMINAR DEPARTAMENTO"));

        // PANEL SUPERIOR (BUSCAR DEPARTAMENTO POR ID)
        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblBuscar = new JLabel("ID del departamento a eliminar:");
        JTextField txtId = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR");

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtId);
        panelBuscar.add(btnBuscar);

        // ÁREA CENTRAL QUE MUESTRA LA INFORMACIÓN DEL DEPARTAMENTO
        JTextArea areaInfo = new JTextArea(8, 50);
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaInfo.setText("Ingrese el ID del departamento y haga clic en BUSCAR para ver la información...");
        JScrollPane scrollInfo = new JScrollPane(areaInfo);

        panelEliminar.add(panelBuscar, BorderLayout.NORTH);
        panelEliminar.add(scrollInfo, BorderLayout.CENTER);

        // EVENTO DEL BOTÓN BUSCAR
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // OBTENER ID INGRESADO
                String idBuscar = txtId.getText().trim();

                // VALIDACIÓN INICIAL
                if (idBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                            "Por favor ingrese un ID para buscar",
                            "Campo Vacío",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {

                    // BUSCAR DEPARTAMENTO POR CONTROLADOR
                    ControladorDepartamento controlador = new ControladorDepartamento();
                    Departamento departamento = controlador.buscarPorId(idBuscar);

                    // SI EXISTE, MOSTRAR INFORMACIÓN Y GENERAR BOTÓN DE CONFIRMACIÓN
                    if (departamento != null) {

                        // MOSTRAR INFORMACIÓN EN EL PANEL
                        areaInfo.setText("DEPARTAMENTO ENCONTRADO:\n\n" +
                                "ID: " + departamento.getId() + "\n" +
                                "Nombre: " + departamento.getNombre() + "\n" +
                                "Descripción: " + departamento.getDescripcion() + "\n" +
                                "Contacto: " + departamento.getContacto() + "\n\n" +
                                "¿Desea eliminar este departamento permanentemente?\n" +
                                "Esta acción no se puede deshacer.");

                        // BOTÓN PARA CONFIRMAR ELIMINACIÓN
                        JButton btnConfirmarEliminar = new JButton("CONFIRMAR ELIMINACIÓN");
                        btnConfirmarEliminar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        btnConfirmarEliminar.setBackground(Color.RED);
                        btnConfirmarEliminar.setForeground(Color.WHITE);

                        // REMOVER CONFIRMACIONES ANTERIORES (SI EXISTIERAN)
                        if (panelEliminar.getComponentCount() > 2) {
                            panelEliminar.remove(2);
                        }

                        // CREAR PANEL INFERIOR CON BOTÓN CONFIRMAR
                        JPanel panelConfirmacion = new JPanel(new FlowLayout());
                        panelConfirmacion.add(btnConfirmarEliminar);
                        panelEliminar.add(panelConfirmacion, BorderLayout.SOUTH);
                        panelEliminar.revalidate();
                        panelEliminar.repaint();

                        // EVENTO DEL BOTÓN CONFIRMAR ELIMINACIÓN
                        btnConfirmarEliminar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent ev) {

                                // CONFIRMACIÓN EMERGENTE (JOPtionPane)
                                int confirmacion = JOptionPane.showConfirmDialog(
                                        VentanaDepartamentos.this,
                                        "¿Está seguro de eliminar este departamento?\n\n" +
                                                "ID: " + departamento.getId() + "\n" +
                                                "Nombre: " + departamento.getNombre(),
                                        "CONFIRMAR ELIMINACIÓN",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE);

                                // SI CONFIRMA
                                if (confirmacion == JOptionPane.YES_OPTION) {

                                    try {
                                        boolean exito = controlador.eliminarDepartamento(departamento.getId());

                                        // EXITO EN ELIMINACIÓN
                                        if (exito) {
                                            JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                                    "Departamento eliminado correctamente",
                                                    "Eliminación Exitosa",
                                                    JOptionPane.INFORMATION_MESSAGE);

                                            // LIMPIAR PANELES
                                            areaInfo.setText("Departamento eliminado.\nPuede buscar otro.");
                                            txtId.setText("");

                                            // REMOVER PANEL DE CONFIRMACIÓN
                                            panelEliminar.remove(2);
                                            panelEliminar.revalidate();
                                            panelEliminar.repaint();

                                        } else {
                                            JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                                    "Error al eliminar el departamento"+ "\nNO SE PUEDEN ELIMINAR DEPARTAMENTOS ASOCIADOS A TICKETS",
                                                    "Error de Eliminación",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }

                                    } catch (Exception ex) {
                                        JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                                "Error inesperado:\n" + ex.getMessage(),
                                                "Error del Sistema",
                                                JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        });

                    } else {

                        // SI NO EXISTE EL DEPARTAMENTO
                        areaInfo.setText("No se encontró ningún departamento con el ID: " + idBuscar +
                                "\nVerifique e intente nuevamente." + "\nTIENE QUE RESPETAR MAYUSCULAS Y MINUSCULAS");

                        // REMOVER CONFIRMACIÓN SI ESTABA
                        if (panelEliminar.getComponentCount() > 2) {
                            panelEliminar.remove(2);
                            panelEliminar.revalidate();
                            panelEliminar.repaint();
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                            "Error al buscar departamento:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // AGREGAR AL CARDLAYOUT
        panelContenido.add(panelEliminar, "eliminar");
    }

    //PANEL PARA MOSTRAR TODOS LOS DEPARTAMENTOS
    private void crearPanelMostrar() {
        JPanel panelMostrar = new JPanel(new BorderLayout());
        panelMostrar.setBorder(BorderFactory.createTitledBorder("LISTA DE TODOS LOS DEPARTAMENTOS"));

        //CREAR TABLA CON MODELO DINÁMICO
        String[] columnas = {"ID", "Nombre", "Descripción", "Contacto"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };

        JTable tablaDepartamentos = new JTable(modeloTabla);
        tablaDepartamentos.setFillsViewportHeight(true);
        tablaDepartamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //MEJORAR APARIENCIA DE LA TABLA
        tablaDepartamentos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDepartamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollTabla = new JScrollPane(tablaDepartamentos);

        // BOTÓN PARA ACTUALIZAR
        JButton btnActualizar = new JButton("ACTUALIZAR LISTA");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // LLAMAR AL CONTROLADOR PARA OBTENER DEPARTAMENTOS
                    ControladorDepartamento controlador = new ControladorDepartamento();
                    Collection<Departamento> departamentos = controlador.obtenerTodos();

                    if (departamentos != null && !departamentos.isEmpty()) {
                        // LIMPIAR TABLA
                        modeloTabla.setRowCount(0);

                        // LLENAR TABLA CON DATOS REALES
                        for (Departamento depto : departamentos) {
                            Object[] fila = {
                                    depto.getId(),
                                    depto.getNombre(),
                                    depto.getDescripcion() != null ? depto.getDescripcion() : "",
                                    depto.getContacto() != null ? depto.getContacto() : ""
                            };
                            modeloTabla.addRow(fila);
                        }

                        JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                "Lista actualizada correctamente.\n" +
                                        "Departamentos encontrados: " + departamentos.size(),
                                "Lista Actualizada",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                                "No hay departamentos registrados en el sistema.",
                                "Lista Vacía",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaDepartamentos.this,
                            "Error al cargar la lista de departamentos:\n" + ex.getMessage(),
                            "Error del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        //PANEL INFERIOR CON BOTÓN
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(btnActualizar);

        panelMostrar.add(scrollTabla, BorderLayout.CENTER);
        panelMostrar.add(panelInferior, BorderLayout.SOUTH);

        panelContenido.add(panelMostrar, "mostrar");
    }

    //METODO PAR ABRIR VENTANA
    public static void abrirVentana() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaDepartamentos().setVisible(true);
            }
        });
    }
}
