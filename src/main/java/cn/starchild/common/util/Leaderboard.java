package cn.starchild.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Leaderboard {
    public static void main(String[] args) {
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        try {
            date = dateFormat.parse("2019年05月07日 20时59分20秒");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 1; i <= 10000; i++) {
                // 时间
                date.setTime(date.getTime() + 1000);
                // ip
                Random random = new Random();
                int ipHead = Math.abs(random.nextInt() % 255);
                if (ipHead < 100) {
                    ipHead += 100;
                }
                int ipFoot1 = Math.abs(random.nextInt() % 255);
                int ipFoot2 = Math.abs(random.nextInt() % 255);
                int ipFoot3 = Math.abs(random.nextInt() % 255);

                String ip = ipHead + "." + ipFoot1 + "." + ipFoot2 + "." + ipFoot3;

                FileWriter fileWriter = null;
                File file = new File("C://Users/LQCai/Desktop/board.txt");
                fileWriter = new FileWriter(file, true);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println(i
                        + "--------"
                        + "[" + dateFormat.format(date) + "]"
                        + "--------"
                        + "\"给一个很爱的人\"...\"赵磊\"...｛\"flag\": 100,\" msg \" : \"打榜成功，｝"
                        + "--------" + "[" + ip + "]"
                );
                printWriter.flush();
                fileWriter.flush();
                printWriter.close();
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
