package cs355.controller;

import java.awt.event.MouseEvent;
import java.util.Iterator;

import com.sun.glass.events.KeyEvent;

import cs355.model.scene.Point3D;
import cs355.model.scene.SceneModel;

public class Controller3DState implements IControllerState
{

	public Controller3DState() 
	{
		
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		// DO NOTHING
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		// DO NOTHING
	}

	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		// DO NOTHING
	}

	@Override
	public stateType getType() 
	{
		return stateType.THREE_DIM;
	}

}
