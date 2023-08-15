package project.user.dto;

import lombok.*;
import project.user.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성 애노테이션
@AllArgsConstructor //모든 필드의 매개변수 생성자 자동 생성 애노테이션
@ToString
public class MemberDTO {
    //필드 선언
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;

    // dto > entity 변환 처리
    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        return memberDTO;
    }
}
