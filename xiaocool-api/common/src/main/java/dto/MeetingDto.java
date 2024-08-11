package dto;

import entity.Meeting;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MeetingDto extends Meeting {

    /**
     * 当前用户对该会议的参加情况
     */
    private int participateStatus;
}
