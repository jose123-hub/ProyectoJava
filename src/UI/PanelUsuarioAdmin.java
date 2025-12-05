package UI;
import DAO.UsuarioDAOImplementacion;
import DAO.CompraDAOImplementacion;
import Modelos.Usuario;
import Modelos.Compra;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelUsuarioAdmin extends JPanel {
    private JTable tablaUsuarios, tablaCompras;
    private DefaultTableModel modeloUsuarios, modeloCompras;
    private UsuarioDAOImplementacion usuarioDAO = new UsuarioDAOImplementacion();
    private CompraDAOImplementacion compraDAO = new CompraDAOImplementacion();
    private static final Color DARK_BG = new Color(10, 10, 20);
    private static final Color PANEL_BG = new Color(20, 20, 35);
    private static final Color NEON_CYAN = new Color(0, 255, 255);
    private static final Color NEON_RED = new Color(255, 50, 80);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Font FONT_TITLE = new Font("Consolas", Font.BOLD, 14);
    private static final Font FONT_TABLE = new Font("Consolas", Font.PLAIN, 12);

    public PanelUsuarioAdmin() {
        setLayout(new GridLayout(1, 2, 15, 0));
        setBackground(DARK_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlIzq = new JPanel(new BorderLayout());
        pnlIzq.setBackground(DARK_BG);

        TitledBorder bordeIzq = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(NEON_RED), " 👤 CLIENTES REGISTRADOS ");
        bordeIzq.setTitleColor(NEON_RED);
        bordeIzq.setTitleFont(FONT_TITLE);
        pnlIzq.setBorder(bordeIzq);

        modeloUsuarios = new DefaultTableModel(new Object[]{"ID", "Usuario", "Estado"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaUsuarios = new JTable(modeloUsuarios);
        estilizarTablaNeon(tablaUsuarios, NEON_RED);

        JButton btnBloquear = new JButton("🚫 BLOQUEAR / DESBLOQUEAR");
        btnBloquear.setFont(new Font("Consolas", Font.BOLD, 14));
        btnBloquear.setBackground(NEON_RED);
        btnBloquear.setForeground(Color.WHITE);
        btnBloquear.setFocusPainted(false);
        btnBloquear.setContentAreaFilled(false);
        btnBloquear.setOpaque(true);
        btnBloquear.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NEON_RED),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        btnBloquear.addActionListener(e -> toggleBloqueo());

        pnlIzq.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        pnlIzq.add(btnBloquear, BorderLayout.SOUTH);

        JPanel pnlDer = new JPanel(new BorderLayout());
        pnlDer.setBackground(DARK_BG);

        TitledBorder bordeDer = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(NEON_CYAN), " 🛒 HISTORIAL DE COMPRAS ");
        bordeDer.setTitleColor(NEON_CYAN);
        bordeDer.setTitleFont(FONT_TITLE);
        pnlDer.setBorder(bordeDer);

        modeloCompras = new DefaultTableModel(new Object[]{"ID Compra", "Total Pagado", "Fecha"}, 0);
        tablaCompras = new JTable(modeloCompras);
        estilizarTablaNeon(tablaCompras, NEON_CYAN);

        pnlDer.add(new JScrollPane(tablaCompras), BorderLayout.CENTER);

        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> cargarCompras());

        add(pnlIzq);
        add(pnlDer);
        cargarUsuarios();
        pintarScrolls(pnlIzq);
        pintarScrolls(pnlDer);
    }

    private void estilizarTablaNeon(JTable tabla, Color colorNeon) {
        tabla.setBackground(PANEL_BG);
        tabla.setForeground(TEXT_WHITE);
        tabla.setSelectionBackground(colorNeon);
        tabla.setSelectionForeground(DARK_BG);

        tabla.setFont(FONT_TABLE);
        tabla.setRowHeight(30);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new Color(50, 50, 70));

        tabla.getTableHeader().setBackground(new Color(30, 30, 50));
        tabla.getTableHeader().setForeground(colorNeon);
        tabla.getTableHeader().setFont(FONT_TITLE);
        tabla.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, colorNeon));
    }

    private void pintarScrolls(JPanel panel) {
        for (Component c : panel.getComponents()) {
            if (c instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) c;
                scroll.getViewport().setBackground(DARK_BG);
                scroll.setBackground(DARK_BG);
                scroll.setBorder(null);
            }
        }
    }

    private void cargarUsuarios() {
        modeloUsuarios.setRowCount(0);
        List<Usuario> lista = usuarioDAO.listarTodos();
        for(Usuario u : lista) {
            String estado = u.isActivo() ? "ACTIVO" : "BLOQUEADO";
            modeloUsuarios.addRow(new Object[]{u.getId(), u.getNombre(), estado});
        }
    }

    private void cargarCompras() {
        int row = tablaUsuarios.getSelectedRow();
        if(row == -1) return;

        Object objId = modeloUsuarios.getValueAt(row, 0);
        int idUser = Integer.parseInt(objId.toString());

        modeloCompras.setRowCount(0);
        List<Compra> lista = compraDAO.listarComprasPorUsuario(idUser);

        for(Compra c : lista) {
            modeloCompras.addRow(new Object[]{c.getId(), "$" + c.getTotal(), c.getFecha()});
        }
    }

    private void toggleBloqueo() {
        int row = tablaUsuarios.getSelectedRow();
        if(row == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario primero.");
            return;
        }
        Object objId = modeloUsuarios.getValueAt(row, 0);
        int idUser = Integer.parseInt(objId.toString());

        String estadoActual = (String) modeloUsuarios.getValueAt(row, 2);
        boolean nuevoEstado = estadoActual.equals("BLOQUEADO");

        if(usuarioDAO.cambiarEstadoUsuario(idUser, nuevoEstado)) {
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar estado.");
        }
    }
}
