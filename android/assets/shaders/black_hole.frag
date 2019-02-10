#ifdef GL_ES
    #define LOWP lowp
    precision highp float;
#else
    #define LOWP
#endif
varying vec2 v_texCoord0;
varying vec4 v_color;
uniform LOWP sampler2D u_texture;
uniform LOWP vec2 center;
uniform LOWP float time;
uniform LOWP float dmax;
uniform float isGrey;
uniform vec2 resolution;

void main (void)
{
    vec4 texColor = texture2D(u_texture, v_texCoord0);

    vec2 sub2 = v_texCoord0 - center;

    sub2.x *= resolution.x / resolution.y;

    gl_FragColor = texture2D(u_texture, v_texCoord0);

    float d = length(sub2);
    //float dmax = 0.07;
    if(d < dmax) {
        float a = d * 90.0 + time;
        float s = sin(a);
        float c = cos(a);
        mat2 m = mat2(c, -s, s, c);
        sub2 = m * sub2;

        gl_FragColor = texture2D(u_texture, v_texCoord0 + sub2);
        gl_FragColor.b += (dmax - d) * 4.0;
        if(isGrey > 0.0){
            gl_FragColor = vec4(vec3(dot(gl_FragColor.rgb, vec3(0.299, 0.587, 0.114))), gl_FragColor.a);
        }

        if (d < 0.02) {
            gl_FragColor.rgb *= d / 0.02;
        }
    }
        if (d > dmax - 0.01 && d < (dmax + 0.02)) {
            vec4 sum = vec4(0.0);

            float blur = 3.0;

            // Blur direction
            float hstep = 1.0;
            float vstep = 1.0;

            // Apply blur using 9 samples and predefined gaussian weights
            sum += texture2D(u_texture, vec2(v_texCoord0.x - 4.0*blur*hstep/resolution.x, v_texCoord0.y - 4.0*blur*vstep/resolution.y)) * 0.006;
            sum += texture2D(u_texture, vec2(v_texCoord0.x - 3.0*blur*hstep/resolution.x, v_texCoord0.y - 3.0*blur*vstep/resolution.y)) * 0.044;
            sum += texture2D(u_texture, vec2(v_texCoord0.x - 2.0*blur*hstep/resolution.x, v_texCoord0.y - 2.0*blur*vstep/resolution.y)) * 0.121;
            sum += texture2D(u_texture, vec2(v_texCoord0.x - 1.0*blur*hstep/resolution.x, v_texCoord0.y - 1.0*blur*vstep/resolution.y)) * 0.194;

            sum += texture2D(u_texture, vec2(v_texCoord0.x, v_texCoord0.y)) * 0.27;

            sum += texture2D(u_texture, vec2(v_texCoord0.x + 1.0*blur*hstep/resolution.x, v_texCoord0.y + 1.0*blur*vstep/resolution.y)) * 0.194;
            sum += texture2D(u_texture, vec2(v_texCoord0.x + 2.0*blur*hstep/resolution.x, v_texCoord0.y + 2.0*blur*vstep/resolution.y)) * 0.121;
            sum += texture2D(u_texture, vec2(v_texCoord0.x + 3.0*blur*hstep/resolution.x, v_texCoord0.y + 3.0*blur*vstep/resolution.y)) * 0.044;
            sum += texture2D(u_texture, vec2(v_texCoord0.x + 4.0*blur*hstep/resolution.x, v_texCoord0.y + 4.0*blur*vstep/resolution.y)) * 0.006;

            gl_FragColor = sum;
        }
}