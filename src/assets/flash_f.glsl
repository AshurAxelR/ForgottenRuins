#version 150 core

uniform vec4 color = vec4(0, 0, 0, 1);
uniform float alpha = 1;
uniform float black = 0;

out vec4 out_Color;

void main(void) {
	out_Color = mix(vec4(1), color, clamp(alpha, 0, 1)*color.a) * (1-black);
	out_Color.a = 1;
}
