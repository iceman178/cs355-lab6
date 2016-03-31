package cs355.controller;

import java.awt.event.MouseEvent;
import java.util.Iterator;

public interface IControllerState 
{	
	public enum stateType 
	{
		NOTHING, DRAWING, SELECT, THREE_DIM
	}
	
	public void mousePressed(MouseEvent arg0);
	
	public void mouseReleased(MouseEvent arg0);
	
	public void mouseDragged(MouseEvent arg0);
	
	public stateType getType();
}
