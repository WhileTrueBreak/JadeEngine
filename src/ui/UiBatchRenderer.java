package ui;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import engine.Window;
import engine.gfx.Shader;
import engine.gfx.Texture;
import game.res.Assets;

public class UiBatchRenderer {

	private final int POS_SIZE = 3;
	private final int COLOR_SIZE = 4;
	private final int TEX_COORDS_SIZE = 2;
	private final int TEX_ID_SIZE = 1;

	private final int POS_OFFSET = 0;
	private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
	private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

	private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE;
	private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

	private final int NUM_VERTICES = 4;
	private final int NUM_ELEMENTS = 6;
	
	int maxRenderers;
	int numRenderers;
	UiRenderer[] uiRenderers;
	
	private float[] vertices;
	private int[] indices;
	private int[] texSlots;
	
	private int vaoID, vboID, eboID;
	private Shader shader;
	
	private int maxTextures;
	private List<Texture>textures;

	private boolean rebufferData = true;
	
	private Matrix4f projMatrix, viewMatrix;
	private boolean isDirty;
	
	public UiBatchRenderer(int maxRenderers) {
		IntBuffer binds = BufferUtils.createIntBuffer(1);
		GL30.glGetIntegerv(GL30.GL_MAX_TEXTURE_IMAGE_UNITS, binds);

		this.maxTextures = binds.get(0);
		this.maxRenderers = maxRenderers;
		this.numRenderers = 0;
		
		this.uiRenderers = new UiRenderer[this.maxRenderers];
		
		this.vertices = new float[this.maxRenderers * NUM_VERTICES * VERTEX_SIZE];
		this.indices = genDefaultIndices();
		this.textures = new ArrayList<Texture>();
		
		this.shader = Assets.getShader(Assets.SHDR_GUI);

		this.viewMatrix = createViewMatrix();
		this.projMatrix = createProjMatrix();
		
		this.texSlots = new int[this.maxTextures];
		for(int i = 0;i < this.texSlots.length;i++) {
			this.texSlots[i] = i;
		}
	}
	
	public void start() {
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);

		vboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL30.GL_DYNAMIC_DRAW);

		eboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboID);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW);

		GL30.glVertexAttribPointer(0, POS_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
		GL30.glEnableVertexAttribArray(0);

		GL30.glVertexAttribPointer(1, COLOR_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		GL30.glEnableVertexAttribArray(1);

		GL30.glVertexAttribPointer(2, TEX_COORDS_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
		GL30.glEnableVertexAttribArray(2);

		GL30.glVertexAttribPointer(3, TEX_ID_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
		GL30.glEnableVertexAttribArray(3);
	}
	
	public void render() {
		GL30.glBindVertexArray(this.vaoID);
		
		for(int i = 0;i < this.numRenderers;i++) {
			if(this.uiRenderers[i].isDirtyVertex()){
				this.updateVertexData(i);
			}
			this.rebufferData = true;
		}
		
		if(this.rebufferData) rebuffer();

		this.shader.use();
		this.shader.uploadIntArray("uTextures", this.texSlots);
		this.shader.uploadMat4f("uProj", createProjMatrix());
		this.shader.uploadMat4f("uView", this.viewMatrix);

		for(int i = 0;i < this.textures.size();i++) {
			GL30.glActiveTexture(GL30.GL_TEXTURE0 + i);
			this.textures.get(i).bind();
		}

		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL30.glEnableVertexAttribArray(3);

		GL30.glDrawElements(GL30.GL_TRIANGLES, this.NUM_ELEMENTS*this.numRenderers, GL30.GL_UNSIGNED_INT, 0);

		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glDisableVertexAttribArray(3);

		GL30.glBindVertexArray(0);

		for(Texture tex:this.textures) {
			tex.unbind();
		}

		this.shader.detach();
	}

	public void rebuffer() {
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, vertices);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboID);
		rebufferData = false;
	}
	
	private int[] genDefaultIndices() {
		// 6 indices per quad (3 per triangle)
		int[] elements = new int[6 * this.maxRenderers];
		for (int i=0; i < this.maxRenderers; i++) {
			loadElementIndices(elements, i);
		}

		return elements;
	}
	
	public void addUiRenderer(UiRenderer renderer) {
		//get index to add // inc num components
		int index = this.numRenderers++;
		this.uiRenderers[index] = renderer;
		//add texture to textures
		if(renderer.getTexture() != null)
			this.textures.add(renderer.getTexture());
		updateVertexData(index);
	}
	
	private void updateVertexData(int index) {
		UiRenderer renderer = this.uiRenderers[index];
		int vertexIndex = index*this.VERTEX_SIZE*this.NUM_VERTICES;
		//update vertex data
		float xAdd = 1;
		float yAdd = 1;
		for(int i = 0;i < NUM_VERTICES;i++) {
			if (i == 1) {
				yAdd = 0;
			} else if (i == 2) {
				xAdd = 0;
			} else if (i == 3) {
				yAdd = 1;
			}
			double offx = renderer.getParent().transform.getScale().x * xAdd - renderer.getParent().transform.getRotPoint().x;
			double offy = renderer.getParent().transform.getScale().y * yAdd - renderer.getParent().transform.getRotPoint().y;
			double rotx = offx*Math.cos(renderer.getParent().transform.getRot()) - offy*Math.sin(renderer.getParent().transform.getRot());
			double roty = offx*Math.sin(renderer.getParent().transform.getRot()) + offy*Math.cos(renderer.getParent().transform.getRot());
			
			this.vertices[vertexIndex++] = renderer.getParent().transform.getPosition().x + renderer.getParent().offset.x + (float)rotx + renderer.getParent().transform.getRotPoint().x;
			this.vertices[vertexIndex++] = renderer.getParent().transform.getPosition().y + renderer.getParent().offset.y + (float)roty + renderer.getParent().transform.getRotPoint().y;
			this.vertices[vertexIndex++] = 0;

			this.vertices[vertexIndex++] = renderer.getColor().x;
			this.vertices[vertexIndex++] = renderer.getColor().y;
			this.vertices[vertexIndex++] = renderer.getColor().z;
			this.vertices[vertexIndex++] = renderer.getColor().w;

			this.vertices[vertexIndex++] = renderer.getTexCoords()[i].x;
			this.vertices[vertexIndex++] = renderer.getTexCoords()[i].y;

			this.vertices[vertexIndex++] = this.textures.indexOf(renderer.getTexture());
		}
	}

	private void loadElementIndices(int[] elements, int index) {
		int offsetArrayIndex = NUM_ELEMENTS * index;
		int offset = NUM_VERTICES * index;

		// Triangle 1
		elements[offsetArrayIndex++] = offset + 3;
		elements[offsetArrayIndex++] = offset + 2;
		elements[offsetArrayIndex++] = offset + 0;

		// Triangle 2
		elements[offsetArrayIndex++] = offset + 0;
		elements[offsetArrayIndex++] = offset + 2;
		elements[offsetArrayIndex] = offset + 1;
	}
	
	public Matrix4f createProjMatrix() {
		this.projMatrix = new Matrix4f();
		this.projMatrix.identity();
		this.projMatrix.ortho(0.0f, Window.getWidth(), 0.0f, Window.getHeight(), 0, 100);
		return this.projMatrix;
	}
	
	public Matrix4f createViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		this.viewMatrix = new Matrix4f();
		this.viewMatrix.identity();
		this.viewMatrix = viewMatrix.lookAt(new Vector3f(0, 0, 20.0f), 
				cameraFront.add(new Vector3f(0, 0, 0.0f)), cameraUp);
		return this.viewMatrix;
	}
	
	public int getNumComponents() {
		return this.numRenderers;
	}

	public boolean hasRoom() {
		return this.numRenderers < this.maxRenderers;
	}

	public boolean hasTextureRoom() {
		return this.textures.size() < this.maxTextures;
	}

	public boolean hasTexture(Texture texure) {
		return this.textures.contains(texure);
	}
	
}
