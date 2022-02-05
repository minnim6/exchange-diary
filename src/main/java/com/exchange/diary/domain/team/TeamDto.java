package com.exchange.diary.domain.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public static class RequestTeam{
        Long teamId;
        //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) -> json to date 에러시 사용 , LocalDateTime
        LocalDate teamDate;

        public void setTeamDate(){
            this.teamDate = LocalDate.now();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RequestTeamId{
        Long teamId;
    }

    @AllArgsConstructor
    @Getter
    public static class ResponseLink{
       String teamLink;

       public ResponseLink(Team team){
           this.teamLink = team.getTeamLink();
       }
    }

    @AllArgsConstructor
    @Getter
    public static class ResponseInfo{

        public enum CheckWroteMy{
            Y,N
        }

        int members;
        int wroteMembers;
        List<String> todayMemberList;
        CheckWroteMy checkWroteMy;

        public void setTodayMemberList(List<String> memberList){
            this.wroteMembers = memberList.size();
            this.todayMemberList = memberList;
        }

        public ResponseInfo(Team team){
            this.members = team.getMemberList().size();
            this.checkWroteMy = CheckWroteMy.N;
        }

        public void changeCheckWroteMy() {
            this.checkWroteMy = CheckWroteMy.Y;
        }

    }


}
