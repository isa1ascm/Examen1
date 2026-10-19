
package tienda;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TiendaVista vista = new TiendaVista();
                new TiendaControlador(vista);
            }
        });
    }
}
