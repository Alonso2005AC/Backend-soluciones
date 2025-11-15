package mass.backendmass.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class VentaRequest {
    @JsonProperty("id_cliente")
    private int id_cliente;
    
    @JsonProperty("metodo_pago")
    private String metodo_pago;
    
    @JsonProperty("total")
    private double total;
    
    @JsonProperty("tipo_comprobante")
    private String tipo_comprobante;
    
    @JsonProperty("datos_fiscales")
    private String datos_fiscales;
    
    @JsonProperty("detalles")
    private List<DetalleVentaDTO> detalles;

    // Constructor vacío
    public VentaRequest() {}

    // Getters y Setters
    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        // Solo limpiar espacios, la validación se hace en el Service
        this.metodo_pago = metodo_pago != null ? metodo_pago.trim() : null;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTipo_comprobante() {
        return tipo_comprobante;
    }

    public void setTipo_comprobante(String tipo_comprobante) {
        this.tipo_comprobante = tipo_comprobante;
    }

    public String getDatos_fiscales() {
        return datos_fiscales;
    }

    public void setDatos_fiscales(String datos_fiscales) {
        this.datos_fiscales = datos_fiscales;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaDTO> detalles) {
        this.detalles = detalles;
    }
}
