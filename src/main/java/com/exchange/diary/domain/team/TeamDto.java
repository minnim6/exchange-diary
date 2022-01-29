package com.exchange.diary.domain.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class TeamDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RequestCreateTeam{
        String teamName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RequestJoinTeam{
        Long teamId;
    }

    @AllArgsConstructor
    @Getter
    public static class ResponseTeamInfo{
        Long teamId;
        String teamName;
        String teamAdminName;
        List<String> memberList;
    }

}
