package cqu.jsjds.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Function {

    public static Object[] splitListIntoArrays(List<Map<String, Integer>> list) {
        System.out.println("分割List<Map>:" + list);
        int size = list.size();

        // 创建两个数组
        String[] keys = new String[size];
        int[] counts = new int[size];

        // 填充数组
        for (int i = 0; i < size; i++) {
            Map<String, Integer> map = list.get(i);
            keys[i] = new ArrayList<>(map.keySet()).get(0);
            counts[i] = map.get(keys[i]);
        }

        return new Object[]{keys, counts};
    }
}
