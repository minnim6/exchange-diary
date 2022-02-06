package com.exchange.diary.application;

import com.exchange.diary.domain.team.TeamDto;
import com.exchange.diary.domain.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public TeamDto.ResponseLink createTeam(@RequestBody TeamDto.RequestCreateTeam requestCreateTeam){
        return teamService.createTeam(requestCreateTeam);
    }

    @DeleteMapping
    public void deleteTeam(@RequestBody TeamDto.RequestTeamId requestTeamId){
        teamService.deleteTeam(requestTeamId);
    }

    @GetMapping
    public TeamDto.ResponseInfo getTeamInfo(@RequestBody TeamDto.RequestTeam requestTeam){
        return teamService.getTeamInfo(requestTeam);
    }
}
