package oprpp2.hw02.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Prozor za chat kojeg koristi klijentski program.
 * @author mskrabic
 *
 */
public class ChatWindow extends JFrame {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Korisničko ime.
	 */
	private String username;
	
	/**
	 * Polje za unos poruke.
	 */
	private JTextField input;
	
	/**
	 * Polje za prikaz poruka. ("chat history")
	 */
	private JTextArea textArea;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	/**
	 * Konstruktor.
	 * @param username korisničko ime koje se postavlja u naslovnu traku prozora.
	 */
	public ChatWindow(String username) {
		super();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Chat client: " + username);
		this.username = username;
		setSize(500, 200);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChatWindow.this.closeWindow();	
			}	
		});
		
		initGUI();
		
		addMessageListener();
	}
	
	/**
	 * Pomoćna metoda za inicijalizaciju GUI-a.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		input = new JTextField();
		input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
		
		cp.add(input, BorderLayout.PAGE_START);
		cp.add(panel, BorderLayout.CENTER);	
	}

	/**
	 * Pomoćna metoda koja registrira promatrača na polje za unos. Pritiskom tipke ENTER
	 * se generira nova poruka i stavlja u red za slanje.
	 */
	private void addMessageListener() {
		input.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && input.isEditable()) {
					input.setEditable(false);
					Main.generateOutMessage(input.getText());
				}
			}
		});	
	}

	/**
	 * Pomoćna metoda za zatvaranje prozora. Postavlja se zastavica klijentskom programu da zna
	 * da korisnik želi prekinuti komunikaciju.
	 */
	private void closeWindow() {
		Main.setExit();
		dispose();
		
	}
	
	/**
	 * Metoda za dodavanje nove poruke u polje za prikaz poruka.
	 * 
	 * @param sender korisničko ime pošiljatelja.
	 * @param messageText tekst poruke.
	 */
	public void appendMessage(String sender, String messageText) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		this.textArea.append("[" + sdf.format(timestamp) + "] " + sender + ":\n");
		this.textArea.append(messageText + "\n");
		
		if (sender.equals(username)) {
			this.input.setText("");
			this.input.setEditable(true);
		}
		
	}

}
