#version 150 core

uniform sampler2D texDiffuse;

uniform float fogNear = 10;
uniform float fogFar = 50;
uniform vec4 fogColor = vec4(0.4, 0.6, 0.9, 1);

in vec4 pass_Position;
in vec2 pass_TexCoord;
in float pass_Light;

out vec4 out_Color;

void main(void) {
	vec4 t_diffuse = texture(texDiffuse, pass_TexCoord);
	if(t_diffuse.a<0.5)
		discard;

	out_Color = t_diffuse * vec4(vec3(pass_Light), 1);
	if(fogFar>0 && fogFar>fogNear) {
		float viewDist = length(pass_Position.xyz);
		out_Color = mix(out_Color, fogColor, clamp((viewDist - fogNear) / (fogFar - fogNear), 0, 1));
	}
}
