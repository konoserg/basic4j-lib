package basic4j.lang.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import basic4j.lang.Function;
import basic4j.lang.GetMaxColor;
import basic4j.lang.Type;
import basic4j.lang.GetMaxColor.Ball;
import basic4j.lang.view.paint.Area;
import basic4j.lang.view.paint.Block;
import basic4j.lang.view.paint.Line;
import basic4j.lang.view.paint.SourceCode;
import basic4j.lang.view.paint.SourceCode.*;

public class FunctionView extends Block {
	
	public void setStaticMethod(Function model) throws NoSuchMethodException, SecurityException {
		{
			Line header = this.addNewNonfocusableLine();
			
			TypeView retType = new TypeView();
			retType.addAllTypes();
			retType.setType(model.retType);
			
			header.add(retType).add(new Punctuation(" ")).add(new Call(model.name)).add(new Punctuation("("));

			for (int i = 0; i < model.paramTypes.length; i ++) {
				if (i != 0) {
					header.add(new Punctuation(", "));
				}
				
				TypeView type = new TypeView();
				type.addAllTypes();
				header.add(type);
				type.setType(model.paramTypes[i]);
							
				header.add(new Punctuation(" ")).add(new SourceCode.Variable(model.paramNames[i]));
			}
			
			header.add(new Punctuation(") {"));
		}

		Block body = this.addNewBlock("    ");
		
		HashMap<String, Type> declaredVars = new HashMap<>();
		for (int i = 0; i < model.paramNames.length; i ++) {
			declaredVars.put(model.paramNames[i], model.paramTypes[i]);
		}
		
		model.firstStep.addStepViews("", declaredVars, body);
		
		this.addNewNonfocusableLine().add(new Punctuation("}"));
	}

	public static void main(String... args) throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		TypeView.ALL.put(GetMaxColor.Ball.class, 0);
		
		JFrame window = new JFrame();
		GetMaxColor model = new GetMaxColor();
		Block view = model.newView();
		window.add(new Area(view));
		window.setTitle(model.title());
		//window.pack();
		window.setSize(800, 450);
	    center(window);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	private static void center(JFrame window) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((screen.getWidth() - window.getWidth()) / 2);
	    int y = (int) ((screen.getHeight() - window.getHeight()) / 2);
	    window.setLocation(x, y);
	}
}
