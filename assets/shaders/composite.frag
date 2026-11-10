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
uniform float u_spin;          // black-hole clock (disk rotation)
uniform float u_aspect;        // visible world width / height

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

// Hot plasma colour: white-yellow near the hole, orange-red further out.
vec3 plasma(float t) {
    return mix(vec3(1.0, 0.93, 0.78), vec3(1.0, 0.42, 0.12), clamp(t, 0.0, 1.0));
}

// Turbulent emission of the accretion disk at radius rd (lens units) and disk angle phi.
float diskDensity(float rd, float phi) {
    // Keplerian shear: inner rings orbit faster.
    float omega = 0.9 / pow(max(rd, 0.05), 1.5);
    float a = phi + u_spin * omega;
    float n = vnoise(vec2(a * 4.0, rd * 14.0)) * 0.7 + vnoise(vec2(a * 8.0, rd * 28.0)) * 0.3;
    return 0.55 + 0.75 * n;
}

/*
 * A Gargantua-style black hole in screen space. Lens units: 1.0 = capture range.
 * Shadow, photon ring, Einstein-ring lensing of the background, a tilted accretion disk
 * with Doppler beaming, the near half crossing in front of the shadow and the far half
 * lensed into an arc above it.
 */
vec3 blackHole(vec2 uv, vec2 aspect) {
    const float SHADOW = 0.17;
    const float EINSTEIN = 0.30;
    const float TILT = 0.30;        // cos of the disk inclination: a thin ellipse
    const float DISK_IN = 0.26;
    const float DISK_OUT = 0.78;

    vec2 d = (uv - u_center) * aspect / u_radius;
    float r = length(d);
    if (r > 1.0) {
        return texture2D(u_texture, uv).rgb;
    }

    // Background lensing: deflection ~ E^2 / r, fading out towards the edge of the zone.
    float fade = smoothstep(1.0, 0.55, r);
    vec2 dir = d / max(r, 0.0001);
    float defl = EINSTEIN * EINSTEIN / max(r, SHADOW) * fade;
    vec2 src = d - dir * defl;
    vec3 bg = texture2D(u_texture, u_center + src * u_radius / aspect).rgb;

    // Shadow.
    float shadow = smoothstep(SHADOW * 0.94, SHADOW * 1.03, r);
    vec3 col = bg * shadow;

    // Disk in its own plane (screen y stretched back by the inclination).
    vec2 p = vec2(d.x, d.y / TILT);
    float rd = length(p);
    float phi = atan(p.y, p.x);
    float band = smoothstep(DISK_IN, DISK_IN + 0.04, rd) * (1.0 - smoothstep(DISK_OUT * 0.55, DISK_OUT, rd));
    // Doppler beaming: the approaching (left) side is brighter and bluer.
    float beam = 1.0 - 0.65 * cos(phi);
    float heat = (rd - DISK_IN) / (DISK_OUT - DISK_IN);
    vec3 disk = plasma(heat) * band * diskDensity(rd, phi) * beam * (0.55 / max(rd, 0.2));
    disk = mix(disk, disk * vec3(0.8, 0.95, 1.25), clamp(-cos(phi), 0.0, 1.0) * 0.5);

    // Far half of the disk sits behind the shadow; the near half (screen y < 0) in front.
    float near = step(d.y, 0.0);
    float behind = (1.0 - near) * (1.0 - smoothstep(SHADOW * 0.95, SHADOW * 1.05, r));
    col += disk * (1.0 - behind);

    // Lensed image of the far disk: an arc hugging the shadow, strongest on top.
    float arcR = SHADOW * 1.55;
    float ad = (r - arcR) / 0.06;
    float arc = exp(-ad * ad);
    float arcAngle = atan(d.y, d.x);
    float arcWeight = 0.35 + 0.95 * smoothstep(-0.35, 0.55, d.y / max(r, 0.0001));
    vec3 arcCol = plasma(0.15) * arc * arcWeight * diskDensity(arcR * 1.7, arcAngle * 1.0) * (1.0 - 0.5 * cos(arcAngle)) * 1.1;
    col += arcCol * (1.0 - near * step(abs(d.x), DISK_OUT) * band);

    // Photon ring.
    float rr = (r - SHADOW * 1.08) / 0.012;
    float ring = exp(-rr * rr);
    col += vec3(1.0, 0.85, 0.65) * ring * 0.9;

    return col;
}

void main() {
    vec2 uv = v_texCoord0;
    vec2 aspect = vec2(u_resolution.x / u_resolution.y, 1.0);

    vec3 col;
    if (u_hole > 0.5) {
        col = blackHole(uv, vec2(u_aspect, 1.0));
    } else {
        col = texture2D(u_texture, uv).rgb;
    }
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
