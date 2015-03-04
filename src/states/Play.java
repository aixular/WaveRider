package states;

import static handlers.B2DVars.PPM;
import handlers.B2DVars;
import handlers.GameStateManager;
import handlers.MyContactListener;
import handlers.MyInput;
import main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Play extends GameState{

	private World world; 
	private Box2DDebugRenderer b2dr;

	private OrthographicCamera b2dCam;

	private Body playerBody;
	private MyContactListener cl;

	private TiledMap tileMap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;

	public Play(GameStateManager gsm){
		super(gsm);

		// set up Box2D stuff
		cl = new MyContactListener();
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();

		// create player
		createPlayer();

		// create tiles
		createTiles();

		// set up Box2D camera
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
	
	}

	public void handleInput(){

		if (MyInput.isPressed(MyInput.BUTTON1)) {
			if (cl.isPlayerOnGround()){
				playerBody.applyForceToCenter(0, 200, true);
			}
		}
		if (MyInput.isPressed(MyInput.BUTTON2)) {
			playerBody.applyForceToCenter(-50, 0, true);
		}

	}		

	public void update(float dt){

		handleInput();

		world.step(dt, 6, 2);

	}

	public void render(){

		//clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//draw tile map
		tmr.setView(cam);
		tmr.render();

		//draw world
		b2dr.render(world,b2dCam.combined);
	}

	public void dispose(){

	}

	private void createPlayer(){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		// create player
		bdef.position.set(160 / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);

		shape.setAsBox(5 / PPM, 5 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_RED;
		playerBody.createFixture(fdef).setUserData("player");


		// create foot sensor
		shape.setAsBox( 2 / PPM, 2 / PPM, new Vector2(0, -5 / PPM), 0);
		fdef.shape = shape;
		fdef.isSensor = true;
		playerBody.createFixture(fdef).setUserData("foot");
	}

	private void createTiles(){
		//load tile map
		tileMap = new TmxMapLoader().load("res/maps/test.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		tileSize = (int) tileMap.getProperties().get("tilewidth",Integer.class);
		
		TiledMapTileLayer layer;

		layer = (TiledMapTileLayer) tileMap.getLayers().get("Red");
		createLayer(layer, B2DVars.BIT_RED);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("Green");
		createLayer(layer, B2DVars.BIT_GREEN);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("Blue");
		createLayer(layer, B2DVars.BIT_BLUE);
		
	}

	private void createLayer(TiledMapTileLayer layer, short bits){

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		for (int y = 0; y < layer.getHeight(); y++) {
			for (int x = 0; x < layer.getWidth(); x++) {
				//get cell
				Cell cell = layer.getCell(x, y);

				//checkcell
				if (cell == null) continue;
				if (cell.getTile() == null) continue;

				//create body/fixture for cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
						(x + .5f) * tileSize / PPM, 		//Tiled tiles are center in the center of the tile
						(y + .5f) * tileSize / PPM);

				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[4];
				v[0] = new Vector2(
						-tileSize/2/PPM, -tileSize/2/PPM);
				v[1] = new Vector2(
						-tileSize/2/PPM, tileSize/2/PPM);
				v[2] = new Vector2(
						tileSize/2/PPM, tileSize/2/PPM);
				v[3] = new Vector2(
						tileSize/2/PPM, -tileSize/2/PPM);

				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = B2DVars.BIT_RED;
				fdef.filter.maskBits = B2DVars.BIT_PLAYER;
				fdef.isSensor= false;
				world.createBody(bdef).createFixture(fdef);

			}
		}
	}

}
