package com.example.mnnu.utils;

import com.example.mnnu.enums.ProblemTypeEnum;
import com.example.mnnu.pojo.Problem;
import com.example.mnnu.pojo.Score;
import com.example.mnnu.vo.ScoreVO;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MathUtil {

    @Synchronized
    public static Long getOrderNo(){
        int random = (int) (Math.random()*8999 +1000);
        return Long.valueOf(TimeUtil.getTime() + random);
    }

    public static List getRandomList(List proIdList, Integer number, ProblemTypeEnum problemTypeEnum) {
        List randomList = new ArrayList(number);

        int n = proIdList.size();
        if (n < number) {
            throw new RuntimeException(problemTypeEnum.getDesc()+" 题库数目不够");
        }

        for (int i=0; i<number; i++) {
            int r = (int) (Math.random()*n);
            randomList.add(proIdList.get(r));
            proIdList.set(r, proIdList.get(n-1));
            n--;
        }
        return randomList;
    }

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        if (a.compareTo(b) < 0)  //表示a小于b
            return b;
        return a;
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        if (a.compareTo(b) < 0)  //表示a小于b
            return a;
        return b;
    }

    public static Integer to(BigDecimal b){
        int i = b.intValue();
        if (i == 100)
            return 9;
        return i/10;
    }

    public static Integer findStringIn(List list, String id) {
        for (int i=0; i<list.size(); i++) {
            if (id.equals(list.get(i))) {
                return i;
            }
        }
        throw new RuntimeException("该数据不在字段里");
    }
}
