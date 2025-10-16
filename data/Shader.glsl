// Shader.glsl
// Shared utilities for other shaders
// Include or copy-paste into shader pipeline where supported.

#ifndef SHADER_UTILS
#define SHADER_UTILS

// Simple hash
float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

// 2D noise (value noise)
float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    // four corners
    float a = hash21(i);
    float b = hash21(i + vec2(1.0, 0.0));
    float c = hash21(i + vec2(0.0, 1.0));
    float d = hash21(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(a, b, u.x) + (c - a)*u.y*(1.0 - u.x) + (d - b)*u.x*u.y;
}

// fbm
float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i=0; i<5; i++) {
        v += a * noise(p);
        p *= 2.0;
        a *= 0.5;
    }
    return v;
}

// Smooth palette: map t in [0,1] to color
vec3 palette(float t) {
    // synthwave palette: deep violet -> magenta -> cyan
    vec3 a = vec3(0.05, 0.02, 0.20);   // deep base
    vec3 b = vec3(0.90, 0.15, 0.55);   // magenta
    vec3 c = vec3(0.00, 0.80, 0.95);   // cyan
    if (t < 0.5) {
        return mix(a, b, smoothstep(0.0, 0.5, t));
    } else {
        return mix(b, c, smoothstep(0.5, 1.0, t));
    }
}

// rotate 2D
mat2 rot(float a) {
    float s = sin(a), c = cos(a);
    return mat2(c,-s,s,c);
}

#endif // SHADER_UTILS

