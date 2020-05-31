package com.example.pmdm5juego.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.example.pmdm5juego.PMDM5Juego;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// Indicamos el título de la ventana del juego.
		config.title = "PMDM5 Batalla en Hot";
		// Indicamos la anchura de la ventana del juego.
		config.width = 800;
		// Indicamos la altura de la ventana del juego.
		config.height = 480;
		// Lanzamos el juego en su versión de escritorio.
		new LwjglApplication(new PMDM5Juego(), config);
	}
}
