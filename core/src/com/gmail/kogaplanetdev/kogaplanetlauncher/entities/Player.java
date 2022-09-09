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
	
	
	//física
	 Body body;
	 BodyDef bodyDef;
	 Fixture fixture;
	 PolygonShape poly;
	 FixtureDef fixtureDef;
	
	
	//gambiarra pura, não toque, só saiba que é os listerners de teclas 
	public boolean isPressedW,isPressedS, isPressedA,isPressedD;

	
	 public Player(SpriteBatch Batch, TextureAtlas atlas) {
		 
		this.Batch = Batch;
		this.atlas = atlas;
		position = new Vector2();
	 }
	
	 /*
	  Use o método create para já setar as configurações de um player,
	  ou corra o risco de receber um glorioso nullPointerException.
	 */ 
	 public void create(int x, int y, String defaultSprite){
		
		this.position.y = y;
		this.position.x = x;
		
		//Cria um sprite que vai ser usado quando nenhuma tecla estiver pressionada
		sprite = atlas.createSprite(defaultSprite);
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		//cria um retangulo que vai servir como "detector de colisão"
		rectangle = new Rectangle(position.x, position.y, sprite.getWidth() + 15, sprite.getHeight() + 25);
		viewport = new FitViewport(cam.viewportWidth, cam.viewportHeight, cam);
	 }
	 
	 
	 /*
	  Método verboso que vai deixar o Render mais "clean".
	  Ah, ele também é o que escuta as teclas do jogador, e faz toda a parte da
	  interação jogo-úsuario
	 */
	public void update(Rectangle r){		
		
		
		/*
		 sempre que for editar uma tecla, ou desenhar algo novo em player, coloque
		 dentro do fluxo "Batch".
		*/
		
		Batch.begin();
		
		//update da camera
		Batch.setProjectionMatrix(cam.combined);
		cam.position.x = position.x;
		cam.position.y = position.y;
		
		cam.update();
	 	viewport.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	 	
		//teclas para a movimentação.
		 isPressedW = Gdx.input.isKeyPressed(Keys.W);
		 isPressedS = Gdx.input.isKeyPressed(Keys.S);
		 isPressedD = Gdx.input.isKeyPressed(Keys.D); 
		 isPressedA = Gdx.input.isKeyPressed(Keys.A);
		 
		 // Tira a velocidade residual quando nenhuma tecla é pressionada
		 body.setLinearVelocity(0, 0);
		 
		 
		 // Da update na posição do sprite na tela.
		 position = body.getPosition();	
		 
		 //verificações de teclas, por enquanto esse sistema vai ficar assim, pretendo melhora-lo no futuro.
		
		 if(isPressedW == true){
			//teste, pode bugar o jogo
			body.setLinearVelocity(body.getLinearVelocity().x, 100);
			 
			sprite = atlas.createSprite(AtlasSprites[0]);
		 
			//fins de debug, vulnerabilidade terrivel se KGP for lançado com isso aqui dentro do código
		 	System.out.println("W");
		 }
	 
	 	 if (isPressedS == true) {
	 		 
	 		//teste, pode bugar o jogo.
	 		body.setLinearVelocity(body.getLinearVelocity().x, -100);
	 		
	 		sprite = atlas.createSprite(AtlasSprites[1]);
	 		
	 		//fins de debug, vulnerabilidade terrivel se KGP for lançado com isso aqui dentro do código
		 	System.out.println("S");
		 }
	 
	 	 if (isPressedD == true) {
	 		
	 		//teste pode bugar o jogo
		 	body.setLinearVelocity(100, body.getLinearVelocity().y);
	 	
	 		sprite = atlas.createSprite(AtlasSprites[2]);
	 		
	 		//fins de debug, vulnerabilidade terrivel se KGP for lançado com isso aqui dentro do código
		 	System.out.println("D");
		 }
	 	 
	 	 if (isPressedA == true) {
	 		 
	 	    //teste, pode bugar o jogo.
	 	    body.setLinearVelocity(-100, body.getLinearVelocity().y);
	 		
	 		sprite = atlas.createSprite(AtlasSprites[3]);
	 		
	 		//fins de debug, vulnerabilidade terrivel se KGP for lançado com isso aqui dentro do código
		 	System.out.println("A");
		 }
	 	 
	 	 /* 
	 	    Desenha o sprite atual que foi passado na verificação,
	 	  	ou o sprite default do método Create()
	 	  */
	 	 Batch.draw(sprite, body.getPosition().x-10, body.getPosition().y-24,21,50);	
	 	Batch.end();
	
	}
	 

	
	
	public void createBody() {
		
		// Cria um corpo com aas especificações necessarias.
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
		
		// Impede a rotação dele.
		body.setFixedRotation(true);
		
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
