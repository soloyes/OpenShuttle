#ifdef GL_ES
    #define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform vec2 resolution;
uniform float radius;


void main(){

    vec4 sum = vec4(0.0);

    //our original texcoord for this fragment
    vec2 tc = v_texCoords;

    //the amount to blur, i.e. how far off center to sample from
    //1.0 -> blur by one pixel
    //2.0 -> blur by two pixels, etc.
    float blur = 0.5;

     // Blur direction
    float hstep = 1.0;
    float vstep = 1.0;
    gl_FragColor = texture2D(u_texture, v_texCoords);
    float alpha = gl_FragColor.a;

    // Apply blur using 9 samples and predefined gaussian weights
    sum += texture2D(u_texture, vec2(v_texCoords.x - 4.0*blur*hstep/resolution.x, v_texCoords.y - 4.0*blur*vstep/resolution.y)) * 0.006;
    sum += texture2D(u_texture, vec2(v_texCoords.x - 3.0*blur*hstep/resolution.x, v_texCoords.y - 3.0*blur*vstep/resolution.y)) * 0.044;
    sum += texture2D(u_texture, vec2(v_texCoords.x - 2.0*blur*hstep/resolution.x, v_texCoords.y - 2.0*blur*vstep/resolution.y)) * 0.121;
    sum += texture2D(u_texture, vec2(v_texCoords.x - 1.0*blur*hstep/resolution.x, v_texCoords.y - 1.0*blur*vstep/resolution.y)) * 0.194;

    sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y)) * 0.27;

    sum += texture2D(u_texture, vec2(v_texCoords.x + 1.0*blur*hstep/resolution.x, v_texCoords.y + 1.0*blur*vstep/resolution.y)) * 0.194;
    sum += texture2D(u_texture, vec2(v_texCoords.x + 2.0*blur*hstep/resolution.x, v_texCoords.y + 2.0*blur*vstep/resolution.y)) * 0.121;
    sum += texture2D(u_texture, vec2(v_texCoords.x + 3.0*blur*hstep/resolution.x, v_texCoords.y + 3.0*blur*vstep/resolution.y)) * 0.044;
    sum += texture2D(u_texture, vec2(v_texCoords.x + 4.0*blur*hstep/resolution.x, v_texCoords.y + 4.0*blur*vstep/resolution.y)) * 0.006;

    gl_FragColor = sum;
    //discard alpha for our simple demo, multiply by vertex color and return
    gl_FragColor = v_color * vec4(gl_FragColor.rgb, 1.0);

    //gl_FragColor = texture2D(u_texture, v_texCoords);
    gl_FragColor = vec4(vec3(dot(gl_FragColor.rgb, vec3(0.299, 0.587, 0.114))), alpha);
}