
package tienda;

public class Tarjeta {

    private String numero;
    private String banco;
    private String ciudad;

    public Tarjeta(String numero, String banco, String ciudad) {
        this.numero = numero;
        this.banco = banco;
        this.ciudad = ciudad;
    }

    public String getNumero() {
        return numero;
    }

    public String getBanco() {
        return banco;
    }

    public String getCiudad() {
        return ciudad;
    }
}
