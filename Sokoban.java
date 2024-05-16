//Name: Anshuman Dhillon
//Date: 02/07/2023  (DD/MM/YYYY)
//Purpose: Creating a 2D sokoban game in java based on the backrooms theme for the CS50x 2023 final project

//Note: This project was developed on the eclipse IDE so some things may not work as expected


//Importing java libraries that will be used throughout the code
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.border.*;

//Class
@SuppressWarnings({ "serial", "unused" })
public class Sokoban extends JPanel implements ActionListener {
	public void addNotify() {
		super.addNotify();
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(new ArrowKeyListener());
	}

	// Declaring global variables

	// This is basically the current layout. It will dynamically change as the
	// character moves.
	int mainLayout[][] = { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };

	// This is a 3D array which contains all of the twelve levels. It will not be
	// changed in the code, but will be used for some things.
	int layouts[][][] = { { {} },

			// Level 1
			{ { 1, 1, 3, 1, 1, 0, 0, 1 }, { 1, 1, 1, 1, 1, 1, 0, 1 }, { 0, 1, 0, 1, 0, 1, 0, 1 },
					{ 0, 1, 1, 1, 1, 1, 1, 1 }, { 0, 1, 0, 1, 2, 1, 1, 1 }, { 0, 1, 1, 1, 1, 1, 1, 1 },
					{ 0, 1, 0, 1, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1, 1, 9, 1 } },

			// Level 2
			{ { 1, 1, 3, 1, 3, 0, 0, 1 }, { 1, 1, 0, 0, 0, 1, 1, 1 }, { 0, 1, 2, 1, 1, 1, 0, 1 },
					{ 0, 1, 1, 1, 1, 0, 1, 1 }, { 0, 1, 0, 1, 1, 0, 2, 0 }, { 0, 9, 1, 1, 1, 1, 1, 1 },
					{ 0, 1, 0, 1, 1, 0, 1, 1 }, { 0, 1, 1, 1, 1, 1, 1, 0 } },

			// Level 3
			{ { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 0, 0 }, { 0, 1, 2, 1, 1, 1, 0, 0 },
					{ 0, 1, 1, 2, 0, 3, 0, 0 }, { 0, 0, 0, 1, 0, 1, 0, 0 }, { 0, 0, 1, 9, 0, 1, 0, 0 },
					{ 0, 0, 1, 1, 1, 1, 1, 1 }, { 0, 0, 0, 3, 3, 1, 2, 1 } },

			// Level 4
			{ { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 1, 0, 0, 0 }, { 0, 1, 0, 2, 1, 0, 0, 0 },
					{ 0, 1, 1, 9, 1, 0, 0, 0 }, { 0, 0, 0, 0, 3, 2, 1, 0 }, { 0, 0, 0, 0, 3, 0, 1, 0 },
					{ 0, 0, 0, 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },

			// Level 5
			{ { 1, 1, 0, 1, 0, 0, 1, 1 }, { 1, 1, 2, 1, 0, 0, 1, 1 }, { 3, 0, 9, 1, 0, 3, 1, 1 },
					{ 0, 0, 0, 1, 0, 0, 1, 0 }, { 0, 1, 3, 1, 0, 0, 1, 0 }, { 0, 1, 1, 1, 2, 1, 2, 0 },
					{ 0, 1, 1, 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },

			// Level 6
			{ { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 1, 1, 0, 0 }, { 0, 3, 9, 2, 1, 1, 0, 0 },
					{ 0, 0, 0, 1, 2, 3, 0, 0 }, { 0, 3, 0, 0, 2, 1, 0, 0 }, { 0, 1, 0, 1, 3, 1, 0, 0 },
					{ 0, 2, 1, 3, 2, 2, 3, 0 }, { 0, 1, 1, 1, 3, 1, 1, 0 } },

			// Level 7
			{ { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 1, 9, 1, 0 }, { 0, 0, 1, 2, 0, 0, 1, 0 },
					{ 0, 1, 1, 1, 2, 1, 3, 0 }, { 0, 1, 1, 0, 0, 2, 3, 0 }, { 0, 1, 1, 1, 1, 3, 1, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },

			// Level 8
			{ { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 3, 1, 2, 3, 3, 0, 0 }, { 0, 3, 2, 1, 2, 9, 0, 0 },
					{ 0, 2, 1, 0, 1, 2, 0, 0 }, { 0, 1, 2, 1, 2, 3, 0, 0 }, { 0, 3, 3, 2, 1, 3, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },

			// Level 9
			{ { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 1, 1, 1, 0 }, { 0, 0, 0, 1, 3, 3, 1, 0 },
					{ 0, 0, 0, 2, 3, 3, 0, 0 }, { 0, 1, 1, 1, 0, 1, 0, 0 }, { 0, 1, 1, 2, 2, 2, 1, 0 },
					{ 0, 1, 9, 1, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },

			// Level 10
			{ { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 3, 1, 1, 0 }, { 0, 1, 2, 1, 3, 1, 1, 0 },
					{ 0, 1, 0, 1, 3, 3, 1, 0 }, { 0, 9, 1, 2, 0, 0, 0, 0 }, { 0, 1, 2, 1, 1, 1, 1, 0 },
					{ 0, 1, 1, 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },

			// Level 11
			{ { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 0, 3, 3, 0 }, { 0, 1, 1, 1, 1, 3, 3, 0 },
					{ 0, 0, 0, 1, 2, 1, 0, 0 }, { 0, 1, 2, 2, 0, 0, 0, 0 }, { 0, 1, 1, 1, 1, 2, 9, 0 },
					{ 0, 1, 1, 1, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },

			// Level 12
			{ { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 9, 1, 0, 0, 0 }, { 0, 0, 2, 0, 2, 1, 1, 0 },
					{ 0, 1, 3, 3, 1, 3, 1, 0 }, { 0, 1, 1, 2, 2, 1, 0, 0 }, { 0, 0, 0, 1, 0, 3, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } } };

	// Row and column, always remains 8x8
	int row = 8;
	int col = 8;

	// X and Y positions, they change
	int x = 4;
	int y = 6;

	// Storing the number of steps character takes
	JLabel steps;
	int stepsNum = 0;

	// An array that contains all of the pictures. This will be the one that changes
	// images when character moves.
	JLabel pics[] = new JLabel[row * col];

	Panel mainPanel; // To hold all of the screens
	CardLayout cdLayout = new CardLayout();

	// Declaring all of the main screens
	JPanel mainMenu, gameboard, instructionsScreen, settingsScreen, winScreen, rulesScreen;
	JCheckBox agree; // Checkbox
	JButton resume;

	// Settings variables
	JSlider musicVolumeSlider;
	JSlider sfxVolumeSlider;

	// Declare a Clip object at class level
	private Clip clip;
	private Clip[] clips = { null, null };

	JTextField name;
	String playerName = "Player";

	// Formatting variables
	Font promptFont = new Font("Segoe Print", Font.PLAIN, 17);
	Font titleFont = new Font("Segoe Print", Font.PLAIN, 30);
	Color titleColour = Color.YELLOW;
	Color backgroundColour = new Color(3, 37, 49);
	Color buttonColour = Color.DARK_GRAY;
	Color buttonText = Color.WHITE;

	// Stores level number
	JLabel level;
	int levelNum = 1;

	JButton next; // Button to move onto the next level
	JButton save; // Button to save the game

	boolean won = false; // Will be used to help out in the code if player won a level or not
	boolean savedSuccess = false; // Changes when game is being saved, used to change the save button on game
									// screen
	JLabel title2;

	// Storing the whole terms of service text in one variable for later use
	String TOS = "Developed by Anshuman Dhillon\r\n" + "\r\n" + "\r\n"
			+ "These terms are FAKE and are only for educational purposes.\r\n" + "\r\n" + "ELIGIBILITY\r\n" + "\r\n"
			+ "     The content provided is rated for everyone and supervision of an adult is not required.\r\n"
			+ "\r\n" + "TERMS OF SERVICE\r\n" + "\r\n"
			+ "     User Content: The service allows the user to communicate with the app only and nothing else.\r\n"
			+ "\r\n" + "TERMS OF USE\r\n" + "   You agree that: \r\n" + "\r\n"
			+ "     You will not ruin the game in any way.\r\n" + "\r\n"
			+ "     You will not try to exploit the game.\r\n" + "\r\n"
			+ "     Third party apps are not allowed and can get you into legal problems.\r\n" + "\r\n"
			+ "PRIVACY POLICY\r\n" + "\r\n"
			+ "     I do not take any information that belongs to you. This may include information such as name, passwords, or information about your device.\r\n"
			+ "\r\n";

	// Constructor
	public Sokoban() {
		// Creating main panel
		mainPanel = new Panel();
		mainPanel.setLayout(cdLayout);

		// Calling all main methods and loading up all the screens
		LoadRulesScreen();
		LoadStartScreen();
		LoadGame();
		LoadInstructionScreen();
		LoadSettingsScreen();
		LoadWinScreen();

		setLayout(new BorderLayout());
		add("Center", mainPanel); // Adding the main panel which contains all of the screens

		// Loads previous data from file. If no data, then start new game.
		Open("file");

		// Starting main background music
		float volume = (float) musicVolumeSlider.getValue() / 100.0f;
		Music("MainMusic.wav", volume);
	}

	// This method adds a changing color effect to a text label
	public void loopedColorEffect(JLabel label, Color from, Color to, int duration) {
		// Set the number of steps that the color transition will take
		int steps = 100;

		// Calculate the delay between each step of the color transition
		int delay = duration / steps;

		// Create a new Timer object that will trigger an action event every 'delay'
		// amount of milliseconds
		Timer timer = new Timer(delay, null);

		// Add an ActionListener to the Timer object
		timer.addActionListener(new ActionListener() {
			// Initialize a counter variable to keep track of the current step in the color
			// transition
			private float i = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				// Calculate the current step's ratio (from 0 to 1) using a sinusoidal function
				float ratio = (float) (0.5 * Math.sin(Math.PI * i / steps) + 0.5);

				// Calculate the current color based on the 'from' and 'to' colors and the
				// current step's ratio
				int red = (int) (from.getRed() * (1 - ratio) + to.getRed() * ratio);
				int green = (int) (from.getGreen() * (1 - ratio) + to.getGreen() * ratio);
				int blue = (int) (from.getBlue() * (1 - ratio) + to.getBlue() * ratio);

				// Set the foreground color of the label to the current color
				label.setForeground(new Color(red, green, blue));

				// Increment the counter variable
				i++;

				// If we have completed all the steps, stop the current timer and start a new
				// one with the 'to' and 'from' colors switched
				if (i > steps) {
					timer.stop();
					loopedColorEffect(label, to, from, duration);
				}
			}
		});

		// Start the timer
		timer.start();
	}

	// Method for the start screen/main menu
	public void LoadStartScreen() {
		// Create main menu
		mainMenu = new JPanel();
		mainMenu.setBackground(new Color(189, 189, 49));

		// Create the background image
		JLabel pic = new JLabel("");
		pic.setIcon(createImageIcon("backroom.png"));
		pic.setLayout(new FlowLayout()); // Make sure it doesn't overlap the other buttons
		pic.setPreferredSize(new Dimension(550, 785));
		mainMenu.add(pic);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(490, 250));
		panel.setOpaque(false);
		pic.add(panel);

		// Adding title
		JLabel title = new JLabel("SOKOBAN");
		title.setOpaque(false);
		title.setFont(new Font("Chiller", Font.BOLD, 65));
		loopedColorEffect(title, new Color(189, 189, 49), new Color(185, 186, 141), 200);
		panel.add(title);

		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(490, 50));
		panel1.setOpaque(false);
		pic.add(panel1);

		// Adding a resume button
		resume = new JButton("Continue");
		resume.setFont(new Font("Chiller", Font.BOLD, 40));
		resume.setPreferredSize(new Dimension(210, 40));
		resume.setBackground(Color.DARK_GRAY);
		resume.setForeground(Color.YELLOW);
		resume.setContentAreaFilled(false);
		resume.setBorderPainted(false);
		resume.addActionListener(this);
		resume.setActionCommand("");
		resume.setVisible(false);
		panel1.add(resume);

		JPanel panel2 = new JPanel();
		panel2.setPreferredSize(new Dimension(490, 50));
		panel2.setOpaque(false);
		pic.add(panel2);

		// Adding new game button
		JButton start = new JButton("NEW GAME");
		start.setFont(new Font("Chiller", Font.BOLD, 40));
		start.setPreferredSize(new Dimension(210, 40));
		start.setBackground(Color.DARK_GRAY);
		start.setForeground(Color.YELLOW);
		start.setContentAreaFilled(false);
		start.setBorderPainted(false);
		start.addActionListener(this);
		start.setActionCommand("Start");
		panel2.add(start);

		JPanel panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(490, 50));
		panel3.setOpaque(false);
		pic.add(panel3);

		// Adding instructions button
		JButton ins = new JButton("Instructions");
		ins.setFont(new Font("Chiller", Font.BOLD, 40));
		ins.setPreferredSize(new Dimension(210, 40));
		ins.setBackground(Color.DARK_GRAY);
		ins.setForeground(Color.YELLOW);
		ins.setContentAreaFilled(false);
		ins.setBorderPainted(false);
		ins.addActionListener(this);
		ins.setActionCommand("Instructions");
		panel3.add(ins);

		JPanel panel4 = new JPanel();
		panel4.setPreferredSize(new Dimension(490, 50));
		panel4.setOpaque(false);
		pic.add(panel4);

		// Adding settings button
		JButton settings = new JButton("Settings");
		settings.setFont(new Font("Chiller", Font.BOLD, 40));
		settings.setPreferredSize(new Dimension(210, 40));
		settings.setBackground(Color.DARK_GRAY);
		settings.setForeground(Color.YELLOW);
		settings.setContentAreaFilled(false);
		settings.setBorderPainted(false);
		settings.addActionListener(this);
		settings.setActionCommand("Settings");
		panel4.add(settings);

		JPanel panel5 = new JPanel();
		panel5.setPreferredSize(new Dimension(490, 250));
		panel5.setOpaque(false);
		pic.add(panel5);

		// Adding quit button
		JButton quit = new JButton("QUIT");
		quit.setFont(new Font("Chiller", Font.BOLD, 40));
		quit.setPreferredSize(new Dimension(210, 40));
		quit.setBackground(Color.DARK_GRAY);
		quit.setForeground(Color.YELLOW);
		quit.setContentAreaFilled(false);
		quit.setBorderPainted(false);
		quit.addActionListener(this);
		quit.setActionCommand("Quit");
		panel5.add(quit);

		JPanel panel6 = new JPanel();
		panel6.setPreferredSize(new Dimension(490, 50));
		panel6.setOpaque(false);
		pic.add(panel6);

		// Adding a credits label
		JLabel credits = new JLabel("Developed by Anshuman Dhillon");
		credits.setFont(new Font("Chiller", Font.ITALIC, 30));
		panel6.add(credits);

		// Adding the whole panel to the main panel
		mainPanel.add("1", mainMenu);
	}

	// Method for loading the game screen and gameboard
	public void LoadGame() {
		gameboard = new JPanel();
		gameboard.setBackground(new Color(185, 186, 141));

		// Calling load layout method, which automatically picks a layout from the 3D
		// array
		LoadLayout();

		// Creating title
		JLabel title = new JLabel("         Sokoban Game         ");
		title.setFont(new Font("Chiller", Font.BOLD, 50));

		// Creating a restart button for the player to use in case they make a mistake
		JButton restart = new JButton("Restart");
		restart.setActionCommand("Restart");
		restart.addActionListener(this);
		restart.setBackground(new Color(145, 145, 110));
		restart.setForeground(Color.WHITE);
		restart.setFont(new Font("Cambria", Font.BOLD, 20));

		// A button to go back to the main menu
		JButton menu = new JButton("Back To Main Menu");
		menu.setActionCommand("Menu");
		menu.addActionListener(this);
		menu.setBackground(new Color(145, 145, 110));
		menu.setForeground(Color.WHITE);
		menu.setFont(new Font("Cambria", Font.BOLD, 25));

		// Creating save game button to save all of your data
		save = new JButton("Save Game");
		save.setActionCommand("Save");
		save.addActionListener(this);
		save.setBackground(new Color(145, 145, 110));
		save.setForeground(Color.WHITE);
		save.setFont(new Font("Cambria", Font.BOLD, 25));

		// Creating label that shows what level you're on
		level = new JLabel("       Level: " + levelNum + "       ");
		level.setFont(new Font("Cambria", Font.BOLD, 20));

		// Creating label that shows how many steps your character has taken
		steps = new JLabel("                   Steps Taken: " + stepsNum + "               ");
		steps.setFont(new Font("Cambria", Font.BOLD, 17));

		// Creating next button to go onto the next level if player has solved the
		// current level
		next = new JButton("Next");
		next.setActionCommand("");
		next.addActionListener(this);
		next.setBackground(new Color(145, 145, 110));
		next.setForeground(Color.LIGHT_GRAY);
		next.setFont(new Font("Cambria", Font.BOLD, 20));

		// Set up grid
		Panel p = new Panel(new GridLayout(row, col));
		int move = 0;

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (mainLayout[i][j] == 9) {
					pics[move] = new JLabel(createImageIcon("b1.png"));
				} else {
					pics[move] = new JLabel(createImageIcon("b" + mainLayout[i][j] + ".png"));
				}

				pics[move].setPreferredSize(new Dimension(63, 63));
				p.add(pics[move]);
				move++;
			}
		}

		// Change the image in the x and y position to the character image facing down
		pics[x * col + y].setIcon(createImageIcon("b4_down.png"));

		// Creating the control buttons, starting with up
		JButton up = new JButton("Up");
		up.setActionCommand("up");
		up.addActionListener(this);
		up.setBackground(new Color(145, 145, 110));
		up.setForeground(Color.WHITE);
		up.setFont(new Font("Cambria", Font.BOLD, 20));

		// Creating down button
		JButton down = new JButton("Down");
		down.setActionCommand("down");
		down.addActionListener(this);
		down.setBackground(new Color(145, 145, 110));
		down.setForeground(Color.WHITE);
		down.setFont(new Font("Cambria", Font.BOLD, 20));

		// Creating right button
		JButton right = new JButton("Right");
		right.setActionCommand("right");
		right.addActionListener(this);
		right.setBackground(new Color(145, 145, 110));
		right.setForeground(Color.WHITE);
		right.setFont(new Font("Cambria", Font.BOLD, 20));

		// Creating left button
		JButton left = new JButton("Left");
		left.setActionCommand("left");
		left.addActionListener(this);
		left.setBackground(new Color(145, 145, 110));
		left.setForeground(Color.WHITE);
		left.setFont(new Font("Cambria", Font.BOLD, 20));

		// Adding some of these buttons and labels to the game screen
		gameboard.add(title);
		gameboard.add(restart);
		gameboard.add(level);
		gameboard.add(next);
		gameboard.add(steps);

		// Adding the actual gameboard
		gameboard.add(p);

		// Creating a panel for the control section
		Panel dir = new Panel(new GridLayout(2, 3));

		// Having some fillers which is just empty space to help with the GUI management
		JLabel filler = new JLabel("");
		JLabel filler2 = new JLabel("");
		JLabel filler3 = new JLabel(
				"                                                                                                       ");
		filler3.setFont(new Font("Cambria", Font.BOLD, 17));

		// Adding control buttons to the panel
		dir.add(filler);
		dir.add(up);
		dir.add(filler2);

		dir.add(left);
		dir.add(down);
		dir.add(right);
		gameboard.add(dir);

		gameboard.add(filler3);

		// Adding save and 'back to main menu' button
		gameboard.add(save);
		gameboard.add(menu);

		// Adding the whole gamescreen panel to the main panel
		mainPanel.add("2", gameboard);
	}

	// Method for loading the instructions screen
	public void LoadInstructionScreen() {
		// Creating the panel for the screen
		instructionsScreen = new JPanel();
		instructionsScreen.setBackground(new Color(189, 189, 49));

		// Adding a back button to ensure player can go back to the main menu
		JButton back = new JButton("Back");
		back.setFont(new Font("Cambria", Font.BOLD, 25));
		back.setPreferredSize(new Dimension(100, 35));
		back.setBackground(new Color(145, 145, 110));
		back.setForeground(Color.WHITE);
		back.addActionListener(this);
		back.setActionCommand("back");
		instructionsScreen.add(back);

		// Create two new JLabels and set their images
		JLabel i1 = new JLabel(createImageIcon("InstructionPage1.png"));
		JLabel i2 = new JLabel(createImageIcon("InstructionPage2.png"));

		// Create a new JPanel for the main content and set its background color and
		// preferred size
		JPanel MainPanel = new JPanel(new GridBagLayout());
		MainPanel.setBackground(Color.YELLOW);
		MainPanel.setPreferredSize(new Dimension(620, 1340));

		// Set up the constraints for the first image label and add it to the main panel
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, -120, 630, 10);
		MainPanel.add(i1, c);

		// Set up the constraints for the second image label and add it to the main
		// panel
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, -380, -700, 10);
		MainPanel.add(i2, c);

		// Create a JScrollPane for the main panel to allow for scrolling, and set its
		// properties
		JScrollPane scrollPane = new JScrollPane(MainPanel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getViewport().setPreferredSize(new Dimension(480, 680));
		scrollPane.setPreferredSize(new Dimension(500, 650));

		instructionsScreen.add(scrollPane);

		// Adding the instructions screen to the main panel
		mainPanel.add("3", instructionsScreen);
	}

	// This method loads the settings screen, which has all the settings for the
	// game that the player can control. It also has some limitations and checks
	// built-in
	// to it to avoid misuse.
	public void LoadSettingsScreen() {
		settingsScreen = new JPanel();
		settingsScreen.setBackground(backgroundColour);

		// Create the title for the settings screen
		JLabel title = new JLabel("Manage Settings");
		title.setFont(titleFont);
		title.setForeground(titleColour);

		JPanel p = new JPanel();
		p.setOpaque(false);

		// Making a prompt for player 1's name
		JLabel namePmt = new JLabel("Player Name:");
		namePmt.setForeground(Color.YELLOW);
		namePmt.setFont(promptFont);

		name = new JTextField(10);
		name.setFont(promptFont);

		// Making the apply button that the player can use to apply their changes
		JButton apply = new JButton("APPLY  SETTINGS");
		apply.setActionCommand("Apply");
		apply.addActionListener(this);
		apply.setPreferredSize(new Dimension(150, 30));
		apply.setFont(new Font("Impact", Font.PLAIN, 17));
		apply.setBackground(buttonColour);
		apply.setForeground(buttonText);

		JPanel p1 = new JPanel();
		p1.setOpaque(false);

		// Making a slider bar for the background music volume
		JLabel volume = new JLabel("Music Volume:");
		volume.setForeground(Color.YELLOW);
		volume.setFont(promptFont);

		musicVolumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		musicVolumeSlider.setMajorTickSpacing(10);
		musicVolumeSlider.setMinorTickSpacing(5);
		musicVolumeSlider.setPaintTicks(true);
		musicVolumeSlider.setPaintLabels(true);
		musicVolumeSlider.setOpaque(false);
		musicVolumeSlider.setForeground(Color.WHITE);
		musicVolumeSlider.setPreferredSize(new Dimension(260, 40));

		JPanel p2 = new JPanel();
		p2.setOpaque(false);

		// Making a slider bar for the SFX volume
		JLabel volume2 = new JLabel("SFX Volume: ");
		volume2.setForeground(Color.YELLOW);
		volume2.setFont(promptFont);

		sfxVolumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		sfxVolumeSlider.setMajorTickSpacing(10);
		sfxVolumeSlider.setMinorTickSpacing(5);
		sfxVolumeSlider.setPaintTicks(true);
		sfxVolumeSlider.setPaintLabels(true);
		sfxVolumeSlider.setOpaque(false);
		sfxVolumeSlider.setForeground(Color.WHITE);
		sfxVolumeSlider.setPreferredSize(new Dimension(260, 40));

		// Adding some extra space
		JPanel blank_p = new JPanel();
		blank_p.setOpaque(false);
		JLabel blank = new JLabel("");
		blank.setOpaque(false);
		blank.setPreferredSize(new Dimension(400, 50));

		// Adding the title and all the other panels
		settingsScreen.add(title);

		p.add(namePmt);
		p.add(name);

		p1.add(volume);
		p1.add(musicVolumeSlider);

		p2.add(volume2);
		p2.add(sfxVolumeSlider);

		blank_p.add(blank);

		settingsScreen.add(p);
		settingsScreen.add(p1);
		settingsScreen.add(p2);
		settingsScreen.add(blank_p);
		settingsScreen.add(apply);

		mainPanel.add("4", settingsScreen);
	}

	// Method for loading the win screen (when all eight levels are completed)
	public void LoadWinScreen() {
		// Creating the win screen panel
		winScreen = new JPanel();
		winScreen.setBackground(new Color(189, 189, 49));

		// Creating background image for the win screen
		JLabel pic = new JLabel("");
		pic.setIcon(createImageIcon("WinScreen.jpg"));
		pic.setLayout(new FlowLayout());
		pic.setPreferredSize(new Dimension(530, 780));
		winScreen.add(pic);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(490, 140));
		panel.setOpaque(false);
		pic.add(panel);

		// Creating title
		JLabel title = new JLabel("        YOU WON!        ");
		title.setFont(new Font("Chiller", Font.BOLD, 80));
		title.setForeground(Color.YELLOW);
		panel.add(title);

		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(490, 400));
		panel1.setOpaque(false);
		pic.add(panel1);

		title2 = new JLabel("Good work, " + playerName + "!");
		title2.setFont(new Font("Chiller", Font.BOLD, 60));
		title2.setForeground(Color.YELLOW);
		panel1.add(title2);

		JPanel panel2 = new JPanel();
		panel2.setPreferredSize(new Dimension(490, 50));
		panel2.setOpaque(false);
		pic.add(panel2);

		// Creating a 'back to main menu' button so that player can go back to the main
		// menu
		JButton back = new JButton("Back To Main Menu");
		back.setFont(new Font("Chiller", Font.BOLD, 40));
		back.setPreferredSize(new Dimension(300, 40));
		back.setBackground(Color.DARK_GRAY);
		back.setForeground(Color.YELLOW);
		back.setContentAreaFilled(false);
		back.setBorderPainted(false);
		back.addActionListener(this);
		back.setActionCommand("back");
		panel2.add(back);

		JPanel panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(490, 50));
		panel3.setOpaque(false);
		pic.add(panel3);

		// Creating a quit button in case player wants to quit after finishing the game
		JButton quit = new JButton("QUIT");
		quit.setFont(new Font("Chiller", Font.BOLD, 40));
		quit.setPreferredSize(new Dimension(210, 40));
		quit.setBackground(Color.DARK_GRAY);
		quit.setForeground(Color.YELLOW);
		quit.setContentAreaFilled(false);
		quit.setBorderPainted(false);
		quit.addActionListener(this);
		quit.setActionCommand("Quit");
		panel3.add(quit);

		// Adding the win screen to the main panel
		mainPanel.add("5", winScreen);
	}

	// Method for loading the rules/TOS screen
	public void LoadRulesScreen() {
		// Creating the rules screen panel
		rulesScreen = new JPanel();
		rulesScreen.setBackground(new Color(245, 245, 144));

		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(490, 38));
		p.setOpaque(false);
		rulesScreen.add(p);

		// Adding main heading
		JLabel heading = new JLabel("TERMS OF SERVICE AND POLICIES");
		heading.setFont(new Font("Cambria", Font.BOLD, 26));
		p.add(heading);

		JPanel p2 = new JPanel();
		p2.setPreferredSize(new Dimension(490, 38));
		p2.setOpaque(false);
		rulesScreen.add(p2);

		// Adding subheading
		JLabel heading2 = new JLabel("PLEASE READ AND AGREE TO PLAY");
		heading2.setFont(new Font("Cambria", Font.BOLD, 22));
		p2.add(heading2);

		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(490, 610));
		panel1.setOpaque(false);
		rulesScreen.add(panel1);

		// Creating a JTextArea and making it non-editable so that it serves the purpose
		// of a label but also fits all that text at once
		JTextArea text = new JTextArea(TOS);
		text.setEditable(false);
		text.setLineWrap(true);
		text.setFont(new Font("Cambria", Font.PLAIN, 14));
		text.setPreferredSize(new Dimension(480, 610));
		panel1.add(text);

		JPanel panel2 = new JPanel();
		panel2.setPreferredSize(new Dimension(490, 30));
		panel2.setOpaque(false);
		rulesScreen.add(panel2);

		agree = new JCheckBox("I agree"); // Creating a check box so that player can agree after reading the TOS
		agree.setActionCommand("agree");
		agree.addActionListener(this);
		agree.setOpaque(false);
		panel2.add(agree);

		JPanel panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(490, 40));
		panel3.setOpaque(false);
		rulesScreen.add(panel3);

		// Creating play button
		JButton play = new JButton("PLAY");
		play.setFont(new Font("Cambria", Font.BOLD, 20));
		play.setPreferredSize(new Dimension(250, 24));
		play.setBackground(Color.DARK_GRAY);
		play.setForeground(Color.WHITE);
		play.addActionListener(this);
		play.setActionCommand("Play");
		panel3.add(play);

		// Adding rules screen to the main panel
		mainPanel.add("6", rulesScreen);
	}

	// Main method that loads and runs the background music of the game
	public void Music(String filepath, float volume) {
		// Get the current working directory
		String mainPath = System.getProperty("user.dir");

		try {
			// Create a File object from the given file path
			File musicPath = new File(mainPath + "\\" + filepath);

			// Check if the file exists
			if (musicPath.exists()) {
				// Create an AudioInputStream object from the file
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);

				// Check if the clip is null
				if (clip == null) {
					// Create a new Clip object and open the audio input
					clip = AudioSystem.getClip();
					clip.open(audioInput);
					// Set the clip to loop continuously
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				}

				// Set the volume of the clip using a FloatControl object
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
				gainControl.setValue(dB);
			} else {
				System.out.println("Can't find file!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Method that runs the button click SFX or win SFX of the game
	public void playSFX(String filepath, float volume, int clipIndex) {
		// Get the current working directory to access the file
		String mainPath = System.getProperty("user.dir");

		try {
			// Create a File object from the filepath
			File sfxPath = new File(mainPath + "\\" + filepath);

			// Check if the file exists
			if (sfxPath.exists()) {
				// Get an AudioInputStream object from the file
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(sfxPath);

				// Check if the clip at the specified index is null
				if (clips[clipIndex - 1] == null) {
					// Create a new Clip object and open the AudioInputStream
					clips[clipIndex - 1] = AudioSystem.getClip();
					clips[clipIndex - 1].open(audioInput);
				} else {
					// Stop the clip and reset its position to the beginning
					clips[clipIndex - 1].stop();
					clips[clipIndex - 1].setFramePosition(0);
				}
				// Start playing the clip
				clips[clipIndex - 1].start();

				// Set the volume of the clip using a FloatControl object
				FloatControl gainControl = (FloatControl) clips[clipIndex - 1]
						.getControl(FloatControl.Type.MASTER_GAIN);

				float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
				gainControl.setValue(dB);
			} else {
				System.out.println("Can't find file!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Method for setting the current layout to a layout from the 3D array
	public void setLayout(int num) // 1 parameter, which is an integer
	{
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				mainLayout[i][j] = layouts[num][i][j];

				// This is specially for level 7
				if (num == 7) { // If it's level 7..
					if (i == 6 && j == 3) { // And the positions are 6 and 3
						mainLayout[i][j] = 2; // Replace it with a box
					}
				}
			}
		}
	}

	// Method for loading the layout
	public void LoadLayout() {
		// Calls the set layout method
		setLayout(levelNum);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (mainLayout[i][j] == 9) { // Also changes the x and y positions as soon as the layout changes
					x = i;
					y = j;
				}
			}
		}
	}

	// Method which simply returns a layout from the 3D array based on the current
	// level the player is on
	public int[][] GetLayout() {
		return layouts[levelNum]; // The only method which returns an integer array
	}

	// MOVEMENT METHODS

	// Method for moving up on the gameboard
	public void moveUp() {
		if (x - 1 < 0) // If x is less than 0, then it means you're going to go off the screen
			System.out.println("Off the board!!");
		else if (mainLayout[x - 1][y] == 0) // If the image above is b0.png, then it means there is a wall
			System.out.println("Wall!");
		else if (mainLayout[x - 1][y] == 2) { // If the image above is b2.png, then it means that there is a box
			System.out.println("Box in the way");

			if (!(x - 2 < 0 || mainLayout[x - 2][y] == 0 || mainLayout[x - 2][y] == 2)) { // If there is no box 2 tiles
																							// above, or there is no
																							// wall 2 tiles above, or
																							// you won't go off the
																							// screen 2 tiles above,
																							// then..
				if (GetLayout()[x][y] == 3) { // If there is a cross there, it will be replaced with a cross again once
												// the player moves away
					pics[x * col + y].setIcon(createImageIcon("b3.png"));
				} else {
					pics[x * col + y].setIcon(createImageIcon("b1.png"));
				}
				x--;
				pics[x * col + y].setIcon(createImageIcon("b4_up.png"));

				System.out.println("Pushing box");

				stepsNum++; // Increase number of steps and update the steps taken label
				steps.setText("                   Steps Taken: " + stepsNum + "               ");

				// Character is now basically pushing the box up
				mainLayout[x - 1][y] = 2;
				mainLayout[x][y] = 1;
				pics[(x - 1) * col + y].setIcon(createImageIcon("b2.png"));
			}
		} else { // Otherwise, just move up normally
			System.out.println("OK! Moving up.");

			stepsNum++;
			steps.setText("                   Steps Taken: " + stepsNum + "               ");

			// If there is a cross there, it will be replaced with a cross again once the
			// player moves away
			if (GetLayout()[x][y] == 3) {
				pics[x * col + y].setIcon(createImageIcon("b3.png"));
			} else {
				pics[x * col + y].setIcon(createImageIcon("b1.png"));
			}
			x--;
			pics[x * col + y].setIcon(createImageIcon("b4_up.png")); // Just move the character up
		}
	}

	// Method for moving down on the gameboard
	public void moveDown() {
		if (x + 1 >= row)
			System.out.println("Off the board!!");
		else if (mainLayout[x + 1][y] == 0)
			System.out.println("Wall!");
		else if (mainLayout[x + 1][y] == 2) {
			System.out.println("Box in the way");

			if (!(x + 2 >= row || mainLayout[x + 2][y] == 0 || mainLayout[x + 2][y] == 2)) {
				if (GetLayout()[x][y] == 3) {
					pics[x * col + y].setIcon(createImageIcon("b3.png"));
				} else {
					pics[x * col + y].setIcon(createImageIcon("b1.png"));
				}
				x++;
				pics[x * col + y].setIcon(createImageIcon("b4_down.png"));

				System.out.println("Pushing box");

				stepsNum++;
				steps.setText("                   Steps Taken: " + stepsNum + "               ");

				mainLayout[x + 1][y] = 2;
				mainLayout[x][y] = 1;
				pics[(x + 1) * col + y].setIcon(createImageIcon("b2.png"));
			}
		} else {
			System.out.println("OK! Moving down.");

			stepsNum++;
			steps.setText("                   Steps Taken: " + stepsNum + "               ");

			if (GetLayout()[x][y] == 3) {
				pics[x * col + y].setIcon(createImageIcon("b3.png"));
			} else {
				pics[x * col + y].setIcon(createImageIcon("b1.png"));
			}
			x++;
			pics[x * col + y].setIcon(createImageIcon("b4_down.png"));
		}
	}

	// Method for moving left on the gameboard
	public void moveLeft() {
		if (y - 1 < 0)
			System.out.println("Off the board!!");
		else if (mainLayout[x][y - 1] == 0)
			System.out.println("Wall!");
		else if (mainLayout[x][y - 1] == 2) {
			System.out.println("Box in the way");

			if (!(y - 2 < 0 || mainLayout[x][y - 2] == 0 || mainLayout[x][y - 2] == 2)) {
				if (GetLayout()[x][y] == 3) {
					pics[x * col + y].setIcon(createImageIcon("b3.png"));
				} else {
					pics[x * col + y].setIcon(createImageIcon("b1.png"));
				}
				y--;
				pics[x * col + y].setIcon(createImageIcon("b4_left.png"));

				System.out.println("Pushing box");

				stepsNum++;
				steps.setText("                   Steps Taken: " + stepsNum + "               ");

				mainLayout[x][y - 1] = 2;
				mainLayout[x][y] = 1;
				pics[x * col + (y - 1)].setIcon(createImageIcon("b2.png"));
			}
		} else {
			System.out.println("OK! Moving left.");

			stepsNum++;
			steps.setText("                   Steps Taken: " + stepsNum + "               ");

			if (GetLayout()[x][y] == 3) {
				pics[x * col + y].setIcon(createImageIcon("b3.png"));
			} else {
				pics[x * col + y].setIcon(createImageIcon("b1.png"));
			}
			y--;
			pics[x * col + y].setIcon(createImageIcon("b4_left.png"));
		}
	}

	// Method for moving right on the gameboard
	public void moveRight() {
		if (y + 1 >= col)
			System.out.println("Off the board!!");
		else if (mainLayout[x][y + 1] == 0)
			System.out.println("Wall!");
		else if (mainLayout[x][y + 1] == 2) {
			System.out.println("Box in the way");

			if (!(y + 2 >= col || mainLayout[x][y + 2] == 0 || mainLayout[x][y + 2] == 2)) {
				if (GetLayout()[x][y] == 3) {
					pics[x * col + y].setIcon(createImageIcon("b3.png"));
				} else {
					pics[x * col + y].setIcon(createImageIcon("b1.png"));
				}
				y++;
				pics[x * col + y].setIcon(createImageIcon("b4_right.png"));

				System.out.println("Pushing box");

				stepsNum++;
				steps.setText("                   Steps Taken: " + stepsNum + "               ");

				mainLayout[x][y + 1] = 2;
				mainLayout[x][y] = 1;
				pics[x * col + (y + 1)].setIcon(createImageIcon("b2.png"));
			}
		} else {
			System.out.println("OK! Moving right.");

			stepsNum++;
			steps.setText("                   Steps Taken: " + stepsNum + "               ");

			if (GetLayout()[x][y] == 3) {
				pics[x * col + y].setIcon(createImageIcon("b3.png"));
			} else {
				pics[x * col + y].setIcon(createImageIcon("b1.png"));
			}
			y++;
			pics[x * col + y].setIcon(createImageIcon("b4_right.png"));
		}
	}

	// Method for updating the gameboard
	public void updateGameboard() {
		int move = 0;

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				// The current layout changes, but player does not see the change, so all the
				// images in the grid need to be updated
				if (mainLayout[i][j] == 9) {
					pics[move].setIcon(createImageIcon("b1.png"));
				} else {
					pics[move].setIcon(createImageIcon("b" + mainLayout[i][j] + ".png"));
				}

				move++;
			}
		}

		// Taking the position and changing the image to the character's image
		pics[x * col + y].setIcon(createImageIcon("b4_down.png"));
	}

	// Method for restarting/resetting the gameboard
	public void restart() {
		// Calling the load layout method
		LoadLayout();

		// Printing restarted (for testing)
		System.out.println("Restarted!");

		// Changing the number of steps to 0 and update the label
		stepsNum = 0;
		steps.setText("                   Steps Taken: " + stepsNum + "               ");

		// Call the update gameboard method to update the grid
		updateGameboard();
	}

	// Method that changes if player won every time the character moves
	public void checkIfWon() {
		// First of all, it sets the won variable to true
		won = true;

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (mainLayout[i][j] != 2 && layouts[levelNum][i][j] == 3) {
					won = false; // Then it checks if the positions of the box images from the current layout are
									// the same as the cross images from the other same layout in the 3D array
					// If not, then change it to false
				}
			}
		}

		// This will only run if won remained as true even after the loop ran
		if (won == true) {
			System.out.println("Player won!");

			// If player won, make sure next can be clicked
			next.setForeground(Color.WHITE);
			next.setActionCommand("Next");
		}
	}

	// Method for changing the level, only runs when the next button on the game
	// screen is clicked
	public void changeLevel() {
		// Changing the number of steps to 0 and update the label
		stepsNum = 0;
		steps.setText("                   Steps Taken: " + stepsNum + "               ");

		// If it's not the final level then..
		if (levelNum != (layouts.length - 1)) {
			levelNum++; // Add the level number

			won = false; // Set won to false
			level.setText("       Level: " + levelNum + "       "); // Update level label

			// Change the next button to make sure it cannot be clicked
			next.setForeground(Color.LIGHT_GRAY);
			next.setActionCommand("");

			// Update the current layout and then the gameboard
			LoadLayout();
			updateGameboard();
		} else { // Otherwise if it's the final level then..
			resume.setActionCommand("");
			resume.setVisible(false); // Make sure the resume button is invisible

			next.setForeground(Color.LIGHT_GRAY); // Changing the next button to make sure it cannot be clicked
			next.setActionCommand("");

			stepsNum = 0;

			// Show the win screen
			cdLayout.show(mainPanel, "5");
		}
	}

	// Method that runs whenever a new game begins
	public void onStart() {
		levelNum = 1; // Set the level number to 1 when a new game begins

		won = false;
		level.setText("       Level: " + levelNum + "       ");

		stepsNum = 0;
		steps.setText("                   Steps Taken: " + stepsNum + "               ");

		// Changing the next button to make sure it cannot be clicked
		next.setForeground(Color.LIGHT_GRAY);
		next.setActionCommand("");

		// Update the current layout and then the gameboard
		LoadLayout();
		updateGameboard();

		// Make the resume button visible
		resume.setVisible(true);
		resume.setActionCommand("Resume");

		// Show the game screen
		cdLayout.show(mainPanel, "2");
	}

	// This method runs once the player clicks on the apply settings button.
	// As the name suggests, it applies every setting that the player chose.
	public void applySettings() {
		// Creating a variable for name length limit
		int nameLimit = 9;

		// Getting the volume from the music volume slider and applying it to the
		// background music immediately
		float volume = (float) musicVolumeSlider.getValue() / 100.0f;
		Music("MainMusic.wav", volume);

		if (name.getText().length() > nameLimit) {
			JOptionPane.showMessageDialog(null,
					"Both player names are too long. Must be equal to or less than " + nameLimit + " characters.",
					"Error", JOptionPane.ERROR_MESSAGE);

			name.setText("");
		} else {
			playerName = name.getText();
			title2.setText("Good work, " + playerName + "!");
		}

		// Show the game screen
		cdLayout.show(mainPanel, "1");
	}

	// Method for saving data to a file in the project directory
	public void Save(String filename, boolean quitting) { // 2 parameters, filename as string and a boolean (to indicate
															// if player is quitting or not)
		PrintWriter out;

		try {
			out = new PrintWriter(new FileWriter(filename));

			out.println(levelNum); // Write the level number in the file as that is what will be saved

			out.println(playerName);

			//Saving the volumes
			float volume = (float) musicVolumeSlider.getValue() / 100.0f;
			float volume2 = (float) sfxVolumeSlider.getValue() / 100.0f;

			out.println(volume);
			out.println(volume2);

			savedSuccess = true; // Set this variable to true to indicate that it worked

			out.close();
		}

		catch (IOException e) {
			System.out.println("Error opening file " + e);
			savedSuccess = false; // If there is an error, print it and set savedSuccess to false
		}

		// If player is not quitting, then show the save button animation
		if (quitting == false) {
			updateSaveButton(); // Calling the updateSaveButton method
		}
	}

	// Method for updating the save button
	public void updateSaveButton() {
		System.out.println("Saving..");

		// Make sure the save button cannot be clicked while the button is changing
		save.setActionCommand("");
		save.setBackground(new Color(99, 99, 76));
		save.setText("Saving..");

		// Using a timer as thread.sleep causes issues with GUI
		Timer timer = new Timer(2000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (savedSuccess == true) { // If the actual saving process was a success, then..
					save.setText("Saved!");
				} else { // Otherwise if there was an issue then..
					save.setText("Error");
				}
			}
		});

		timer.setRepeats(false); // Make sure timer only runs once
		timer.start();

		Timer timer2 = new Timer(3000, new ActionListener() { // Another timer for setting the save button back to its
																// original form

			@Override
			public void actionPerformed(ActionEvent e) {
				save.setText("Save Game");
				save.setBackground(new Color(145, 145, 110));
				save.setActionCommand("Save");

				System.out.println("Saved!");
				savedSuccess = false; // Setting the variable to false
			}
		});

		timer2.setRepeats(false);
		timer2.start();
	}

	// Method for loading the data from the file
	public void Open(String filename) {
	    BufferedReader in = null;
	    float musicVolume = 50.0f;
	    float sfxVolume = 50.0f;

	    try {
	        in = new BufferedReader(new FileReader(filename));

	        String input = in.readLine();
	        if (input != null) {
	        	if (!(input.equals(""))) {
	        		levelNum = Integer.parseInt(input); // Setting the level number to the number saved in the file

	        		// Update the current layout and gameboard
	    	        if (levelNum != 0) {
	    	            LoadLayout();
	    	            updateGameboard();
	    	            won = false; // Set won to false
	    	            level.setText("       Level: " + levelNum + "       "); // Update the level label
	    	            resume.setVisible(true); // Show the continue button because that means there is data in the file
	    	            resume.setActionCommand("Resume");
	    	        }
	        	}
	        }

	        // Read the player name
	        input = in.readLine();
	        if (input != null) {
	        	if (!(input.equals(""))) {
	        		playerName = input; // Set the player name
	            	title2.setText("Good work, " + playerName + "!");
	        	}
	        }

	        // Read the music volume
	        input = in.readLine();
	        if (input != null) {
	            musicVolume = Float.parseFloat(input);
	        }

	        // Read the sound effects volume
	        input = in.readLine();
	        if (input != null) {
	            sfxVolume = Float.parseFloat(input);
	        }

	        in.close();
	    } catch (IOException e) {
	        System.out.println("Error opening file " + e); // If there was an error, print it
	    }

	    // Output the loaded data
	    System.out.println("Data loaded: " + levelNum + ", " + playerName + ", " + musicVolume + ", " + sfxVolume);
	    // Assuming musicVolumeSlider and sfxVolumeSlider are variables accessible in this scope
	    musicVolumeSlider.setValue((int) (musicVolume * 100));
	    sfxVolumeSlider.setValue((int) (sfxVolume * 100));
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand(); // Get the action command and save it in the cmd variable

		// actionPerformed only fires when a button is pressed, so play the button sfx
		float volume = (float) sfxVolumeSlider.getValue() / 100.0f;
		playSFX("ClickSFX.wav", volume, 2);

		if (cmd != null) { // Check to see if the command is not null
			if (cmd.equals("Start")) { // If command equals to start then..
				onStart(); // Call the onStart method and start a new game
			} else if (cmd.equals("Quit")) {// If command equals to quit then..
				Save("file", true); // Save data
				System.exit(0); // Exit the program
			} else if (cmd.equals("Resume")) {// If command equals to resume then..
				cdLayout.show(mainPanel, "2");// Show the game screen
			} else if (cmd.equals("Restart")) {// If command equals to restart then..
				restart();// Call the restart method
			} else if (cmd.equals("Next")) {// If command equals to next then..
				changeLevel();// Call the changeLevel method and move onto the next level
			} else if (cmd.equals("Menu") || cmd.equals("back")) {// If command equals to Menu or back then..
				cdLayout.show(mainPanel, "1");// Show the main menu screen
			} else if (cmd.equals("Instructions")) {// If command equals to instructions then..
				cdLayout.show(mainPanel, "3");// Show the instructions screen
			} else if (cmd.equals("Settings")) {// If command equals to settings then..
				cdLayout.show(mainPanel, "4");// Show the settings screen
			} else if (cmd.equals("Apply")) {// If command equals to settings then..
				applySettings();
			} else if (cmd.equals("Save")) {// If command equals to save then..
				Save("file", false);// Save the data
			} else if (cmd.equals("Play")) {// If command equals to play then..
				if (agree.isSelected() == true) {// If player checked the checkbox and also wrote the password correctly then..
					cdLayout.show(mainPanel, "1");// Show the main menu
				}
			} else if (e.getActionCommand().equals("up")) {// If command equals to up then..
				moveUp(); // Call the move up method and check if player won
				checkIfWon();
			} else if (e.getActionCommand().equals("down")) {// If command equals to down then..
				moveDown(); // Call the move down method and check if player won
				checkIfWon();
			} else if (e.getActionCommand().equals("left")) {// If command equals to left then..
				moveLeft(); // Call the move left method and check if player won
				checkIfWon();
			} else if (e.getActionCommand().equals("right")) {// If command equals to right then..
				moveRight(); // Call the move right method and check if player won
				checkIfWon();
			}
		}

		requestFocusInWindow(); // This ensures that keybinds continue to work after a button is clicked
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Sokoban.class.getResource(path);

		if (imgURL != null)
			return new ImageIcon(imgURL);
		else
			System.err.println("Couldn't find file: " + path);
		return null;
	}

	private class ArrowKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				moveUp(); // Call the move up method and check if player won
				checkIfWon();
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				moveDown(); // Call the move down method and check if player won
				checkIfWon();
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				moveLeft(); // Call the move left method and check if player won
				checkIfWon();
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				moveRight(); // Call the move right method and check if player won
				checkIfWon();
			} else if (e.getKeyCode() == KeyEvent.VK_X) {
				//changeLevel();  This was only added for development purposes and may have unexpected effects on gameplay. Remove the comment at your own risk.
			}
		}
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("Sokoban");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

		// Create and set up the content pane.
		JComponent newContentPane = new Sokoban();

		newContentPane.setOpaque(true);

		frame.setContentPane(newContentPane);

		// Setting the size of the frame
		frame.setSize(520, 820);

		// Setting the frame to visible
		frame.setVisible(true);

		// frame.getContentPane().setBackground();
	}
}