package basic4j.lang.view.paint;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class SourceCode {
	private static final String FONT_NAME = "Times New Roman";
	private static final int FONT_SIZE = 13;
	
	public static class Keyword extends JLabel {
		public Keyword(String text) {
			super(text);
			this.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));
			this.setForeground(Area.DARK ? new Color(82, 216, 239) : new Color(127, 0, 85));
			//this.setBackground(Color.BLUE);
		}
	}
	
	public static class Type extends JLabel {
		public static Color COLOR = Area.DARK ? Color.WHITE : Color.BLACK;
		
		public Type(String text) {
			super(text);
			this.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
			this.setForeground(COLOR);
			//this.setBackground(Color.BLUE);
		}
	}
	
	public static class Call extends JLabel {
		public final static Color COLOR = Variable.COLOR;//Area.DARK ? Color.WHITE : Color.BLACK;
		
		public Call(String text) {
			super(text);
			this.setFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
			this.setForeground(COLOR);
			//this.setBackground(Color.BLUE);
		}
	}
	
	public static class Variable extends JLabel {
		public final static Color COLOR = Area.DARK ? new Color(118, 226, 46) : new Color(0, 0, 192);
		
		public Variable(String text) {
			super(text);
			this.setFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
			this.setForeground(COLOR);
			//this.setBackground(Color.BLUE);
		}
	}
	
	public static class Punctuation extends JLabel {
		public Punctuation(String text) {
			super(text);
			this.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
			this.setForeground(Area.DARK ? Color.RED : Color.BLACK);
			//this.setBackground(Color.BLUE);
		}

		@Override
		public String toString() {
			return getText();
		}
	}
	
	public static class Comment extends JLabel {
		public Comment(String text) {
			super(text);
			this.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
			this.setForeground(Area.DARK ? new Color(81, 84, 81) : new Color(63, 127, 95));
			//this.setBackground(Color.BLUE);
		}
	}
	
	public static class Literal extends JLabel {
		public Literal(String text) {
			super(text);
			this.setFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
			this.setForeground(Area.DARK ? new Color(231, 220, 92) : new Color(78, 136, 56));
			//this.setBackground(Color.BLUE);
		}
	}
}
