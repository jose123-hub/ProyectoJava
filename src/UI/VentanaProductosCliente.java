package UI;
import Modelos.DetalleCompra;
import Modelos.ProductoTecnologico;
import Modelos.Usuario;
import Servicios.CarritoServicio;
import Servicios.ProductoServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaProductosCliente extends JPanel {
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JLabel lblImagenProducto;
    private JTextArea txtDescripcion;
    private JButton btnAgregar;
    private ProductoServicio productoServicio;
    private CarritoServicio carritoServicio;
    private Usuario usuario;
    private ProductoTecnologico productoSeleccionado;
    private static final Color NEON_BLUE = new Color(64, 192, 255);
    private static final Color DARK_BG = new Color(10, 10, 20);
    private static final Color TEXT_COLOR = Color.WHITE;

    public VentanaProductosCliente(Usuario usuario, ProductoServicio productoServicio, CarritoServicio carritoServicio) {
        this.usuario = usuario;
        this.productoServicio = productoServicio;
        this.carritoServicio = carritoServicio;

        setLayout(new BorderLayout());
        setBackground(DARK_BG);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearPanelLista(), crearPanelDetalle());
        splitPane.setDividerLocation(600);
        splitPane.setBackground(DARK_BG);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);
        filtrarProductos("");
    }

    private JPanel crearPanelLista() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(NEON_BLUE),
                "CATÁLOGO",
                0, 0,
                new Font("Consolas", Font.BOLD, 14),
                NEON_BLUE
        ));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.setBackground(DARK_BG);
        pnlBusqueda.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));

        JLabel lblBuscar = new JLabel("BUSCAR:");
        lblBuscar.setForeground(TEXT_COLOR);
        lblBuscar.setFont(new Font("Consolas", Font.BOLD, 12));

        JTextField txtBuscar = new JTextField(15);
        txtBuscar.setBackground(new Color(25, 25, 40));
        txtBuscar.setForeground(NEON_BLUE);
        txtBuscar.setCaretColor(NEON_BLUE);
        txtBuscar.setBorder(BorderFactory.createLineBorder(NEON_BLUE));

        JButton btnBuscar = new JButton("🔍");
        btnBuscar.setBackground(NEON_BLUE);
        btnBuscar.setForeground(DARK_BG);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        btnBuscar.addActionListener(e -> filtrarProductos(txtBuscar.getText().trim()));

        pnlBusqueda.add(lblBuscar);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Desc %"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setBackground(new Color(20, 20, 35));
        tablaProductos.setForeground(TEXT_COLOR);
        tablaProductos.setSelectionBackground(NEON_BLUE);
        tablaProductos.setSelectionForeground(DARK_BG);
        tablaProductos.setRowHeight(25);
        tablaProductos.setFont(new Font("Consolas", Font.PLAIN, 14));
        tablaProductos.setGridColor(new Color(50, 50, 80));
        tablaProductos.setShowVerticalLines(false);

        tablaProductos.getTableHeader().setBackground(new Color(30, 30, 50));
        tablaProductos.getTableHeader().setForeground(NEON_BLUE);
        tablaProductos.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 14));
        tablaProductos.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, NEON_BLUE));

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaProductos.getSelectedRow() != -1) {
                mostrarDetalle(tablaProductos.getSelectedRow());
            }
        });
        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.getViewport().setBackground(DARK_BG);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 60)));
        panel.add(pnlBusqueda, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void filtrarProductos(String texto) {
        modeloTabla.setRowCount(0);
        List<ProductoTecnologico> lista;

        if (texto.isEmpty()) {
            lista = productoServicio.listarProductos();
        } else {
            lista = productoServicio.buscarPorNombre(texto);
        }
        for (ProductoTecnologico p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad(),
                    p.getDescuento()
            });
        }
    }

    private JPanel crearPanelDetalle() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblImagenProducto = new JLabel("🖱️ SELECCIONA UN PRODUCTO", SwingConstants.CENTER);
        lblImagenProducto.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblImagenProducto.setForeground(Color.GRAY);
        lblImagenProducto.setPreferredSize(new Dimension(300, 300));
        lblImagenProducto.setMaximumSize(new Dimension(400, 300));
        lblImagenProducto.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        lblImagenProducto.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtDescripcion = new JTextArea(5, 20);
        txtDescripcion.setBackground(DARK_BG);
        txtDescripcion.setForeground(NEON_BLUE);
        txtDescripcion.setFont(new Font("Consolas", Font.PLAIN, 16));
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        btnAgregar = new JButton("AGREGAR AL CARRITO");
        btnAgregar.setBackground(NEON_BLUE);
        btnAgregar.setForeground(DARK_BG);
        btnAgregar.setFont(new Font("Consolas", Font.BOLD, 16));
        btnAgregar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.setOpaque(true);
        btnAgregar.setEnabled(false);
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        panel.add(lblImagenProducto);
        panel.add(Box.createVerticalStrut(20));
        panel.add(txtDescripcion);
        panel.add(Box.createVerticalGlue());
        panel.add(btnAgregar);
        return panel;
    }

    private void mostrarDetalle(int row) {
        int id = Integer.parseInt(String.valueOf(modeloTabla.getValueAt(row, 0)));
        String nombre = String.valueOf(modeloTabla.getValueAt(row, 1));
        double precio = Double.parseDouble(String.valueOf(modeloTabla.getValueAt(row, 2)));
        int stock = Integer.parseInt(String.valueOf(modeloTabla.getValueAt(row, 3)));
        int descuento = 0;
        try {
            descuento = Integer.parseInt(String.valueOf(modeloTabla.getValueAt(row, 4)));
        } catch (Exception e) {
            descuento = 0;
        }
        String rutaImagen = "sin_imagen";
        try {
            ProductoTecnologico prodFull = productoServicio.listarProductos().stream()
                    .filter(p -> p.getId() == id).findFirst().orElse(null);
            if (prodFull != null) {
                rutaImagen = prodFull.getImagen();
            }
        } catch (Exception e) { }

        productoSeleccionado = new ProductoTecnologico(id, nombre, precio, stock, descuento, rutaImagen);
        double precioFinal = productoSeleccionado.getPrecioFinal();
        ImageIcon icono = null;
        try {
            if (rutaImagen != null && !rutaImagen.equals("sin_imagen") && !rutaImagen.isEmpty()) {
                icono = new ImageIcon(rutaImagen);
                if (icono.getImageLoadStatus() != MediaTracker.COMPLETE) {
                    icono = null;
                }
            }
        } catch (Exception e) {
            icono = null;
        }

        if (icono != null) {
            Image img = icono.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            lblImagenProducto.setIcon(new ImageIcon(img));
            lblImagenProducto.setText("");
            lblImagenProducto.setFont(null);
        } else {
            lblImagenProducto.setIcon(null);
            lblImagenProducto.setText("📦");
            lblImagenProducto.setFont(new Font("Segoe UI", Font.PLAIN, 100));
            lblImagenProducto.setForeground(Color.GRAY);
        }
        lblImagenProducto.setBorder(BorderFactory.createLineBorder(NEON_BLUE, 2));
        StringBuilder sb = new StringBuilder();
        sb.append("DETALLES DEL PRODUCTO:\n\n");
        sb.append("Producto: ").append(nombre).append("\n");

        if (descuento > 0) {
            sb.append("Precio Anterior: $").append(precio).append("\n");
            sb.append("DESCUENTO:       -").append(descuento).append("% OFF 🔥\n");
            sb.append("PRECIO FINAL:    $").append(String.format("%.2f", precioFinal)).append("\n");
        } else {
            sb.append("Precio:          $").append(precio).append("\n");
        }
        sb.append("Stock Disponible: ").append(stock).append(" unidades.\n");
        sb.append("Estado: Nuevo");
        txtDescripcion.setText(sb.toString());
        if (btnAgregar != null) btnAgregar.setEnabled(stock > 0);
    }

    private void agregarAlCarrito() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un producto primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String input = JOptionPane.showInputDialog(this,
                "Producto: " + productoSeleccionado.getNombre() + "\n" +
                        "Stock disponible: " + productoSeleccionado.getCantidad() + "\n\n" +
                        "¿Cuántos deseas llevar?", "1");

        if (input != null) {
            try {
                int cantidad = Integer.parseInt(input);
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ La cantidad debe ser mayor a 0.");
                    return;
                }
                if (cantidad > productoSeleccionado.getCantidad()) {
                    JOptionPane.showMessageDialog(this,
                            "⚠️ Stock insuficiente. Solo quedan " + productoSeleccionado.getCantidad() + " unidades.",
                            "Error de Stock", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                DetalleCompra detalle = new DetalleCompra();
                detalle.setIdProducto(productoSeleccionado.getId());
                detalle.setNombreProducto(productoSeleccionado.getNombre());
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(productoSeleccionado.getPrecioFinal());
                carritoServicio.agregarProducto(detalle);
                JOptionPane.showMessageDialog(this, "✅ Agregado al carrito correctamente.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Error: Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}