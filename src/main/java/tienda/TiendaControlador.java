package tienda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TiendaControlador {

    private TiendaVista vista;
    private Compra compra;
    private Tarjeta tarjetaConsultada;
    private boolean pagoRealizado;

    public TiendaControlador(TiendaVista vista) {
        this.vista = vista;
        this.compra = new Compra();
        this.tarjetaConsultada = null;
        this.pagoRealizado = false;

        vista.botonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });

        vista.botonConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarTarjeta();
            }
        });

        vista.botonPagar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pagar();
            }
        });

        vista.botonQuitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitarProducto();
            }
        });

        vista.botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.dispose();

                TiendaVista nuevaVista = new TiendaVista();
                new TiendaControlador(nuevaVista);
            }
        });
    }

    private void agregarProducto() {
        if (pagoRealizado) {
            JOptionPane.showMessageDialog(vista,
                    "La compra ya fue pagada.");
            return;
        }

        int fila = vista.tablaProductos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona un producto de la lista.");
            return;
        }

        String nombre = vista.modeloProductos
                .getValueAt(fila, 0).toString();

        double precio = Double.parseDouble(
                vista.modeloProductos.getValueAt(fila, 1).toString());

        int cantidad = (Integer) vista.cantidad.getValue();

        Producto producto = new Producto(nombre, precio);
        compra.agregarProducto(producto, cantidad);

        double importe = precio * cantidad;

        vista.modeloCarrito.addRow(new Object[]{
                nombre,
                cantidad,
                String.format("$%.2f", precio),
                String.format("$%.2f", importe)
        });

        actualizarTotal();

        tarjetaConsultada = null;
        compra.setTarjeta(null);
        vista.datosTarjeta.setText(
                "Consulta la tarjeta después de terminar tu selección.");
    }

    private void actualizarTotal() {
        vista.totalCompra.setText(
                String.format("Total de compra: $%.2f",
                        compra.calcularTotal()));
    }

    private void consultarTarjeta() {
        if (pagoRealizado) {
            return;
        }

        String numero = vista.numeroTarjeta.getText().trim();
        String numeroLimpio = numero.replace("-", "")
                .replace(" ", "");

        if (!numeroLimpio.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(vista,
                    "Ingresa un número de tarjeta válido de 16 dígitos.");
            return;
        }

        if (!numeroLimpio.equals("1234123412441234")) {
            tarjetaConsultada = null;
            compra.setTarjeta(null);

            JOptionPane.showMessageDialog(vista,
                    "Tarjeta de prueba no encontrada.");
            return;
        }

        tarjetaConsultada = new Tarjeta(
                "XXXX-XXXX-XXXX-1234",
                "Banco de prueba",
                "Ciudad Obregón"
        );

        compra.setTarjeta(tarjetaConsultada);

        vista.numeroTarjeta.setText("");

        vista.datosTarjeta.setText(
                "<html><b>Banco:</b> Banco de prueba"
                        + " &nbsp;&nbsp; <b>Ciudad:</b> Ciudad Obregón"
                        + " &nbsp;&nbsp; <b>Tarjeta:</b> XXXX-XXXX-XXXX-1234"
                        + "</html>"
        );
    }

    private void quitarProducto() {
        if (pagoRealizado) {
            return;
        }

        int fila = vista.tablaCarrito.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona un producto del carrito.");
            return;
        }

        compra.quitarProducto(fila);
        vista.modeloCarrito.removeRow(fila);

        actualizarTotal();

        tarjetaConsultada = null;
        compra.setTarjeta(null);

        vista.datosTarjeta.setText(
                "Consulta la tarjeta después de terminar tu selección.");
    }

    private void pagar() {
        if (pagoRealizado) {
            return;
        }

        if (compra.getProductos().isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Agrega al menos un producto al carrito.");
            return;
        }

        if (tarjetaConsultada == null) {
            JOptionPane.showMessageDialog(vista,
                    "Primero consulta una tarjeta válida.");
            return;
        }

        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();

        java.time.format.DateTimeFormatter formato =
                java.time.format.DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm:ss");

        StringBuilder ticket = new StringBuilder();

        ticket.append("========================================\n");
        ticket.append("Fecha: ")
                .append(ahora.format(formato)).append("\n");
        ticket.append("Folio: NV-")
                .append(ahora.format(
                        java.time.format.DateTimeFormatter.ofPattern(
                                "yyyyMMddHHmmss")))
                .append("\n");
        ticket.append("Estado: PAGADO\n");
        ticket.append("----------------------------------------\n");
        ticket.append("DATOS DE PAGO\n");
        ticket.append("Banco: ")
                .append(tarjetaConsultada.getBanco()).append("\n");
        ticket.append("Ciudad: ")
                .append(tarjetaConsultada.getCiudad()).append("\n");
        ticket.append("Tarjeta: ")
                .append(tarjetaConsultada.getNumero()).append("\n");
        ticket.append("----------------------------------------\n");
        ticket.append(String.format("%-17s %3s %10s\n",
                "PRODUCTO", "CANT", "IMPORTE"));
        ticket.append("----------------------------------------\n");

        for (int i = 0; i < compra.getProductos().size(); i++) {
            Producto producto = compra.getProductos().get(i);
            int cantidad = compra.getCantidades().get(i);
            double importe = producto.getPrecio() * cantidad;

            ticket.append(String.format("%-17s %3d $%9.2f\n",
                    producto.getNombre(), cantidad, importe));
        }

        ticket.append("----------------------------------------\n");
        ticket.append(String.format("TOTAL:                  $%9.2f\n",
                compra.calcularTotal()));
        ticket.append("========================================\n");
        ticket.append("       Gracias por elegir Arki Store\n");
        ticket.append("       Comprobante de pago simulado\n");

        vista.mostrarTicket(ticket.toString());

        pagoRealizado = true;
        vista.botonAgregar.setEnabled(false);
        vista.botonConsultar.setEnabled(false);
        vista.botonPagar.setEnabled(false);
    }

}