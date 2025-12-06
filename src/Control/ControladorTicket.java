package Control;

import AccesoADatos.ServicioTicket;
import LogicaDeNegocio.entidades.Ticket;
import LogicaDeNegocio.resultados.ResultadoAnalisis;
import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import java.util.Collection;

//CLASE DE CONTROLADOR PARA LOS TIQUETES
public class ControladorTicket {

    private ServicioTicket servicioTicket;

    //CONSTRUCTOR
    public ControladorTicket() {
        this.servicioTicket = new ServicioTicket();
    }

    //CREAR UN TIQUETE
    public boolean crearTicket(Ticket ticket) {
        try {
            servicioTicket.insertar(ticket);
            System.out.println("Ticket creado ID: " + ticket.getId());
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error creando ticket: " + e.getMessage());
            return false;
        }
    }

    //METODO PARA REGRESAR TODOS LOS TIQUETES
    public Collection<Ticket> obtenerTodos() {
        try {
            return servicioTicket.listar();
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error obteniendo tickets: " + e.getMessage());
            return null;
        }
    }

    //BUSCAR UN TIQUETE SEGUN SU ID
    public Ticket buscarPorId(String id) {
        try {
            return servicioTicket.buscar(id);
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error buscando ticket: " + e.getMessage());
            return null;
        }
    }

    //METODO PARA ACTUALIZAR LA INFORMACIÓN DE UN TIQUETE
    public boolean actualizarTicket(Ticket ticket) {
        try {
            servicioTicket.modificar(ticket);
            System.out.println("Ticket actualizado ID: " + ticket.getId());
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error actualizando ticket: " + e.getMessage());
            return false;
        }
    }

    //METODO PARA ELIMINAR UN TIQUETE
    public boolean eliminarTicket(String id) {
        try {
            servicioTicket.eliminar(id);
            System.out.println("Ticket eliminado ID: " + id);
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error eliminando ticket: " + e.getMessage());
            return false;
        }
    }

    //ANALIZAR BAG OF WORDS DE UN TIQUETE ESPECIFICO
    public ResultadoAnalisis analizarTicket(String ticketId) {
        try {
            Ticket ticket = servicioTicket.buscar(ticketId);
            if (ticket != null) {
                return ticket.analizarBoW();
            }
            return null;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error analizando Bag of Words del ticket: " + e.getMessage());
            return null;
        }
    }

}
