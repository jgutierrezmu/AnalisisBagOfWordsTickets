--DEFINICION DEL TYPE REF--
CREATE OR REPLACE PACKAGE types AS
  TYPE ref_cursor IS REF CURSOR;
END;
/

--REINICIAR TABLA USUARIOS--
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE usuario CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL; END;
/

--CREAR TABLA USUARIO--
CREATE TABLE usuario (
  id VARCHAR2(100) PRIMARY KEY,
  nombre VARCHAR2(100),
  email VARCHAR2(100) UNIQUE,
  password VARCHAR2(100),
  telefono VARCHAR2(20),
  rol VARCHAR2(20)
);

--CRUD USUARIO--
--1. INSERTAR USUARIO--
CREATE OR REPLACE PROCEDURE insertarUsuario(
  p_id IN usuario.id%TYPE,
  p_nombre IN usuario.nombre%TYPE,
  p_email IN usuario.email%TYPE,
  p_password IN usuario.password%TYPE,
  p_telefono IN usuario.telefono%TYPE,
  p_rol IN usuario.rol%TYPE
) AS
BEGIN
  INSERT INTO usuario VALUES(p_id,p_nombre,p_email,p_password,p_telefono,p_rol);
END;
/

--2. Modificar usuario--
CREATE OR REPLACE PROCEDURE modificarUsuario(
  p_id IN usuario.id%TYPE,
  p_nombre IN usuario.nombre%TYPE,
  p_email IN usuario.email%TYPE,
  p_password IN usuario.password%TYPE,
  p_telefono IN usuario.telefono%TYPE,
  p_rol IN usuario.rol%TYPE
) AS
BEGIN
  UPDATE usuario
  SET nombre = p_nombre,
      email = p_email,
      password = p_password,
      telefono = p_telefono,
      rol = p_rol
  WHERE id = p_id;
END;
/

--3. Eliminar usuario--
CREATE OR REPLACE PROCEDURE eliminarUsuario(
  p_id IN usuario.id%TYPE
) AS
BEGIN
  DELETE FROM usuario WHERE id = p_id;
END;
/

--4. BUSCAR USUARIO POR ID--
CREATE OR REPLACE FUNCTION buscarUsuario(
  p_id IN usuario.id%TYPE
) RETURN types.ref_cursor
AS u_cursor types.ref_cursor;
BEGIN
  OPEN u_cursor FOR SELECT * FROM usuario WHERE id = p_id;
  RETURN u_cursor;
END;
/

--4a. BUSCAR USUARIO POR EMAIL--
CREATE OR REPLACE FUNCTION buscarUsuarioPorEmail(
  p_email IN usuario.email%TYPE
) RETURN types.ref_cursor
AS u_cursor types.ref_cursor;
BEGIN
  OPEN u_cursor FOR SELECT * FROM usuario WHERE email = p_email;
  RETURN u_cursor;
END;
/

--5. Mostrar todos los usuarios--
CREATE OR REPLACE FUNCTION listarUsuarios
RETURN types.ref_cursor
AS u_cursor types.ref_cursor;
BEGIN
  OPEN u_cursor FOR SELECT * FROM usuario;
  RETURN u_cursor;
END;
/


--REINICIAR TABLA DEPARTAMENTO--
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE departamento CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL; END;
/

--CREAR TABLA DEPARTAMENTO--
CREATE TABLE departamento (
  id VARCHAR2(100) PRIMARY KEY,
  nombre VARCHAR2(100) NOT NULL,
  descripcion VARCHAR2(200),
  contacto VARCHAR2(100)
);

--CRUD DEPARTAMENTO--
--1. Insertar departamento--
CREATE OR REPLACE PROCEDURE insertarDepartamento(
  p_id IN departamento.id%TYPE,
  p_nombre IN departamento.nombre%TYPE,
  p_descripcion IN departamento.descripcion%TYPE,
  p_contacto IN departamento.contacto%TYPE
) AS
BEGIN
  INSERT INTO departamento VALUES(p_id,p_nombre,p_descripcion,p_contacto);
END;
/

--2. Modificar un departamento--
CREATE OR REPLACE PROCEDURE modificarDepartamento(
  p_id IN departamento.id%TYPE,
  p_nombre IN departamento.nombre%TYPE,
  p_descripcion IN departamento.descripcion%TYPE,
  p_contacto IN departamento.contacto%TYPE
) AS
BEGIN
  UPDATE departamento
  SET nombre = p_nombre,
      descripcion = p_descripcion,
      contacto = p_contacto
  WHERE id = p_id;
END;
/

--3. Eliminar departamento--
CREATE OR REPLACE PROCEDURE eliminarDepartamento(
  p_id IN departamento.id%TYPE
) AS
BEGIN
  DELETE FROM departamento WHERE id = p_id;
END;
/

--4. Buscar departamento por id--
CREATE OR REPLACE FUNCTION buscarDepartamento(
  p_id IN departamento.id%TYPE
) RETURN types.ref_cursor
AS
  dep_cursor types.ref_cursor;
BEGIN
  OPEN dep_cursor FOR SELECT * FROM departamento WHERE id = p_id;
  RETURN dep_cursor;
END;
/

--5. Mostrar todos los departamentos--
CREATE OR REPLACE FUNCTION listarDepartamentos
RETURN types.ref_cursor
AS
  dep_cursor types.ref_cursor;
BEGIN
  OPEN dep_cursor FOR SELECT * FROM departamento;
  RETURN dep_cursor;
END;
/

--REINICIAR TABLA TICKET--
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE ticket CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL; END;
/

--CREAR TABLA TICKET--
CREATE TABLE ticket (
  id VARCHAR2(100) PRIMARY KEY,
  usuario_id VARCHAR2(100),
  departamento_id VARCHAR2(100),
  asunto VARCHAR2(200),
  descripcion VARCHAR2(500),
  estado VARCHAR2(20),
  
  CONSTRAINT fk_ticket_usuario FOREIGN KEY(usuario_id) REFERENCES usuario(id),
  CONSTRAINT fk_ticket_departamento FOREIGN KEY(departamento_id) REFERENCES departamento(id)
);

--CRUD TICKET--
--1. Crear ticket--
CREATE OR REPLACE PROCEDURE insertarTicket(
  p_id IN ticket.id%TYPE,
  p_usuario IN ticket.usuario_id%TYPE,
  p_depto IN ticket.departamento_id%TYPE,
  p_asunto IN ticket.asunto%TYPE,
  p_descripcion IN ticket.descripcion%TYPE,
  p_estado IN ticket.estado%TYPE
) AS
BEGIN
  INSERT INTO ticket (id, usuario_id, departamento_id, asunto, descripcion, estado)
  VALUES (p_id, p_usuario, p_depto, p_asunto, p_descripcion, p_estado);
END;
/

--2. Modificar ticket---
CREATE OR REPLACE PROCEDURE modificarTicket(
  p_id IN ticket.id%TYPE,
  p_asunto IN ticket.asunto%TYPE,
  p_descripcion IN ticket.descripcion%TYPE,
  p_estado IN ticket.estado%TYPE
) AS
BEGIN
  UPDATE ticket
  SET asunto = p_asunto,
      descripcion = p_descripcion,
      estado = p_estado
  WHERE id = p_id;
END;
/

--3. Eliminar ticket--
CREATE OR REPLACE PROCEDURE eliminarTicket(
  p_id IN ticket.id%TYPE
) AS
BEGIN
  DELETE FROM ticket WHERE id = p_id;
END;
/

-- BUSCAR TICKETS
CREATE OR REPLACE FUNCTION buscarTicket(
  p_id IN ticket.id%TYPE
) RETURN types.ref_cursor
AS
  t_cursor types.ref_cursor;
BEGIN
  OPEN t_cursor FOR 
    SELECT t.id as ticket_id,
           t.usuario_id,
           t.departamento_id,
           t.asunto,
           t.descripcion as ticket_descripcion,
           t.estado,
           u.id as usuario_id, 
           u.nombre as usuario_nombre, 
           u.email as usuario_email,
           u.password as usuario_password,
           u.telefono as usuario_telefono, 
           u.rol as usuario_rol,
           d.id as departamento_id, 
           d.nombre as departamento_nombre, 
           d.descripcion as departamento_descripcion,
           d.contacto as departamento_contacto
    FROM ticket t
    JOIN usuario u ON t.usuario_id = u.id
    JOIN departamento d ON t.departamento_id = d.id
    WHERE t.id = p_id;
  RETURN t_cursor;
END;
/

-- LISTAR LOS TICKETS
CREATE OR REPLACE FUNCTION listarTickets
RETURN types.ref_cursor
AS
  t_cursor types.ref_cursor;
BEGIN
  OPEN t_cursor FOR 
    SELECT t.id as ticket_id,
           t.usuario_id,
           t.departamento_id,
           t.asunto,
           t.descripcion as ticket_descripcion,
           t.estado,
           u.id as usuario_id, 
           u.nombre as usuario_nombre, 
           u.email as usuario_email,
           u.password as usuario_password,
           u.telefono as usuario_telefono, 
           u.rol as usuario_rol,
           d.id as departamento_id, 
           d.nombre as departamento_nombre, 
           d.descripcion as departamento_descripcion,
           d.contacto as departamento_contacto
    FROM ticket t
    JOIN usuario u ON t.usuario_id = u.id
    JOIN departamento d ON t.departamento_id = d.id;
  RETURN t_cursor;
END;
/

-- LISTAR TICKETS POR USUARIOS
CREATE OR REPLACE FUNCTION listarTicketsPorUsuario(
  p_usuario IN ticket.usuario_id%TYPE
) RETURN types.ref_cursor
AS
  t_cursor types.ref_cursor;
BEGIN
  OPEN t_cursor FOR 
    SELECT t.id as ticket_id,
           t.usuario_id,
           t.departamento_id,
           t.asunto,
           t.descripcion as ticket_descripcion,
           t.estado,
           u.id as usuario_id, 
           u.nombre as usuario_nombre, 
           u.email as usuario_email,
           u.password as usuario_password,
           u.telefono as usuario_telefono, 
           u.rol as usuario_rol,
           d.id as departamento_id, 
           d.nombre as departamento_nombre, 
           d.descripcion as departamento_descripcion,
           d.contacto as departamento_contacto
    FROM ticket t
    JOIN usuario u ON t.usuario_id = u.id
    JOIN departamento d ON t.departamento_id = d.id
    WHERE t.usuario_id = p_usuario;
  RETURN t_cursor;
END;
/

-- LISTAR TICKETS POR DEPARTAMENTOS
CREATE OR REPLACE FUNCTION listarTicketsPorDepartamento(
  p_depto IN ticket.departamento_id%TYPE
) RETURN types.ref_cursor
AS
  t_cursor types.ref_cursor;
BEGIN
  OPEN t_cursor FOR 
    SELECT t.id as ticket_id,
           t.usuario_id,
           t.departamento_id,
           t.asunto,
           t.descripcion as ticket_descripcion,
           t.estado,
           u.id as usuario_id, 
           u.nombre as usuario_nombre, 
           u.email as usuario_email,
           u.password as usuario_password,
           u.telefono as usuario_telefono, 
           u.rol as usuario_rol,
           d.id as departamento_id, 
           d.nombre as departamento_nombre, 
           d.descripcion as departamento_descripcion,
           d.contacto as departamento_contacto
    FROM ticket t
    JOIN usuario u ON t.usuario_id = u.id
    JOIN departamento d ON t.departamento_id = d.id
    WHERE t.departamento_id = p_depto;
  RETURN t_cursor;
END;
/


-- REINICIAR TABLA DICCIONARIO EMOCIONAL
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE diccionario_emocional CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- CREAR TABLA DICCIONARIO EMOCIONAL
CREATE TABLE diccionario_emocional (
  palabra VARCHAR2(100) PRIMARY KEY,
  emocion VARCHAR2(50) NOT NULL
);

--AGREGAR PALABRA A DICCIONARIO EMOCIONAL--
CREATE OR REPLACE PROCEDURE insertarDiccionarioEmocional(
  p_palabra IN diccionario_emocional.palabra%TYPE,
  p_emocion IN diccionario_emocional.emocion%TYPE
) AS
BEGIN
  INSERT INTO diccionario_emocional (palabra, emocion)
  VALUES (p_palabra, p_emocion);
END;
/

--MODIFICAR PALABRA EMOCIONAL
CREATE OR REPLACE PROCEDURE modificarDiccionarioEmocional(
  p_palabra IN diccionario_emocional.palabra%TYPE,
  p_emocion IN diccionario_emocional.emocion%TYPE
) AS
BEGIN
  UPDATE diccionario_emocional
  SET emocion = p_emocion
  WHERE palabra = p_palabra;
END;
/

--ELIMINAR PALABRA EMOCIONAL
CREATE OR REPLACE PROCEDURE eliminarDiccionarioEmocional(
  p_palabra IN diccionario_emocional.palabra%TYPE
) AS
BEGIN
  DELETE FROM diccionario_emocional
  WHERE palabra = p_palabra;
END;
/

--BUSCAR PALABRA EMOCIONAL
CREATE OR REPLACE FUNCTION buscarDiccionarioEmocional(
  p_palabra IN diccionario_emocional.palabra%TYPE
) RETURN types.ref_cursor
AS
  d_cursor types.ref_cursor;
BEGIN
  OPEN d_cursor FOR
    SELECT * FROM diccionario_emocional
    WHERE palabra = p_palabra;
  RETURN d_cursor;
END;
/

--MOSTRAR DICCIONARIO EMOCIONAL
CREATE OR REPLACE FUNCTION listarDiccionarioEmocional
RETURN types.ref_cursor
AS
  d_cursor types.ref_cursor;
BEGIN
  OPEN d_cursor FOR
    SELECT * FROM diccionario_emocional
    ORDER BY palabra;
  RETURN d_cursor;
END;
/

--DICCINOARIO TECNICO
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE diccionario_tecnico CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL; END;
/

CREATE TABLE diccionario_tecnico (
  palabra VARCHAR2(100) PRIMARY KEY,
  categoria VARCHAR2(50) NOT NULL
);

--INSERTAR PALABRA
CREATE OR REPLACE PROCEDURE insertarDiccionarioTecnico(
  p_palabra IN diccionario_tecnico.palabra%TYPE,
  p_categoria IN diccionario_tecnico.categoria%TYPE
) AS
BEGIN
  INSERT INTO diccionario_tecnico (palabra, categoria)
  VALUES (p_palabra, p_categoria);
END;
/

--MODIFCAR PALABRA
CREATE OR REPLACE PROCEDURE modificarDiccionarioTecnico(
  p_palabra IN diccionario_tecnico.palabra%TYPE,
  p_categoria IN diccionario_tecnico.categoria%TYPE
) AS
BEGIN
  UPDATE diccionario_tecnico
  SET categoria = p_categoria
  WHERE palabra = p_palabra;
END;
/

--ELOIMINAR PALABRA
CREATE OR REPLACE PROCEDURE eliminarDiccionarioTecnico(
  p_palabra IN diccionario_tecnico.palabra%TYPE
) AS
BEGIN
  DELETE FROM diccionario_tecnico
  WHERE palabra = p_palabra;
END;
/

--BUSCAR PALABRA
CREATE OR REPLACE FUNCTION buscarDiccionarioTecnico(
  p_palabra IN diccionario_tecnico.palabra%TYPE
) RETURN types.ref_cursor
AS
  d_cursor types.ref_cursor;
BEGIN
  OPEN d_cursor FOR
    SELECT * FROM diccionario_tecnico
    WHERE palabra = p_palabra;
  RETURN d_cursor;
END;
/

--MOSTRAR DICCIONARIO
CREATE OR REPLACE FUNCTION listarDiccionarioTecnico
RETURN types.ref_cursor
AS
  d_cursor types.ref_cursor;
BEGIN
  OPEN d_cursor FOR
    SELECT * FROM diccionario_tecnico
    ORDER BY palabra;
  RETURN d_cursor;
END;
/


--USUARIOS
BEGIN
  insertarUsuario('U001', 'Arturo Molina', 'arturo@estudiante.cr', '123456', '88881111', 'ESTUDIANTE');
  insertarUsuario('U002', 'Daniela Rojas', 'drojas@estudiante.cr', 'abcdefg', '88882222', 'ESTUDIANTE');
  insertarUsuario('U003', 'Carlos Méndez', 'cmendez@funcionario.cr', 'xyz12377', '88883333', 'FUNCIONARIO');
  insertarUsuario('U004', 'Ana Gómez', 'agomez@admin.cr', 'admin007', '88884444', 'ADMINISTRADOR');
  COMMIT;
END;
/

--DEPARTAMENTOS
BEGIN
  insertarDepartamento('D001', 'Soporte Técnico', 'Problemas de equipos y accesos', 'soporte@centro.cr');
  insertarDepartamento('D002', 'Finanzas', 'Pagos y becas', 'finanzas@centro.cr');
  insertarDepartamento('D003', 'Registro', 'Matrículas y certificados', 'registro@centro.cr');
  insertarDepartamento('D004', 'Infraestructura', 'Mantenimiento físico', 'infra@centro.cr');
  COMMIT;
END;
/

-- 
-- TICKETS DE PRUEBA
-- Ticket 1: Emoción fuerte negativa + RED/WIFI/ERROR
BEGIN
  insertarTicket(
    'T001',
    'U001',
    'D001',
    'Wifi no funciona en el edificio',
    'Estoy frustrado y muy molesto porque el wifi dejó de funcionar. El internet se cae, el router marca error y no puedo trabajar.',
    'NUEVO'
  );
  COMMIT;
END;
/

-- Ticket 2: Ansiedad + AUTENTICACION/CREDENCIALES
BEGIN
  insertarTicket(
    'T002',
    'U002',
    'D002',
    'Correo institucional bloqueado',
    'Tengo ansiedad porque el sistema bloqueó mis credenciales. No puedo acceder al correo por un error de autenticacion.',
    'EN_PROCESO'
  );
  COMMIT;
END;
/

-- Ticket 3: Neutral + IMPRESORAS/MANTENIMIENTO
BEGIN
  insertarTicket(
    'T003',
    'U003',
    'D003',
    'Impresora sin tinta en laboratorio',
    'La impresora muestra fallo de cartucho y no imprime. Solicito mantenimiento urgente, parece daño en el equipo.',
    'NUEVO'
  );
  COMMIT;
END;
/

-- Ticket 4: Emoción positiva + FINANZAS/BECAS
BEGIN
  insertarTicket(
    'T004',
    'U004',
    'D002',
    'Consulta sobre beca y pagos',
    'Estoy satisfecho con el servicio recibido y agradecido por la atención. Necesito información actualizada sobre el pago de la beca.',
    'RESUELTO'
  );
  COMMIT;
END;
/

BEGIN
  insertarDiccionarioEmocional('enojado', 'ENOJO');
  insertarDiccionarioEmocional('molesto', 'ENOJO');
  insertarDiccionarioEmocional('indignado', 'ENOJO');
  insertarDiccionarioEmocional('irritado', 'ENOJO');
  insertarDiccionarioEmocional('furioso', 'ENOJO');
  insertarDiccionarioEmocional('frustrado', 'FRUSTRACION');
  insertarDiccionarioEmocional('impotente', 'FRUSTRACION');
  insertarDiccionarioEmocional('cansado', 'FRUSTRACION');
  insertarDiccionarioEmocional('harto', 'FRUSTRACION');
  insertarDiccionarioEmocional('triste', 'TRISTEZA');
  insertarDiccionarioEmocional('desanimado', 'TRISTEZA');
  insertarDiccionarioEmocional('deprimido', 'TRISTEZA');
  insertarDiccionarioEmocional('abatido', 'TRISTEZA');
  insertarDiccionarioEmocional('afligido', 'TRISTEZA');
  insertarDiccionarioEmocional('asustado', 'MIEDO');
  insertarDiccionarioEmocional('temeroso', 'MIEDO');
  insertarDiccionarioEmocional('preocupado', 'MIEDO');
  insertarDiccionarioEmocional('inseguro', 'MIEDO');
  insertarDiccionarioEmocional('ansioso', 'ANSIEDAD');
  insertarDiccionarioEmocional('nervioso', 'ANSIEDAD');
  insertarDiccionarioEmocional('inquieto', 'ANSIEDAD');
  insertarDiccionarioEmocional('agradecido', 'GRATITUD');
  insertarDiccionarioEmocional('complacido', 'GRATITUD');
  insertarDiccionarioEmocional('feliz', 'ALEGRIA');
  insertarDiccionarioEmocional('contento', 'ALEGRIA');
  insertarDiccionarioEmocional('animado', 'ALEGRIA');
  insertarDiccionarioEmocional('satisfecho', 'SATISFACCION');
  insertarDiccionarioEmocional('conforme', 'SATISFACCION');
  insertarDiccionarioEmocional('normal', 'NEUTRAL');
  insertarDiccionarioEmocional('neutro', 'NEUTRAL');
  COMMIT;
END;
/

BEGIN
  -- SOPORTE TI
  insertarDiccionarioTecnico('soporte', 'SOPORTE_TI');
  insertarDiccionarioTecnico('helpdesk', 'SOPORTE_TI');
  insertarDiccionarioTecnico('actualizacion', 'SOPORTE_TI');
  insertarDiccionarioTecnico('instalacion', 'SOPORTE_TI');
  insertarDiccionarioTecnico('error', 'SOPORTE_TI');

  -- SOFTWARE
  insertarDiccionarioTecnico('software', 'SOFTWARE');
  insertarDiccionarioTecnico('aplicacion', 'SOFTWARE');
  insertarDiccionarioTecnico('programa', 'SOFTWARE');
  insertarDiccionarioTecnico('licencia', 'SOFTWARE');

  -- HARDWARE
  insertarDiccionarioTecnico('hardware', 'HARDWARE');
  insertarDiccionarioTecnico('computadora', 'HARDWARE');
  insertarDiccionarioTecnico('equipo', 'HARDWARE');
  insertarDiccionarioTecnico('monitor', 'HARDWARE');
  insertarDiccionarioTecnico('teclado', 'HARDWARE');

  -- REDES
  insertarDiccionarioTecnico('red', 'REDES');
  insertarDiccionarioTecnico('router', 'REDES');
  insertarDiccionarioTecnico('switch', 'REDES');

  -- WIFI
  insertarDiccionarioTecnico('wifi', 'WIFI');
  insertarDiccionarioTecnico('inalambrico', 'WIFI');

  -- INTERNET
  insertarDiccionarioTecnico('internet', 'INTERNET');
  insertarDiccionarioTecnico('conexion', 'INTERNET');
  insertarDiccionarioTecnico('velocidad', 'INTERNET');

  -- INFRAESTRUCTURA
  insertarDiccionarioTecnico('servidor', 'INFRAESTRUCTURA');
  insertarDiccionarioTecnico('infraestructura', 'INFRAESTRUCTURA');
  insertarDiccionarioTecnico('data', 'INFRAESTRUCTURA');

  -- IMPRESORAS
  insertarDiccionarioTecnico('impresora', 'IMPRESORAS');
  insertarDiccionarioTecnico('cartucho', 'IMPRESORAS');
  insertarDiccionarioTecnico('tinta', 'IMPRESORAS');

  -- ESCANER
  insertarDiccionarioTecnico('escaner', 'ESCANER');

  -- PERIFERICOS
  insertarDiccionarioTecnico('mouse', 'PERIFERICOS');
  insertarDiccionarioTecnico('periferico', 'PERIFERICOS');

  -- CORREO
  insertarDiccionarioTecnico('correo', 'CORREO');
  insertarDiccionarioTecnico('buzon', 'CORREO');

  -- AUTENTICACION
  insertarDiccionarioTecnico('login', 'AUTENTICACION');
  insertarDiccionarioTecnico('acceso', 'AUTENTICACION');
  insertarDiccionarioTecnico('bloqueo', 'AUTENTICACION');

  -- SEGURIDAD
  insertarDiccionarioTecnico('seguridad', 'SEGURIDAD');
  insertarDiccionarioTecnico('token', 'SEGURIDAD');
  insertarDiccionarioTecnico('phishing', 'SEGURIDAD');

  -- CREDENCIALES
  insertarDiccionarioTecnico('contraseña', 'CREDENCIALES');
  insertarDiccionarioTecnico('clave', 'CREDENCIALES');
  insertarDiccionarioTecnico('credenciales', 'CREDENCIALES');

  -- FINANZAS
  insertarDiccionarioTecnico('factura', 'FINANZAS');
  insertarDiccionarioTecnico('cobro', 'FINANZAS');
  insertarDiccionarioTecnico('pago', 'FINANZAS');

  -- PAGOS
  insertarDiccionarioTecnico('transferencia', 'PAGOS');
  insertarDiccionarioTecnico('deposito', 'PAGOS');

  -- MATRICULA
  insertarDiccionarioTecnico('matricula', 'MATRICULA');
  insertarDiccionarioTecnico('admisiones', 'MATRICULA');

  -- REGISTRO
  insertarDiccionarioTecnico('registro', 'REGISTRO');
  insertarDiccionarioTecnico('expediente', 'REGISTRO');

  -- BECAS
  insertarDiccionarioTecnico('beca', 'BECAS');
  insertarDiccionarioTecnico('beneficio', 'BECAS');

  -- RRHH
  insertarDiccionarioTecnico('rrhh', 'RRHH');
  insertarDiccionarioTecnico('recursos', 'RRHH');

  -- NOMINA
  insertarDiccionarioTecnico('nomina', 'NOMINA');
  insertarDiccionarioTecnico('planilla', 'NOMINA');

  -- VACACIONES
  insertarDiccionarioTecnico('vacaciones', 'VACACIONES');

  -- MANTENIMIENTO
  insertarDiccionarioTecnico('mantenimiento', 'MANTENIMIENTO');
  insertarDiccionarioTecnico('reparacion', 'MANTENIMIENTO');
  insertarDiccionarioTecnico('daño', 'MANTENIMIENTO');

  -- LIMPIEZA
  insertarDiccionarioTecnico('limpieza', 'LIMPIEZA');
  insertarDiccionarioTecnico('aseo', 'LIMPIEZA');

  -- ELECTRICIDAD
  insertarDiccionarioTecnico('electricidad', 'ELECTRICIDAD');
  insertarDiccionarioTecnico('corto', 'ELECTRICIDAD');
  insertarDiccionarioTecnico('interruptor', 'ELECTRICIDAD');

  -- GENERAL (Fallback)
  insertarDiccionarioTecnico('problema', 'GENERAL');
  insertarDiccionarioTecnico('incidencia', 'GENERAL');
  COMMIT;
END;
/





























