package UI;
import Modelos.Usuario;
import Servicios.CarritoServicio;
import Servicios.CompraServicio;
import Servicios.ProductoServicio;
import Servicios.UsuarioServicio;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaCliente extends JFrame {
    private Usuario usuarioActual;
    private JPanel panelCentral;
    private CardLayout cardLayout;

    private ProductoServicio productoServicio;
    private CarritoServicio carritoServicio;
    private CompraServicio compraServicio;

    private static final Color NEON_BLUE = new Color(64, 192, 255);
    private static final Color DARK_BG = new Color(10, 10, 20);
    private static final Color SIDEBAR_BG = new Color(15, 15, 25);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font FONT_MENU = new Font("Consolas", Font.BOLD, 16);

    public VentanaCliente(ProductoServicio productoServicio, CarritoServicio carritoServicio, Usuario usuario) {
        this.usuarioActual = usuario;
        this.productoServicio = productoServicio;
        this.carritoServicio = carritoServicio;

        this.compraServicio = new CompraServicio();

        setTitle("NEON TECH - PANEL CLIENTE");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(crearSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.setBackground(DARK_BG);

        panelCentral.add(new VentanaProductosCliente(usuarioActual, productoServicio, carritoServicio), "Productos");

        panelCentral.add(new VentanaCarrito(usuarioActual, carritoServicio), "Carrito");

        panelCentral.add(new VentanaComprasCliente(usuarioActual, compraServicio), "Historial");

        add(panelCentral, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, NEON_BLUE));

        JPanel pnlPerfil = new JPanel();
        pnlPerfil.setBackground(SIDEBAR_BG);
        pnlPerfil.setLayout(new BoxLayout(pnlPerfil, BoxLayout.Y_AXIS));
        pnlPerfil.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        lblAvatar.setForeground(NEON_BLUE);
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel(usuarioActual.getNombre().toUpperCase());
        lblUser.setForeground(TEXT_COLOR);
        lblUser.setFont(FONT_MENU);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlPerfil.add(lblAvatar);
        pnlPerfil.add(Box.createVerticalStrut(10));
        pnlPerfil.add(lblUser);

        JButton btnProd = crearBotonMenu(" PRODUCTOS", "🛒");
        btnProd.addActionListener(e -> cardLayout.show(panelCentral, "Productos"));

        JButton btnCar = crearBotonMenu(" MI CARRITO", "🛍️");
        btnCar.addActionListener(e -> cardLayout.show(panelCentral, "Carrito"));

        JButton btnHist = crearBotonMenu(" HISTORIAL", "🧾");
        btnHist.addActionListener(e -> cardLayout.show(panelCentral, "Historial"));

        JButton btnSalir = crearBotonMenu(" SALIR", "🚪");
        btnSalir.setForeground(new Color(255, 80, 80));
        btnSalir.addActionListener(e -> {
            new VentanaLogin().setVisible(true);
            this.dispose();
        });
        sidebar.add(pnlPerfil);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(btnProd);
        sidebar.add(btnCar);
        sidebar.add(btnHist);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnSalir);
        sidebar.add(Box.createVerticalStrut(20));
        return sidebar;
    }

    private JButton crearBotonMenu(String texto, String icono) {
        JButton btn = new JButton(icono + texto);
        btn.setFont(FONT_MENU);
        btn.setForeground(Color.GRAY);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 0));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(25, 25, 40));
                btn.setForeground(NEON_BLUE);
                btn.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, NEON_BLUE));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(SIDEBAR_BG);
                btn.setForeground(Color.GRAY);
                btn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 0));
            }
        });
        return btn;
    }
}
