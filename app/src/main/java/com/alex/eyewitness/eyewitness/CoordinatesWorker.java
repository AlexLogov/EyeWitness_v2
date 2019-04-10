package com.alex.eyewitness.eyewitness;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Alex on 13.03.2018.
 */

public class CoordinatesWorker {

    public static double genMinDistance(
            Double pLng,
            Double pLat,
            ArrayList <Coordinates> pClist
    ) {
        double fMin = Double.MAX_VALUE;
        double fnow = Double.MAX_VALUE;
        for (int i = 1; i < pClist.size(); i++) {
            Coordinates vPrevC = pClist.get(i - 1);
            Coordinates vNowC = pClist.get(i);
            fnow = getLengthFromLine(pLng, pLat, vPrevC.getLng(), vPrevC.getLat(), vNowC.getLng(), vNowC.getLat());
            if (!Double.isNaN(fnow)) {
                fMin = fMin < fnow ? fMin : fnow;
            }
        }
        return fMin;
    }

    private static double getLengthFromLine(Double x0, Double y0, Double x1, Double y1, Double x2, Double y2) {
        double a, b, c, s, h, d;
        //{квадраты боковых сторон треугольника}
        a = Math.pow((x0 - x1), 2) + Math.pow((y0 - y1), 2);
        b = Math.pow((x0 - x2), 2) + Math.pow((y0 - y2), 2);
        //{квадрат основания}
        c = Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2);
        //{площадь треугольника}
        s = Math.abs((x0 - x2) * (y1 - y2) - (x1 - x2) * (y0 - y2)) / 2;
        //{высота к основанию}
        h = 2 * s / Math.sqrt(c); //{и его высоту}
        //{если треугольник тупоугольный, найдем до какого конца расстояние меньше}
        if ((a + c < b) || (b + c < a)) {
            if (a < b) {
                d = Math.sqrt(a);
            } else {
                d = Math.sqrt(b);
            }
        } else {
            d = h; //{если не тупоугольный, расстояние=высоте}
        }
        return d;


    }



    public static Coordinates getMediumCoordinates(Context pCont){

        ArrayList<Coordinates> pC =  DBHelper.getInstance(pCont).getLastCoords(100);
        Double pLat = 0.0;
        Double pLng = 0.0;
        for (Coordinates fC : pC){
            pLat = pLat + fC.getLat();
            pLng = pLng + fC.getLng();
        }
        pLat = pLat / pC.size();
        pLng = pLng / pC.size();

        Coordinates fNewC= new Coordinates();
        fNewC.setLat(pLat);
        fNewC.setLng(pLng);
        return fNewC;
    }

}
