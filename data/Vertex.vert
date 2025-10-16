#version 330 core
layout(location = 0) in vec2 aPos;   // [-1,1] clip quad
layout(location = 1) in vec2 aUV;    // [0,1] UV

out vec2 vUV;
out vec2 vPos;

void main() {
    vUV = aUV;
    vPos = aPos;
    gl_Position = vec4(aPos.xy, 0.0, 1.0);
}

