#version 400 core
layout(vertices = 3) out;

void main() {
    // Set inner/outer tess levels
    if (gl_InvocationID == 0) {
        gl_TessLevelInner[0] = 3.0;
        gl_TessLevelOuter[0] = 3.0;
        gl_TessLevelOuter[1] = 3.0;
        gl_TessLevelOuter[2] = 3.0;
    }
    // pass through position to evaluation stage
    gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
}