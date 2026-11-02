package basic4j.lang.view.paint;

import java.awt.Component;

import javax.swing.BoxLayout;

public class Line extends FocusablePanel {
	public Line(Component c) {
		this();
		add(c);
	}
	
	public Line() {
		this(true);
	}
	
	Line(boolean focusable) {
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBackground(Area.BACKGROUND_COLOR);
		
		this.setFocusable(focusable);
	}
	
	public Line add(Component c) {
		super.add(c);
		return this;
	}
}
