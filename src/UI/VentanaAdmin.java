package UI;
import DAO.CompraDAOImplementacion;
import DAO.UsuarioDAOImplementacion;
import Modelos.Compra;
import Modelos.ProductoTecnologico;
import Modelos.Usuario;
import Servicios.CompraServicio;
import Servicios.ProductoServicio;
import Servicios.UsuarioServicio;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaAdmin extends JFrame {
    private ProductoServicio productoServicio;
    private CompraServicio compraServicio;
    private Usuario usuarioActual;

    private JTabbedPane tabbedPane;
    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;

    private static final Color DARK_BG = new Color(10, 10, 20);
    private static final Color PANEL_BG = new Color(20, 20, 35);
    private static final Color NEON_BLUE = new Color(64, 192, 255);
    private static final Color NEON_RED = new Color(255, 50, 80);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Font FONT_NEON = new Font("Consolas", Font.BOLD, 14);
    public VentanaAdmin(ProductoServicio ps, CompraServicio cs, Usuario usuario) {
        this.productoServicio = ps;
        this.compraServicio = cs;
        this.usuarioActual = usuario;
        inicializarUI();
    }
    public VentanaAdmin(ProductoServicio ps, CompraServicio cs) {
        this(ps, cs, new Usuario(0, "ADMIN", "Admin", ""));
    }

    private void inicializarUI() {
        setTitle("NEON TECH - PANEL DE CONTROL [ADMIN]");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_BG);
        setContentPane(mainPanel);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BG);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("MODO ADMINISTRADOR");
        lblTitulo.setFont(new Font("Consolas", Font.BOLD, 28));
        lblTitulo.setForeground(NEON_BLUE);

        JButton btnSalir = new JButton("CERRAR SESIÓN");
        btnSalir.setBackground(NEON_RED);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Consolas", Font.BOLD, 12));
        btnSalir.setFocusPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setOpaque(true);
        btnSalir.setBorder(BorderFactory.createLineBorder(NEON_RED));

        btnSalir.addActionListener(e -> {
            new VentanaLogin(new UsuarioServicio()).setVisible(true);
            this.dispose();
        });

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnSalir, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        tabbedPane.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = DARK_BG;
                lightHighlight = DARK_BG;
                shadow = DARK_BG;
                darkShadow = DARK_BG;
                focus = DARK_BG;
            }
        });
        tabbedPane.setBackground(new Color(40, 40, 60));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(FONT_NEON);
        tabbedPane.setOpaque(true);
        tabbedPane.addTab("📦 GESTIÓN INVENTARIO", crearPanelProductos());
        tabbedPane.addTab("👥 USUARIOS Y VENTAS", new PanelUsuarioAdmin());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        cargarProductos();
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modeloProductos = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Desc %"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaProductos = new JTable(modeloProductos);
        estilizarTablaNeon(tablaProductos, NEON_BLUE);

        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.getViewport().setBackground(DARK_BG);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBotones.setBackground(DARK_BG);

        JButton btnAgregar = crearBoton("AGREGAR PRODUCTO");
        JButton btnEditar = crearBoton("EDITAR PRODUCTO");
        JButton btnEliminar = crearBoton("ELIMINAR PRODUCTO");

        pnlBotones.add(btnAgregar);
        pnlBotones.add(btnEditar);
        pnlBotones.add(btnEliminar);

        panel.add(pnlBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        return panel;
    }

    // --- MÉTODOS VISUALES ---
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_NEON);
        btn.setForeground(DARK_BG);
        btn.setBackground(NEON_BLUE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    private void estilizarTablaNeon(JTable tabla, Color colorNeon) {
        tabla.setBackground(PANEL_BG);
        tabla.setForeground(TEXT_WHITE);
        tabla.setSelectionBackground(colorNeon);
        tabla.setSelectionForeground(DARK_BG);
        tabla.setFont(new Font("Consolas", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new Color(50, 50, 70));
        tabla.getTableHeader().setBackground(new Color(30, 30, 50));
        tabla.getTableHeader().setForeground(colorNeon);
        tabla.getTableHeader().setFont(FONT_NEON);
        tabla.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, colorNeon));
    }

    // --- LÓGICA CRUD ---
    private void cargarProductos() {
        modeloProductos.setRowCount(0);
        List<ProductoTecnologico> lista = productoServicio.listarProductos();
        for (ProductoTecnologico p : lista) {
            modeloProductos.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getCantidad(), p.getDescuento()});
        }
    }

    private void agregarProducto() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();
        JTextField txtDesc = new JTextField("0");
        JTextField txtRutaImagen = new JTextField();
        txtRutaImagen.setEditable(false);
        txtRutaImagen.setForeground(Color.BLUE);
        JButton btnImagen = new JButton("Seleccionar Foto 📂");

        btnImagen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtRutaImagen.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        panel.add(new JLabel("Nombre del Producto (*):")); panel.add(txtNombre);
        panel.add(new JLabel("Precio Unitario (*):"));     panel.add(txtPrecio);
        panel.add(new JLabel("Stock Inicial (*):"));       panel.add(txtStock);
        panel.add(new JLabel("Descuento (%):"));           panel.add(txtDesc);
        panel.add(btnImagen);                              panel.add(txtRutaImagen);
        int resultado = JOptionPane.showConfirmDialog(this, panel, "Nuevo Producto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {

            String nombre = txtNombre.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            String stockStr = txtStock.getText().trim();
            String descStr = txtDesc.getText().trim();
            String rutaImagen = txtRutaImagen.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ El nombre es obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (precioStr.isEmpty() || stockStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Precio y Stock son obligatorios.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr);
                int stock = Integer.parseInt(stockStr);
                int descuento = descStr.isEmpty() ? 0 : Integer.parseInt(descStr);
                if (precio <= 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ El precio debe ser mayor a 0.", "Dato Inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (stock < 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ El stock no puede ser negativo.", "Dato Inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (descuento < 0 || descuento > 100) {
                    JOptionPane.showMessageDialog(this, "⚠️ El descuento debe ser entre 0 y 100.", "Dato Inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (rutaImagen.isEmpty()) {
                    rutaImagen = "sin_imagen";
                }
                if (productoServicio.agregarProducto(nombre, precio, stock, descuento, rutaImagen)) {
                    JOptionPane.showMessageDialog(this, "✅ Producto agregado exitosamente.");
                    cargarProductos();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error crítico al guardar en la Base de Datos.", "Error BD", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Error de formato: Precio, Stock y Descuento deben ser números válidos.", "Error de Tipo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un producto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(String.valueOf(modeloProductos.getValueAt(fila, 0)));
        String nombreActual = String.valueOf(modeloProductos.getValueAt(fila, 1));
        double precioActual = Double.parseDouble(String.valueOf(modeloProductos.getValueAt(fila, 2)));
        int stockActual = Integer.parseInt(String.valueOf(modeloProductos.getValueAt(fila, 3)));
        int descActual = Integer.parseInt(String.valueOf(modeloProductos.getValueAt(fila, 4)));
        String rutaActual = "sin_imagen";
        try {
            ProductoTecnologico prodActual = productoServicio.listarProductos().stream()
                    .filter(p -> p.getId() == id).findFirst().orElse(null);
            if (prodActual != null) {
                rutaActual = prodActual.getImagen();
            }
        } catch (Exception e) { }

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField txtNombre = new JTextField(nombreActual);
        JTextField txtPrecio = new JTextField(String.valueOf(precioActual));
        JTextField txtStock = new JTextField(String.valueOf(stockActual));
        JTextField txtDesc = new JTextField(String.valueOf(descActual));
        JTextField txtRutaImagen = new JTextField(rutaActual);
        txtRutaImagen.setEditable(false);
        txtRutaImagen.setForeground(Color.BLUE);

        JButton btnImagen = new JButton("Cambiar Foto 📂");

        btnImagen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtRutaImagen.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        panel.add(new JLabel("Nombre (*):"));    panel.add(txtNombre);
        panel.add(new JLabel("Precio (*):"));    panel.add(txtPrecio);
        panel.add(new JLabel("Stock (*):"));     panel.add(txtStock);
        panel.add(new JLabel("Descuento (%):")); panel.add(txtDesc);
        panel.add(btnImagen);                    panel.add(txtRutaImagen);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Editar Producto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            String stockStr = txtStock.getText().trim();
            String descStr = txtDesc.getText().trim();
            String nuevaRuta = txtRutaImagen.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ El nombre no puede quedar vacío.");
                return;
            }
            if (precioStr.isEmpty() || stockStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Precio y Stock obligatorios.");
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr);
                int stock = Integer.parseInt(stockStr);
                int descuento = descStr.isEmpty() ? 0 : Integer.parseInt(descStr);

                if (precio <= 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ Precio inválido (debe ser > 0).");
                    return;
                }
                if (stock < 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ Stock inválido (no puede ser negativo).");
                    return;
                }
                if (descuento < 0 || descuento > 100) {
                    JOptionPane.showMessageDialog(this, "⚠️ Descuento inválido (0-100).");
                    return;
                }
                if (nuevaRuta.isEmpty()) nuevaRuta = "sin_imagen";

                if (productoServicio.actualizarProducto(id, nombre, precio, stock, descuento, nuevaRuta)) {
                    JOptionPane.showMessageDialog(this, "✅ Producto actualizado correctamente.");
                    cargarProductos();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al actualizar en BD.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Error: Ingrese solo números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }
        int id = (int) modeloProductos.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar producto?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (productoServicio.eliminarProducto(id)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado.");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar.");
            }
        }
    }
}