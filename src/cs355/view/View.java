package cs355.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.controller.Controller;
import cs355.controller.IControllerState;
import cs355.model.drawing.*;
import cs355.model.scene.HouseModel;
import cs355.model.scene.Instance;
import cs355.model.scene.Line3D;
import cs355.model.scene.Point3D;
import cs355.model.scene.SceneModel;

public class View implements ViewRefresher 
{

	
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		GUIFunctions.refresh();
	}

	public void render3DModel(Graphics2D g2d) 
	{
		ArrayList<Instance> instCollection = SceneModel.instance().instances();
		g2d.setTransform(Controller.instance().worldToView());
		
		for(Instance inst : instCollection) 
		{
			g2d.setColor(inst.getColor());
			List<Line3D> list = inst.getModel().getLines();

			for(Line3D l : list) 
			{
				double[] startCoord = Controller.instance().threeDWorldToClip(l.start, inst);
				double[] endCoord = Controller.instance().threeDWorldToClip(l.end, inst);

				if (!Controller.instance().clipTest(startCoord, endCoord)) 
				{
					Point3D start = Controller.instance().clipToScreen(new Point3D(startCoord[0] / startCoord[3], startCoord[1] / startCoord[3], startCoord[2] / startCoord[3]));
					Point3D end = Controller.instance().clipToScreen(new Point3D(endCoord[0] / endCoord[3], endCoord[1] / endCoord[3], endCoord[2] / endCoord[3]));
					g2d.drawLine((int) Math.round(start.x), (int) Math.round(start.y), (int) Math.round(end.x), (int) Math.round(end.y));
				}
			}
		}
	}
	
	@Override
	public void refreshView(Graphics2D g2d) 
	{
		ArrayList<Shape> shapes = (ArrayList<Shape>) Model.instance().getShapes();
		
		int curShapeIndex = Model.instance().getCurShapeIndex();
		
		for(int a = 0; a < shapes.size(); a++) 
		{
			Shape currentShape = shapes.get(a);
			
			g2d.setColor(currentShape.getColor());
			
			g2d.setTransform(Controller.instance().objectToView(currentShape));
			
			//Uses the factory to determine the current shape to set the fill
			g2d.fill(shapeFactory(currentShape, g2d, false)); 
			//Uses the factory to determine the current shape to draw the image
			g2d.draw(shapeFactory(currentShape, g2d, curShapeIndex == a)); 
			g2d.setColor(currentShape.getColor());
		}
		
		
		if(Controller.instance().getState() == IControllerState.stateType.THREE_DIM) 
		{
			render3DModel(g2d);
		}
	}
	
	//Use a factory to determine what type is being dealt with
	//TODO Needs to add logic for the rest of the shapes
	public java.awt.Shape shapeFactory(Shape currentShape, Graphics2D g2d, boolean shapeSelected)
	{
		if (currentShape.getShapeType() == Shape.type.LINE)
		{
			Line line = (Line)currentShape;
			Point2D.Double start = new Point2D.Double(line.getCenter().x, line.getCenter().y);		
			Point2D.Double end = new Point2D.Double(line.getEnd().x, line.getEnd().y);
			
			if(shapeSelected)
			{
				g2d.setColor(new Color(204, 0, 204));
				g2d.drawOval(-6, -6, 11, 11); //center
				g2d.drawOval((int)(line.getEnd().getX()-line.getCenter().getX())-6, (int)(line.getEnd().getY()-line.getCenter().getY())-6, 11, 11);
				g2d.setColor(line.getColor());
			}
			return new Line2D.Double(0, 0, end.x - start.x, end.y - start.y);
		}
		else if (currentShape.getShapeType() == Shape.type.CIRCLE)
		{
			Circle circle = (Circle)currentShape;			
			double width = circle.getRadius() * 2;
			double height = circle.getRadius() * 2;
			
			if(shapeSelected)
			{
				g2d.setColor(new Color(204, 0, 204));
				g2d.drawRect((int)(-(width/2))-1, (int)(-(height/2))-1, (int)width+2, (int)height+2);
				g2d.drawOval(-6, (int)-width/2 - 15, 11, 11);
				g2d.setColor(circle.getColor());
			}
			return new Ellipse2D.Double(-(width/2), -(height/2), width, height);
		}
		else if (currentShape.getShapeType() == Shape.type.ELLIPSE)
		{
			Ellipse ellipse = (Ellipse)currentShape;
			double width = ellipse.getWidth();
			double height = ellipse.getHeight();
			
			if(shapeSelected)
			{
				g2d.setColor(new Color(204, 0, 204));
				g2d.drawRect((int)(-(width/2))-1, (int)(-(height/2))-1, (int)width+2, (int)height+2);
				g2d.drawOval(-6, (int)(-height/2 - 15), 11, 11);
				g2d.setColor(ellipse.getColor());
			}
			return new Ellipse2D.Double(-(width/2), -(height/2), width, height);
		}
		else if (currentShape.getShapeType() == Shape.type.RECTANGLE)
		{
			Rectangle rectangle = (Rectangle)currentShape;
			double width = rectangle.getWidth();
			double height = rectangle.getHeight();
			if(shapeSelected)
			{
				g2d.setColor(new Color(204, 0, 204));
				g2d.drawRect((int)(-(width/2))-1, (int)(-(height/2))-1, (int)width+2, (int)height+2);
				g2d.drawOval(-6, (int)(-height/2 - 15), 11, 11);
				g2d.setColor(rectangle.getColor());
			}
			return new Rectangle2D.Double(-(width/2), -(height/2), width, height);
		}
		else if (currentShape.getShapeType() == Shape.type.SQUARE)
		{
			Square square = (Square)currentShape;
			double width = square.getSize();
			double height = square.getSize();
			if(shapeSelected)
			{
				g2d.setColor(new Color(204, 0, 204));
				g2d.drawRect((int)(-(width/2))-1, (int)(-(height/2))-1, (int)width+2, (int)height+2);
				g2d.drawOval(-6, (int)-height/2 - 15, 11, 11);
				g2d.setColor(square.getColor());
			}
			return new Rectangle2D.Double(-(width/2), -(height/2), width, height);
		}
		else if (currentShape.getShapeType() == Shape.type.TRIANGLE)
		{
			Triangle triangle = (Triangle)currentShape;
			
			int[] x = new int[3];
			int[] y = new int[3];
			
			x[0] = (int) (triangle.getA().x - triangle.getCenter().x);
			x[1] = (int) (triangle.getB().x - triangle.getCenter().x);
			x[2] = (int) (triangle.getC().x - triangle.getCenter().x);
			
			y[0] = (int) (triangle.getA().y - triangle.getCenter().y);
			y[1] = (int) (triangle.getB().y - triangle.getCenter().y);
			y[2] = (int) (triangle.getC().y - triangle.getCenter().y);
			
			Polygon tri = new Polygon();
			
			tri.addPoint(x[0], y[0]);
			tri.addPoint(x[1], y[1]);
			tri.addPoint(x[2], y[2]);
			
			if(shapeSelected)
			{
				g2d.setColor(new Color(204, 0, 204));
				g2d.draw(tri);
				if(y[0] <= y[1] && y[0] <= y[2])
				{
					g2d.drawOval(x[0]-6, y[0] - 15, 11, 11);
				}
				else if(y[1] <= y[0] && y[1] <= y[2])
				{	
					g2d.drawOval(x[1]-6, y[1] - 15, 11, 11);
				}
				else if(y[2] <= y[1] && y[2] <= y[0])
				{
					g2d.drawOval(x[2]-6, y[2] - 15, 11, 11);
				}
			}
			return tri;
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
}

