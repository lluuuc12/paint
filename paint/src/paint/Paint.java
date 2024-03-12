package paint;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Paint extends JPanel {
	
	// variables locales
	private JFrame frmPaint;
	private JLabel lblCoordinates;
	private JPanel drawingPanel;

	int x1 = 0;
	int y1 = 0;
	int x2 = 0;
	int y2 = 0;
	int countFigure = 0;
	boolean undo = false;

	// arrays
	static String[] figuresComboBox = { "Line", "Rectangle", "Oval" };
	static String[] colorsComboBox = { "Blue", "Green", "Red", "Yellow", "Orange", "Pink", "Black" };
	static Color[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.ORANGE, Color.PINK, Color.BLACK };
	static ArrayList<Figure> figures = new ArrayList<>();

	public static ArrayList<Figure> getFigures() {
		return figures;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (Figure f : figures) {
			if (f != null) {
				f.draw(g);
			}
		}
	}

	public void redrawFigures(Graphics g) {
		drawingPanel.update(g);
		for (int i = 0; i < countFigure; i++) {
			getFigures().get(i).draw(g);
		}
		drawingPanel.paintComponents(g);
	}

	// métodos de la clase para obtener color del comboBox y pintar dependiendo de
	// la figura que se le pase
	public static Color selectColor(JComboBox combo) {
		int numColor = combo.getSelectedIndex();
		return colors[numColor];
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Paint window = new Paint();
					window.frmPaint.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Paint() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frmPaint = new JFrame();
		frmPaint.setResizable(false);
		frmPaint.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				Graphics g = drawingPanel.getGraphics();
				redrawFigures(g);
			}

			public void windowLostFocus(WindowEvent e) {
				Graphics g = drawingPanel.getGraphics();
				redrawFigures(g);
			}
		});
		frmPaint.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				drawingPanel.setBounds(0, 44, frmPaint.getWidth(), frmPaint.getHeight());
				lblCoordinates.setBounds(0, drawingPanel.getHeight() - 60, 335, 23);
				Graphics g = drawingPanel.getGraphics();
				redrawFigures(g);
			}
		});
		frmPaint.setTitle("Paint");
		frmPaint.setBounds(100, 100, 1000, 600);
		frmPaint.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPaint.getContentPane().setLayout(null);

		lblCoordinates = new JLabel("Mouse Coordinates");
		lblCoordinates.setBounds(0, 540, 335, 23);
		frmPaint.getContentPane().add(lblCoordinates);

		JComboBox comboBoxColors = new JComboBox();
		comboBoxColors.setModel(new DefaultComboBoxModel(colorsComboBox));
		comboBoxColors.setBounds(399, 12, 124, 24);
		frmPaint.getContentPane().add(comboBoxColors);

		JComboBox comboBoxFigures = new JComboBox();
		comboBoxFigures.setModel(new DefaultComboBoxModel(figuresComboBox));
		comboBoxFigures.setBounds(535, 12, 124, 24);
		frmPaint.getContentPane().add(comboBoxFigures);

		JCheckBox chckbxFilled = new JCheckBox("Filled");
		chckbxFilled.setBounds(667, 13, 129, 23);
		frmPaint.getContentPane().add(chckbxFilled);

		drawingPanel = new JPanel();
		drawingPanel.setBackground(Color.WHITE);
		drawingPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// cuando se clica sobre el panel se guardan las coordenadas x1 e y1
				x1 = e.getX();
				y1 = e.getY();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// lo mismo para cuando el raton está pesionado
				x1 = e.getX();
				y1 = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// cuando el raton se suelta, se obtienen las coordenadas para indicar que ese
				// va a ser el fin de la figura
				x2 = e.getX();
				y2 = e.getY();
				Graphics g = drawingPanel.getGraphics();
				// seleccionar figura
				int figureType = comboBoxFigures.getSelectedIndex();
				// relleno
				boolean filled = chckbxFilled.isSelected();
				// poner color
				Color color = selectColor(comboBoxColors);
				// declaramos objeto
				Figure f = null;
				// si se ha clicado sobre el boton undo y luego se pinta sobre el panel, se
				// eliminan todas las figuras
				// desde el ultimo undo
				if (undo) {
					Iterator<Figure> iterator = getFigures().iterator();
					int i = 0;
					while (iterator.hasNext()) {
					    iterator.next();
					    if (i >= countFigure) {
					        iterator.remove();
					    }
					    i++;
					}
				}
				undo = false;
				switch (figureType) {
				case 0:
					f = new Line(x1, y1, x2, y2, color);
					break;
				case 1:
					f = new Rectangle(x1, y1, x2, y2, color, filled);
					break;
				case 2:
					f = new Oval(x1, y1, x2, y2, color, filled);
					break;
				}
				// fuera del switch y una vez creado el objeto
				getFigures().add(f);
				countFigure++;
				// Volver a pintar todas las figuras
				for (int i = 0; i < countFigure; i++) {
					getFigures().get(i).draw(g);
				}
				paintComponents(g);
				repaint();
			}
		});
		drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					Point position = drawingPanel.getMousePosition();
					int positionX = (int) position.getX();
					int positionY = (int) position.getY();
					lblCoordinates.setText("Mouse Coordinates X: " + positionX + " Y: " + positionY);
				} catch (NullPointerException n) {

				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// cuando el raton se pulsa y se arrastra
				x2 = e.getX();
				y2 = e.getY();
				// tambien han de cambiar las coordenadas
				try {
					Point position = drawingPanel.getMousePosition();
					int positionX = (int) position.getX();
					int positionY = (int) position.getY();
					lblCoordinates.setText("Mouse Coordinates X: " + positionX + " Y: " + positionY);
				} catch (NullPointerException n) {
				}
				Graphics g = drawingPanel.getGraphics();
				drawingPanel.update(g);
				if (undo) {
					for (int i = getFigures().size() - 1; i >= countFigure; i--) {
						getFigures().remove(i);
					}
					undo = false;
				}
				paintComponent(g);
				// seleccionar figura
				int figureType = comboBoxFigures.getSelectedIndex();
				// relleno
				boolean filled = chckbxFilled.isSelected();
				// poner color
				Color color = selectColor(comboBoxColors);
				g.setColor(color);
				// declaramos objeto figura temporal para la previsualizacion
				int x = Math.min(x1, x2);
				int y = Math.min(y1, y2);
				int width = Math.abs(x2 - x1);
				int height = Math.abs(y2 - y1);
				switch (figureType) {
				case 0:
					g.drawLine(x1, y1, x2, y2);
					break;
				case 1:
					if (filled) {
						g.fillRect(x, y, width, height);
					} else {
						g.drawRect(x, y, width, height);
					}
					break;
				case 2:
					if (filled) {
						g.fillOval(x, y, width, height);
					} else {
						g.drawOval(x, y, width, height);
					}
					break;
				}
				repaint();
			}
		});
		drawingPanel.setBounds(0, 44, frmPaint.getWidth(), frmPaint.getHeight());
		frmPaint.getContentPane().add(drawingPanel);
		drawingPanel.setLayout(null);

		JButton btnUndo = new JButton("Undo");
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// volver hacia atras

				if (countFigure != 0) {
					Graphics g = drawingPanel.getGraphics();

					countFigure--;
					undo = true;
					drawingPanel.update(g);
					// Volver a pintar todas las figuras
					for (int i = 0; i < countFigure; i++) {
						getFigures().get(i).draw(g);
					}
					drawingPanel.paintComponents(g);
					repaint();
				}
			}
		});
		btnUndo.setBounds(12, 12, 117, 25);
		frmPaint.getContentPane().add(btnUndo);

		JButton btnRedo = new JButton("Redo");
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// volver hacia delante
				if (countFigure < getFigures().size()) {
					Graphics g = drawingPanel.getGraphics();

					countFigure++;
					drawingPanel.update(g);
					// Volver a pintar todas las figuras
					for (int i = 0; i < countFigure; i++) {
						getFigures().get(i).draw(g);
					}
					drawingPanel.paintComponents(g);
					repaint();
				}
			}
		});
		btnRedo.setBounds(141, 12, 117, 25);
		frmPaint.getContentPane().add(btnRedo);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				countFigure = 0;
				drawingPanel.repaint();
				getFigures().clear();
			}
		});
		btnClear.setBounds(270, 12, 117, 25);
		frmPaint.getContentPane().add(btnClear);
	}
}
