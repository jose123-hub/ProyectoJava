package UI;
import Modelos.DetalleCompra;
import Modelos.Usuario;
import Servicios.CarritoServicio;
import Servicios.CompraServicio;
import Servicios.ProductoServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaCarrito extends JPanel {
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private JButton btnEliminar;
    private JButton btnComprar;

    private Usuario usuario;
    private CarritoServicio carritoServicio;
    private CompraServicio compraServicio;
    private ProductoServicio productoServicio;

    private static final Color NEON_BLUE = new Color(64, 192, 255);
    private static final Color DARK_BG = new Color(10, 10, 20);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font FONT_TABLE = new Font("Consolas", Font.PLAIN, 14);
    private static final Font FONT_BOLD = new Font("Consolas", Font.BOLD, 16);

    public VentanaCarrito(Usuario usuario, CarritoServicio carritoServicio) {
        this.usuario = usuario;
        this.carritoServicio = carritoServicio;
        this.compraServicio = new CompraServicio();
        this.productoServicio = new ProductoServicio();

        setLayout(new BorderLayout());
        setBackground(DARK_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inicializarComponentes();

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                cargarTabla();
                actualizarTotal();
            }
        });
    }

    private void inicializarComponentes() {

        JLabel lblTitulo = new JLabel("MI CARRITO DE COMPRAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Consolas", Font.BOLD, 24));
        lblTitulo.setForeground(NEON_BLUE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Producto", "Cant.", "Precio Unit.", "Subtotal"}, 0
        ) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tablaCarrito = new JTable(modeloTabla);

        tablaCarrito.setBackground(new Color(20, 20, 35));
        tablaCarrito.setForeground(TEXT_COLOR);
        tablaCarrito.setSelectionBackground(NEON_BLUE);
        tablaCarrito.setSelectionForeground(DARK_BG);
        tablaCarrito.setFont(FONT_TABLE);
        tablaCarrito.setRowHeight(30);
        tablaCarrito.setShowVerticalLines(false);
        tablaCarrito.setGridColor(new Color(50, 50, 80));

        tablaCarrito.getTableHeader().setBackground(new Color(30, 30, 50));
        tablaCarrito.getTableHeader().setForeground(NEON_BLUE);
        tablaCarrito.getTableHeader().setFont(FONT_BOLD);
        tablaCarrito.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, NEON_BLUE));

        JScrollPane scroll = new JScrollPane(tablaCarrito);
        scroll.getViewport().setBackground(DARK_BG);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 60)));

        add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(DARK_BG);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        lblTotal = new JLabel("TOTAL: S/. 0.00");
        lblTotal.setFont(new Font("Consolas", Font.BOLD, 22));
        lblTotal.setForeground(Color.GREEN);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.setBackground(DARK_BG);

        btnEliminar = crearBoton("ELIMINAR ITEM", false);
        btnComprar = crearBoton("FINALIZAR COMPRA", true);

        pnlBotones.add(btnEliminar);
        pnlBotones.add(Box.createHorizontalStrut(10));
        pnlBotones.add(btnComprar);

        panelInferior.add(lblTotal, BorderLayout.WEST);
        panelInferior.add(pnlBotones, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

        btnEliminar.addActionListener(e -> eliminarItem());
        btnComprar.addActionListener(e -> realizarCompra());
    }

    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<DetalleCompra> carrito = carritoServicio.getCarrito();

        for (DetalleCompra d : carrito) {
            String nombreProducto = "Producto ID " + d.getIdProducto();
            double subtotal = d.getCantidad() * d.getPrecioUnitario();
            modeloTabla.addRow(new Object[]{
                    d.getIdProducto(),
                    nombreProducto,
                    d.getCantidad(),
                    String.format("S/. %.2f", d.getPrecioUnitario()),
                    String.format("S/. %.2f", subtotal)
            });
        }
        actualizarTotal();
    }

    private void eliminarItem() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar");
            return;
        }
        int idProducto = (int) modeloTabla.getValueAt(fila, 0);
        carritoServicio.eliminarProducto(idProducto);
        cargarTabla();
    }

    private void actualizarTotal() {
        List<DetalleCompra> carrito = carritoServicio.getCarrito();
        double total = carrito.stream().mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario()).sum();
        lblTotal.setText("TOTAL: S/. " + String.format("%.2f", total));
    }

    private void realizarCompra() {
        if (carritoServicio.getCarrito().isEmpty()) return;
        StringBuilder ticket = new StringBuilder();
        ticket.append("--------------------------------\n");
        ticket.append("      NEON TECH STORE      \n");
        ticket.append("--------------------------------\n\n");
        ticket.append("Cliente: ").append(usuario.getNombre()).append("\n");
        ticket.append("Fecha: ").append(java.time.LocalDate.now()).append("\n\n");
        ticket.append("PRODUCTOS:\n");
        double total = 0;
        for (DetalleCompra d : carritoServicio.getCarrito()) {
            double sub = d.getCantidad() * d.getPrecioUnitario();
            total += sub;
            ticket.append(String.format("- Prod ID %d (x%d) ..... $%.2f\n", d.getIdProducto(), d.getCantidad(), sub));
            new DAO.ProductoTecDAOImple().restarStock(d.getIdProducto(), d.getCantidad());
        }
        ticket.append("\n--------------------------------\n");
        ticket.append(String.format("TOTAL PAGADO: $%.2f\n", total));
        ticket.append("--------------------------------\n");
        ticket.append("¡Gracias por su compra!");
        boolean exito = compraServicio.procesarCompra(usuario.getId(), carritoServicio.getCarrito());
        if (exito) {
            JTextArea area = new JTextArea(ticket.toString());
            area.setBackground(new Color(10,10,20));
            area.setForeground(Color.CYAN);
            area.setFont(new Font("Monospaced", Font.BOLD, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "TICKET DE COMPRA", JOptionPane.PLAIN_MESSAGE);
            carritoServicio.limpiarCarrito();
            cargarTabla();
        }
    }

    private JButton crearBoton(String texto, boolean relleno) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Consolas", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        if (relleno) {
            btn.setBackground(NEON_BLUE);
            btn.setForeground(DARK_BG);
        } else {
            btn.setBackground(DARK_BG);
            btn.setForeground(NEON_BLUE);
            btn.setBorder(BorderFactory.createLineBorder(NEON_BLUE));
        }
        return btn;
    }
}