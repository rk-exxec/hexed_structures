package com.rk_exxec.hexlands_struct.util;

import java.util.ArrayList;

public class HexCoord{
    public int q;
    public int r;    

    public HexCoord(int q, int r){
        this.q = q;
        this.r = r;
    }

    public HexCoord add(int q, int r){
        return new HexCoord(this.q+q, this.r+r);
    }

    public HexCoord add(HexCoord o){
        return new HexCoord(this.q+o.q, this.r+o.r);
    }

    public HexCoord mult(int fac){
        return new HexCoord(this.q*fac, this.r*fac);
    }

    public static HexCoord getDir(int corner){
        switch ( corner){
            case 0: return new HexCoord(+1, 0);
            case 1: return new HexCoord(+1, -1);
            case 2: return new HexCoord(0, -1);
            case 3: return new HexCoord(-1, 0);
            case 4: return new HexCoord(-1, +1);
            case 5: return new HexCoord(0, +1);
            default: return new HexCoord(0, +1);
        }
    }

    public static ArrayList<HexCoord> hexRing(HexCoord center, int radius){
        ArrayList<HexCoord> results = new ArrayList<>();
        // this code doesn't work for radius == 0; can you see why?
        HexCoord hex = center.add(HexCoord.getDir(4).mult(radius));
        for(int i = 0; i<6;i++){
            for(int j = 0; j< radius; j++){
                results.add(hex);
                hex = hex.add(HexCoord.getDir(i));
            }
        }
        return results;
    }
}