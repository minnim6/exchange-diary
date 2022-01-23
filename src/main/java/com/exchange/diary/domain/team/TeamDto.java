package com.exchange.diary.domain.team;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TeamDto {

    public static class RequestCreateTeam{
        String teamName;
    }

}
