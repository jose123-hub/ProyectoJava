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
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("MIS COMPRAS REALIZADAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Consolas", Font.BOLD, 24));
        lblTitulo.setForeground(NEON_BLUE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        inicializarComponentes();

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                cargarCompras();
            }
        });
    }

    private void inicializarComponentes() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID Compra", "Fecha", "Total Pagado"}, 0
        ) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCompras = new JTable(modeloTabla);
        tablaCompras.setBackground(new Color(20, 20, 35));
        tablaCompras.setForeground(TEXT_COLOR);
        tablaCompras.setSelectionBackground(NEON_BLUE);
        tablaCompras.setSelectionForeground(DARK_BG);
        tablaCompras.setRowHeight(30);
        tablaCompras.setFont(new Font("Consolas", Font.PLAIN, 14));
        tablaCompras.setShowVerticalLines(false);
        tablaCompras.setGridColor(new Color(50, 50, 80));
        tablaCompras.getTableHeader().setBackground(new Color(30, 30, 50));
        tablaCompras.getTableHeader().setForeground(NEON_BLUE);
        tablaCompras.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 14));
        tablaCompras.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, NEON_BLUE));

        JScrollPane scroll = new JScrollPane(tablaCompras);
        scroll.getViewport().setBackground(DARK_BG);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 60)));

        add(scroll, BorderLayout.CENTER);
    }

    private void cargarCompras() {
        modeloTabla.setRowCount(0);
        List<Compra> compras = compraServicio.listarComprasPorUsuario(usuario.getId());
        for (Compra c : compras) {
            modeloTabla.addRow(new Object[]{
                    c.getId(),
                    c.getFecha().toString(),
                    String.format("$ %.2f", c.getTotal()),
            });
        }
    }
}
