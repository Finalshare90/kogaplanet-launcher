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
		
	//SpriteBatch, armazene uma instancia aqui para poder desenhar o player
	private SpriteBatch Batch; 
	private String AtlasSprites[] = new String[4]; //outra gambiarra, ele vai armazenar os nomes de cada sprite do spritesheet
	private TextureAtlas idleJames;	// Esse carinha serve justamente para armazenar o nosso .atlas(spritesheet) 
	private Sprite sprite;
	private TextureAtlas walkingJamesAtlas;
	Animation<Sprite> walkingJames;
	
	private OrthographicCamera cam;
	public Viewport viewport;
	
	private Vector2 position;
	public Vector2 originPosition = new Vector2();
	
	// Esses atributos só vão ser usados no CollsionHandler.
	HashMap<String, Object> fixtureData = new HashMap<>();	
	
	//física
	 Body body;
	 BodyDef bodyDef;
	 Fixture fixture;
	 PolygonShape poly;
	 FixtureDef fixtureDef;
	
	
	//gambiarra pura, não toque, só saiba que é os listerners de teclas 
	public boolean isPressedW, isPressedS, isPressedA, isPressedD;

	
	 public Player(SpriteBatch Batch, TextureAtlas idleJames, TextureAtlas walkingJames){
		
		this.Batch = Batch;
		this.idleJames = idleJames;
		this.walkingJamesAtlas = walkingJames;
		
		position = new Vector2();
		
	 }
	
	 /*
	  Use o método create para já setar as configurações de um player,
	  ou corra o risco de receber um glorioso nullPointerException.
	 */ 
	 public void create(Vector2 originPosition, String defaultSprite){
		
		this.originPosition.set(originPosition);
		position.set(this.originPosition);
		
		//Cria um sprite que vai ser usado quando nenhuma tecla estiver pressionada
		sprite = idleJames.createSprite(defaultSprite);
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		//cria um retangulo que vai servir como "detector de colisão"
		viewport = new FitViewport(cam.viewportWidth, cam.viewportHeight, cam);
		
		// Cria o corpo e seu formato
		createBody();
		
		// Atribui ao formato do corpo alguns dados para ser usado no Handler de colisão.
		attributesFactory("isAlive",true);
		attributesFactory("canBeKilled", true);
		setFixtureData();
	 }
	 
	 private void attributesFactory(String attributeName, Object value){
		 // Put the attributes inside of a hashMap
		 fixtureData.put(attributeName, value);
	 }
	 
	 private void setFixtureData(){
		// Define os dados de úsuario dessa fixture, só pode 1, por isso fiz um hashmap.
		fixture.setUserData(fixtureData);
	}
	 
	 private void createBody() {
		
		// Cria um corpo com as especificações necessarias.
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		body = KogaPlanetLauncher.WORLD.createBody(bodyDef);
		poly = new PolygonShape();
		poly.setAsBox(sprite.getWidth(),sprite.getHeight());
		
		// Cria acessorios ao corpo, como o formato que ele tem.
		fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		fixture = body.createFixture(fixtureDef);
		
		// Impede a rotação dele.
		body.setFixedRotation(true);
		
	}
	 
	 float stateTime = 0;
	 
	 /*
	  Método verboso que vai deixar o Render mais "clean".
	  Ah, ele também é o que escuta as teclas do jogador, e faz toda a parte da
	  interação jogo-úsuario
	 */
	 
	public void update(){		
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
	 	
	 	 //teclas para a movimentação.
		 isPressedW = Gdx.input.isKeyPressed(Keys.W);
		 isPressedS = Gdx.input.isKeyPressed(Keys.S);
		 isPressedD = Gdx.input.isKeyPressed(Keys.D); 
		 isPressedA = Gdx.input.isKeyPressed(Keys.A);
		 
		 isAlive();
		 
		 // Tira a velocidade residual quando nenhuma tecla é pressionada
		 body.setLinearVelocity(0, 0);
		 
		 
		 // Da update na posição do sprite na tela.
		 position = body.getPosition();	
		 
		 //verificações de teclas, por enquanto esse sistema vai ficar assim, pretendo melhora-lo no futuro.
		 if(isPressedW){
			// move o BODY do player, mas não move necessariamente o Sprite, a posição dele é diretamente vinculada com a do body
			body.setLinearVelocity(body.getLinearVelocity().x, 100);
			 
			//define o sprite atual com base no input
			sprite = idleJames.createSprite(AtlasSprites[0]);
			
		 	}
	 
	 	 if (isPressedS) {	 		 
	 		body.setLinearVelocity(body.getLinearVelocity().x, -100);
	 		
	 		sprite = idleJames.createSprite(AtlasSprites[1]);
		 }
	 
	 	 if (isPressedD) {
		 	body.setLinearVelocity(100, body.getLinearVelocity().y);
	 	
		 	walkingJames = createAnimation("right", 8, walkingJamesAtlas);	
		 	sprite = walkingJames.getKeyFrame(stateTime, isPressedD);
	 	 }
	 	 
	 	 if (isPressedA) {
	 		body.setLinearVelocity(-100, body.getLinearVelocity().y);
	 		
	 		walkingJames = createAnimation("left", 8, walkingJamesAtlas);
	 		sprite = walkingJames.getKeyFrame(stateTime, isPressedA);
	 		
		 }
	 	 
	 	 /* 
	 	    Desenha o sprite atual que foi passado na verificação,
	 	  	ou o sprite default do método Create()
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
	

	// Parece funcionar, então não vale a pena criar algo descente para isso:^)
	
	
	/*
	Prefix é simplesmente em que pasta, ou em que pacote os frames dessa animação estão
	localizados dentro de um SpriteAtlas.
	Por exemplo: left/1 = [prefixo]/[frames] (o atributo frames é a quantia total de frames existentes.)
	*/ 
	private Animation<Sprite> createAnimation(String prefix, float frames, TextureAtlas spriteSheet){
		
		String framesIndex[] = new String[(int)frames];
		Sprite animationFrames[] = new Sprite[(int)frames];
		
		for(int currentFrame = 0; currentFrame < frames; currentFrame++) {
			framesIndex[currentFrame] = prefix + "/" + currentFrame;
			animationFrames[currentFrame] = spriteSheet.createSprite(framesIndex[currentFrame]);
		}		
		
		return new Animation<Sprite>(1/frames, animationFrames);
	}
	
	/*
	 Esse merece um comentario engraçaralho só para ele:
	 Gambiarra a moda Final de qualidade, tu basicamente coloca um número de onde que o atlasSprites
	 vai armazenar o nome do Sprite a ser usado nos listerners, e mudar os sprites de acordo.
	 */
	public void setAtlasSprites(int SpritePos, String SpriteName) {
		AtlasSprites[SpritePos] = SpriteName;
	}
	
		public void setAtlas(TextureAtlas atlas) {
		this.idleJames = atlas;
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
}
