package kong.qingwei.kqwfacedetectiondemo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by qihang on 15/12/2017.
 */

public class CoalitionUtil {

    public static String findMax(Map<String, Double> map) {
        double maxValue = 0;
        String key = "";
        Iterator ite = map.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry entry = (Map.Entry) ite.next();
            double value = (double) entry.getValue();
            if (value > maxValue) {
                maxValue = value;
                key = (String) entry.getKey();
            }
        }
        return key;

    }
}
