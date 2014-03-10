package plant_project;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.Animator;

public class Drawer extends WindowAdapter implements KeyListener,
		MouseListener, MouseWheelListener, MouseMotionListener,
		GLEventListener {

	private final static char NOTHING     = 'X';
	private final static char GROW        = 'F';
	private final static char GROW_FlOWER = 'L';
	private final static char NORTH       = 'n';
	private final static char EAST        = 'e';
	private final static char WEST        = 'w';
	private final static char SOUTH       = 's';
	private final static char PUSH        = '[';
	private final static char POP         = ']';

	public final static float NORTH_BOUND =  4.0f;
	public final static float EAST_BOUND  =  4.0f;
	public final static float WEST_BOUND  = -4.0f;
	public final static float SOUTH_BOUND = -4.0f;

	private enum DragMode {
		NONE,
		PAN,
		ZOOM,
		TILT
	}

	private ArrayList<Plant> plants = new ArrayList<Plant>();
	private float rotateT = 0.0f;
	private GLU   glu = null;

	// Interface the rest of the world
	private Main     main;

	// GUI Widgets
	private GLCanvas canvas;
	private Frame    frame;
	private Animator animator;

	// Camera state
	private DragMode dragMode = DragMode.NONE;
	private double   dragX, dragY;
	private double   rotation[] = {0,   0,  0};
	private double   location[] = {35, -25, 5};

	public Drawer(Main main) {
		this.main     = main;
		this.canvas   = new GLCanvas();
		this.frame    = new Frame("Plant world");
		this.animator = new Animator(canvas);
		this.glu      = new GLU();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseWheelListener(this);
		canvas.addMouseMotionListener(this);
		frame.add(canvas);
		frame.setSize(640, 480);
		frame.setResizable(true);
		frame.addWindowListener(this);
		frame.setVisible(true);
		animator.start();
		canvas.requestFocus();
	}

	/*
	 * Private drawing methods
	 */
	private void drawLine(GL2 gl, float x1, float y1, float x2, float y2) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2f((x1), (y1));
		gl.glVertex2f((x2), (y2));
		gl.glEnd();
	}

	private void drawStem(GL2 gl, Color color, float length) {
		final int   sides  = 3;
		final float radius = length/30f;

		gl.glColor3ub((byte)color.getRed(),
			      (byte)color.getGreen(),
			      (byte)color.getBlue());

		gl.glBegin(GL2.GL_QUAD_STRIP);
		for (int i = 0; i <= sides; i++) {
			float a  = (float)Math.PI * 2f / sides * i;
			float nx = (float)Math.sin(a);
			float nz = (float)Math.cos(a);
			gl.glNormal3f(nx,        0,      nz);
			gl.glVertex3f(nx*radius, 0,      nz*radius);
			gl.glVertex3f(nx*radius, length, nz*radius);
		}
		gl.glEnd();
	}

	private void drawFlower(GL2 gl, Color color) {
		final int   pedals = 5;
		final float length = 0.10f;
		final float radius = 0.05f;

		gl.glColor3ub((byte)color.getRed(),
		              (byte)color.getGreen(),
		              (byte)color.getBlue());

		// Draw pedals
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glNormal3f(0f, -1f, 0f);
		gl.glVertex3f(0f,  0f, 0f);
		for (int i = 0; i <= pedals; i++) {
			float a  = (float)Math.PI * 2f / pedals * i;
			float x  = (float)Math.sin(a) * radius;
			float z  = (float)Math.cos(a) * radius;

			float na = (float)((2f*Math.PI) / pedals)*i;
			float nx = (float)Math.sin(a) * -length;
			float nz = (float)Math.cos(a) * -length;

			float d  = (float)Math.sqrt(length*length + radius*radius);

			gl.glNormal3f(-nx/d, -radius/d, -nz/d);
			gl.glVertex3f(  x,    length,     z);

			gl.glNormal3f(-nx/d, -radius/d, -nz/d);
			gl.glVertex3f(  x,    length,     z);
		}
		gl.glEnd();
	}

	private void drawPlant(GL2 gl, Plant plant) {
		String lString    = plant.getlString();

		Color stemColor   = plant.getGene().getStemColor();
		Color flowerColor = plant.getGene().getFlowerColor();

		gl.glPushMatrix();
		// move to plant origin
		gl.glTranslatef(plant.getX(), 0.0f, plant.getY());
		for (int i = 0; i < lString.length(); i++) {
			char c = lString.charAt(i);
			switch (c) {
			case NOTHING:
				// do nothing
				break;
			case GROW:
				// drawLine
				float lineLength = 0.1f;
				this.drawStem(gl, stemColor, lineLength);
				gl.glTranslatef(0, lineLength, 0.0f);
				break;
			case GROW_FlOWER:
				if (plant.getAge() >= Plant.MAX_ITERATION)
					this.drawFlower(gl, flowerColor);
				break;
			case PUSH:
				// push position and angle
				gl.glPushMatrix();
				break;
			case POP:
				// pop position and angle
				gl.glPopMatrix();
				break;
			case NORTH:
				// rotate against north, Z
				gl.glRotatef(plant.getGene().getAngle(), 0.0f, 0.0f, 1.0f);
				gl.glRotatef(0, 0.0f, 1.0f, 0.0f);
				break;
			case EAST:
				// rotate against east, X
				gl.glRotatef(plant.getGene().getAngle(), 0.0f, 0.0f, 1.0f);
				gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
				break;
			case WEST:
				// rotate against west, -X
				gl.glRotatef(plant.getGene().getAngle(), 0.0f, 0.0f, 1.0f);
				gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
				break;
			case SOUTH:
				// rotate against south, -Z
				gl.glRotatef(plant.getGene().getAngle(), 0.0f, 0.0f, 1.0f);
				gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
				break;
			default:
				break;
			}
		}
		gl.glPopMatrix();
	}

	private void drawGround(GL2 gl) {
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(0.17f, 0.12f, 0.10f); // set the color of the ground
		gl.glNormal3f(0f, 1f, 0f);
		gl.glVertex3f(WEST_BOUND, 0.0f, NORTH_BOUND);
		gl.glVertex3f(EAST_BOUND, 0.0f, NORTH_BOUND);
		gl.glVertex3f(EAST_BOUND, 0.0f, SOUTH_BOUND);
		gl.glVertex3f(WEST_BOUND, 0.0f, SOUTH_BOUND);
		gl.glEnd();
	}


	private void drawTests(GL2 gl) {
		boolean testLighting = false;
		if (testLighting) {
			final float radius = 1f;
			final int   slices = 16;
			final int   stacks = 16;
			GLUquadric earth = glu.gluNewQuadric();
			gl.glColor3f(0.3f, 0.5f, 1f);
			gl.glTranslatef(-2f, 1f, -2f);
			glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
			glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
			glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
			glu.gluSphere(earth, radius, slices, stacks);
			glu.gluDeleteQuadric(earth);
		}
	}

	/*
	 * Public methods - called by Main
	 */
	public void draw(ArrayList<Plant> plants) {
		this.plants = plants;
	}

	public void pan(double fwd, double side, double up) {
		//System.out.println("Drawer.pan");

		double dist   = Math.sqrt(fwd*fwd + side*side);
		double angle1 = this.rotation[2] * (180/Math.PI);
		double angle2 = Math.atan2(side, fwd);
		double angle  = angle1 + angle2;

		/* This isn't accurate, but it's usable */
		this.location[0] += dist*Math.cos(angle);
		this.location[1] += dist*Math.sin(angle);
		this.location[2] += up;

		/* Fix location */
		while (this.location[0] <    1) this.location[0]  =   1;
		while (this.location[0] >   90) this.location[0]  =  90;
		while (this.location[1] < -180) this.location[1] += 360;
		while (this.location[1] >  180) this.location[1] -= 360;
		this.location[2] = Math.abs(this.location[2]);
	}

	public void rotate(double x, double y, double z) {
		//System.out.println("Drawer.rotate");

		/* set rotation */
		this.rotation[0] += x;
		this.rotation[1] += y;
		this.rotation[2] += z;

		/* fix rotation */
		while (this.rotation[0] < -180) this.rotation[0] += 360;
		while (this.rotation[0] >  180) this.rotation[0] -= 360;
		while (this.rotation[1] < -180) this.rotation[1] += 360;
		while (this.rotation[1] >  180) this.rotation[1] -= 360;
		while (this.rotation[2] < -180) this.rotation[2] += 360;
		while (this.rotation[2] >  180) this.rotation[2] -= 360;
	}

	public void zoom(double scale) {
		//System.out.println("Drawer.zoom - " + scale);

		this.location[2] *= scale;
	}

	public void quit() {
		this.animator.stop();
		this.frame.dispose();
		System.exit(0);
	}

	/*
	 * KeyListener Methods
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println("Drawer.keyPressed - " + e.getKeyChar());
		int pan = 5;
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:  this.pan( 0,  -pan, 0); break;
			case KeyEvent.VK_DOWN:  this.pan(-pan, 0,   0); break;
			case KeyEvent.VK_UP:    this.pan( pan, 0,   0); break;
			case KeyEvent.VK_RIGHT: this.pan( 0,   pan, 0); break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println("Drawer.keyReleased - " + e.getKeyChar());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//System.out.println("Drawer.keyTyped - " + e.getKeyChar());
		int pan  = 5;
		switch (e.getKeyChar()) {
			case 'h': this.pan( 0,  -pan, 0); break;
			case 'j': this.pan(-pan, 0,   0); break;
			case 'k': this.pan( pan, 0,   0); break;
			case 'l': this.pan( 0,   pan, 0); break;
			case '-': case '_': 
			case 'o': this.zoom(10./9);       break;
			case '=': case '+': 
			case 'i': this.zoom(9./10);       break;
			case 'H': this.rotate(0, 0, -2);  break;
			case 'J': this.rotate(2, 0,  0);  break;
			case 'K': this.rotate(-2, 0,  0); break;
			case 'L': this.rotate(0, 0,  2);  break;
			default:  this.main.keyPress(e.getKeyChar(), "");
		}
	}

	/*
	 * MouseListener Methods
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println("Drawer.mouseClicked");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//System.out.println("Drawer.mouseEntered");
		canvas.requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//System.out.println("Drawer.mouseExited");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//System.out.println("Drawer.mousePressed - " + e.getButton());
		switch (e.getButton()) {
			case 1:  this.dragMode = DragMode.PAN;  break;
			case 2:  this.dragMode = DragMode.ZOOM; break;
			case 3:  this.dragMode = DragMode.TILT; break;
			default: this.dragMode = DragMode.NONE; break;
		}
		this.dragX = e.getX();
		this.dragY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println("Drawer.mouseReleased");
		this.dragMode = DragMode.NONE;
	}

	/*
	 * MouseWheelListener Methods
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		//System.out.println("Drawer.mouseWheelMoved");
		int clicks = e.getWheelRotation();
		if (clicks > 0)
			this.zoom(10.0/9.0);
		if (clicks < 0)
			this.zoom(9.0/10.0);
	}

	/*
	 * MouseMotionListener Methods
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		//System.out.println("Drawer.mouseDragged");
		double x = this.dragX - e.getX();
		double y = this.dragY - e.getY();
		double scale = 1;
		// Drag on button 2, doens't work for some reason..
		switch (this.dragMode) {
			case PAN:  this.pan(-y*scale*0.782, x*scale, 0); break;
			case ZOOM: this.zoom(Math.pow(2, -y/500)); break;
			case TILT: this.rotate(y/10, 0, x/10); break;
		}
		this.dragX = e.getX();
		this.dragY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("Drawer.mouseMoved");
	}

	/*
	 * GLEventListener Methods
	 */
	@Override
	public void display(GLAutoDrawable gLDrawable) {

		final GL2 gl = gLDrawable.getGL().getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);

		// Setup projection and camera
		gl.glLoadIdentity();

		// Camera 1
		gl.glRotated( this.rotation[0], 1f, 0f, 0f); // tiltx
		gl.glRotated( this.rotation[2], 0f, 0f, 1f); // tiltz

		// Camera 2
		gl.glTranslated(0f, 0f, -this.location[2]);
		gl.glRotated( this.location[0], 1f, 0f, 0f); // lat
		gl.glRotated(-this.location[1], 0f, 1f, 0f); // lon

		// Lighting
		float light_position[] = {-40.0f, 80.0f, 40.0f, 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);

		// draw plants
		for (Plant plant : plants)
			this.drawPlant(gl, plant);

		// draw ground
		this.drawGround(gl);

		// debugging
		this.drawTests(gl);

		// swap buffers
		gl.glFlush();
	}

	@Override
	public void init(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
		gl.glClearColor(0.7f, 0.8f, 1.0f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

		/* Coloring */
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_BLEND);
		gl.glDisable(GL2.GL_ALPHA_TEST);

		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glEnable(GL2.GL_LINE_SMOOTH);

		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glDisable(GL2.GL_COLOR_MATERIAL);

		/* Lighting */
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		float light_ambient[]  = {0.4f, 0.4f, 0.4f, 1.0f};
		float light_diffuse[]  = {1.0f, 1.0f, 1.0f, 1.0f};
		float light_position[] = {-400.0f, 800.0f, 400.0f, 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT,  light_ambient,  0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE,  light_diffuse,  0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
	}

	@Override
	public void reshape(GLAutoDrawable gLDrawable,
			int x, int y, int width, int height) {
		GL2 gl = gLDrawable.getGL().getGL2();
		final double aspect = (float) width / (float) height;
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		final double near = 0.001;
		final double far  = 1000;
		final double fh   = near/2;
		final double fw   = fh * aspect;
		gl.glFrustum(-fw, fw, -fh, fh, near, far);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void dispose(GLAutoDrawable gLDrawable) {
	}

	/*
	 * WindowAdapter Methods
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		this.quit();
	}
}
