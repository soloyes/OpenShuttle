// Final scene composite: black-hole lensing, bloom, colour grade, vignette, grain, pause grey.
#ifdef GL_ES
    #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
    #else
        precision mediump float;
    #endif
#endif

varying vec2 v_texCoord0;
uniform sampler2D u_texture;   // scene
uniform sampler2D u_bloom;     // blurred highlights
uniform vec2 u_resolution;     // scene size (aspect for the lens)
uniform float u_time;

uniform float u_hole;          // 1 = draw black-hole lensing
uniform vec2 u_center;         // hole centre in uv
uniform float u_radius;        // lens radius in uv (height-normalised)
uniform float u_spin;          // swirl phase

uniform float u_bloomStrength;
uniform float u_vignette;
uniform float u_grain;
uniform float u_grey;          // 1 = desaturate (pause, game over)
uniform float u_flash;         // 0..1 white/red flash
uniform vec3 u_flashColor;
uniform vec3 u_tint;           // per-sector colour grade multiplier

float rand(vec2 p) {
    return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec2 uv = v_texCoord0;
    vec2 aspect = vec2(u_resolution.x / u_resolution.y, 1.0);
    float darken = 1.0;
    float rim = 0.0;

    if (u_hole > 0.5) {
        vec2 d = (uv - u_center) * aspect;
        float r = length(d);
        if (r < u_radius) {
            // Swirl + pull toward the centre, stronger near the core.
            float k = 1.0 - r / u_radius;
            // Zero twist at the lens edge so there is no seam.
            float a = k * k * (6.0 + u_spin);
            float s = sin(a);
            float c = cos(a);
            vec2 rd = mat2(c, -s, s, c) * d * (1.0 - 0.55 * k * k);
            uv = u_center + rd / aspect;
            // Event horizon: fade to black in the core.
            darken = smoothstep(u_radius * 0.18, u_radius * 0.42, r);
            // Thin bright photon ring.
            rim = exp(-pow((r - u_radius * 0.46) / (u_radius * 0.028), 2.0));
        }
    }

    vec3 col = texture2D(u_texture, uv).rgb * darken;
    col += vec3(1.0, 0.72, 0.45) * rim * 0.55;
    col += texture2D(u_bloom, v_texCoord0).rgb * u_bloomStrength;
    col *= u_tint;

    // Gentle filmic curve keeps highlights from clipping harshly.
    col = col / (1.0 + col * 0.25) * 1.18;

    float luma = dot(col, vec3(0.299, 0.587, 0.114));
    col = mix(col, vec3(luma), u_grey);

    vec2 v = v_texCoord0 - 0.5;
    col *= 1.0 - u_vignette * dot(v * aspect * 0.9, v * aspect * 0.9);

    col += (rand(v_texCoord0 * u_resolution + u_time) - 0.5) * u_grain;
    col = mix(col, u_flashColor, u_flash);

    gl_FragColor = vec4(col, 1.0);
}
