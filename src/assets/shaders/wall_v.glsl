#version 150 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightDirection = normalize(vec3(0.15, -0.5, 0.3));
uniform float ambient = 0.7;
uniform float lightScale = 0.1;


in vec3 in_Position;
in vec3 in_Normal;
in vec2 in_TexCoord;
in float in_Light;

out vec4 pass_Position;
out vec2 pass_TexCoord;
out float pass_Light;

void main(void) {
	pass_Position = viewMatrix * vec4(in_Position, 1);
	gl_Position = projectionMatrix * pass_Position;
	
	vec3 normal = normalize(vec3(viewMatrix * vec4(in_Normal, 0)));
	vec3 lightDir = normalize((viewMatrix * vec4(-lightDirection, 0)).xyz);
	float diffuse = dot(normal, lightDir)*0.5+0.5;
	
	pass_TexCoord = in_TexCoord;
	pass_Light = diffuse*(1-ambient-lightScale) + ambient + in_Light*lightScale;
}