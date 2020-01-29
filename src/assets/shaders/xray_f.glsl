#version 150 core

uniform sampler2D texMask;
uniform sampler2D texNoise;

uniform vec4 color = vec4(1, 1, 1, 1);
uniform float alpha = 1;
uniform float time = 0;
uniform vec2 texSize;

in vec2 pass_TexCoord;

out vec4 out_Color;

void main(void) {
	float mask = texture(texMask, pass_TexCoord).r;
	if(mask<0.5) discard;
	
	vec2 noiseCoord = pass_TexCoord*texSize;
	float noise = min(1, 0.5 + 1.5 * (
		texture(texNoise, noiseCoord+vec2(-fract(time*1.07), 0)).r *
		texture(texNoise, noiseCoord+vec2(fract(time*0.97), 0)).r *
		texture(texNoise, noiseCoord+vec2(0, -fract(time*1.21))).r *
		texture(texNoise, noiseCoord+vec2(0, fract(time*0.73))).r
	));
	
	out_Color = color;
	out_Color.a = mask * alpha * noise;
}
