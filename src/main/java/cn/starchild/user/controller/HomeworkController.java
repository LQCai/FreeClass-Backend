package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.util.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RequestMapping("/user/homework")
@RestController
public class HomeworkController {

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResData postJob(@RequestParam("annex") MultipartFile annex,
                           @NotNull String teacherId,
                           @NotNull String homeworkName,
                           @NotNull String classId,
                           @NotNull String homeworkIntroduction,
                           @NotNull String sendByEmail,
                           @NotNull String fullScore,
                           @NotNull Date deadline) throws IOException {


        // 定义一下文件命名
        String annexUrl = FileUtils.saveFile(annex, 0, "test");

        return ResData.ok(annexUrl);
    }
}
