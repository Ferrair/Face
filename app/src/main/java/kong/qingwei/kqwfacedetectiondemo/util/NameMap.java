package kong.qingwei.kqwfacedetectiondemo.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qihang on 21/12/2017.
 */

public class NameMap {

    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("620d448bbed00f7979cebeaf75931145", "胡歌照片-1");
        map.put("708f4f3b73c4e2137b5dc79c88dfd591", "胡歌照片-2");
        map.put("de9a098d048f11c7bb580e144ea2ac8d", "胡歌照片-3");
        map.put("32670c78728257947f9108ca27ca57ee", "胡歌照片-4");
        map.put("05d2bd16aec703d2b97cbe4cc79788d5", "胡歌照片-5");

        map.put("6fae65dd778ec3b527f71ff0576bca47", "彭于晏照片-1");
        map.put("38665a738411d36378c51d6e977bd806", "彭于晏照片-2");
        map.put("ef014eecfd99bce3cfaa4ee77aabe05a", "彭于晏照片-3");
        map.put("a42c32ff7496f7038e56ef59e910f5a7", "彭于晏照片-4");


        map.put("f51bb14bbccc7bc2a84dad55dd63bafe", "莱昂纳多-1");
        map.put("eb02b0e52799f4ecab12487d899e162e", "莱昂纳多-2");
        map.put("50be87b2d62162b4985e81948d519140", "莱昂纳多-3");
        map.put("b7bc6344b81349b12f44d0bb8a14668f", "莱昂纳多-4");
        map.put("325436a014da805d568ea537e4d3916e", "莱昂纳多-5");

    }

    public static String getName(String serialID) {
        return map.get(serialID);
    }
}
