// Sprite crumbling into sand: grains vanish by a noise threshold, a hot rim burns at the edge.
#ifdef GL_ES
    #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
    #else
        precision mediump float;
    #endif
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_dissolve;   // 0 = intact, 1 = gone
uniform vec2 u_atlas;       // texture size in pixels

float hash(vec2 p) {
    p = mod(p, 289.0);
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float vnoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), u.x),
               mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), u.x), u.y);
}

void main() {
    vec4 tex = texture2D(u_texture, v_texCoords) * v_color;
    vec2 px = v_texCoords * u_atlas;
    // Coarse cracks plus per-grain jitter (3-pixel grains) so it reads as sand, not a fade.
    float n = vnoise(px / 26.0) * 0.65 + hash(floor(px / 3.0)) * 0.35;
    float d = u_dissolve * 1.1;
    if (n < d) {
        discard;
    }
    float rim = 1.0 - smoothstep(d, d + 0.09, n);
    vec3 hot = vec3(1.0, 0.62, 0.28) * 1.8;
    vec3 col = mix(tex.rgb, hot, rim * step(0.001, u_dissolve));
    gl_FragColor = vec4(col, tex.a);
}
