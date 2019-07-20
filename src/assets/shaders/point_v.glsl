#version 150 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform float screenHeight = 900;
uniform float size = 0.1;

in vec3 in_Position;
in float in_Scale;
in float in_Phase;

out vec4 pass_Position;
out float pass_Phase;

void main(void) {
	pass_Position = viewMatrix * vec4(in_Position, 1);
	gl_Position = projectionMatrix * pass_Position;
	gl_PointSize = size * in_Scale * screenHeight / gl_Position.w;
	
	pass_Phase = in_Phase;
}