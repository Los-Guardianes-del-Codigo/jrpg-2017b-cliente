package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cliente.Cliente;
import dominio.Personaje;
import juego.Pantalla;
import mensajeria.Comando;
import javax.swing.ButtonGroup;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class MenuAsignarSkills extends JFrame {

	private JPanel contentPane;
	private int puntosAsignarInicial = 10;
	private int puntosFuerzaInicial = 0;
	private int puntosDestrezaInicial = 0;
	private int puntosInteligenciaInicial = 0;
	private int puntosAsignar = puntosAsignarInicial;
	private int puntosFuerza = puntosFuerzaInicial;
	private int puntosDestreza = puntosDestrezaInicial;
	private int puntosInteligencia = puntosInteligenciaInicial;
	private final Gson gson = new Gson();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final Action action = new SwingAction();

	/**
	 * Create the frame.
	 */
	public MenuAsignarSkills(final Cliente cliente) {
		puntosAsignarInicial = cliente.getPaquetePersonaje().getPuntosAasignar();
		puntosFuerzaInicial = cliente.getPaquetePersonaje().getFuerza();
		puntosDestrezaInicial = cliente.getPaquetePersonaje().getDestreza();
		puntosInteligenciaInicial = cliente.getPaquetePersonaje().getInteligencia();
		puntosAsignar = puntosAsignarInicial;
		puntosFuerza = puntosFuerzaInicial;
		puntosDestreza = puntosDestrezaInicial;
		puntosInteligencia = puntosInteligenciaInicial;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 298, 294);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("recursos//1up.png"));
		setTitle("Asignar");
		setBounds(100, 100, 298, 294);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Pantalla.menuAsignar = null;
				dispose();
			}
		});
		
		// atributo fuerza
		final JLabel lblFuerza = new JLabel("Fuerza");
		lblFuerza.setForeground(Color.WHITE);
		lblFuerza.setHorizontalAlignment(SwingConstants.CENTER);
		lblFuerza.setBounds(50, 72, 56, 16);
		contentPane.add(lblFuerza);

		final JLabel labelFuerza = new JLabel("");
		labelFuerza.setForeground(Color.WHITE);
		labelFuerza.setHorizontalAlignment(SwingConstants.CENTER);
		labelFuerza.setBounds(50, 101, 56, 16);
		labelFuerza.setText(String.valueOf(puntosFuerzaInicial));
		contentPane.add(labelFuerza);
		
		// atributo destreza
		JLabel lblDestreza = new JLabel("Destreza");
		lblDestreza.setForeground(Color.WHITE);
		lblDestreza.setHorizontalAlignment(SwingConstants.CENTER);
		lblDestreza.setBounds(50, 130, 56, 16);
		contentPane.add(lblDestreza);

		final JLabel labelDestreza = new JLabel("");
		labelDestreza.setForeground(Color.WHITE);
		labelDestreza.setHorizontalAlignment(SwingConstants.CENTER);
		labelDestreza.setBounds(50, 159, 56, 16);
		labelDestreza.setText(String.valueOf(puntosDestrezaInicial));
		contentPane.add(labelDestreza);
		
		// atributo inteligencia
		final JLabel lblInteligencia = new JLabel("Inteligencia");
		lblInteligencia.setForeground(Color.WHITE);
		lblInteligencia.setHorizontalAlignment(SwingConstants.CENTER);
		lblInteligencia.setBounds(39, 188, 83, 16);
		contentPane.add(lblInteligencia);

		final JLabel labelInteligencia = new JLabel("");
		labelInteligencia.setForeground(Color.WHITE);
		labelInteligencia.setHorizontalAlignment(SwingConstants.CENTER);
		labelInteligencia.setBounds(50, 217, 56, 16);
		labelInteligencia.setText(String.valueOf(puntosInteligenciaInicial));
		contentPane.add(labelInteligencia);
		
		// Puntos a asignar
		final JLabel lblCantidadDePuntos = new JLabel("Cantidad de Puntos a Asignar");
		lblCantidadDePuntos.setForeground(Color.WHITE);
		lblCantidadDePuntos.setBounds(12, 13, 177, 29);
		contentPane.add(lblCantidadDePuntos);

		final JLabel labelPuntos = new JLabel("");
		labelPuntos.setForeground(Color.WHITE);
		labelPuntos.setHorizontalAlignment(SwingConstants.CENTER);
		labelPuntos.setBounds(39, 41, 83, 26);
		labelPuntos.setText(String.valueOf(puntosAsignarInicial));
		contentPane.add(labelPuntos);
		
		
		final JButton buttonConfirm = new JButton("Confirmar");
		ImageIcon icono_confirm = new ImageIcon("recursos//botonConfirmar.png");
		buttonConfirm.setIcon(icono_confirm);
		buttonConfirm.setEnabled(false);
		buttonConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {;
				puntosAsignarInicial = puntosAsignar;
				int bonusF = puntosFuerza-puntosFuerzaInicial;
				int bonusD = puntosDestreza-puntosDestrezaInicial;
				int bonusI = puntosInteligencia-puntosInteligenciaInicial;
				
				cliente.getPaquetePersonaje().setPuntosAasignar(puntosAsignar);
				
				cliente.getPaquetePersonaje().useBonus(0, 0, bonusF, bonusD, bonusI);
				cliente.getPaquetePersonaje().removerBonus();
				cliente.getPaquetePersonaje().setComando(Comando.ACTUALIZARPERSONAJELV);
				try {
					cliente.getSalida().writeObject(gson.toJson(cliente.getPaquetePersonaje()));
				} catch (JsonSyntaxException | IOException e1) {
					JOptionPane.showMessageDialog(null, "Error al actualizar stats");

				}
				JOptionPane.showMessageDialog(null,"Se han actualizado tus atributos.");
				dispose();
			}
		});
		buttonConfirm.setBounds(176, 112, 97, 25);
		contentPane.add(buttonConfirm);
		
		JButton buttonCancel = new JButton("Cancelar");
		ImageIcon icono_c = new ImageIcon("recursos//botonCancelar.png");
		buttonCancel.setIcon(icono_c);
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Pantalla.menuAsignar = null;
				dispose();
			}
		});
		buttonCancel.setBounds(176, 146, 97, 25);
		contentPane.add(buttonCancel);
		
		//Instanciando el boton reiniciar
		final JButton buttonReiniciarPuntos = new JButton("Reiniciar");
		buttonReiniciarPuntos.setBounds(176, 40, 97, 25);
		contentPane.add(buttonReiniciarPuntos);
		
		// aumentar o dismunir de fuerza
		final JButton buttonMinus = new JButton("");
		final JButton buttonMore = new JButton("");

		// aumentar o dismunir de destreza
		final JButton buttonMinus1 = new JButton("");
		final JButton buttonMore1 = new JButton("");

		// aumentar o dismunir de inteligencia
		final JButton buttonMinus2 = new JButton("");
		final JButton buttonMore2 = new JButton("");

		buttonMinus.setEnabled(false);
		buttonMinus1.setEnabled(false);
		buttonMinus2.setEnabled(false);
		
		ImageIcon icono_1 = new ImageIcon("recursos//botonMenoss.png");
		buttonMinus.setIcon(icono_1);
		buttonMinus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(puntosFuerza > puntosFuerzaInicial){
					puntosFuerza--;
					if(puntosAsignar == 0){
						if(puntosInteligencia != 200){
							buttonMore2.setEnabled(true);
						}
						if(puntosDestreza != 200){
							buttonMore1.setEnabled(true);
						}
					} else {
							buttonMore.setEnabled(true);
							buttonMore1.setEnabled(true);
							buttonMore2.setEnabled(true);
					}
					puntosAsignar++;
					if(puntosAsignar == puntosAsignarInicial){
						buttonConfirm.setEnabled(false);
					}
					labelPuntos.setText(String.valueOf(puntosAsignar));
					labelFuerza.setText(String.valueOf(puntosFuerza));
					if(puntosFuerza == puntosFuerzaInicial){
						buttonMinus.setEnabled(false);
						buttonMore.setEnabled(true);
					} else if(puntosFuerza >= puntosFuerzaInicial) {
						buttonMore.setEnabled(true);
					}
				}
			}
		});
		
				
		buttonMinus.setBounds(12, 92, 34, 25);
		contentPane.add(buttonMinus);
		
		buttonMinus1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(puntosDestreza > puntosDestrezaInicial){
					puntosDestreza--;
					if(puntosAsignar == 0){
						if(puntosInteligencia != 200){
							buttonMore2.setEnabled(true);
						}
						if(puntosFuerza != 200){
							buttonMore.setEnabled(true);
						}
					} else {
							buttonMore.setEnabled(true);
							buttonMore1.setEnabled(true);
							buttonMore2.setEnabled(true);
					}
					puntosAsignar++;
					if(puntosAsignar == puntosAsignarInicial){
						buttonConfirm.setEnabled(false);
					}
					labelPuntos.setText(String.valueOf(puntosAsignar));
					labelDestreza.setText(String.valueOf(puntosDestreza));
					if(puntosDestreza == puntosDestrezaInicial){
						buttonMinus1.setEnabled(false);
						buttonMore1.setEnabled(true);
					} else if(puntosDestreza >= puntosDestrezaInicial) {
						buttonMore1.setEnabled(true);
					}
				}
			}
		});
		buttonMinus1.setIcon(icono_1);
		buttonMinus1.setBounds(12, 159, 34, 25);
		contentPane.add(buttonMinus1);
		
		buttonMinus2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(puntosInteligencia > puntosInteligenciaInicial){
					puntosInteligencia--;
					if(puntosAsignar == 0){
						if(puntosFuerza != 200){
							buttonMore.setEnabled(true);
						}
						if(puntosDestreza != 200){
							buttonMore1.setEnabled(true);
						}
					} else {
							buttonMore.setEnabled(true);
							buttonMore1.setEnabled(true);
							buttonMore2.setEnabled(true);
					}
					puntosAsignar++;
					if(puntosAsignar == puntosAsignarInicial){
						buttonConfirm.setEnabled(false);
					}
					labelPuntos.setText(String.valueOf(puntosAsignar));
					labelInteligencia.setText(String.valueOf(puntosInteligencia));
					if(puntosInteligencia == puntosInteligenciaInicial){
						buttonMinus2.setEnabled(false);
						buttonMore2.setEnabled(true);
					} else if(puntosInteligencia >= puntosInteligenciaInicial) {
						buttonMore2.setEnabled(true);
					}
				}
			}
		});
		buttonMinus2.setIcon(icono_1);
		buttonMinus2.setBounds(12, 217, 34, 25);
		contentPane.add(buttonMinus2);
		
		buttonMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(puntosAsignar != 0 && !labelFuerza.getText().equals("200")){
					puntosFuerza++;
					puntosAsignar--;
					buttonConfirm.setEnabled(true);
					labelPuntos.setText(String.valueOf(puntosAsignar));
					labelFuerza.setText(String.valueOf(puntosFuerza));
					buttonMinus.setEnabled(true);
					if(puntosAsignar == 0){
							buttonMore.setEnabled(false);
							buttonMore1.setEnabled(false);
							buttonMore2.setEnabled(false);
					}
				}
				if(puntosAsignar == 0 || labelFuerza.getText().equals("200")){
					buttonMore.setEnabled(false);
				}
			}
		});
		ImageIcon icono_2 = new ImageIcon("recursos//botonMass.png");
		buttonMore.setIcon(icono_2);
		buttonMore.setBounds(118, 92, 34, 25);
		contentPane.add(buttonMore);
		
		
		buttonMore1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(puntosAsignar != 0 && !labelDestreza.getText().equals("200")){
					puntosDestreza++;
					puntosAsignar--;
					buttonConfirm.setEnabled(true);
					labelPuntos.setText(String.valueOf(puntosAsignar));
					labelDestreza.setText(String.valueOf(puntosDestreza));
					buttonMinus1.setEnabled(true);
					if(puntosAsignar == 0){
						buttonMore.setEnabled(false);
						buttonMore1.setEnabled(false);
						buttonMore2.setEnabled(false);
					}
					if(puntosAsignar == 0 || labelDestreza.getText().equals("200")){
						buttonMore1.setEnabled(false);
					}
				}
			}
		});
		buttonMore1.setIcon(icono_2);
		buttonMore1.setBounds(118, 159, 34, 25);
		contentPane.add(buttonMore1);
		
		buttonMore2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(puntosAsignar != 0 && !labelInteligencia.getText().equals("200")){
					puntosInteligencia++;
					puntosAsignar--;
					buttonConfirm.setEnabled(true);
					labelPuntos.setText(String.valueOf(puntosAsignar));
					labelInteligencia.setText(String.valueOf(puntosInteligencia));					
					buttonMinus2.setEnabled(true);
					if(puntosAsignar == 0){
						buttonMore.setEnabled(false);
						buttonMore1.setEnabled(false);
						buttonMore2.setEnabled(false);
					}
					if(puntosAsignar == 0 || labelInteligencia.getText().equals("200")){
						buttonMore2.setEnabled(false);
					}
				}
			}
		});
		buttonMore2.setIcon(icono_2);
		buttonMore2.setBounds(118, 217, 34, 25);
		contentPane.add(buttonMore2);
		
		//boton resetear
		final JButton btnResetear = new JButton("Resetear");
		//ImageIcon icono_r = new ImageIcon("recursos//botonCancelar.png");
		//btnResetear.setIcon(icono_r);
		btnResetear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				puntosAsignar = 3*cliente.getPaquetePersonaje().getNivel();
				labelPuntos.setText(String.valueOf(puntosAsignar));
				if (cliente.getPaquetePersonaje().getCasta().equals("Guerrero")) {
					puntosFuerza = 15;
					puntosDestreza = 10;
					puntosInteligencia = 10;
				}
				else if (cliente.getPaquetePersonaje().getCasta().equals("Hechicero")) {
					puntosFuerza = 10;
					puntosDestreza = 10;
					puntosInteligencia = 15;
				}
				else if ( cliente.getPaquetePersonaje().getCasta().equals("Asesino")) {
					puntosFuerza = 10;
					puntosDestreza = 15;
					puntosInteligencia = 10;
				}
				
				labelFuerza.setText(String.valueOf(puntosFuerza));
				labelDestreza.setText(String.valueOf(puntosDestreza));
				labelInteligencia.setText(String.valueOf(puntosInteligencia));
				buttonMinus.setEnabled(false);
				buttonMinus1.setEnabled(false);
				buttonMinus2.setEnabled(false);
				buttonMore.setEnabled(true);
				buttonMore1.setEnabled(true);
				buttonMore2.setEnabled(true);
			}
		});
		btnResetear.setBounds(176, 180, 97, 25);
		contentPane.add(btnResetear);
		
		final JLabel imageLabel = new JLabel(new ImageIcon("recursos//background.jpg")); 
		imageLabel.setBounds(0, 0, 298, 294);
		imageLabel.setVisible(true);
		contentPane.add(imageLabel, BorderLayout.SOUTH);
		
		//Acciones del boton reiniciar
		buttonReiniciarPuntos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cliente.getPaquetePersonaje().reiniciarPuntos();
				
				labelFuerza.setText(String.valueOf(cliente.getPaquetePersonaje().getFuerza()));
				labelDestreza.setText(String.valueOf(cliente.getPaquetePersonaje().getDestreza()));
				labelInteligencia.setText(String.valueOf(cliente.getPaquetePersonaje().getInteligencia()));
				labelPuntos.setText(String.valueOf(cliente.getPaquetePersonaje().getPuntosAasignar()));
				
				buttonMinus.setEnabled(false);
				buttonMinus1.setEnabled(false);
				buttonMinus2.setEnabled(false);
				
				buttonMore.setEnabled(true);
				buttonMore1.setEnabled(true);
				buttonMore2.setEnabled(true);
			}
		});
		
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
