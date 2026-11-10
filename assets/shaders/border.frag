// Sector border: one continuous glowing outline around the map, with marching dashes.
// Distance to the rectangle outline gives seamless rounded corners.
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
uniform vec2 u_size;      // sector size, world units
uniform float u_time;
uniform float u_alpha;
uniform float u_pixel;    // world units per screen pixel
uniform vec3 u_color;

void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution - 0.5;
    vec2 p = u_camera + uv * u_view;

    // Signed distance to the sector rectangle, then distance to its outline.
    vec2 q = abs(p - u_size * 0.5) - u_size * 0.5;
    float sd = length(max(q, 0.0)) + min(max(q.x, q.y), 0.0);
    float d = abs(sd);

    float glow = exp(-(d * d) / (170.0 * 170.0)) * 0.45;

    // Coordinate along the edge: y on the left/right sides, x on the top/bottom.
    float along = q.x > q.y ? p.y : p.x;
    float dash = step(0.42, fract((along - u_time * 120.0) / 220.0));
    // Corners are solid so dashes never meet at odd angles.
    float corner = step(-60.0, min(q.x, q.y));
    dash = max(dash, corner);

    float hw = 9.0;
    float line = 1.0 - smoothstep(hw - u_pixel, hw + u_pixel, d);

    vec3 c = u_color * (glow + line * dash) * u_alpha;
    gl_FragColor = vec4(c, 1.0);
}
