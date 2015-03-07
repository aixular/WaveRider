package handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class MyContactListener implements ContactListener{

	private int numFootContacts;
	private Array<Body> bodiesToRemove;

	public MyContactListener() {
		
		super();
		
		bodiesToRemove = new Array<Body>();
		
	}
	
	//called when two fixtures start to collide
	public void beginContact(Contact c) {

		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if (fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts++;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts++;
		}

		if (fa.getUserData() != null && fa.getUserData().equals("crystal")){
			bodiesToRemove.add(fa.getBody());
		}
		if (fb.getUserData() != null && fb.getUserData().equals("crystal")){
			bodiesToRemove.add(fb.getBody());
		}
	}

	//called when two fixtures no longer collide
	public void	endContact(Contact c) {

		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if (fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts--;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts--;
		}

	}

	public boolean isPlayerOnGround() { return numFootContacts > 0; }
	public Array<Body> getBodiesToRemove() { return bodiesToRemove; }
	
	//collision detection
	//before handling object, you can change the way you handle the object and other stuff
	public void preSolve(Contact c, Manifold m) {}
	//collision handling
	//do stuff after collision handling
	public void postSolve(Contact c, ContactImpulse ci) {}


}
