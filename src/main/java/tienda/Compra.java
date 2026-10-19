
package tienda;

import java.util.ArrayList;
import java.util.List;

public class Compra {

    private List<Producto> productos;
    private List<Integer> cantidades;
    private Tarjeta tarjeta;

    public Compra() {
        productos = new ArrayList<>();
        cantidades = new ArrayList<>();
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (producto != null && cantidad > 0) {
            productos.add(producto);
            cantidades.add(cantidad);
        }
    }

    public double calcularTotal() {
        double total = 0;

        for (int i = 0; i < productos.size(); i++) {
            total += productos.get(i).getPrecio() * cantidades.get(i);
        }

        return total;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Integer> getCantidades() {
        return cantidades;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }
    public void quitarProducto(int indice) {
        if (indice >= 0 && indice < productos.size()) {
            productos.remove(indice);
            cantidades.remove(indice);
        }
    }
}
