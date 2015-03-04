package entities;

import handlers.Animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class B2DSprite {

	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	
	public B2DSprite(Body body){
		this.body = body;
		animation = new Animation();
	}
	
	public void setAnimation(TextureRegion[] reg, float delay){
		setAnimation(reg, delay);
	}
	
	public void update(float dt) {
		animation.update(dt);
	}
	
	
}
