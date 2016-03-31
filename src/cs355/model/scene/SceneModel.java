package cs355.model.scene;


public class SceneModel extends CS355Scene
{
	private static SceneModel _instance;

	
	//If the model had not been initialized, it will be.
	public static SceneModel instance() {
		if (_instance == null) {
			_instance = new SceneModel();
		}
		return _instance;
	}
	
	public SceneModel()	
	{
		this.setCameraPosition(new Point3D(0.0d, 0.0d, 0.0d));
		this.setCameraRotation(180.0);
	}

	
	public void changeAltitude(float distance) 
	{
		Point3D cam = this.getCameraPosition();
		cam.y += distance;
		this.setCameraPosition(cam);
	}

	public void moveForward(float distance)	
	{
		Point3D cam = this.getCameraPosition();
		cam.x -= distance * (float) Math.sin(Math.toRadians(this.getCameraRotation()));
		cam.z += distance * (float) Math.cos(Math.toRadians(this.getCameraRotation()));
		this.setCameraPosition(cam);
	}

	public void moveBackward(float distance) 
	{
		Point3D cam = this.getCameraPosition();
		cam.x += distance * (float) Math.sin(Math.toRadians(this.getCameraRotation()));
		cam.z -= distance * (float) Math.cos(Math.toRadians(this.getCameraRotation()));
		this.setCameraPosition(cam);
	}

	public void moveLeft(float distance) 
	{
		Point3D cam = this.getCameraPosition();
		cam.x -= distance * (float) Math.sin(Math.toRadians(this.getCameraRotation() + 90));
		cam.z += distance * (float) Math.cos(Math.toRadians(this.getCameraRotation() + 90));
		this.setCameraPosition(cam);
	}

	public void moveRight(float distance)	
	{
		Point3D cam = this.getCameraPosition();
		cam.x -= distance * (float) Math.sin(Math.toRadians(this.getCameraRotation() - 90));
		cam.z += distance * (float) Math.cos(Math.toRadians(this.getCameraRotation() - 90));
		this.setCameraPosition(cam);
	}


	public void yaw(float yaw) 
	{
		this.setCameraRotation((float)this.getCameraRotation() + yaw);
	}

	
	//------------------------------Getters and Setters--------------------------
	
	public float getYaw() {
		return (float) Math.toRadians(this.getCameraRotation());
	}

	public void setYaw(float yaw) {
		this.setCameraRotation(yaw);
	}

	
}


