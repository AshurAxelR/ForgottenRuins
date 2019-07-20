#version 150 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightDirection = normalize(vec3(0.15, -1, 0.3));
uniform float ambient = 0.7;
uniform float lightScale = 0.1;

uniform int highlightInstance = -1;

in vec3 in_Position;
in vec3 in_Normal;
in vec2 in_TexCoord;

in vec3 ins_Position;
in float ins_RotationY;
in float ins_Scale;
in float ins_Light;

out vec4 pass_Position;
out vec2 pass_TexCoord;
out float pass_Light;
out float pass_Highlight;

mat4 translationMatrix(vec3 t) {
	mat4 m = mat4(1);
	m[3] = vec4(t, 1);
	return m;
}

mat4 rotationYMatrix(float a) {
	mat4 m = mat4(1);
	m[0][0] = cos(a);
	m[0][2] = sin(a);
	m[2][0] = -m[0][2];
	m[2][2] = m[0][0];
	return m;
}

mat4 scaleMatrix(float s) {
	mat4 m = mat4(1);
	m[0][0] = s;
	m[1][1] = s;
	m[2][2] = s;
	return m;
}

void main(void) {
	mat4 modelMatrix = translationMatrix(ins_Position) * rotationYMatrix(ins_RotationY) * scaleMatrix(ins_Scale);
	pass_Position = viewMatrix * modelMatrix * vec4(in_Position, 1);
	gl_Position = projectionMatrix * pass_Position;
	
	vec3 normal = normalize(vec3(viewMatrix * modelMatrix * vec4(in_Normal, 0)));
	vec3 lightDir = normalize((viewMatrix * vec4(-lightDirection, 0)).xyz);
	float diffuse = dot(normal, lightDir)*0.5+0.5;
	
	pass_TexCoord = in_TexCoord;
	pass_Light = diffuse*(1-ambient-lightScale) + ambient + ins_Light*lightScale;
	
	pass_Highlight = (gl_InstanceID==highlightInstance) ? 0.075 : 0;
}