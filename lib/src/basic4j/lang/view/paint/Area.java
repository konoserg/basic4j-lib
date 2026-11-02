package basic4j.lang.view.paint;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class Area extends JPanel {
	public static final boolean DARK = false;
	
	public static final Color BACKGROUND_COLOR = DARK ? Color.BLACK : Color.WHITE;
	
	public Area(Block b) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBackground(Area.BACKGROUND_COLOR);
		this.add(b);
	}
}
