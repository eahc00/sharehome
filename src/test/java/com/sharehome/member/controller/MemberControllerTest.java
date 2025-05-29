package com.sharehome.member.controller;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.member.service.MemberService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@DisplayName("MemberController 은(는)")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class 회원가입_시 {

        @Test
        void 요청_성공() throws Exception {
            // given
            SignupRequest request = new SignupRequest(
                    "eahc00@naver.com",
                    "영채",
                    LocalDate.of(2002, 10, 9),
                    "Abc123##"
            );

            //when&then
            mockMvc.perform(post("/members")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Nested
        class 이름은 {

            @ParameterizedTest
            @ValueSource(ints = {2, 4, 9, 10, 11, 20})
            void 두글자_이상_20글자_이내여야_한다(int length) throws Exception {
                //given
                String username = "a".repeat(length);

                SignupRequest request = new SignupRequest(
                        "eahc00@naver.com",
                        username,
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(ints = {
                    0, 1, 23, 50, 100
            })
            void 두글자_이상_20글자_이내가_아니면_예외(int length) throws Exception {
                //given
                String username = "a".repeat(length);
                SignupRequest request = new SignupRequest(
                        "eahc00@naver.com",
                        username,
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "하영채", "YoungchaiHa", "chxxry", "홍길동"
            })
            void 영어와_한국어만_입력_가능하다(String username) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "eahc00@naver.com",
                        username,
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "영채Ha", "채리chxxry", "Ha영채"
            })
            void 영어와_한국어를_섞어_쓰면_예외(String username) throws Exception {
                 //given
                SignupRequest request = new SignupRequest(
                        "eahc00@naver.com",
                        username,
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "채리🍒", "영채1009", "123", "chxxry!@"
            })
            void 영어_한국어_외_다른_문자가_들어오면_예외(String username) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "eahc00@naver.com",
                        username,
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "하 영채",
                    "Youngchai Ha",
                    "    ",
                    "",
                    "\n",
                    " abc"
            })
            void 공백이_들어오면_예외(String username) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "eahc00@naver.com",
                        username,
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        class 이메일은 {

            @ParameterizedTest
            @ValueSource(strings = {
                    "chxxry@naver.com", "ycha1009@gmail.com", "c123@o.cnu.ac.kr"
            })
            void 정확히_하나의_at_기호가_포함되어야_한다(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "cr@@123.com", "chxxrynaver.com", "a@b@abc.com"
            })
            void at_기호가_하나가_아니면_예외(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "chxxry@naver.com", "ycha1009@gmail.com", "c123@o.cnu.ac.kr"
            })
            void at_기호로_분리되는_두_개의_텍스트열이_있어야_한다(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "@naver.com", "ycha1009@", "@"
            })
            void at_기호로_분리되는_두_개의_텍스트열이_없으면_예외(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "chxxry@naver.com", "ycha1009@gmail.com", "c123@o.cnu.ac.kr"
            })
            void 도메인_이름에는_마침표가_최소_하나_포함되어야_한다(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "abc@abc", "abc@chxxry", "abc@domain123"
            })
            void 도메인_이름에_마침표가_없으면_예외(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "chxxry@naver.com", "ycha1009@gmail.com", "c123@o.cnu.ac.kr"
            })
            void 도메인_이름에는_마침표로_분리되는_두_개의_텍스트열이_있어야_한다(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "chxxry@naver.", "ycha1009@.com", "c123@."
            })
            void 도메인_이름에는_마침표로_분리되는_두_개의_텍스트열이_없으면_예외(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }


            @ParameterizedTest
            @ValueSource(strings = {
                    "chxxry@naver.com",
                    "eahc00@naver.com",
                    "1234123@gmail.com",
                    "Youngchai.ha@naver.com"
            })
            void 사용자_이름에는_영어_숫자_마침표만_입력_가능하다(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "chxrry🍒@naver.com",
                    "a-bc!@gmail.com",
                    "채리@naver.com",
            })
            void 영어_숫자_마침표_외_문자가_들어오면_예외(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "ha chxxry@naver.com",
                    "  abc@gmail.com",
                    "ha\n@naver.com",
                    "cx@ naver.com"
            })
            void 공백이_들어오면_예외(String email) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        email,
                        "하채리",
                        LocalDate.of(2002, 10, 9),
                        "Abc123##"
                );

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        class 생년월일은 {

            /*
            생년월일은 "yyyyMMdd" 형식으로 입력받는다.
            만 18세 미만은 회원가입 할 수 없다.
             */
            @ParameterizedTest
            @ValueSource(strings = {
              "20021009", "20000907", "19991111"
            })
            void yyyyMMdd_형식으로_입력받는다(String date) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        "Abc123##"
                );

                String content = objectMapper.writeValueAsString(request)
                        .replace("00010101", date);

                //when&then
                mockMvc.perform(post("/members")
                               .contentType(APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "2002-10-09", "2000.09.07", "021009", "2002/10/9"
            })
            void yyyyMMdd_형식이_아니면_예외(String date) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        "Abc123##"
                );

                String content = objectMapper.writeValueAsString(request)
                        .replace("00010101", date);

                //when&then
                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(content))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "20021009", "20000907", "19991111"
            })
            void 만_18세_이상이면_성공(String date) throws Exception {
                //given
                LocalDate birth = LocalDate.parse(date, BASIC_ISO_DATE);
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        birth,
                        "Abc123##"
                );

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "20231009", "20220907", "20081111"
            })
            void 만_18세_미만이면_예외(String date) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        "Abc123##"
                );

                String content = objectMapper.writeValueAsString(request)
                        .replace("00010101", date);

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(content))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        class 비밀번호는 {

            /**
             * 비밀번호는 8글자 이상, 50글자 이내여야 한다.
             * 비밀번호는 영어, 숫자, 특수문자(!@#,.<>?)만 입력 가능하다.
             * 비밀번호는 최소 한 개 이상의 숫자, 소문자, 대문자, 특수문자를 포함해야 한다.
             * 비밀번호는 공백이 들어올 수 없다.
             */
            @ParameterizedTest
            @ValueSource(ints = {8, 10, 25, 30, 50})
            void 여덟_글자_이상_50글자_이내여야_한다(int length) throws Exception {
                //given
                String password = "a".repeat(length-4) + "Aa1#";
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        password
                );

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(ints = {4, 7, 51, 70, 100})
            void 여덟_글자_이상_50글자_이내가_아니면_예외(int length) throws Exception {
                //given
                String password = "a".repeat(length-4) + "Aa1#";
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        password
                );

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "Aabc123@", "123Abc!?", "#123Abcd", "qwer<123A"
            })
            void 영어_숫자_특수문자만_입력_가능하다(String password) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        password
                );

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "Abc123$^", "채리채리1#Aa", "Chxxry123🍒"
            })
            void 영어_숫자_특수문자_외_다른_문자가_들어오면_예외(String password) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        password
                );

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "Aabc123@", "123Abc!?", "#123Abcd", "qwer<123A"
            })
            void 최소_하나의_숫자_대문자_소문자_특수문자를_포함해야_한다(String password) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        password
                );

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "abc123@", "123ABC!?", "#AbcdEfg", "qwer123A"
            })
            void 최소_하나의_숫자_대문자_소문자_특수문자를_포함하지_않으면_예외(String password) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        password
                );

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "Aabc123@", "123Abc!?", "#123Abcd", "qwer<123A"
            })
            void 공백이_들어올_수_없다(String password) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        password
                );

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "Aabc 123@", "123Abc!?\n", " #123Abc", "qwer<12  "
            })
            void 공백이_들어오면_예외(String password) throws Exception {
                //given
                SignupRequest request = new SignupRequest(
                        "chxxry@naver.com",
                        "하채리",
                        LocalDate.of(1, 1, 1),
                        password
                );

                mockMvc.perform(post("/members")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}