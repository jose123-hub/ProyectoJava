package UI;
import Modelos.Usuario;
import Servicios.CarritoServicio;
import Servicios.CompraServicio;
import Servicios.ProductoServicio;
import Servicios.UsuarioServicio;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {
    private JTextField txtNombre;
    private JPasswordField txtPassword;
    private final UsuarioServicio service;

    private static final Color NEON_BLUE = new Color(64, 192, 255);
    private static final Color DARK_BG = new Color(10, 10, 20);
    private static final Color FIELD_BG = new Color(25, 25, 40);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font NEON_TITLE_FONT = new Font("Consolas", Font.BOLD, 28);
    private static final Font NEON_BUTTON_FONT = new Font("Consolas", Font.BOLD, 16);

    public VentanaLogin(UsuarioServicio usuarioServicio) {
        this.service = usuarioServicio;
        inicializarUI();
    }

    public VentanaLogin() {
        this.service = new UsuarioServicio();
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("SISTEMA NEON TECH - LOGIN");
        setSize(450, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BG);
        add(panel);

        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(DARK_BG);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblIcono = new JLabel("⚡");
        lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblIcono.setForeground(NEON_BLUE);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAppTitle = new JLabel("NEON TECH");
        lblAppTitle.setFont(NEON_TITLE_FONT);
        lblAppTitle.setForeground(NEON_BLUE);
        lblAppTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Correcto

        panelHeader.add(lblIcono);
        panelHeader.add(lblAppTitle);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(DARK_BG);
        form.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JLabel lblTitulo = new JLabel("INICIAR SESIÓN");
        lblTitulo.setForeground(TEXT_COLOR);
        lblTitulo.setFont(NEON_BUTTON_FONT);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNombre = new JLabel("USUARIO");
        lblNombre.setForeground(TEXT_COLOR);
        lblNombre.setFont(new Font("Consolas", Font.PLAIN, 12));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtNombre = new JTextField();
        configurarCampo(txtNombre);

        JLabel lblPass = new JLabel("CONTRASEÑA");
        lblPass.setForeground(TEXT_COLOR);
        lblPass.setFont(new Font("Consolas", Font.PLAIN, 12));
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtPassword = new JPasswordField();
        configurarCampo(txtPassword);

        JPanel pnlBotones = new JPanel();
        pnlBotones.setLayout(new BoxLayout(pnlBotones, BoxLayout.Y_AXIS));
        pnlBotones.setBackground(DARK_BG);
        pnlBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnLogin = new JButton("INGRESAR");
        configurarBotonNeon(btnLogin, true);
        btnLogin.addActionListener(e -> login());

        JButton btnRegistrar = new JButton("REGISTRARSE");
        configurarBotonNeon(btnRegistrar, false);
        btnRegistrar.addActionListener(e -> {
            new VentanaRegistro(service).setVisible(true);
            dispose();
        });

        pnlBotones.add(btnLogin);
        pnlBotones.add(Box.createVerticalStrut(15));
        pnlBotones.add(btnRegistrar);

        form.add(Box.createVerticalStrut(10));
        form.add(lblTitulo);
        form.add(Box.createVerticalStrut(20));

        form.add(lblNombre);
        form.add(Box.createVerticalStrut(5)); // Pequeño espacio entre label y campo
        form.add(txtNombre);
        form.add(Box.createVerticalStrut(20));

        form.add(lblPass);
        form.add(Box.createVerticalStrut(5));
        form.add(txtPassword);
        form.add(Box.createVerticalStrut(30)); // Más espacio antes de los botones

        form.add(pnlBotones);

        panel.add(panelHeader, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        setVisible(true);
    }
    private void configurarCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campo.setBackground(FIELD_BG);
        campo.setForeground(NEON_BLUE);
        campo.setCaretColor(NEON_BLUE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NEON_BLUE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void configurarBotonNeon(JButton btn, boolean relleno) {
        btn.setFont(NEON_BUTTON_FONT);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        if (relleno) {
            btn.setBackground(NEON_BLUE);
            btn.setForeground(DARK_BG);
        } else {
            btn.setBackground(DARK_BG);
            btn.setForeground(NEON_BLUE);
            btn.setBorder(BorderFactory.createLineBorder(NEON_BLUE, 1));
        }
    }

    private void login() {
        String nombre = txtNombre.getText();
        String pass = new String(txtPassword.getPassword());

        Usuario u = service.login(nombre, pass);

        if (u == null) {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre(), "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);

        if ("Admin".equalsIgnoreCase(u.getRol())) {
             new VentanaAdmin(new ProductoServicio(), new CompraServicio()).setVisible(true);
        } else {
            new VentanaCliente(new ProductoServicio(), new CarritoServicio(), u).setVisible(true);
        }

        dispose();
    }
}