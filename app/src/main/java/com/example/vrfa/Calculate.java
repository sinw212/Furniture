package com.example.vrfa;

public class Calculate {

    float width;
    float height;

    public void Calc_ratio(float[] x, float[] y) {

        if(x[0] < x[1]){
            width = x[1] - x[0];
            if(y[0] < y[1]){
                height = y[1] - y[0];
            }
            else if(y[0] > y[1]){
                height = y[0] - y[1];
            }
            else { height = 0;}
        }
        else if(x[0] > x[1]){
            width = x[0] - x[1];
            if(y[0] < y[1]){
                height = y[1] - y[0];
            }
            else if(y[0] > y[1])
            {
                height= y[1]- y[0];
            }
            else { height = 0; }
        }
    }
}