package game;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main {
	
    public static void main(String[] args) {
		// Initialize the global thread-pool
		ThreadPool.init();
		
		// Show the game menu ...
		
		// After the palyer clicks 'PLAY' ...dd
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Frame frame = new Frame("ThroughTheEssence");
				frame.setLocationRelativeTo(null); // put frame at center of screen
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
				frame.initBufferStrategy();
				// Create and execute the game-loop
				GameLoop game = new GameLoop(frame);
				game.init();
				ThreadPool.execute(game);
				// and the game starts ...
			}
		});
    }
}
