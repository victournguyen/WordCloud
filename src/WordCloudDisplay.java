import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;

//made by victor
//class for displaying word cloud

public class WordCloudDisplay extends JPanel {
	
	public static final long serialVersionUID = 294829847492374L;
	
	public WordCloud cloud;
	public Map<String, Integer> orient;
	public Map<String, Color> colors;
	public List<Rectangle> done;
	public int max_font_size;
	
	public int WIDTH, HEIGHT;
	public String title;
	
	//sets given parameters and instantiates class variables
	public WordCloudDisplay(WordCloud cloud, int width, int height, int max_font_size) {
		this.cloud = cloud;
		WIDTH = width;
		HEIGHT = height;
		this.max_font_size = max_font_size;
		orient = new HashMap<String, Integer>();
		colors = new HashMap<String, Color>();
		done = new ArrayList<Rectangle>();
	}
	
	//sets given parameters and instantiates class variables
	public WordCloudDisplay(WordCloud cloud, int width, int height, int max_font_size, String title) {
		this.cloud = cloud;
		WIDTH = width;
		HEIGHT = height;
		this.max_font_size = max_font_size;
		this.title = title;
		orient = new HashMap<String, Integer>();
		colors = new HashMap<String, Color>();
		done = new ArrayList<Rectangle>();
		//System.out.println(cloud.weights.size());
	}
	
	//paints the words onto the panel
	@Override
	public void paint(Graphics window) {
		done = new ArrayList<Rectangle>();
		Graphics2D w2 = (Graphics2D) window;
		//for each entry, calculates the font size to use and rectangle bounds of the word using FontMetrics
		for(Entry<String, Integer> ent : cloud.entries) {
			String key = ent.getKey();
			int weight = ent.getValue();
			int font_size_to_use = (int) (max_font_size * ((double) weight / cloud.max_weight));
			w2.setColor(Color.WHITE);
			w2.setFont(new Font("Comic Sans MS", Font.PLAIN, font_size_to_use));
			FontMetrics fm = window.getFontMetrics();
			//calculates the bounds of the rectangle
			int text_width = fm.stringWidth(key), text_height = fm.getAscent() - fm.getDescent();
			//accounts for words that are tall
			if (key.matches(".*[gjpqy].*") && key.matches(".*[bdfhijklt].*"))
				text_height = fm.getAscent();
			else if (!key.matches(".*[gjpqy].*") && !key.matches(".*[bdfhijklt].*"))
				text_height -= fm.getDescent();
			//selects position of word by finding the minimum distance from the center that does not intersect another word
			double min_distance = Double.MAX_VALUE;
			Rectangle choose = new Rectangle();
			//selects orientation of word and stores for repainting
			int random = orient.containsKey(key) ? orient.get(key) : (int) (Math.random() * 3);
			if (!orient.containsKey(key))
				orient.put(key, random);
			for (int x = 0; x < WIDTH; x++) {
				middle: for (int y = 0; y < HEIGHT; y++) {
					Rectangle possible = random == 0 ? new Rectangle(x, y, text_width, text_height) : new Rectangle(x, y, text_height, text_width);
					for (Rectangle r : done) {
						if (r.intersects(possible))
							continue middle;
					}
					double center_x = possible.getCenterX(), center_y = possible.getCenterY();
					double mid_x = WIDTH / 2.0, mid_y = HEIGHT / 2.0;
					double distance = Math.sqrt((center_x - mid_x) * (center_x - mid_x) + (center_y - mid_y) * (center_y - mid_y));
					if (distance < min_distance) {
						min_distance = distance;
						choose = possible;
					}
				}
			}
			done.add(choose);
			//color based on weight
			if (!colors.containsKey(key))
				colors.put(key, new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
			w2.setColor(colors.get(key));
			int adjust_descent = key.matches(".*[gjpqy].*") ? fm.getDescent() : 0;
			//paints words differently based on orientation
			if (random == 0) {
				w2.drawString(key, choose.x, choose.y + text_height - adjust_descent);
			}
			else if (random == 1) {
				AffineTransform transform = new AffineTransform();
				transform.rotate(Math.toRadians(90), choose.x, choose.y);
				transform.translate(0, -text_height);
				AffineTransform old = w2.getTransform();
				w2.transform(transform);
				w2.drawString(key, choose.x, choose.y + text_height - adjust_descent);
				w2.setTransform(old);
			}
			else { //random == 2
				AffineTransform transform = new AffineTransform();
				transform.rotate(Math.toRadians(-90), choose.x, choose.y);
				transform.translate(-text_width, 0);
				AffineTransform old = w2.getTransform();
				w2.transform(transform);
				w2.drawString(key, choose.x, choose.y + text_height - adjust_descent);
				w2.setTransform(old);
			}
			//w2.drawRect(choose.x, choose.y, choose.width, choose.height);
		}
	}
	
	//method for displaying the cloud
	public void show() {
		JFrame frame = new JFrame("Word Cloud for " + (title == null ? cloud.url : title));
		//sets options for frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH + 15, HEIGHT + 40); //add 15 and 40 to compensate for window bar and weird stuff
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBackground(Color.BLACK);
		frame.add(this);
		//escape to close window
		KeyAdapter keybinds = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					frame.dispose();
			}
		};
		frame.addKeyListener(keybinds);
		frame.setResizable(false);
		//frame.setLocationRelativeTo(null); //for centering in the middle of the screen
		frame.setVisible(true);
	}
	
}
