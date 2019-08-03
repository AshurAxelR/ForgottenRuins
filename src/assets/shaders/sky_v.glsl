#version 150 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

in vec3 in_Position;

out vec4 pass_Position;

void main(void) {
	pass_Position = vec4(normalize(in_Position), 1);
	gl_Position = projectionMatrix * viewMatrix * vec4(in_Position, 1);
}