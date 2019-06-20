#version 150 core

in vec2 in_Position;
in vec2 in_TexCoord;

out vec2 pass_TexCoord;

void main(void) {
	gl_Position = vec4(in_Position.x*2.0-1.0, in_Position.y*2.0-1.0, 0, 1);
	pass_TexCoord = in_TexCoord;
}