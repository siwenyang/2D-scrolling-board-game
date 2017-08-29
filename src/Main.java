import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;


public class Main {
    public static void main(String[] args) {
		JFrame frame = new JFrame("ESCAPE");


        Model model = new Model();
		
		//Controller controller = new Controller();
		
        MainView mainView = new MainView(model/*, controller*/);
		
		model.addObserver(mainView);
		
		model.notifyObservers();
		

		frame.getContentPane().add(mainView);
		frame.setPreferredSize(new Dimension(1000,1000));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
    }
}
