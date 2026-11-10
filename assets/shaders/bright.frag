// Bloom pre-pass: keep only what is brighter than the threshold.
#ifdef GL_ES
    precision mediump float;
#endif
varying vec2 v_texCoord0;
uniform sampler2D u_texture;
uniform float u_threshold;

void main() {
    vec3 c = texture2D(u_texture, v_texCoord0).rgb;
    float l = max(c.r, max(c.g, c.b));
    float k = max(l - u_threshold, 0.0) / max(l, 0.0001);
    gl_FragColor = vec4(c * k, 1.0);
}
