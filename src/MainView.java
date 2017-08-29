
import java.io.*;
//import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.undo.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.Point;




public class MainView extends JPanel implements Observer {

	//private Controller controller;
    private Model model;
	private Rectangle me = new Rectangle(100, 200, 80, 80);
	private boolean is_start = false;
	//private Rectangle obstacle[];
	
	private UndoManager undoManager;

    /*Menu*/
	private JPanel Menu = new JPanel();
	private JButton button_start = new JButton("start"); 
	private JButton button_options = new JButton("options"); 
	private JButton button_about = new JButton("about");

	/*Options*/
	private JPanel Options = new JPanel();
	private JButton button_create = new JButton("create new map");
	private JButton button_load = new JButton("load map");
	private JButton button_others = new JButton("others");

	/*Game*/
	private JPanel Game = new JPanel();
	private GamePanel gp1 = new GamePanel();
	
	public class GamePanel extends JPanel{
		private Rectangle obstacle[];
		
		private AnimationComponent animationComponent;
		
		public void redo(){/*notifyObservers();*/}
		
		void loadmap(String filename){
			int lines = 0;
			//private Rectangle obstacle[];
			String[] words;
			
			try{
				BufferedReader in = new BufferedReader(new FileReader(filename));
				String str;
				str = in.readLine();
				lines = Integer.parseInt(str);
				obstacle= new Rectangle[lines];
				str = in.readLine();
				
				for(int i=0; i<lines; i++){
					str = in.readLine();
					//System.out.println(str);
					words = str.split(" ");
					Rectangle r= new Rectangle(Integer.parseInt(words[0]),
												Integer.parseInt(words[1]),
												Integer.parseInt(words[2]),
												Integer.parseInt(words[3]));
					obstacle[i] = r;
				}
				in.close();
			}catch(IOException e){
				
			}
			animationComponent = new AnimationComponent();
		}
		
		GamePanel(){
			setMaximumSize(new Dimension(1000,900));
			setPreferredSize(new Dimension(1000,900));
			setBackground(Color.blue);
			//animationComponent = new AnimationComponent();
		}
		
		public class AnimationComponent extends JComponent {
			private int FPS = 30;
			private Timer t;

			public AnimationComponent() {
				super();
				
				this.t = new Timer(1000/FPS, new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						if(is_start == true){
					   for(int i=0; i<obstacle.length; i++){
							obstacle[i].x = obstacle[i].x - 3;
							//System.out.println(obstacle[0].x);
						}
						me.x=me.x-3;
						}
						repaint();
					}
				});
				
				this.t.start();

			}
		}			
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			g.fillRect(me.x, me.y, 80, 80);
			Color c = Color.RED;
			g.setColor(c);
			Graphics2D g2d = (Graphics2D)g;
			AffineTransform old = g2d.getTransform();
			g2d.rotate(Math.toRadians(45));
			g2d.setTransform(old);
			
			for(int i=0; i<obstacle.length; i++){
				g.fillRect(obstacle[i].x, obstacle[i].y, obstacle[i].width, obstacle[i].height);
			}
			
			repaint();
		}
	}		

	private JPanel buttons_onGame = new JPanel();
	private JButton button_stop = new JButton("Stop");
	private JButton button_restart = new JButton("Restart");
	private JButton button_play = new JButton("Play");
	private JButton button_exit = new JButton("Exit");
	private JButton button_MainMenu = new JButton("MainMenu");
	
	
	
	
	
	
    public MainView(Model model/*, Controller controller*/) {

		/*Menu*/
		button_start.setMaximumSize(new Dimension(250, 100));
		button_start.setPreferredSize(new Dimension(250, 100));
		button_options.setMaximumSize(new Dimension(250, 100));
		button_options.setPreferredSize(new Dimension(250, 100));
		button_about.setMaximumSize(new Dimension(250, 100));
		button_about.setPreferredSize(new Dimension(250, 100));
		
		Box bo = Box.createVerticalBox();
		bo.add(Box.createVerticalStrut(80));
		bo.add(button_start);
	    bo.add(Box.createVerticalStrut(40));
		bo.add(button_options);
		bo.add(Box.createVerticalStrut(40));
		bo.add(button_about);
		
		Menu.add(bo);

		/*Options*/
		button_create.setMaximumSize(new Dimension(250, 100));
		button_create.setPreferredSize(new Dimension(250, 100));
		button_load.setMaximumSize(new Dimension(250, 100));
		button_load.setPreferredSize(new Dimension(250, 100));
		button_others.setMaximumSize(new Dimension(250, 100));
		button_others.setPreferredSize(new Dimension(250, 100));

		Box boo = Box.createVerticalBox();
		boo.add(Box.createVerticalStrut(80));
		boo.add(button_create);
		boo.add(Box.createVerticalStrut(40));
		boo.add(button_load);
		boo.add(Box.createVerticalStrut(40));
		boo.add(button_others);

		Options.add(boo);
		
		//Game
		buttons_onGame.setMaximumSize(new Dimension(1000, 100));
		buttons_onGame.setPreferredSize(new Dimension(1000, 100));
		
		Box bog = Box.createHorizontalBox();
		bog.add(Box.createHorizontalStrut(50));
		bog.add(button_stop);
		bog.add(Box.createHorizontalStrut(40));
		bog.add(button_restart);
		bog.add(Box.createHorizontalStrut(40));
		bog.add(button_play);
		bog.add(Box.createHorizontalStrut(40));
		bog.add(button_exit);
		bog.add(Box.createHorizontalStrut(40));
		bog.add(button_MainMenu);
		
		buttons_onGame.add(bog);
		Box bog2 = Box.createVerticalBox();
		bog2.add(buttons_onGame);
		bog2.add(gp1);
		Game.add(bog2);




		this.add(Menu);
		
		this.model = model;
		//this.controller = controller;

		button_options.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				remove(Menu);
				add(Options);
				repaint();
				revalidate();
			}
		});
		
		button_start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				remove(Menu);
				add(Game);
				gp1.loadmap("sample_level.txt");
				repaint();
				revalidate();
			}
		});
		
		button_play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				is_start = true;
				/*JUST*/
				setFocusable(true);
				requestFocusInWindow();
				/*Just*/
				repaint();
				revalidate();
			}
		});
		
		button_stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				is_start = false;
				repaint();
				revalidate();
			}
		});
		
		button_restart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				is_start = true;
				/*JUST*/
				setFocusable(true);
				requestFocusInWindow();
				/*Just*/
				gp1.loadmap("sample_level.txt");
				repaint();
				revalidate();				
			}
		});
		
		button_exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				is_start = false;
				System.exit(0);
			}
		});
		
		button_MainMenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				is_start = false;
				remove(Game);
				add(Menu);
				repaint();
				revalidate();
			}
		});
		
		
		
		addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_A){
					me.x = me.x-80;
					repaint();
					revalidate();
				}
				else if(key == KeyEvent.VK_D){
					me.x = me.x+80;
					repaint();
					revalidate();
				}
				else if(key == KeyEvent.VK_W){
					me.y = me.y-80;
					repaint();
					revalidate();
				}
				else if(key == KeyEvent.VK_S){
					me.y = me.y+80;
					repaint();
					revalidate();
				}
			}
		});
		
		
		
        model.addObserver(this);
		
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        //System.out.println("Model changed!");
    }
}
