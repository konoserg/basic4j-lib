package basic4j.lang.view.paint;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class Block extends FocusablePanel {
	public Block() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Area.BACKGROUND_COLOR);
		
		this.setFocusable(true);
	}
	
	public Block addNewBlock(String shift) {
		Block result = new Block();

		if (shift.isEmpty()) {
			this.add(result);
		} 
		else {
			JPanel row = new JPanel();
			row.setAlignmentX(Component.LEFT_ALIGNMENT);
			row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
			row.setBackground(Area.BACKGROUND_COLOR);
			
			row.add(new SourceCode.Punctuation(shift));
			row.add(result);
			
			this.add(row);
		}
		
		return result;
	}
	
	public Line addNewNonfocusableLine() {
		Line result = new Line(false /* focusable */);
		this.add(result);
		return result;
	}
}
