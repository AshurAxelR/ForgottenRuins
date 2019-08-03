#version 150 core

uniform vec3 lightDirection = normalize(vec3(0.15, -0.5, 0.3));

uniform vec4 upColor = vec4(0, 0, 0, 1);
uniform vec4 midColor = vec4(0.5, 0.5, 0.5, 1);
uniform vec4 downColor = vec4(1, 1, 1, 1);
uniform float midPoint = 0.5;

in vec4 pass_Position;

out vec4 out_Color;

void main(void) {
	if(pass_Position.y>midPoint)
		out_Color = mix(midColor, upColor, (pass_Position.y-midPoint)/(1-midPoint));
	else
		out_Color = mix(downColor, midColor, max(0, pass_Position.y/midPoint));
	float dot = max(dot(pass_Position.xyz, -lightDirection), 0);
	out_Color += pow(dot, 750);
	out_Color += pow(dot, 8) * 0.125;
}
