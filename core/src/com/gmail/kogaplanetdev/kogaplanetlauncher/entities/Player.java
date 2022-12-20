package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.kogaplanetdev.kogaplanetlauncher.KogaPlanetLauncher;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player{
		
	//SpriteBatch, armazene uma instancia aqui para poder desenhar o player
	private SpriteBatch Batch; 
	private String AtlasSprites[] = new String[4]; //outra gambiarra, ele vai armazenar os nomes de cada sprite do spritesheet
	private TextureAtlas atlas;	// Esse carinha serve justamente para armazenar o nosso .atlas(spritesheet) 
	private Sprite sprite;
	private OrthographicCamera cam;
	private Rectangle rectangle;
	private Vector2 position;
	public Viewport viewport;
	
	public Vector2 originPosition = new Vector2();
	
	// Esses atributos s� v�o ser usados no CollsionHandler.
	HashMap<String, Object> fixtureData = new HashMap<>();
	
	
	
	//f�sica
	 Body body;
	 BodyDef bodyDef;
	 Fixture fixture;
	 PolygonShape poly;
	 FixtureDef fixtureDef;
	
	
	//gambiarra pura, n�o toque, s� saiba que � os listerners de teclas 
	public boolean isPressedW, isPressedS, isPressedA, isPressedD;

	
	 public Player(SpriteBatch Batch, TextureAtlas atlas) {
		 
		this.Batch = Batch;
		this.atlas = atlas;
		position = new Vector2();
	 }
	
	 /*
	  Use o m�todo create para j� setar as configura��es de um player,
	  ou corra o risco de receber um glorioso nullPointerException.
	 */ 
	 public void create(int x, int y, String defaultSprite){
		
		originPosition.set(x, y);
		position.set(originPosition);
		
		
		//Cria um sprite que vai ser usado quando nenhuma tecla estiver pressionada
		sprite = atlas.createSprite(defaultSprite);
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		//cria um retangulo que vai servir como "detector de colis�o"
		rectangle = new Rectangle(position.x, position.y, sprite.getWidth() + 15, sprite.getHeight() + 25);
		viewport = new FitViewport(cam.viewportWidth, cam.viewportHeight, cam);
		
		// Cria o corpo e seu formato
		createBody();
		
		// Atribui ao formato do corpo alguns dados para ser usado no Handler de colis�o.
		attributesFactory("isAlive",true);
		attributesFactory("canBeKilled", true);
		setFixtureData();
	 }
	 
	 private void attributesFactory(String attributeName, Object value){
		 // Put the attributes inside of a hashMap
		 fixtureData.put(attributeName, value);
	 }
	 
	 private void setFixtureData(){
		// Define os dados de �suario dessa fixture, s� pode 1, por isso fiz um hashmap.
		fixture.setUserData(fixtureData);
	}
	 
	 private void createBody() {
		
		// Cria um corpo com aas especifica��es necessarias.
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(getRectangle().x,getRectangle().y);
		body = KogaPlanetLauncher.WORLD.createBody(bodyDef);
		poly = new PolygonShape();
		poly.setAsBox(getRectangle().width/2,getRectangle().height/2);
		
		// Cria acessorios ao corpo, como o formato que ele tem.
		fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		fixture = body.createFixture(fixtureDef);
		
		// Impede a rota��o dele.
		body.setFixedRotation(true);
		
	}
	 
	 /*
	  M�todo verboso que vai deixar o Render mais "clean".
	  Ah, ele tamb�m � o que escuta as teclas do jogador, e faz toda a parte da
	  intera��o jogo-�suario
	 */
	public void update(){		
		/*
		 sempre que for editar uma tecla, ou desenhar algo novo em player, coloque
		 dentro do fluxo "Batch".
		*/	
		Batch.begin();
		
		//update da camera
		Batch.setProjectionMatrix(cam.combined);
		cam.position.x = position.x;
		cam.position.y = position.y;
		viewport.setScreenY((int)cam.position.y);
		viewport.setScreenX((int)cam.position.x);

		cam.update();
	 	viewport.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	 	
		//teclas para a movimenta��o.
		 isPressedW = Gdx.input.isKeyPressed(Keys.W);
		 isPressedS = Gdx.input.isKeyPressed(Keys.S);
		 isPressedD = Gdx.input.isKeyPressed(Keys.D); 
		 isPressedA = Gdx.input.isKeyPressed(Keys.A);
		 
		 isAlive();
		 
		 // Tira a velocidade residual quando nenhuma tecla � pressionada
		 body.setLinearVelocity(0, 0);
		 
		
		 
		 // Da update na posi��o do sprite na tela.
		 position = body.getPosition();	
		 
		 //verifica��es de teclas, por enquanto esse sistema vai ficar assim, pretendo melhora-lo no futuro.
		 if(isPressedW){
			// move o BODY do player, mas n�o move necessariamente o Sprite, a posi��o dele � diretamente vinculada com a do body
			body.setLinearVelocity(body.getLinearVelocity().x, 100);
			 
			//define o sprite atual com base no input
			sprite = atlas.createSprite(AtlasSprites[0]);
		 	}
	 
	 	 if (isPressedS) {	 		 
	 		body.setLinearVelocity(body.getLinearVelocity().x, -100);
	 		
	 		sprite = atlas.createSprite(AtlasSprites[1]);
		 }
	 
	 	 if (isPressedD) {
		 	body.setLinearVelocity(100, body.getLinearVelocity().y);
	 	
	 		sprite = atlas.createSprite(AtlasSprites[2]);	
	 	 }
	 	 
	 	 if (isPressedA) {
	 		body.setLinearVelocity(-100, body.getLinearVelocity().y);
	 		
	 		sprite = atlas.createSprite(AtlasSprites[3]);
		 }
	 	 
	 	 /* 
	 	    Desenha o sprite atual que foi passado na verifica��o,
	 	  	ou o sprite default do m�todo Create()
	 	  */
	 	 Batch.draw(sprite, body.getPosition().x-10, body.getPosition().y-24,21,50);	
	 	Batch.end();
	
	}
	public void isAlive(Boolean isAlive){
		fixtureData.replace("isAlive", isAlive);
	}
	
	private void isAlive(){
		if(!(Boolean)fixtureData.get("isAlive")) {
			body.setTransform(originPosition.x, originPosition.y, 0);
			fixtureData.replace("isAlive", true);
		}
	}
	

	/*
	 Esse merece um comentario engra�aralho s� para ele:
	 Gambiarra a moda Final de qualidade, tu basicamente coloca um n�mero de onde que o atlasSprites
	 vai armazenar o nome do Sprite a ser usado nos listerners, e mudar os sprites de acordo.
	 */
	
	public void setAtlasSprites(int SpritePos, String SpriteName) {
		AtlasSprites[SpritePos] = SpriteName;
	}
	
		public void setAtlas(TextureAtlas atlas) {
		this.atlas = atlas;
	}
	
	public float getY() {
		return body.getPosition().y;
	}
	public float getX() {
		return body.getPosition().x;
	}
	public void setX(int x) {
		body.getPosition().x = x;
	}
	public void setY(int y) {
		body.getPosition().y = y;
	}
	public Sprite getSprite() {
		return sprite;
	}
	public OrthographicCamera getCam() {
		return cam;
	}
	public Rectangle getRectangle() {
		return rectangle;
	}
}
