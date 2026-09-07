package com.sms.dto.attendance;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceSessionDetailsResponseDto {
    private AttendanceSessionResponseDto session;
    private List<AttendanceResponseDto> attendance;
}
