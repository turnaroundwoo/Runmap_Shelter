package project.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.user.dto.MemberDTO;
import project.user.service.MemberService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }
    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "login"; //회원가입이 완료되면 로그인 페이지 리턴
    }

    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }


    // 세션 정보 이용(HttpSession session) : 해당 웹사이트에서 로그인 정보를 계속 유지하는 것
    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            // 로그인한 유저의 이메일 정보를 세션에 담아준다 => main.html ${session.loginEmail} 세션에 담겨있는 파라미터 값 사용
            return "main";
        } else {
            // login 실패
            return "login";
        }
    }

    @GetMapping("/member/")
    public String findAll(Model model) {
        // List<> : 여러개의 데이터를 받아올 때 많이 사용 (다양한 데이터를 같이 가져가야 하는 경우에는 map)
        List<MemberDTO> memberDTOList = memberService.findAll(); // 서비스 클래스에서 findAll 만듦
        // 어떠한 html로 가져갈 데이터가 있다면 model 객체 사용
        model.addAttribute("memberList", memberDTOList); // MemberService의 54번 줄
        return "list";
    }

    @GetMapping("/member/{id}") //{id} : 일종의 표현식으로, 해당 경로의 값을 취하겠다는 의미
    public String findById(@PathVariable Long id, Model model) { //@PathVariable : {id} 경로상의 값을 받아오는 애노테이션
        MemberDTO memberDTO = memberService.findById(id); // 받아올 데이터가 하나이기 때문에 DTO 타입으로 받아줌
        model.addAttribute("member", memberDTO); // 조회 결과를 model에 담고 리턴
        return "detail";
    }

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail"); // (String): 캐스팅 = 강제 형 변환
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }

    @GetMapping("/member/delete/{id}") //응용을 한다면, 관리자:강제탈퇴, 회원:회원탈퇴
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션을 무효화한다
        return "index"; // 무효화 후, 메인화면으로 이동
    }

    @PostMapping("/member/email-check") //save.html
    //ajax를 사용할 때는 @ResponseBody 필수!
    //@RequestParam : ModelAttribute의 방식과 비슷함
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail); //파라미터가 제대로 오는지 체크
        String checkResult = memberService.emailCheck(memberEmail); //emailCheck 메서드 정의
        return checkResult;
//        if (checkResult != null) {
//            return "ok";
//        } else {
//            return "no";
//        }
    }


}
