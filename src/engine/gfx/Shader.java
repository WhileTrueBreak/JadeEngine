package engine.gfx;

import java.nio.FloatBuffer;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public class Shader {
	
	private int shaderProgramID;
	private boolean isUsed = false;
	
	public Shader(int shaderProgramID) {
		this.shaderProgramID = shaderProgramID;
	}
	
	public Shader(String vertexPath, String fragmentPath) {
		this.shaderProgramID = ShaderLoader.createShaderProgram(vertexPath, fragmentPath);
	}
	
	public void use() {
		if(!isUsed) {
        	GL30.glUseProgram(shaderProgramID);
        	isUsed = true;
		}
	}
	
	public void detach() {
		GL30.glUseProgram(0);
		isUsed = false;
	}

	public void uploadMat4f(String varName, Matrix4f mat4) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		GL30.glUniformMatrix4fv(varLocation, false, matBuffer);
	}

	public void uploadMat3f(String varName, Matrix3f mat3) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat3.get(matBuffer);
		GL30.glUniformMatrix3fv(varLocation, false, matBuffer);
	}

	public void uploadMat2f(String varName, Matrix2f mat2) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4);
		mat2.get(matBuffer);
		GL30.glUniformMatrix2fv(varLocation, false, matBuffer);
	}
	
	public void uploadVec4f(String varName, Vector4f vec4) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		GL30.glUniform4f(varLocation, vec4.x, vec4.y, vec4.z, vec4.w);
	}
	
	public void uploadVec3f(String varName, Vector3f vec3) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		GL30.glUniform3f(varLocation, vec3.x, vec3.y, vec3.z);
	}
	
	public void uploadVec2f(String varName, Vector2f vec2) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		GL30.glUniform2f(varLocation, vec2.x, vec2.y);
	}
	
	public void uploadFloat(String varName, float val) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		GL30.glUniform1f(varLocation, val);
	}
	
	public void uploadInt(String varName, int val) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		GL30.glUniform1i(varLocation, val);
	}
	
	public void uploadTexture(String varName, int texID) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		GL30.glUniform1i(varLocation, texID);
	}
	
	public void uploadIntArray(String varName, int[] array) {
		use();
		int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
		GL30.glUniform1iv(varLocation, array);
	}
	
}
