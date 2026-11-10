// Procedural nebula: domain-warped fBm in two parallax layers, rendered at half resolution.
// World position comes from gl_FragCoord, so it works for any viewport and camera zoom.
#ifdef GL_ES
    #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
    #else
        precision mediump float;
    #endif
#endif

uniform vec2 u_resolution;   // render target size in pixels
uniform vec2 u_camera;       // camera centre, world units
uniform vec2 u_view;         // visible world size
uniform float u_time;
uniform float u_seed;
uniform vec3 u_deep;         // background colour
uniform vec3 u_gasA;         // main gas colour
uniform vec3 u_gasB;         // accent gas colour
uniform float u_intensity;   // 0..1, overall gas amount

float hash(vec2 p) {
    // Keep the argument small so sin() stays precise on mediump GPUs.
    p = mod(p, 289.0);
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), u.x),
               mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), u.x), u.y);
}

float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i = 0; i < 5; i++) {
        v += a * noise(p);
        p = p * 2.03 + vec2(1.7, 9.2);
        a *= 0.5;
    }
    return v;
}

// World position seen through a layer that moves at `depth` of the camera speed.
// Each layer also drifts and sways on its own, so space is never frozen.
vec2 layer(float depth) {
    vec2 uv = gl_FragCoord.xy / u_resolution - 0.5;
    vec2 drift = u_time * vec2(55.0, 20.0) * depth
            + vec2(sin(u_time * 0.07 + depth * 9.0), cos(u_time * 0.05 + depth * 5.0)) * 260.0 * depth;
    return u_camera * depth + uv * u_view + drift;
}

void main() {
    vec2 seed = vec2(u_seed * 1.7, u_seed * 3.1);

    // Far gas: big, slow, warped.
    vec2 p = layer(0.12) * 0.0011 + seed;
    vec2 q = vec2(fbm(p + vec2(0.0, u_time * 0.02)), fbm(p + vec2(5.2, 1.3) + u_time * 0.012));
    float gas = fbm(p + 1.8 * q);
    // Dust lanes cut dark filaments through the gas.
    float dust = smoothstep(0.45, 0.75, fbm(p * 2.3 + q * 1.5 + 7.0));

    // Near wisps: smaller, faster parallax.
    vec2 p2 = layer(0.28) * 0.0024 + seed * 1.7;
    float wisp = fbm(p2 + 0.8 * vec2(fbm(p2 + 3.1), fbm(p2 + 8.4)));

    float g = smoothstep(0.3, 0.85, gas) * u_intensity;
    float w = smoothstep(0.5, 0.9, wisp) * u_intensity;

    vec3 col = u_deep;
    col = mix(col, u_gasA, g * 0.85);
    col = mix(col, u_gasB, smoothstep(0.55, 1.0, gas * q.x * 1.6) * 0.6 * u_intensity);
    col += u_gasA * w * 0.35;
    col *= 1.0 - dust * 0.55;

    gl_FragColor = vec4(col, 1.0);
}
