package UI;
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

    private ProductoServicio productoServicio;
    private CarritoServicio carritoServicio;
    private Usuario usuario;

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

        cargarProductos();
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

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock"}, 0) {
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

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaProductos.getSelectedRow() != -1) {
                mostrarDetalle(tablaProductos.getSelectedRow());
            }
        });

        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.getViewport().setBackground(DARK_BG);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
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

        JButton btnAgregar = new JButton("AGREGAR AL CARRITO");
        btnAgregar.setBackground(NEON_BLUE);
        btnAgregar.setForeground(DARK_BG);
        btnAgregar.setFont(new Font("Consolas", Font.BOLD, 16));
        btnAgregar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.setOpaque(true);

        btnAgregar.addActionListener(e -> agregarAlCarrito());

        panel.add(lblImagenProducto);
        panel.add(Box.createVerticalStrut(20));
        panel.add(txtDescripcion);
        panel.add(Box.createVerticalGlue());
        panel.add(btnAgregar);

        return panel;
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<ProductoTecnologico> lista = productoServicio.listarProductos(); // Trae todos los productos
        for (ProductoTecnologico p : lista) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getCantidad()});
        }
    }

    private void mostrarDetalle(int row) {
        String nombre = (String) modeloTabla.getValueAt(row, 1);
        double precio = (double) modeloTabla.getValueAt(row, 2);
        int stock = (int) modeloTabla.getValueAt(row, 3);

        lblImagenProducto.setText("");
        lblImagenProducto.setIcon(null);
        lblImagenProducto.setBorder(BorderFactory.createLineBorder(NEON_BLUE, 2));
        lblImagenProducto.setText("FOTO: " + nombre);

        txtDescripcion.setText("DETALLES DEL PRODUCTO:\n\n" +
                "Producto: " + nombre + "\n" +
                "Precio: $" + precio + "\n" +
                "Stock Disponible: " + stock + " unidades.\n" +
                "Estado: Nuevo");
    }

    private void agregarAlCarrito() {
        int row = tablaProductos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto primero.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(row, 0);
        double precio = (double) modeloTabla.getValueAt(row, 2);

        String input = JOptionPane.showInputDialog(this, "Cantidad a comprar:", "1");
        if (input != null) {
            try {
                int cant = Integer.parseInt(input);
                carritoServicio.agregarProducto(id, cant, precio);
                JOptionPane.showMessageDialog(this, "Producto agregado al carrito.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
        }
    }
}