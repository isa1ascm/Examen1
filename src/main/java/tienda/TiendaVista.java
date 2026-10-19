package tienda;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class TiendaVista extends JFrame {

    JTable tablaProductos;
    JTable tablaCarrito;

    DefaultTableModel modeloProductos;
    DefaultTableModel modeloCarrito;

    JSpinner cantidad;
    JTextField numeroTarjeta;

    JLabel totalCompra;
    JLabel datosTarjeta;

    JButton botonAgregar;
    JButton botonConsultar;
    JButton botonPagar;
    JButton botonMenos;
    JButton botonMas;
    JButton botonQuitar;
    JButton botonVolver;

    JPanel panelPago;
    JPanel panelDatosPago;
    CardLayout tarjetasPago;

    private final Color fondo = new Color(245, 247, 251);
    private final Color blanco = Color.WHITE;
    private final Color azul = new Color(37, 99, 235);
    private final Color azulOscuro = new Color(30, 41, 59);
    private final Color texto = new Color(51, 65, 85);
    private final Color gris = new Color(226, 232, 240);
    private final Color verde = new Color(22, 163, 74);
    private final Color grisScroll = new Color(148, 163, 184);
    private final Color fondoScroll = new Color(248, 250, 252);

    public TiendaVista() {
        setTitle("Arki Store | Punto de venta");
        setSize(1120, 760);
        setMinimumSize(new Dimension(950, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        botonVolver = crearBoton("Nueva Venta", azul);

        getContentPane().setBackground(fondo);

        JPanel principal = new JPanel(new BorderLayout(0, 18));
        principal.setBackground(fondo);
        principal.setBorder(
                BorderFactory.createEmptyBorder(22, 26, 22, 26));

        principal.add(crearEncabezado(), BorderLayout.NORTH);
        principal.add(crearContenido(), BorderLayout.CENTER);
        principal.add(crearPanelPago(), BorderLayout.SOUTH);

        add(principal);
        setVisible(true);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titulos = new JPanel();
        titulos.setOpaque(false);
        titulos.setLayout(new BoxLayout(titulos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Punto de venta");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 27));
        titulo.setForeground(azulOscuro);

        JLabel subtitulo = new JLabel(
                "Administra tus productos y procesa una compra");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(100, 116, 139));

        titulos.add(titulo);
        titulos.add(Box.createVerticalStrut(5));
        titulos.add(subtitulo);

        JLabel marca = new JLabel("ARKI STORE");
        marca.setFont(new Font("SansSerif", Font.BOLD, 15));
        marca.setForeground(azul);
        marca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(gris),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        marca.setOpaque(true);
        marca.setBackground(blanco);

        panel.add(titulos, BorderLayout.WEST);
        panel.add(marca, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new GridLayout(1, 2, 18, 0));
        contenido.setOpaque(false);

        contenido.add(crearPanelProductos());
        contenido.add(crearPanelCarrito());

        return contenido;
    }

    private JPanel crearPanelProductos() {
        JPanel panel = crearTarjeta();
        panel.setLayout(new BorderLayout(0, 14));

        panel.add(crearTituloPanel(
                        "Productos disponibles",
                        "Selecciona un producto para agregarlo"),
                BorderLayout.NORTH);

        modeloProductos = new DefaultTableModel(
                new Object[]{"Producto", "Precio"}, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloProductos);

        modeloProductos.addRow(new Object[]{"Audífonos", 350.00});
        modeloProductos.addRow(new Object[]{"Teclado", 500.00});
        modeloProductos.addRow(new Object[]{"Mouse", 250.00});
        modeloProductos.addRow(new Object[]{"Bocina", 700.00});
        modeloProductos.addRow(new Object[]{"Memoria USB", 180.00});

        configurarTabla(tablaProductos);
        tablaProductos.setRowHeight(42);
        tablaProductos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tablaProductos.getColumnModel().getColumn(1)
                .setCellRenderer(new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean selected, boolean focus,
                            int row, int column) {

                        JLabel label = (JLabel) super
                                .getTableCellRendererComponent(
                                        table, value, selected,
                                        focus, row, column);

                        label.setText(String.format("$%.2f",
                                Double.parseDouble(value.toString())));
                        label.setHorizontalAlignment(
                                SwingConstants.RIGHT);
                        label.setBorder(
                                BorderFactory.createEmptyBorder(
                                        0, 0, 0, 12));

                        return label;
                    }
                });

        JScrollPane scrollProductos = new JScrollPane(tablaProductos);
        configurarScroll(scrollProductos);

        panel.add(scrollProductos, BorderLayout.CENTER);

        JPanel controles = new JPanel(new BorderLayout(10, 0));
        controles.setOpaque(false);

        JPanel selector = new JPanel(new BorderLayout(5, 5));
        selector.setOpaque(false);

        JLabel etiquetaCantidad = new JLabel("Cantidad");
        etiquetaCantidad.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        etiquetaCantidad.setForeground(texto);

        JPanel controlCantidad = new JPanel(new BorderLayout(4, 0));
        controlCantidad.setOpaque(false);

        cantidad = new JSpinner(
                new SpinnerNumberModel(1, 1, 100, 1));

        cantidad.setUI(new BasicSpinnerUI() {
            @Override
            protected Component createNextButton() {
                return crearBotonSpinnerOculto();
            }

            @Override
            protected Component createPreviousButton() {
                return crearBotonSpinnerOculto();
            }
        });

        cantidad.setFont(new Font("SansSerif", Font.BOLD, 14));
        cantidad.setPreferredSize(new Dimension(55, 38));
        cantidad.setBorder(
                BorderFactory.createLineBorder(gris));

        JSpinner.DefaultEditor editor =
                (JSpinner.DefaultEditor) cantidad.getEditor();

        editor.getTextField().setHorizontalAlignment(
                JTextField.CENTER);
        editor.getTextField().setEditable(false);
        editor.getTextField().setFocusable(false);
        editor.getTextField().setBackground(blanco);
        editor.getTextField().setBorder(
                BorderFactory.createEmptyBorder());

        botonMenos = crearBoton("−", new Color(226, 232, 240));
        botonMenos.setForeground(azulOscuro);
        botonMenos.setPreferredSize(new Dimension(38, 38));
        botonMenos.setBorder(
                BorderFactory.createLineBorder(gris));

        botonMas = crearBoton("+", new Color(226, 232, 240));
        botonMas.setForeground(azulOscuro);
        botonMas.setPreferredSize(new Dimension(38, 38));
        botonMas.setBorder(
                BorderFactory.createLineBorder(gris));

        botonMenos.addActionListener(e -> {
            int valor = (Integer) cantidad.getValue();
            if (valor > 1) {
                cantidad.setValue(valor - 1);
            }
        });

        botonMas.addActionListener(e -> {
            int valor = (Integer) cantidad.getValue();
            if (valor < 100) {
                cantidad.setValue(valor + 1);
            }
        });

        controlCantidad.add(botonMenos, BorderLayout.WEST);
        controlCantidad.add(cantidad, BorderLayout.CENTER);
        controlCantidad.add(botonMas, BorderLayout.EAST);

        selector.add(etiquetaCantidad, BorderLayout.NORTH);
        selector.add(controlCantidad, BorderLayout.CENTER);

        botonAgregar = crearBoton(
                "＋  Agregar al carrito", azul);

        controles.add(selector, BorderLayout.WEST);
        controles.add(botonAgregar, BorderLayout.CENTER);

        panel.add(controles, BorderLayout.SOUTH);

        return panel;
    }

    private Component crearBotonSpinnerOculto() {
        JButton boton = new JButton();

        boton.setPreferredSize(new Dimension(0, 0));
        boton.setMinimumSize(new Dimension(0, 0));
        boton.setMaximumSize(new Dimension(0, 0));
        boton.setBorder(BorderFactory.createEmptyBorder());
        boton.setFocusable(false);
        boton.setVisible(false);

        return boton;
    }

    private JPanel crearPanelCarrito() {
        JPanel panel = crearTarjeta();
        panel.setLayout(new BorderLayout(0, 14));

        panel.add(crearTituloPanel(
                        "Tu compra",
                        "Productos seleccionados"),
                BorderLayout.NORTH);

        modeloCarrito = new DefaultTableModel(
                new Object[]{"Producto", "Cant.", "Precio", "Importe"},
                0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloCarrito);

        configurarTabla(tablaCarrito);
        tablaCarrito.setRowHeight(38);
        tablaCarrito.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollCarrito = new JScrollPane(tablaCarrito);
        configurarScroll(scrollCarrito);

        panel.add(scrollCarrito, BorderLayout.CENTER);

        JPanel resumen = new JPanel(new BorderLayout());
        resumen.setBackground(new Color(239, 246, 255));
        resumen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(191, 219, 254)),
                BorderFactory.createEmptyBorder(15, 14, 15, 14)));

        JLabel etiquetaTotal = new JLabel("TOTAL A PAGAR");
        etiquetaTotal.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        etiquetaTotal.setForeground(new Color(71, 85, 105));

        totalCompra = new JLabel("Total de compra: $0.00");
        totalCompra.setFont(
                new Font("SansSerif", Font.BOLD, 21));
        totalCompra.setForeground(azulOscuro);

        resumen.add(etiquetaTotal, BorderLayout.NORTH);
        resumen.add(totalCompra, BorderLayout.SOUTH);

        botonQuitar = crearBoton(
                "Quitar seleccionado", new Color(220, 38, 38));

        JPanel pieCarrito = new JPanel(new BorderLayout(0, 10));
        pieCarrito.setOpaque(false);

        pieCarrito.add(botonQuitar, BorderLayout.NORTH);
        pieCarrito.add(resumen, BorderLayout.SOUTH);

        panel.add(pieCarrito, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelPago() {
        panelPago = crearTarjeta();
        panelPago.setLayout(new BorderLayout(0, 12));

        panelPago.add(crearTituloPanel(
                        "Pago con tarjeta",
                        "Consulta la tarjeta de prueba para continuar"),
                BorderLayout.NORTH);

        tarjetasPago = new CardLayout();
        panelDatosPago = new JPanel(tarjetasPago);
        panelDatosPago.setBackground(blanco);

        JPanel formulario = new JPanel(new BorderLayout(12, 10));
        formulario.setBackground(blanco);

        JPanel entrada = new JPanel(new BorderLayout(8, 0));
        entrada.setOpaque(false);

        JPanel campoPanel = new JPanel(new BorderLayout(0, 6));
        campoPanel.setOpaque(false);

        JLabel etiqueta = new JLabel("Número de tarjeta");
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiqueta.setForeground(texto);

        numeroTarjeta = new JTextField();
        numeroTarjeta.setFont(
                new Font("SansSerif", Font.PLAIN, 15));
        numeroTarjeta.setPreferredSize(new Dimension(250, 40));
        numeroTarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(gris),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        numeroTarjeta.setToolTipText(
                "Ingresa el número completo de la tarjeta de prueba");

        campoPanel.add(etiqueta, BorderLayout.NORTH);
        campoPanel.add(numeroTarjeta, BorderLayout.CENTER);

        botonConsultar = crearBoton("Consultar tarjeta", azul);

        entrada.add(campoPanel, BorderLayout.CENTER);
        entrada.add(botonConsultar, BorderLayout.EAST);

        datosTarjeta = new JLabel(
                "Aún no se ha consultado ninguna tarjeta.");
        datosTarjeta.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        datosTarjeta.setForeground(new Color(100, 116, 139));

        JPanel parteInferior = new JPanel(new BorderLayout(0, 10));
        parteInferior.setOpaque(false);

        parteInferior.add(entrada, BorderLayout.NORTH);
        parteInferior.add(datosTarjeta, BorderLayout.CENTER);

        botonPagar = crearBoton("Pagar compra", verde);
        botonPagar.setPreferredSize(new Dimension(160, 43));

        formulario.add(parteInferior, BorderLayout.CENTER);
        formulario.add(botonPagar, BorderLayout.EAST);

        panelDatosPago.add(formulario, "formulario");

        panelPago.add(panelDatosPago, BorderLayout.CENTER);

        return panelPago;
    }

    private JPanel crearTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(blanco);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(gris),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        return panel;
    }

    private JPanel crearTituloPanel(
            String titulo, String subtitulo) {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel encabezado = new JLabel(titulo);
        encabezado.setFont(
                new Font("SansSerif", Font.BOLD, 17));
        encabezado.setForeground(azulOscuro);

        JLabel descripcion = new JLabel(subtitulo);
        descripcion.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        descripcion.setForeground(new Color(100, 116, 139));

        panel.add(encabezado);
        panel.add(Box.createVerticalStrut(5));
        panel.add(descripcion);

        return panel;
    }

    private void configurarTabla(JTable tabla) {
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setForeground(texto);
        tabla.setBackground(blanco);
        tabla.setSelectionBackground(new Color(219, 234, 254));
        tabla.setSelectionForeground(azulOscuro);
        tabla.setGridColor(new Color(241, 245, 249));
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setRowHeight(36);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setFillsViewportHeight(true);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 12));
        encabezado.setBackground(new Color(248, 250, 252));
        encabezado.setForeground(texto);
        encabezado.setPreferredSize(new Dimension(100, 38));
        encabezado.setReorderingAllowed(false);
    }

    private JButton crearBoton(String textoBoton, Color color) {
        JButton boton = new JButton(textoBoton);

        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(
                BorderFactory.createEmptyBorder(11, 15, 11, 15));

        return boton;
    }

    public void mostrarTicket(String ticket) {
        JPanel comprobante = new JPanel(new BorderLayout(0, 15));
        comprobante.setBackground(blanco);
        comprobante.setBorder(
                BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(
                new BoxLayout(encabezado, BoxLayout.Y_AXIS));

        JLabel marca = new JLabel("ARKI STORE");
        marca.setFont(new Font("SansSerif", Font.BOLD, 22));
        marca.setForeground(azul);
        marca.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("COMPROBANTE DE COMPRA");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(azulOscuro);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mensaje = new JLabel("Gracias por tu compra");
        mensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));
        mensaje.setForeground(new Color(100, 116, 139));
        mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        encabezado.add(marca);
        encabezado.add(Box.createVerticalStrut(4));
        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(4));
        encabezado.add(mensaje);

        comprobante.add(encabezado, BorderLayout.NORTH);

        JTextArea detalle = new JTextArea(ticket);
        detalle.setEditable(false);
        detalle.setFocusable(false);
        detalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detalle.setForeground(azulOscuro);
        detalle.setBackground(fondoScroll);
        detalle.setBorder(
                BorderFactory.createEmptyBorder(12, 14, 12, 14));
        detalle.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(detalle);
        configurarScroll(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(gris));
        scroll.getViewport().setBackground(fondoScroll);

        comprobante.add(scroll, BorderLayout.CENTER);

        JLabel estado = new JLabel(
                "\u2713  COMPRA COMPLETADA  |  Pago simulado");
        estado.setFont(new Font("SansSerif", Font.BOLD, 12));
        estado.setForeground(verde);
        estado.setHorizontalAlignment(SwingConstants.CENTER);
        estado.setBorder(
                BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.add(estado, BorderLayout.NORTH);

        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.add(botonVolver);
        panelSur.add(panelBoton, BorderLayout.SOUTH);

        comprobante.add(panelSur, BorderLayout.SOUTH);

        panelDatosPago.add(comprobante, "ticket");
        tarjetasPago.show(panelDatosPago, "ticket");

        panelDatosPago.revalidate();
        panelDatosPago.repaint();
    }

    private void configurarScroll(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(blanco);
        scroll.getViewport().setBackground(blanco);
        scroll.setWheelScrollingEnabled(true);

        configurarBarraScroll(
                scroll.getVerticalScrollBar(), true);

        configurarBarraScroll(
                scroll.getHorizontalScrollBar(), false);
    }

    private void configurarBarraScroll(
            JScrollBar barra, boolean vertical) {

        barra.setUnitIncrement(16);
        barra.setBorder(BorderFactory.createEmptyBorder());
        barra.setOpaque(false);

        if (vertical) {
            barra.setPreferredSize(new Dimension(8, 0));
        } else {
            barra.setPreferredSize(new Dimension(0, 8));
        }

        barra.setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor = grisScroll;
                trackColor = fondoScroll;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return crearBotonScroll();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return crearBotonScroll();
            }

            @Override
            protected void paintTrack(
                    Graphics g, JComponent c, Rectangle r) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setColor(fondoScroll);
                g2.fillRect(r.x, r.y, r.width, r.height);

                g2.dispose();
            }

            @Override
            protected void paintThumb(
                    Graphics g, JComponent c, Rectangle r) {

                if (r.isEmpty()) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(isDragging ? azul : grisScroll);

                if (vertical) {
                    g2.fillRoundRect(
                            r.x + 1,
                            r.y + 2,
                            Math.max(1, r.width - 2),
                            Math.max(1, r.height - 4),
                            8, 8);
                } else {
                    g2.fillRoundRect(
                            r.x + 2,
                            r.y + 1,
                            Math.max(1, r.width - 4),
                            Math.max(1, r.height - 2),
                            8, 8);
                }

                g2.dispose();
            }

            @Override
            protected void setThumbBounds(
                    int x, int y, int width, int height) {

                super.setThumbBounds(x, y, width, height);
            }
        });
    }

    private JButton crearBotonScroll() {
        JButton boton = new JButton();

        boton.setPreferredSize(new Dimension(0, 0));
        boton.setMinimumSize(new Dimension(0, 0));
        boton.setMaximumSize(new Dimension(0, 0));
        boton.setBorder(BorderFactory.createEmptyBorder());
        boton.setFocusable(false);
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);

        return boton;
    }
}