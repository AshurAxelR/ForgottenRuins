#version 150 core

uniform sampler2D texDiffuse;
uniform int frames = 1;

uniform float fogNear = 10;
uniform float fogFar = 50;
uniform vec4 fogColor = vec4(0.4, 0.6, 0.9, 1);

in vec4 pass_Position;
in float pass_Phase;

out vec4 out_Color;

void main(void) {
	float w = 1/float(frames);
	float x = floor(pass_Phase*frames)*w;
	vec2 texCoord = vec2(gl_PointCoord.x*w+x, gl_PointCoord.y);
	vec4 t_diffuse = texture(texDiffuse, texCoord);
	if(t_diffuse.a<0.5)
		discard;

	if(fogFar>0 && fogFar>fogNear) {
		float viewDist = length(pass_Position.xyz);
		out_Color = fogColor * clamp((viewDist - fogNear) / (fogFar - fogNear), 0, 1);
	}
	else {
		out_Color = vec4(0, 0, 0, 1);
	}
	
	out_Color += t_diffuse;
}
