package cs355;

import cs355.model.scene.Point3D;


public class Camera 
{
	private Point3D location;
	private float yaw;
	private float pitch;
	private float roll;
	
	
	
	
	public Camera()	
	{
		this.location = new Point3D(0.0d, 0.0d, 0.0d);
		this.yaw = 0.0f; // yaw is turn rotation
		this.pitch = 0.0f;
		this.roll = 0.0f;
	}
	
	public Camera(Point3D location)	
	{
		this.location = location;
		this.yaw = 0.0f;
		this.pitch = 0.0f;
		this.roll = 0.0f;
	}
	
	
	
	
	
	
	
}
