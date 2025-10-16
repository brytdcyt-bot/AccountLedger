#version 430
layout (local_size_x = 16, local_size_y = 16) in;

layout(rgba32f, binding = 0) uniform image2D destTex;

uniform float u_time;
uniform ivec2 u_texSize;

float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

void main() {
    ivec2 px = ivec2(gl_GlobalInvocationID.xy);
    if (px.x >= u_texSize.x || px.y >= u_texSize.y) return;
    vec2 uv = vec2(px) / vec2(u_texSize);
    float n = 0.0;
    float amp = 0.5;
    vec2 p = uv * 8.0 + vec2(u_time * 0.05);
    for (int i=0;i<4;i++) {
        n += amp * hash21(p + float(i) * 12.345);
        p *= 2.0;
        amp *= 0.5;
    }
    vec4 col = vec4(vec3(n), 1.0);
    imageStore(destTex, px, col);
}