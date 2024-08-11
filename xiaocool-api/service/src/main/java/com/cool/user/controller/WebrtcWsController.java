/**
 * FileName: WebSocketController
 * Author:   10418
 * Date:     2020-05-16 18:19
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 楠楠(Nannan)
 */
package com.cool.user.controller;


import domain.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DESC〈一句话功能简述〉<br>
 * 〈返回ws url〉
 *
 * @author yys
 * @create 2021-05-16
 * @since 1.0.0
 */
@RestController
@RequestMapping("/WebrtcWs")
public class WebrtcWsController {

//
    @Value("8002")
    private Integer port;

    @GetMapping("/url")
    public Result getIpAddress() {
        //http用ws,https用wss,ws可以用ip访问，wss的话，用ip会报证书错误，需要用域名访问,
//         return new JsonResult("ws:localhost:" + port + "/ws/webrtc");
            return Result.success("wss://xiaoku.store:" + port + "/ws/webrtc");

    }
}
