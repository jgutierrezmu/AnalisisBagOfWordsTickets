package Aplicacion;

import Presentacion.MenuPrincipal;

import javax.swing.*;

//CLASE PARA INICIAR APLICACIÓN
public class BagOfWordsApp {
    public static void main(String[] args) {
        // Configuraciones iniciales
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}

