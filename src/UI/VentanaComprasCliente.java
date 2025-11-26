package UI;
import Modelos.Compra;
import Modelos.DetalleCompra;
import Modelos.Usuario;
import Servicios.CompraServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaComprasCliente extends JPanel {

    private JTable tablaCompras;
    private DefaultTableModel modeloTabla;
    private CompraServicio compraServicio;
    private Usuario usuario;

    private static final Color NEON_BLUE = new Color(64, 192, 255);
    private static final Color DARK_BG = new Color(10, 10, 20);
    private static final Color TEXT_COLOR = Color.WHITE;

    public VentanaComprasCliente(Usuario usuario, CompraServicio compraServicio) {
        this.usuario = usuario;
        this.compraServicio = compraServicio;

        setLayout(new BorderLayout());
        setBackground(DARK_BG);

        inicializarComponentes();
        cargarCompras();
    }

    private void inicializarComponentes() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID Compra", "Fecha", "Producto", "Cantidad", "Precio Unitario", "Subtotal"}, 0
        ) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaCompras = new JTable(modeloTabla);
        tablaCompras.setBackground(new Color(20, 20, 35));
        tablaCompras.setForeground(TEXT_COLOR);
        tablaCompras.setSelectionBackground(NEON_BLUE);
        tablaCompras.setSelectionForeground(DARK_BG);
        tablaCompras.setRowHeight(25);
        tablaCompras.setFont(new Font("Consolas", Font.PLAIN, 14));

        add(new JScrollPane(tablaCompras), BorderLayout.CENTER);
    }

    private void cargarCompras() {
        modeloTabla.setRowCount(0);
        List<Compra> compras = compraServicio.listarComprasPorUsuario(usuario.getId());

        for (Compra c : compras) {
            for (DetalleCompra d : c.getDetalles()) {
                modeloTabla.addRow(new Object[]{
                        c.getId(),
                        c.getFecha(),
                        d.getNombreProducto(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getCantidad() * d.getPrecioUnitario()
                });
            }
        }
    }
    public void actualizar() {
        cargarCompras();
    }
}
