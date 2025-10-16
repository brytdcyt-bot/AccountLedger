#version 330 core
in vec2 vUV;
in vec2 vPos;
out vec4 FragColor;

uniform float u_time;
uniform vec2 u_resolution;
uniform sampler2D u_noiseTex; // optional

// include helpers (copy paste Shader.glsl content here or include)
//
// For single-file pipelines, paste the Shader.glsl code at the top.
// For multi-file systems, use preprocessor includes if supported.

//
// --- Begin helpers (copy from Shader.glsl) ---
//
float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}
float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    float a = hash21(i);
    float b = hash21(i + vec2(1.0, 0.0));
    float c = hash21(i + vec2(0.0, 1.0));
    float d = hash21(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(a, b, u.x) + (c - a)*u.y*(1.0 - u.x) + (d - b)*u.x*u.y;
}
float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i=0;i<5;i++) {
        v += a * noise(p);
        p *= 2.0;
        a *= 0.5;
    }
    return v;
}
vec3 palette(float t) {
    vec3 a = vec3(0.05, 0.02, 0.20);
    vec3 b = vec3(0.90, 0.15, 0.55);
    vec3 c = vec3(0.00, 0.80, 0.95);
    if (t < 0.5) {
        return mix(a, b, smoothstep(0.0, 0.5, t));
    } else {
        return mix(b, c, smoothstep(0.5, 1.0, t));
    }
}
mat2 rot(float a){float s=sin(a), c=cos(a); return mat2(c,-s,s,c);}
//
// --- End helpers
//

void main() {
    vec2 uv = vUV;
    vec2 p = (gl_FragCoord.xy / u_resolution.xy);
    // center and aspect
    vec2 centered = (gl_FragCoord.xy - 0.5*u_resolution.xy) / u_resolution.y;

    // animated gradient (vertical waves)
    float t = u_time * 0.12;
    float wave = sin((uv.y * 6.0) + t*2.0 + fbm(uv * 3.0 + t*0.5)) * 0.5 + 0.5;
    float gradient = smoothstep(0.0, 1.0, uv.y + wave * 0.08);

    vec3 base = palette(gradient);

    // subtle horizon glow
    float glow = exp(-pow((centered.y + 0.2)*6.0, 2.0));
    base += glow * 0.25;

    // scanlines
    float scan = 0.5 + 0.5 * sin((gl_FragCoord.y * 0.5) + u_time*8.0);
    base *= mix(0.98, 1.02, 0.5*scan);

    // glass sheen (moving streak)
    float streak = smoothstep(0.0, 1.0, 1.0 - abs((centered.x + sin(u_time*0.6)*0.2)*3.0));
    base = mix(base, base + vec3(0.25,0.3,0.45)*0.6, streak * 0.12);

    // subtle noise overlay (either from texture or procedural)
    float n = fbm(uv * 10.0 + u_time * 0.1);
    base += (n - 0.5) * 0.02;

    // vignette
    float vign = smoothstep(0.8, 0.2, length(centered) * 1.2);
    base *= vign;

    // exposure & clamp
    base = pow(base, vec3(0.95));
    FragColor = vec4(clamp(base, 0.0, 1.0), 1.0);
}
