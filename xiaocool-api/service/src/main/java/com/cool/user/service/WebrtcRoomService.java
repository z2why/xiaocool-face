/**
 * FileName: WebrtcRoomService
 * Author:   10418
 * Date:     2020-05-16 19:40
 * Description: 房间管理
 * History:
 * <author>          <time>          <version>          <desc>
 * 楠楠(Nannan)
 */
package com.cool.user.service;


import com.cool.user.controller.WebrtcWS;
import domain.WebrtcMessage;
import org.springframework.stereotype.Service;



@Service
public interface WebrtcRoomService {
    /**
     * 查询指定房间存在人数
     *
     * @param roomId 房间ID
     * @return 房间人数
     */
    int countOfUserInRoom(String roomId);

    /**
     * 踢出用户
     *
     * @param roomId   房间ID
     * @param userId userId
     * @return 成功true，失败false
     */
    boolean kickUser(String roomId, String userId);

    /**
     * @param roomId   房间ID
     * @param webrtcWS websocket连接
     * @param roomPw   房间密码
     * @return 成功true，失败false
     */
    boolean enterRoom(String roomId, String roomPw, WebrtcWS webrtcWS, String nickname, String userId) throws Exception;

    /**
     * @param roomId   房间ID
     * @param webrtcWS websocket连接
     * @param roomPw   房间密码
     * @return 成功true，失败false
     */
    boolean createRoom(String roomId, String roomPw, WebrtcWS webrtcWS, String nickname,String userId) throws Exception;

    /**
     * @param roomId   房间ID
     * @param userId 用户ID
     * @return 成功true，失败false
     */
    boolean userLeave(String roomId, String userId);

    /**
     * 获取房间内所有人
     *
     * @param roomId 房间id
     * @return 所有人
     */
    String getRoomUsers(String roomId);

    /**
     * 转发消息到房间所有人
     *
     * @param roomId        房间Id
     * @param message       要转发的消息
     * @param excludeUserId 不转发的userId
     * @return 是否成功
     */
    boolean forwardToEveryoneInRoom(String roomId, WebrtcMessage message, String excludeUserId);

    /**
     * 转发消息到指定人
     *
     * @param roomId        房间Id
     * @param message       要转发的消息
     * @param userId 转发的userId
     * @return 是否成功
     */
    boolean forwardToOneInRoom(String roomId, WebrtcMessage message, String userId);
}
