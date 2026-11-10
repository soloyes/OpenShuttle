// Separable 9-tap Gaussian (5 fetches using linear filtering).
#ifdef GL_ES
    precision mediump float;
#endif
varying vec2 v_texCoord0;
uniform sampler2D u_texture;
uniform vec2 u_dir;   // one texel along the blur axis

void main() {
    vec3 c = texture2D(u_texture, v_texCoord0).rgb * 0.227027;
    c += texture2D(u_texture, v_texCoord0 + u_dir * 1.384615).rgb * 0.316216;
    c += texture2D(u_texture, v_texCoord0 - u_dir * 1.384615).rgb * 0.316216;
    c += texture2D(u_texture, v_texCoord0 + u_dir * 3.230769).rgb * 0.070270;
    c += texture2D(u_texture, v_texCoord0 - u_dir * 3.230769).rgb * 0.070270;
    gl_FragColor = vec4(c, 1.0);
}
