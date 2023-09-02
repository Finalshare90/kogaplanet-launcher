package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player{
		
	// Sprite Buffer, stores the Sprite, and flushes him to the game.
	private String AtlasSprites[] = new String[4]; //outra gambiarra, ele vai armazenar os nomes de cada sprite do spritesheet
	private TextureAtlas idleJames;	// Esse carinha serve justamente para armazenar o nosso .atlas(spritesheet) 
	private Sprite currentSprite;
	private TextureAtlas walkingJamesAtlas;
	Animation<Sprite> walkingJames;
	private HashMap<String, Sprite> idleSprites = new HashMap<>();
	
	private OrthographicCamera cam;
	public Viewport viewport;
	
	private Vector2 position;
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

	
	 public Player(TextureAtlas idleJames, TextureAtlas walkingJames){
		
		this.idleJames = idleJames;
		this.walkingJamesAtlas = walkingJames;
		
		position = new Vector2();
		
	 }
	
	 /*
	  Use o m�todo create para j� setar as configura��es de um player,
	  ou corra o risco de receber um glorioso nullPointerException.
	 */ 
	 public void create(Vector2 originPosition, String defaultSprite){
		
		this.originPosition.set(originPosition);
		position.set(this.originPosition);
		
		// Idle Sprites
		idleSprites.put("a", idleJames.createSprite("Idle_left"));
		idleSprites.put("w", idleJames.createSprite("Idle_back"));
		idleSprites.put("s", idleJames.createSprite("Idle_up"));
		idleSprites.put("d", idleJames.createSprite("Idle_right"));
		
		//Cria um sprite que vai ser usado quando nenhuma tecla estiver pressionada
		currentSprite = idleJames.createSprite(defaultSprite);
		cam = new OrthographicCamera(1366,706);
		//cria um retangulo que vai servir como "detector de colis�o"
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
		
		// Cria um corpo com as especifica��es necessarias.
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		body = KogaPlanetLauncher.WORLD.createBody(bodyDef);
		poly = new PolygonShape();
		poly.setAsBox(idleSprites.get("w").getWidth(),idleSprites.get("w").getHeight());
		
		// Cria acessorios ao corpo, como o formato que ele tem.
		fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		fixture = body.createFixture(fixtureDef);
		
		// Impede a rota��o dele.
		body.setFixedRotation(true);
		
	}
	 
	 float stateTime = 0;
	 
	 /*
	  M�todo verboso que vai deixar o Render mais "clean".
	  Ah, ele tamb�m � o que escuta as teclas do jogador, e faz toda a parte da
	  intera��o jogo-�suario
	 */
	String lastInput = "s";
	public void update(SpriteBatch Batch){		
		/*
		 sempre que for editar uma tecla, ou desenhar algo novo em player, coloque
		 dentro do fluxo "Batch".
		*/
 		stateTime += Gdx.graphics.getDeltaTime();

		
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
		 
		  if(!isPressedA && !isPressedD && !isPressedS && !isPressedW){
	 		currentSprite = checkIdleSprite(lastInput, idleSprites);
	 	 }
		  
		 //verifica��es de teclas, por enquanto esse sistema vai ficar assim, pretendo melhora-lo no futuro.
		 if(isPressedW){
			// move o BODY do player, mas n�o move necessariamente o Sprite, a posi��o dele � diretamente vinculada com a do body
			body.setLinearVelocity(body.getLinearVelocity().x, 100);
			lastInput = "w";
			walkingJames = createAnimation("up", 8, walkingJamesAtlas);	
		 	currentSprite = walkingJames.getKeyFrame(stateTime, isPressedW);
		 	}
	 
	 	 if (isPressedS) {	 
	 		body.setLinearVelocity(body.getLinearVelocity().x, -100);
	 		lastInput = "s";
	 		walkingJames = createAnimation("down", 8, walkingJamesAtlas);	
		 	currentSprite = walkingJames.getKeyFrame(stateTime, isPressedS);
		 	
		 }
	 
	 	 if (isPressedD) {
	 		 
		 	body.setLinearVelocity(100, body.getLinearVelocity().y);
		 	lastInput = "d";
		 	walkingJames = createAnimation("right", 8, walkingJamesAtlas);	
		 	currentSprite = walkingJames.getKeyFrame(stateTime, isPressedD);
	 	 }
	 	 
	 	 if (isPressedA) {
	 		body.setLinearVelocity(-100, body.getLinearVelocity().y);
	 		lastInput = "a";
	 		walkingJames = createAnimation("left", 8, walkingJamesAtlas);
	 		currentSprite = walkingJames.getKeyFrame(stateTime, isPressedA);
	 		
		 }
	 	 
	 	 
	 	
	 	 
	 	 /* 
	 	    Desenha o sprite atual que foi passado na verifica��o,
	 	  	ou o sprite default do m�todo Create()
	 	  */
	 	 Batch.draw(currentSprite, body.getPosition().x-15, body.getPosition().y-24,30,50);	
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
	
	
	
	// Parece funcionar, ent�o n�o vale a pena criar algo descente para isso:^)
	/*
	Prefix � simplesmente em que pasta, ou em que pacote os frames dessa anima��o est�o
	localizados dentro de um SpriteAtlas.
	Por exemplo: left/1 = [prefixo]/[frames] (o atributo frames � a quantia total de frames existentes.)
	*/ 
	private Animation<Sprite> createAnimation(String prefix, float frames, TextureAtlas spriteSheet){
		
		String framesIndex[] = new String[(int)frames];
		Sprite animationFrames[] = new Sprite[(int)frames];
		
		for(int currentFrame = 0; currentFrame < frames; currentFrame++) {
			framesIndex[currentFrame] = prefix + "/" + currentFrame;
			animationFrames[currentFrame] = spriteSheet.createSprite(framesIndex[currentFrame]);
		}		
		
		return new Animation<Sprite>(1/frames,animationFrames);
	}
	
	private Sprite checkIdleSprite(String input, HashMap<String, Sprite> idleSprites) {
		return idleSprites.get(input);
	}
	
	/*
	 Esse merece um comentario engra�aralho s� para ele:
	 Gambiarra a moda Final de qualidade, tu basicamente coloca um n�mero de onde que o atlasSprites
	 vai armazenar o nome do Sprite a ser usado nos listerners, e mudar os sprites de acordo.
	 */
	public void setAtlasSprites(int SpritePos, String SpriteName) {
		AtlasSprites[SpritePos] = SpriteName;
	}
	
	public void dispose() {
		walkingJamesAtlas.dispose();
		idleJames.dispose();
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
		return currentSprite;
	}
	public OrthographicCamera getCam() {
		return cam;
	}
}
