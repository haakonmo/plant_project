package plant_project;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.Animator;

public class Drawer implements GLEventListener {
	
	private final static char NOTHING = 'X';
	private final static char GROW = 'F';
	private final static char NORTH = 'n';
	private final static char EAST = 'e';
	private final static char WEST = 'w';
	private final static char SOUTH = 's';
	private final static char PUSH = '[';
	private final static char POP = ']';
	
	private ArrayList<Plant> plants = new ArrayList<Plant>();
	private float rotateT = 0.0f;
	
	public Drawer() {
		final GLCanvas canvas = new GLCanvas();
		final Frame frame = new Frame("Plant world");
		final Animator animator = new Animator(canvas);
		canvas.addGLEventListener(this);
		frame.add(canvas);
		frame.setSize(640, 480);
		frame.setResizable(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				animator.stop();
				frame.dispose();
				System.exit(0);
			}
		});
		frame.setVisible(true);
		animator.start();
		canvas.requestFocus();
	}
	
	public void draw(ArrayList<Plant> plants){
		this.plants = plants;
	}

	@Override
	public void display(GLAutoDrawable gLDrawable) {
		final GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, -2.0f, -5.0f);
 
		// rotate about the three axes
//		gl.glRotatef(rotateT, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
//		gl.glRotatef(rotateT, 0.0f, 0.0f, 1.0f);
		
		for (Plant plant : plants){
			String lString = plant.lString;
			gl.glPushMatrix();
			Color stemColor = plant.gene.stemColor;
			gl.glColor3f(stemColor.getRed()/255.0f, stemColor.getGreen()/255.0f, stemColor.getBlue()/255.0f);
			//move to plant origin
			gl.glTranslatef(plant.x, plant.y, 0.0f);
			for (int i = 0; i < lString.length(); i++) {
				char c = lString.charAt(i);
				switch (c) {
				case NOTHING:
					//do nothing
					break;
				case GROW:
					//drawLine
					float lineLength = 0.01f;
					drawLine(gl, 0, 0, 0, lineLength);
					gl.glTranslatef(0, lineLength, 0.0f);
					break;
				case PUSH:
					//push position and angle
					gl.glPushMatrix();
					break;
				case POP:
					//pop position and angle
					gl.glPopMatrix();
					break;
				case NORTH:
					//rotate against north, Z
					gl.glRotatef(plant.gene.angle, 0.0f, 0.0f, 1.0f);
					gl.glRotatef(0, 0.0f, 1.0f, 0.0f);
					break;
				case EAST:
					//rotate against east, X
					gl.glRotatef(plant.gene.angle, 0.0f, 0.0f, 1.0f);
					gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
					break;
				case WEST:
					//rotate against west, -X
					gl.glRotatef(plant.gene.angle, 0.0f, 0.0f, 1.0f);
					gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
					break;
				case SOUTH:
					//rotate against south, -Z
					gl.glRotatef(plant.gene.angle, 0.0f, 0.0f, 1.0f);
					gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
					break;
				default:
					break;
				}
			}
			gl.glPopMatrix();
		}
		//draw ground
		gl.glBegin(GL2.GL_QUADS);       
		gl.glColor3f(0.0f, 0.6f, 0.2f);   // set the color of the ground
		gl.glVertex3f(-4.0f, 0.0f, -4.0f);   // Top Left
		gl.glVertex3f( 4.0f, 0.0f, -4.0f);   // Top Right
		gl.glVertex3f( 4.0f, 0.0f, 4.0f);   // Bottom Right
		gl.glVertex3f(-4.0f, 0.0f, 4.0f);   // Bottom Left
		gl.glEnd();                                                     
 
		// increasing rotation for the next iteration                   
		rotateT += 0.2f;
		gl.glFlush();
	}

	@Override
	public void init(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}

	@Override
	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
		GL2 gl = gLDrawable.getGL().getGL2();
		final float aspect = (float) width / (float) height;
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		final float fh = 0.5f;
		final float fw = fh * aspect;
		gl.glFrustumf(-fw, fw, -fh, fh, 1.0f, 1000.0f);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void drawLine(GL2 gl, float x1, float y1, float x2, float y2){
	    gl.glBegin(GL2.GL_LINES);
	    gl.glVertex2f((x1), (y1));
	    gl.glVertex2f((x2), (y2));
	    gl.glEnd();
	}
}
