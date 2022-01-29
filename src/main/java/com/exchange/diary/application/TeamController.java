package com.exchange.diary.application;

import com.exchange.diary.domain.team.TeamDto;
import com.exchange.diary.domain.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public Long createTeam(TeamDto.RequestCreateTeam requestCreateTeam){
        return teamService.createTeam(requestCreateTeam);
    }
}
