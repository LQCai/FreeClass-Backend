package cn.starchild.common.config;

import cn.starchild.user.service.AttendanceService;
import cn.starchild.user.service.HomeworkService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TimerTask {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private HomeworkService homeworkService;

    private Logger logger = Logger.getLogger(this.getClass());


    /**
     * 监测考勤任务(每隔30秒)
     * 在考勤发布后10分钟后将考勤改为结束状态
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void listenAttendance(){
        boolean stopStartingAttendanceResult = attendanceService.stopStartingAttendanceResult();
        if (stopStartingAttendanceResult) {
            logger.info("停止考勤成功");
        }
    }

    /**
     * 10分钟执行一次监测是否有需要有需要发送的作业附件
     */
    @Scheduled(cron = "0/600 * * * * ?")
    public void sendEmail() {
        try {
            homeworkService.sendEmail();
        } catch (Exception e) {
            logger.error("发送邮件异常" + e.getMessage());
        }
    }
}
