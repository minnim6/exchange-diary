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
    public static class RequestTeamId{
        Long teamId;
    }

    @AllArgsConstructor
    @Getter
    public static class ResponseJoin{
        Long teamId;
        String teamName;
        String teamAdminName;

        public ResponseJoin(Team team){
            this.teamId = team.getTeamId();
            this.teamName = team.getTeamName();
            this.teamAdminName = team.getMemberAdmin().getMemberNickname();
        }
    }

    @AllArgsConstructor
    @Getter
    public static class ResponseCreate{
        Long teamId;
        String teamName;
        String teamAdminName;

        public ResponseCreate(Team team){
            this.teamId = team.getTeamId();
            this.teamName = team.getTeamName();
            this.teamAdminName = team.getMemberAdmin().getMemberNickname();
        }
    }

}
