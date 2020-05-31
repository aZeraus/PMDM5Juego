package com.example.pmdm5juego;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;

public class PMDM5Juego extends ApplicationAdapter implements InputProcessor {
	/* ----- Atributos necesarios para mostrar el mapa. ----- */

	// Objeto que recoge el mapa de baldosas.
	private TiledMap mapa;
	// Objeto con el que se pinta el mapa de baldosas.
	private TiledMapRenderer mapaRenderer;

	// Cámara que nos da la vista del juego.
	private OrthographicCamera camara;

	/* Atributos necesarios para mostrar el Sprite. */

	// Atributo Textura en el que se cargará la imagen del Sprite.
	private Texture img;
	// Atributo que permitirá la representación de la imagen de textura anterior.
	//private Sprite sprite;
	// Atributo que permite dibujar imágenes 2D, en este caso el sprite.
	private SpriteBatch sb;

	// Atributos para obtener las dimensiones de la pantalla.
	int anchoPant, altoPant;

	/* ----- Atributos necesarios para animar el Sprite. ----- */

	// Constantes que indican el número de filas y columnas de la hoja de sprites.
	private static final int FRAME_COLS = 3;
	private static final int FRAME_ROWS = 4;

	// Animación que se muestra en el método render()
	private Animation jugador;

	// Animaciones para cada una de las direcciones de movimiento del personaje del jugador.
	private Animation jugadorArriba;
	private Animation jugadorDerecha;
	private Animation jugadorAbajo;
	private Animation jugadorIzquierda;

	// Posición actual del jugador en el eje de coordenadas.
	private float jugadorX, jugadorY;

	// Para realizar el movimiento al pulsar las teclas respectivas.
	boolean arriba, abajo, izquierda, derecha;

	/* Este atributo indica el tiempo en segundos transcurridos desde que se inicia la animación,
	   servirá para determinar cual es el frame que se debe representar. */
	private float stateTime;

	// Contendrá el frame que se va a mostrar en cada momento.
	private TextureRegion cuadroActual;

	// Atributos para obtener las dimensiones del Sprite.
	int anchoImg, altoImg;

	//Atributos que indican la anchura y altura del sprite animado del jugador.
	int anchoJugador, altoJugador;

	// Tamaño del mapa de baldosas.
	private int anchoMapa, altoMapa;

	//Atributos que indican la anchura y la altura de un tile del mapa de baldosas
	int anchoCelda,altoCelda;

	/* ----- Atributos necesarios para los Obstáculos. ----- */

	// Matriz booleana para cargar los obstáculos.
	private boolean[][] obstaculo;

	// Capa del mapa del baldosas que contiene los obstáculos
	private TiledMapTileLayer capaObstaculos;

	/* ----- Atributos necesarios para los Agujeros . ----- */

	// Matriz booleana que contiene los agujeros del TiledMap.
	private boolean[][] muros;

	// Capa del mapa del baldosas que contiene los muros.
	private TiledMapTileLayer capaMuros;

	/* ----- Atributos necesarios para los Agujeros . ----- */

	// Matriz booleana que contiene los agujeros del TiledMap.
	private boolean[][] agujeros;

	// Capa del mapa del baldosas que contiene los agujeros.
	private TiledMapTileLayer capaAgujeros;

	/* ----- Atributos necesarios para los premios. ----- */

	// Matriz booleana para cargar los premios.
	private boolean[][] premios;

	// Capa del mapa del baldosas que contiene los premios
	private TiledMapTileLayer capaPremios;

	// Premios conseguidos.
	int numPremios = 0;

	/* ----- Atributos necesarios para los Sprite No Jugadores (NPC). ----- */

	// Animaciones posicionales relacionadas con los NPC del juego.
	private Animation noJugadorArriba;
	private Animation noJugadorDerecha;
	private Animation noJugadorAbajo;
	private Animation noJugadorIzquierda;

	// Array con los objetos Animation de los NPC.
	private Animation[] noJugador;
	// Atributos que indican la anchura y altura del sprite animado de los NPC.
	int anchoNoJugador, altoNoJugador;
	// Posición inicial X de cada uno de los NPC
	private float[] noJugadorX;
	// Posición inicial Y de cada uno de los NPC
	private float[] noJugadorY;
	// Posición final X de cada uno de los NPC
	//private float[] destinoX;
	private float destinoX;
	// Posición final Y de cada uno de los NPC
	//private float[] destinoY;
	private float destinoY;
	// Número de NPC que van a aparecer en el juego, aleatorio entre 1 y 3.
	private static final int numeroNPCs = (int)(Math.random()*3)+1;
	//private static final int numeroNPCs = 3;
	/* Este atributo indica el tiempo en segundos transcurridos desde que se inicia la animación de
	   los NPC , servirá para determinar cual es el frame que se debe representar. */
	private float stateTimeNPC;

	/* ----- Atributos necesarios para los Sprite de los disparos. ----- */

	// Sprites para dibujar los disparos.
	Sprite disp1, disp2, disp3;
	// Posición de los disparos en el eje de coordenadas.
	private float disp1X, disp1Y, disp2X, disp2Y, disp3X, disp3Y;
	int anchoDisp1, altoDisp1, anchoDisp2, altoDisp2, anchoDisp3, altoDisp3;

	// Capa del mapa del baldosas que contiene la Salida.
	private TiledMapTileLayer capaDecoracion;

	// Veriable que nos indica si ha perdido el juego.
	private boolean finJuego = false;
	// Veriable que nos indica si ha ganado el juego.
	private boolean ganaJuego = false;

	// Atributos relacionados con los efectos de sonido y música del juego.
	private Music musicaFondo;
	private Sound sonidoVuelo;
	private Sound sonidoColisionEnemigo;
	private Sound sonidoObstaculo;
	private Sound sonidoPremio;
	private Sound sonidoPuerta;
	private Sound sonidoDisparo1;
	private Sound sonidoDisparo3;
	private Sound sonidoExplota;
	private Music musicaPierde;
	private Music musicaGana;

	/**
	 * Crea todos los elementos multimedia antes de que puedan ser mostrados.
	 */
	@Override
	public void create () {

		// Creamos una cámara y la vinculamos con el lienzo del juego. Le damos un tamaño de 800x480 px.
		camara = new OrthographicCamera(800, 480);
		// Posicionamos la vista de la cámara para que su vértice inferior izquierdo sea (0,0)
		camara.position.set(camara.viewportWidth / 2f, camara.viewportHeight / 2f, 0);
		// Vinculamos los eventos de entrada a esta clase.
		Gdx.input.setInputProcessor(this);
		// Actualizamos la cámara.
		camara.update();

		// Cargamos la imagen del Sprite en el objeto img de la clase Texture.
		img = new Texture(Gdx.files.internal("snowspeeder.png"));

		/* --------- Código necesario para animar el Sprite. --------- */

		//Sacamos los frames de img en un array de TextureRegion.
		TextureRegion[][] tmp = TextureRegion.split(img, img.getWidth() / FRAME_COLS, img.getHeight() / FRAME_ROWS);

		/* Creamos las distintas animaciones, teniendo en cuenta que el tiempo de muestra de cada
		   frame será de 150 milisegundos, y que les pasamos las distintas filas de la matriz tmp a
		   las mismas. */
		jugadorArriba = new Animation(0.150f, tmp[0]);
		jugadorDerecha = new Animation(0.150f, tmp[1]);
		jugadorAbajo = new Animation(0.150f, tmp[2]);
		jugadorIzquierda = new Animation(0.150f, tmp[3]);
		//En principio se utiliza la animación del jugador derecha como animación por defecto.
		jugador = jugadorDerecha;
		// Posición inicial del jugador.
		jugadorX = 0;
		jugadorY = 400;
		//Ponemos a cero el atributo stateTime, que marca el tiempo e ejecución de la animación.
		stateTime = 0f;

		/*---------------------------------------------------------------*/
		// Creamos el objeto batería SpriteBatch para dibujar los Sprites en el método render().
		sb = new SpriteBatch();

		// Cargamos el mapa de baldosas desde la carpeta de assets.
		mapa = new TmxMapLoader().load("PMDM5Juego.tmx");
		mapaRenderer = new OrthogonalTiledMapRenderer(mapa);
		/*
		Determinamos el alto y ancho del mapa de baldosas. Para ello necesitamos extraer la capa
		base del mapa y, a partir de ella, determinamos el número de celdas a lo ancho y alto, así
		como el tamaño de la celda, que multiplicando por el número de celdas a lo alto y ancho, da
		como resultado el alto y ancho en pixeles del mapa.
		*/
		TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(0);
		anchoCelda = (int) capa.getTileWidth();
		altoCelda = (int) capa.getTileHeight();
		anchoMapa = capa.getWidth() * anchoCelda;
		altoMapa = capa.getHeight() * altoCelda;

		// Obtiene el ancho de la pantalla.
		anchoPant = Gdx.graphics.getWidth();
		// Obtiene el alto de la pantalla.
		altoPant = Gdx.graphics.getHeight();
		// Obtiene el ancho de la textura.
		anchoImg = img.getWidth();
		// Obtiene el alto de la textura.
		altoImg = img.getHeight();

		// Cargamos la capa de los obstáculos, que es la quinta en el TiledMap.
		capaObstaculos = (TiledMapTileLayer) mapa.getLayers().get(4);
		//String nom = capaObstaculos.getName();
		//System.out.println("Nombre: " + nom);

		// Cargamos la matriz de los obstáculos del mapa de baldosas.
		int anchoCapa = capaObstaculos.getWidth(), altoCapa = capaObstaculos.getHeight();
		obstaculo = new boolean[anchoCapa][altoCapa];
		for (int x = 0; x < anchoCapa; x++) {
			for (int y = 0; y < altoCapa; y++) {
				obstaculo[x][y] = (capaObstaculos.getCell(x, y) != null);
			}
		}

		// Cargamos la capa de los muros, que es la segunda en el TiledMap.
		capaMuros = (TiledMapTileLayer) mapa.getLayers().get(1);

		// Cargamos la matriz de los muros del mapa de baldosas.
		muros = new boolean[anchoCapa][altoCapa];
		for (int x = 0; x < anchoCapa; x++) {
			for (int y = 0; y < altoCapa; y++) {
				muros[x][y] = (capaMuros.getCell(x, y) != null);
			}
		}

		// Cargamos la capa de los agujeros, que es la tercera en el TiledMap.
		capaAgujeros = (TiledMapTileLayer) mapa.getLayers().get(2);

		// Cargamos la matriz de los obstáculos del mapa de baldosas.
		agujeros = new boolean[anchoCapa][altoCapa];
		for (int x = 0; x < anchoCapa; x++) {
			for (int y = 0; y < altoCapa; y++) {
				agujeros[x][y] = (capaAgujeros.getCell(x, y) != null);
			}
		}

		// Cargamos la capa de los premios, que es la sexta en el TiledMap.
		capaPremios = (TiledMapTileLayer) mapa.getLayers().get(5);

		// Cargamos la matriz de los premios del mapa de baldosas.
		premios = new boolean[anchoCapa][altoCapa];
		for (int x = 0; x < anchoCapa; x++) {
			for (int y = 0; y < altoCapa; y++) {
				premios[x][y] = (capaPremios.getCell(x, y) != null);
			}
		}

		// Cargamos la capa de la decoración, que es la cuarta en el TiledMap para obtener la celda de salida.
		capaDecoracion = (TiledMapTileLayer) mapa.getLayers().get(3);

		// Cargamos en los atributos del ancho y alto del sprite sus valores.
		cuadroActual = (TextureRegion) jugador.getKeyFrame(stateTime);
		anchoJugador = cuadroActual.getRegionWidth();
		altoJugador = cuadroActual.getRegionHeight();

		/* --------- Inicializamos el apartado referente a los NPC --------- */

		// Arrays para contener los enemigos y sus posiciones.
		noJugador = new Animation[numeroNPCs];
		noJugadorX = new float[numeroNPCs];
		noJugadorY = new float[numeroNPCs];
		//destinoX = new float[numeroNPCs];
		//destinoY = new float[numeroNPCs];

		/* --------- Creamos las animaciones posicionales de los NPC --------- */

		// Cargamos la imagen de los frames del enemigo en el objeto img de la clase Texture.
		img = new Texture(Gdx.files.internal("tie.png"));

		// Extraemos los frames de img en un array de TextureRegion.
		tmp = TextureRegion.split(img, img.getWidth() / FRAME_COLS, img.getHeight() / FRAME_ROWS);

		/* Creamos las distintas animaciones, teniendo en cuenta que el tiempo de muestra de cada
		   frame será de 150 milisegundos, y que les pasamos las distintas filas de la matriz tmp a
		   las mismas. */

		noJugadorArriba = new Animation(0.150f, tmp[0]);
		noJugadorArriba.setPlayMode(Animation.PlayMode.LOOP);
		noJugadorDerecha = new Animation(0.150f, tmp[1]);
		noJugadorDerecha.setPlayMode(Animation.PlayMode.LOOP);
		noJugadorAbajo = new Animation(0.150f, tmp[2]);
		noJugadorAbajo.setPlayMode(Animation.PlayMode.LOOP);
		noJugadorIzquierda = new Animation(0.150f, tmp[3]);
		noJugadorIzquierda.setPlayMode(Animation.PlayMode.LOOP);

		// Cargamos en los atributos del ancho y alto del sprite los valores del enemigo.
		cuadroActual = (TextureRegion) noJugadorDerecha.getKeyFrame(stateTimeNPC);
		anchoNoJugador = cuadroActual.getRegionWidth();
		altoNoJugador = cuadroActual.getRegionHeight();

		// Se van a crear tantos NPC como nos haya salido aleatoriamente al principio.
		for (int i=0;i<numeroNPCs;i++) {
			creaNPC(i);
		}

		/* ----- Creamos y definimos los Sprites de los disparos. ----- */

		// Asignamos la imagen al objeto sprite para que pueda ser presentada en pantalla.
		img = new Texture(Gdx.files.internal("disparoAba.png"));
		// Creamos el disparo del AT-ST hacia abajo.
		disp1 = new Sprite(img);
		disp1X = 650;
		disp1Y = 367;
		anchoDisp1 = (int) disp1.getWidth();
		altoDisp1 = (int) disp1.getHeight();
		// Asignamos la imagen al objeto sprite para que pueda ser presentada en pantalla.
		img = new Texture(Gdx.files.internal("disparoIzq.png"));
		// Creamos el disparo del AT-ST hacia la izquierda.
		disp2 = new Sprite(img);
		disp2X = 720;
		disp2Y = 297;
		anchoDisp2 = (int) disp2.getWidth();
		altoDisp2 = (int) disp2.getHeight();
		// Asignamos la imagen al objeto sprite para que pueda ser presentada en pantalla.
		img = new Texture(Gdx.files.internal("canonIzq.png"));
		// Creamos el disparo del cañón.
		disp3 = new Sprite(img);
		disp3X = 590;
		disp3Y = 96;
		anchoDisp3 = (int) disp3.getWidth();
		altoDisp3 = (int) disp3.getHeight();

		// Ponemos a 0 el atributo stateTimeNPC, que marca el tiempo de ejecución de la animación de los NPC.
		stateTimeNPC = 0f;

		// Llamamos al método para inicializar los sonidos.
		creaSonidos();

		/* Se inicializan, la animación por defecto y, de forma aleatoria, las posiciones iniciales
		   y finales de los NPC. Para simplificar un poco, los NPC pares, se moverán de forma
		   vertical y los impares de forma horizontal. */
/*		for (int i=0;i<numeroNPCs;i++) {
			noJugadorX[i] = (float) (Math.random()*anchoMapa);
			noJugadorY[i] = (float) (Math.random()*altoMapa);
			if (i%2==0 ){
				destinoX[i] = noJugadorX[i];
				destinoY[i] = (float) (Math.random()*altoMapa);
				// Determinamos cual de las animaciones verticales se utiliza.
				if (noJugadorY[i] < destinoY[i]){
					noJugador[i] = noJugadorArriba;
				} else {
					noJugador[i] = noJugadorAbajo;
				}
			} else {
				destinoX[i] = (float) (Math.random()*anchoMapa);
				destinoY[i] = noJugadorY[i];
				// Determinamos cual de las animaciones horizontales se utiliza.
				if (noJugadorX[i] < destinoX[i]){
					noJugador[i] = noJugadorDerecha;
				} else {
					noJugador[i] = noJugadorIzquierda;
				}
			}
		}*/
	}

	/**
	 * Muestra en pantalla los Sprites, mapas y demás elementos.
	 * Invoca a los diferentes métodos encargados de actualizar tanto la posición como el estado de
	 * cada elemento.
	 */
	@Override
	public void render () {
		// Almacenamos las coordenadas del jugador antes de que se modifiquen para comprobas las colisiones.
		float jugadorAnteriorX = jugadorX;
		float jugadorAnteriorY = jugadorY;
		// Si se ha perdido la partida.
		if (finJuego) {
			// Apagamos todos los sonidos.
			apagaSonido();
			// Mostramos la pantalla final.
			muestrarPantallaFin("Pierde.png", musicaPierde);
			// Reproducimos la música correspondiente.
			musicaPierde.play();
			// Tras 7,5 segundos cerramos la aplicación.
			Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					// Salimos de la aplicación.
					Gdx.app.exit();
				}
			}, 7,5);

		} else if (ganaJuego) {
			// Apagamos todos los sonidos.
			apagaSonido();
			// Mostramos la pantalla final.
			muestrarPantallaFin("Gana.png", musicaGana);
			// Reproducimos la música correspondiente.
			musicaGana.play();
			// Tras 4 segundos cerramos la aplicación.
			Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					// Salimos de la aplicación.
					Gdx.app.exit();
				}
			}, 4);
		} else {
			// Ponemos el color del fondo de la pantalla en gris claro.
			Gdx.gl.glClearColor(10, 10, 10, 1);
			// Borramos el contenido de la pantalla.
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			// Trasladamos la cámara para que se centre en el Sprite.
			camara.position.set(jugadorX, jugadorY, 0f);

			/*
			Comprobamos que la cámara no se salga de los límites del mapa de baldosas.
			Verificamos, con el método clamp(), que el valor de la posición X de la cámara esté entre la
			mitad de la anchura de la vista de la cámara y la diferencia entre la anchura del mapa
			restando la mitad de la anchura de la vista de la cámara.
			 */
			camara.position.x = MathUtils.clamp(camara.position.x, camara.viewportWidth / 2f,
					anchoMapa - camara.viewportWidth / 2f);
			/*
			Verificamos, con el método clamp(), que el valor de la posición Y de la cámara esté entre la
			mitad de la altura de la vista de la cámara y la diferencia entre la altura del mapa restando
			la mitad de la altura de la vista de la cámara.
			*/
			camara.position.y = MathUtils.clamp(camara.position.y, camara.viewportHeight / 2f,
					altoMapa - camara.viewportHeight / 2f);

			// Actualizamos la cámara del juego.
			camara.update();
			// Vinculamos el objeto que dibuja el TiledMap con la cámara del juego.
			mapaRenderer.setView(camara);

			// Dibujamos todas capas del TiledMap menos la de los gráficos con altura.
			int[] capas = {0, 1, 2, 3, 4, 5};
			mapaRenderer.render(capas);

			// Dibujamos el TiledMap.
			//mapaRenderer.render();

			/* --------- Código necesario para animar el Sprite. --------- */

			// Extraemos el tiempo de la última actualización del sprite y la acumulamos a stateTime.
			stateTime += Gdx.graphics.getDeltaTime();
			// Hacemos lo mismo para los NPC.
			stateTimeNPC += Gdx.graphics.getDeltaTime();
			// Extraermos el frame que debe ir asociado al momento actual.
			cuadroActual = (TextureRegion) jugador.getKeyFrame(stateTime);
			// Le indicamos al SpriteBatch que se muestre en el sistema de coordenadas específicas de la cámara.
			sb.setProjectionMatrix(camara.combined);

			/* --- Mientras los booleanos sean true se irá desplazando el jugador. --- */
			if (arriba) {
				jugadorY += 3;
				jugador = jugadorArriba;
			}
			if (abajo) {
				jugadorY += -3;
				jugador = jugadorAbajo;
			}
			if (izquierda) {
				jugadorX += -3;
				jugador = jugadorIzquierda;
			}
			if (derecha) {
				jugadorX += 3;
				jugador = jugadorDerecha;
			}

			// Llamamos al método para comprobar que el jugador no choca con los obstáculos.
			detectaObstaculos(jugadorAnteriorX, jugadorAnteriorY);

			// Inicializamos el objeto batería SpriteBatch.
			sb.begin();

			// Dibuja la textura en la posición 200, 50 modificando su tamaño.
			//sb.draw(img, (anchoPant-anchoImg)/2, (altoPant-altoImg)/2, 200, 50);

			/* Pintamos el objeto Sprite a través del objeto SpriteBatch usando la textura
			   cuadroActual y las coordenadas jugadorX y jugadorY. */
			sb.draw(cuadroActual, jugadorX, jugadorY); // 2

			// Dibujamos las animaciones de los NPC.
			for (int i = 0; i < numeroNPCs; i++) {
				actualizaNPC(i, 0.2f);
				cuadroActual = (TextureRegion) noJugador[i].getKeyFrame(stateTimeNPC);
				sb.draw(cuadroActual, noJugadorX[i], noJugadorY[i]);
			}

			// Actualizamos las posiciones de los disparos.
			actualizaDisparos();

			// Dibujamos los disparos.
			sb.draw(disp1, disp1X, disp1Y);
			sb.draw(disp2, disp2X, disp2Y);
			sb.draw(disp3, disp3X, disp3Y);

			// Desplazamos los disparos.
			disp1.translate(0,disp1Y);
			disp2.translate(disp2X,0);
			disp3.translate(0,disp3Y);

			// Llamamos al método que detecta si se producen colisiones entre los enemigos y el jugador.
			detectaColisiones();
			detectaAgujeros();
			detectaPremios();
			detectaDisparos();

			// Finalizamos la batería SpriteBatch.
			sb.end();

			// Pintamos la última capa del mapa de baldosas que es la de los gráficos con altura.
			capas = new int[1];
			capas[0] = 6;
			mapaRenderer.render(capas);

			// Comprobamos si se han obtenido 4 o más premios.
			if (numPremios >= 4){
				// Si el jugador está en la salida gana el juego.
				if (compruebaSalida(jugadorX, jugadorY)) ganaJuego = true;
			}
		}
	}

	/**
	 * Método para crear un NPC.
	 *
	 * Se inicializan, la animación por defecto y, de forma aleatoria, las posiciones iniciales de
	 * los NPC.
	 * Dichas posiciones se establecen en la mitad derecha e inferior de la pantalla respectivamente.
	 * Las finales se hacen coincidir con el jugador para que vayan a buscarlo, dicha posición final
	 * se actualiza con el movimiento del jugador.
	 */
	public void creaNPC(int i) {
		/* Inicializamos la animación por defecto del enemigo en la mitad derecha e inferior de la
		   pantalla respectivamente. */
		noJugadorX[i] = (float) (Math.random()*(anchoMapa-anchoMapa/2)+(anchoMapa / 2));
		noJugadorY[i] = (float) (Math.random()*altoMapa/2);

		destinoX = jugadorX;
		destinoY = jugadorY;
		// Determinamos cual de las animaciones verticales se utiliza.
		if (noJugadorY[i] < destinoY){
			noJugador[i] = noJugadorArriba;
		} else {
			noJugador[i] = noJugadorAbajo;
		}
		// Determinamos cual de las animaciones horizontales se utiliza.
		if (noJugadorX[i] < destinoX){
			noJugador[i] = noJugadorDerecha;
		} else {
			noJugador[i] = noJugadorIzquierda;
		}
	}

	/**
	 * Método que permite cambiar las coordenadas del NPC número "i", con una variación "delta" en ambas coordenadas.
	 */
/*
	private void actualizaNPC(int i, float delta) {
		if (destinoY[i]>noJugadorY[i]) {
			noJugadorY[i] += delta;
			noJugador[i] = noJugadorArriba;
		}
		if (destinoY[i]<noJugadorY[i]) {
			noJugadorY[i] -= delta;
			noJugador[i] = noJugadorAbajo;
		}
		if (destinoX[i]>noJugadorX[i]) {
			noJugadorX[i] += delta;
			noJugador[i] = noJugadorDerecha;
		}
		if (destinoX[i]<noJugadorX[i]) {
			noJugadorX[i] -= delta;
			noJugador[i] = noJugadorIzquierda;
		}
	}
*/

	/**
	 * Permite cambiar las coordenadas del NPC número "i", con una variación "delta" en ambas coordenadas.
	 * También actualiza las coordenadas de destino del NPC a las que tiene el jugador en ese
	 * momento para que lo persiga.
	 * @param i número de NPC.
	 * @param delta desplazamiento.
	 */
	private void actualizaNPC(int i, float delta) {
		float NPCAnteriorX = noJugadorX[i];
		float NPCAnteriorY = noJugadorY[i];

		if (destinoY>noJugadorY[i]) {
			noJugadorY[i] += delta;
			noJugador[i] = noJugadorArriba;
		}
		if (destinoY<noJugadorY[i]) {
			noJugadorY[i] -= delta;
			noJugador[i] = noJugadorAbajo;
		}
		if (destinoX>noJugadorX[i]) {
			noJugadorX[i] += delta;
			noJugador[i] = noJugadorDerecha;
		}
		if (destinoX<noJugadorX[i]) {
			noJugadorX[i] -= delta;
			noJugador[i] = noJugadorIzquierda;
		}

		// Comprobamos si los NPC chocan con los obstáculos de forma similar a como lo hace el jugador.
		//detectaColisionesNPC(i, NPCAnteriorX, NPCAnteriorY, delta);

		// Actualizamos la posixión final de los NPC.
		destinoX = jugadorX;
		destinoY = jugadorY;
	}

	/**
	 * Detecta si se producen colisiones entre el jugador y los NPC.
	 * Comprueba si se solapan sus respectivos rectángulos y si es así se termina el juego.
	 */
	private void detectaColisiones() {
		/* Vamos a comprobar que el rectángulo que rodea al jugador, no se solape con el rectángulo
		   de alguno de los NPC. */
		// Calculamos el rectángulo en torno al jugador.
		Rectangle rJugador = new Rectangle(jugadorX, jugadorY, anchoJugador, altoJugador);
		Rectangle rNPC;

		/* Ahora recorremos el array de NPC, para cada uno de ellos generamos su rectángulo
		   envolvente y comprobamos si se solapa o no con el del Jugador. */
		for (int i=0; i<numeroNPCs; i++) {
			// Calculamos los rectángulos en torno a los NPC.
			rNPC = new Rectangle(noJugadorX[i], noJugadorY[i], anchoNoJugador, altoNoJugador);
			// Comprobamos si se solapan.
			if (rJugador.overlaps(rNPC)){
				//System.out.println("¡Colisión!");
				// Activamos el sonido de Colisión.
				sonidoColisionEnemigo.play(0.99f);
				// Cambiamos la variable de perder el juego.
				finJuego = true;
			}
		}
	}

	/**
	 * Detecta si se producen colisiones entre el jugador y los muros u obstáculos.
	 * @param jugadorAnteriorX posición X anterior del jugador.
	 * @param jugadorAnteriorY posición Y anterior del jugador.
	 */
	private void detectaObstaculos(float jugadorAnteriorX, float jugadorAnteriorY) {
		/* Detectamos las colisiones con los obstáculos del mapa y si el jugador se sale del mismo. */
		if ((jugadorX < 0 || jugadorY < 0
				|| jugadorX > (anchoMapa - anchoJugador) || jugadorY > (altoMapa - altoJugador)) ||
				((obstaculo[(int) ((jugadorX + anchoJugador / 4) / anchoCelda)][((int) (jugadorY) / altoCelda)]) ||
						(obstaculo[(int) ((jugadorX + 3 * anchoJugador / 4) / anchoCelda)][((int) (jugadorY) / altoCelda)])) ||
				((muros[(int) ((jugadorX + anchoJugador / 4) / anchoCelda)][((int) (jugadorY) / altoCelda)]) ||
						(muros[(int) ((jugadorX + 3 * anchoJugador / 4) / anchoCelda)][((int) (jugadorY) / altoCelda)]))) {
			jugadorX = jugadorAnteriorX;
			jugadorY = jugadorAnteriorY;
			// Activamos el sonido de choque.
			sonidoObstaculo.play(0.5f);
		}else {
			// Si no hay colisiones activamos el sonido de vuelo del jugador.
			sonidoVuelo.play(0.5f);
		}
	}

	/**
	 * Detecta si se producen colisiones entre el jugador y los NPC.
	 * Comprueba si se solapan sus respectivos rectángulos y si es así se termina el juego.
	 * @param i número de NPC.
	 * @param NPCAnteriorX posición X anterior del NPC.
	 * @param NPCAnteriorY posición Y anterior del NPC.
	 * @param delta velocidad del NPC.
	 */
	private void detectaColisionesNPC(int i, float NPCAnteriorX, float NPCAnteriorY, float delta) {

		// Detectamos las colisiones con los obstáculos del mapa y si el NPC se sale del mismo.
		if ((noJugadorX[i] < 0 || noJugadorY[i] < 0 || noJugadorX[i] > (anchoMapa - anchoNoJugador) || noJugadorY[i] > (altoMapa - altoNoJugador)) ||
				((obstaculo[(int) ((noJugadorX[i] + anchoNoJugador / 4) / anchoCelda)][((int) (noJugadorY[i]) / altoCelda)]) ||
						(obstaculo[(int) ((noJugadorX[i] + 3 * anchoNoJugador / 4) / anchoCelda)][((int) (noJugadorY[i]) / altoCelda)])) ||
				((muros[(int) ((noJugadorX[i] + anchoNoJugador / 4) / anchoCelda)][((int) (noJugadorY[i]) / altoCelda)]) ||
						(muros[(int) ((noJugadorX[i] + 3 * anchoNoJugador / 4) / anchoCelda)][((int) (noJugadorY[i]) / altoCelda)]))) {

			// Si el NPC viene de abajo lo volvemos hacia abajo.
			if (noJugadorY[i]>NPCAnteriorY) {
				noJugadorY[i] -= delta*3;
				noJugador[i] = noJugadorAbajo;
				destinoX = jugadorX;
			}
			// Si el NPC viene de arriba lo volvemos hacia arriba.
			if (noJugadorY[i]<NPCAnteriorY) {
				noJugadorY[i] += delta*3;
				noJugador[i] = noJugadorArriba;
				destinoX = jugadorX;
			}
			// Si el NPC viene de la izquierda lo volvemos hacia atrás.
			if (noJugadorX[i]>NPCAnteriorX) {
				noJugadorX[i] -= delta*3;
				noJugador[i] = noJugadorIzquierda;
				destinoY = jugadorY;
			}
			// Si el NPC viene de la derecha lo volvemos hacia alante.
			if (noJugadorX[i]<NPCAnteriorX) {
				noJugadorX[i] += delta*3;
				noJugador[i] = noJugadorDerecha;
				destinoY = jugadorY;
			}
			//noJugadorX[i] = NPCAnteriorX;
			//noJugadorY[i] = NPCAnteriorY;
		}
	}

	/**
	 * Médodo que controla el movimiento de los disparos.
	 */
	private void actualizaDisparos() {
		// Si el Sprite del disparo se encuentra en el principio reproducimos su sonido.
		// Lo hacemos aquí para que al arrancar el juego también suenen.
		if (disp1Y == 367) sonidoDisparo1.play(.25f);
		if (disp2X == 720) sonidoDisparo1.play(.25f);
		if (disp3X == 590) sonidoDisparo3.play(.25f);

		// Comprobamos si el disparo 1 no ha llegado al final y modificamos su coordenada Y.
		if (disp1Y > 0){
			disp1Y -=2;
		} else {// Si ha llegado al final reiniciamos la coordenada.
			disp1Y = 367;
		}

		// Comprobamos si el disparo 2 no ha llegado al final y modificamos su coordenada X.
		if (disp2X > 0){
			disp2X -=2;
		} else {// Si ha llegado al final reiniciamos la coordenada.
			disp2X = 720;
		}

		// Comprobamos si el disparo 3 no ha llegado al final y modificamos su coordenada Y.
		if (disp3X > 0){
			disp3X -=2;
		} else {// Si ha llegado al final reiniciamos la coordenada.
			disp3X = 590;
		}
	}

	/**
	 * Detecta si los disparos alcanzan al jugador.
	 * Comprueba si se solapan sus respectivos rectángulos y si es así reproduce el sonido de
	 * explosión y se termina el juego.
	 */
	private void detectaDisparos() {
		/* Vamos a comprobar que el rectángulo que rodea al jugador, no se solape con el rectángulo
		   de alguno de los disparos. */
		// Calculamos el rectángulo en torno al jugador.
		Rectangle rJugador = new Rectangle(jugadorX, jugadorY, anchoJugador, altoJugador);
		// Calculamos los rectángulos en torno a los disparos.
		Rectangle rDisp1 = new Rectangle(disp1X, disp1Y, anchoDisp1, altoDisp1);
		Rectangle rDisp2 = new Rectangle(disp2X, disp2Y, anchoDisp2, altoDisp2);
		Rectangle rDisp3 = new Rectangle(disp3X, disp3Y, anchoDisp3, altoDisp3);
		// Detectamos si alguno de los disparos da al jugador.
		if (rJugador.overlaps(rDisp1) || rJugador.overlaps(rDisp2) || rJugador.overlaps(rDisp3)){
			//System.out.println("¡Disparo!");
			// Activamos el sonido de Explosión.
			sonidoExplota.play();
			// Cambiamos la variable de perder el juego.
			finJuego = true;
		}
	}

	/**
	 * Detecta si el jugador pasa sobre un agujero.
	 * Comprueba si el jugador pasa por encima y si es así se termina el juego.
	 */
	private void detectaAgujeros() {
		if ((agujeros[(int) ((jugadorX + anchoJugador/4) / anchoCelda)][((int) (jugadorY) / altoCelda)])
				|| (agujeros[(int) ((jugadorX + 3*anchoJugador/4) / anchoCelda)][((int) (jugadorY) / altoCelda)])) {
			//System.out.println("¡Agujero!");
			sonidoColisionEnemigo.play(0.25f);
			finJuego = true;
		}
	}

	/**
	 * Detecta si el jugador pasa sobre un premio.
	 * Si es así reproduce el sonido de premio, aumenta el contador de los mismos y elimina el
	 * premio en cuestión.
	 * También comprueba si los premios son 4 o más y elimina la celda de puerta cerrada para que
	 * se pueda terminar el juego.
	 */
	private void detectaPremios() {
		int x, yant;//y
		yant = (int)(jugadorY / altoCelda);
		//y = (int) (capaPremios.getHeight() - yant);
/*
		if(celdaPremio(jugadorX, jugadorY)){
			//System.out.println("¡Premio!");
			TiledMapTileLayer.Cell celda = capaPremios.getCell(
			(int) (jugadorX / capaPremios.getTileWidth()),
			(int) (jugadorY / capaPremios.getTileHeight()));
			celda.setTile(null);
			sonidoPremio.play(0.25f);
			numPremios ++;
		}
*/
		if ((premios[(int) ((jugadorX + anchoJugador/4) / anchoCelda)][yant])
				|| (premios[(int) ((jugadorX + 3*anchoJugador/4) / anchoCelda)][yant])) {
			//System.out.println("¡Premio!");

			if ((premios[(int) ((jugadorX + anchoJugador/4) / anchoCelda)][yant])){
				x = (int)((jugadorX + anchoJugador/4) / anchoCelda);
			} else {
				x = (int) ((jugadorX + 3*anchoJugador/4) / anchoCelda);
			}

			// Elimina el Tile del premio.
			//TiledMapTileLayer.Cell celda = capaPremios.getCell(x, yant);
			//celda.setTile(null);
			//capaPremios.getCell(x, yant).setTile(null);
			capaPremios.setCell(x, yant, null);
			// Desactiva el Tile del Array de premios.
			premios[x][yant] = false;
			// Sonido del premio.
			sonidoPremio.play(0.99f);
			// Aumentamos la cuenta de premios.
			numPremios ++;
			// Si tenemos 4 premios abrimos la puerta de salida ocultando su Tile de la capa de ostáculos.
			if (numPremios == 4){
				sonidoPuerta.play();
				capaObstaculos.setCell(24, 3, null);
				obstaculo[24][3] = false;
				//System.out.println("¡Puerta!");
			}
		}
	}

	/**
	 * Inicializa la música y los sonidos para que estén listos para reproducir.
	 * Reproduce en bucle la música de fondo para que esté presente mientras dure el juego.
	 */
	private void creaSonidos() {
		// Inicializamos la música de fondo del juego y la reproducimos.
		musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Scherzo for X-Wings.mp3"));
		// Le damos un volumen del 25%
		musicaFondo.setVolume(.25f);
		// Establecemos que se reproduzca en bucle.
		musicaFondo.setLooping(true);
		// Reproducimos la música.
		musicaFondo.play();

		// Inicializamos los atributos de los efectos de sonido.
		sonidoColisionEnemigo = Gdx.audio.newSound(Gdx.files.internal("colision.ogg"));
		sonidoVuelo = Gdx.audio.newSound(Gdx.files.internal("vuelo.ogg"));
		sonidoObstaculo = Gdx.audio.newSound(Gdx.files.internal("muro.wav"));
		sonidoPremio = Gdx.audio.newSound(Gdx.files.internal("premio.wav"));
		sonidoPuerta = Gdx.audio.newSound(Gdx.files.internal("puerta.ogg"));
		sonidoDisparo1 = Gdx.audio.newSound(Gdx.files.internal("disp1.wav"));
		sonidoDisparo3 = Gdx.audio.newSound(Gdx.files.internal("disp3.ogg"));
		sonidoExplota = Gdx.audio.newSound(Gdx.files.internal("explota.mp3"));

		// Inicializamos la música de para el caso de que se gane el juego.
		musicaGana = Gdx.audio.newMusic(Gdx.files.internal("Gana.mp3"));
		// Le damos un volumen del 15%
		musicaGana.setVolume(.15f);
		// Inicializamos la música de para el caso de que se gane el juego.
		musicaPierde = Gdx.audio.newMusic(Gdx.files.internal("Pierde.mp3"));
		// Le damos un volumen del 15%
		musicaPierde.setVolume(.15f);
	}

	/**
	 * Libera la memoria de los recursos que incluimos.
	 * Limpiando del buffer los elementos.
	 */
	@Override
	public void dispose () {
		sb.dispose();
		img.dispose();
		mapa.dispose();
		musicaFondo.dispose();
		sonidoObstaculo.dispose();
		sonidoVuelo.dispose();
		sonidoColisionEnemigo.dispose();
		sonidoPuerta.dispose();
		sonidoPremio.dispose();
		sonidoDisparo1.dispose();
		sonidoDisparo3.dispose();
		sonidoExplota.dispose();
		musicaGana.dispose();
		musicaPierde.dispose();
	}

	/**
	 * Detecta la pulsación de una tecla.
	 * En este caso pone a true el booleano correspondiente a la dirección de la tecla pulsada.
	 * También pone a 0 el stateTime del jugador y actualiza las coordenadas de destino de los NPC.
	 * @param keycode código de la tecla pulsada.
	 * @return
	 */
	@Override
	public boolean keyDown(int keycode) {
		/*
		Al final lo he hecho como tu dices, dejando pulsado que es mas jugable.
		La idea es usar KeyDown para activar un boolean con la direccion y KeyUp lo desactiva.
		Luego comprobamos si el boolean esta activado para ver si hay movimiento o no.
		Al final no es tan difícil como parece.
		*/
		/* Movimiento del fondo de pantalla cuando se produzcan los eventos.

		//Si pulsamos uno de los cursores, se desplaza la cámara 32 pixels de forma adecuada.
		if(keycode == Input.Keys.LEFT)
			camara.translate(-32,0);
		if(keycode == Input.Keys.RIGHT)
			camara.translate(32,0);
		if(keycode == Input.Keys.UP)
			camara.translate(0,32);
		if(keycode == Input.Keys.DOWN)
			camara.translate(0,-32);
		---------------------------------------------------------------*/

		/* Movimiento del Sprite cuando se produzcan los eventos.

		// Si pulsamos uno de los cursores, se desplaza el sprite un pixel de forma adecuada.
		if(keycode == Input.Keys.LEFT)
			sprite.translate(-1,0);
		if(keycode == Input.Keys.RIGHT)
			sprite.translate(1,0);
		if(keycode == Input.Keys.UP)
			sprite.translate(0,1);
		if(keycode == Input.Keys.DOWN)
			sprite.translate(0,-1);
		---------------------------------------------------------------*/

		/* Movimiento del Sprite Animado cuando se produzcan los eventos. */

		/* Si pulsamos uno de los cursores, se desplaza el sprite de forma adecuada 5 pixeles, y se
		 pone a cero el atributo que marca el tiempo de ejecución de la animación, provocando que la
		  misma se reinicie. */

		/* Guardamos la posición anterior del jugador por si al desplazarlo topa con un obstáculo,
		   podamos volverlo a la posición anterior. */
		//float jugadorAnteriorX = jugadorX;
		//float jugadorAnteriorY = jugadorY;

		stateTime = 0;

		if (keycode == Input.Keys.UP) {
			arriba = true;
			//jugadorY += 10;
			//jugador = jugadorArriba;
		}
		if (keycode == Input.Keys.DOWN) {
			abajo = true;
			//jugadorY += -10;
			//jugador = jugadorAbajo;
		}
		if (keycode == Input.Keys.LEFT) {
			izquierda = true;
			//jugadorX += -10;
			//jugador = jugadorIzquierda;
		}
		if (keycode == Input.Keys.RIGHT) {
			derecha = true;
			//jugadorX += 10;
			//jugador = jugadorDerecha;
		}

		// Llamamos al método para comprobar que el jugador no choca con los obstáculos.
		//detectaObstaculos(jugadorAnteriorX, jugadorAnteriorY);

		// Actualizamos la posixión final de los NPC.
		destinoX = jugadorX;
		destinoY = jugadorY;
/*
		// Si pulsamos la tecla del número 1, se alterna la visibilidad de la primera capa del mapa de baldosas.
		if (keycode == Input.Keys.NUM_1)
			mapa.getLayers().get(0).setVisible(!mapa.getLayers().get(0).isVisible());
		//Si pulsamos la tecla del número 2, se alterna la visibilidad de la segunda capa del mapa de baldosas.
		if (keycode == Input.Keys.NUM_2)
			mapa.getLayers().get(1).setVisible(!mapa.getLayers().get(1).isVisible());
*/
		return false;
	}

	/**
	 * Detecta la liberación de una tecla.
	 * En este caso pone a false lo booleanos de las direcciones.
	 * @param keycode código de la tecla pulsada.
	 * @return
	 */
	@Override
	public boolean keyUp(int keycode) {
		arriba = abajo = izquierda = derecha = false;
		sonidoObstaculo.stop();
		//sonidoVuelo.stop();
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/**
	 * Detecta el toque en la pantalla táctil.
	 * En función de la posición del toque con respecto del jugador pone a true el booleano
	 * correspondiente a la dirección adecuada.
	 * También pone a 0 el stateTime del jugador y actualiza las coordenadas de destino de los NPC.
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 * @return
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		/* Movimiento del fondo de pantalla cuando se produzcan los eventos.

		int incrementox=0, incrementoy=0;
		// Si tocamos a la derecha de la posición de la cámara, ésta se mueve 32 pixels a la derecha.
		if (camara.position.x<screenX){
			incrementox = 32;
			// Si tocamos a la izquierda de la posición de la cámara, ésta se mueve 32 pixels a la izquierda.
		} else if(camara.position.x>screenX){
			incrementox = -32;
		}
		//Si tocamos por encima de la posición  de la cámara, ésta se mueve 32 pixels arriba.
		if (camara.position.y<screenX){
			incrementoy = 32;
			//Si tocamos por debajo de la posición  de la cámara, ésta se mueve 32 pixels abajo.
		} else if(camara.position.y>screenX){
			incrementoy = -32;
		}
		camara.translate(incrementox,incrementoy);
		---------------------------------------------------------------*/

		/* Movimiento del Sprite cuando se produzcan los eventos.

		// Vector en tres dimensiones que recoge las coordenadas donde se ha hecho clic o toque en la pantalla.
		Vector3 clickCoordinates = new Vector3(screenX, screenY,0);
		// Transformamos las coordenadas del vector a coordenadas de nuestra cámara.
		Vector3 position = camara.unproject(clickCoordinates);
		// Mostramos el sprite en la posición adecada respecto a nuestra cámara.
		sprite.setPosition(position.x, position.y);
		---------------------------------------------------------------*/

		/* Movimiento del Sprite Animado cuando se produzcan los eventos. */

		// Vector en tres dimensiones que recoge las coordenadas donde se ha hecho clic o toque de la pantalla.
		Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
		// Transformamos las coordenadas del vector a coordenadas de nuestra cámara.
		Vector3 posicion = camara.unproject(clickCoordinates);

		/* Guardamos la posición anterior del jugador por si al desplazarlo topa con un obstáculo,
		   podamos volverlo a la posición anterior. */
		float jugadorAnteriorX = jugadorX;
		float jugadorAnteriorY = jugadorY;

		// Se pone a cero el atributo que marca el tiempo de ejecución de la animación, provocando que la misma se reinicie.
		stateTime = 0;
		/* Si se pulsa por encima de la animación más del alto del sprite, ésta sube 10 píxeles y se
		   reproduce la animación del jugador desplazándose hacia arriba. */
		if ((jugadorY + altoJugador) < posicion.y) {
			arriba = true;
			//jugadorY += 10;
			//jugador = jugadorArriba;
			/* Si se pulsa por debajo de la animación, ésta baja 10 píxeles y se reproduce la
			   animación del jugador desplazándose hacia abajo. */
		} else if (jugadorY > posicion.y) {
			abajo = true;
			//jugadorY -= 10;
			//jugador = jugadorAbajo;
		}
		/* Si se pulsa a la derecha de la animación más de la mitad del ancho del sprite, ésta se
		   mueve 10 píxeles a la derecha se reproduce la animación del jugador desplazándose hacia
		   la derecha. */
		if ((jugadorX + anchoJugador/2) < posicion.x) {
			derecha = true;
			//jugadorX += 10;
			//jugador = jugadorDerecha;
			/* Si se pulsa a la izquierda de la animación más de la mitad del ancho del sprite, ésta
			   se mueve 10 píxeles a la derecha se reproduce la animación del jugador desplazándose
			   hacia la izquierda. */
		} else if ((jugadorX - anchoJugador/2) > posicion.x) {
			izquierda = true;
			//jugadorX -= 10;
			//jugador = jugadorIzquierda;
		}

		// Llamamos al método para comprobar que el jugador no choca con los obstáculos.
		//detectaObstaculos(jugadorAnteriorX, jugadorAnteriorY);

		// Actualizamos la posixión final de los NPC.
		destinoX = jugadorX;
		destinoY = jugadorY;

		return true;
	}

	/**
	 * Detecta la liberación de la pulsación sobre la pantalla táctil.
	 * Pone a false los booleanos de las direcciones.
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 * @return
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		arriba = abajo = izquierda = derecha = false;
		sonidoObstaculo.stop();
		//sonidoVuelo.stop();
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	/**
	 * Muestra una pantalla, reproduce la música para terminar el juego y detiene el renderizado.
	 *
	 * @param fondo imagen a mostrar
	 * @param musicaFin música a reproducir
	 */
	private void muestrarPantallaFin(String fondo, Music musicaFin) {
		Texture imagen = new Texture(Gdx.files.internal(fondo));
		// Reproducimos la música final.
		musicaFin.play();
		// Mostramos la imagen final.
		sb.begin();
		sb.draw(imagen, 0, 0, anchoMapa, altoMapa);
		sb.end();

		Gdx.graphics.setContinuousRendering(false);
	}

	/**
	 * Apaga todos los sonidos del juego.
	 */
	public void apagaSonido(){
		musicaFondo.stop();
		sonidoColisionEnemigo.stop();
		sonidoVuelo.stop();
		sonidoObstaculo.stop();
		sonidoPremio.stop();
		sonidoPuerta.stop();
		sonidoDisparo1.stop();
		sonidoDisparo3.stop();
		sonidoExplota.stop();
	}

	/**
	 * Comprueba si el protagonista está en la casilla final.
	 * Cuando carga el mapa busca el tile marcado como salida.
	 *
	 * @param x Coordenada X del tile salida.
	 * @param y Coordenada Y del tile salida.
	 * @return boolean
	 */
	private boolean compruebaSalida(float x, float y) {
		/* Creamos el objeto Cell a partir de la capa donde hemos definido los obstáculos en función
		   de las coordenadas que pasamos como parámetro a este método. */
		TiledMapTileLayer.Cell cell = capaDecoracion.getCell(
				(int) (x / capaDecoracion.getTileWidth()),
				(int) (y / capaDecoracion.getTileHeight()));
		/* Devolvemos true si el objeto Cell no es nulo, y el objeto TiledMapTile que contiene no es
		   nulo y si en las propiedades del TiledMapTile hay una que de llama "Salida". */
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey("Salida");
	}

	/**
	 * Comprueba los tiles del tileset marcados como obstáculos.
	 * Cuando carga el mapa busca los tiles marcados como bloqueados.
	 *
	 * @param x Coordenada X del tile bloqueado.
	 * @param y Coordenada Y del tile bloqueado.
	 * @return boolean
	 */
	public boolean celdaBloqueada(float x, float y) {
		/* Creamos el objeto Cell a partir de la capa donde hemos definido los obstáculos en función
		   de las coordenadas que pasamos como parámetro a este método. */
		TiledMapTileLayer.Cell cell = capaObstaculos.getCell(
				(int) (x / capaObstaculos.getTileWidth()),
				// Otra opción.
				//(int) ((x + anchoJugador/2) / capaObstaculos.getTileWidth()),
				(int) (y / capaObstaculos.getTileHeight()));
		/* Devolvemos true si el objeto Cell no es nulo, y el objeto TiledMapTile que contiene no es
		   nulo y si en las propiedades del TiledMapTile hay una que de llama "bloqueado". */
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey("bloqueado");
	}

	/**
	 * Comprueba los tiles del tileset marcados como premios.
	 * Cuando carga el mapa busca los tiles marcados como premios.
	 *
	 * @param x Coordenada X del tile marcado como Premio.
	 * @param y Coordenada Y del tile marcado como Premio.
	 * @return boolean
	 */
	public boolean celdaPremio(float x, float y) {
		/* Creamos el objeto Cell a partir de la capa donde hemos definido los obstáculos en función
		   de las coordenadas que pasamos como parámetro a este método. */
		TiledMapTileLayer.Cell cell = capaPremios.getCell(
				(int) (x / capaPremios.getTileWidth()),
				(int) (y / capaPremios.getTileHeight()));
		/* Devolvemos true si el objeto Cell no es nulo, y el objeto TiledMapTile que contiene no es
		   nulo y si en las propiedades del TiledMapTile hay una que de llama "Premio". */
		// Elimina el Tile del premio.
		//cell.setTile(null);
		//cell.setCell(x, yant, null);
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey("Premio");
	}
}
