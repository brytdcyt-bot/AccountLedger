#version 400 core
layout(triangles, fractional_even_spacing, cw) in;

void main() {
    vec3 b = gl_TessCoord;
    gl_Position = b.x * gl_in[0].gl_Position +
    b.y * gl_in[1].gl_Position +
    b.z * gl_in[2].gl_Position;
}