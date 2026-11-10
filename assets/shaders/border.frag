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

    // Soft halo, a little stronger on the outside so "out" reads as a different place.
    float glow = exp(-(d * d) / (130.0 * 130.0)) * 0.22 + step(0.0, sd) * exp(-sd / 600.0) * 0.08;

    // Coordinate along the edge: y on the left/right sides, x on the top/bottom.
    float along = q.x > q.y ? p.y : p.x;
    float f = fract((along - u_time * 60.0) / 230.0);
    float dash = smoothstep(0.0, 0.06, f) * (1.0 - smoothstep(0.58, 0.63, f));
    // Corners are solid so dashes never meet at odd angles.
    float corner = 1.0 - smoothstep(-80.0, -40.0, min(q.x, q.y));
    dash = max(dash, 1.0 - corner);

    float hw = max(5.0, u_pixel * 1.5);
    float line = 1.0 - smoothstep(hw - u_pixel, hw + u_pixel, d);

    vec3 c = u_color * (glow + line * dash * 0.85) * u_alpha;
    gl_FragColor = vec4(c, 1.0);
}
