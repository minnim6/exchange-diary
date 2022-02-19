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

    @PostMapping //ok
    public TeamDto.ResponseLink createTeam(@RequestBody TeamDto.RequestCreateTeam requestCreateTeam){
        return teamService.createTeam(requestCreateTeam);
    }

    @DeleteMapping // ok
    public void deleteTeam(@RequestBody TeamDto.RequestTeamId requestTeamId){
        teamService.deleteTeam(requestTeamId);
    }

    @GetMapping
    public TeamDto.ResponseInfo getTeamInfo(@RequestBody TeamDto.RequestTeam requestTeam){
        return teamService.getTeamInfo(requestTeam);
    }

    @PatchMapping //ok
    public void updateTeam(@RequestBody TeamDto.RequestUpdateTeam requestUpdateTeam){
        teamService.updateTeamName(requestUpdateTeam);
    }

    @PostMapping("/{teamLink}")
    public void joinTeam(@PathVariable(name = "teamLink") String teamUrl){
        teamService.joinTeam(teamUrl);
    }

}
