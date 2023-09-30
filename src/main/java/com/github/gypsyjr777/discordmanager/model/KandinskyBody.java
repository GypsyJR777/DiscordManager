package com.github.gypsyjr777.discordmanager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KandinskyBody {
    public String prompt;
    public String negative_prompt = "NONE";
    public int samples = 1;
    public int num_inference_steps = 25;
    public int img_width = 512;
    public int img_height = 512;
    public int prior_steps = 25;
    public int seed = 100;
    public boolean base64 = false;

    public KandinskyBody(String prompt) {
        this.prompt = prompt;
    }
}
