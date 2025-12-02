package UI;
import Servicios.UsuarioServicio;
import javax.swing.*;
import java.awt.*;

public class VentanaRegistro extends JFrame {
    private JTextField txtNombre;
    private JPasswordField txtPassword;
    private JButton btnTogglePassword;
    private boolean passwordVisible = false;

    private UsuarioServicio service;

    private static final Color NEON_BLUE = new Color(64, 192, 255);
    private static final Color DARK_BG = new Color(10, 10, 20);
    private static final Color FIELD_BG = new Color(25, 25, 40);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font NEON_TITLE_FONT = new Font("Consolas", Font.BOLD, 28);
    private static final Font NEON_BUTTON_FONT = new Font("Consolas", Font.BOLD, 16);

    public VentanaRegistro(UsuarioServicio service) {
        this.service = service;
        inicializarUI();
    }

    public VentanaRegistro() {
        this.service = new UsuarioServicio();
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("SISTEMA NEON TECH - REGISTRO");
        setSize(450, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BG);
        add(panel);

        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(DARK_BG);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JLabel lblIcono = new JLabel("📝");
        lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 60));
        lblIcono.setForeground(NEON_BLUE);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("CREAR CUENTA");
        lblTitulo.setFont(NEON_TITLE_FONT);
        lblTitulo.setForeground(NEON_BLUE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblIcono);
        panelHeader.add(lblTitulo);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(DARK_BG);
        form.setBorder(BorderFactory.createEmptyBorder(10, 50, 40, 50));

        JLabel lblSubtitulo = new JLabel("INGRESE SUS DATOS");
        lblSubtitulo.setForeground(TEXT_COLOR);
        lblSubtitulo.setFont(new Font("Consolas", Font.PLAIN, 14));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNombre = new JLabel("NUEVO USUARIO");
        lblNombre.setForeground(TEXT_COLOR);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtNombre = new JTextField();
        configurarCampo(txtNombre);
        txtNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblContra = new JLabel("CONTRASEÑA");
        lblContra.setForeground(TEXT_COLOR);
        lblContra.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pnlPassContainer = new JPanel(new BorderLayout());
        pnlPassContainer.setBackground(DARK_BG);
        pnlPassContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pnlPassContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setEchoChar('•');
        txtPassword.setBackground(FIELD_BG);
        txtPassword.setForeground(NEON_BLUE);
        txtPassword.setCaretColor(NEON_BLUE);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 0, NEON_BLUE),
                BorderFactory.createEmptyBorder(5, 10, 5, 5)
        ));

        btnTogglePassword = new JButton("👁");
        btnTogglePassword.setFocusPainted(false);
        btnTogglePassword.setBackground(FIELD_BG);
        btnTogglePassword.setForeground(NEON_BLUE);
        btnTogglePassword.setPreferredSize(new Dimension(50, 40));
        btnTogglePassword.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        btnTogglePassword.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, NEON_BLUE));
        btnTogglePassword.setContentAreaFilled(true);

        btnTogglePassword.addActionListener(e -> togglePassword());

        pnlPassContainer.add(txtPassword, BorderLayout.CENTER);
        pnlPassContainer.add(btnTogglePassword, BorderLayout.EAST);

        JButton btnRegistrar = new JButton("REGISTRAR");
        configurarBotonNeon(btnRegistrar, true);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado
        btnRegistrar.addActionListener(e -> registrar());

        JButton btnVolver = new JButton("VOLVER AL LOGIN");
        configurarBotonNeon(btnVolver, false);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado
        btnVolver.addActionListener(e -> {
            new VentanaLogin(this.service).setVisible(true);
            this.dispose();
        });

        form.add(lblSubtitulo);
        form.add(Box.createVerticalStrut(20));

        form.add(lblNombre);
        form.add(Box.createVerticalStrut(5));
        form.add(txtNombre);

        form.add(Box.createVerticalStrut(20));

        form.add(lblContra);
        form.add(Box.createVerticalStrut(5));
        form.add(pnlPassContainer);

        form.add(Box.createVerticalStrut(40));

        form.add(btnRegistrar);
        form.add(Box.createVerticalStrut(15));
        form.add(btnVolver);

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
    }

    private void configurarBotonNeon(JButton btn, boolean relleno) {
        btn.setFont(NEON_BUTTON_FONT);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

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

    private void togglePassword() {
        passwordVisible = !passwordVisible;
        txtPassword.setEchoChar(passwordVisible ? (char) 0 : '•');
        btnTogglePassword.setText(passwordVisible ? "🙈" : "👁");
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String contrasena = new String(txtPassword.getPassword()).trim();
        if (nombre.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String patronSeguro = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$";
        if (!contrasena.matches(patronSeguro)) {
            JOptionPane.showMessageDialog(this,
                    "<html>⚠️ <b>Contraseña Insegura</b><br><br>" +
                            "Para su seguridad, la contraseña debe tener:<br>" +
                            "• Mínimo <b>8 caracteres</b><br>" +
                            "• Al menos <b>1 número</b><br>" +
                            "• Al menos <b>1 letra Mayúscula</b></html>",
                    "Seguridad Neon", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean exito = service.registrar(nombre, contrasena);
        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Cuenta creada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            txtNombre.setText("");
            txtPassword.setText("");
            new VentanaLogin(this.service).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar. El usuario podría ya existir.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}