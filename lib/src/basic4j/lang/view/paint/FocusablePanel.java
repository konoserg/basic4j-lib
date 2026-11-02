package basic4j.lang.view.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class FocusablePanel extends JPanel implements FocusListener, MouseListener {
	//protected final static Color TRANSPARENT = new Color(f, 1f, 1f, 1f);
	
	public FocusablePanel() {
		//this.addFocusListener(this);
		//this.addMouseListener(this);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		//System.out.println("focusGained " + this);
		this.repaint();
	}

	@Override
	public void focusLost(FocusEvent e) {
		//System.out.println("focusLost " + this);
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println("mouseClicked " + this);
		this.requestFocusInWindow();
	}
	
	@Override
	protected void paintChildren(Graphics graphics) {
		super.paintChildren(graphics);
		
		// Add a border if panel currently has focus
		Graphics g = graphics.create();
		if (isFocusOwner()) {
			g.setColor(new Color(127, 0, 85));
			g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		} else {
			g.setColor(Area.BACKGROUND_COLOR);
		}
		
		
		g.dispose();
    }


	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
