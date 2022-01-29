package com.exchange.diary.domain.team;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class TeamDto {

    public static class RequestCreateTeam{
        String teamName;
    }

    public static class RequestJoinTeam{
        Long teamId;
    }

    public static class ResponseTeamInfo{
        Long teamId;
        String teamName;
        String teamAdminName;
        List<String> memberList;
    }

}
