package com.exchange.diary.domain.team;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public void joinTeam(){

    }

    public void createTeam(TeamDto.RequestCreateTeam requestCreateTeam){

    }

    public void deleteTeam(){

    }

}
