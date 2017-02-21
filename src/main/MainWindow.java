package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import engine.AntState;
import engine.Automaton;
import engine.BinaryState;
import engine.CellNeighborhood;
import engine.CellState;
import engine.CellStateFactory;
import engine.GameOfLife;
import engine.GameOfLifeRules;
import engine.InitialStructure;
import engine.InitialStructure.GOLStructure;
import engine.InitialStructure.GOLStructure.Still;
import engine.InitialStructure.Structure;
import engine.LangtonAnt;
import engine.LangtonCell;
import engine.MooreNeighborhood;
import engine.OneDimNeighborhood;
import engine.OneDimensionGame;
import engine.OneDimensionRules;
import engine.QuadState;
import engine.UniformStateFactory;
import engine.VonNeumanNeighborhood;
import engine.WireElectronState;
import engine.WireWorld;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 7695602625722584344L;

	private JComboBox<Game> cboxGameType;
	private JComboBox<Neighborhood> cboxNeighborsType;
	private JSpinner spinNeighborRadius;
	private JLabel lblNeighborRadius;
	private JPanel pnlRulesODG;
	private JSpinner spinRulesODG;
	private JPanel pnlRulesGOL;
	private JTextField txtRulesGOL;
	private JPanel pnlMapSize;
	private JPanel pnlNeighborhood;
	private JPanel pnlGameType;
	private Board board;
	private JPanel panel_1;
	private JButton btnNextStep;
	private JButton btnApplySettings;
	private JSlider sliderMapWidth;
	private JSlider sliderMapHeight;
	private JCheckBox chkIsMapWrapped;
	private JCheckBox chkIsQuadState;
	private Presenter presenter;
	private JPanel pnlMain;
	private JLabel lblNewCellState;
	private JComboBox<Structure> cboxStructureFamily;
	private JLabel lblStructureFamily;
	private JLabel lblStructure;
	private JButton btnAddStructure;
	private JComboBox<Structure> cboxStructure;
	
	//GAME SETTINGS
	Settings settings = new Settings();

	public MainWindow() {
		initComponents();
		createEvents();
	}
	
	private void initComponents() {
		setBounds(100, 100, 1209, 736);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Cellular Automaton - \u00A9 Maciej Znamirowski - maciek@znamirowski.pl");
		
		pnlMain = new JPanel();
		pnlMain.setBorder(null);
		setContentPane(pnlMain);
		
		JPanel pnlSettings = new JPanel();
		pnlSettings.setPreferredSize(new Dimension(210, 617));
		pnlSettings.setBorder(new EmptyBorder(0, 0, 0, 1));
		
		board = new Board(settings.mapWidth, settings.mapHeight, settings.gameType, settings.isQuadState);
		board.setBorder(null);
		
		pnlMain.setLayout(new BorderLayout(0, 0));
		pnlSettings.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		pnlGameType = new JPanel();
		pnlGameType.setPreferredSize(new Dimension(200, 64));
		pnlSettings.add(pnlGameType);
		pnlGameType.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Game type", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		cboxGameType = new JComboBox<>();
		cboxGameType.setModel(new DefaultComboBoxModel<Game>(Game.values()));
		GroupLayout gl_pnlGameType = new GroupLayout(pnlGameType);
		gl_pnlGameType.setHorizontalGroup(
			gl_pnlGameType.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlGameType.createSequentialGroup()
					.addContainerGap()
					.addComponent(cboxGameType, 0, 135, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_pnlGameType.setVerticalGroup(
			gl_pnlGameType.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlGameType.createSequentialGroup()
					.addContainerGap()
					.addComponent(cboxGameType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlGameType.setLayout(gl_pnlGameType);
		
		pnlNeighborhood = new JPanel();
		pnlNeighborhood.setPreferredSize(new Dimension(200, 89));
		pnlSettings.add(pnlNeighborhood);
		pnlNeighborhood.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Neighborhood", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		cboxNeighborsType = new JComboBox<>();
		cboxNeighborsType.setModel(new DefaultComboBoxModel<Neighborhood>(Neighborhood.values()));
		
		lblNeighborRadius = new JLabel("Radius:");
		
		spinNeighborRadius = new JSpinner();
		spinNeighborRadius.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		GroupLayout gl_pnlNeighborhood = new GroupLayout(pnlNeighborhood);
		gl_pnlNeighborhood.setHorizontalGroup(
			gl_pnlNeighborhood.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlNeighborhood.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlNeighborhood.createParallelGroup(Alignment.LEADING)
						.addComponent(cboxNeighborsType, 0, 168, Short.MAX_VALUE)
						.addGroup(gl_pnlNeighborhood.createSequentialGroup()
							.addComponent(lblNeighborRadius)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(spinNeighborRadius, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_pnlNeighborhood.setVerticalGroup(
			gl_pnlNeighborhood.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlNeighborhood.createSequentialGroup()
					.addContainerGap()
					.addComponent(cboxNeighborsType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlNeighborhood.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNeighborRadius)
						.addComponent(spinNeighborRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlNeighborhood.setLayout(gl_pnlNeighborhood);
		
		pnlMapSize = new JPanel();
		pnlMapSize.setPreferredSize(new Dimension(200, 185));
		pnlSettings.add(pnlMapSize);
		pnlMapSize.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Map dimensions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLabel lblSzeroko = new JLabel("Width:");
		
		JLabel lblWysoko = new JLabel("Height:");
		
		sliderMapWidth = new JSlider();
		sliderMapWidth.setValue(settings.mapWidth);
		sliderMapWidth.setMaximum(250);
		sliderMapWidth.setMinimum(10);
		sliderMapWidth.setPaintTicks(true);
		sliderMapWidth.setPaintLabels(true);
		sliderMapWidth.setMinorTickSpacing(5);
		sliderMapWidth.setMajorTickSpacing(30);
		
		sliderMapHeight = new JSlider();
		sliderMapHeight.setValue(settings.mapHeight);
		sliderMapHeight.setMaximum(250);
		sliderMapHeight.setMinimum(10);
		sliderMapHeight.setPaintTicks(true);
		sliderMapHeight.setPaintLabels(true);
		sliderMapHeight.setMinorTickSpacing(5);
		sliderMapHeight.setMajorTickSpacing(30);
		GroupLayout gl_pnlMapSize = new GroupLayout(pnlMapSize);
		gl_pnlMapSize.setHorizontalGroup(
			gl_pnlMapSize.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlMapSize.createSequentialGroup()
					.addGroup(gl_pnlMapSize.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlMapSize.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_pnlMapSize.createParallelGroup(Alignment.LEADING)
								.addComponent(lblSzeroko)
								.addComponent(lblWysoko)))
						.addGroup(gl_pnlMapSize.createParallelGroup(Alignment.TRAILING)
							.addComponent(sliderMapWidth, 0, 0, Short.MAX_VALUE)
							.addComponent(sliderMapHeight, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)))
					.addGap(0))
		);
		gl_pnlMapSize.setVerticalGroup(
			gl_pnlMapSize.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlMapSize.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSzeroko)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sliderMapWidth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(16)
					.addComponent(lblWysoko)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sliderMapHeight, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(20, Short.MAX_VALUE))
		);
		pnlMapSize.setLayout(gl_pnlMapSize);
		
		pnlRulesGOL = new JPanel();
		pnlRulesGOL.setPreferredSize(new Dimension(200, 64));
		pnlSettings.add(pnlRulesGOL);
		pnlRulesGOL.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Rules", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		txtRulesGOL = new JTextField();
		txtRulesGOL.setText("2,3/3");
		txtRulesGOL.setColumns(10);
		GroupLayout gl_pnlRulesGOL = new GroupLayout(pnlRulesGOL);
		gl_pnlRulesGOL.setHorizontalGroup(
			gl_pnlRulesGOL.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlRulesGOL.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtRulesGOL, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_pnlRulesGOL.setVerticalGroup(
			gl_pnlRulesGOL.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlRulesGOL.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtRulesGOL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(26, Short.MAX_VALUE))
		);
		pnlRulesGOL.setLayout(gl_pnlRulesGOL);
		
		pnlRulesODG = new JPanel();
		pnlRulesODG.setPreferredSize(new Dimension(200, 64));
		pnlSettings.add(pnlRulesODG);
		pnlRulesODG.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Rules", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		spinRulesODG = new JSpinner();
		spinRulesODG.setModel(new SpinnerNumberModel(30, 0, 255, 1));
		GroupLayout gl_pnlRulesODG = new GroupLayout(pnlRulesODG);
		gl_pnlRulesODG.setHorizontalGroup(
			gl_pnlRulesODG.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlRulesODG.createSequentialGroup()
					.addContainerGap()
					.addComponent(spinRulesODG, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_pnlRulesODG.setVerticalGroup(
			gl_pnlRulesODG.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlRulesODG.createSequentialGroup()
					.addContainerGap()
					.addComponent(spinRulesODG, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(49, Short.MAX_VALUE))
		);
		pnlRulesODG.setLayout(gl_pnlRulesODG);
		pnlMain.add(pnlSettings, BorderLayout.WEST);
		
		chkIsMapWrapped = new JCheckBox("Map wrapping");
		chkIsMapWrapped.setPreferredSize(new Dimension(200, 23));
		pnlSettings.add(chkIsMapWrapped);
		
		chkIsQuadState = new JCheckBox("Quad State");
		chkIsQuadState.setPreferredSize(new Dimension(200, 23));
		pnlSettings.add(chkIsQuadState);
		
		btnApplySettings = new JButton("Apply settings");
		btnApplySettings.setPreferredSize(new Dimension(200, 23));
		pnlSettings.add(btnApplySettings);
		pnlMain.add(board);
		
		panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(10, 40));
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlMain.add(panel_1, BorderLayout.SOUTH);
		
		btnNextStep = new JButton("Next state");
		
		JLabel lblDodanaKomrka = new JLabel("Added cell:");
		
		lblNewCellState = new JLabel("");
		
		lblStructureFamily = new JLabel("Structure family:");
		
		cboxStructureFamily = new JComboBox<>();
		cboxStructureFamily.setModel(new DefaultComboBoxModel<>(GOLStructure.values()));
		
		cboxStructure = new JComboBox<Structure>();
		cboxStructure.setModel(new DefaultComboBoxModel<>(Still.values()));
		
		lblStructure = new JLabel("Structure:");
		
		btnAddStructure = new JButton("Add");
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDodanaKomrka)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewCellState)
					.addPreferredGap(ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
					.addComponent(lblStructureFamily)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cboxStructureFamily, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblStructure)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cboxStructure, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnAddStructure)
					.addGap(230)
					.addComponent(btnNextStep)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
					.addComponent(btnNextStep, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
					.addComponent(cboxStructure, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(btnAddStructure)
					.addComponent(lblStructure)
					.addComponent(cboxStructureFamily, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(lblStructureFamily))
				.addComponent(lblNewCellState, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
				.addComponent(lblDodanaKomrka, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
		);
		panel_1.setLayout(gl_panel_1);
		
		presenter = new Presenter(getMainWindow(), createGame(settings.gameType), settings.mapWidth, settings.mapHeight);
		board.addMouseListener(board.createMouseListener(presenter));
		prepareGameSettings(settings.gameType);
	}
	
	private void createEvents() {
		cboxGameType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prepareGameSettings((Game)cboxGameType.getSelectedItem());
			}
		});
		
		btnNextStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				presenter.nextClicked();
			}
		});
		
		btnApplySettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applySettings();
				setStructureComponents();
				presenter = new Presenter(getMainWindow(), createGame(settings.gameType), settings.mapWidth, settings.mapHeight);
				board.setVisible(false);
				pnlMain.remove(board);
				board = new Board(settings.mapWidth, settings.mapHeight, settings.gameType, settings.isQuadState);
				board.setBorder(null);
				pnlMain.add(board);
				board.addMouseListener(board.createMouseListener(presenter));
				presenter.updateView();
			}
		});
		
		cboxStructureFamily.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStructureList();
			}
		});
		
		btnAddStructure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.setAddingNewStructure(true);
			}
		});
	}
	
	private void setStructureComponents() {
		switch (settings.gameType) {
		case GAME_OF_LIFE:
			setStructureComponentsVisibility(true);
			cboxStructureFamily.setModel(new DefaultComboBoxModel<>(InitialStructure.GOLStructure.values()));
			setStructureList();
			break;
		case WIRE_WORLD:
			setStructureComponentsVisibility(true);
			cboxStructureFamily.setModel(new DefaultComboBoxModel<>(InitialStructure.WWStructure.values()));
			setStructureList();
			break;
		case LANGTON_ANT:
		case ONE_DIM:
			setStructureComponentsVisibility(false);
			break;
		default:
			throw new GUIException("Error");
		}
	}
	
	private void setStructureComponentsVisibility(boolean isVisible) {
		lblStructureFamily.setVisible(isVisible);
		cboxStructureFamily.setVisible(isVisible);
		lblStructure.setVisible(isVisible);
		cboxStructure.setVisible(isVisible);
		btnAddStructure.setVisible(isVisible);
	}
	
	private void setStructureList() {
		if (settings.gameType.equals(Game.GAME_OF_LIFE)) {
			switch((InitialStructure.GOLStructure)cboxStructureFamily.getSelectedItem()) {
			case GUN:
				cboxStructure.setModel(new DefaultComboBoxModel<>(InitialStructure.GOLStructure.Gun.values()));
				break;
			case OSCILLATOR:
				cboxStructure.setModel(new DefaultComboBoxModel<>(InitialStructure.GOLStructure.Oscillator.values()));
				break;
			case SPACESHIP:
				cboxStructure.setModel(new DefaultComboBoxModel<>(InitialStructure.GOLStructure.Spaceship.values()));
				break;
			case STILL:
				cboxStructure.setModel(new DefaultComboBoxModel<>(InitialStructure.GOLStructure.Still.values()));
				break;
			default:
				break;
			}
		} else if (settings.gameType.equals(Game.WIRE_WORLD)) {
			switch ((InitialStructure.WWStructure)cboxStructureFamily.getSelectedItem()) {
			case CLOCK:
				cboxStructure.setModel(new DefaultComboBoxModel<>(InitialStructure.WWStructure.Clock.values()));
				break;
			case GATE:
				cboxStructure.setModel(new DefaultComboBoxModel<>(InitialStructure.WWStructure.Gate.values()));
				break;
			case OTHER:
				cboxStructure.setModel(new DefaultComboBoxModel<>(InitialStructure.WWStructure.Other.values()));
				break;
			default:
				break;
			}
		}
	}
	
	private MainWindow getMainWindow() {
		return this;
	}
	
	public Game getGame() {
		return (Game)cboxGameType.getSelectedItem();
	}
	
	public void changeCell(int x, int y, CellState state) {
		board.setCellTo(x, y, state);
		board.repaint();
	}
	
	private void applySettings() {
		applyGameType();
		applyNeighborhoodType();
		applyMapSize();
		applyNeighborhoodRadius();
		applyGOLRules();
		applyODGRules();
		applyWrappedMap();
		applyQuadState();
		
		InitialStructure.isQuadState = settings.isQuadState;
	}
	
	private void applyGameType() {
		settings.gameType = (Game)cboxGameType.getSelectedItem();
	}
	
	private void applyNeighborhoodType() {
		settings.neighborhoodType = (Neighborhood)cboxNeighborsType.getSelectedItem();
	}

	private void applyNeighborhoodRadius() {
		int newRadius = (int)spinNeighborRadius.getValue();
		int minRadius = 1;
		int maxRadius = (settings.mapHeight > settings.mapWidth) ? settings.mapWidth / 2 - 1 : settings.mapHeight / 2 - 1;
		
		if (newRadius < minRadius) {
			settings.neighborhoodRadius = minRadius;
			spinNeighborRadius.setValue(minRadius);
		} else if (newRadius > maxRadius) {
			settings.neighborhoodRadius = maxRadius;
			spinNeighborRadius.setValue(maxRadius);
		} else
			settings.neighborhoodRadius = newRadius;
	}
	
	private void applyMapSize() {
		settings.mapWidth = readDimFromSlider(sliderMapWidth);
		settings.mapHeight = readDimFromSlider(sliderMapHeight);
	}
	
	private int readDimFromSlider(JSlider slider) {
		int newValue = slider.getValue();
		int minValue = slider.getMinimum();
		int maxValue = slider.getMaximum();
		
		if (newValue < minValue) {
			slider.setValue(minValue);
			return minValue;
		} else if (newValue > maxValue) {
			slider.setValue(maxValue);
			return maxValue;
		} else
			return newValue;
	}

	private void applyGOLRules() {
		String text = txtRulesGOL.getText();
		String regex = "^(\\d+(,\\d+)*)?/(\\d+(,\\d+)*)?";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		
		if(!matcher.matches()) {
			txtRulesGOL.setText("2,3/3");
			return;
		}
		
		String[] sustainingLife = matcher.group(1).split(",");
		String[] beginningLife = matcher.group(3).split(",");
		ArrayList<Integer> sl = new ArrayList<>(sustainingLife.length);
		ArrayList<Integer> bl = new ArrayList<>(beginningLife.length);
		
		for (String s : sustainingLife)
			sl.add(Integer.parseInt(s));
		
		for (String s : beginningLife)
			bl.add(Integer.parseInt(s));
		
		settings.rulesGOL = new GameOfLifeRules(sl, bl);
	}

	private void applyODGRules() {
		try {
			int value = (int)spinRulesODG.getValue();
			
			if (value < 0) {
				spinRulesODG.setValue(0);
				settings.rulesODG = new OneDimensionRules(0);
			} else if (value > 255) {
				spinRulesODG.setValue(255);
				settings.rulesODG = new OneDimensionRules(255);
			} else
				settings.rulesODG = new OneDimensionRules(value);
		} catch (Exception e) {
			spinRulesODG.setValue(settings.rulesGOL);
		}
	}

	private void applyWrappedMap() {
		settings.isMapWrapped = chkIsMapWrapped.isSelected();
	}
	
	private void applyQuadState() {
		settings.isQuadState = chkIsQuadState.isSelected();
	}
	
	private Automaton createGame(Game game) {
		switch (game) {
		case GAME_OF_LIFE:
			return createGameOfLife();
		case LANGTON_ANT:
			return createLangtonAnt();
		case ONE_DIM:
			return createOneDimensionGame();
		case WIRE_WORLD:
			return createWireWorld();
		default:
			throw new GUIException("Error");
		}
	}
	
	private Automaton createGameOfLife() {
		CellState state = settings.isQuadState ? QuadState.DEAD : BinaryState.DEAD;
		CellStateFactory factory = new UniformStateFactory(state);
		CellNeighborhood neighborhood;
		
		if (settings.neighborhoodType == Neighborhood.MOORE)
			neighborhood = new MooreNeighborhood(settings.mapWidth, settings.mapHeight, settings.neighborhoodRadius, settings.isMapWrapped);
		else
			neighborhood = new VonNeumanNeighborhood(settings.mapWidth, settings.mapHeight, settings.neighborhoodRadius, settings.isMapWrapped);
		
		return new GameOfLife(factory, neighborhood, settings.rulesGOL, settings.mapWidth, settings.mapHeight, settings.isQuadState);
	}

	private Automaton createLangtonAnt() {
		CellState state = new LangtonCell(BinaryState.DEAD, AntState.NONE);
		CellStateFactory factory = new UniformStateFactory(state);
		CellNeighborhood neighborhood = new MooreNeighborhood(settings.mapWidth, settings.mapHeight, 1, settings.isMapWrapped);

		return new LangtonAnt(factory, neighborhood, settings.mapWidth, settings.mapHeight);
	}

	private Automaton createOneDimensionGame() {
		return new OneDimensionGame(new UniformStateFactory(BinaryState.DEAD),
									new OneDimNeighborhood(settings.mapWidth),
									settings.rulesODG,
									settings.mapWidth);
	}

	private Automaton createWireWorld() {
		return new WireWorld(new UniformStateFactory(WireElectronState.VOID),
							 new MooreNeighborhood(settings.mapWidth, settings.mapHeight, 1, settings.isMapWrapped),
							 settings.mapWidth,
							 settings.mapHeight);
	}

	private void prepareGameSettings(Game game) {
		if (game.equals(Game.GAME_OF_LIFE)) {
			pnlNeighborhood.setVisible(true);
			pnlRulesGOL.setVisible(true);
			pnlRulesODG.setVisible(false);
			chkIsMapWrapped.setVisible(true);
			chkIsQuadState.setVisible(true);
		} else if (game.equals(Game.LANGTON_ANT)) {
			pnlNeighborhood.setVisible(false);
			pnlRulesGOL.setVisible(false);
			pnlRulesODG.setVisible(false);
			chkIsMapWrapped.setVisible(true);
			chkIsQuadState.setVisible(false);
		} else if (game.equals(Game.WIRE_WORLD)) {
			pnlNeighborhood.setVisible(false);
			pnlRulesGOL.setVisible(false);
			pnlRulesODG.setVisible(false);
			chkIsMapWrapped.setVisible(true);
			chkIsQuadState.setVisible(false);
		} else if (game.equals(Game.ONE_DIM)) {
			pnlNeighborhood.setVisible(false);
			pnlRulesGOL.setVisible(false);
			pnlRulesODG.setVisible(true);
			chkIsMapWrapped.setVisible(false);
			chkIsQuadState.setVisible(false);
		}
	}

	public void resizeBoardSizeInCellsTo(int mapWidth, int mapHeight) {
		board.resizeBoardSizeInCellsTo(mapWidth, mapHeight);
	}
	
	public void setNewCellText(CellState state) {
		lblNewCellState.setText(state.toString());
	}
	
	public InitialStructure.Structure getNewStructureType() {
		return (InitialStructure.Structure)cboxStructure.getSelectedItem();
	}
}
