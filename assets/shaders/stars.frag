// Procedural star field: three parallax layers of hashed grid stars with twinkle.
// Drawn additively over the nebula at full resolution so stars stay crisp.
#ifdef GL_ES
    #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
    #else
        precision mediump float;
    #endif
#endif

uniform vec2 u_resolution;
uniform vec2 u_camera;
uniform vec2 u_view;
uniform float u_time;
uniform float u_seed;
uniform float u_pixel;       // world units per screen pixel

float hash(vec2 p) {
    p = mod(p, 289.0);
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

vec3 starLayer(float depth, float cell, float density, float radius, vec3 tint) {
    vec2 uv = gl_FragCoord.xy / u_resolution - 0.5;
    vec2 w = u_camera * depth + uv * u_view + vec2(u_seed * 97.0, u_seed * 61.0);
    vec2 g = floor(w / cell);
    vec2 f = w / cell - g;
    float h = hash(g);
    if (h > density) {
        return vec3(0.0);
    }
    vec2 c = vec2(hash(g + 3.1), hash(g + 7.7)) * 0.8 + 0.1;
    // Distance in screen pixels so stars keep their size at any zoom.
    float d = length((f - c) * cell) / u_pixel;
    float core = smoothstep(radius, 0.0, d);
    float halo = smoothstep(radius * 4.0, 0.0, d) * 0.18;
    float tw = 0.65 + 0.35 * sin(u_time * (0.7 + h * 3.0) + h * 60.0);
    float b = (core + halo) * tw;
    // Colour temperature: most white, some blue, a few warm.
    float t = hash(g + 1.7);
    vec3 col = t < 0.15 ? vec3(1.0, 0.82, 0.62) : (t < 0.45 ? vec3(0.7, 0.82, 1.0) : vec3(1.0));
    return col * tint * b;
}

void main() {
    vec3 c = vec3(0.0);
    c += starLayer(0.06, 90.0, 0.35, 1.2, vec3(0.6));
    c += starLayer(0.2, 180.0, 0.3, 1.7, vec3(0.9));
    c += starLayer(0.5, 420.0, 0.22, 2.6, vec3(1.3));
    gl_FragColor = vec4(c, 1.0);
}
