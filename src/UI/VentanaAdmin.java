package UI;
import Modelos.ProductoTecnologico;
import Servicios.CompraServicio;
import Servicios.ProductoServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaAdmin extends JFrame {

    private ProductoServicio productoServicio;
    private CompraServicio compraServicio;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    private static final Color NEON_BLUE = new Color(64, 192, 255);
    private static final Color DARK_BG = new Color(10, 10, 20);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font FONT_NEON = new Font("Consolas", Font.BOLD, 14);

    public VentanaAdmin(ProductoServicio ps, CompraServicio cs) {
        this.productoServicio = ps;
        this.compraServicio = cs;

        setTitle("NEON TECH - ADMIN");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK_BG);

        // --- Panel superior (título) ---
        JLabel lblTitulo = new JLabel("PANEL ADMINISTRADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Consolas", Font.BOLD, 28));
        lblTitulo.setForeground(NEON_BLUE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // --- Panel central (tabla productos) ---
        tablaProductos = new JTable();
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos.setModel(modeloTabla);
        tablaProductos.setBackground(new Color(20, 20, 35));
        tablaProductos.setForeground(TEXT_COLOR);
        tablaProductos.setSelectionBackground(NEON_BLUE);
        tablaProductos.setSelectionForeground(DARK_BG);
        tablaProductos.setFont(FONT_NEON);
        tablaProductos.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.getViewport().setBackground(DARK_BG);
        add(scroll, BorderLayout.CENTER);

        // --- Panel inferior (botones) ---
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(DARK_BG);
        JButton btnAgregar = crearBoton("AGREGAR PRODUCTO");
        JButton btnEditar = crearBoton("EDITAR PRODUCTO");
        JButton btnEliminar = crearBoton("ELIMINAR PRODUCTO");
        JButton btnSalir = crearBoton("SALIR");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.SOUTH);

        // --- Acciones botones ---
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnSalir.addActionListener(e -> {
            new VentanaLogin().setVisible(true);
            dispose();
        });

        cargarProductos();
        setVisible(true);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_NEON);
        btn.setForeground(DARK_BG);
        btn.setBackground(NEON_BLUE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<ProductoTecnologico> lista = productoServicio.listarProductos();
        for (ProductoTecnologico p : lista) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getCantidad()});
        }
    }

    private void agregarProducto() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del producto:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        String precioStr = JOptionPane.showInputDialog(this, "Precio del producto:");
        String stockStr = JOptionPane.showInputDialog(this, "Cantidad en stock:");

        try {
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            ProductoTecnologico p = new ProductoTecnologico();
            p.setNombre(nombre);
            p.setPrecio(precio);
            p.setCantidad(stock);
            boolean exito = productoServicio.agregarProducto(nombre, precio, stock);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar producto");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos");
        }
    }

    private void editarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para editar");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", modeloTabla.getValueAt(fila, 1));
        String precioStr = JOptionPane.showInputDialog(this, "Nuevo precio:", modeloTabla.getValueAt(fila, 2));
        String stockStr = JOptionPane.showInputDialog(this, "Nueva cantidad:", modeloTabla.getValueAt(fila, 3));

        try {
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            ProductoTecnologico p = new ProductoTecnologico(id, nombre, precio, stock);
            boolean exito = productoServicio.actualizarProducto(id, nombre, precio, stock);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto actualizado");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos");
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = productoServicio.eliminarProducto(id);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto eliminado");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar");
            }
        }
    }
}